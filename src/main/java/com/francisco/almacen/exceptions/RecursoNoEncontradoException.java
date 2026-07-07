package com.francisco.almacen.exceptions;

public class RecursoNoEncontradoException extends RuntimeException {
    public void RecursoNoEncontrado(String message) {
        super(message);
    }
}
