package com.react.contabil.conta;

import com.react.contabil.datalayer.dataobject.UsuarioDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/conta")
@RequestScoped
public class ContaService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ContaService.class);

    @Inject
    private ContaServiceHandler handler;

    public ContaService() {
        System.out.println("y17");
    }

    @POST
    @Path("/adicionar")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response adicionar(Conta conta) {
        //LOGGER.info("adicionar :: Acessando /conta/adiciona Adicionando nova conta login: {}", conta.getLogin());

        //this.handler.adicionar(conta);

        //LOGGER.info("adicionar :: Acessando /conta/adiciona Conta login: {} adicionado com sucesso", conta.getLogin());

        return Response.ok("Test").build();
    }

}