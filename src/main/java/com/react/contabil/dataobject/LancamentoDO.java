package com.react.contabil.dataobject;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "lancamento")
public class LancamentoDO implements Entidade {
    @Id
    @Column(name = "codigo", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Column(name = "codigo_usuario", updatable = false, nullable = false)
    private Long codigoUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name="codigo_usuario",
        referencedColumnName = "codigo",
        insertable = false,
        updatable = false,
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
        fetch = FetchType.EAGER,
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

    public Long getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(Long codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public List<ValorDO> getValores() {
        return valores;
    }

    public void setValores(List<ValorDO> valores) {
        this.valores = valores;
    }

    @Override
    public String toString() {
        return "Lancamento código: " + this.codigo + " do usuário código: " +
                this.codigoUsuario;
    }
}
