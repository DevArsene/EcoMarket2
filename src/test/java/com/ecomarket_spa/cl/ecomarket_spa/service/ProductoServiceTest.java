package com.ecomarket_spa.cl.ecomarket_spa.service;

import com.ecomarket_spa.cl.ecomarket_spa.model.Producto;
import com.ecomarket_spa.cl.ecomarket_spa.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void testFindByEan_existe() {
        Producto producto = new Producto(1L, "1234567890123", "Manzana", "Verde", 500, null);
        when(productoRepository.findByEan("1234567890123")).thenReturn(Optional.of(producto));

        Producto resultado = productoService.findByEan("1234567890123");

        assertNotNull(resultado);
        assertEquals("1234567890123", resultado.getEan());
    }
}