package com.react.contabil.excecao;

public class BancoDadosException extends ContabilException {

    public BancoDadosException() {
    }

    public BancoDadosException(String message) {
        super(message);
    }

    public BancoDadosException(String message, Throwable cause) {
        super(message, cause);
    }

    public BancoDadosException(Throwable cause) {
        super(cause);
    }

    public BancoDadosException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
