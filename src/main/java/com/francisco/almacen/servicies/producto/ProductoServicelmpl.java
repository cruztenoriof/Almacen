package com.francisco.almacen.servicies.producto;

import com.francisco.almacen.dto.productos.ProductoRequets;
import com.francisco.almacen.dto.productos.ProductoResponse;
import com.francisco.almacen.entities.Producto;
import com.francisco.almacen.enums.Categoria;
import com.francisco.almacen.exceptions.RecursoNoEncontradoException;
import com.francisco.almacen.mappers.ProductoMapper;
import com.francisco.almacen.repositores.ProductoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ProductoServicelmpl implements ProductoServices {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    @Transactional(readOnly = true)
    @Override
    public List<ProductoResponse> listar(String nombre, String categoria, BigDecimal precioMin, BigDecimal precioMax) {
        log.info("Listando todos los productos");
        return productoRepository.buscarConFiltrosAvanzados(nombre, categoria, precioMin, precioMax)
                .stream()
                .map(productoMapper::entidadAReponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public ProductoResponse obtenerPorId(Long id) {
        return productoMapper.entidadAReponse(obtenerProducto0Exception(id));
    }

    @Override
    public ProductoResponse registrar(ProductoRequets requets) {
        log.info("Registrando nuevo producto...");
        Categoria categoria = Categoria.obtenerCategoriaPorDescripcion(requets.categoria());
        Producto producto = productoMapper.requestAEntidad(requets, categoria);
        productoRepository.save(producto);
        log.info("Nuevo producto {} registrado", producto.getNombre());

        return productoMapper.entidadAReponse(producto);
    }

    @Override
    public ProductoResponse actualizar(ProductoRequets requets, Long id) {
        Producto producto = obtenerProducto0Exception(id);
        Categoria categoria = Categoria.obtenerCategoriaPorDescripcion(requets.categoria());
        log.info("Actualizando producto id: {}", id);

        producto.actualizar(
                requets.nombre(),
                categoria,
                requets.precio(),
                requets.cantidad()
        );
        log.info("Producto con id {} actualizado", id);
        return productoMapper.entidadAReponse(producto);
    }

    @Override
    public void eliminar(Long id) {
        Producto producto = obtenerProducto0Exception(id);
        productoRepository.delete(producto);
        log.info("Producto con id {} eliminado", id);
    }

    private Producto obtenerProducto0Exception(Long id) {
        log.info("Buscando producto con id: {}", id);
        return productoRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Producto no encontrado con id: " + id));

    }
}