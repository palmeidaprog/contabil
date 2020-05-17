package com.react.contabil.datalayer.usuario;

import javax.persistence.Column;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
}
