package com.react.contabil.conta;

import com.react.contabil.datalayer.dao.UsuarioDao;
import com.react.contabil.datalayer.dataobject.UsuarioDO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.Transactional;

@ApplicationScoped
public class ContaServiceHandler {

    @Inject
    private UsuarioDao dao;


    public ContaServiceHandler() {
    }

    @Transactional
    public void inserir(UsuarioDO usuario) {

        try {
            this.dao.inserir(usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void inserir(UsuarioDO usuario) {
//        EntityManager em = null;
//        EntityTransaction et = null;
//
//        try {
//            em = UsuarioDao.getEntityManager();
//            et = UsuarioDao.getTransaction(em);
//            et.begin();
//            this.dao.inserir(usuario);
//            et.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            UsuarioDao.close(em, et, UsuarioDO.class.getSimpleName());
//        }
//    }

}
