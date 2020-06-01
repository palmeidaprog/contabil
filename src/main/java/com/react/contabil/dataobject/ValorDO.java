package com.react.contabil.dataobject;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "valor")
public class ValorDO implements Entidade {
    @Id
    @Column(name = "codigo", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Column(name = "tipo", nullable = false, columnDefinition = "char(7)")
    @Enumerated(EnumType.STRING)
    private TipoValor tipo;

    @Column(name = "valor", nullable = false, columnDefinition = "float(18,2)")
    private BigDecimal valor;

    @Column(name = "saldo_conta", nullable = false, columnDefinition = "float(18,2)")
    private BigDecimal saldoConta;

    @Column(name = "lancamento_codigo", nullable = false)
    private Long codigoLancamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "lancamento_codigo",
        referencedColumnName = "codigo",
        insertable = false,
        updatable = false
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

    public TipoValor getTipo() {
        return tipo;
    }

    public void setTipo(TipoValor tipo) {
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

    public Long getCodigoLancamento() {
        return codigoLancamento;
    }

    public void setCodigoLancamento(Long codigoLancamento) {
        this.codigoLancamento = codigoLancamento;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Valor código: ");
        sb.append(this.codigo).append(" tipo: ").append(this.tipo.getTipo())
                .append(" do lançamento código: ").append(this.codigoLancamento);
        return sb.toString();
    }
}
