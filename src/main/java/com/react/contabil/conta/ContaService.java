package com.react.contabil.conta;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.react.contabil.excecao.*;
import com.react.contabil.util.Constantes;
import com.react.contabil.util.Util;
import org.slf4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

import static com.react.contabil.util.Constantes.Conta.BALANCETE_NOT_NULL_RETURN;

@Path("/conta")
@RequestScoped
public class ContaService {

    @Inject
    private Logger logger;

    @Inject
    private ContaServiceHandler handler;

    public ContaService() {
        // construtor padrão
    }

    /**
     * Adiciona nova conta
     * @param contaStr Conta a ser adicionada
     * @return Response com codigo 200 ou excecao
     */
    @POST
    @Path("/adicionar")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(Constantes.APPLICATION_JSON_UTF8)
    public Response adicionar(String contaStr) {

        final ObjectMapper mapper = new ObjectMapper();
        Conta conta;
        try {
            conta = mapper.readValue(contaStr, Conta.class);
        } catch (Exception e) {
            logger.error("adicionar :: Respondendo INTERNAL_SERVER_ERROR, " +
                    "ocorreu um erro ao adicionar Erro: {}", e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e)
                    .build();
        }

        try {
            logger.info("adicionar :: Acessando /conta/adiciona Adicionando" +
                            " nova {}", conta.toString());
            this.handler.adicionar(conta);
            logger.info("adicionar :: Acessando /conta/adiciona {} " +
                            "adicionada com sucesso!", conta.toString());

            return Response.ok().build();
        } catch (EntidadeExistenteException e) {
            logger.error("adicionar :: Respondendo BAD_REQUEST, {} já existe!",
                    conta.toString());
            return Response.status(Status.BAD_REQUEST).entity(e).build();
        } catch (ContabilException e) {
            logger.error("adicionar :: Respondendo INTERNAL_SERVER_ERROR, " +
                          "ocorreu um erro ao adicionar {} Erro: {}",
                           conta.toString(), e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e)
                    .build();
        }
    }

    @GET
    @Path("/getNumero/{codigoUsuario}/{contaPai}")
    public Response getNumero(@PathParam("codigoUsuario") Long codigoUsuario,
                              @PathParam("contaPai") String numeroContaPai) {
        return Response.ok().build();
    }

    /**
     * Remover conta
     * @param conta conta a ser removida
     * @return 200 para ok ou exceção e codigo de erro
     */
    @POST
    @Path("/remover")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(Constantes.APPLICATION_JSON_UTF8)
    public Response remover(String contaStr) {
        final ObjectMapper mapper = new ObjectMapper();
        Conta conta;
        try {
            conta = mapper.readValue(contaStr, Conta.class);
        } catch (Exception e) {
            logger.error("adicionar :: Respondendo INTERNAL_SERVER_ERROR, " +
                    "ocorreu um erro ao remover Erro: {}", e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e)
                    .build();
        }


        try {
            logger.info("remover :: /conta/remover Removendo a {} ...",
                    conta.toString());
            this.handler.remover(conta);
            logger.info("remover :: /conta/remover {} removida com sucesso",
                    conta.toString());

            return Response.ok().build();
        } catch (EntidadeNaoEncontradaException e) {
            logger.error("remover :: Respondendo BAD_REQUEST, {} não existe!",
                    conta.toString());
            return Response.status(Status.BAD_REQUEST).entity(e).build();
        } catch (EntitadeNaoRemovivelException e) {
            logger.error("remover :: Respondendo BAD_REQUEST, {} não pode " +
                    "ser removida. Erro: {}", conta.toString(), e.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(e).build();
        } catch (BancoDadosException e) {
            logger.error("remover :: Respondendo INTERNAL_SERVER_ERROR, " +
                    "encontrado erro de banco ao remover {} Erro: {}",
                    conta.toString(), e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e)
                    .build();
        } catch (ContabilException e) {
            logger.error("remover :: Respondendo INTERNAL_SERVER_ERROR, " +
                            "encontrado erro ao remover {} Erro: {}",
                    conta.toString(), e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e)
                    .build();
        }
    }

    @POST
    @Path("/atualizar")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(Constantes.APPLICATION_JSON_UTF8)
    public Response atualizar(String contaStr) {
        final ObjectMapper mapper = new ObjectMapper();
        Conta conta;
        try {
            conta = mapper.readValue(contaStr, Conta.class);
        } catch (Exception e) {
            logger.error("adicionar :: Respondendo INTERNAL_SERVER_ERROR, " +
                    "ocorreu um erro ao atualizar Erro: {}", e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e)
                    .build();
        }

        try {
            this.handler.atualizar(conta);
            return Response.ok().build();
        } catch (EntidadeNaoEncontradaException e) {
            return Response.status(Status.BAD_REQUEST).entity(e).build();
        } catch (BancoDadosException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
        } catch (ContabilException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
        //return Response.ok().build();
    }

    /**
     * Lista contas do usuário
     * @param codigoUsuario Código do usuário
     * @param numero (Opcional) Número da conta/Centro de custo
     * @param nome (Opcional) Nome ou parte do nome da conta
     * @return Http Code com lista de Contas
     */
    @GET
    @Produces(Constantes.APPLICATION_JSON_UTF8)
    @Path("/listar/{codigoUsuario}")
    public Response listar(@PathParam("codigoUsuario") Long codigoUsuario,
                           @QueryParam("numero") String numero,
                           @QueryParam("nome") String nome) {

        final String msg = this.log(codigoUsuario, numero, nome);

        try {
            logger.info("listar :: /conta/listar/{} Procurando {}",
                    codigoUsuario, msg);
            final List<Conta> contas = this.handler.listar(codigoUsuario,
                                                           numero, nome);
            logger.info("listar :: /conta/listar/{} {} encontrada com " +
                    "sucesso!", codigoUsuario, msg);

            return Response.ok(contas).build();
        } catch (BancoDadosException e) {
            logger.error("listar :: Retornando INTERNAL_SERVER_ERROR " +
                    "devido a um erro de banco ocorrido ao procurar {}", msg);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e)
                    .build();
        } catch (ContabilException e) {
            logger.error("listar :: Retornando INTERNAL_SERVER_ERROR " +
                    "devido a um erro ocorrido ao procurar {}", msg);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e)
                    .build();
        }
    }

    /**
     * retorna dados da conta
     * @param codigo Codigo da conta
     * @return Http Code com Conta ou Excecao em caso de erro
     */
    @GET
    @Path("/get/{codigo}")
    @Produces(Constantes.APPLICATION_JSON_UTF8)
    public Response get(@PathParam("codigo") Long codigo) {
        final String msg = String.format("conta com código %d", codigo);

        try {
            logger.info("get :: /conta/get/{} Procurando {} ...", codigo, msg);
            final Conta conta = this.handler.procurar(codigo);
            logger.info("get :: /conta/get/{} Conta {} encontrada", codigo,
                    conta.toString());

            return Response.ok(conta).build();
        } catch (EntidadeNaoEncontradaException e) {
            logger.error("get :: Retornando BAD_REQUEST para procura de {}",
                    msg);
            return Response.status(Status.BAD_REQUEST).entity(e).build();
        } catch (BancoDadosException e) {
            logger.error("get :: Retornando INTERNAL_SERVER_ERROR para " +
                    "procura de {}", msg);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e)
                    .build();
        } catch (ContabilException e) {
            logger.error("get :: Retornando INTERNAL_SERVER_ERROR para " +
                    "procura de {}", msg);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e)
                    .build();
        }
    }

    /**
     * Recebe uma lista de contas do usuário para ser montado o balancete
     * @param codigoUsuario codigo do usuário
     * @return Lista de Contas
     */
    @GET
    @Path("/balancete/{codigoUsuario}")
    @Produces(Constantes.APPLICATION_JSON_UTF8)
    @NotNull(message = BALANCETE_NOT_NULL_RETURN)
    public Response balancete(@PathParam("codigoUsuario") Long codigoUsuario) {
        final String msg = String.format("balancete do usuário código %d", codigoUsuario);

        try {
            logger.info("balancete :: /conta/balancete/{} Buscando {}", codigoUsuario, msg);
            final List<Conta> balancete = this.handler.balancete(codigoUsuario);
            logger.info("balancete :: /conta/balancete/{} Busca de {} executada com sucesso",
                    codigoUsuario, msg);

            return Response.ok(balancete).build();
        } catch (ContabilException e) {
            logger.error("get :: Retornando INTERNAL_SERVER_ERROR para procura de {}", msg);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

    /**
     * Monta mensagem de log
     * @param codigoUsuario
     * @param numero
     * @param nome
     * @return
     */
    private String log(@NotNull Long codigoUsuario, String numero,
                       String nome) {
        final StringBuilder sb = new StringBuilder("Lista de contas do ");
        sb.append("usuário código: ").append(codigoUsuario);

        if (Util.isNotBlank(numero)) {
            sb.append(" numero: ").append(numero);
        }

        if (Util.isNotBlank(nome)) {
            sb.append(" nome: ").append(nome);
        }

        return sb.toString();
    }

}