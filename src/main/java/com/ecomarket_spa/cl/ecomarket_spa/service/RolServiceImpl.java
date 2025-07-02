package com.ecomarket_spa.cl.ecomarket_spa.service;

import com.ecomarket_spa.cl.ecomarket_spa.model.Rol;
import com.ecomarket_spa.cl.ecomarket_spa.model.TipoRol;
import com.ecomarket_spa.cl.ecomarket_spa.repository.RolRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolServiceImpl implements RolService {

    private final RolRepository repo;

    public RolServiceImpl(RolRepository repo) {
        this.repo = repo;
    }

    @Override
    public Rol crearRol(Rol rol) {
        return repo.save(rol);
    }

    @Override
    public Rol buscarPorId(Long id) {                                // ← nuevo
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con id " + id));
    }


    @Override
    public Rol buscarPorTipo(TipoRol tipoRol) {
        return repo.findByTipoRol(tipoRol)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado"));
    }

    @Override
    public List<Rol> listarTodos() {
        return repo.findAll();
    }
}