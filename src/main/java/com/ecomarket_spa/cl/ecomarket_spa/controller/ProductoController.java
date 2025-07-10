package com.ecomarket_spa.cl.ecomarket_spa.controller;

import com.ecomarket_spa.cl.ecomarket_spa.hateoas.ProductoModelAssembler;
import com.ecomarket_spa.cl.ecomarket_spa.model.Producto;
import com.ecomarket_spa.cl.ecomarket_spa.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Gestión de Productos" , description = "API para operaciones CRUD de productos con HATEOAS")
public class ProductoController {

    private final ProductoService productoService;
    private final ProductoModelAssembler productoAssembler;

    public ProductoController(ProductoService productoService, ProductoModelAssembler productoAssembler) {
        this.productoService = productoService;
        this.productoAssembler = productoAssembler;
    }

    // 2. Usa el ensamblador en los métodos de respuesta
    @Operation(summary = "Obtener todos los productos")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> listar() {
        List<EntityModel<Producto>> productos = productoService.findAll().stream()
                .map(productoAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(
                productos,
                linkTo(methodOn(ProductoController.class).listar()).withSelfRel()
        ));
    }

    @Operation(summary = "Agregar producto")
    @PostMapping
    public ResponseEntity<EntityModel<Producto>> guardar(@RequestBody Producto producto) {
        Producto productoNuevo = productoService.save(producto);
        EntityModel<Producto> model = productoAssembler.toModel(productoNuevo);
        return ResponseEntity
                .created(linkTo(methodOn(ProductoController.class).buscar(productoNuevo.getEan())).toUri())
                .body(model);
    }

    @Operation(summary = "Buscar por EAN (codigo producto)")
    @GetMapping("/ean/{ean}")
    public ResponseEntity<EntityModel<Producto>> buscar(@PathVariable String ean) {
        try {
            Producto producto = productoService.findByEan(ean);
            return ResponseEntity.ok(productoAssembler.toModel(producto));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}