package com.react.contabil.lancamento;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.react.contabil.datalayer.dataobject.LancamentoDO;
import com.react.contabil.usuario.Usuario;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Lancamento {

    @Size(max = 15, message="O codigo do lançamento não pode ser maior que {max}")
    private Long codigo;

    @Size(max = 15, message="O codigo do usuário não pode ser maior que {max}")
    @NotBlank(message="O código do usuário não pode ser nulo")
    private Long codigoUsuario;

    @NotBlank(message="A data não pode ser nula")
    private Date data;

    @NotBlank(message="O histórico não pode ser nulo")
    private String historico;

    private List<Valor> valores;

    private LancamentoDO lancamentoDO;

    public Lancamento() { }

    public Lancamento(LancamentoDO lancamentoDO) {
        this.lancamentoDO = lancamentoDO;
    }

    public LancamentoDO toDataObject() {
        return lancamentoDO;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Long getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(Long usuario) {
        this.codigoUsuario = codigoUsuario;
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
