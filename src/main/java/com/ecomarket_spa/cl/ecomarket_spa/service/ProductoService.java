package com.ecomarket_spa.cl.ecomarket_spa.service;

import com.ecomarket_spa.cl.ecomarket_spa.model.Producto;
import com.ecomarket_spa.cl.ecomarket_spa.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> findAll(){
        return productoRepository.findAll();
    }

    public Producto findByEan(String ean) {
        return productoRepository.findByEan(ean).orElse(null);
    }

    public Producto save(Producto producto) {
        if (producto.getEan() == null) {
            throw new IllegalArgumentException("El EAN no puede ser nulo");
        }
        return productoRepository.save(producto);
    }

    public void deleteByEan(String ean) {
        Producto producto = productoRepository.findByEan(ean).orElse(null);
        if (producto != null) {
            productoRepository.delete(producto);
        } else {
            throw new RuntimeException("Producto no encontrado");
        }
    }

    public Producto updateByEan(String ean, Producto producto) {
        Producto existente = productoRepository.findByEan(ean).orElse(null);
        if (existente == null) {
            throw new RuntimeException("Producto no encontrado");
        }
        // No modificar el EAN en el update
        if (producto.getNombre() != null) existente.setNombre(producto.getNombre());
        if (producto.getDescripcion() != null) existente.setDescripcion(producto.getDescripcion());
        if (producto.getPrecio() != null) existente.setPrecio(producto.getPrecio());
        if (producto.getFechaVencimiento() != null) existente.setFechaVencimiento(producto.getFechaVencimiento());

        return productoRepository.save(existente);
    }
}
