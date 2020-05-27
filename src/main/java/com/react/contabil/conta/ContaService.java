package com.react.contabil.conta;

import com.react.contabil.datalayer.dataobject.UsuarioDO;
import com.react.contabil.excecao.BancoDadosException;
import com.react.contabil.excecao.ContabilException;
import com.react.contabil.excecao.EntidadeExistenteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Status;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/conta")
@RequestScoped
public class ContaService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ContaService.class);

    @Inject
    private ContaServiceHandler handler;

    public ContaService() {

    }

    @POST
    @Path("/adicionar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response adicionar(Conta conta) {
        //LOGGER.info("adicionar :: Acessando /conta/adiciona Adicionando nova conta login: {}",
        // conta.getLogin());

        //this.handler.adicionar(conta);

        //LOGGER.info("adicionar :: Acessando /conta/adiciona Conta login: {} adicionado com sucesso",
        // conta.getLogin());

        return Response.ok("Test").build();
    }

    @POST
    @Path("/remover")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response remover(Conta conta) {
        return Response.ok("Test").build();
    }

    @POST
    @Path("/atualizar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response atualizar(Conta conta) {
        return Response.ok("Teste").build();
    }

    @GET
    @Path("/listar/{codigoUsuario}/{codigo}")
    public List<Conta> listar(@QueryParam("codigo") Long codigo, @QueryParam("numero") String numero,
                             @QueryParam("nome") String nome) {
        return new ArrayList<>();
    }

    @GET
    @Path("/get/{codigo}")
    public Response get(@PathParam("codigo") Long codigo) {
            //this.handler.procurar(codigo);
            return Response.ok().build();
    }


}