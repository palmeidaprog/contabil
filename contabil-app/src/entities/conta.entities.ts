import {SaldoEntities} from "./saldo.entities";
import {ValorEntities} from "./valor.entities";

export class ContaEntities {
    codigo?: number;
    contaPaiCodigo?: number;
    condigoUsuario: number;
    numero?: string;
    nome: string
    saldo?: SaldoEntities;
    descricao?: string;
    valores?: Array<ValorEntities>
}

export interface ContaMapper {
    noConta: string;
    nomeConta: string;
}