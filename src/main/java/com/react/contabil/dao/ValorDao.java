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
        String sql = "UPDATE valor v " +
                "INNER JOIN lancamento l ON v.lancamento_codigo = l.codigo " +
                "INNER JOIN conta con ON con.codigo = v.conta_codigo " +
                "SET v.saldo_conta = v.saldo_conta + :adicionar " +
                "WHERE con.codigo = :conta AND ";

        if (remover) {
            sql += "(l.data > :data OR (l.data = :data AND v.codigo > :codigo))";
        } else {
            sql += "l.data > :data";
        }

        try {
            logger.debug("atualizaSaldoInsercao :: Atualizando valores de " +
                    "saldo das contas antes de {} {}", acao, valor);
            final Query query = this.em.createNativeQuery(sql);
            query.setParameter("conta", valor.getConta().getCodigo());
            query.setParameter("adicionar", adicionar);
            logger.info("atualizaSaldo :: Data: {}", converteDateParaSqlDate(data));
            query.setParameter("data", converteDateParaSqlDate(data));

            if (remover) {
                query.setParameter("codigo", valor.getCodigo());
            }

            final int modificados = query.executeUpdate();
            logger.info("atualizaSaldoInsercao :: Atualizado {} valores " +
                    "para {} de {}", modificados, acao, valor);
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro no banco ao" +
                    " atualizar o saldo da conta nos valores para %s " +
                    "de %s", acao, valor);
            logger.error("atualizaSaldoInsercao :: {} Erro: {}", erro,
                    e.getMessage(), e);
            throw new BancoDadosException(erro, e);
        }
    }
}
