package com.react.contabil.dao;

import com.react.contabil.dataobject.ContaDO;
import com.react.contabil.excecao.BancoDadosException;
import com.react.contabil.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
    public void inserir(ContaDO contaDO) throws BancoDadosException {
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
     * @return Conta (null se nào existir)
     * @throws BancoDadosException Erro de banco
     */
    public ContaDO procurar(Long codigo) throws BancoDadosException {
        try {
            return this.find(codigo);
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro de banco de" +
                    " dados ao procurar Conta de código %d", codigo);
            logger.error("inserir :: {} Erro: {}", erro, e.getMessage(), e);
            throw new BancoDadosException(erro, e);
        }
    }

    /**
     * Remover conta por código
     * @param codigo codigo da conta
     * @throws BancoDadosException Erro de banco
     */
    public void remover(Long codigo) throws BancoDadosException {
        try {
            this.delete(codigo);
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro de banco de" +
                    " dados ao remover Conta de código %d", codigo);
            logger.error("remover :: {} Erro: {}", erro, e.getMessage(), e);
            throw new BancoDadosException(erro, e);
        }
    }

    /**
     * Atualiza conta
     * @param contaDO Atualiza conta
     * @throws BancoDadosException
     */
    public void atualizar(ContaDO contaDO) throws BancoDadosException {
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
    public List<ContaDO> listar(Long codigoUsuario, String numero, String nome) throws
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
                predicados.add(cb.like(cb.upper(conta.get("numero")),
                        numero.toUpperCase().trim() + "%"));
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
}
