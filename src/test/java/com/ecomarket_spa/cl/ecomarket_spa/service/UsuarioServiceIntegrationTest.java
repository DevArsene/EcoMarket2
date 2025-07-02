package com.ecomarket_spa.cl.ecomarket_spa.service;

import com.ecomarket_spa.cl.ecomarket_spa.model.Rol;
import com.ecomarket_spa.cl.ecomarket_spa.model.TipoRol;
import com.ecomarket_spa.cl.ecomarket_spa.model.Usuario;
import com.ecomarket_spa.cl.ecomarket_spa.repository.RolRepository;
import com.ecomarket_spa.cl.ecomarket_spa.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class UsuarioServiceIntegrationTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private RolRepository rolRepo;

    private Rol rolPorDefecto;

    @BeforeEach
    void setup() {
        usuarioRepo.deleteAll();
        rolRepo.deleteAll();

        rolPorDefecto = rolRepo.save(new Rol(null, TipoRol.EMPLEADO));
    }

    @Test
    void givenValidUsuario_whenCrearUsuario_thenPersistWithRunAndRol() {
        // given
        Usuario u = new Usuario();
        u.setRun("11111111-1");                 // aseguro run no nulo
        u.setNombre("Juan");
        u.setApellido("Pérez");
        u.setCorreo("juan@mail.com");
        u.setPassword("1234");
        u.setRol(rolPorDefecto);

        // when
        Usuario guardado = usuarioService.crearUsuario(u);

        // then
        assertThat(guardado.getId()).isNotNull();
        assertThat(guardado.getRun()).isEqualTo("11111111-1");
        assertThat(guardado.getRol().getId())
                .isEqualTo(rolPorDefecto.getId());
    }
}