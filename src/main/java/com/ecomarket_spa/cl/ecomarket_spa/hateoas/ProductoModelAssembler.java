package com.ecomarket_spa.cl.ecomarket_spa.hateoas;

import com.ecomarket_spa.cl.ecomarket_spa.controller.ProductoController;
import com.ecomarket_spa.cl.ecomarket_spa.model.Producto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<Producto, EntityModel<Producto>> {
    @Override
    public EntityModel<Producto> toModel(Producto producto) {
        return EntityModel.of(producto,
                linkTo(methodOn(ProductoController.class).buscar(producto.getEan())).withSelfRel(),
                linkTo(methodOn(ProductoController.class).listar()).withRel("productos")
        );
    }
}
