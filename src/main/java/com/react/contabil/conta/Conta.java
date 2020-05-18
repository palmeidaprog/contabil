package com.react.contabil.conta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.react.contabil.usuario.Usuario;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Conta {

    @Size(max = 15, message="O codigo da conta não pode ser maior que {max}")
    private Long codigo;

    @Size(max = 15, message="O codigo da conta não pode ser maior que {max}")
    private Long contaPaiCodigo;

    @NotBlank(message="O número não pode ser nulo")
    private String numero;

    @NotBlank(message="O nome da conta não pode ser nulo")
    private String nome;

    private Conta contaPai;

    private BigDecimal saldo;

    private String descricao;

    public Conta() { }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
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

    public Long getContaPaiCodigo() {
        return contaPaiCodigo;
    }

    public void setContaPaiCodigo(Long contaPaiCodigo) {
        this.contaPaiCodigo = contaPaiCodigo;
    }

    public Conta getContaPai() {
        return contaPai;
    }

    public void setContaPai(Conta contaPai) {
        this.contaPai = contaPai;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
