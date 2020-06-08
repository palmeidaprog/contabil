package com.react.contabil.dao;

import com.react.contabil.dataobject.UsuarioDO;
import com.react.contabil.excecao.BancoDadosException;
import com.react.contabil.excecao.ParametrosInvalidosException;
import com.react.contabil.util.Util;
import org.slf4j.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Stateless
public class UsuarioDao extends DaoGenerico<UsuarioDO, Long> {

    @Inject
    private Logger logger;

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
            logger.error("procurar :: {} Erro: {}", msg, e.getMessage(), e);
            throw new BancoDadosException(msg, e);
        }
    }

    /**
     * Procura usuario por login ou codigo
     * @param codigo codigo do usuario
     * @param login login do usuario
     * @return Usuario
     * @throws BancoDadosException
     */
    public UsuarioDO procurar(Long codigo, String login) throws
            BancoDadosException, ParametrosInvalidosException {

        if (codigo == null && Util.isBlank(login)) {
            final String msg = "Ao procurar usuário ou login ou" +
                    " conta deve ser informado";
            logger.error("procurar :: {}", msg);
            throw new ParametrosInvalidosException(msg);
        }

        try {
            logger.info("procurar :: Procurando usuário com código: {} " +
                    "login: {} ...", codigo, login);
            final CriteriaBuilder cb = this.em.getCriteriaBuilder();
            final CriteriaQuery<UsuarioDO> cq = cb.createQuery(UsuarioDO.class);
            final Root<UsuarioDO> usuario = cq.from(UsuarioDO.class);

            if (codigo != null) {
                cq.where(cb.equal(usuario.get("codigo"), codigo));
                logger.info("procurar :: Procurando usuário por código: {}",
                        codigo);
            } else {
                cq.where(cb.equal(usuario.get("login"), login));
                logger.info("procurar :: Procurando usuário por login: {}",
                        login);
            }

            TypedQuery<UsuarioDO> query = this.em.createQuery(cq);

            final UsuarioDO usuarioDO = query.getSingleResult();
            logger.info("procurar :: {} encontrado com sucesso!",
                    usuarioDO.toString());

            return usuarioDO;
        } catch (NoResultException e) {
            final String erro = String.format("Não foi encontrado usuário" +
                    " com código: %d login: %s", codigo, login);
            logger.info("procurar :: {}", erro);
            return null;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro ao procurar" +
                    " o usuário código: %d login: %s", codigo,  login);
            logger.error("procurar :: {} Erro: {}", erro, e.getMessage(), e);
            throw new BancoDadosException(erro, e);
        }
    }

    /**
     * Inserir usuario
     * @param usuario usuario a ser inserido
     * @throws BancoDadosException
     */
    public UsuarioDO inserir(UsuarioDO usuario) throws BancoDadosException {
        try {
            return this.create(usuario);
        } catch (Exception e) {
            final String msg = String.format("Ocorreu um erro ao inserir %s",
                    usuario.toString());
            logger.error("inserir :: {} Erro: {}", msg, e.getMessage(), e);
            throw new BancoDadosException(msg, e);
        }
    }

    /**
     * Atualiza usuario
     * @param usuarioDo usuario
     * @throws BancoDadosException Excecao de banco
     */
    public UsuarioDO atualizar(UsuarioDO usuarioDo) throws BancoDadosException {
        try {
            return this.merge(usuarioDo);
        } catch (Exception e) {
            final String msg = String.format("Ocorreu um erro ao atualizar" +
                    " {}", usuarioDo.toString());
            logger.error("atualizar :: {} Erro: {}", msg, e.getMessage(), e);
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
