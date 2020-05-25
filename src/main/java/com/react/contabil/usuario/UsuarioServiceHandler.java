package com.react.contabil.usuario;

import com.react.contabil.datalayer.dao.UsuarioDao;
import com.react.contabil.datalayer.dataobject.ContaDO;
import com.react.contabil.datalayer.dataobject.UsuarioDO;
import com.react.contabil.excecao.BancoDadosException;
import com.react.contabil.excecao.ContabilException;
import com.react.contabil.excecao.EntidadeExistenteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UsuarioServiceHandler {

    private static final Logger LOGGER = LoggerFactory
                .getLogger(UsuarioServiceHandler.class);

    @Inject
    private UsuarioDao dao;

    public UsuarioServiceHandler() {
    }

    /**
     * Adiciona novo usuario e cria sua estrutura basica de contas
     * @param usuario usuario a ser adicionado
     * @throws BancoDadosException Erro de Banco
     * @throws EntidadeExistenteException Usuario ja existre
     * @throws ContabilException Erro desconhecido
     */
    @Transactional
    public void adicionar(Usuario usuario) throws BancoDadosException, 
            EntidadeExistenteException, ContabilException {
        
        try {
            LOGGER.debug("adicionar :: Adicionando usuário login {}",
                    usuario.getLogin());
            UsuarioDO usuarioDO = this.dao.procurar(usuario.getCodigo());
            if (usuarioDO != null) {
                if (usuarioDO.isCongelado()) { // ativo usuario inativo
                    usuario.setCongelado(false);
                    this.dao.atualizar(usuarioDO);
                    LOGGER.info("adicionar :: {} já existia, porém estava" +
                            " inativa. Conta reativada!", usuarioDO.toString());
                } else {
                    final String msg = String.format("%s já existe!",
                            usuarioDO.toString());
                    LOGGER.error("adicionar :: {}", msg);
                    throw new EntidadeExistenteException(msg);
                }
            }
            
            usuarioDO = usuario.toDataObject();
            usuarioDO.setContas(this.criaContasBasicas(usuarioDO));
            this.dao.inserir(usuarioDO);
            LOGGER.info("adicionar :: {} adicionado com sucesso!",
                    usuarioDO.toString());
        } catch (BancoDadosException | EntidadeExistenteException e) {
            throw e;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro desconhecido" +
                    " ao adicionar %s", usuario.toString());
            LOGGER.error("adicionar :: {} Erro: {}", erro, e.getMessage(), e);
            throw new ContabilException(erro, e);
        }
    }

    /**
     * Suporte para adicionar, cria contas basicas para um novo usuário
     * @param usuarioDO Usuário dono da conta
     * @return Lista de contas básicas
     */
    private List<ContaDO> criaContasBasicas(UsuarioDO usuarioDO) {
        final List<ContaDO> contas = new ArrayList<>();
        contas.add(this.criaConta(usuarioDO, "Ativo", "01"));
        contas.add(this.criaConta(usuarioDO, "Passivo", "02"));
        contas.add(this.criaConta(usuarioDO, "Patrimônio Líquido", "03"));
        contas.add(this.criaConta(usuarioDO, "Receitas", "04"));
        contas.add(this.criaConta(usuarioDO, "Despesas e Custos", "05"));

        return contas;
    }

    /**
     * Cria conta
     * @param usuarioDO usuario
     * @param nome Nome da conta
     * @param numero Numero daconta (ex: 01.02)
     * @return ContaDO
     */
    private ContaDO criaConta(UsuarioDO usuarioDO, String nome, String numero
            ) {
        final ContaDO contaDO = new ContaDO();
        contaDO.setNome(nome);
        contaDO.setNumero(numero);
        contaDO.setSaldo(new BigDecimal(0));
        contaDO.setUsuario(usuarioDO);

        return contaDO;
    }
}
