package com.francisco.almacen.servicies;

import com.francisco.almacen.dto.productos.ProductoRequets;
import com.francisco.almacen.dto.productos.ProductoResponse;
import com.francisco.almacen.entities.Producto;
import com.francisco.almacen.enums.Categoria;
import com.francisco.almacen.exceptions.RecursoNoEncontradoException;
import com.francisco.almacen.mappers.ProductoMapper;
import com.francisco.almacen.repositores.ProductoRepository;
import com.francisco.almacen.servicies.producto.ProductoServices;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ProductoServicelmpl implements ProductoServices {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    @Override
    public List<ProductoResponse> Listar() {
        return List.of();
    }

    @Override
    public ProductoResponse obtenerporID(Long id) {
        return null;
    }

    @Override
    public ProductoResponse registar(ProductoRequets requets) {
        return null;
    }

    @Override
    public ProductoResponse actualizar(ProductoRequets requets, Long id) {
        Producto producto= obtenerProducto0Exceçition(id);
        Categoria categoria= Categoria.obtenerCategoriaPorDescripcion(requets.categoria());
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
        Producto producto= obtenerProducto0Exceçition(id);
        productoRepository.delete(producto);
        log.info("Porducto con id {} eliminado", id);
    }
    private Producto obtenerProducto0Exceçition(Long id){
        log.info("Buscando producto con id: {]", id);
        return  productoRepository.findById(id).orElseThrow(
                ()->new RecursoNoEncontradoException("Producto no encontrado con id: "+id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductoResponse> listar() {
        log.info("Listando todos los productos");

        return productoRepository.findAll().stream()
                .map(productoMapper::entidadAReponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public ProductoResponse obtenerPorId(Long id) {
        return productoMapper.entidadAReponse(obtenerProducto0Exceçition(id));
    }

    @Override
    public ProductoResponse registrar(ProductoRequets requets) {
    log.info("Registrando nuevo producto...");
        Categoria categoria= Categoria.obtenerCategoriaPorDescripcion(requets.categoria());
        Producto producto= productoMapper.requestAEntidad(requets, categoria);
        productoRepository.save(producto);
        log.info("Nuevo producto {} registro", producto.getNombre());

        return productoMapper.entidadAReponse(producto);
    }
}