package com.react.contabil.dao;

import com.react.contabil.dataobject.LancamentoDO;
import com.react.contabil.excecao.BancoDadosException;
import com.react.contabil.lancamento.Lancamento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;

@Stateless
public class LancamentoDao extends DaoGenerico<LancamentoDO, Long> {

    private static final Logger LOGGER = LoggerFactory
                .getLogger(LancamentoDao.class);

    /**
     * Adiciona lançamento no banco de dados
     * @param lancamentoDO Lançamento a ser adicionado
     * @return Lançamento adicionado (recurso managed do JPA)
     * @throws BancoDadosException Erro de banco
     */
    public LancamentoDO inserir(LancamentoDO lancamentoDO) throws
            BancoDadosException {
        try {
            LOGGER.debug("adicionar :: Adicionando {} no banco de dados...",
                    lancamentoDO.toString());
            lancamentoDO = this.create(lancamentoDO);
            LOGGER.debug("adicionar :: {} adicionado no banco com sucesso!",
                    lancamentoDO.toString());

            return lancamentoDO;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro de banco " +
                    "ao adicionar %s", lancamentoDO.toString());
            LOGGER.error("adicionar :: {} Erro: {}", erro, e.getMessage(), e);
            throw new BancoDadosException(erro, e);
        }
    }
}
