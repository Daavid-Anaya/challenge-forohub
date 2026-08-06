package com.david.foro_hub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.david.foro_hub.domain.perfil.NombrePerfil;
import com.david.foro_hub.domain.perfil.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    Optional<Perfil> findByRol(NombrePerfil rol);
    boolean existsByRol(NombrePerfil rol);
    boolean existsByRolAndIdNot(NombrePerfil rol, Long id);
}
