package com.david.foro_hub.domain.respuesta;

import java.time.LocalDateTime;

import com.david.foro_hub.domain.topicos.Topico;
import com.david.foro_hub.domain.usuario.Usuario;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "respuestas")
@Entity(name = "Respuesta")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Respuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensaje;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topico_id")
    private Topico topico;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Usuario autor;
    
    private Boolean solucion;
    private Boolean activo = true;

    public Respuesta(DatosRegistroRespuesta datos, Topico topico, Usuario autor) {
        this.mensaje = datos.mensaje();
        this.topico = topico;
        this.fechaCreacion = LocalDateTime.now();
        this.autor = autor;
        this.solucion = datos.solucion();
    }

    public void actualizarInformacion(DatosActualizarRespuesta datos) {
        this.mensaje = datos.mensaje();
        this.solucion = datos.solucion();
    }

    public void desactivar() {
        this.activo = false;
    }
}