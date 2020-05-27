package com.react.contabil.conta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.react.contabil.datalayer.dataobject.ContaDO;
import com.react.contabil.usuario.Usuario;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Conta {

    @Size(max = 15, message="O codigo da conta não pode ser maior que {max}")
    private Long codigo;

    @Size(max = 15, message="O codigo da conta pai não pode ser maior que {max}")
    private Long contaPaiCodigo;

    @NotNull(message="O código do usuário não pode ser nulo")
    private Long codigoUsuario;

    @NotNull(message="O número da conta não pode ser nulo")
    private String numero;

    @NotNull(message="O nome da conta não pode ser nulo")
    private String nome;

    private BigDecimal saldo;

    private String descricao;

    public Conta() { }

    public Conta(ContaDO contaDO) {
        this.codigo = contaDO.getCodigo();
        this.contaPaiCodigo = contaDO.getContaPaiCodigo();
        this.codigoUsuario = contaDO.getCodigoUsuario();
        this.numero = contaDO.getNumero();
        this.nome = contaDO.getNome();
        this.saldo = contaDO.getSaldo();
        this.descricao = contaDO.getDescricao();
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Long getContaPaiCodigo() {
        return contaPaiCodigo;
    }

    public void setContaPaiCodigo(Long contaPaiCodigo) {
        this.contaPaiCodigo = contaPaiCodigo;
    }

    public Long getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(Long codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getNivelConta() {
        return this.numero.split("\\.").length;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Conta");

        if (this.codigo != null) {
            sb.append(" codigo: ").append(this.codigo);
        }

        return sb.append(" ").append(this.numero).append(" - ")
                  .append(this.nome).toString();
    }

    public ContaDO toDataObject() {
        final ContaDO contaDO = new ContaDO();
        contaDO.setNumero(this.numero);
        contaDO.setSaldo(this.saldo == null ? new BigDecimal(0) :
                this.saldo);
        contaDO.setNome(this.nome);
        contaDO.setCodigo(this.codigo);
        contaDO.setCodigoUsuario(this.codigoUsuario);

        return contaDO;
    }
}
