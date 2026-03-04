package com.david.foro_hub.domain.curso;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
    name = "cursos",
    uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nombre", "categoria"})
}
)
@Entity(name = "Curso")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String categoria;

    public Curso(DatosRegistroCurso datos) {
        this.nombre = datos.nombre();
        this.categoria = datos.categoria();
    }

    public void actualizarInformacion(DatosActualizarCurso datos) {
        if (datos.nombre() != null && !datos.nombre().isBlank()) {
            this.nombre = datos.nombre().trim();
        }

        if (datos.categoria() != null && !datos.categoria().isBlank()) {
            this.categoria = datos.categoria().trim();
        }
    }
}