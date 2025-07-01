package com.ecomarket_spa.cl.ecomarket_spa.service;

import com.ecomarket_spa.cl.ecomarket_spa.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test") // ¡Esta línea activa tu application-test.properties!
@Transactional
public class UsuarioServiceIntegrationTest {

    @Autowired
    private UsuarioService usuarioService;

    @Test
    void testH2ConfiguradoCorrectamente() {
        // Usa un RUN único de máximo 10 caracteres para cada prueba
        String runUnico = String.valueOf(System.currentTimeMillis() % 1_000_000_000L); // Máximo 10 dígitos

        Usuario usuario = new Usuario();
        usuario.setRun(runUnico);
        usuario.setNombre("Prueba H2");
        usuario.setApellido("Apellido Test");
        usuario.setCorreo("test" + System.currentTimeMillis() + "@h2.com");
        usuario.setPassword("password123");

        Usuario guardado = usuarioService.save(usuario);
        assertNotNull(guardado.getId());
    }
}