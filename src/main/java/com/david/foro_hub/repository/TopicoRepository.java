package com.david.foro_hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.david.foro_hub.domain.topicos.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    
}