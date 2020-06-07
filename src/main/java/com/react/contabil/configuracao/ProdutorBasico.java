package com.react.contabil.configuracao;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ProdutorBasico {

    @Produces
    @RequestScoped
    public List<?> getLista() {
        return new ArrayList<>();
    }

}
