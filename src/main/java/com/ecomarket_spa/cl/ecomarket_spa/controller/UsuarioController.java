package com.ecomarket_spa.cl.ecomarket_spa.controller;

import com.ecomarket_spa.cl.ecomarket_spa.model.Usuario;
import com.ecomarket_spa.cl.ecomarket_spa.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Gestión de Usuarios", description = "API para operaciones CRUD de usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(
        summary = "Obtener todos los usuarios",
        description = "Retorna una lista completa de usuarios registrados en el sistema"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de usuarios encontrada",
        content = @Content(schema = @Schema(implementation = Usuario[].class))
    )
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @Operation(
        summary = "Buscar usuario por RUN",
        description = "Obtiene los detalles de un usuario específico usando su RUN"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Usuario encontrado",
        content = @Content(schema = @Schema(implementation = Usuario.class))
    )
    @ApiResponse(
        responseCode = "404",
        description = "Usuario no encontrado"
    )
    @GetMapping("/{run}")
    public ResponseEntity<Usuario> obtenerPorRun(
        @Parameter(
            description = "RUN del usuario (formato: 12345678-9)",
            example = "12345678-9",
            required = true
        )
        @PathVariable String run
    ) {
        return usuarioService.findByRun(run)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Crear nuevo usuario",
        description = "Registra un nuevo usuario en el sistema"
    )
    @ApiResponse(
        responseCode = "201",
        description = "Usuario creado exitosamente",
        content = @Content(schema = @Schema(implementation = Usuario.class))
    )
    @ApiResponse(
        responseCode = "400",
        description = "Datos de usuario inválidos"
    )
    @PostMapping
    public ResponseEntity<Usuario> crear(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del nuevo usuario",
            required = true,
            content = @Content(schema = @Schema(implementation = Usuario.class))
        )
        @RequestBody Usuario usuario
    ) {
        return new ResponseEntity<>(usuarioService.save(usuario), HttpStatus.CREATED);
    }

    @Operation(
        summary = "Actualizar usuario existente",
        description = "Actualiza la información de un usuario usando su RUN"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Usuario actualizado exitosamente",
        content = @Content(schema = @Schema(implementation = Usuario.class))
    )
    @ApiResponse(
        responseCode = "404",
        description = "Usuario no encontrado"
    )
    @PutMapping("/{run}")
    public ResponseEntity<Usuario> actualizar(
        @Parameter(
            description = "RUN del usuario a actualizar",
            example = "12345678-9",
            required = true
        )
        @PathVariable String run,

        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos actualizados del usuario",
            required = true,
            content = @Content(schema = @Schema(implementation = Usuario.class))
        )
        @RequestBody Usuario usuarioActualizado
    ) {
        Optional<Usuario> usuarioExistente = usuarioService.findByRun(run);

        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();
            usuario.setNombre(usuarioActualizado.getNombre());
            usuario.setApellido(usuarioActualizado.getApellido());
            usuario.setCorreo(usuarioActualizado.getCorreo());
            usuario.setPassword(usuarioActualizado.getPassword());

            return ResponseEntity.ok(usuarioService.save(usuario));
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(
        summary = "Eliminar usuario",
        description = "Elimina un usuario existente usando su RUN"
    )
    @ApiResponse(
        responseCode = "204",
        description = "Usuario eliminado exitosamente"
    )
    @ApiResponse(
        responseCode = "404",
        description = "Usuario no encontrado"
    )
    @DeleteMapping("/{run}")
    public ResponseEntity<Void> eliminar(
        @Parameter(
            description = "RUN del usuario a eliminar",
            example = "12345678-9",
            required = true
        )
        @PathVariable String run
    ) {
        usuarioService.deleteByRun(run);
        return ResponseEntity.noContent().build();
    }
}