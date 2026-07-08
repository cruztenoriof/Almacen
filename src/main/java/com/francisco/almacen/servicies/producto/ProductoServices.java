package com.francisco.almacen.servicies.producto;

import com.francisco.almacen.dto.productos.ProductoRequets;
import com.francisco.almacen.dto.productos.ProductoResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductoServices {
    List<ProductoResponse> Listar();
    ProductoResponse obtenerporID(Long id);
    ProductoResponse registar (ProductoRequets requets);
    ProductoResponse actualizar (ProductoRequets requets, Long id);

    void eliminar(Long id);

    @Transactional(readOnly = true)
    List<ProductoResponse> listar();

    @Transactional(readOnly = true)
    ProductoResponse obtenerPorId(Long id);

    ProductoResponse registrar(ProductoRequets requets);
}
