package com.ecomarket_spa.cl.ecomarket_spa.hateoas;

import com.ecomarket_spa.cl.ecomarket_spa.controller.RolController;
import com.ecomarket_spa.cl.ecomarket_spa.model.Rol;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RolModelAssembler implements RepresentationModelAssembler<Rol, EntityModel<Rol>> {
    @Override
    public EntityModel<Rol> toModel(Rol rol) {
        return EntityModel.of(rol,
                          linkTo(methodOn(RolController.class).getById(rol.getId())).withSelfRel(),
                 linkTo(methodOn(RolController.class).listarTodos()).withRel("roles")
        );
    }
}
