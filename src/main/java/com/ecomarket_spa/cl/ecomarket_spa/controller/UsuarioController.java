package com.ecomarket_spa.cl.ecomarket_spa.controller;

import com.ecomarket_spa.cl.ecomarket_spa.hateoas.UsuarioModelAssembler;
import com.ecomarket_spa.cl.ecomarket_spa.model.Usuario;
import com.ecomarket_spa.cl.ecomarket_spa.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Gestión de Usuarios", description = "API para operaciones CRUD de usuarios con HATEOAS")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioModelAssembler usuarioAssembler;

    @Operation(summary = "Obtener todos los usuarios")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> obtenerTodos() {
        List<EntityModel<Usuario>> usuarios = usuarioService.findAll().stream()
                .map(usuarioAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(usuarios, linkTo(methodOn(UsuarioController.class).obtenerTodos()).withSelfRel())
        );
    }

    @Operation(summary = "Buscar usuario por RUN")
    @GetMapping("/{run}")
    public ResponseEntity<EntityModel<Usuario>> obtenerPorRun(@PathVariable String run) {
        return usuarioService.findByRun(run)
                .map(usuarioAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear nuevo usuario")
    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> crear(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.save(usuario);
        EntityModel<Usuario> resource = usuarioAssembler.toModel(nuevoUsuario);

        return ResponseEntity
                .created(resource.getRequiredLink("self").toUri())
                .body(resource);
    }

    @Operation(summary = "Actualizar usuario existente")
    @PutMapping("/{run}")
    public ResponseEntity<EntityModel<Usuario>> actualizar(@PathVariable String run, @RequestBody Usuario usuarioActualizado) {
        return usuarioService.findByRun(run)
            .map(usuario -> {
                usuario.setNombre(usuarioActualizado.getNombre());
                usuario.setApellido(usuarioActualizado.getApellido());
                usuario.setCorreo(usuarioActualizado.getCorreo());
                usuario.setPassword(usuarioActualizado.getPassword());
                Usuario actualizado = usuarioService.save(usuario);
                return ResponseEntity.ok(usuarioAssembler.toModel(actualizado));
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar usuario")
    @DeleteMapping("/{run}")
    public ResponseEntity<Void> eliminar(@PathVariable String run) {
        usuarioService.deleteByRun(run);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar usuario por correo")
    @GetMapping("/buscar")
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> buscarPorCorreo(@RequestParam String correo) {
        List<EntityModel<Usuario>> usuarios = usuarioService.findByCorreo(correo).stream()
            .map(usuarioAssembler::toModel)
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            CollectionModel.of(usuarios,
                linkTo(methodOn(UsuarioController.class).buscarPorCorreo(correo)).withSelfRel())
        );
    }
}