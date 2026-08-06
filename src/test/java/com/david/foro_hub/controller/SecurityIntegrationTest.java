package com.david.foro_hub.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.david.foro_hub.domain.curso.Curso;
import com.david.foro_hub.domain.curso.DatosRegistroCurso;
import com.david.foro_hub.domain.perfil.NombrePerfil;
import com.david.foro_hub.domain.perfil.Perfil;
import com.david.foro_hub.domain.respuesta.DatosRegistroRespuesta;
import com.david.foro_hub.domain.respuesta.Respuesta;
import com.david.foro_hub.domain.topicos.DatosRegistroTopico;
import com.david.foro_hub.domain.topicos.StatusTopicos;
import com.david.foro_hub.domain.topicos.Topico;
import com.david.foro_hub.domain.usuario.DatosRegistroUsuario;
import com.david.foro_hub.domain.usuario.Usuario;
import com.david.foro_hub.infra.security.TokenService;
import com.david.foro_hub.repository.CursoRepository;
import com.david.foro_hub.repository.PerfilRepository;
import com.david.foro_hub.repository.RespuestaRepository;
import com.david.foro_hub.repository.TopicoRepository;
import com.david.foro_hub.repository.UsuarioRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Test
    void publicRegistrationIgnoresSubmittedProfilesAndSavesOnlyUserRole() throws Exception {
        var email = "user-%s@example.com".formatted(UUID.randomUUID());

        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "nombre": "Test User",
                          "correoElectronico": "%s",
                          "contrasena": "secret123",
                          "perfilesIds": [1, 3]
                        }
                        """.formatted(email)))
                .andExpect(status().isCreated());

        var savedUser = usuarioRepository.findAll().stream()
                .filter(usuario -> email.equals(usuario.getCorreoElectronico()))
                .findFirst()
                .orElseThrow();

        assertThat(savedUser.getPerfiles())
                .extracting(Perfil::getRol)
                .containsExactly(NombrePerfil.ROLE_USER);
    }

    @Test
    void invalidTokenOnProtectedEndpointReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/usuarios")
                .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginRejectsEmptyPayload() throws Exception {
        mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginRejectsInvalidEmail() throws Exception {
        mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "correoElectronico": "not-an-email",
                          "contrasena": "secret123"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void userCannotUpdateAnotherUser() throws Exception {
        var user = saveUser(NombrePerfil.ROLE_USER);
        var otherUser = saveUser(NombrePerfil.ROLE_USER);

        mockMvc.perform(put("/usuarios/{id}", otherUser.getId())
                .header("Authorization", bearerToken(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "nombre": "Forbidden Update"
                        }
                        """))
                .andExpect(status().isForbidden());
    }

    @Test
    void userCanUpdateOwnUser() throws Exception {
        var user = saveUser(NombrePerfil.ROLE_USER);

        mockMvc.perform(put("/usuarios/{id}", user.getId())
                .header("Authorization", bearerToken(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "nombre": "Own Update"
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    void adminCanUpdateAnotherUser() throws Exception {
        var admin = saveUser(NombrePerfil.ROLE_ADMIN);
        var otherUser = saveUser(NombrePerfil.ROLE_USER);

        mockMvc.perform(put("/usuarios/{id}", otherUser.getId())
                .header("Authorization", bearerToken(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "nombre": "Admin Update"
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    void userCannotCreateTopicForAnotherAuthor() throws Exception {
        var user = saveUser(NombrePerfil.ROLE_USER);
        var otherUser = saveUser(NombrePerfil.ROLE_USER);

        mockMvc.perform(post("/topicos")
                .header("Authorization", bearerToken(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "titulo": "Ownership topic %s",
                          "mensaje": "This topic should be forbidden",
                          "status": "ABIERTO",
                          "autorId": %d,
                          "cursoId": 999
                        }
                        """.formatted(UUID.randomUUID(), otherUser.getId())))
                .andExpect(status().isForbidden());
    }

    @Test
    void moderatorCanCreateTopicForAnotherAuthor() throws Exception {
        var moderator = saveUser(NombrePerfil.ROLE_MODERADOR);
        var otherUser = saveUser(NombrePerfil.ROLE_USER);
        var course = cursoRepository.saveAndFlush(new Curso(new DatosRegistroCurso(
                "Course %s".formatted(UUID.randomUUID()), "Backend")));
        var title = "Moderated topic %s".formatted(UUID.randomUUID());

        mockMvc.perform(post("/topicos")
                .header("Authorization", bearerToken(moderator))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "titulo": "%s",
                          "mensaje": "Moderator creates for another user",
                          "status": "ABIERTO",
                          "autorId": %d,
                          "cursoId": %d
                        }
                        """.formatted(title, otherUser.getId(), course.getId())))
                .andExpect(status().isCreated());

        var savedTopic = topicoRepository.findAll().stream()
                .filter(topic -> title.equals(topic.getTitulo()))
                .findFirst()
                .orElseThrow();

        assertThat(savedTopic.getAutor().getId()).isEqualTo(otherUser.getId());
    }

    @Test
    void userCannotUpdateAnotherUsersAnswer() throws Exception {
        var user = saveUser(NombrePerfil.ROLE_USER);
        var otherUser = saveUser(NombrePerfil.ROLE_USER);
        var course = cursoRepository.saveAndFlush(new Curso(new DatosRegistroCurso(
                "Course %s".formatted(UUID.randomUUID()), "Backend")));
        var topic = topicoRepository.saveAndFlush(new Topico(new DatosRegistroTopico(
                "Topic %s".formatted(UUID.randomUUID()), "Topic message", StatusTopicos.ABIERTO,
                otherUser.getId(), course.getId()), otherUser, course));
        var answer = respuestaRepository.saveAndFlush(new Respuesta(new DatosRegistroRespuesta(
                "Answer message", topic.getId(), otherUser.getId(), false), topic, otherUser));

        mockMvc.perform(put("/respuestas/{id}", answer.getId())
                .header("Authorization", bearerToken(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "mensaje": "Forbidden answer update",
                          "solucion": true
                        }
                        """))
                .andExpect(status().isForbidden());
    }

    @Test
    void moderatorCanUpdateAnotherUsersAnswer() throws Exception {
        var moderator = saveUser(NombrePerfil.ROLE_MODERADOR);
        var otherUser = saveUser(NombrePerfil.ROLE_USER);
        var course = cursoRepository.saveAndFlush(new Curso(new DatosRegistroCurso(
                "Course %s".formatted(UUID.randomUUID()), "Backend")));
        var topic = topicoRepository.saveAndFlush(new Topico(new DatosRegistroTopico(
                "Topic %s".formatted(UUID.randomUUID()), "Topic message", StatusTopicos.ABIERTO,
                otherUser.getId(), course.getId()), otherUser, course));
        var answer = respuestaRepository.saveAndFlush(new Respuesta(new DatosRegistroRespuesta(
                "Answer message", topic.getId(), otherUser.getId(), false), topic, otherUser));

        mockMvc.perform(put("/respuestas/{id}", answer.getId())
                .header("Authorization", bearerToken(moderator))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "mensaje": "Moderator answer update",
                          "solucion": true
                        }
                        """))
                .andExpect(status().isOk());
    }

    private Usuario saveUser(NombrePerfil role) {
        var perfil = perfilRepository.findByRol(role).orElseThrow();
        var email = "user-%s@example.com".formatted(UUID.randomUUID());
        var datos = new DatosRegistroUsuario("Test User", email, "secret123", Set.of(perfil.getId()));
        var usuario = new Usuario(datos, new HashSet<>(Set.of(perfil)), passwordEncoder.encode(datos.contrasena()));

        return usuarioRepository.saveAndFlush(usuario);
    }

    private String bearerToken(Usuario usuario) {
        return "Bearer " + tokenService.generarToken(usuario);
    }
}
