package com.react.contabil.usuario;

import com.react.contabil.excecao.BancoDadosException;
import com.react.contabil.excecao.ContabilException;
import com.react.contabil.excecao.EntidadeExistenteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/usuario")
@RequestScoped
public class UsuarioService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(UsuarioService.class);

    private UsuarioServiceHandler handler;


    public UsuarioService() {
        System.out.println("v3");
    }

    @GET
    @Path("/test")
    public Response test() {
        LOGGER.info("test :: Teste.... ");
        return Response.ok().build();
    }

    @POST
    @Path("/adicionar")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response adicionar(Usuario usuario) {
        try {
            LOGGER.info("adicionar :: Acessando /usuario/adiciona Adicionando novo usuario: {}",
                    usuario.getLogin());
            this.handler.adicionar(usuario);
            LOGGER.info("adicionar :: Acessando /usuario/adiciona Usuario: {} adicionado com sucesso",
                    usuario.getLogin());
            return Response.ok().build();
        } catch (EntidadeExistenteException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        } catch (BancoDadosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        } catch (ContabilException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

    @GET
    @Path("/get/{codigo}")
    public Response get(@PathParam("codigo") Long codigo) {
        try {
            this.handler.procurar(codigo);
            return Response.ok().build();
        } catch (EntidadeExistenteException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        } catch (BancoDadosException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        } catch (ContabilException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }

    }
}
