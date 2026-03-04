package com.david.foro_hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.david.foro_hub.domain.respuesta.Respuesta;

public interface RespuestaRepository extends JpaRepository<Respuesta, Long>{
    
}