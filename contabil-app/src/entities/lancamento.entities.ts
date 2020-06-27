import {ValorEntities} from "./valor.entities";

export class LancamentoEntities {
    codigo?: number;
    codigoUsuario: string;
    data: Date;
    historico: string;
    valores: Array<ValorEntities>;
}