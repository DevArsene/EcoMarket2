package com.ecomarket_spa.cl.ecomarket_spa.service;

import com.ecomarket_spa.cl.ecomarket_spa.model.Usuario;
import com.ecomarket_spa.cl.ecomarket_spa.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Métodos “nativos” que ya existían
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findByRun(String run) {
        return usuarioRepository.findByRun(run);
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void deleteByRun(String run) {
        usuarioRepository.findByRun(run)
                .ifPresent(usuarioRepository::delete);
    }

    public List<Usuario> findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    // Alias para los tests de integración y controller slices

    /** Para tests que llaman a listarTodos() */
    public List<Usuario> listarTodos() {
        return this.findAll();
    }

    /** Para tests que llaman a crearUsuario(...) */
    public Usuario crearUsuario(Usuario usuario) {
        return this.save(usuario);
    }

    /** Para tests que llaman a buscarPorId(...) */
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id " + id));
    }
}