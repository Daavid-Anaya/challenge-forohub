package com.david.foro_hub.domain.respuesta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosActualizarRespuesta(
    @NotBlank String mensaje,
    @NotNull Boolean solucion
) { }