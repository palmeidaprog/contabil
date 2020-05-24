package com.react.contabil.usuario;

import com.react.contabil.datalayer.dao.UsuarioDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class UsuarioServiceHandler {

    private static final Logger LOGGER = LoggerFactory
                .getLogger(UsuarioServiceHandler.class);

    @Inject
    private UsuarioDao dao;

    public UsuarioServiceHandler() {
    }

    /**
     * Adiciona novo usuario e cria sua estrutura basica de contas
     * @param usuario usuario a ser adicionado
     */
    @Transactional
    public void adicionar(Usuario usuario) {
        try {
            LOGGER.debug("adicionar :: Adicionando usuário login {}",
                    usuario.getLogin());
        } catch (Exception e) {

        }
    }
}
