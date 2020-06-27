package com.react.contabil.lancamento;

import com.react.contabil.dao.FiltroLancamentos;
import com.react.contabil.excecao.BancoDadosException;
import com.react.contabil.excecao.ContabilException;
import com.react.contabil.excecao.EntidadeExistenteException;
import org.slf4j.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Date;
import java.util.List;

@Path("/lancamento")
@RequestScoped
public class LancamentoService {

    @Inject
    private Logger logger;

    @Inject
    private LancamentoServiceHandler handler;

    public LancamentoService() {
        // construtor padrào para JAXRS
    }

    @POST
    @Path("/adicionar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response adicionar(@Valid Lancamento lancamento) {
            try {
            logger.info("adicionar :: Acessando /lancamento/adiciona " +
                        "Adicionando novo lancamento: {}", lancamento
                        .getCodigo());
            this.handler.adicionar(lancamento);

            logger.info("adicionar :: Acessando /lancamento/adiciona " +
                            "{} adicionado com sucesso", lancamento);
            return Response.ok().build();
        } catch (EntidadeExistenteException e) {
            return Response.status(Status.BAD_REQUEST).entity(e).build();
        } catch (BancoDadosException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
        } catch (ContabilException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

    @POST
    @Path("/remover")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response remover(@Valid Lancamento lancamento) {
        try {
            logger.info("remover :: /lancamento/remover Removendo {} ...", 
                    lancamento);
            this.handler.remover(lancamento);
            logger.info("remover :: /lancamento/remover {} removido com " +
                    "sucesso!", lancamento);
            return Response.ok().build();
        } catch (EntidadeExistenteException e) {
            return Response.status(Status.BAD_REQUEST).entity(e).build();
        } catch (BancoDadosException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
        } catch (ContabilException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

    @POST
    @Path("/atualizar")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response atualizar(@Valid Lancamento lancamento) {
        try {
            logger.info("atualizar :: /lancamento/atualizar Atualizando {}" +
                    " ... ", lancamento);
            this.handler.atualizar(lancamento);
            logger.info("atualizar :: /lancamento/atualizar {} atualizado " +
                    "com sucesso!", lancamento);
            return Response.ok().build();
        } catch (EntidadeExistenteException e) {
            return Response.status(Status.BAD_REQUEST).entity(e).build();
        } catch (BancoDadosException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
        } catch (ContabilException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

    @GET
    @Path("/listar/{codigoUsuario}/{dataInicio}/{dataFinal}")
    public Response listar(@PathParam("codigoUsuario") Long codigoUsuario,
                              @PathParam("dataInicio") Date dataInicio,
                              @PathParam("dataFinal") Date dataFinal) {
        final FiltroLancamentos filtro = new  FiltroLancamentos(codigoUsuario,
                dataInicio, dataFinal);

        try {
            logger.info("listar :: /lancamento/listar/{} Procurando lista" +
                    " de lançamentos com {}", codigoUsuario, filtro);
            final List<Lancamento> lancamentos = this.handler.listar(filtro);
            logger.info("listar :: Lista de lançamentos encontrado com {}",
                    filtro);
            return Response.ok(lancamentos).build();
        } catch (ContabilException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e)
                    .build();
        }
    }

    @GET
    @Path("/get/{codigo}")
    public Response get(@PathParam("codigo") Long codigo) {
        try {
            logger.info("get :: /get/{} Procurando lançamento com código {}",
                    codigo, codigo);
            final Lancamento lancamento = this.handler.procurar(codigo);
            logger.info("get :: /get/{} {} encontrado com sucesso", codigo,
                    lancamento);
            return Response.ok(lancamento).build();
        } catch (EntidadeExistenteException e) {
            return Response.status(Status.BAD_REQUEST).entity(e).build();
        } catch (BancoDadosException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
        } catch (ContabilException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
        
    }
}
