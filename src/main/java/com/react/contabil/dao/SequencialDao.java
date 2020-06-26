package com.react.contabil.dao;

import com.react.contabil.excecao.BancoDadosException;
import org.slf4j.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;

import java.math.BigInteger;

import static com.react.contabil.util.Constantes.Sequencial.PROX_SEQUENCIAL_NOT_EMPTY;

@Stateless
public class SequencialDao {

    @Inject
    private EntityManagerFactory emf;

    @Inject
    private EntityManager em;

    @Inject
    private Logger logger;

    private static final String SCHEMA = "contabil";

    public SequencialDao() {
        // construtor padrão
    }

    @SuppressWarnings("unchecked")
    public Long proximoCodigo(@NotNull(message = PROX_SEQUENCIAL_NOT_EMPTY) Tabela tabela)
                throws BancoDadosException {
        String sql = "SELECT AUTO_INCREMENT FROM information_schema.TABLES " +
                "WHERE TABLE_SCHEMA = :schema AND TABLE_NAME = :tabela";

        try {
            logger.info("proximoId :: Pesquisando novo codigo para tabela {}", tabela.getTabela());
            Query query = this.em.createNativeQuery(sql);
            query.setParameter("tabela", tabela.getTabela());
            query.setParameter("schema", SCHEMA);
            long id = ((BigInteger) query.getSingleResult()).longValue();
            ++id;
            logger.info("proximoId :: Novo id {} para tabela {}", id, tabela.getTabela());

            return id;
        } catch (NoResultException e) {
            String erro = String.format("Tabela %s não existe ou não teve seu sequencial id " +
                    "adicionado a tabela de sequenciais", tabela.getTabela());
            logger.error("proximoCodigo :: {} Erro: {}", erro, e.getMessage(), e);
            throw new BancoDadosException(erro, e);
        } catch (Exception e) {
            String erro = String.format("Ocorreu um erro de banco ao procurar por sequencial/id " +
                    "para tabela %s", tabela.getTabela());
            logger.error("proximoCodigo :: {} Erro: {}", erro, e.getMessage(), e);
            throw new BancoDadosException(erro, e);
        }
    }

    @Deprecated
    public Long proximoCodigoAntigo(@NotNull(message = PROX_SEQUENCIAL_NOT_EMPTY) Tabela tabela,
                              int quantidadeIds) throws BancoDadosException {
        String sql = "SELECT s.sequencial FROM sequencial s WHERE s.tabela = :tabela";

        try {
            logger.info("proximoId :: Pesquisando novo {} codigo(s) para tabela {}", quantidadeIds, tabela.getTabela());
            Query query = this.em.createNativeQuery(sql);
            query.setParameter("tabela", tabela.getTabela());
            final Long id = ((BigInteger) query.getSingleResult()).longValue();

            logger.info("proximoId :: Incrementando id em pra tabela {}", tabela.getTabela());

            sql = "UPDATE sequencial SET sequencial = sequencial + " + quantidadeIds +
                    " WHERE tabela = :tabela";
            query = this.em.createNativeQuery(sql);
            query.setParameter("tabela", tabela.getTabela());
            query.executeUpdate();

            final StringBuilder ids = new StringBuilder();
            for (int i = 0; i < quantidadeIds; i++) {
                ids.append(id + i).append(" ");
            }

            logger.info("proximoId :: Novo id para tabela {} ids: {}", tabela.getTabela(), ids.toString());

            return id;
        } catch (NoResultException e) {
            String erro = String.format("Tabela %s não existe ou não teve seu sequencial id " +
                    "adicionado a tabela de sequenciais", tabela.getTabela());
            logger.error("proximoId :: {} Erro: {}", erro, e.getMessage(), e);
            throw new BancoDadosException(erro, e);
        } catch (Exception e) {
            String erro = String.format("Ocorreu um erro de banco ao procurar por sequencial/id " +
                    "para tabela %s", tabela.getTabela());
            logger.error("proximoId :: {} Erro: {}", erro, e.getMessage(), e);
            throw new BancoDadosException(erro, e);
        }
    }


}
