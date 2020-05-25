package com.react.contabil.usuario;

import com.react.contabil.conta.Conta;
import com.react.contabil.conta.ContaService;
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

    public UsuarioService() {
        System.out.println("v3");
    }


    @POST
    @Path("/adicionar")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response adicionar(Usuario usuario) {
        //LOGGER.info("adicionar :: Acessando /usuario/adiciona Adicionando novo usuario: {}", usuario.getLogin());

        //this.handler.adicionar(usuario);

        //LOGGER.info("adicionar :: Acessando /usuario/adiciona Usuario: {} adicionado com sucesso", usuario.getLogin());

        return Response.ok("Test").build();
    }

    @GET
    @Path("/get/{id}")
    public Conta get(@PathParam("id") Long id) {
        //this.handler.procurar(id);
        return new Conta();

    }
}
