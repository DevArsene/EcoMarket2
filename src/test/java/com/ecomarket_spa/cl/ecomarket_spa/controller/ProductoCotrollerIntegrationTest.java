package com.ecomarket_spa.cl.ecomarket_spa.controller;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ecomarket_spa.cl.ecomarket_spa.model.Producto;
import com.ecomarket_spa.cl.ecomarket_spa.repository.ProductoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Buena práctica para que las pruebas no afecten a otras
class ProductoCotrollerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductoRepository productoRepository;

    // 👇 MEJORA: Inyecta el ObjectMapper que Spring ya tiene configurado
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void limpiar() {
        productoRepository.deleteAll();
    }

    @Test
    void testCrearYObtenerProducto() throws Exception {
        String eanDePrueba = "3333333333333";
        Producto producto = new Producto(null, eanDePrueba, "Aceite", "Vegetal", 2800, null);

        mockMvc.perform(post("/api/v1/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/productos/ean/" + eanDePrueba))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Aceite"));
    }
}