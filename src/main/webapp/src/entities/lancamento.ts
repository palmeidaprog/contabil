import {Valor} from "./valor";

export class Lancamento {
    codigo: number;
    codigoUsuario?: string;
    data?: string;
    historico?: string;
    valores: Array<Valor>;
}