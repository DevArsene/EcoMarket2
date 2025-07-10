package com.ecomarket_spa.cl.ecomarket_spa.controller;

import com.ecomarket_spa.cl.ecomarket_spa.model.Rol;
import com.ecomarket_spa.cl.ecomarket_spa.model.TipoRol;
import com.ecomarket_spa.cl.ecomarket_spa.service.RolService;
import com.ecomarket_spa.cl.ecomarket_spa.hateoas.RolModelAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/roles")
@Tag(name = "Gestión de Roles", description = "API para operaciones CRUD de roles")
public class RolController {

    private final RolService service;
    private final RolModelAssembler assembler;

    public RolController(RolService service, RolModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Operation(summary = "Obtener todos los roles")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Rol>>> listarTodos() {
        List<Rol> roles = service.listarTodos();
        List<EntityModel<Rol>> roleModels = roles.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Rol>> collectionModel = CollectionModel.of(roleModels);
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Obtener rol por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Rol>> getById(@PathVariable Long id) {
        Rol rol = service.buscarPorId(id);
        return ResponseEntity.ok(assembler.toModel(rol));
    }

    @Operation(summary = "Obtener rol por tipo")
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<EntityModel<Rol>> getByTipo(@PathVariable TipoRol tipo) {
        Rol rol = service.buscarPorTipo(tipo);
        return ResponseEntity.ok(assembler.toModel(rol));
    }

    @Operation(summary = "Crear nuevo rol")
    @PostMapping
    public ResponseEntity<EntityModel<Rol>> crear(@RequestBody Rol nuevoRol) {
        Rol saved = service.crearRol(nuevoRol);
        EntityModel<Rol> model = assembler.toModel(saved);
        URI location = URI.create("/api/v1/roles/" + saved.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        return new ResponseEntity<>(model, headers, HttpStatus.CREATED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNotFound() {
    }
}