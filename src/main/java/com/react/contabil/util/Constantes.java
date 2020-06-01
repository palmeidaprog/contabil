package com.react.contabil.util;

import javax.ws.rs.core.MediaType;

public interface Constantes {
    String APPLICATION_JSON_UTF8 =  MediaType.APPLICATION_JSON +
            ";charset=utf8";

    interface Usuario {
        String NOT_NULL = "Usuário não pode ser vazio ou nulo";
    }

    interface Lancamento {
        String NOT_NULL = "Lançamento não pode ser nulo";

        String CODIGO_DIGITS = "O codigo do lançamento não pode ter mais de 15 " +
                "digitos";

        String CODIGO_USUARIO_DIGITS = "O codigo do usuario do lançamento" +
                " não pode ter mais de 15 digitos";

        String CODIGO_USUARIO_NOT_NULL = "O código do usuário não pode" +
                " ser nulo";

        String VALORES_NOT_EMTPY = "O codigo do lançamento não pode " +
                "ter mais de 15 digitos";

        String DATA_NOT_NULL = "A data do lançamento não pode ser nula";

        String HISTORICO_NOT_BLANK = "O histórico não pode ser nulo";
    }

    interface Valor {
        String CODIGO_DIGITS = "O codigo do valor não pode ter mais que " +
                "15 digitos";

        String TIPO_NOT_NULL = "O tipo do valor não pode ser nulo";

        String CODIGO_CONTA_DIGITS =  "O codigo da conta não pode ter" +
                " mais que 15 digitos";

        String CODIGO_CONTA_NOT_NULL = "O codigo da conta não pode " +
                "ser nulo";

        String SALDO_CONTA_NOT_NULL = "O saldo da conta não pode ser nulo";

        String CODIGO_LANCAMENTO_DIGITS = "O codigo do lançamento não " +
                "pode ter mais que 15 digitos";

        String CODIGO_LANCAMENTO_NOT_NULL = "O código do lançamento não" +
                " pode ser nulo";

    }

}
