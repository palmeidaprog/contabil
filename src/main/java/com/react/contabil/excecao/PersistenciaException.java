package com.react.contabil.excecao;

import com.react.contabil.excecao.ContabilException;

public class PersistenciaException extends ContabilException {
    public PersistenciaException() {
    }

    public PersistenciaException(String message) {
        super(message);
    }

    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenciaException(Throwable cause) {
        super(cause);
    }

    public PersistenciaException(String message, Throwable cause,
                                 boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
