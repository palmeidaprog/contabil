package com.react.contabil.dao;

import com.react.contabil.dataobject.TipoValor;

import java.math.BigDecimal;

public class Saldo {
    private Long codigoConta;

    private BigDecimal totalDebito;

    private BigDecimal totalCredito;

    private TipoValor tipoSaldo;

    private BigDecimal saldo;

    public Saldo(BigDecimal totalDebito, BigDecimal totalCredito) {
        this.totalDebito = totalDebito;
        this.totalCredito = totalCredito;
        this.calculaSaldo();
    }

    public Saldo() {
        // Construtor padrao
    }

    /**
     * Calcula o tipo e o valor do saldo
     */
    public void calculaSaldo() {
        if (this.totalDebito.compareTo(this.totalCredito) >= 0) {
            this.tipoSaldo = TipoValor.DEBITO;
            this.saldo = this.totalDebito.subtract(this.totalCredito);
        } else {
            this.tipoSaldo = TipoValor.CREDITO;
            this.saldo = this.totalCredito.subtract(this.totalDebito);
        }
    }

    public Long getCodigoConta() {
        return codigoConta;
    }

    public void setCodigoConta(Long codigoConta) {
        this.codigoConta = codigoConta;
    }

    public BigDecimal getTotalDebito() {
        return totalDebito;
    }

    public void setTotalDebito(BigDecimal totalDebito) {
        this.totalDebito = totalDebito;
    }

    public BigDecimal getTotalCredito() {
        return totalCredito;
    }

    public void setTotalCredito(BigDecimal totalCredito) {
        this.totalCredito = totalCredito;
    }

    public TipoValor getTipoSaldo() {
        return tipoSaldo;
    }

    public void setTipoSaldo(TipoValor tipoSaldo) {
        this.tipoSaldo = tipoSaldo;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
