package com.francisco.almacen.dto.Sucursales;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SucursalesRequest(
        @NotBlank(message = "El nombre de la sucursal es obligatorio")
        String nombre,
        @Size(min = 5, max = 30, message = "El nombre es requerido y debe tener entre 5 y 30 caracteres")
        @NotBlank(message = "La dirección es obligatoria")
        String direccion
) {
}
