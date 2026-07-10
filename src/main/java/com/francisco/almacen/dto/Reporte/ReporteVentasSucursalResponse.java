package com.francisco.almacen.dto.Reporte;

public interface ReporteVentasSucursalResponse {
        Long getIdSucursal();
        String getNombreSucursal();
        Double getTotalFacturado();
        Long getCantidadProductosVendidos();
    }
