package com.react.contabil.datalayer.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Classe de repositorio genérico
 * @param <T> Tipo da entidade que o DAO será para
 * @param <K> Tipo da primary key
 */
public abstract class DaoGenerico<T, K> {

    private static final Logger LOGGER = LoggerFactory
    			.getLogger(DaoGenerico.class);

    private Class<T> type;

    public DaoGenerico() { }

    @Inject
    private EntityManagerFactory emf;

    @Inject
    private EntityManager em;

    public DaoGenerico(Class<T> type) {
        this.type = type;
    }

    protected T procurar(K key) {
        if (key == null) {
            LOGGER.error("procurar :: Chave {} do Objeto nao pode ser " +
                    "nulo", key.getClass().getSimpleName());
            return null;
        }
        return this.em.find(this.type, key);
    }


    public void inserir(T obj) {
        this.em.persist(obj);
    }

    /**
     * Da roll back e fecha entity manager
     * @param em Entity Manager
     * @param et Transação
     * @param objName Nome do tipo do objeto
     */
    public static void close(EntityManager em, EntityTransaction et,
                       String objName) {
        if (et != null && et.isActive()) {
            et.rollback();
            LOGGER.debug("close :: Transação ROLLBACK para objeto {}",
                    objName);
        }

        if (em != null && em.isOpen()) {
            em.close();
            LOGGER.debug("close :: Fechando o entity manager");
        }
    }


}
