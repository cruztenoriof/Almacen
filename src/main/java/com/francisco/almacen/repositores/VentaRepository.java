package com.francisco.almacen.repositores;

import com.francisco.almacen.dto.Reporte.ReporteVentasSucursalResponse;
import com.francisco.almacen.entities.Venta;
import com.francisco.almacen.enums.EstadoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository <Venta, Long> {

    @Query("SELECT v FROM Venta v WHERE v.estadoVenta = :estado")
    List<Venta> buscarConEstado(@Param("estado") EstadoVenta estado);

    @Query("""
                SELECT 
                    s.id AS idSucursal, 
                    s.nombre AS nombreSucursal, 
                    SUM(d.precioProducto * d.cantidadProducto) AS totalFacturado, 
                    SUM(d.cantidadProducto) AS cantidadProductosVendidos
                FROM Venta v
                JOIN v.sucursal s
                JOIN v.detalleVenta d
                WHERE v.estadoVenta = com.francisco.almacen.enums.EstadoVenta.REGISTRADA
                GROUP BY s.id, s.nombre
            """)
    List<ReporteVentasSucursalResponse> obtenerReporteEconomicoPorSucursal();
}