package com.david.foro_hub.domain.topicos;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.david.foro_hub.domain.curso.Curso;
import com.david.foro_hub.domain.respuesta.Respuesta;
import com.david.foro_hub.domain.usuario.Usuario;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "topicos")
@Entity(name = "Topico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String mensaje;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    private StatusTopicos status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Usuario autor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @OneToMany(mappedBy = "topico", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Respuesta> respuestas = new HashSet<>();

    public Topico(DatosRegistroTopico datos, Usuario autor, Curso curso) {
        this.titulo = datos.titulo();
        this.mensaje = datos.mensaje();
        this.fechaCreacion = LocalDateTime.now();
        this.status = datos.status();
        this.autor = autor;
        this.curso = curso;
        this.respuestas = new HashSet<>();
    }

    public void actualizarInformacion(DatosActualizarTopico datos, Curso curso) {
        this.titulo = datos.titulo();
        this.mensaje = datos.mensaje();
        this.status = datos.status();
        this.curso = curso;
    }
}