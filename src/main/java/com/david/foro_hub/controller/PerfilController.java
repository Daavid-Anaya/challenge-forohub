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

import com.david.foro_hub.domain.perfil.DatosPerfil;
import com.david.foro_hub.domain.perfil.DatosRegistroActualizacionPerfil; 
import com.david.foro_hub.domain.perfil.Perfil;
import com.david.foro_hub.repository.PerfilRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/perfiles")
public class PerfilController {

    @Autowired
    private PerfilRepository repository;

    @Transactional
    @PostMapping
    public ResponseEntity<DatosPerfil> registrar(@RequestBody @Valid DatosRegistroActualizacionPerfil datos, UriComponentsBuilder uriComponentBuilder) {
        if (repository.existsByRol(datos.rol())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 si ya existe un perfil con el mismo nombre
        }
        
        var perfil = new Perfil(datos);
        repository.save(perfil);

        var uri = uriComponentBuilder.path("/perfiles/{id}").buildAndExpand(perfil.getId()).toUri();

        return ResponseEntity.created(uri).body(new DatosPerfil(perfil)); // 201
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosPerfil> detallar(@PathVariable Long id) {
        return repository.findById(id)
            .map(perfil -> ResponseEntity.ok(new DatosPerfil(perfil)))
            .orElse(ResponseEntity.notFound().build()); // 404 si no existe
    }
    
    @GetMapping
    public ResponseEntity<Page<DatosPerfil>> listar(@PageableDefault(size = 10, sort = "rol") Pageable paginacion) {
        var page = repository.findAll(paginacion).map(DatosPerfil::new);
        
        return ResponseEntity.ok(page); // 200
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<DatosPerfil> actualizar(@PathVariable Long id, @RequestBody @Valid DatosRegistroActualizacionPerfil datos) {
        var perfil = repository.findById(id);

        if (perfil.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404
        }

        if (repository.existsByRolAndIdNot(datos.rol(), id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
        }

        var p = perfil.get();
        p.actualizarInformacion(datos);
        return ResponseEntity.ok(new DatosPerfil(p)); // 200
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return repository.findById(id)
            .map(perfil -> {
                repository.delete(perfil);
                return ResponseEntity.noContent().<Void>build(); // 204
            })
            .orElse(ResponseEntity.notFound().build()); // 404
    }
}