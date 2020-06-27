package com.react.contabil.usuario;

import com.react.contabil.conta.Conta;
import com.react.contabil.dao.ContaDao;
import com.react.contabil.dao.SequencialDao;
import com.react.contabil.dao.Tabela;
import com.react.contabil.dao.UsuarioDao;
import com.react.contabil.dataobject.ContaDO;
import com.react.contabil.dataobject.UsuarioDO;
import com.react.contabil.excecao.BancoDadosException;
import com.react.contabil.excecao.ContabilException;
import com.react.contabil.excecao.EntidadeExistenteException;
import com.react.contabil.excecao.EntidadeNaoEncontradaException;
import org.slf4j.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UsuarioServiceHandler {

    @Inject
    private Logger logger;

    @Inject
    private UsuarioDao dao;

    @Inject
    private SequencialDao sequencialDao;

    @Inject
    private ContaDao contaDao;

    public UsuarioServiceHandler() {
    }

    /**
     * Adiciona novo usuario e cria sua estrutura basica de contas
     * @param usuario usuario a ser adicionado
     * @returns Usuario adicinado
     * @throws BancoDadosException Erro de Banco
     * @throws EntidadeExistenteException Usuario ja existre
     * @throws ContabilException Erro desconhecido
     */
    @Transactional
    public Usuario adicionar(Usuario usuario) throws BancoDadosException,
            EntidadeExistenteException, ContabilException {
        
        try {
            logger.debug("adicionar :: Adicionando usuário login {}",
                    usuario.getLogin());


            UsuarioDO usuarioDO = this.dao.procurar(null,
                                                    usuario.getLogin());

            if (usuarioDO != null) { // verifica existencia do usuario
                if (usuarioDO.isCongelado()) { // ativa usuario inativo
                    usuario.setCongelado(false);
                    this.dao.atualizar(usuarioDO);
                    logger.info("adicionar :: {} já existia, porém estava" +
                            " inativa. Conta reativada!", usuarioDO.toString());
                    return new Usuario(usuarioDO);
                } else { // usuario existente e ativo
                    final String msg = String.format("%s já existe!",
                            usuarioDO.toString());
                    logger.error("adicionar :: {}", msg);
                    throw new EntidadeExistenteException(msg);
                }
            }

            usuarioDO = usuario.toDataObject();
            usuarioDO = this.dao.inserir(usuarioDO);
            this.criaContasBasicas(usuarioDO);
            this.dao.atualizar(usuarioDO);
            logger.info("adicionar :: {} adicionado com sucesso!", usuarioDO);

            return new Usuario(usuarioDO);
        } catch (BancoDadosException | EntidadeExistenteException e) {
            throw e;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro desconhecido" +
                    " ao adicionar %s", usuario.toString());
            logger.error("adicionar :: {} Erro: {}", erro, e.getMessage(), e);
            throw new ContabilException(erro, e);
        }
    }

    /**
     * Suporte para adicionar, cria contas basicas para um novo usuário
     * @param usuarioDO Usuário dono da conta
     */
    private void criaContasBasicas(UsuarioDO usuarioDO) throws ContabilException {

        logger.info("criaContasBasicas :: Criando contas basicas do {}", usuarioDO);
        final List<ContaDO> contas = new ArrayList<>();
        final Long cod = usuarioDO.getCodigo();

        ContaDO ativo =  new ContaDO(cod, "Ativo", "01");
        contas.add(ativo);
        ContaDO passivo =  new ContaDO(cod, "Passivo", "02");
        contas.add(passivo);
        ContaDO patrimonio = new ContaDO(cod, "Patrimônio Líquido", "03");
        contas.add(patrimonio);
        ContaDO receita = new ContaDO(cod, "Receita", "04");
        contas.add(receita);
        ContaDO despesas = new ContaDO(cod, "Despesas e Custos", "05");
        contas.add(despesas);
        usuarioDO.setContas(contas);
        usuarioDO = this.dao.atualizar(usuarioDO);
        logger.info("criaContasBasicas :: Atualizando contas nivel 1 para {}", usuarioDO);


        ativo = this.procuraConta(usuarioDO.getContas(), "01");
        ativo.adicionaContaFilha(new ContaDO(cod, "Circulante", "01.01", ativo.getCodigo()));
        ativo.adicionaContaFilha(new ContaDO(cod, "Não Circulante", "01.02", ativo.getCodigo()));
        ativo.adicionaContaFilha(new ContaDO(cod, "Realizavel a Longo Prazo", "01.03", ativo.getCodigo()));
        ativo.adicionaContaFilha(new ContaDO(cod, "Investimentos", "01.04", ativo.getCodigo()));
        ativo.adicionaContaFilha(new ContaDO(cod, "Imobilizado", "01.05", ativo.getCodigo()));
        ativo.adicionaContaFilha(new ContaDO(cod, "Intangível", "01.06", ativo.getCodigo()));

        passivo = this.procuraConta(usuarioDO.getContas(), "02");
        passivo.adicionaContaFilha(new ContaDO(cod, "Circulante", "02.01", passivo.getCodigo()));
        passivo.adicionaContaFilha(new ContaDO(cod, "Não Circulante", "02.02", passivo.getCodigo()));

        patrimonio = this.procuraConta(usuarioDO.getContas(), "03");
        patrimonio.adicionaContaFilha(new ContaDO(cod, "Capital", "03.01", patrimonio.getCodigo()));

        receita = this.procuraConta(usuarioDO.getContas(), "04");
        receita.adicionaContaFilha(new ContaDO(cod, "Receita Bruta", "04.01", receita.getCodigo()));
        receita.adicionaContaFilha(new ContaDO(cod, "Outras Receitas Operacionais",
                "04.02", receita.getCodigo()));

        despesas = this.procuraConta(usuarioDO.getContas(), "05");
        despesas.adicionaContaFilha(new ContaDO(cod, "Despesas", "05.01", despesas.getCodigo()));
        despesas.adicionaContaFilha(new ContaDO(cod, "Custos", "05.02", despesas.getCodigo()));
        usuarioDO.setContas(contas);
        logger.info("criaContasBasicas :: Contas básicas do {} adicionadas com sucesso!", usuarioDO);
    }

    private ContaDO procuraConta(List<ContaDO> contas, String numero) {
        for (final ContaDO contaDO : contas) {
            if (contaDO.getNumero().equals(numero)) {
                return contaDO;
            }
        }
        return null;
    }

    /**
     * Procura usuário por codigo
     * @param codigo Código do usuário
     * @return Usuário caso encontrado
     * @throws BancoDadosException Erro de banco
     * @throws EntidadeNaoEncontradaException Usuário nao encontrado ou congelado
     * @throws ContabilException Erro desconhecido
     */
    public Usuario procurar(Long codigo, String login) throws
            ContabilException {
        try {
            final UsuarioDO usuarioDO = this.dao.procurar(codigo, login);
            if (usuarioDO == null) {
                this.criaExcecao(String.format("Usuário codigo %d " +
                        "não encontrado", codigo), "procurar");
            } else if (usuarioDO.isCongelado()) {
                this.criaExcecao(String.format("Usuário codigo %d " +
                        "encontra-se congelada", codigo), "procurar");
            }

            return new Usuario(usuarioDO);
        } catch (BancoDadosException | EntidadeNaoEncontradaException e) {
            throw e;
        } catch (Exception e) {
            final String erro = String.format("Occorreu um erro desconhecido" +
                    " ao procurar usuário código %d", codigo);
            logger.error("procurar :: {} Erro: {}", erro, e.getMessage(), e);
            throw new ContabilException(erro, e);
        }
    }

    /**
     * Suporte para procurar, joga excecao
     * @param msg  Mensagem da excecao
     * @throws EntidadeNaoEncontradaException
     */
    private void criaExcecao(String msg, String metodo) throws
            EntidadeNaoEncontradaException {
        logger.error("{} :: {}", metodo, msg);
        throw new EntidadeNaoEncontradaException(msg);
    }

    // TODO: estaAtiva(), congelaConta()
//    public void congelaConta(Usuario usuario) {
//        try {
//            final UsuarioDO usuarioDO = this.dao.procurar(usuario.getCodigo());
//            if (usuarioDO == null) {
//                this.criaExcecao();
//            }
//
//        } catch (Exception e) {
//
//        }
//    }
}
