import {SaldoEntities} from "./saldo.entities";
import {ValorEntities} from "./valor.entities";

export class ContaEntities {
    codigo?: number;
    contaPaiCodigo?: number;
    codigoUsuario: number;
    numero?: string;
    nome: string
    saldo?: SaldoEntities;
    descricao?: string;
    valores?: Array<ValorEntities>;
    nivel?: number;
}

export interface ContaMapper {
    noConta: string;
    nomeConta: string;
}