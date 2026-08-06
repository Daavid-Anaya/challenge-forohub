package com.david.foro_hub.domain.usuario;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;

class UsuarioTest {

    @Test
    void isEnabledReturnsFalseWhenUserIsDeactivated() {
        var usuario = new Usuario(
                new DatosRegistroUsuario("Test User", "user@example.com", "secret123", null),
                Set.of(),
                "encoded-password");

        usuario.desactivar();

        assertThat(usuario.isEnabled()).isFalse();
    }
}
