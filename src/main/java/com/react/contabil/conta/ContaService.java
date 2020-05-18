package com.react.contabil.conta;

import com.react.contabil.datalayer.dataobject.UsuarioDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/conta")
@RequestScoped
public class ContaService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ContaService.class);

    @Inject
    private ContaServiceHandler handler;

    @GET
    @Path("/test")
    public Response test() {
        final UsuarioDO usuarioDO = new UsuarioDO();
        usuarioDO.setLogin("teste");
        usuarioDO.setNome("Tester");
        usuarioDO.setSobrenome("Test");
        this.handler.inserir(usuarioDO);

        return Response.ok().build();
    }

}