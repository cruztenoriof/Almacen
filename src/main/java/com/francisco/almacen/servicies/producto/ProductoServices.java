package com.francisco.almacen.servicies.producto;

import com.francisco.almacen.dto.productos.ProductoRequets;
import com.francisco.almacen.dto.productos.ProductoResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoServices {
    List<ProductoResponse> listar(
            String nombre, String categoria, BigDecimal precioMin, BigDecimal precioMax);
    ProductoResponse obtenerPorId(Long id);
    ProductoResponse registrar (ProductoRequets requets);
    ProductoResponse actualizar (ProductoRequets requets, Long id);
    void eliminar(Long id);
}
