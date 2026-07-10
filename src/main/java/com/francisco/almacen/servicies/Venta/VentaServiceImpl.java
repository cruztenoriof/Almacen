package com.francisco.almacen.servicies.Venta;

import com.francisco.almacen.dto.Reporte.ReporteVentasSucursalResponse;
import com.francisco.almacen.dto.Sucursales.SucursalResponse;
import com.francisco.almacen.dto.ventas.DetalleVentaRequest;
import com.francisco.almacen.dto.ventas.DetalleVentaResponse;
import com.francisco.almacen.dto.ventas.VentaResponse;
import com.francisco.almacen.dto.ventas.VentasRequest;
import com.francisco.almacen.entities.Producto;
import com.francisco.almacen.entities.Sucursal;
import com.francisco.almacen.entities.Venta;
import com.francisco.almacen.entities.DetalleVenta;
import com.francisco.almacen.enums.EstadoVenta;
import com.francisco.almacen.exceptions.RecursoNoEncontradoException;
import com.francisco.almacen.repositores.ProductoRepository;
import com.francisco.almacen.repositores.SucursalRepository;
import com.francisco.almacen.repositores.VentaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listarActivas() {
        log.info("Listando todas las ventas activas con estado REGISTRADA");
        return ventaRepository.buscarConEstado(EstadoVenta.REGISTRADA).stream()
                .map(this::convertirAEntityResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listarHistoricaCancelas() {
        log.info("Listando el histórico de ventas CANCELADAS");
        return ventaRepository.buscarConEstado(EstadoVenta.CANCELADA).stream()
                .map(this::convertirAEntityResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VentaResponse obetenerPorIdActive(Long id) {
        log.info("Buscando venta con ID: {}", id);
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada con ID: " + id));
        return convertirAEntityResponse(venta);
    }

    @Override
    @Transactional
    public VentaResponse registrar(VentasRequest request) {
        log.info("Iniciando el registro de una nueva venta");
        Sucursal sucursalReal = sucursalRepository.findById(request.idSucursal())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Sucursal no encontrada con ID: " + request.idSucursal()));

        Venta ventaCompleta = new Venta(
                null, EstadoVenta.REGISTRADA, LocalDate.now(), sucursalReal, new ArrayList<>()
        );

        for (DetalleVentaRequest prodRequest : request.productos()) {
            Producto producto = productoRepository.findById(prodRequest.idProducto())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Producto no encontrado con ID: " + prodRequest.idProducto()));
            producto.descontarCantidad(prodRequest.cantidadProducto());
            productoRepository.save(producto);

            BigDecimal precioFotografia = producto.getPrecio();

            DetalleVenta detalle = DetalleVenta.builder()
                    .venta(ventaCompleta)
                    .producto(producto)
                    .cantidadProducto(prodRequest.cantidadProducto())
                    .precioProducto(precioFotografia)
                    .build();

            ventaCompleta.agregarDetalle(detalle);
        }

        Venta ventaGuardada = ventaRepository.save(ventaCompleta);
        log.info("Venta registrada de forma atómica con ID: {}", ventaGuardada.getId());
        return convertirAEntityResponse(ventaGuardada);
    }

    @Override
    @Transactional
    public VentaResponse cancelar(Long id) {
        log.info("Cancelando la venta con ID: {}", id);

        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada con ID: " + id));
        venta.cancelar();

        for (DetalleVenta detalle : venta.getDetalleVenta()) {
            Producto producto = detalle.getProducto();
            producto.aumentarCantidad(detalle.getCantidadProducto());
            productoRepository.save(producto);
        }

        Venta ventaCancelada = ventaRepository.save(venta);
        log.info("Venta ID: {} cancelada correctamente. Stock devuelto.", id);
        return convertirAEntityResponse(ventaCancelada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteVentasSucursalResponse> obtenerVentaPorSucursal() {
        log.info("Generando reporte economico agregado por sucursales."); // 🔥 Corregido log.info
        return ventaRepository.obtenerReporteEconomicoPorSucursal();
    }

    private VentaResponse convertirAEntityResponse(Venta venta) {
        BigDecimal totalCalculado = BigDecimal.ZERO;
        List<DetalleVentaResponse> detallesDTO = new ArrayList<>();

        if (venta.getDetalleVenta() != null) {
            detallesDTO = venta.getDetalleVenta().stream()
                    .map(d -> {
                        Long idProd = (d.getProducto() != null) ? d.getProducto().getId() : null;
                        String nomProd = (d.getProducto() != null) ? d.getProducto().getNombre() : "Producto No Definido";
                        Integer cantidad = (d.getCantidadProducto() != null) ? d.getCantidadProducto() : 0;
                        BigDecimal precio = (d.getPrecioProducto() != null) ? d.getPrecioProducto() : BigDecimal.ZERO;
                        BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(cantidad));

                        return new DetalleVentaResponse(idProd, nomProd, cantidad, precio, subtotal);
                    }).toList();

            for (DetalleVentaResponse det : detallesDTO) {
                totalCalculado = totalCalculado.add(det.subtotal());
            }
        }
        SucursalResponse sucursalDTO = null;
        if (venta.getSucursal() != null) {
            sucursalDTO = new SucursalResponse(
                    venta.getSucursal().getId(),
                    venta.getSucursal().getNombre(),
                    venta.getSucursal().getDireccion()
            );
        }

        String fechaStr = (venta.getFecha() != null) ? venta.getFecha().toString() : "Sin fecha asignada";
        String estadoStr = (venta.getEstadoVenta() != null) ? venta.getEstadoVenta().name() : "DESCONOCIDO";

        return new VentaResponse(
                venta.getId(),
                fechaStr,
                estadoStr,
                sucursalDTO,
                detallesDTO,
                totalCalculado
        );
    }
}