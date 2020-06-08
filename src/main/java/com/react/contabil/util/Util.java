package com.react.contabil.util;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

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
        return collection != null && !collection.isEmpty();
    }

    /**
     * Retorna a data como string no formato ano-mes-dia
     * @param data data
     * @return string
     */
    public static String converteDateParaSqlDate(Date data) {
        final SimpleDateFormat fmt = new SimpleDateFormat(
                "yyyy-MM-dd");
        return fmt.format(data);
    }

    /**
     * Retorna a data como string no formato dia/mes/ano
     * @param data data
     * @return string
     */
    public static String converteParaDiaMesAno(Date data) {
        final SimpleDateFormat fmt = new SimpleDateFormat(
                "dd/MM/yyyy");
        return fmt.format(data);
    }
}
