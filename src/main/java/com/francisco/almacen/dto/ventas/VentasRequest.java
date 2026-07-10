package com.francisco.almacen.dto.ventas;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record VentasRequest(
        @NotNull(message = "El id de la sucursal es requerido")
        @Positive(message = "El id de la sucursal debe ser positivo")
        Long idSucursal,

        @NotEmpty(message = "La lista de productos es requerida y no debe estar vacía")
        List<@Valid DetalleVentaRequest> productos
) {
}
