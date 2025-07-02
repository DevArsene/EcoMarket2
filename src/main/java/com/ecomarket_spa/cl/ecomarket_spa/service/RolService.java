package com.ecomarket_spa.cl.ecomarket_spa.service;

import com.ecomarket_spa.cl.ecomarket_spa.model.Rol;
import com.ecomarket_spa.cl.ecomarket_spa.model.TipoRol;

import java.util.List;

public interface RolService {
    Rol crearRol(Rol rol);
    Rol buscarPorTipo(TipoRol tipoRol);
    Rol buscarPorId (Long id);
    List<Rol> listarTodos();
}