package com.react.contabil.lancamento;

import com.react.contabil.dao.ContaDao;
import com.react.contabil.dao.LancamentoDao;
import com.react.contabil.dao.ValorDao;
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

    @Inject
    private Logger logger;

    @Inject
    private LancamentoDao dao;

    @Inject
    private ContaDao contaDao;

    @Inject
    private ValorDao valorDao;

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
            logger.info("adicionar :: Validando os {} valores de {} ...",
                    lancamentoDO.getValores().size(), lancamento);
            this.validaValores(lancamentoDO);
            logger.info("adicionar :: Adicionando {} ...", lancamento);

            lancamentoDO = this.dao.inserir(lancamentoDO);
            logger.info("adicionar :: {} adicionado com sucesso!",
                    lancamento);

            return new Lancamento(lancamentoDO);
        } catch (LancamentoInvalidoException e) {
            logger.error("adicionar :: {}", e.getMessage());
            throw e;
        } catch (BancoDadosException e) {
            final String erro = String.format("Ocorreu um erro de banco de " +
                    "dados ao adicionar %s", lancamento.toString());
            logger.error("adicionar :: {} Erro: {}", erro, e.getMessage());
            throw e;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro desconhecido" +
                    " ao adicionar %s", lancamento.toString());
            logger.error("adicionar :: {} Erro: {}", erro, e.getMessage(), e);
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


    // TODO: atualiza saldo do lançamento
//    private void atualizaSaldos(List<ValorDO> lista) {
//        for (final ValorDO valor : lista) {
//            valor.getValor().multiply()
//        }
//    }
//
//    private BigDecimal modifier(ValorDO valorDO) {
//        if (valorDO.getConta()) {
//
//        }
//    }
}
