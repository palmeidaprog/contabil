package com.react.contabil.configuracao;

import com.react.contabil.conta.ContaService;
import com.react.contabil.usuario.UsuarioService;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;


@ApplicationPath("/api")
public class ConfiguracaoWebService extends Application {
    private Set<Class<?>> classes = new HashSet<>();

    private final static Logger LOGGER =
            LoggerFactory.getLogger(ConfiguracaoWebService.class.getName());

    public ConfiguracaoWebService() {
        classes.add(ContaService.class);
        classes.add(UsuarioService.class);
    }

    /**
     * Metodo chamado pelo Jetty Servlet para receber as classes que contém
     * endpoint
     * @return Retorna a lista de classes
     */
    public Set<Class<?>> getClasses() {
        LOGGER.error("GETCLASS");
        final Set<Class<?>> classes = new HashSet<>();
        classes.add(ContaService.class);
        classes.add(UsuarioService.class);
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
        LOGGER.error("GETSINGLETON");
        corsFilter.getAllowedOrigins().add("*");
        corsFilter.setAllowedMethods("OPTIONS, GET, POST, DELETE, PUT, PATCH");
        providers.add(corsFilter);
        return providers;
    }
}

