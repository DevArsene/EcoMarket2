package com.ecomarket_spa.cl.ecomarket_spa.controller;

import com.ecomarket_spa.cl.ecomarket_spa.model.Usuario;
import com.ecomarket_spa.cl.ecomarket_spa.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // solo si tienes configuraciones especiales para test
class UsuarioControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();
    }

    @Test
    void testCrearYObtenerUsuario() throws Exception {
        Usuario nuevo = new Usuario();
        nuevo.setRun("88888888-8");
        nuevo.setNombre("Test");
        nuevo.setApellido("User");
        nuevo.setCorreo("test@example.com");
        nuevo.setPassword("pass123");

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

        mockMvc.perform(get("/api/v1/usuarios/88888888-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.run").value("88888888-8"))
                .andExpect(jsonPath("$.nombre").value("Test"));
    }
}