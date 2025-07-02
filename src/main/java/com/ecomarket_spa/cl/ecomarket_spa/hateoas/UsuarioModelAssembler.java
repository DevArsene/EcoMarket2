package com.ecomarket_spa.cl.ecomarket_spa.hateoas;

import com.ecomarket_spa.cl.ecomarket_spa.controller.UsuarioController;
import com.ecomarket_spa.cl.ecomarket_spa.model.Usuario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {
    @Override
    public EntityModel<Usuario> toModel(Usuario usuario) {
        
        return EntityModel.of(usuario,
               
                linkTo(methodOn(UsuarioController.class).obtenerPorRun(usuario.getRun())).withSelfRel(),

                
                linkTo(methodOn(UsuarioController.class).obtenerTodos()).withRel("usuarios")
        );
    }
}