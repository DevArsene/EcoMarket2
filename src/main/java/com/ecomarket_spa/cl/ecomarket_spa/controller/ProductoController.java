package com.ecomarket_spa.cl.ecomarket_spa.controller;

import com.ecomarket_spa.cl.ecomarket_spa.model.Producto;
import com.ecomarket_spa.cl.ecomarket_spa.service.ProductoService;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;
    @GetMapping
    public ResponseEntity<List<Producto>> listar(){
        List<Producto> productos = productoService.findAll();
        if(productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }

    @PostMapping
    public ResponseEntity<Producto> guardar(@RequestBody Producto producto) {
        Producto productoNuevo = productoService.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoNuevo);
    }

    @GetMapping("/{ean}")
    public ResponseEntity<Producto> buscar(@PathVariable String ean) {
        try{
            Producto producto = productoService.findByEan(ean);
            return ResponseEntity.ok(producto);
        } catch ( Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{ean}")
    public ResponseEntity<Producto> actualizar(@PathVariable String ean, @RequestBody Producto productoActualizado) {
        Producto producto = productoService.findByEan(ean);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        producto.setNombre(productoActualizado.getNombre());
        producto.setDescripcion(productoActualizado.getDescripcion());
        producto.setPrecio(productoActualizado.getPrecio());
        producto.setFechaVencimiento(productoActualizado.getFechaVencimiento());
        productoService.save(producto);

        return ResponseEntity.ok(producto);
    }

@DeleteMapping("/{ean}")
public ResponseEntity<Void> eliminar(@PathVariable String ean) {
    try {
        Producto producto = productoService.findByEan(ean);
        if (producto != null) {
            productoService.delete(producto.getId());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
}