package com.react.contabil.datalayer.dataobject;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "usuario")
public class UsuarioDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo", nullable = false)
    private Long codigo;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "sobrenome")
    private String sobrenome;

    @Column(name = "congelado", columnDefinition = "BIT")
    private boolean congelado;

    @Column(
        name = "login",
        nullable = false,
        columnDefinition = "CHAR(16)",
        unique = true
    )
    private String login;

    @OneToMany(
        mappedBy = "usuario",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        orphanRemoval = false
    )
    private List<ContaDO> contas;

    public UsuarioDO() { }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public boolean isCongelado() {
        return congelado;
    }

    public void setCongelado(boolean congelado) {
        this.congelado = congelado;
    }

    public List<ContaDO> getContas() {
        return contas;
    }

    public void setContas(List<ContaDO> contas) {
        this.contas = contas;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
