package com.react.contabil.lancamento;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.react.contabil.conta.Conta;
import com.react.contabil.datalayer.dataobject.ValorDO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Valor {

    @Size(max = 15, message="O codigo do valor não pode ser maior que {max}")
    private Long codigo;

    @NotBlank(message="O tipo não pode ser nulo")
    private String tipo;

    @Size(max = 15, message="O codigo da conta não pode ser maior que {max}")
    @NotBlank(message="O codigo da conta não pode ser nulo")
    private Long codigoConta;

    @NotBlank(message="O saldo da conta não pode ser nulo")
    private BigDecimal saldoConta;

    @Size(max = 15, message="O codigo do lançamento não pode ser maior que {max}")
    @NotBlank(message="O lançamento não pode ser nulo")
    private Long codigoLancamento;

    private ValorDO valorDO;

    public Valor() {  }

    public Valor(ValorDO valorDO) {
        this.valorDO = valorDO;
    }

    public ValorDO toDataObject() {
        return valorDO;
    }

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

    public Long getCodigoConta() {
        return codigoConta;
    }

    public void setCodigoConta(Long conta) {
        this.codigo = codigoConta;
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

}
