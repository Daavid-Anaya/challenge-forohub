package com.david.foro_hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.david.foro_hub.domain.usuario.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByCorreoElectronico(String correoElectronico);
    boolean existsByCorreoElectronicoAndIdNot(String correoElectronico, Long id);
    UserDetails findByCorreoElectronico(String correoElectronico);
}