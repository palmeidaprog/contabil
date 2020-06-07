package com.react.contabil.dao;

import com.react.contabil.dataobject.LancamentoDO;
import com.react.contabil.excecao.BancoDadosException;
import org.slf4j.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Stateless
public class LancamentoDao extends DaoGenerico<LancamentoDO, Long> {

    @Inject
    private Logger logger;

    /**
     * Adiciona lançamento no banco de dados
     * @param lancamentoDO Lançamento a ser adicionado
     * @return Lançamento adicionado (recurso managed do JPA)
     * @throws BancoDadosException Erro de banco
     */
    public LancamentoDO inserir(LancamentoDO lancamentoDO) throws
            BancoDadosException {
        try {
            logger.debug("adicionar :: Adicionando {} no banco de dados...",
                    lancamentoDO.toString());
            lancamentoDO = this.create(lancamentoDO);
            logger.debug("adicionar :: {} adicionado no banco com sucesso!",
                    lancamentoDO.toString());

            return lancamentoDO;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro de banco " +
                    "ao adicionar %s", lancamentoDO.toString());
            logger.error("adicionar :: {} Erro: {}", erro, e.getMessage(), e);
            throw new BancoDadosException(erro, e);
        }
    }

    /**
     * Procurar conta por código
     * @param codigo codigo do lançamento a ser procurado
     * @return lancamento
     * @throws BancoDadosException erro de banco encapsulado
     */
    public LancamentoDO buscar(@NotNull Long codigo) throws BancoDadosException {
        final String jpql = "SELECT l FROM LancamentoDO l " +
                "INNER JOIN FETCH l.valores v " +
                "WHERE l.codigo = :codigo";

        try {
            final TypedQuery<LancamentoDO> query = this.em.createQuery(jpql,
                    LancamentoDO.class);
            query.setParameter("codigo", codigo);
            logger.info("buscar :: Buscando LancamentoDO código: {} ...", codigo);

            final LancamentoDO lancamentoDO = query.getSingleResult();
            logger.info("buscar :: {} adicionado com sucesso!", lancamentoDO);
            return lancamentoDO;

        } catch (Exception e) {
            final String error = String.format("Ocorreu um erro no banco" +
                    " ao procurar LancamentoDO código: %d", codigo);
            logger.error("buscar :: {} Erro: {}", error, e.getMessage(), e);
            throw new BancoDadosException(error);
        }
    }

    /**
     * Erro
     * @param lancamentoDO
     * @return
     * @throws BancoDadosException
     */
    public LancamentoDO atualizar(@NotNull @Valid LancamentoDO lancamentoDO)
        throws BancoDadosException {
        try {
            logger.info("atualizar :: Atualizando {} ...", lancamentoDO);
            final LancamentoDO atualizado = super.merge(lancamentoDO);
            logger.info("atualizar :: {} atualizado com sucesso!", atualizado);

            return atualizado;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro ao atualizar" +
                    " %s no banco de dados");
            logger.error("atualizar :: {} Erro: {}", erro, e.getMessage(), e);
            throw new BancoDadosException(erro, e);
        }
    }

    /**
     * Remove ko lançamentoDO
     * @param lancamentoDO lançamento
     * @return Lancamento Deletado
     * @throws BancoDadosException Erro de bancos
     */
    public void remover(@NotNull @Valid LancamentoDO lancamentoDO) throws
            BancoDadosException {
        try {
            logger.info("remover :: Removendo {} ...", lancamentoDO);
            super.delete(lancamentoDO);

            logger.info("remover :: {} removido com sucesso!", lancamentoDO);
        } catch (Exception e) {
            final String erro = String.format("Erro ao remover %s do banco " +
                    "de dados", lancamentoDO);
            logger.error("remover :: {} Erro: {}", erro, e.getMessage(), e);
            throw new BancoDadosException(erro, e);
        }
    }

    public List<LancamentoDO> listar(@Valid FiltroLancamentos filtros) throws
            BancoDadosException {

        final String jpql = "SELECT l FROM LancamentoDO l " +
                "INNER JOIN FETCH l.valores v " +
                "WHERE l.codigoUsuario = :codUsuario " +
                "AND l.data >= :dataInicio AND l.data <= :dataFinal";
        try {
            logger.info("listar :: Procurando listas com {} ...", filtros);
            TypedQuery<LancamentoDO> query = this.em.createQuery(jpql,
                    LancamentoDO.class);
            query.setParameter("codUsuario", filtros.getCodigoUsuario());
            query.setParameter("dataInicio", filtros.getDataInicio());
            query.setParameter("dataFinal", filtros.getDataFinal());

            final List<LancamentoDO> lancamentos = query.getResultList();
            logger.info("listar :: Lista de lançamento com {} encontrou {} " +
                    "resultados", filtros, lancamentos.size());

            return lancamentos;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro ao procurar" +
                    " lista de lançamentos com %s", filtros);
            logger.error("listar :: {} Erro: {}", filtros, e.getMessage(), e);
            throw new BancoDadosException(erro, e);
        }
    }
}
