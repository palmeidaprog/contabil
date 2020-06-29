import {GenericRestService} from "./GenericRestService";
import {LancamentoEntities} from "../../entities/lancamento.entities";


export class LancamentoService extends GenericRestService {

    constructor() {
        super('lancamento');
    }

    /**
     * Atualiza informações lancamento
     * @param lancamento
     */
    async atualizar(lancamento: LancamentoEntities): Promise<any> {
        return this.postMethod("/atualizar", lancamento);
    }

    /**
     * Adicionar lancamento
     * @param lancamento
     */
    async adicionar(lancamento: LancamentoEntities): Promise<any> {
        return this.postMethod("/adicionar", lancamento);
    }

    /**
     * Remove lancamento
     * @param lancamento
     */
    async remover(lancamento: LancamentoEntities): Promise<any> {
        return this.postMethod("/remover", lancamento);
    }

    /**
     * Busca lancamento
     * @param codigo codigo do lançamento
     */
    async get(codigo: number): Promise<any> {
        return this.getMethod( `/get/${codigo}`);
    }

    /**
     * Lista de lançamento num intevalo de tempo
     * @param codigoUsuario codigo usuário
     * @param dataInicio data inicio
     * @param dataFinal data final
     */
    async listar(codigoUsuario: number, dataInicio: string, dataFinal: string): Promise<any> {
        return this.getMethod(`/listar/${codigoUsuario}/${dataInicio}/${dataFinal}}`);
    }


}