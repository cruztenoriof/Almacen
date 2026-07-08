package com.francisco.almacen.dto.productos;

public record CustomErrorResponse(
        int codigo,
        String mensaje
) {}
