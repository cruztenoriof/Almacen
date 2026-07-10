package com.francisco.almacen.enums;

import com.francisco.almacen.exceptions.RecursoNoEncontradoException;
import com.francisco.almacen.utils.StringCustomUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@Getter

public enum EstadoVenta {
    REGISTRADA(1L,"Registrada"),
    CANCELADA(0L, "Cancelada");

    private final Long codigo;
    private final String descripcion;

    public static EstadoVenta obtenerEstadoPorDescripcion(String descripcion) {
        StringCustomUtils.validarNoVacio(descripcion, "La descripción es requerida");

        String descripcionNormalizada = StringCustomUtils.quitarAcentos(descripcion.trim());

        for (EstadoVenta estadoVenta : values()) {
            if (StringCustomUtils.quitarAcentos(estadoVenta.getDescripcion()).equalsIgnoreCase(descripcionNormalizada)) {
                return estadoVenta;
            }
        }
        throw new RecursoNoEncontradoException("No existe un estado de venta con la descripcion: " + descripcion);
    }
    public static EstadoVenta obtenerEstadoPorCodigo(Long codigo) {
        for (EstadoVenta estadoVenta : values()) {
            if (Objects.equals(estadoVenta.codigo, codigo))
                return estadoVenta;
        }
        throw new RecursoNoEncontradoException("No existe un estado de venta con el codigo: " + codigo);
    }
}
