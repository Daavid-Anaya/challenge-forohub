package com.david.foro_hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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

import com.david.foro_hub.domain.topicos.DatosActualizarTopico;
import com.david.foro_hub.domain.topicos.DatosRegistroTopico;
import com.david.foro_hub.domain.topicos.DatosTopico;
import com.david.foro_hub.domain.topicos.Topico;
import com.david.foro_hub.repository.CursoRepository;
import com.david.foro_hub.repository.TopicoRepository;
import com.david.foro_hub.repository.UsuarioRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/topicos")
public class TopicoController {
    
    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Transactional
    @PostMapping
    public ResponseEntity<DatosTopico> registrar(@RequestBody @Valid DatosRegistroTopico datos, UriComponentsBuilder uriComponentsBuilder) {
        // Verificar topico duplicado
        if (topicoRepository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
        }

         // Verificar que el autor existe
        var autor = usuarioRepository.findById(datos.autorId());
        if (autor.isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400
        }

        // Verificar que el curso existe
        var curso = cursoRepository.findById(datos.cursoId());
        if (curso.isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400
        }

        var topico = new Topico(datos, autor.get(), curso.get());
        topicoRepository.save(topico);

        var uri = uriComponentsBuilder.path("/topicos/{id}")
                .buildAndExpand(topico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DatosTopico(topico));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosTopico> detallar(@PathVariable Long id) {
        return topicoRepository.findById(id)
            .map(topico -> ResponseEntity.ok(new DatosTopico(topico)))
            .orElse(ResponseEntity.notFound().build()); // 404
    }

    @GetMapping()
    public ResponseEntity<Page<DatosTopico>> listar(@PageableDefault(size = 10, sort = "titulo") Pageable paginacion) {
        var page = topicoRepository.findAll(paginacion).map(DatosTopico::new);

        return ResponseEntity.ok(page);
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<DatosTopico> actualizar(@PathVariable Long id, @RequestBody @Valid DatosActualizarTopico datos) {
        var topico = topicoRepository.findById(id);

        if (topico.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404
        }

        // Verificar duplicado excluyendo el propio tópico
        if (topicoRepository.existsByTituloAndMensajeAndIdNot(datos.titulo(), datos.mensaje(), id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 
        }

        // Verificar que el curso existe
        var curso = cursoRepository.findById(datos.cursoId());
        if (curso.isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400
        }

        var t = topico.get();
        t.actualizarInformacion(datos, curso.get());
        return ResponseEntity.ok(new DatosTopico(t));
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return topicoRepository.findById(id)
            .map(topico -> {
                topicoRepository.delete(topico);
                return ResponseEntity.noContent().<Void>build(); // 204
            })
            .orElse(ResponseEntity.notFound().build()); // 404
    }
}