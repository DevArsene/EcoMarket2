package com.ecomarket_spa.cl.ecomarket_spa.service;

import com.ecomarket_spa.cl.ecomarket_spa.model.Rol;
import com.ecomarket_spa.cl.ecomarket_spa.model.TipoRol;
import com.ecomarket_spa.cl.ecomarket_spa.repository.RolRepository;
import com.ecomarket_spa.cl.ecomarket_spa.service.RolServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class RolServiceTest {

    @Mock
    private RolRepository rolRepo;

    @InjectMocks
    private RolServiceImpl service;  // O la clase que implemente RolService

    @Test
    void whenValidTipoRol_thenSave_ReturnsRol() {
        // dado
        Rol entrada = new Rol();
        entrada.setTipoRol(TipoRol.ADMINISTRADOR);
        given(rolRepo.save(any(Rol.class))).willReturn(entrada);

        // cuando
        Rol resultado = service.crearRol(entrada);

        // entonces
        assertThat(resultado).isNotNull();
        assertThat(resultado.getTipoRol()).isEqualTo(TipoRol.ADMINISTRADOR);
        then(rolRepo).should().save(entrada);
    }

    @Test
    void whenBuscarPorTipoExistente_thenReturnsRol() {
        // dado
        Rol entrada = new Rol();
        entrada.setTipoRol(TipoRol.EMPLEADO);
        given(rolRepo.findByTipoRol(TipoRol.EMPLEADO))
                .willReturn(Optional.of(entrada));

        // cuando
        Rol rol = service.buscarPorTipo(TipoRol.EMPLEADO);

        // entonces
        assertThat(rol.getTipoRol()).isEqualTo(TipoRol.EMPLEADO);
        then(rolRepo).should().findByTipoRol(TipoRol.EMPLEADO);
    }

    @Test
    void whenBuscarPorTipoInexistente_thenThrowsNotFound() {
        // dado
        given(rolRepo.findByTipoRol(TipoRol.LOGISTICA))
                .willReturn(Optional.empty());

        // cuando / entonces
        assertThatThrownBy(() -> service.buscarPorTipo(TipoRol.LOGISTICA))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Rol no encontrado");
    }
}