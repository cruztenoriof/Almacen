package com.francisco.almacen.controllers;

import com.francisco.almacen.dto.productos.SucursalResponse;
import com.francisco.almacen.dto.productos.SucursalesRequest;
import com.francisco.almacen.servicies.Sucursal.SucursalService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
@AllArgsConstructor
@Validated
public class SucursalController {
    private final SucursalService SucursalServices;

    @GetMapping
    public ResponseEntity<List<SucursalResponse>> listar() {
        return ResponseEntity.ok(SucursalServices.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SucursalResponse> obtenerPorId(
            @PathVariable @Positive(message = "El ID deber ser positivo") Long id) {
        return ResponseEntity.ok(SucursalServices.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<SucursalResponse> registrar(@Valid @RequestBody SucursalesRequest requets) {
        return ResponseEntity.status(HttpStatus.CREATED).body(SucursalServices.registrar(requets));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SucursalResponse> actualizar(
            @PathVariable @Positive(message = "El ID deber ser positivo") Long id,
            @Valid @RequestBody SucursalesRequest requets) {
        return ResponseEntity.status(HttpStatus.CREATED).body(SucursalServices.actualizar(requets, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable @Positive(message = "El ID deber ser positivo") Long id) {
        SucursalServices.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
