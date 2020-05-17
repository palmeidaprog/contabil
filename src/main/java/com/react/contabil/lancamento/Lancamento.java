package com.react.contabil.lancamento;

import com.react.contabil.usuario.Usuario;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

public class Lancamento {

    @Size(max = 15, message="O codigo da conta não pode ser maior que {max}")
    @NotBlank(message="O código não pode ser nulo")
    private Long codigo;

    @NotBlank(message="O usuário não pode ser nulo")

    private Usuario usuario;

    @NotBlank(message="A data não pode ser nula")
    private Date data;

    @NotBlank(message="O histórico não pode ser nulo")
    private String historico;

    private List<Valor> valores;

    public Lancamento() { }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
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
