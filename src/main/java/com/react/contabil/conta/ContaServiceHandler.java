package com.react.contabil.conta;

import com.react.contabil.datalayer.dao.ContaDao;
import com.react.contabil.datalayer.dataobject.ContaDO;
import com.react.contabil.excecao.BancoDadosException;
import com.react.contabil.excecao.ContabilException;
import com.react.contabil.excecao.EntidadeExistenteException;
import com.react.contabil.excecao.EntidadeNaoEncontradaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class ContaServiceHandler {

    private static final Logger LOGGER = LoggerFactory
                .getLogger(ContaServiceHandler.class);

    @Inject
    private ContaDao dao;


    public ContaServiceHandler() {
    }

    /**
     * Adicionar conta
     * @param conta conta a ser adicionada
     * @throws EntidadeExistenteException Conta adicionada já existe
     * @throws BancoDadosException Erro de banco
     * @throws ContabilException Erro desconhecido
     */
    @Transactional
    public void adicionar(Conta conta) throws EntidadeExistenteException,
            BancoDadosException, ContabilException {
        try {
            LOGGER.debug("adicionar :: Adicionando {} ...", conta.toString());

            ContaDO contaDO = this.dao.procurar(conta.getCodigo());
            if (contaDO != null) {
                final String msg = String.format("A %s já existe!",
                        conta.toString());
                LOGGER.error("adicionar :: {}", msg);
                throw new EntidadeExistenteException(msg);
            }
            contaDO = conta.toDataObject();
            this.dao.inserir(contaDO);
            LOGGER.info("adicionar :: {} adicionada com sucesso!",
                    conta.toString());
        } catch (EntidadeExistenteException | BancoDadosException e) {
            throw e;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro desconhecido" +
                    " ao adicionar %s", conta.toString());
            LOGGER.error("adicionar :: {} Erro: {}", erro, e.getMessage(), e);
            throw new ContabilException(erro, e);
        }
    }

    // TODO: continuar remover conta()
//    public void remover(Conta conta) throws EntidadeNaoEncontradaException,
//            BancoDadosException, ContabilException {
//        try {
//            final ContaDO contaDO = this.dao.procurar(conta.getCodigo());
//            if (contaDO == null) {
//
//            }
//
//
//        } catch (EntidadeNaoEncontradaException | BancoDadosException e) {
//            throw e;
//        } catch (Exception e) {
//            final String erro = String.format("Ocorreu um erro desconhecido" +
//                    " ao adicionar %s", conta.toString());
//            LOGGER.error("adicionar :: {} Erro: {}", erro, e.getMessage(), e);
//            throw new ContabilException(erro, e);
//        }
//    }




}
