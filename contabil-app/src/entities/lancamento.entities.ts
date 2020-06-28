import {ValorEntities} from "./valor.entities";
import { TipoValor } from "./tipo-valor.enum";

export class LancamentoEntities {
    codigo?: number;
    codigoUsuario: string;
    data: Date;
    historico: string;
    valores: Array<ValorEntities>;
}

export class LancamentoMapper{
    noConta : number;
    nomeConta : string;
    tipoValor : TipoValor;
    valor : number;
}