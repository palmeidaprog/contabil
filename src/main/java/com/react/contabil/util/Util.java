package com.react.contabil.util;

import java.util.Collection;

public class Util {

    /**
     * Verifica se a string é null ou esta vazia (contem algo alem de espacos)
     * @param s String a ser testada
     * @return Verdadeiro se tiver conteudo
     */
    public static boolean isNotBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    /**
     * Verifica se a string é null ou esta vazia (contem algo alem de espacos)
     * @param s String a ser testada
     * @return Verdadeiro se NÃO tiver conteudo
     */
    public static boolean isBlank(String s) {
        return !isNotBlank(s);
    }

    /**
     * Verifica se a lista ou qualquer coleçào é null ou vazia
     * @param collection Coleçào
     * @return Verdadeiro se vazia ou nula
     */
    public static boolean isNotNullNorEmpty(Collection<?> collection) {
        return collection != null && collection.isEmpty();
    }
}
