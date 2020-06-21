package com.react.contabil.usuario;

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
import java.math.BigDecimal;
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
            usuarioDO.setCodigo(this.sequencialDao.proximoCodigo(Tabela.USUARIO, 1));
            usuarioDO = this.dao.inserir(usuarioDO);
            usuarioDO.setContas(this.criaContasBasicas(usuarioDO));
            this.dao.atualizar(usuarioDO);
            logger.info("adicionar :: {} adicionado com sucesso!",
                    usuarioDO.toString());

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
     * @return Lista de contas básicas
     */
    private List<ContaDO> criaContasBasicas(UsuarioDO usuarioDO) throws ContabilException {
        long id;
        try {
            id = this.sequencialDao.proximoCodigo(Tabela.CONTA, 5);
        } catch (Exception e) {
            String erro = String.format("Ocorreu um erro ao gerar códido para contas para %s", usuarioDO);
            logger.error("criaContasBasicas :: {} Erro: {} ", erro, e.getMessage(), e);
            throw new ContabilException(erro, e);
        }
        final List<ContaDO> contas = new ArrayList<>();
        contas.add(this.criaConta(id, usuarioDO, "Ativo", "01"));
        contas.add(this.criaConta(id + 1, usuarioDO, "Passivo", "02"));
        contas.add(this.criaConta(id + 2, usuarioDO, "Patrimônio Líquido", "03"));
        contas.add(this.criaConta(id + 3, usuarioDO, "Receitas", "04"));
        contas.add(this.criaConta(id + 4, usuarioDO, "Despesas e Custos", "05"));

        return contas;
    }

    /**
     * Cria conta
     * @param usuarioDO usuario
     * @param nome Nome da conta
     * @param numero Numero daconta (ex: 01.02)
     * @return ContaDO
     */
    private ContaDO criaConta(Long codigo, UsuarioDO usuarioDO, String nome, String numero) {
        final ContaDO contaDO = new ContaDO();
        contaDO.setCodigo(codigo);
        contaDO.setNome(nome);
        contaDO.setNumero(numero);
        contaDO.setSaldo(new BigDecimal(0));
        contaDO.setCodigoUsuario(usuarioDO.getCodigo());

        return contaDO;
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
