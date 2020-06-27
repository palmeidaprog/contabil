package com.react.contabil.dao;

import com.react.contabil.conta.Conta;
import com.react.contabil.dataobject.ContaDO;
import com.react.contabil.excecao.BancoDadosException;
import com.react.contabil.util.Util;
import org.slf4j.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ContaDao extends DaoGenerico<ContaDO, Long> {

    @Inject
    private Logger logger;

    public ContaDao() {
        super(ContaDO.class);
    }

    /**
     * Adiciona nova conta
     * @param contaDO conta
     * @throws BancoDadosException Erro de banco
     */
    public void inserir(@Valid @NotNull ContaDO contaDO) throws BancoDadosException {
        try {
            this.create(contaDO);
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro de banco de" +
                    " dados ao adicionar %s", contaDO.toString());
            logger.error("inserir :: {} Erro: {}", erro, e.getMessage(), e);
            throw new BancoDadosException(erro, e);
        }
    }

    /**
     * Procurear conta por codigo
     * @param codigo codigo da conta
     * @param carregaValores Se verdadeiro carrega valores
     * @return Conta (null se nào existir)
     * @throws BancoDadosException Erro de banco
     */
    public ContaDO procurar(@NotNull Long codigo, boolean carregaValores) throws BancoDadosException {
        String jpql = "SELECT c FROM ContaDO c ";

        if (carregaValores) {
            jpql += "LEFT JOIN FETCH c.valores v ";
        }
        jpql += "WHERE c.codigo = :codigo ";

        try {
            logger.debug("procurar :: Procurando conta código: {}", codigo);
            final TypedQuery<ContaDO> query = this.em.createQuery(jpql,
                    ContaDO.class);
            query.setParameter("codigo", codigo);
            final ContaDO contaDO = query.getSingleResult();
            logger.debug("procurar :: {} encontrado com sucesso", contaDO);

            return contaDO;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro de banco de" +
                    " dados ao procurar Conta de código %d", codigo);
            logger.error("inserir :: {} Erro: {}", erro, e.getMessage(), e);
            throw new BancoDadosException(erro, e);
        }
    }


    /**
     * Procura saldo local da conta especifica
     * @param codigo codigo da conta
     * @return Saldo com dados sumarizados
     * @throws BancoDadosException
     */
    public Saldo saldoLocalConta(@NotNull Long codigo) throws BancoDadosException {
        String sql = "select v.tipo, sum(v.valor) from conta c " +
                "inner join valor v " +
                "on c.codigo = v.conta_codigo " +
                "where c.codigo = :codigo " +
                "group by v.tipo order by v.tipo ";

        try {
            logger.debug("procurar :: Procurando saldo conta código: {}", codigo);
            final Query query = this.em.createNativeQuery(sql);
            query.setParameter("codigo", codigo);
            final List<Object[]> saldos = query.getResultList();

            BigDecimal debitos = new BigDecimal(0);
            BigDecimal creditos = new BigDecimal(0);
            for (final Object[] saldo : saldos) {
                if (saldo[0].equals("DEBITO")) {
                    debitos = BigDecimal.valueOf((Double) saldo[1]);
                } else {
                    creditos = BigDecimal.valueOf((Double) saldo[1]);
                }
            }

            final Saldo saldo = new Saldo(debitos, creditos);
            logger.debug("procurar :: Saldo de codigo conta {} encontrado com sucesso", codigo);

            return saldo;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro de banco de" +
                    " dados ao procurar saldo da Conta de código %d", codigo);
            logger.error("inserir :: {} Erro: {}", erro, e.getMessage(), e);
            throw new BancoDadosException(erro, e);
        }
    }

    /**
     * Remover conta por código
     * @param conta contaDo a ser deletada
     * @throws BancoDadosException Erro de banco
     */
    public void remover(@NotNull ContaDO conta) throws BancoDadosException {
        try {
            this.delete(conta);
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro de banco de" +
                    " dados ao remover Conta de código %d", conta);
            logger.error("remover :: {} Erro: {}", erro, e.getMessage(), e);
            throw new BancoDadosException(erro, e);
        }
    }

    /**
     * Atualiza conta
     * @param contaDO Atualiza conta
     * @throws BancoDadosException
     */
    public void atualizar(@Valid @NotNull ContaDO contaDO) throws BancoDadosException {
        try {
            this.merge(contaDO);
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro de banco de" +
                    " dados ao atualizar %s", contaDO.toString());
            logger.error("atualizar :: {} Erro: {}", erro, e.getMessage(), e);
            throw new BancoDadosException(erro, e);
        }
    }

    /**
     * Procura lista baseado em filtros
     * @param codigoUsuario codigo usuario
     * @param numero numero da conta
     * @param nome nome da conta
     * @return lista de usuarios
     * @throws BancoDadosException
     */
    public List<ContaDO> listar(@NotNull Long codigoUsuario, String numero,
                                String nome) throws
            BancoDadosException {
        final String msg = String.format("filtros codigo usuário: %d%s%s",
                codigoUsuario, numero == null ? "" : " numero: " + numero,
                nome == null ? "" : " nome: " + nome);

        try {
            final CriteriaBuilder cb = this.em.getCriteriaBuilder();
            final CriteriaQuery<ContaDO> query = cb.createQuery(ContaDO.class);
            final Root<ContaDO> conta = query.from(ContaDO.class);

            final List<Predicate> predicados = new ArrayList<>();
            predicados.add(cb.equal(conta.get("codigoUsuario"), codigoUsuario));

            if (Util.isNotBlank(numero)) {
                predicados.add(cb.like(conta.get("numero"),numero.trim() + "%"));
            }

            if (Util.isNotBlank(nome)) {
                predicados.add(cb.like(cb.upper(conta.get("nome")),
                        "%" + nome.toUpperCase().trim() + "%"));
            }

            query.where(cb.and(predicados.toArray(
                    new Predicate[predicados.size()])));
            final TypedQuery<ContaDO> typedQuery = this.em.createQuery(query);
            return typedQuery.getResultList();

        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro de banco de" +
                    " dados ao listar contas com %s", msg);
            logger.error("atualizar :: {} Erro: {}", erro, e.getMessage(), e);
            throw new BancoDadosException(erro, e);
        }
    }

    /**
     * Procurar conta por código
     * @param codigo codigo da conta a ser procurado
     * @return contaDo
     * @throws BancoDadosException erro de banco encapsulado
     */
    public ContaDO buscar(@NotNull Long codigo) throws BancoDadosException {
        try {
            logger.info("buscar :: Buscando ContaDO código: {} ...", codigo);
            final ContaDO contaDO = super.find(codigo);

            logger.info("buscar :: {} adicionado com sucesso!", contaDO);
            return contaDO;
        } catch (Exception e) {
            final String error = String.format("Ocorreu um erro no banco" +
                    " ao procurar Conta código: %d", codigo);
            logger.error("buscar :: {} Erro: {}", error, e.getMessage(), e);
            throw new BancoDadosException(error);
        }
    }

}
