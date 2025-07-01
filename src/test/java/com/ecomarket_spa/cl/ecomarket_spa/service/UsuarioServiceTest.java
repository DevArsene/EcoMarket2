package com.ecomarket_spa.cl.ecomarket_spa.service;

import com.ecomarket_spa.cl.ecomarket_spa.model.Usuario;
import com.ecomarket_spa.cl.ecomarket_spa.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario1, usuario2));

        List<Usuario> result = usuarioService.findAll();

        assertEquals(2, result.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testFindByRunFound() {
        Usuario usuario = new Usuario();
        usuario.setRun("12345678-9");
        when(usuarioRepository.findByRun("12345678-9")).thenReturn(Optional.of(usuario));

        Optional<Usuario> result = usuarioService.findByRun("12345678-9");

        assertTrue(result.isPresent());
        assertEquals("12345678-9", result.get().getRun());
        verify(usuarioRepository, times(1)).findByRun("12345678-9");
    }

    @Test
    void testFindByRunNotFound() {
        when(usuarioRepository.findByRun("00000000-0")).thenReturn(Optional.empty());

        Optional<Usuario> result = usuarioService.findByRun("00000000-0");

        assertFalse(result.isPresent());
        verify(usuarioRepository, times(1)).findByRun("00000000-0");
    }

    @Test
    void testSave() {
        Usuario usuario = new Usuario();
        usuario.setRun("98765432-1");
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario result = usuarioService.save(usuario);

        assertEquals("98765432-1", result.getRun());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void testDeleteByRunExists() {
        Usuario usuario = new Usuario();
        usuario.setRun("11111111-1");
        when(usuarioRepository.findByRun("11111111-1")).thenReturn(Optional.of(usuario));

        usuarioService.deleteByRun("11111111-1");

        verify(usuarioRepository, times(1)).findByRun("11111111-1");
        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    void testDeleteByRunNotExists() {
        when(usuarioRepository.findByRun("22222222-2")).thenReturn(Optional.empty());

        usuarioService.deleteByRun("22222222-2");

        verify(usuarioRepository, times(1)).findByRun("22222222-2");
        verify(usuarioRepository, never()).delete(any());
    }
}
