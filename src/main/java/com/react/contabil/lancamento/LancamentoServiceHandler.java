package com.react.contabil.lancamento;

import com.react.contabil.dao.ContaDao;
import com.react.contabil.dao.FiltroLancamentos;
import com.react.contabil.dao.LancamentoDao;
import com.react.contabil.dao.SequencialDao;
import com.react.contabil.dao.Tabela;
import com.react.contabil.dao.ValorDao;
import com.react.contabil.dataobject.ContaDO;
import com.react.contabil.dataobject.LancamentoDO;
import com.react.contabil.dataobject.TipoValor;
import com.react.contabil.dataobject.ValorDO;
import com.react.contabil.excecao.BancoDadosException;
import com.react.contabil.excecao.ContabilException;
import com.react.contabil.excecao.LancamentoInvalidoException;
import org.slf4j.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static com.react.contabil.util.Constantes.Lancamento.NOT_NULL;
import static com.react.contabil.util.Util.isBlank;


@ApplicationScoped
public class LancamentoServiceHandler {

    @Inject
    private Logger logger;

    @Inject
    private LancamentoDao dao;

    @Inject
    private ValorDao valorDao;

    @Inject
    private ContaDao contaDao;

    @Inject
    private SequencialDao sequencialDao;

    /**
     * Adiciona novo lancamento
     *
     * @param lancamento lançamento a ser adicionado
     * @return Lançamento adicionado
     */
    @Transactional
    public void adicionar(@NotNull(message = NOT_NULL)
                                @Valid Lancamento lancamento) throws
            ContabilException {

        try {
            LancamentoDO lancamentoDO = lancamento.toDataObject();
            logger.info("adicionar :: Validando os {} valores de {} ...",
                    lancamentoDO.getValores().size(), lancamento);
            this.validaValores(lancamentoDO);
            logger.info("adicionar :: Adicionando {} ...", lancamento);

//            logger.info("adicionar :: Atualizando saldos das contas para o {}", lancamento);
//            this.atualizaSaldos(lancamentoDO.getValores(), lancamento.getData(),
//                    false);
//            logger.info("adicionar :: Saldo de {} atualizados com sucesso!",
//                    lancamento);

            final List<ValorDO> valores = lancamentoDO.getValores();
            lancamentoDO.setValores(null);
            lancamentoDO = this.dao.inserir(lancamentoDO);
            lancamentoDO.setValores(valores);
            for (final ValorDO valorDO : lancamentoDO.getValores()) {
                valorDO.setCodigoLancamento(lancamentoDO.getCodigo());
                valorDO.setSaldoConta(BigDecimal.valueOf(0));
            }
            lancamentoDO = this.dao.atualizar(lancamentoDO);
            logger.info("adicionar :: {} adicionado com sucesso!", lancamento);
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

    @Valid @NotNull @Transactional
    private ModoAtualizacao verificaModoAtualizacao(
                        @NotNull(message = NOT_NULL)
                        @Valid Lancamento lancamento) throws
            ContabilException {
        try {
            LancamentoDO lancamentoDO = lancamento.toDataObject();
            final LancamentoDO lancamentoAntigo = this.dao.buscar(
                    lancamento.getCodigo());

            logger.info("verificaModoAtualizacao :: Validando os {} valores de {} ...",
                    lancamentoDO.getValores().size(), lancamento);
            this.validaValores(lancamentoDO);
            logger.info("verificaModoAtualizacao :: Adicionando {} ...", lancamento);

            logger.info("verificaModoAtualizacao :: Atualizando saldos das contas para o " +
                    "{}", lancamento);

            // atualiza em caso de modificacoes apenas de lançamento
            // remove e readiciona fora do attached
            if (!this.valoresModificou(lancamentoDO.getValores(),
                    lancamentoAntigo.getValores())) {
                lancamentoDO = this.dao.atualizar(lancamentoDO);
                logger.info("verificaModoAtualizacao :: Atualização de {} " +
                        "efetuada com sucesso!", lancamentoDO);
                return ModoAtualizacao.ATUALIZA;
            }
            return ModoAtualizacao.REMOVE_ADICIONA;
        } catch (LancamentoInvalidoException e) {
            logger.error("verificaModoAtualizacao :: {}", e.getMessage());
            throw e;
        } catch (BancoDadosException e) {
            final String erro = String.format("Ocorreu um erro de banco de " +
                    "dados ao adicionar %s", lancamento.toString());
            logger.error("verificaModoAtualizacao :: {} Erro: {}", erro, e.getMessage());
            throw e;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro desconhecido" +
                    " ao adicionar %s", lancamento.toString());
            logger.error("verificaModoAtualizacao :: {} Erro: {}", erro, e.getMessage(), e);
            throw new ContabilException(erro, e);
        }
    }



    public void atualizar(@NotNull(message = NOT_NULL)
                                @Valid Lancamento lancamento) throws
            ContabilException {

        ModoAtualizacao modo = this.verificaModoAtualizacao(lancamento);
        try {
            if (modo == ModoAtualizacao.REMOVE_ADICIONA) {
                this.remover(lancamento);
                this.adicionar(lancamento);
                logger.info("atualizar :: Atualização de {} executada com " +
                        "sucesso!", lancamento);
            }
        } catch (Exception e) {
            logger.error("atualizar :: Ocorreu um erro ao atualizar {} " +
                    "Erro: {}", lancamento, e.getCause() != null ?
                    e.getCause().getMessage() : e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Remove lançamento
     * @param lancamento lancamento a ser removido
     * @throws ContabilException Erros
     */
    public void remover(@NotNull(message = NOT_NULL) @Valid Lancamento
                                lancamento) throws ContabilException {
        try {
            logger.info("remover :: Removendo {} ...", lancamento);
            final LancamentoDO lancamentoDO = lancamento.toDataObject();
            this.atualizaSaldos(lancamentoDO.getValores(),
                                lancamentoDO.getData(), true);
            this.dao.remover(lancamentoDO);
            logger.info("remover :: {} removido com sucesso", lancamento);
        } catch (BancoDadosException e) {
            final String erro = String.format("Erro ao remover %s do banco",
                    lancamento);
            logger.error("remover :: {} Erro: {}", erro, e.getMessage());
            throw e;
        } catch (Exception e) {
            final String erro = String.format("Erro ao remover %s", lancamento);
            logger.error("remover :: {} Erro: {}", erro, e.getMessage(), e);
            throw new ContabilException(erro, e);
        }
    }

    /**
     * Verifica se houve mudanças nos valores
     * @param antigos
     * @param novos
     * @return
     */
    private boolean valoresModificou(@NotEmpty List<ValorDO> antigos,
                                     @NotEmpty List<ValorDO> novos) {
        final Map<Long, ValorDO> map = new HashMap<>();

        for (final ValorDO antigo : antigos) {
            map.put(antigo.getCodigo(), antigo);
        }

        for (final ValorDO novo : novos) {
            boolean encontrou = map.containsKey(novo.getCodigo());
            if (!encontrou) {
                return true; // recém adicionado
            }

            final ValorDO antigo = map.get(novo.getCodigo());
            if (!antigo.getValor().equals(novo.getCodigo())) {
                return true; // valor modificado
            }

            map.remove(novo.getCodigo());
        }

        return map.size() > 0; // se restar algo é porque foi removido
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
                creditos += valor.getValor().doubleValue();
            }
        }

        if (debitos != creditos) {
            throw new LancamentoInvalidoException(String.format("Lançamento" +
                    " código %d necessita de valor total de débito e crédito " +
                    "sejam iguais", lancamentoDO.getCodigo()));
        }
    }

    /**
     * Atualiza saldos dos valores e das contas
     * @param lista lista de valores do lançamento
     * @param remover verdadeiro se for um update, falsoo se for inserir
     * @throws BancoDadosException erro de banco
     */
    private void atualizaSaldos(@NotEmpty List<ValorDO> lista, Date data,
                                boolean remover) throws Exception {
        for (final ValorDO valor : lista) {
            double adicionar = valor.getValor().multiply(
                    this.modificador(valor)).doubleValue();
            this.valorDao.atualizaSaldo(valor, data, adicionar, remover);

            final ContaDO contaDO = this.contaDao.procurar(valor.getConta().getCodigo(), false);
            if (contaDO.getSaldo() == null) {
                contaDO.setSaldo(BigDecimal.valueOf(adicionar));
            } else {
                contaDO.setSaldo(contaDO.getSaldo().add(BigDecimal.valueOf(
                        adicionar)));
            }

            this.contaDao.atualizar(contaDO);
        }
    }

    /**
     * Determina se o salkdo da conta vai ser incrementado ou decrementado
     * em função do valor novo
     * @param valorDO valor
     * @return 1 ou -1
     */
    private BigDecimal modificador(ValorDO valorDO) throws Exception {
        ContaDO contaDO = valorDO.getConta();
        if (contaDO != null && isBlank(contaDO.getNumero())) {
            contaDO = this.contaDao.procurar(contaDO.getCodigo(), false);
        }

        final String numero = contaDO.getNumero();
        if (numero.startsWith("01") || numero.startsWith("03")) {
            return new BigDecimal(valorDO.getTipo() == TipoValor.CREDITO ?
                    -1 : 1);
        } else {
            return new BigDecimal(valorDO.getTipo() == TipoValor.DEBITO ?
                    -1 : 1);
        }
    }

    /**
     * Busca lista de lançamentos baseado em filtro
     * @param filtros filtro
     * @return Lista de lancamentos
     * @throws ContabilException Erros
     */
    @Valid @Transactional
    public List<Lancamento> listar(@Valid @NotNull FiltroLancamentos filtros)
            throws ContabilException {

        try {
            logger.info("listar :: Procurando lista de lançamentos com {}" +
                    " ...", filtros);
            final List<LancamentoDO> lancamentos = this.dao.listar(filtros);

            logger.info("listar :: Lista de lancamentos encontrada com {}",
                    filtros);

            return lancamentos.stream().map(Lancamento::new)
                    .collect(Collectors.toList());
        } catch (BancoDadosException e) {
            final String erro = String.format("Ocorreu um erro no banco ao" +
                    " buscar listade lançamentos com %s", filtros);
            logger.error("listar :: {} Erro: {}", erro, e.getMessage());
            throw e;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro ao buscar listade " +
                    "lançamentos com %s", filtros);
            logger.error("listar :: {} Erro: {}", erro, e.getMessage(), e);
            throw new ContabilException(erro, e);
        }
    }

    /**
     * Procura lançamento por código
     * @param codigo codigo do lancamento
     * @return lancamento
     * @throws ContabilException erros
     */
    @NotNull @Valid @Transactional
    public Lancamento procurar(@NotNull Long codigo) throws ContabilException {

        try {
            logger.info("procurar :: Procurando lançamento com código: {} " +
                    "...", codigo);
            final LancamentoDO lancamentoDO = this.dao.buscar(codigo);
            logger.info("procurar :: {} encontrado com sucesso!", lancamentoDO);

            return new Lancamento(lancamentoDO);
        } catch (BancoDadosException e) {
            final String erro = String.format("Ocorreu um erro no banco ao" +
                    " buscar lançamento com código %d", codigo);
            logger.error("procurar :: {} Erro: {}", erro, e.getMessage());
            throw e;
        } catch (Exception e) {
            final String erro = String.format("Ocorreu um erro ao buscar " +
                    "lançamento com código %d", codigo);
            logger.error("procurar :: {} Erro: {}", erro, e.getMessage(), e);
            throw new ContabilException(erro, e);
        }
    }
}
