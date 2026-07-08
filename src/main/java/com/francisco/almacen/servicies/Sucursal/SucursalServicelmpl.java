package com.francisco.almacen.servicies.Sucursal;

import com.francisco.almacen.dto.productos.SucursalResponse;
import com.francisco.almacen.dto.productos.SucursalesRequest;
import com.francisco.almacen.entities.Sucursal;
import com.francisco.almacen.exceptions.RecursoNoEncontradoException;
import com.francisco.almacen.mappers.SucursalMapper;
import com.francisco.almacen.repositores.SucursalRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class SucursalServicelmpl implements SucursalService {
    private final SucursalRepository sucursalRepository;
    private final SucursalMapper sucursalMapper;

    @Transactional(readOnly = true)
    @Override
    public List<SucursalResponse> listar() {
        log.info("Listando todas las sucursales");
        return sucursalRepository.findAll().stream()
                .map(sucursalMapper::entidadAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public SucursalResponse obtenerPorId(Long id) {
        return sucursalMapper.entidadAResponse(obtenerSucursalOException(id));
    }

    @Override
    public SucursalResponse registrar(SucursalesRequest request) {
        log.info("Registrando nueva sucursal: {}", request.nombre());
        validarDatosUnicos(request);
        Sucursal sucursal = sucursalMapper.requestAEntidadc(request);
        sucursalRepository.save(sucursal);
        log.info("Sucursal {} registrada exitosamente", sucursal.getNombre());
        return sucursalMapper.entidadAResponse(sucursal);
    }

    @Override
    public SucursalResponse actualizar(SucursalesRequest request, Long id) {
        Sucursal sucursal = obtenerSucursalOException(id);
        log.info("Actualizando sucursal ID: {}", id);
        validarCambios(request, id);
        sucursal.actualizar(request.nombre(), request.direccion());
        log.info("Sucursal ID {} actualizada", id);
        return sucursalMapper.entidadAResponse(sucursal);
    }

    @Override
    public void eliminar(Long id) {
        Sucursal sucursal = obtenerSucursalOException(id);
        sucursalRepository.delete(sucursal);
        log.info("Sucursal ID {} eliminada", id);
    }

    private Sucursal obtenerSucursalOException(Long id) {
        log.info("Buscando sucursal ID: {}", id);
        return sucursalRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Sucursal no encontrada con ID: " + id));
    }
    private void validarDatosUnicos(SucursalesRequest request){
        if (sucursalRepository.existsByNombreIgnoreCase(request.nombre().trim()))
            throw new IllegalArgumentException("Ya exsite una sucursal con el nombre de: "+ request.nombre());
    }
    private void validarCambios(SucursalesRequest request, Long id){
        if (sucursalRepository.existsByNombreIgnoreCaseAndIdNot(request.nombre().trim(), id))
            throw new IllegalArgumentException("Ya exsite una sucursal con el nombre de: "+ request.nombre());
    }
}
