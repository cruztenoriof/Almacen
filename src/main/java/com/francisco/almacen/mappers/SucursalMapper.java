package com.francisco.almacen.mappers;

import com.francisco.almacen.dto.Sucursales.SucursalResponse;
import com.francisco.almacen.dto.Sucursales.SucursalesRequest;
import com.francisco.almacen.entities.Sucursal;
import org.springframework.stereotype.Component;

@Component
public class SucursalMapper {
    public Sucursal requestAEntidadc(SucursalesRequest request) {
        if (request == null) return null;
        return Sucursal.builder()
                .nombre(request.nombre().trim())
                .direccion(request.direccion().trim())
                .build();
    }

    public SucursalResponse entidadAResponse(Sucursal sucursal) {
        if (sucursal == null) return null;
        return new SucursalResponse(
                sucursal.getId(),
                sucursal.getNombre(),
                sucursal.getDireccion()
        );
    }
}