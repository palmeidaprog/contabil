package com.react.contabil.util;

import javax.ws.rs.core.MediaType;

public interface Constantes {
    String APPLICATION_JSON_UTF8 =  MediaType.APPLICATION_JSON +
            ";charset=utf8";

    interface Usuario {
        String NOT_NULL = "Usuário não pode ser vazio ou nulo";
    }

    interface Conta {
        String COD_DIGITS = "O código da conta não pode ter mais de 15 digitos";


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
        String LISTA_NOT_EMPTY = "Lista de Valores não pode ser nula ou vazia";

        String NOT_NULL = "O valor não pode ser nulo";

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

    interface FiltroLancamentos {
        String COD_USUARIO_NOT_EMPY = "O código usuário não pode ser nulo ou vazio";

        String COD_USUARIO_DIGITS = "O código usuário não pode ter mais de 15 digitos";

        String DATA_NO_FUTURO = "A data não pode estar no futuro";

        String DATA_INICIO_NOT_NULL = "A data início não pode estar no futuro";

        String DATA_FINAL_NOT_NULL = "A data final não pode estar no futuro";

        String NUMERO_CONTA_INVALIDA = "O formato da conta é inválido. Ex formato: 01.03.02";
    }

}
