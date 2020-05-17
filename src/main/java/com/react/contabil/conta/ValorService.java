package com.react.contabil.conta;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class ValorService {

    @Size(max = 15, message="O codigo da conta não pode ser maior que {max}")
    @NotBlank(message="O código não pode ser nulo")
    private Long codigo;

    @NotBlank(message="O tipo não pode ser nulo")
    private String tipo;

    @Size(max = 15, message="O codigo do valor não pode ser maior que {max}")
    @NotBlank(message="O valor não pode ser nulo")
    private Long codigoValor;

    @NotBlank(message="O saldo da conta não pode ser nulo")
    private BigDecimal saldoConta;

    @Size(max = 15, message="O codigo do lançamento não pode ser maior que {max}")
    @NotBlank(message="O lançamento não pode ser nulo")
    private Long codigoLancamento;

    private ContaService conta;

    public ValorService() {  }

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

    public Long getCodigoValor() {
        return codigoValor;
    }

    public void setCodigoValor(Long codigoValor) {
        this.codigoValor = codigoValor;
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

    public ContaService getConta() {
        return conta;
    }

    public void setConta(ContaService conta) {
        this.conta = conta;
    }
}
