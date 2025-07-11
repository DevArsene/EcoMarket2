package com.ecomarket_spa.cl.ecomarket_spa.controller;

import com.ecomarket_spa.cl.ecomarket_spa.hateoas.RolModelAssembler;
import com.ecomarket_spa.cl.ecomarket_spa.model.Rol;
import com.ecomarket_spa.cl.ecomarket_spa.model.TipoRol;
import com.ecomarket_spa.cl.ecomarket_spa.service.RolService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RolController.class)
class RolControllerSliceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RolService rolService;

    @MockBean
    private RolModelAssembler rolModelAssembler;

    @Test
    void whenGetAllRoles_thenReturnJsonArray() throws Exception {
        Rol rol = new Rol(1L, TipoRol.ADMINISTRADOR);
        List<Rol> roles = List.of(rol);

        when(rolService.listarTodos()).thenReturn(roles);
        when(rolModelAssembler.toModel(rol)).thenReturn(EntityModel.of(rol));

        mockMvc.perform(get("/api/v1/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.rolList[0].tipoRol").value("ADMINISTRADOR"));
    }

    @Test
    void whenGetRoleByIdNotFound_thenReturn404() throws Exception {
        when(rolService.buscarPorId(99L)).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/v1/roles/99"))
                .andExpect(status().isNotFound());
    }
}