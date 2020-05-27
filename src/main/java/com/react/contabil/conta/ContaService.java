package com.react.contabil.conta;

import com.react.contabil.datalayer.dataobject.UsuarioDO;
import com.react.contabil.excecao.BancoDadosException;
import com.react.contabil.excecao.ContabilException;
import com.react.contabil.excecao.EntidadeExistenteException;
import org.jboss.logging.Param;
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
    public Response adicionar(Conta conta) {
        /*try {
            LOGGER.info("adicionar :: Acessando /conta/adiciona Adicionando nova conta login: {}",
            conta.getCodigo());

            this.handler.adicionar(conta);

            LOGGER.info("adicionar :: Acessando /conta/adiciona Conta login: {} adicionado com sucesso",
            conta.getCodigo());

            return Response.ok().build();

        } catch (EntidadeExistenteException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        } catch (BancoDadosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        } catch (ContabilException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }*/

        return Response.ok().build();
    }

    @POST
    @Path("/remover")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response remover(Conta conta) {
       /* try {
            this.handler.remover(conta);
            return Response.ok().build();
        } catch (EntidadeExistenteException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        } catch (BancoDadosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        } catch (ContabilException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }*/
        return Response.ok().build();
    }

    @POST
    @Path("/atualizar")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response atualizar(Conta conta) {
        /*try {
            this.handler.atualizar(conta);
            return Response.ok().build();
        } catch (EntidadeExistenteException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        } catch (BancoDadosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        } catch (ContabilException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }*/
        return Response.ok().build();
    }

    @GET
    @Path("/listar/{codigoUsuario}")
    public List<Conta> listar(@PathParam("codigoUsuario") Long codigoUsuario, @QueryParam("numero") String numero,
                              @QueryParam("nome") String nome) {
        return new ArrayList<>();
    }

    @GET
    @Path("/get/{codigo}")
    public Response get(@PathParam("codigo") Long codigo) {
        /*try {
            this.handler.procurar(codigo);
            return Response.ok().build();
        } catch (EntidadeExistenteException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        } catch (BancoDadosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        } catch (ContabilException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }*/
        return Response.ok().build();
    }


}