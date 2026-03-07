package com.david.foro_hub.controller;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.david.foro_hub.domain.usuario.DatosActualizarUsuario;
import com.david.foro_hub.domain.usuario.DatosRegistroUsuario;
import com.david.foro_hub.domain.usuario.DatosUsuario;
import com.david.foro_hub.domain.usuario.Usuario;
import com.david.foro_hub.repository.PerfilRepository;
import com.david.foro_hub.repository.UsuarioRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @PostMapping
    public ResponseEntity<DatosUsuario> registrar(@RequestBody @Valid DatosRegistroUsuario datos, UriComponentsBuilder uriComponentBuilder) {
        // Verificar correo duplicado
        if (usuarioRepository.existsByCorreoElectronico(datos.correoElectronico())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
        }

        // Buscar perfiles por IDs
        var perfiles = perfilRepository.findAllById(datos.perfilesIds());
        if (perfiles.size() != datos.perfilesIds().size()) {
            return ResponseEntity.badRequest().build(); // 400
        }

        var contrasenaEncriptada = passwordEncoder.encode(datos.contrasena());

        var usuario = new Usuario(datos, new HashSet<>(perfiles), contrasenaEncriptada);
        usuarioRepository.save(usuario);

        var uri = uriComponentBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();

        return ResponseEntity.created(uri).body(new DatosUsuario(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosUsuario> detallar(@PathVariable Long id) {
        return usuarioRepository.findById(id)
            .map(usuario -> ResponseEntity.ok(new DatosUsuario(usuario)))
            .orElse(ResponseEntity.notFound().build()); // 404
    }

    @GetMapping
    public ResponseEntity<Page<DatosUsuario>> listar(@PageableDefault(size = 10, sort = "nombre") Pageable paginacion) {
        var page = usuarioRepository.findByActivoTrue(paginacion).map(DatosUsuario::new);

        return ResponseEntity.ok(page);
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<DatosUsuario> actualizar(@PathVariable Long id, @RequestBody @Valid DatosActualizarUsuario datos) {
            var usuario = usuarioRepository.findById(id);

            if (usuario.isEmpty()) {
                return ResponseEntity.notFound().build(); // 404
            }

            if (datos.correoElectronico() != null &&
                    usuarioRepository.existsByCorreoElectronicoAndIdNot(datos.correoElectronico(), id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
            }

            if (datos.contrasena() != null && !datos.contrasena().isBlank()) {
                datos = new DatosActualizarUsuario(
                    datos.nombre(),
                    datos.correoElectronico(),
                    passwordEncoder.encode(datos.contrasena())
                );
            }

            var u = usuario.get();
            u.actualizarInformacion(datos);
            return ResponseEntity.ok(new DatosUsuario(u)); // 200
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return usuarioRepository.findById(id)
            .map(usuario -> {
                usuario.desactivar();
                return ResponseEntity.noContent().<Void>build(); // 204
            })
            .orElse(ResponseEntity.notFound().build()); // 404
    }
}