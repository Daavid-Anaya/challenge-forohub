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

import com.david.foro_hub.domain.curso.Curso;
import com.david.foro_hub.domain.curso.DatosActualizarCurso;
import com.david.foro_hub.domain.curso.DatosCurso;
import com.david.foro_hub.domain.curso.DatosRegistroCurso;
import com.david.foro_hub.repository.CursoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoRepository repository;

    @Transactional
    @PostMapping
    public ResponseEntity<DatosCurso> registrar(@RequestBody @Valid DatosRegistroCurso datos, UriComponentsBuilder uriComponentBuilder) {
        if (repository.existsByNombreAndCategoria(datos.nombre(), datos.categoria())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 Conflict
        }
        
        var curso = new Curso(datos);
        repository.save(curso);

        var uri = uriComponentBuilder.path("/cursos/{id}").buildAndExpand(curso.getId()).toUri();

        return ResponseEntity.created(uri).body(new DatosCurso(curso));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosCurso> detallar(@PathVariable Long id) {
        return repository.findById(id)
            .map(curso -> ResponseEntity.ok(new DatosCurso(curso)))
            .orElse(ResponseEntity.notFound().build()); // 404 si no existe  
    }

    @GetMapping
    public ResponseEntity<Page<DatosCurso>> listar(@PageableDefault(size = 10, sort = "nombre") Pageable paginacion) {
        var page = repository.findAll(paginacion).map(DatosCurso::new);

        return ResponseEntity.ok(page);
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<DatosCurso> actualizar(@PathVariable Long id, @RequestBody @Valid DatosActualizarCurso datos) {
        return repository.findById(id)
            .map(curso -> {
                // Verifica duplicado solo si los campos vienen con valor
                if (datos.nombre() != null && datos.categoria() != null
                        && repository.existsByNombreAndCategoriaAndIdNot(
                                datos.nombre(), datos.categoria(), id)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).<DatosCurso>build(); // 409
                }
                curso.actualizarInformacion(datos);
                return ResponseEntity.ok(new DatosCurso(curso));
            })
            .orElse(ResponseEntity.notFound().build()); // 404
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity eliminar(@PathVariable Long id) {
        return repository.findById(id)
            .map(curso -> {
                repository.delete(curso);
                return ResponseEntity.noContent().build(); // 204
            })
            .orElse(ResponseEntity.notFound().build()); // 404 si no existe
    }
}