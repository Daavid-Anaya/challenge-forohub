package com.david.foro_hub.infra.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GestorDeErrores {
    // 404 — Entidad no encontrada
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> gestionarError404() {
        return ResponseEntity.notFound().build();
    }

    // 400 — Errores de validación en los campos
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> gestionarError400(MethodArgumentNotValidException ex) {
        var errores = ex.getFieldErrors();
        return ResponseEntity.badRequest()
                .body(errores.stream().map(DatosErrorValidacion::new).toList());
    }

    // 400 — JSON mal formado o tipos incorrectos
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> gestionarErrorDeMensaje(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    // 405 — Método HTTP no permitido (GET en lugar de POST, etc.)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> gestionarError405(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(ex.getMessage());
    }

    // 409 — Recurso duplicado
    @ExceptionHandler(ValidacionException.class)
    public ResponseEntity<?> gestionarError409(ValidacionException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    // 500 — Errores inesperados del servidor
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> gestionarError500(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor: " + ex.getMessage());
    }

    public record DatosErrorValidacion(String campo, String mensaje) {
        public DatosErrorValidacion(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
}