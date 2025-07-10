package com.ecomarket_spa.cl.ecomarket_spa.controller;

import com.ecomarket_spa.cl.ecomarket_spa.hateoas.UsuarioModelAssembler;
import com.ecomarket_spa.cl.ecomarket_spa.model.Rol;
import com.ecomarket_spa.cl.ecomarket_spa.model.Usuario;
import com.ecomarket_spa.cl.ecomarket_spa.service.RolService;
import com.ecomarket_spa.cl.ecomarket_spa.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name= "Gestion de Usuarios", description = "API para operaciones CRUD de usuarios con HATEOAS")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RolService rolService;
    private final UsuarioModelAssembler usuarioAssembler;

    public UsuarioController(UsuarioService usuarioService,
                             RolService rolService,
                             UsuarioModelAssembler usuarioAssembler) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
        this.usuarioAssembler = usuarioAssembler;
    }

    @Operation(summary = "Obtener todos los usuarios")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> obtenerTodos() {
        List<EntityModel<Usuario>> usuarios = usuarioService.listarTodos().stream()
                .map(usuarioAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(
                usuarios,
                linkTo(methodOn(UsuarioController.class).obtenerTodos()).withSelfRel()
        ));
    }

    @Operation(summary = "Obtener usuario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> obtenerPorId(@PathVariable Long id) {
        Usuario u = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuarioAssembler.toModel(u));
    }

    @Operation(summary = "Obtener usuario por RUN")
    @GetMapping("/run/{run}")
    public ResponseEntity<EntityModel<Usuario>> obtenerPorRun(@PathVariable String run) {
        Optional<Usuario> opt = usuarioService.findByRun(run);
        return opt
                .map(usuarioAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo usuario")
    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> crear(@RequestBody Usuario payload) {
        // se espera que el JSON traiga { "rol": { "id": <rolId> }, ... }
        Long rolId = payload.getRol().getId();
        Rol rol = rolService.buscarPorId(rolId);
        payload.setRol(rol);

        Usuario nuevo = usuarioService.crearUsuario(payload);
        EntityModel<Usuario> model = usuarioAssembler.toModel(nuevo);

        return ResponseEntity
                .created(linkTo(methodOn(UsuarioController.class).obtenerPorId(nuevo.getId())).toUri())
                .body(model);
    }

    @Operation(summary = "Actualizar usuario por RUN")
    @PutMapping("/run/{run}")
    public ResponseEntity<EntityModel<Usuario>> actualizarPorRun(
            @PathVariable String run,
            @RequestBody Usuario update
    ) {
        Optional<Usuario> opt = usuarioService.findByRun(run);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario existente = opt.get();
        existente.setNombre(update.getNombre());
        existente.setApellido(update.getApellido());
        existente.setCorreo(update.getCorreo());
        existente.setPassword(update.getPassword());
        if (update.getRol() != null) {
            Rol rolUpd = rolService.buscarPorId(update.getRol().getId());
            existente.setRol(rolUpd);
        }

        Usuario actualizado = usuarioService.crearUsuario(existente);
        return ResponseEntity.ok(usuarioAssembler.toModel(actualizado));
    }

    @Operation(summary = "Eliminar usuario por RUN")
    @DeleteMapping("/run/{run}")
    public ResponseEntity<Void> eliminarPorRun(@PathVariable String run) {
        usuarioService.deleteByRun(run);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar usuarios por correo")
    @GetMapping("/buscar")
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> buscarPorCorreo(
            @RequestParam String correo
    ) {
        List<EntityModel<Usuario>> usuarios = usuarioService.findByCorreo(correo).stream()
                .map(usuarioAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(
                usuarios,
                linkTo(methodOn(UsuarioController.class).buscarPorCorreo(correo)).withSelfRel()
        ));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus
    public void handleNotFound() {
    }
}