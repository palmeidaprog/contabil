package com.react.contabil.datalayer.dataobject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "valor")
public class ValorDO implements Entidade {
    @Id
    @Column(name = "codigo", nullable = false)
    private Long codigo;

    @Column(name = "tipo", nullable = false, columnDefinition = "char(7)")
    private String tipo;

    @Column(name = "valor", nullable = false, columnDefinition = "float(18,2)")
    private BigDecimal valor;

    @Column(name = "saldo_conta", nullable = false, columnDefinition = "float(18,2)")
    private BigDecimal saldoConta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "lancamento_codigo",
        referencedColumnName = "codigo",
        nullable = false
    )
    private LancamentoDO lancamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "conta_codigo",
        referencedColumnName = "codigo",
        nullable = false
    )
    private ContaDO conta;

    public ValorDO() {  }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getSaldoConta() {
        return saldoConta;
    }

    public void setSaldoConta(BigDecimal saldoConta) {
        this.saldoConta = saldoConta;
    }

    public LancamentoDO getLancamento() {
        return lancamento;
    }

    public void setLancamento(LancamentoDO lancamento) {
        this.lancamento = lancamento;
    }

    public ContaDO getConta() {
        return conta;
    }

    public void setConta(ContaDO conta) {
        this.conta = conta;
    }
}
