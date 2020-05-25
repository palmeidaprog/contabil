package com.react.contabil;

import com.react.contabil.datalayer.dataobject.UsuarioDO;

public class TestUtils {

    public static UsuarioDO getUsuarioDO() {
        final UsuarioDO usuarioDO = new UsuarioDO();
        usuarioDO.setLogin("teste");
        usuarioDO.setNome("Tester");
        usuarioDO.setSobrenome("Test");

        return usuarioDO;
    }
}
