package com.francisco.almacen.enums;

import com.francisco.almacen.exceptions.RecursoNoEncontradoException;
import com.francisco.almacen.utils.StringCustomUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Categoria {

    ALIMENTO,
    HIGIENE,
    JUGUETE,
    ELECTRONICA,
    ROPA,
    ACCESORIO,
    FARMARCIA;

    private final String descripcion;

    public static Categoria obtenerCategoriaPorDescripcion(String descripcion) {
        StringCustomUtils.validarNoVacio(descripcion, "La descripción es requerida");

        String descripcionNormalizada = StringCustomUtils.quitarAcentos(descripcion.trim());

        for (Categoria categoria : values()) {
            if (StringCustomUtils.quitarAcentos(categoria.descripcion).equalsIgnoreCase(descripcionNormalizada))
                return categoria;
            throw new RecursoNoEncontradoException("No existe una categoría con la descripcion: " + descripcion);
        }
    }
}
