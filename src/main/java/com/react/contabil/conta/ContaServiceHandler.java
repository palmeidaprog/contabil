package com.react.contabil.conta;

import com.react.contabil.dao.ContaDao;
import com.react.contabil.dataobject.ContaDO;
import com.react.contabil.excecao.*;
import com.react.contabil.util.Util;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.stream.Collectors;

import static com.react.contabil.util.Util.isNotBlank;

@ApplicationScoped
public class ContaServiceHandler {

    @Inject
    private Logger logger;

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
            logger.debug("adicionar :: Adicionando {} ...", conta);

            if (conta.getCodigo() != null) {
                this.verificaExistenciaConta(conta.getCodigo(), "adicionar");
            }

            final ContaDO contaDO = conta.toDataObject();
            final ContaDO contaPai = this.pegaConta(contaDO.getContaPaiCodigo(),
                    "adicionar");

            contaDO.setNumero(this.novoNumero(contaPai));

            this.dao.inserir(contaDO);
            logger.info("adicionar :: {} adicionada com sucesso!", conta);


        } catch (EntidadeExistenteException | BancoDadosException e) {
            throw e;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro desconhecido" +
                    " ao adicionar %s", conta);
            logger.error("adicionar :: {} Erro: {}", erro, e.getMessage(), e);
            throw new ContabilException(erro, e);
        }
    }

    /**
     * Gera o numero da nova conta
     * @param contaPai
     * @return
     */
    @NotNull
    private String novoNumero(@NotNull @Valid ContaDO contaPai) {

        //TODO: conta agregadora pode ter lancamentos? decidir design

        if (contaPai.getContasFilhas().isEmpty()) {
            return contaPai.getNumero() + ".01";
        }

        final List<ContaDO> filhas = contaPai.getContasFilhas();
        final int nivel = contaPai.getNivelConta();
        String numeroFinal = filhas.get(filhas.size()-1).getNumero()
                .split("\\.")[nivel];
        int numero = Integer.parseInt(numeroFinal) + 1;
        numeroFinal = String.format("%02d", numero);

        final String novoNumero = contaPai.getNumero() + "." + numeroFinal;
        logger.info("novoNumero :: Novo numero gerado para conta {}",
                novoNumero);

        return novoNumero;
    }

    /**
     * Pega conta baseado no código
     * @param codigo codigo da conta
     * @param nomeMetodo nome do metodo que chamou a funcao (para log)
     * @return Conta achada
     * @throws BancoDadosException Erro de banco
     * @throws EntidadeNaoEncontradaException Conta nao encontrada
     */
    @NotNull @Valid
    private ContaDO pegaConta(@NotNull Long codigo, String nomeMetodo)
            throws BancoDadosException, EntidadeNaoEncontradaException {

        final ContaDO contaDO = this.dao.procurar(codigo);
        if (contaDO == null) { // valida existencia
            final String msg = String.format("Conta código %d não existe",
                    codigo);
            logger.error("{} :: {}", nomeMetodo, msg);
            throw new EntidadeNaoEncontradaException(msg);
        }
        return contaDO;
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
            logger.error("{} :: {}", nomeMetodo, msg);
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
            logger.debug("remover :: Removendo {} ...", conta);
            final ContaDO contaDO = this.dao.procurar(conta.getCodigo());
            if (contaDO == null) { // valida existencia
                final String msg = String.format("%s não existe",
                        conta.toString());
                logger.error("remover :: {}", msg);
                throw new EntidadeNaoEncontradaException(msg);
            }

            logger.debug("remover :: Validando a remoção da {}", conta);
            this.validaRemocao(contaDO);
            logger.debug("remover :: {} valida para remoção", conta);

            this.dao.remover(contaDO);
            logger.info("remover :: {} removida com sucesso", conta);
        } catch (EntidadeNaoEncontradaException | EntitadeNaoRemovivelException
                    | BancoDadosException e) {
            throw e;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro desconhecido" +
                    " ao adicionar %s", conta.toString());
            logger.error("adicionar :: {} Erro: {}", erro, e.getMessage(), e);
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
            logger.info("atualizar :: Atualizando {}", conta.toString());
            final ContaDO contaDO = this.dao.procurar(conta.getCodigo());
            if (contaDO == null) {
                final String erro = String.format("%s não existe",
                        conta.toString());
                logger.error("atualizar :: {}", conta.toString());
                throw new EntidadeNaoEncontradaException(erro);
            }
            this.dao.atualizar(conta.update(contaDO));
            logger.info("atualizar :: Atualizaçào de {} efetuada com sucesso",
                    conta.toString());
        } catch (EntidadeNaoEncontradaException | BancoDadosException e) {
            throw e;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro desconhecido" +
                    " ao adicionar %s", conta.toString());
            logger.error("adicionar :: {} Erro: {}", erro, e.getMessage(), e);
            throw new ContabilException(erro, e);
        }
    }

    /**
     * Valida remoçào da conta
     * @param contaDO Conta data object
     * @throws EntitadeNaoRemovivelException Se nao for removivel
     */
    private void validaRemocao(ContaDO contaDO) throws
            EntitadeNaoRemovivelException {

        // Valida se conta é nivel 1 (nao pode ser adicionada ou removida)
        // ex: 01 - Ativo
        if (contaDO.getNivelConta() == 1) { // valida se é removivel
            final String msg = String.format("%s é nível primário e " +
                    "não pode ser removida", contaDO);
            logger.error("remover :: {}", msg);
            throw new EntitadeNaoRemovivelException(msg);
        }

        // Verifica se contem valores
        if (Util.isNotNullNorEmpty(contaDO.getValores())) {
            final String msg = String.format("%s contém valores. " +
                            "Apenas contas vazias podem ser removidas",
                    contaDO);
            logger.error("remover :: {}", msg);
            throw new EntitadeNaoRemovivelException(msg);
        }

        // verifica se a conta tem filhas
        if (Util.isNotNullNorEmpty(contaDO.getContasFilhas())) {
            final String msg = String.format("%s contém contas filhas. " +
                            "Remova as contas filhas primeiro",
                    contaDO);
            logger.error("remover :: {}", msg);
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
            logger.info("listar :: Procurando lista de contas {}", msg);
            final List<Conta> contas = this.dao.listar(codigoUsuario, numero, nome)
                    .stream().map(Conta::new).collect(Collectors.toList());
            logger.info("listar :: Lista encontrada com sucesso ({})", msg);
            return contas;
        } catch (BancoDadosException e) {
            throw e;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro desconhecido" +
                    " ao procurar lista com %s", msg);
            logger.error("adicionar :: {} Erro: {}", erro, e.getMessage(), e);
            throw new ContabilException(erro, e);
        }
    }

    /**
     * Procurar conta baseado no código
     * @param codigo código da conta
     * @return Conta achada
     * @throws ContabilException Erros
     */
    public Conta procurar(Long codigo) throws
            ContabilException {

        final String msg = String.format("conta com código: %d", codigo);
        try {
            logger.info("procurar :: Procurando {}", msg);
            final ContaDO contaDO = this.dao.procurar(codigo);
            if (contaDO == null) {
                final String erro = String.format("Conta com código %d não existe",
                        codigo);
                logger.error("procurar :: {}", erro);
                throw new EntidadeNaoEncontradaException(erro);
            }

            logger.info("procurar :: Procura de {} efetuada com sucesso",
                    msg);
            return new Conta(contaDO);
        } catch (EntidadeNaoEncontradaException | BancoDadosException e) {
            throw e;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro desconhecido" +
                    " ao procurar %s", msg);
            logger.error("adicionar :: {} Erro: {}", erro, e.getMessage(), e);
            throw new ContabilException(erro, e);
        }
    }
}
