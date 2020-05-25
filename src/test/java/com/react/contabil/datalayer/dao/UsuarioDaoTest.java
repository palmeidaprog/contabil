package com.react.contabil.datalayer.dao;

import com.react.contabil.TestUtils;
import com.react.contabil.datalayer.dataobject.UsuarioDO;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.junit.jupiter.api.Assertions.*;


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