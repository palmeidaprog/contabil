package com.react.contabil.dao;

import javax.validation.constraints.*;
import java.util.Date;
import static com.react.contabil.util.Constantes.FiltroLancamentos.*;
import static com.react.contabil.util.Util.converteParaDiaMesAno;

public class FiltroLancamentos {

    @NotEmpty(message = COD_USUARIO_NOT_EMPY)
    @Digits(integer = 15,
            fraction = 0,
            message = COD_USUARIO_DIGITS)
    private Long codigoUsuario;

    @PastOrPresent(message = DATA_NO_FUTURO)
    @NotNull(message = DATA_INICIO_NOT_NULL)
    private Date dataInicio;

    @PastOrPresent(message = DATA_NO_FUTURO)
    @NotNull(message = DATA_FINAL_NOT_NULL)
    private Date dataFinal;


    public FiltroLancamentos(Long codigoUsuario, Date dataInicio,
                             Date dataFinal) {
        this.codigoUsuario = codigoUsuario;
        this.dataInicio = dataInicio;
        this.dataFinal = dataFinal;
    }

    public Long getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(Long codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }


    public String getInfo() {
        final StringBuilder sb = new StringBuilder();
        sb.append(" código usuário: ").append(this.codigoUsuario)
                .append(" data início: ")
                .append(converteParaDiaMesAno(this.dataInicio))
                .append(" data final: ")
                .append(converteParaDiaMesAno(this.dataFinal));

        return sb.toString();
    }

    @Override
    public String toString() {
        return "FiltroLancamentos " + this.getInfo();
    }


}
