package com.ecomarket_spa.cl.ecomarket_spa.controller;

import com.ecomarket_spa.cl.ecomarket_spa.controller.ProductoController;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.ecomarket_spa.cl.ecomarket_spa.model.Producto;
import com.ecomarket_spa.cl.ecomarket_spa.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @Test
    void testGetProductoPorEan() throws Exception {
        Producto producto = new Producto(1L, "2222222222222", "Pan", "Integral", 1200, null);
        when(productoService.findByEan("2222222222222")).thenReturn(producto);

        mockMvc.perform(get("/api/v1/productos/2222222222222"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ean").value("2222222222222"))
                .andExpect(jsonPath("$.nombre").value("Pan"));
    }
}