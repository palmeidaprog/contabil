package com.react.contabil.configuracao;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

@ApplicationScoped
public class ProdutorJPA {
    @PersistenceContext
    private EntityManager em;

    @Produces
    @RequestScoped
    public EntityManager getEntityManager() {
        return em;
    }

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Produces
    @RequestScoped
    public EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
}
