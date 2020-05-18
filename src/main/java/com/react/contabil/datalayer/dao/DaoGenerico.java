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

//    private final static EntityManagerFactory emf;

    private Class<T> type;

    public DaoGenerico() { }

    @Inject
    private EntityManagerFactory emf;

    @Inject
    private EntityManager em;

//    static {
//        emf = Persistence.createEntityManagerFactory(
//                "primary");
//    }

//    public static EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }
//
//    public static EntityTransaction getTransaction(EntityManager em) {
//        return em.getTransaction();
//    }

    public DaoGenerico(Class<T> type) {
        this.type = type;
    }

    protected T procurar(K key) {
        if (key == null) {
            LOGGER.error("procurar :: Chave {} do Objeto nao pode ser " +
                    "nulo", key.getClass().getSimpleName());
            return null;
        }

//        EntityTransaction et = null;
//        EntityManager em = null;
//        T retorno = null;
//        try {
//            em = getEntityManager();
//            et = em.getTransaction();
//            et.begin();
//
//            retorno = em.find(this.type, key);
//            et.commit();
//            em.close();
//        } catch (Exception e) {
//            LOGGER.error("procurar :: Ocorreu um erro ao procurar o objeto" +
//                            " {} com chave primaria {}. Erro: {}", type.getSimpleName(),
//                    key.getClass().getSimpleName(), e.getMessage(), e);
//        } finally {
//            close(em, et, this.type.getSimpleName());
//        }
//        return retorno;
        return this.em.find(this.type, key);
    }


    public void inserir(T obj) {
        this.em.persist(obj);
    }


//    /**
//     * Adiciona objeto ao banco
//     * @param obj Objeto a ser adicionado
//     */
//    protected void inserir(T obj) {
//        if (obj == null) {
//            LOGGER.error("inserir :: Objeto {} não pode ser nulo para ser " +
//                    "adicionado", obj.getClass().getSimpleName());
//            return;
//        }
//
//        EntityTransaction et = null;
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            et = em.getTransaction();
//            et.begin();
//            LOGGER.debug("inserir :: Iniciada transação para adicionar " +
//                    "objeto {}", obj.getClass().getSimpleName());
//
//            em.persist(obj);
//            et.commit();
//            em.close();
//        } catch (Exception e) {
//            LOGGER.error("inserir :: Ocorreu um erro ao adicionar o objeto " +
//                            "{} ao banco. Erro: {}", obj.getClass().getSimpleName(),
//                    e.getMessage(), e);
//        } finally {
//            close(em, et, obj.getClass().getSimpleName());
//        }
//    }

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
