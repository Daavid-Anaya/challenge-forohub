package com.david.foro_hub.domain.topicos;

import java.time.LocalDateTime;

public record DatosTopico(
    Long id,
    String titulo,
    String mensaje,
    LocalDateTime fechaCreacion,
    StatusTopicos status,
    String autor,
    String curso
) {
    public DatosTopico(Topico topico) {
        this(
            topico.getId(),
            topico.getTitulo(),
            topico.getMensaje(),
            topico.getFechaCreacion(),
            topico.getStatus(),
            topico.getAutor().getNombre(),
            topico.getCurso().getNombre()
        );
    }
}