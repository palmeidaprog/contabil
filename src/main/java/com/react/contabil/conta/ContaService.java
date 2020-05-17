package com.react.contabil.conta;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class ContaService {

    @Size(max = 15, message="O codigo da conta não pode ser maior que {max}")
    @NotBlank(message="O código não pode ser nulo")
    private Long codigo;

    @Size(max = 15, message="O codigo da conta não pode ser maior que {max}")
    private Long contaPaiCodigo;

    @NotBlank(message="O usuário não pode ser nulo")
    private UsuarioService usuario;

    @NotBlank(message="O número não pode ser nulo")
    private String numero;

    @NotBlank(message="O nome da conta não pode ser nulo")
    private String nome;

    private ContaService contaPai;

    private BigDecimal saldo;

    private String descricao;

    public ContaService() { }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public UsuarioService getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioService usuario) {
        this.usuario = usuario;
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

    public ContaService getContaPai() {
        return contaPai;
    }

    public void setContaPai(ContaService contaPai) {
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
