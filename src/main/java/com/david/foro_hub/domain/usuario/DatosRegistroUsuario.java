package com.david.foro_hub.domain.usuario;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DatosRegistroUsuario(
    @NotBlank String nombre,
    @NotBlank @Email String correoElectronico,
    @NotBlank String contrasena,
    Set<Long> perfilesIds
) { }
