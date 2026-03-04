package com.david.foro_hub.domain.curso;

public record DatosCurso(
    Long id,
    String nombre,
    String categoria
) {
    public DatosCurso(Curso curso) {
        this(
            curso.getId(),
            curso.getNombre(),
            curso.getCategoria()
        );
    }
}