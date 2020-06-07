package com.react.contabil.dao;

import com.react.contabil.dataobject.ValorDO;
import com.react.contabil.excecao.BancoDadosException;
import com.react.contabil.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;

import static com.react.contabil.util.Util.converteDateParaSqlDate;

@Stateless
public class ValorDao extends DaoGenerico<ValorDO, Long> {

    @Inject
    private Logger logger;

    /**
     * Atualiza os saldos da conta contido nos valores
     * @param valor valor a ser atualizado
     * @param data Data do lançamento
     * @param remover Verdadeiro se remoção
     * @throws BancoDadosException Erro de banco
     */
    public void atualizaSaldo(@Valid @NotNull ValorDO valor,
                              @NotNull @PastOrPresent Date data,
                              double adicionar, boolean remover) throws
            BancoDadosException {

        adicionar = remover ? adicionar * -1 : adicionar;
        final String acao = remover ? "remover" : "inserir";
        String sql = "UPDATE v " +
                "SET v.saldo_conta = v.saldo_conta + :adicionar " +
                "FROM valor v INNER JOIN lancamento l " +
                "ON v.lancamento_codigo = l.codigo " +
                "WHERE l.data > :data";

        if (remover) {
            sql += " OR (l.data = :data AND v.codigo > :codigo')'";
        }



        try {
            logger.debug("atualizaSaldoInsercao :: Atualizando valores de " +
                    "saldo das contas antes de {} {}", acao, valor);
            final Query query = this.em.createNativeQuery(sql);
            query.setParameter("adicionar", adicionar);
            query.setParameter("data", converteDateParaSqlDate(data));

            if (remover) {
                query.setParameter("codigo", valor.getCodigo());
            }

            final int modificados = query.executeUpdate();
            logger.info("atualizaSaldoInsercao :: Atualizado {} valores " +
                    "para {} de {}", modificados, acao, valor);
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro no banco ao" +
                    " atualizar o saldo da conta nos valores para {} " +
                    "de {}", acao, valor);
            logger.error("atualizaSaldoInsercao :: {} Erro: {}", erro,
                    e.getMessage(), e);
            throw new BancoDadosException(erro, e);
        }
    }
}
