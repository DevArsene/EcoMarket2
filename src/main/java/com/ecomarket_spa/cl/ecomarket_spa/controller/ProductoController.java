package com.ecomarket_spa.cl.ecomarket_spa.controller;

import com.ecomarket_spa.cl.ecomarket_spa.hateoas.ProductoModelAssembler;
import com.ecomarket_spa.cl.ecomarket_spa.model.Producto;
import com.ecomarket_spa.cl.ecomarket_spa.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final ProductoModelAssembler productoAssembler;

    public ProductoController(ProductoService productoService, ProductoModelAssembler productoAssembler) {
        this.productoService = productoService;
        this.productoAssembler = productoAssembler;
    }

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
        // 👇 CAMBIO AQUÍ: Lógica mejorada para manejar productos no encontrados
        Producto producto = productoService.findByEan(ean);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productoAssembler.toModel(producto));
    }

    @Operation(summary = "Modificar un producto existente")
    @PutMapping("/ean/{ean}")
    public ResponseEntity<EntityModel<Producto>> modificar(@PathVariable String ean, @RequestBody Producto producto) {
        try {
            Producto actualizado = productoService.updateByEan(ean, producto);
            EntityModel<Producto> model = productoAssembler.toModel(actualizado);
            return ResponseEntity.ok(model);
        } catch (RuntimeException e) { // Es mejor capturar una excepción más específica
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar un producto por EAN")
    @DeleteMapping("/ean/{ean}")
    public ResponseEntity<Void> eliminar(@PathVariable String ean) {
        try {
            productoService.deleteByEan(ean);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) { // Es mejor capturar una excepción más específica
            return ResponseEntity.notFound().build();
        }
    }
}