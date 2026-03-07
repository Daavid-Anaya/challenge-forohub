package com.david.foro_hub.domain.perfil;

public record DatosPerfil(
    Long id,
    NombrePerfil rol
) {
    public DatosPerfil(Perfil perfil) {
        this(
            perfil.getId(),
            perfil.getRol()
        );
    }
}