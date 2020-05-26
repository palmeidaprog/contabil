package com.react.contabil.excecao;

public class EntitadeNaoRemovivelException extends ContabilException {
    public EntitadeNaoRemovivelException() {
    }

    public EntitadeNaoRemovivelException(String message) {
        super(message);
    }

    public EntitadeNaoRemovivelException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntitadeNaoRemovivelException(Throwable cause) {
        super(cause);
    }

    public EntitadeNaoRemovivelException(String message, Throwable cause,
                                         boolean enableSuppression,
                                         boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
