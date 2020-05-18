package com.react.contabil.datalayer.dao;

import com.react.contabil.datalayer.dataobject.UsuarioDO;
import com.react.contabil.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsuarioDao extends DaoGenerico<UsuarioDO, Long> {

    private static final Logger LOGGER = LoggerFactory
    			.getLogger(UsuarioDao.class);

    public UsuarioDao() {
        super(UsuarioDO.class);
    }

//    public UsuarioDO busca(Long id, String login, String nome) throws
//            PersistenciaException {
//
//        final String msg = String.format("usuario com filtros id %d login " +
//                "%s nome %s", id, login, nome);
//        try {
//            LOGGER.debug("busca :: Procurando {} ...", msg);
//            //this.
//            throw new NotImplementedYetException();
//
//        } catch (Exception e) {
//            final String error = String.format("Ocorreu um erro de banco " +
//                    "ao buscar %s", msg);
//            LOGGER.error("busca :: {}  Erro: {}", error, e.getMessage(), e);
//            throw new PersistenciaException(error, e);
//        }
//    }

    /**
     * Monta mensagem de log
     * @param id id do usuario
     * @param login login do usuário
     * @param nome nome ou filtro aplicados aos nomes do usuarios
     * @return String formata para log e excecoes
    */
    private String mensagemLog(Long id, String login, String nome) {
        final StringBuilder sb = new StringBuilder("usuário com filtro ");

        if (id != null) {
            sb.append("id ");
            sb.append(id);
        }

        if (StringUtil.isNotBlank(login)) {
            sb.append("login ");
            sb.append(login);
        }

        if (StringUtil.isNotBlank(nome)) {
            sb.append("nome ");
            sb.append(nome);
        }

        return sb.toString();
    }

    public void inserir(UsuarioDO usuarioDO) {
        super.inserir(usuarioDO);
    }

}
