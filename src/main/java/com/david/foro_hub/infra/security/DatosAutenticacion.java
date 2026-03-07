package com.david.foro_hub.infra.security;

public record DatosAutenticacion(
    String correoElectronico,
    String contrasena
) { }