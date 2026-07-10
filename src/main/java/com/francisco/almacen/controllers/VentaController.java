package com.francisco.almacen.controllers;

import com.francisco.almacen.dto.Reporte.ReporteVentasSucursalResponse;
import com.francisco.almacen.dto.ventas.VentaResponse;
import com.francisco.almacen.dto.ventas.VentasRequest;
import com.francisco.almacen.servicies.Venta.VentaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@AllArgsConstructor
@Validated
public class VentaController {

    private final VentaService ventaService;
    @PostMapping
    public ResponseEntity<VentaResponse> registrar(@Valid @RequestBody VentasRequest request) {
        VentaResponse respuesta = ventaService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<VentaResponse> cancelar(
            @PathVariable @Positive(message = "El ID de la venta debe ser positivo") Long id) {
        VentaResponse respuesta = ventaService.cancelar(id);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/activas")
    public ResponseEntity<List<VentaResponse>> listarActivas() {
        return ResponseEntity.ok(ventaService.listarActivas());
    }

    @GetMapping("/canceladas")
    public ResponseEntity<List<VentaResponse>> listarHistoricaCancelas() {
        return ResponseEntity.ok(ventaService.listarHistoricaCancelas());
    }
    @GetMapping("/{id}")
    public ResponseEntity<VentaResponse> obtenerPorIdActive(
            @PathVariable @Positive(message = "El ID de la venta debe ser positivo") Long id) {
        return ResponseEntity.ok(ventaService.obetenerPorIdActive(id));

    }
    @GetMapping("/reporte-sucursales")
    public ResponseEntity<List<ReporteVentasSucursalResponse>> obtenerReportePorSucursal() {
        return ResponseEntity.ok(ventaService.obtenerVentaPorSucursal());
    }
}
