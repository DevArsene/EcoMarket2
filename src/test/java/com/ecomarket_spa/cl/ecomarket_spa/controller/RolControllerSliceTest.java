package com.ecomarket_spa.cl.ecomarket_spa.controller;

import com.ecomarket_spa.cl.ecomarket_spa.model.Rol;
import com.ecomarket_spa.cl.ecomarket_spa.model.TipoRol;
import com.ecomarket_spa.cl.ecomarket_spa.service.RolService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RolController.class)
class RolControllerSliceTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RolService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenGetAllRoles_thenReturnJsonArray() throws Exception {
        List<Rol> roles = List.of(
                new Rol(1L, TipoRol.ADMINISTRADOR),
                new Rol(2L, TipoRol.GERENTE)
        );
        given(service.listarTodos()).willReturn(roles);

        mvc.perform(get("/api/v1/roles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].tipoRol").value("ADMINISTRADOR"))
                .andExpect(jsonPath("$[1].tipoRol").value("GERENTE"));

        then(service).should().listarTodos();
    }

    @Test
    void whenGetRoleByIdExists_thenReturnJson() throws Exception {
        Rol rol = new Rol(5L, TipoRol.EMPLEADO);
        given(service.buscarPorId(5L)).willReturn(rol);

        mvc.perform(get("/api/v1/roles/{id}", 5))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.tipoRol").value("EMPLEADO"));

        then(service).should().buscarPorId(5L);
    }

    @Test
    void whenGetRoleByIdNotFound_thenReturn404() throws Exception {
        given(service.buscarPorId(99L))
                .willThrow(new EntityNotFoundException("no existe"));

        mvc.perform(get("/api/v1/roles/{id}", 99))
                .andExpect(status().isNotFound());

        then(service).should().buscarPorId(99L);
    }

    @Test
    void whenCreateRole_thenReturnCreatedAndLocationHeader() throws Exception {
        Rol input = new Rol(null, TipoRol.LOGISTICA);
        Rol saved = new Rol(10L, TipoRol.LOGISTICA);
        given(service.crearRol(input)).willReturn(saved);

        mvc.perform(post("/api/v1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/roles/10"))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.tipoRol").value("LOGISTICA"));

        then(service).should().crearRol(input);
    }
}