package com.ecomarket_spa.cl.ecomarket_spa.controller;

import com.ecomarket_spa.cl.ecomarket_spa.controller.ProductoController;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.ecomarket_spa.cl.ecomarket_spa.hateoas.ProductoModelAssembler;
import com.ecomarket_spa.cl.ecomarket_spa.model.Producto;
import com.ecomarket_spa.cl.ecomarket_spa.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductoControllerSliceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @MockBean
    private ProductoModelAssembler productoModelAssembler;

    @Test
    void testGetProductoPorEan() throws Exception {
        // 1. Preparamos los datos de prueba
        String eanDePrueba = "1234567890123";
        Producto producto = new Producto(
                1L,
                eanDePrueba,
                "Producto de prueba",
                "Descripción de prueba",
                1000,
                null
        );

        when(productoService.findByEan(eanDePrueba)).thenReturn(producto);
        when(productoModelAssembler.toModel(any(Producto.class))).thenReturn(EntityModel.of(producto));

        mockMvc.perform(get("/api/v1/productos/ean/" + eanDePrueba))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Producto de prueba"));
    }
}