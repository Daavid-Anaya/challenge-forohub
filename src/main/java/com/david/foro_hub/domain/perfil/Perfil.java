package com.david.foro_hub.domain.perfil;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "perfiles", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nombre"})
})
@Entity(name = "Perfil")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NombrePerfil rol;

    public Perfil(DatosRegistroActualizacionPerfil datos) {
        this.rol = datos.rol();
    }

    public void actualizarInformacion(DatosRegistroActualizacionPerfil datos) {
        this.rol = datos.rol();
    }
}