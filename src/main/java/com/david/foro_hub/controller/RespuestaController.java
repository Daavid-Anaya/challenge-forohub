package com.david.foro_hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.david.foro_hub.domain.respuesta.DatosActualizarRespuesta;
import com.david.foro_hub.domain.respuesta.DatosRegistroRespuesta;
import com.david.foro_hub.domain.respuesta.DatosRespuesta;
import com.david.foro_hub.domain.respuesta.Respuesta;
import com.david.foro_hub.repository.RespuestaRepository;
import com.david.foro_hub.repository.TopicoRepository;
import com.david.foro_hub.repository.UsuarioRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/respuestas")
public class RespuestaController {
    
    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TopicoRepository topicoRepository;
    
    @Transactional
    @PostMapping
    public ResponseEntity<DatosRespuesta> registrar(@RequestBody @Valid DatosRegistroRespuesta datos , UriComponentsBuilder uriComponentBuilder) {
        // Verificar que el tópico existe
        var topico = topicoRepository.findById(datos.topicoId());
        if (topico.isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400
        }
        // Verificar que el autor existe
        var autor = usuarioRepository.findById(datos.autorId());
        if (autor.isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400
        }

        var respuesta = new Respuesta(datos, topico.get(), autor.get());
        respuestaRepository.save(respuesta);

        var uri = uriComponentBuilder.path("/respuestas/{id}")
            .buildAndExpand(respuesta.getId()).toUri();
        
        return ResponseEntity.created(uri).body(new DatosRespuesta(respuesta));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuesta> detallar(@PathVariable Long id) {
        return respuestaRepository.findById(id)
            .map(respuesta -> ResponseEntity.ok(new DatosRespuesta(respuesta)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<DatosRespuesta>> listar(@PageableDefault(size = 10, sort = "fechaCreacion") Pageable paginacion) {
        var page = respuestaRepository.findByActivoTrue(paginacion).map(DatosRespuesta::new);

        return ResponseEntity.ok(page);
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<DatosRespuesta> actualizar(@PathVariable Long id, @RequestBody @Valid DatosActualizarRespuesta datos) {
        return respuestaRepository.findById(id)
            .map(respuesta -> {
                respuesta.actualizarInformacion(datos);
                return ResponseEntity.ok(new DatosRespuesta(respuesta));
            })
            .orElse(ResponseEntity.notFound().build());
    }


    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return respuestaRepository.findById(id)
            .map(respuesta -> {
                respuesta.desactivar();
                return ResponseEntity.noContent().<Void>build(); // 204
            })
            .orElse(ResponseEntity.notFound().build()); // 404
    }
}