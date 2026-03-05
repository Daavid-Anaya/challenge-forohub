package com.david.foro_hub.domain.topicos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosRegistroTopico(
    @NotBlank String titulo,
    @NotBlank String mensaje,
    @NotNull StatusTopicos status,
    @NotNull Long autorId,
    @NotNull Long cursoId
) { }