package com.react.contabil.configuracao;

import com.react.contabil.conta.ContaService;
import com.react.contabil.lancamento.LancamentoService;
import com.react.contabil.usuario.UsuarioService;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;


@ApplicationPath("/api")
public class ConfiguracaoWebService extends Application {
    private Set<Class<?>> classes = new HashSet<>();


    public ConfiguracaoWebService() {
        classes.add(ContaService.class);
        classes.add(UsuarioService.class);
        classes.add(LancamentoService.class);
    }

    /**
     * Metodo chamado pelo Jetty Servlet para receber as classes que contém
     * endpoint
     * @return Retorna a lista de classes
     */
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<>();
        classes.add(ContaService.class);
        classes.add(UsuarioService.class);
        classes.add(LancamentoService.class);
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

