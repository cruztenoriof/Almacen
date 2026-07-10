package com.francisco.almacen.servicies.Venta;

import com.francisco.almacen.dto.Reporte.ReporteVentasSucursalResponse;
import com.francisco.almacen.dto.ventas.VentaResponse;
import com.francisco.almacen.dto.ventas.VentasRequest;

import java.util.List;

public interface VentaService {

    List<VentaResponse> listarActivas();

    List<VentaResponse> listarHistoricaCancelas();

    VentaResponse obetenerPorIdActive (Long id);

    VentaResponse registrar (VentasRequest request);

    VentaResponse cancelar(Long id);

    List<ReporteVentasSucursalResponse> obtenerVentaPorSucursal ();
}
