package com.react.contabil.dao;

import com.react.contabil.dataobject.UsuarioDO;
import com.react.contabil.excecao.BancoDadosException;
import com.react.contabil.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;

@Stateless
public class UsuarioDao extends DaoGenerico<UsuarioDO, Long> {

    private static final Logger LOGGER = LoggerFactory
    			.getLogger(UsuarioDao.class);

    public UsuarioDao() {
        super(UsuarioDO.class);
    }


    /**
     * Procura usuario pelo codigo
     * @param codigo codigo do usuario
     * @return Usuario
     * @throws BancoDadosException Exceção do banco
     */
    public UsuarioDO procurar(Long codigo) throws BancoDadosException {
        try {
            return this.find(codigo);
        } catch (Exception e) {
            final String msg = String.format("Ocorreu um erro ao procurar " +
                    "o usuário código: ", codigo);
            LOGGER.error("procurar :: {} Erro: {}", msg, e.getMessage(), e);
            throw new BancoDadosException(msg, e);
        }
    }

    /**
     * Inserir usuario
     * @param usuario usuario a ser inserido
     * @throws BancoDadosException
     */
    public void inserir(UsuarioDO usuario) throws BancoDadosException {
        try {
            this.create(usuario);
        } catch (Exception e) {
            final String msg = String.format("Ocorreu um erro ao inserir {}",
                    usuario.toString());
            LOGGER.error("inserir :: {} Erro: {}", msg, e.getMessage(), e);
            throw new BancoDadosException(msg, e);
        }
    }

    /**
     * Atualiza usuario
     * @param usuarioDo usuario
     * @throws BancoDadosException Excecao de banco
     */
    public void  atualizar(UsuarioDO usuarioDo) throws BancoDadosException {
        try {
            this.merge(usuarioDo);
        } catch (Exception e) {
            final String msg = String.format("Ocorreu um erro ao atualizar" +
                    " {}", usuarioDo.toString());
            LOGGER.error("atualizar :: {} Erro: {}", msg, e.getMessage(), e);
            throw new BancoDadosException(msg, e);
        }
    }

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

        if (Util.isNotBlank(login)) {
            sb.append("login ");
            sb.append(login);
        }

        if (Util.isNotBlank(nome)) {
            sb.append("nome ");
            sb.append(nome);
        }

        return sb.toString();
    }

}
