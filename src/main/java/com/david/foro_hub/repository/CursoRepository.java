package com.david.foro_hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.david.foro_hub.domain.curso.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    
}