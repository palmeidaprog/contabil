package com.react.contabil.excecao;

public class LancamentoInvalidoException extends ContabilException {
    public LancamentoInvalidoException() {
    }

    public LancamentoInvalidoException(String message) {
        super(message);
    }

    public LancamentoInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }

    public LancamentoInvalidoException(Throwable cause) {
        super(cause);
    }

    public LancamentoInvalidoException(String message, Throwable cause,
                                       boolean enableSuppression,
                                       boolean writableStackTrace) {

        super(message, cause, enableSuppression, writableStackTrace);
    }
}
