package com.react.contabil.excecao;

public class EntidadeNaoEncontradaException extends ContabilException {
    public EntidadeNaoEncontradaException() {
    }

    public EntidadeNaoEncontradaException(String message) {
        super(message);
    }

    public EntidadeNaoEncontradaException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntidadeNaoEncontradaException(Throwable cause) {
        super(cause);
    }

    public EntidadeNaoEncontradaException(String message, Throwable cause,
                                          boolean enableSuppression,
                                          boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
