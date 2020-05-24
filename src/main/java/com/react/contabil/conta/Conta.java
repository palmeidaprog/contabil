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

    @Size(max = 15, message="O codigo da conta pai não pode ser maior que {max}")
    private Long contaPaiCodigo;

    @NotBlank(message="O código do usuário não pode ser nulo")
    private Long codigoUsuario;

    @NotBlank(message="O número da conta não pode ser nulo")
    private String numero;

    @NotBlank(message="O nome da conta não pode ser nulo")
    private String nome;

    private BigDecimal saldo;

    private String descricao;

    public Conta() { }

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


}
