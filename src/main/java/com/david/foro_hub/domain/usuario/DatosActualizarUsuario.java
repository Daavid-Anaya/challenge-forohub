package com.david.foro_hub.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record DatosActualizarUsuario(
    @Size(min = 1, message = "El nombre no puede estar vacío")
    String nombre,

    @Email(message = "El correo no tiene un formato válido")
    @Size(min = 1, message = "El correo no puede estar vacío")
    String correoElectronico,

    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    String contrasena
) { }