package com.david.foro_hub.domain.respuesta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosRegistroRespuesta(
    @NotBlank String mensaje,
    @NotNull Long topicoId,
    @NotNull Long autorId,
    @NotNull Boolean solucion
) {
    
}