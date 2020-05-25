package com.react.contabil.datalayer.dao;

import com.react.contabil.conta.Conta;
import com.react.contabil.datalayer.dataobject.ContaDO;
import com.react.contabil.excecao.BancoDadosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;

@Stateless
public class ContaDao extends DaoGenerico<ContaDO, Long> {

    private static final Logger LOGGER = LoggerFactory
                .getLogger(ContaDao.class);

    public ContaDao() {
        super(ContaDO.class);
    }

    /**
     * Adiciona nova conta
     * @param contaDO conta
     * @throws BancoDadosException Erro de banco
     */
    public void inserir(ContaDO contaDO) throws BancoDadosException {
        try {
            this.create(contaDO);
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro de banco de" +
                    " dados ao adicionar %s", contaDO.toString());
            LOGGER.error("inserir :: {} Erro: {}", erro, e.getMessage(), e);
            throw new BancoDadosException(erro, e);
        }
    }

    /**
     * Procurear conta por codigo
     * @param codigo codigo da conta
     * @return Conta (null se nào existir)
     * @throws BancoDadosException Erro de banco
     */
    public ContaDO procurar(Long codigo) throws BancoDadosException {
        try {
            return this.find(codigo);
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro de banco de" +
                    " dados ao procurar Conta de código %d", codigo);
            LOGGER.error("inserir :: {} Erro: {}", erro, e.getMessage(), e);
            throw new BancoDadosException(erro, e);
        }
    }
}
