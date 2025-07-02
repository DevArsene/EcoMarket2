package com.ecomarket_spa.cl.ecomarket_spa.repository;

import com.ecomarket_spa.cl.ecomarket_spa.model.Rol;
import com.ecomarket_spa.cl.ecomarket_spa.model.TipoRol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest(properties = {
        "spring.sql.init.mode=never",          // no cargar import.sql/data.sql
        "spring.jpa.hibernate.ddl-auto=create" // esquema limpio para cada test
})
class RolRepositoryTest {

    @Autowired
    private RolRepository repo;

    @BeforeEach
    void cleanDatabase() {
        repo.deleteAll();
    }

    @Test
    void givenRolSaved_whenFindByTipoRol_thenReturnRol() {
        // given
        Rol rol = new Rol(null, TipoRol.ADMINISTRADOR);

        // when
        repo.saveAndFlush(rol);

        // then
        assertThat(repo.findByTipoRol(TipoRol.ADMINISTRADOR))
                .isPresent()
                .get()
                .extracting(Rol::getTipoRol)
                .isEqualTo(TipoRol.ADMINISTRADOR);
    }

    @Test
    void givenDuplicateTipoRol_whenSaving_thenThrowConstraintViolation() {
        // given
        repo.saveAndFlush(new Rol(null, TipoRol.EMPLEADO));

        // when / then
        assertThatThrownBy(() ->
                repo.saveAndFlush(new Rol(null, TipoRol.EMPLEADO))
        )
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}