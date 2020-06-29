package com.react.contabil.conta;

import com.react.contabil.dao.ContaDao;
import com.react.contabil.dao.Saldo;
import com.react.contabil.dao.SequencialDao;
import com.react.contabil.dataobject.ContaDO;
import com.react.contabil.excecao.*;
import com.react.contabil.util.Util;
import org.slf4j.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.react.contabil.util.Util.comparaCodigos;

@ApplicationScoped
public class ContaServiceHandler {

    @Inject
    private Logger logger;

    @Inject
    private ContaDao dao;

    @Inject
    private SequencialDao sequencialDao;

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
            final ContaDO contaDO = conta.toDataObject();

            final ContaDO contaPai = this.pegaConta(contaDO.getContaPaiCodigo(),contaDO.getCodigoUsuario(),
                    "adicionar");

            contaDO.setNumero(this.novoNumero(contaPai));

            this.dao.inserir(contaDO);
            logger.info("adicionar :: {} adicionada com sucesso!", conta);

        } catch (ContabilException e) {
            logger.error("adicionar :: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro desconhecido ao adicionar %s", conta);
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
                .split("\\.")[nivel-1];
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
     * @throws UsuarioInvalidoException Conta nào pertence ao usuário
     */
    @NotNull @Valid
    private ContaDO pegaConta(@NotNull Long codigo, @NotNull Long codigoUsuario, String nomeMetodo)
            throws BancoDadosException, EntidadeNaoEncontradaException, UsuarioInvalidoException {

        final ContaDO contaDO = this.dao.procurar(codigo, false);
        if (contaDO == null) { // valida existencia
            String msg = String.format("Conta código %d não existe", codigo);
            logger.error("{} :: {}", nomeMetodo, msg);
            throw new EntidadeNaoEncontradaException(msg);
        } else if (!contaDO.getCodigoUsuario().equals(codigoUsuario)) {
            String msg = String.format("Conta pai código %s não pertence ao usuário código %s",
                    codigo, codigoUsuario);
            logger.error("{} :: {}", nomeMetodo, msg);
            throw new UsuarioInvalidoException(msg);
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

        final ContaDO contaDO = this.dao.procurar(codigo, false);
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
            final ContaDO contaDO = this.dao.procurar(conta.getCodigo(), false);
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
    @Transactional
    public void atualizar(Conta conta) throws ContabilException {
        try {
            logger.info("atualizar :: Atualizando {}", conta.toString());
            final ContaDO contaDO = this.dao.procurar(conta.getCodigo(), false);
            if (contaDO == null) {
                final String erro = String.format("%s não existe",
                        conta.toString());
                logger.error("atualizar :: {}", conta.toString());
                throw new EntidadeNaoEncontradaException(erro);
            }

            this.validaUsuarioConta(conta, contaDO);
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
     * Verifica se a conta pertence ao usuario
     * @param conta Conta recebida pelo endpoint
     * @param contaDO Conta persistida a ser modificada
     * @throws Exception
     */
    private void validaUsuarioConta(Conta conta, ContaDO contaDO) throws Exception {
        if (comparaCodigos(conta.getCodigoUsuario(), contaDO.getCodigoUsuario())) {
            String erro = String.format("Conta código %d não pertence ao usuário código %d",
                    contaDO.getCodigo(), conta.getCodigoUsuario());
            logger.error("validaUsuarioConta :: {}", erro);
            throw new UsuarioInvalidoException(erro);
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
    @Transactional
    public List<Conta> listar(Long codigoUsuario, String numero, String nome)
                throws BancoDadosException, ContabilException {

        final String msg = String.format("filtros codigo usuário: %d%s%s",
                codigoUsuario, numero == null ? "" : " numero: " + numero,
                nome == null ? "" : " nome: " + nome);

        try {
            logger.info("listar :: Procurando lista de contas {}", msg);
            final List<Conta> contas = this.dao.listar(codigoUsuario, numero, nome)
                    .stream().map(contaDO -> new Conta(contaDO, true)).collect(Collectors.toList());
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
    @Transactional
    public Conta procurar(Long codigo) throws ContabilException {

        final String msg = String.format("conta com código: %d", codigo);
        try {
            logger.info("procurar :: Procurando {}", msg);
            ContaDO contaDO = this.dao.procurar(codigo, true);
            if (contaDO == null) {
                final String erro = String.format("Conta com código %d não existe",
                        codigo);
                logger.error("procurar :: {}", erro);
                throw new EntidadeNaoEncontradaException(erro);
            }

            logger.info("procurar :: Atualizando saldo da {}", contaDO);
            contaDO = this.calculaSaldo(contaDO);
            final Conta conta = new Conta(contaDO);

            logger.info("procurar :: Procura de {} efetuada com sucesso", msg);
            return conta;
        } catch (EntidadeNaoEncontradaException | BancoDadosException e) {
            throw e;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro desconhecido" +
                    " ao procurar %s", msg);
            logger.error("adicionar :: {} Erro: {}", erro, e.getMessage(), e);
            throw new ContabilException(erro, e);
        }
    }

    /**
     * Monta balanacete para o usuário informado
     * @param codigoUsuario codigo do usuário
     * @return Lista de Conta do usuario para montar o balancete
     * @throws ContabilException Erro desconhecido ou BancoDadosException
     */
    @Transactional @NotNull
    public List<Conta> balancete(@NotNull Long codigoUsuario) throws ContabilException {
        try {
            logger.info("balancete :: Buscando contas do usuário código {} para montar o balancete" +
                    " ...", codigoUsuario);
            final List<ContaDO> contas = this.dao.listar(codigoUsuario, null, null);
            final List<Conta> retorno = new ArrayList<>();
            final Set<Conta> balancete = new TreeSet<>();
            for (final ContaDO contaDO : contas) {
                if (contaDO.getNumero().equals("01")) {
                    this.calculaSaldo(contaDO);
                    this.insereArvore(balancete, contaDO);
                    retorno.addAll(balancete);
                }
            }

            logger.info("balancete :: Busca de contas do usuário código {} para montar o balancete" +
                    " executada com sucesso!", codigoUsuario);

            return retorno;
        } catch (BancoDadosException e) {
            throw e;
        } catch (Exception e) {
            String erro = String.format("Ocorreu um erro desconhecido ao montar o balancete para" +
                    " o usuário código %d", codigoUsuario);
            logger.error("balancete :: {} Erro: {}", erro, e.getMessage(), e);
            throw new ContabilException(erro, e);
        }
    }

    private void insereArvore(final Set<Conta> balancete, ContaDO contaDO) {
        balancete.add(new Conta(contaDO));
        if (contaDO.getContasFilhas() != null) {
            for (final ContaDO filha : contaDO.getContasFilhas()) {
                this.insereArvore(balancete, filha);
            }
       }
    }

    private ContaDO calculaSaldo(ContaDO conta) throws Exception {
        logger.info("calculaSaldo :: Calculando saldo {}", conta);
        final Saldo saldo = new Saldo(new BigDecimal(0), new BigDecimal(0));
        if (conta.getContasFilhas() != null) {
            for (ContaDO contaFilha : conta.getContasFilhas()) {
                contaFilha = this.calculaSaldo(contaFilha);
                final Saldo saldoContaFilha = contaFilha.getSaldo();
                saldo.setTotalCredito(saldo.getTotalCredito().add(saldoContaFilha.getTotalCredito()));
                saldo.setTotalDebito(saldo.getTotalDebito().add(saldoContaFilha.getTotalDebito()));
            }
        }
        final Saldo saldoLocal = this.dao.saldoLocalConta(conta.getCodigo());
        saldo.setTotalCredito(saldo.getTotalCredito().add(saldoLocal.getTotalCredito()));
        saldo.setTotalDebito(saldo.getTotalDebito().add(saldoLocal.getTotalDebito()));
        saldo.calculaSaldo();
        conta.setSaldo(saldo);
        logger.info("calculaSaldo :: Saldo da conta {} encontrado com sucesso", conta);

        return conta;
    }


    /**
     * Converte lista de contaDO em contas
     * @param contas lista de contaDO
     * @param acao String acao
     * @return Lista de conta (DTO) convertidas
     */
    private List<Conta> converteParaDTO(List<ContaDO> contas, String acao) {
        logger.info("converteParaDTO :: Convertendo lista de ContaDO para Conta para {}", acao);
        final List<Conta> contasConvertidas = new ArrayList<>();

        for (final ContaDO contaDO : contas) {
            try {
                contasConvertidas.add(new Conta(contaDO));
            } catch (Exception e) {
                logger.error("converteParaDTO :: Ocorreu um erro ao converter {} em DTO para {}",
                        contaDO, acao, e);
            }
        }

        logger.info("converteParaDTO :: Lista de ContaDO convertida para Conta com sucesso para" +
                " {}", acao);

        return contasConvertidas;
    }
}
