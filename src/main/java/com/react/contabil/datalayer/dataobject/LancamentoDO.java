package com.react.contabil.datalayer.dataobject;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "lancamento")
public class LancamentoDO implements Entidade {
    @Id
    @Column(name = "codigo", nullable = false)
    private Long codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name="codigo_usuario",
        referencedColumnName = "codigo",
        nullable = false
    )
    private UsuarioDO usuario;

    @Column(name = "data", nullable = false)
    private Date data;

    @Column(name = "historico", nullable = false)
    private String historico;

    @OneToMany(
        mappedBy = "lancamento",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    private List<ValorDO> valores;

    public LancamentoDO() { }

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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }
}
