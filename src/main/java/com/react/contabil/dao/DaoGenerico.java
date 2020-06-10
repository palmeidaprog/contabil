package com.react.contabil.dao;

import com.react.contabil.dataobject.Entidade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

/**
 * Classe de repositorio genérico
 * @param <T> Tipo da entidade que o DAO será para
 * @param <K> Tipo da primary key
 */
public abstract class DaoGenerico<T extends Entidade, K> {

    @Inject
    private Logger logger;

    private Class<T> type;

    public DaoGenerico() { }

    @Inject
    private EntityManagerFactory emf;

    @Inject
    protected EntityManager em;

    public DaoGenerico(Class<T> type) {
        this.type = type;
    }

    protected T find(K key) {
        if (key == null) {
            logger.error("procurar :: Chave {} do Objeto nao pode ser " +
                    "nulo", key.getClass().getSimpleName());
            return null;
        }
        return this.em.find(this.type, key);
    }


    public T create(T obj) {
        return this.em.merge(obj);
    }

    public T merge(T obj) {
        return this.em.merge(obj);
    }

    /**
     * Remove pelo Objeto
     * @param obj
     */
    public void delete(T obj) {
        this.em.remove(obj);
    }

    /**
     * Remove pela chave
     * @param chave pk
     */
    public void delete(K chave) {
        this.em.remove(chave);
    }


}
