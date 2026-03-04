package com.david.foro_hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.david.foro_hub.domain.usuario.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
}