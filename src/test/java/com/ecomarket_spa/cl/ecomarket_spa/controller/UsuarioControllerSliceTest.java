package com.ecomarket_spa.cl.ecomarket_spa.controller;

import com.ecomarket_spa.cl.ecomarket_spa.hateoas.UsuarioModelAssembler;
import com.ecomarket_spa.cl.ecomarket_spa.model.Usuario;
import com.ecomarket_spa.cl.ecomarket_spa.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerSliceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioModelAssembler usuarioModelAssembler;

    @Test
    void testGetAllUsuarios() throws Exception {
        Usuario u1 = new Usuario(); u1.setRun("11111111-1");
        Usuario u2 = new Usuario(); u2.setRun("22222222-2");
        List<Usuario> lista = Arrays.asList(u1, u2);

        when(usuarioService.findAll()).thenReturn(lista);


        when(usuarioModelAssembler.toModel(any(Usuario.class)))
                .thenAnswer(inv -> {
                    Usuario u = inv.getArgument(0);
                    return EntityModel.of(u,
                            linkTo(methodOn(UsuarioController.class).obtenerPorRun(u.getRun())).withSelfRel()
                    );
                });

        mockMvc.perform(get("/api/v1/usuarios")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.usuarioList.length()").value(2))
                .andExpect(jsonPath("$._embedded.usuarioList[0].run").value("11111111-1"));
    }

    @Test
    void testCreateUsuario() throws Exception {
        Usuario nuevo = new Usuario();
        nuevo.setRun("99999999-9");
        nuevo.setNombre("Vicente");
        nuevo.setApellido("Tester");
        nuevo.setCorreo("vicente@example.com");
        nuevo.setPassword("segura123");

        when(usuarioService.save(nuevo)).thenReturn(nuevo);

        when(usuarioModelAssembler.toModel(any(Usuario.class)))
                .thenAnswer(inv -> {
                    Usuario u = inv.getArgument(0);
                    return EntityModel.of(u,
                            linkTo(methodOn(UsuarioController.class).obtenerPorRun(u.getRun())).withSelfRel()
                    );
                });

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.run").value("99999999-9"));
    }
}