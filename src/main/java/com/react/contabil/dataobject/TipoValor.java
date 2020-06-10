package com.react.contabil.dataobject;

public enum TipoValor {
    DEBITO("Débito"),
    CREDITO("Crédito");

    private String tipo;

    TipoValor(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    /**
     * Converte valor em constante
     * @param valor Valor a ser convertido
     * @return Constante que representa o valor (null se não encontrar)
     */
    public static TipoValor of(String valor) {
        for (TipoValor v : TipoValor.values()) {
            if (valor.equalsIgnoreCase(v.getTipo())) {
                return v;
            }
        }
        return null;
    }
}
