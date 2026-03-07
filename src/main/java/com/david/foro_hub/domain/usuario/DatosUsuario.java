package com.david.foro_hub.domain.usuario;

import java.util.Set;
import java.util.stream.Collectors;

import com.david.foro_hub.domain.perfil.NombrePerfil;
import com.david.foro_hub.domain.perfil.Perfil;

public record DatosUsuario(
    Long id,
    String nombre,
    String correoElectronico,
    Set<NombrePerfil> perfiles
) { 
    public DatosUsuario(Usuario usuario) {
        this(usuario.getId(),
        usuario.getNombre(),
        usuario.getCorreoElectronico(),
        usuario.getPerfiles().stream()
            .map(Perfil::getRol)
            .collect(Collectors.toSet())
        );
    }
}