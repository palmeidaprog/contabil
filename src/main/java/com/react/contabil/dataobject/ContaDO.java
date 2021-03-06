package com.react.contabil.dataobject;

import com.react.contabil.dao.Saldo;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conta")
public class ContaDO implements Entidade {
    @Id
    @Column(name = "codigo", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "codigo_usuario",
            referencedColumnName = "codigo",
            nullable = false,
            insertable =  false,
            updatable = false
    )
    private UsuarioDO usuario;

    @Column(name = "codigo_usuario", nullable = false)
    private Long codigoUsuario;

    @Column(name = "numero", nullable = false)
    private String numero;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "codigo_conta_pai")
    private Long contaPaiCodigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name="codigo_conta_pai",
        referencedColumnName="codigo",
        insertable = false,
        updatable = false
    )
    private ContaDO contaPai;

    @OneToMany(
        mappedBy = "conta",
        fetch = FetchType.LAZY
    )
    private List<ValorDO> valores;

    @OneToMany(
        mappedBy = "contaPai",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @OrderBy("numero")
    private List<ContaDO> contasFilhas;

    @Transient
    private Saldo saldo;

    @Column(name = "descricao", columnDefinition = "TEXT(500)")
    private String descricao;

    public ContaDO() {
        this.inicializaValoresPadroes();
    }

    public ContaDO(Long codigoUsuario, String nome, String numero) {
        this.codigoUsuario = codigoUsuario;
        this.numero = numero;
        this.nome = nome;
    }

    public ContaDO(Long codigo, Long codigoUsuario, String nome, String numero) {
        this();
        this.codigo = codigo;
        this.codigoUsuario = codigoUsuario;
        this.numero = numero;
        this.nome = nome;
    }

    public ContaDO(Long codigo, Long codigoUsuario, String nome, String numero, Long contaPaiCodigo) {
        this(codigo, codigoUsuario, nome, numero);
        this.contaPaiCodigo = contaPaiCodigo;
    }

    public ContaDO(Long codigoUsuario, String nome, String numero, Long contaPaiCodigo) {
        this(codigoUsuario, nome, numero);
        this.contaPaiCodigo = contaPaiCodigo;
    }

    @PrePersist
    @PreUpdate
    private void inicializaValoresPadroes() {
        //this.saldo = this.saldo == null ? BigDecimal.valueOf(0) : this.saldo;
    }

    public List<ContaDO> getContasFilhas() {
        return contasFilhas;
    }

    public void setContasFilhas(List<ContaDO> contasFilhas) {
        this.contasFilhas = contasFilhas;
    }

    /**
     * Adiciona conta como filha
     * @param conta Conta a ser adicionada
     */
    public void adicionaContaFilha(ContaDO conta) {
        if (this.contasFilhas == null) {
            this.contasFilhas = new ArrayList<>();
        }

        if (this.contaPaiCodigo == null) {
            conta.setContaPaiCodigo(this.codigo);
        }
        this.contasFilhas.add(conta);
    }

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

    public Saldo getSaldo() {
        return saldo;
    }

    public void setSaldo(Saldo saldo) {
        this.saldo = saldo;
    }

    public List<ValorDO> getValores() {
        return valores;
    }

    public void setValores(List<ValorDO> valores) {
        this.valores = valores;
    }

    public Long getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(Long codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public int getNivelConta() {
        return this.numero.split("\\.").length;
    }

    @Override
    public String toString() {
        return "Conta codigo: " + this.codigo + " " + this.numero + " - " +
                this.nome;
    }
}
