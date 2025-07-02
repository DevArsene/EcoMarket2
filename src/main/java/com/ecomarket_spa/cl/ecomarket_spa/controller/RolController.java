package com.ecomarket_spa.cl.ecomarket_spa.controller;

import com.ecomarket_spa.cl.ecomarket_spa.model.Rol;
import com.ecomarket_spa.cl.ecomarket_spa.model.TipoRol;
import com.ecomarket_spa.cl.ecomarket_spa.service.RolService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RolController {

    private final RolService service;

    public RolController(RolService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Rol>> listarTodos() {
        List<Rol> roles = service.listarTodos();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rol> getById(@PathVariable Long id) {
        Rol rol = service.buscarPorId(id);
        return ResponseEntity.ok(rol);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<Rol> getByTipo(@PathVariable TipoRol tipo) {
        Rol rol = service.buscarPorTipo(tipo);
        return ResponseEntity.ok(rol);
    }

    @PostMapping
    public ResponseEntity<Rol> crear(@RequestBody Rol nuevoRol) {
        Rol saved = service.crearRol(nuevoRol);
        URI location = URI.create("/api/v1/roles/" + saved.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        return new ResponseEntity<>(saved, headers, HttpStatus.CREATED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNotFound() {
    }
}