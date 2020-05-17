package com.react.contabil.configuracao;

import com.react.contabil.conta.ContaService;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;


public class ConfiguracaoWebService extends Application {
    private Set<Class<?>> classes = new HashSet<>();

    public ConfiguracaoWebService() {
        classes.add(ContaService.class);
//        classes.add(AtendimentoService.class);
    }

    /**
     * Metodo chamado pelo Jetty Servlet para receber as classes que contém
     * endpoint
     * @return Retorna a lista de classes
     */
    public Set<Class<?>> getClasses() {
        return classes;
    }

    /**
     * Configura o CORS para o Application do JAX-RS
     * @return Set das configuraçòes dos filtros do JAX-RS
     */
    @Override
    public Set<Object> getSingletons() {
        Set<Object> providers = new HashSet<>();
        CorsFilter corsFilter = new CorsFilter();
        corsFilter.getAllowedOrigins().add("*");
        corsFilter.setAllowedMethods("OPTIONS, GET, POST, DELETE, PUT, PATCH");
        providers.add(corsFilter);
        return providers;
    }
}

