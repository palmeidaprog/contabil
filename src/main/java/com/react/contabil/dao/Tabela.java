package com.react.contabil.dao;

public enum Tabela {
    USUARIO("usuario"),
    CONTA("conta"),
    VALOR("valor"),
    LANCAMENTO("lancamento");

    private String tabela;

    Tabela(String tabela) {
        this.tabela = tabela;
    }

    public String getTabela() {
        return tabela;
    }
}
