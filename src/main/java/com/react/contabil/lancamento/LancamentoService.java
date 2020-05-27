package com.react.contabil.lancamento;

import com.react.contabil.conta.Conta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("/lancamento")
@RequestScoped
public class LancamentoService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(LancamentoService.class);

    //private LancamentoServiceHandler handler;

    public LancamentoService() {
        System.out.println("s2");
    }

    @POST
    @Path("/adicionar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response adicionar(Lancamento lancamento) {
        /*try {
            LOGGER.info("adicionar :: Acessando /lancamento/adiciona Adicionando novo lancamento: {}",
            lancamento.getCodigo());
            this.handler.adicionar(lancamento);
            LOGGER.info("adicionar :: Acessando /lancamento/adiciona Lancamento: {} adicionado com sucesso",
             lancamento.getCodigo());
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
    public Response remover(Lancamento lancamento) {
        /*try {
            this.handler.remover(lancamento);
            return Response.ok("Test").build();
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
    public Response atualizar(Lancamento lancamento) {
        /*try {
            this.handler.atualizar(lancamento);
            return Response.ok("Teste").build();
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
    @Path("/listar/{codigoUsuario}/{dataInicio}/{dataFinal}")
    public List<Conta> listar(@PathParam("codigoUsuario") Long codigoUsuario,
                              @PathParam("dataInicio") Date dataInicio,
                              @PathParam("dataFinal") Date dataFinal,
                              @QueryParam("numeroConta") Long numeroConta) {
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
