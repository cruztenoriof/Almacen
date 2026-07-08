package com.francisco.almacen.controllers;

import com.francisco.almacen.dto.productos.ProductoRequets;
import com.francisco.almacen.dto.productos.ProductoResponse;
import com.francisco.almacen.servicies.producto.ProductoServices;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@AllArgsConstructor

public class ProductoController {
    private final ProductoServices productoServices;

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listar() {
        return ResponseEntity.ok(productoServices.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerPorId(
            @PathVariable @Positive(message = "El ID deber ser positivo") Long id) {
        return ResponseEntity.ok(productoServices.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> registrar(@Valid @RequestBody ProductoRequets requets) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoServices.registrar(requets));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(
            @PathVariable @Positive(message = "El ID deber ser positivo") Long id,
            @Valid @RequestBody ProductoRequets requets) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoServices.actualizar(requets, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable @Positive(message = "El ID deber ser positivo") Long id) {
        productoServices.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}