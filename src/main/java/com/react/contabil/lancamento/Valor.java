package com.react.contabil.lancamento;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.react.contabil.dataobject.ContaDO;
import com.react.contabil.dataobject.LancamentoDO;
import com.react.contabil.dataobject.TipoValor;
import com.react.contabil.dataobject.ValorDO;
import com.react.contabil.util.Constantes;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import static com.react.contabil.util.Constantes.Valor.*;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Valor {

    @Digits(integer = 15,
            fraction = 0,
            message = CODIGO_DIGITS)
    private Long codigo;

    @NotNull(message = TIPO_NOT_NULL)
    private TipoValor tipo;

    @Digits(integer = 15,
            fraction = 0,
            message = CODIGO_CONTA_DIGITS)
    @NotNull(message = CODIGO_CONTA_NOT_NULL)
    private Long codigoConta;

    private Date data;

    private String historico;

//    @NotNull(message = Constantes.Valor.SALDO_CONTA_NOT_NULL)
    private BigDecimal saldoConta;

    @NotNull(message = SALDO_CONTA_NOT_NULL)
    private BigDecimal valor;

    @Digits(integer = 15,
            fraction = 0,
            message = Constantes.Valor.CODIGO_LANCAMENTO_DIGITS)
    @NotNull(message = Constantes.Valor.CODIGO_LANCAMENTO_NOT_NULL)
    private Long codigoLancamento;

    public Valor() {  }

    public Valor(ValorDO valorDO) {
        this.codigo = valorDO.getCodigo();
        this.tipo = valorDO.getTipo();
        this.codigoConta = valorDO.getCodigoConta();
        this.saldoConta = valorDO.getSaldoConta();
        this.codigoLancamento = valorDO.getCodigoLancamento();

        if (valorDO.getLancamento() != null) {
            this.historico = valorDO.getLancamento().getHistorico();
            this.data = valorDO.getLancamento().getData();
        }

        this.valor = valorDO.getValor();
    }

    public Valor(ValorDO valorDO, LancamentoDO lancamentoDO) {
        this.codigo = valorDO.getCodigo();
        this.tipo = valorDO.getTipo();
        this.codigoConta = valorDO.getConta().getCodigo();
        this.saldoConta = valorDO.getSaldoConta();
        this.codigoLancamento = valorDO.getCodigoLancamento();
        this.historico = lancamentoDO.getHistorico();
        this.data = lancamentoDO.getData();
        this.valor = valorDO.getValor();
    }

    /**
     * Converte do DTO para o objeto da persistencia
     * @return Objeto da persistencia
     */
    public ValorDO toDataObject() {
        final ValorDO valorDO = new ValorDO();
        valorDO.setCodigoLancamento(this.codigoLancamento);
        valorDO.setSaldoConta(this.saldoConta);
        valorDO.setCodigoConta(this.codigoConta);

        valorDO.setTipo(this.tipo);
        valorDO.setCodigo(this.codigo);
        valorDO.setValor(this.valor);

        return valorDO;
    }

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

    public Long getCodigoConta() {
        return codigoConta;
    }

    public void setCodigoConta(Long codigoConta) {
        this.codigoConta = codigoConta;
    }

    public BigDecimal getSaldoConta() {
        return saldoConta;
    }

    public void setSaldoConta(BigDecimal saldoConta) {
        this.saldoConta = saldoConta;
    }

    public Long getCodigoLancamento() {
        return codigoLancamento;
    }

    public void setCodigoLancamento(Long codigoLancamento) {
        this.codigoLancamento = codigoLancamento;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Valor código: ");
        sb.append(this.codigo).append(" tipo: ").append(this.tipo.getTipo())
          .append(" do lançamento código: ").append(this.codigoLancamento);
        return sb.toString();
    }
}
