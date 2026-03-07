package com.david.foro_hub.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.david.foro_hub.domain.topicos.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    boolean existsByTituloAndMensaje(String titulo, String mensaje);
    boolean existsByTituloAndMensajeAndIdNot(String titulo, String mensaje, Long id);
    Page<Topico> findByActivoTrue(Pageable pageable);
}