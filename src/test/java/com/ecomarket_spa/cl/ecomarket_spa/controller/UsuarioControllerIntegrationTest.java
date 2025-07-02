package com.ecomarket_spa.cl.ecomarket_spa.controller;

import com.ecomarket_spa.cl.ecomarket_spa.model.Rol;
import com.ecomarket_spa.cl.ecomarket_spa.model.TipoRol;
import com.ecomarket_spa.cl.ecomarket_spa.repository.RolRepository;
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

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UsuarioControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RolRepository rolRepository;

    private Rol rolPorDefecto;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();
        rolRepository.deleteAll();
        rolPorDefecto = rolRepository.save(new Rol(null, TipoRol.EMPLEADO));
    }

    @Test
    void testCrearYObtenerUsuario() throws Exception {
        Map<String, Object> payload = Map.of(
                "run",      "88888888-8",
                "nombre",   "Test",
                "apellido", "User",
                "correo",   "test@example.com",
                "password", "pass123",
                "rol",      Map.of("id", rolPorDefecto.getId())
        );
        String json = objectMapper.writeValueAsString(payload);

        var response = mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/v1/usuarios/")))
                .andExpect(jsonPath("$.run").value("88888888-8"))
                .andExpect(jsonPath("$.rol.id").value(rolPorDefecto.getId()))
                .andReturn();

        String location = response.getResponse().getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.run").value("88888888-8"));
    }
}