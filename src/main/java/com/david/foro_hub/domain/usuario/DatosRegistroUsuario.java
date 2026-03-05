package com.david.foro_hub.domain.usuario;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record DatosRegistroUsuario(
    @NotBlank String nombre,
    @NotBlank @Email String correoElectronico,
    @NotBlank String contrasena,
    @NotNull @NotEmpty Set<Long> perfilesIds

) { }