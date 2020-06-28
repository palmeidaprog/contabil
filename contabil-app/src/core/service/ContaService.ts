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
    async listar(codigoUsuario: number, params: any): Promise<Array<ContaEntities>> {
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



}