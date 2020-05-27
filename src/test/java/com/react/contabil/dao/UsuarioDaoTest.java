package com.react.contabil.dao;

import com.react.contabil.TestUtils;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;


class UsuarioDaoTest {

    private UsuarioDao dao;

    private EntityManager em;

    private EntityTransaction et;

    @Test
    void inserir() {
//        this.dao = new UsuarioDao();
//        final UsuarioDO usuarioDO = TestUtils.getUsuarioDO();
//        try {
//            em = UsuarioDao.getEntityManager();
//            et = UsuarioDao.getTransaction(em);
//            et.begin();
//            this.dao.inserir(usuarioDO);
//            et.commit();
////            et = UsuarioDao.getTransaction(em);
////            et.begin();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            UsuarioDao.close(em, et, UsuarioDO.class.getSimpleName());
//        }
    }

}