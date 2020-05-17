package com.react.contabil.conta;

import com.react.contabil.datalayer.dao.UsuarioDao;
import com.react.contabil.datalayer.dataobject.UsuarioDO;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;


public class ContaServiceHandler {

    private final UsuarioDao dao;


    public ContaServiceHandler() {
        this.dao = new UsuarioDao();
    }

    public void inserir(UsuarioDO usuario) {
        EntityManager em = null;
        EntityTransaction et = null;

        try {
            em = UsuarioDao.getEntityManager();
            et = UsuarioDao.getTransaction(em);
            et.begin();
            this.dao.inserir(usuario);
            et.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            UsuarioDao.close(em, et, UsuarioDO.class.getSimpleName());
        }
    }

}
