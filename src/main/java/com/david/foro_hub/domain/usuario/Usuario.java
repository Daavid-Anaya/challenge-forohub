package com.david.foro_hub.domain.usuario;

import java.util.HashSet;
import java.util.Set;

import com.david.foro_hub.domain.perfil.Perfil;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "usuarios")
@Entity(name = "Usuario")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String correoElectronico;
    private String contrasena;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "usuario_perfil",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "perfil_id")
    )
    private Set<Perfil> perfiles = new HashSet<>();

    public Usuario(DatosRegistroUsuario datos, Set<Perfil> perfiles) {
        this.nombre = datos.nombre();
        this.correoElectronico = datos.correoElectronico();
        this.contrasena = datos.contrasena();
        this.perfiles = perfiles;
    }

    public void actualizarInformacion(DatosActualizarUsuario datos) {
        if (datos.nombre() != null && !datos.nombre().isBlank()) {
            this.nombre = datos.nombre().trim();
        }
        if (datos.correoElectronico() != null && !datos.correoElectronico().isBlank()) {
            this.correoElectronico = datos.correoElectronico().trim();
        }
        if (datos.contrasena() != null && !datos.contrasena().isBlank()) {
            // this.contrasena = new BCryptPasswordEncoder().encode(datos.contrasena());
            this.contrasena = datos.contrasena().trim();
        }
    }
}