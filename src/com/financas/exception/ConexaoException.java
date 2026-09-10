package com.financas.exception;

import java.io.Serial;

public class ConexaoException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ConexaoException(String message) {
        super(message);
    }
}
