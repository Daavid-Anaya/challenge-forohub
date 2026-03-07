package com.david.foro_hub.domain.perfil;

import jakarta.validation.constraints.NotNull;

public record DatosRegistroActualizacionPerfil(
    @NotNull NombrePerfil rol
) { }