package com.francisco.almacen.mappers;

import com.francisco.almacen.dto.productos.ProductoRequets;
import com.francisco.almacen.dto.productos.ProductoResponse;
import com.francisco.almacen.entities.Producto;
import com.francisco.almacen.enums.Categoria;
import org.springframework.stereotype.Component;

@Component
public @interface ProductoMapper {

    public Producto requestAEntidad(ProductoRequets requets, Categoria categoria){
        if (requets=null) return null;
        return Producto.builder ()
                .nombre(requets.nombre().trim())
                .categoria(categoria)
                .precio (requets.precio())
                .cantidad(requets.cantidad())
                .build();
    }
    public ProductoResponse entidadAReponse(Producto producto){
        if (producto=null) return null;

        return new ProductoResponse(
                producto.getId(),
                producto.getNombre (),
                producto.getCategoria().getDescrpcion(),
                producto.getPrecio(),
                producto.getCantidad ()
        );
    }
}
