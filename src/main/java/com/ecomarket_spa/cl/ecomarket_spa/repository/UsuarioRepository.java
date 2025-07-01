package com.ecomarket_spa.cl.ecomarket_spa.repository;

import com.ecomarket_spa.cl.ecomarket_spa.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByRun(String run);

    // --- MÉTODO NUEVO ---
    // Spring Data JPA creará la consulta automáticamente a partir del nombre del método.
    List<Usuario> findByCorreo(String correo);
}