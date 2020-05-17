package com.react.contabil.excecao;

public class ContabilException extends Exception {

    public ContabilException() {
    }

    public ContabilException(String message) {
        super(message);
    }

    public ContabilException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContabilException(Throwable cause) {
        super(cause);
    }

    public ContabilException(String message, Throwable cause,
                             boolean enableSuppression,
                             boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
