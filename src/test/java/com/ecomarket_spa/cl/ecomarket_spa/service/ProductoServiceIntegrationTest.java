package com.ecomarket_spa.cl.ecomarket_spa.service;

import com.ecomarket_spa.cl.ecomarket_spa.model.Producto;
import com.ecomarket_spa.cl.ecomarket_spa.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
class ProductoServiceIntegrationTest {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoService productoService;

    @BeforeEach
    void limpiar() {
        productoRepository.deleteAll();
    }

    @Test
    void testGuardarYBuscarPorEan() {
        Producto producto = new Producto(null, "9876543210987", "Leche", "Entera", 1100, null);
        productoService.save(producto);

        Producto resultado = productoService.findByEan("9876543210987");

        assertNotNull(resultado);
        assertEquals("Leche", resultado.getNombre());
    }
}