package com.david.foro_hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.david.foro_hub.repository.RespuestaRepository;

@RestController
@RequestMapping("/respuestas")
public class RespuestaController {
    
    @Autowired
    private RespuestaRepository repository;
    
}