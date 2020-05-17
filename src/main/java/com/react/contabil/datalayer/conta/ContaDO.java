package com.react.contabil.datalayer.conta;

import com.react.contabil.datalayer.usuario.UsuarioDO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "conta")
public class ContaDO {
    @Id
    @Column(name = "codigo", nullable = false)
    private Long codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "codigo_usuario",
            referencedColumnName = "codigo",
            nullable = false
    )
    private UsuarioDO usuario;

    @Column(name = "numero", nullable = false)
    private String numero;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "codigo_conta_pai")
    private Long contaPaiCodigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name="conta_conta_pai",
        referencedColumnName="codigo",
        insertable = false,
        updatable = false
    )
    private ContaDO contaPai;

    @Column(name = "descricao")
    private String descricao;

    public ContaDO() { }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public UsuarioDO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDO usuario) {
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

    public ContaDO getContaPai() {
        return contaPai;
    }

    public void setContaPai(ContaDO contaPai) {
        this.contaPai = contaPai;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
