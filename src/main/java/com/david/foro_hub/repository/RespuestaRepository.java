package com.david.foro_hub.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.david.foro_hub.domain.respuesta.Respuesta;

public interface RespuestaRepository extends JpaRepository<Respuesta, Long>{
    Page<Respuesta> findByActivoTrue(Pageable pageable);
}