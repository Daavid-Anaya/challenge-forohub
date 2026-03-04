package com.david.foro_hub.domain.curso;

import jakarta.validation.constraints.Size;

public record DatosActualizarCurso(
    @Size(min = 1, message = "El nombre no puede estar vacío")
    String nombre,

    @Size(min = 1, message = "La categoría no puede estar vacía")
    String categoria
) { }