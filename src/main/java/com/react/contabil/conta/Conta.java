package com.react.contabil.conta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.react.contabil.dao.Saldo;
import com.react.contabil.dataobject.ContaDO;
import com.react.contabil.dataobject.ValorDO;
import com.react.contabil.lancamento.Valor;
import com.react.contabil.util.Util;
import static com.react.contabil.util.Constantes.Conta.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Conta implements Comparable<Conta> {

    @Digits(integer = 15,
            fraction = 0,
            message = COD_DIGITS)
    private Long codigo;

    @Digits(integer = 15,
            fraction = 0,
            message = COD_CONTA_PAI_DIGITS)
    private Long contaPaiCodigo;

    @NotNull(message = COD_USUARIO_NOT_NULL)
    private Long codigoUsuario;


    private String numero;

    @NotNull(message="O nome da conta não pode ser nulo")
    private String nome;

    private Saldo saldo;

    private String descricao;

    private List<Valor> valores;

    public Conta() { }

    public Conta(ContaDO contaDO) {
        this(contaDO, false);
    }

    public Conta(ContaDO contaDO, boolean isFlat) {
        this.codigo = contaDO.getCodigo();
        this.contaPaiCodigo = contaDO.getContaPaiCodigo();
        this.codigoUsuario = contaDO.getCodigoUsuario();
        this.numero = contaDO.getNumero();
        this.nome = contaDO.getNome();
        this.descricao = contaDO.getDescricao();
        this.saldo = contaDO.getSaldo();

        if (contaDO.getValores() != null && !isFlat) {
            this.inicializaValores(contaDO.getValores());
        }
    }

    private void inicializaValores(List<ValorDO> valores) {
        if (this.valores == null) {
            this.valores = new ArrayList<>();
        }

        for (final ValorDO valorDO : valores) {
            this.valores.add(new Valor(valorDO));
        }
    }

    public List<Valor> getValores() {
        return valores;
    }

    public void setValores(List<Valor> valores) {
        this.valores = valores;
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

    public Saldo getSaldo() {
        return saldo;
    }

    public void setSaldo(Saldo saldo) {
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
        contaDO.setNome(this.nome);
        contaDO.setCodigo(this.codigo);
        contaDO.setCodigoUsuario(this.codigoUsuario);
        contaDO.setDescricao(this.descricao);
        contaDO.setContaPaiCodigo(this.contaPaiCodigo);

        return contaDO;
    }

    /**
     * Atualiza o objeto DO
     * @param contaDO
     * @return Objeto atualizado
     */
    public ContaDO update(ContaDO contaDO) {
        if (this.contaPaiCodigo != null) {
            contaDO.setContaPaiCodigo(this.contaPaiCodigo);
        }

        if (Util.isNotBlank(this.numero)) {
            contaDO.setNumero(this.numero);
        }

        if (Util.isNotBlank(this.nome)) {
            contaDO.setNome(this.nome);
        }

        if (Util.isNotBlank(this.descricao)) {
            contaDO.setDescricao(this.descricao);
        }

        return contaDO;
    }

    /**
     * Para arvore red and black ordenar por numero da conta
     */
    @Override
    public int compareTo(Conta o) {
        return this.numero.compareTo(o.getNumero());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Conta)) return false;
        Conta conta = (Conta) o;
        return Objects.equals(codigo, conta.codigo) &&
                Objects.equals(contaPaiCodigo, conta.contaPaiCodigo) &&
                Objects.equals(codigoUsuario, conta.codigoUsuario) &&
                Objects.equals(numero, conta.numero) &&
                Objects.equals(nome, conta.nome) &&
                Objects.equals(saldo, conta.saldo) &&
                Objects.equals(descricao, conta.descricao) &&
                Objects.equals(valores, conta.valores);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, contaPaiCodigo, codigoUsuario, numero, nome, saldo, descricao, valores);
    }
}
