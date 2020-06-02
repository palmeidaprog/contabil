package com.react.contabil.lancamento;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.react.contabil.dataobject.LancamentoDO;
import com.react.contabil.dataobject.ValorDO;
import com.react.contabil.util.Constantes;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Lancamento {

    @Digits(integer = 15,
            fraction = 0,
            message = Constantes.Lancamento.CODIGO_DIGITS)
    private Long codigo;

    @Digits(integer = 15,
            fraction = 0,
            message = Constantes.Lancamento.CODIGO_USUARIO_DIGITS)
    @NotNull(message = Constantes.Lancamento.CODIGO_USUARIO_NOT_NULL)
    private Long codigoUsuario;

    @NotNull(message = Constantes.Lancamento.DATA_NOT_NULL)
    private Date data;

    @NotBlank(message = Constantes.Lancamento.HISTORICO_NOT_BLANK)
    private String historico;

    @NotEmpty(message = Constantes.Lancamento.VALORES_NOT_EMTPY)
    private List<Valor> valores;

    public Lancamento() { }

    public Lancamento(LancamentoDO lancamentoDO) {
        this.codigo = lancamentoDO.getCodigo();
        this.codigoUsuario = lancamentoDO.getCodigoUsuario();
        this.data = lancamentoDO.getData();
        this.historico = lancamentoDO.getHistorico();
        this.valores = new ArrayList<>();
        for (final ValorDO valor : lancamentoDO.getValores()) {
            this.valores.add(new Valor(valor));
        }
    }

    public LancamentoDO toDataObject() {
        final LancamentoDO lancamentoDO = new LancamentoDO();
        lancamentoDO.setHistorico(this.historico);
        lancamentoDO.setData(this.data);
        lancamentoDO.setCodigoUsuario(this.codigoUsuario);
        lancamentoDO.setCodigo(this.codigo);

        final List<ValorDO> valoresDO = new ArrayList<>();
        for (final Valor valor : this.valores) {
            valoresDO.add(valor.toDataObject());
        }
        lancamentoDO.setValores(valoresDO);

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

    public List<Valor> getValores() {
        return valores;
    }

    public void setValores(List<Valor> valores) {
        this.valores = valores;
    }

    @Override
    public String toString() {
        return "Lancamento código: " + this.codigo + " do usuário código: " +
                this.codigoUsuario;
    }
}
