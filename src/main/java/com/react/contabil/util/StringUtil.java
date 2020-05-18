package com.react.contabil.util;

public class StringUtil {

    /**
     * Verifica se a string é null ou esta vazia (contem algo alem de espacos)
     * @param s String a ser testada
     * @return Verdadeiro se tiver conteudo
     */
    public static boolean isNotBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }
}
