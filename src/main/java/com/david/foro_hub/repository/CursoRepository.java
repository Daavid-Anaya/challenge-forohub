package com.david.foro_hub.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.david.foro_hub.domain.curso.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    
    boolean existsByNombreAndCategoria(String nombre, String categoria);
    boolean existsByNombreAndCategoriaAndIdNot(String nombre, String categoria, Long id);
    Page<Curso> findByActivoTrue(Pageable pageable);
}