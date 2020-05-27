package com.react.contabil.conta;

import com.react.contabil.dao.ContaDao;
import com.react.contabil.dataobject.ContaDO;
import com.react.contabil.excecao.*;
import com.react.contabil.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ContaServiceHandler {

    private static final Logger LOGGER = LoggerFactory
                .getLogger(ContaServiceHandler.class);

    @Inject
    private ContaDao dao;


    public ContaServiceHandler() {
    }

    /**
     * Adicionar conta
     * @param conta conta a ser adicionada
     * @throws EntidadeExistenteException Conta adicionada já existe
     * @throws BancoDadosException Erro de banco
     * @throws ContabilException Erro desconhecido
     */
    @Transactional
    public void adicionar(Conta conta) throws EntidadeExistenteException,
            BancoDadosException, ContabilException {
        try {
            LOGGER.debug("adicionar :: Adicionando {} ...", conta.toString());

            this.verificaExistenciaConta(conta.getCodigo(), "adicionar");
            final ContaDO contaDO = conta.toDataObject();
            this.dao.inserir(contaDO);
            LOGGER.info("adicionar :: {} adicionada com sucesso!",
                    conta.toString());
        } catch (EntidadeExistenteException | BancoDadosException e) {
            throw e;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro desconhecido" +
                    " ao adicionar %s", conta.toString());
            LOGGER.error("adicionar :: {} Erro: {}", erro, e.getMessage(), e);
            throw new ContabilException(erro, e);
        }
    }

    /**
     * Metodo de suporte pra procrar conta por codigo
     * @param codigo COidog da conta
     * @return ContaDO
     * @throws BancoDadosException
     * @throws EntidadeExistenteException
     */
    private ContaDO verificaExistenciaConta(Long codigo, String nomeMetodo)
            throws BancoDadosException, EntidadeExistenteException {
        final ContaDO contaDO = this.dao.procurar(codigo);
        if (contaDO != null) {
            final String msg = String.format("A conta codigo %d já existe!",
                    codigo);
            LOGGER.error("{} :: {}", nomeMetodo, msg);
            throw new EntidadeExistenteException(msg);
        }
        return contaDO;
    }

    /**
     * Remover conta
     * @param conta conta a ser removida
     * @throws EntidadeNaoEncontradaException Erro de Remoçao
     * @throws BancoDadosException Erro de Banco
     * @throws ContabilException Erro desconhecido
     */
    @Transactional
    public void remover(Conta conta) throws EntidadeNaoEncontradaException,
            BancoDadosException, ContabilException {
        try {
            LOGGER.debug("remover :: Removendo {}", conta.toString());
            final ContaDO contaDO = this.dao.procurar(conta.getCodigo());
            if (contaDO == null) { // valida existencia
                final String msg = String.format("%s não existe",
                        conta.toString());
                LOGGER.error("remover :: {}", msg);
                throw new EntidadeNaoEncontradaException(msg);
            }

            LOGGER.debug("remover :: Validando a remoção da {}",
                    conta.toString());
            this.validaRemocao(conta, contaDO);
            LOGGER.debug("remover :: {} valida para remoção",
                    conta.toString());

            this.dao.remover(contaDO.getCodigo());
            LOGGER.info("remover :: {} removida com sucesso",
                    conta.toString());
        } catch (EntidadeNaoEncontradaException | EntitadeNaoRemovivelException
                    | BancoDadosException e) {
            throw e;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro desconhecido" +
                    " ao adicionar %s", conta.toString());
            LOGGER.error("adicionar :: {} Erro: {}", erro, e.getMessage(), e);
            throw new ContabilException(erro, e);
        }
    }

    /**
     * Atualiza conta
     * @param conta Conta a ser atualizada
     * @throws ContabilException
     */
    public void atualizar(Conta conta) throws ContabilException {
        try {
            LOGGER.info("atualizar :: Atualizando {}", conta.toString());
            final ContaDO contaDO = this.dao.procurar(conta.getCodigo());
            if (contaDO == null) {
                final String erro = String.format("%s não existe",
                        conta.toString());
                LOGGER.error("atualizar :: {}", conta.toString());
                throw new EntidadeNaoEncontradaException(erro);
            }
            this.dao.atualizar(conta.update(contaDO));
            LOGGER.info("atualizar :: Atualizaçào de {} efetuada com sucesso",
                    conta.toString());
        } catch (EntidadeNaoEncontradaException | BancoDadosException e) {
            throw e;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro desconhecido" +
                    " ao adicionar %s", conta.toString());
            LOGGER.error("adicionar :: {} Erro: {}", erro, e.getMessage(), e);
            throw new ContabilException(erro, e);
        }
    }

    /**
     * Valida remoçào da conta
     * @param conta conta DTO
     * @param contaDO Conta data object
     * @throws EntitadeNaoRemovivelException Se nao for removivel
     */
    private void validaRemocao(Conta conta, ContaDO contaDO) throws
            EntitadeNaoRemovivelException {

        // Valida se conta é nivel 1 (nao pode ser adicionada ou removida)
        // ex: 01 - Ativo
        if (conta.getNivelConta() == 1) { // valida se é removivel
            final String msg = String.format("%s é nível primário e " +
                    "não pode ser removida", conta.toString());
            LOGGER.error("remover :: {}", msg);
            throw new EntitadeNaoRemovivelException(msg);
        }

        // Verifica se contem valores
        if (Util.isNotNullNorEmpty(contaDO.getValores())) {
            final String msg = String.format("%s contém valores. " +
                            "Apenas contas vazias podem ser removidas",
                    conta.toString());
            LOGGER.error("remover :: {}", msg);
            throw new EntitadeNaoRemovivelException(msg);
        }

        // verifica se a conta tem filhas
        if (Util.isNotNullNorEmpty(contaDO.getContasFilhas())) {
            final String msg = String.format("%s contém contas filhas. " +
                            "Remova as contas filhas primeiro",
                    conta.toString());
            LOGGER.error("remover :: {}", msg);
            throw new EntitadeNaoRemovivelException(msg);
        }
    }

    /**
     * Procura lista de contas baseado nos filtros
     * @param codigoUsuario codigo usuario
     * @param numero numero da conta (ou parte dela)
     * @param nome nome da conta (ou parte dela)
     * @return lista de contas
     * @throws BancoDadosException erro de banco
     * @throws ContabilException erro desconhecido
     */
    public List<Conta> listar(Long codigoUsuario, String numero, String nome)
                throws BancoDadosException, ContabilException {

        final String msg = String.format("filtros codigo usuário: %d%s%s",
                codigoUsuario, numero == null ? "" : " numero: " + numero,
                nome == null ? "" : " nome: " + nome);

        try {
            LOGGER.info("listar :: Procurando lista de contas {}", msg);
            final List<Conta> contas = this.dao.listar(codigoUsuario, numero, nome)
                    .stream().map(Conta::new).collect(Collectors.toList());
            LOGGER.info("listar :: Lista encontrada com sucesso ({})", msg);
            return contas;
        } catch (BancoDadosException e) {
            throw e;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro desconhecido" +
                    " ao procurar lista com %s", msg);
            LOGGER.error("adicionar :: {} Erro: {}", erro, e.getMessage(), e);
            throw new ContabilException(erro, e);
        }
    }

    public Conta procurar(Long codigo) throws EntidadeNaoEncontradaException,
            BancoDadosException, ContabilException {
        final String msg = String.format("conta com código: %d", codigo);
        try {
            LOGGER.info("procurar :: Procurando {}", msg);
            final ContaDO contaDO = this.dao.procurar(codigo);
            if (contaDO == null) {
                final String erro = String.format("Conta com código %d não existe",
                        codigo);
                LOGGER.error("procurar :: {}", erro);
                throw new EntidadeNaoEncontradaException(erro);
            }

            LOGGER.info("procurar :: Procura de {} efetuada com sucesso",
                    msg);
            return new Conta(contaDO);
        } catch (EntidadeNaoEncontradaException | BancoDadosException e) {
            throw e;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro desconhecido" +
                    " ao procurar %s", msg);
            LOGGER.error("adicionar :: {} Erro: {}", erro, e.getMessage(), e);
            throw new ContabilException(erro, e);
        }
    }
}
