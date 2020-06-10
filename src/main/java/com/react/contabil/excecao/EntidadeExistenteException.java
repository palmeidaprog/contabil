package com.react.contabil.excecao;

public class EntidadeExistenteException extends ContabilException {
    public EntidadeExistenteException() {
    }

    public EntidadeExistenteException(String message) {
        super(message);
    }

    public EntidadeExistenteException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntidadeExistenteException(Throwable cause) {
        super(cause);
    }

    public EntidadeExistenteException(String message, Throwable cause,
                                      boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
