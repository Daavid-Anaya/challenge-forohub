package com.david.foro_hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.david.foro_hub.domain.perfil.NombrePerfil;
import com.david.foro_hub.domain.perfil.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    boolean existsByNombre(NombrePerfil nombre);
    boolean existsByNombreAndIdNot(NombrePerfil nombre, Long id);
}