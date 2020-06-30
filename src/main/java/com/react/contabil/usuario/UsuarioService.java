package com.react.contabil.usuario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.react.contabil.conta.Conta;
import com.react.contabil.excecao.*;
import com.react.contabil.util.Constantes;
import com.react.contabil.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/usuario")
@RequestScoped
public class UsuarioService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(UsuarioService.class);

    @Inject
    private UsuarioServiceHandler handler;

    public UsuarioService() {
    }

    /**
     * Adiciona usuário
     * @param usuario usuário a ser adicionado
     * @return retorna o codigo do usuário
     */
    @POST
    @Path("/adicionar")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(Constantes.APPLICATION_JSON_UTF8)
    public Response adicionar(String usuarioStr) {
        final ObjectMapper mapper = new ObjectMapper();
        Usuario usuario;
        try {
            usuario = mapper.readValue(usuarioStr, Usuario.class);
        } catch (Exception e) {
            LOGGER.error("remover :: Respondendo INTERNAL_SERVER_ERROR, " +
                    "ocorreu um erro ao remover Erro: {}", e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e)
                    .build();
        }


        try {
            LOGGER.info("adicionar :: Acessando /usuario/adiciona " +
                    "Adicionando novo {}", usuario.getLogin());
            this.handler.adicionar(usuario);
            LOGGER.info("adicionar :: Acessando /usuario/adiciona {} " +
                    "adicionado com sucesso", usuario.getLogin());

            return Response.ok(usuario.getCodigo()).build();
        } catch (EntidadeExistenteException e) {
            LOGGER.error("adicionar :: Respondendo BAD_REQUEST, {} já " +
                    "existe!", usuario.toString());
            return Response.status(Status.BAD_REQUEST).entity(e).build();
        } catch (BancoDadosException e) {
            LOGGER.error("adicionar :: Respondendo INTERNAL_SERVER_ERROR, " +
                            "ocorreu um erro de banco ao adicionar {} Erro: {}",
                    usuario.toString(), e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e)
                    .build();
        } catch (ContabilException e) {
            LOGGER.error("adicionar :: Respondendo INTERNAL_SERVER_ERROR, " +
                            "ocorreu um erro ao adicionar {} Erro: {}",
                    usuario.toString(), e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e)
                    .build();
        }
    }

    /**
     * Procura usuário baseado no código ou login (se ambos preenchidos,
     * procura por código
     * @param codigo (Opcional) código do usuário
     * @param login (Opcional) login do usuário
     * @return Usuário procura (se não encontrar retorna BAD_REQUEST)
     */
    @GET
    @Path("/get")
    @Produces(Constantes.APPLICATION_JSON_UTF8)
    public Response get(@QueryParam("codigo") Long codigo,
                        @QueryParam("login") String login) {

        // erro se ambos forem vazios
        if (codigo == null && Util.isBlank(login)) {
            final String erro = "Necessário informar código ou login do " +
                    "usuário para procurá-lo";
            LOGGER.error("get :: /usuario/get {}", erro);
            return Response.status(Status.BAD_REQUEST).entity(
                    new ParametrosInvalidosException(erro)).build();
        }

        final String filtroMsg = codigo == null ? String.format(
                "código: %d", codigo) : String.format("login: %s", login);

        try {
            LOGGER.info("get :: /usuario/get/{} Procurando usuário código:" +
                    " {} ...", codigo, codigo);
            final Usuario usuario = this.handler.procurar(codigo, login);
            LOGGER.info("get :: /usuario/get/{} Usuário codigo: {} " +
                    "encontrado com sucesso", codigo, codigo);

            return Response.ok(usuario).build();
        } catch (EntidadeNaoEncontradaException e) {
            LOGGER.error("get :: Respondendo BAD_REQUEST, usuário com {} " +
                    "não existe", filtroMsg);
            return Response.status(Status.BAD_REQUEST).entity(e).build();
        } catch (BancoDadosException e) {
            LOGGER.info("get :: Respondendo INTERNAL_SERVER_ERROR, " +
                    "ocorreu um erro de banco procurando usuário com {} " +
                    "Erro: {}", filtroMsg, e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e)
                    .build();
        } catch (ContabilException e) {
            LOGGER.error("get :: Respondendo INTERNAL_SERVER_ERROR. " +
                    "ocorreu um erro ao procurar usuário com {} Erro: {}",
                    filtroMsg, e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e)
                    .build();
        }
    }
}
