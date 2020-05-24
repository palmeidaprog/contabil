package com.react.contabil.conta;

import com.react.contabil.datalayer.dao.UsuarioDao;
import com.react.contabil.datalayer.dataobject.UsuarioDO;
import com.react.contabil.usuario.Usuario;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class ContaServiceHandler {

    @Inject
    private UsuarioDao dao;


    public ContaServiceHandler() {
    }

    @Transactional
    public void inserir(Usuario usuario) {

        try {
//            if (this.dao.procurar(usuario.getCodigo()) != null) {
//                this.
//            }



            //this.dao.inserir(usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void criaContasBasicas(UsuarioDO usuario) {

    }



}
