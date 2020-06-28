import {GenericRestService} from "./GenericRestService";
import {ContaEntities} from "../../entities/conta.entities";

export class ContaService extends GenericRestService {

    constructor() {
        super("conta");
    }

    /**
     * Procura lista da contas
     * @param codigoUsuario
     * @param params
     */
    async listar(codigoUsuario: number, params: any): Promise<any> {
        let first = true;
        let path = `/listar/${codigoUsuario}`;

        if (params && params['numero']) {
            path += `${first ? '?' : '&'}numero=${params.numero}`;
            first = false;
        }

        if (params && params['nome']) {
            path += `${first ? '?' : '&'}nome=${params.nome}`
        }

        return this.getMethod(path);
    }

    /**
     * Atualiza informações conta
     * @param conta
     */
    async atualizar(conta: ContaEntities): Promise<any> {
        return this.postMethod("/atualizar", conta);
    }

    /**
     * Adicionar conta
     * @param conta
     */
    async adicionar(conta: ContaEntities): Promise<any> {
        return this.postMethod("/adicionar", conta);
    }

    /**
     * Remove conta
     * @param conta
     */
    async remover(conta: ContaEntities): Promise<any> {
        return this.postMethod("/remover", conta);
    }


    /**
     * Recebe balancete
     * @param codigoUsuario
     */
    async balancete(codigoUsuario: number): Promise<any> {
        return this.getMethod( `/balancete/${codigoUsuario}`);
    }

    /**
     * Query da conta completa
     * @param codigoConta
     */
    async get(codigoConta: number): Promise<any> {
        return this.getMethod( `/get/${codigoConta}`);
    }

}