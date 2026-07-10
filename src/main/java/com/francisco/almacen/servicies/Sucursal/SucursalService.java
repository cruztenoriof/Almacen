package com.francisco.almacen.servicies.Sucursal;

import com.francisco.almacen.dto.Sucursales.SucursalResponse;
import com.francisco.almacen.dto.Sucursales.SucursalesRequest;

import java.util.List;

public interface SucursalService {
        List<SucursalResponse> listar();
        SucursalResponse obtenerPorId(Long id);
        SucursalResponse registrar(SucursalesRequest request);
        SucursalResponse actualizar(SucursalesRequest request, Long id);

        void eliminar(Long id);
    }