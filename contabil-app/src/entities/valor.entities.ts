import {TipoValor} from "./tipo-valor.enum";

export class ValorEntities {
    codigo?: number;
    tipo: TipoValor;
    codigoConta: number;
    data?: Date;
    historico?: string;
    saldoConta?: number;
    valor: number;
    codigoLancamento: number;
}