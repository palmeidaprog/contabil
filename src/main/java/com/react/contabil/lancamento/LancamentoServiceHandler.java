package com.react.contabil.lancamento;

import com.react.contabil.dao.LancamentoDao;
import com.react.contabil.dataobject.LancamentoDO;
import com.react.contabil.dataobject.TipoValor;
import com.react.contabil.dataobject.ValorDO;
import com.react.contabil.excecao.BancoDadosException;
import com.react.contabil.excecao.ContabilException;
import com.react.contabil.excecao.LancamentoInvalidoException;
import com.react.contabil.util.Constantes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class LancamentoServiceHandler {

    private static final Logger LOGGER = LoggerFactory
                .getLogger(LancamentoServiceHandler.class);

    @Inject
    private LancamentoDao dao;

    public LancamentoServiceHandler() { }

    /**
     * Adiciona novo lancamento
     * @param lancamento lançamento a ser adicionado
     * @return Lançamento adicionado
     */
    public Lancamento adicionar(@NotNull(message = Constantes.Lancamento.NOT_NULL)
                                @Valid Lancamento lancamento) throws
            ContabilException {

        try {
            LancamentoDO lancamentoDO = lancamento.toDataObject();
            LOGGER.info("adicionar :: Validando os {} valores de {} ...",
                    lancamentoDO.getValores().size(), lancamento.toString());
            this.validaValores(lancamentoDO);
            LOGGER.info("adicionar :: Adicionando {} ...", lancamento
                    .toString());

            lancamentoDO = this.dao.inserir(lancamentoDO);
            LOGGER.error("adicionar :: {} adicionado com sucesso!",
                    lancamento.toString());

            return new Lancamento(lancamentoDO);
        } catch (LancamentoInvalidoException e) {
            LOGGER.error("adicionar :: {}", e.getMessage());
            throw e;
        } catch (BancoDadosException e) {
            final String erro = String.format("Ocorreu um erro de banco de " +
                    "dados ao adicionar %s", lancamento.toString());
            LOGGER.error("adicionar :: {} Erro: {}", erro, e.getMessage());
            throw e;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro desconhecido" +
                    " ao adicionar %s", lancamento.toString());
            LOGGER.error("adicionar :: {} Erro: {}", erro, e.getMessage(), e);
            throw new ContabilException(erro, e);
        }
    }


    /**
     * Valida se o total de débitos e créditos que compoem o lançamento são
     * iguais
     * @param lancamentoDO Lançamento a ser validado
     * @throws LancamentoInvalidoException Caso total de débito e créditos
     * sejam diferentes
     */
    private void validaValores(LancamentoDO lancamentoDO) throws
            LancamentoInvalidoException {
        double debitos = 0;
        double creditos = 0;

        //final BigDecimal creditos = new BigDecimal(0);
        for (final ValorDO valor : lancamentoDO.getValores()) {
            if (valor.getTipo() == TipoValor.DEBITO) {
                debitos += valor.getValor().doubleValue();
            } else {
                creditos += valor.getCodigo().doubleValue();
            }
        }

        if (debitos != creditos) {
            throw new LancamentoInvalidoException(String.format("Lançamento" +
                    " código %d necessita de valor total de débito e crédito " +
                    "sejam iguais", lancamentoDO.getCodigo()));
        }
    }


}
