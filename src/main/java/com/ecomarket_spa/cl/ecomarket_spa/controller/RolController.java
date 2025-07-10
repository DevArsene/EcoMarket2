package com.ecomarket_spa.cl.ecomarket_spa.controller;

import com.ecomarket_spa.cl.ecomarket_spa.model.Rol;
import com.ecomarket_spa.cl.ecomarket_spa.model.TipoRol;
import com.ecomarket_spa.cl.ecomarket_spa.service.RolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@Tag(name = "Gestión de Roles", description = "API para operaciones CRUD de roles")
public class RolController {

    private final RolService service;

    public RolController(RolService service) {
        this.service = service;
    }

    @Operation(summary = "Obtener todos los roles")
    @GetMapping
    public ResponseEntity<List<Rol>> listarTodos() {
        List<Rol> roles = service.listarTodos();
        return ResponseEntity.ok(roles);
    }

    @Operation(summary = "Obtener rol por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Rol> getById(@PathVariable Long id) {
        Rol rol = service.buscarPorId(id);
        return ResponseEntity.ok(rol);
    }

    @Operation(summary = "Obtener rol por tipo")
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<Rol> getByTipo(@PathVariable TipoRol tipo) {
        Rol rol = service.buscarPorTipo(tipo);
        return ResponseEntity.ok(rol);
    }

    @Operation(summary = "Crear nuevo rol")
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