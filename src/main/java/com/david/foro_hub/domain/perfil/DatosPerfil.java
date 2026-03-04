package com.david.foro_hub.domain.perfil;

public record DatosPerfil(
    Long id,
    NombrePerfil nombre
) {
    public DatosPerfil(Perfil perfil) {
        this(
            perfil.getId(),
            perfil.getNombre()
        );
    }
}