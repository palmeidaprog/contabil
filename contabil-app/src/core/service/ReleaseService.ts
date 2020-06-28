import { LancamentoEntities } from "../../entities/lancamento.entities";
import { ValorEntities } from "../../entities/valor.entities";
import { TipoValor } from "../../entities/tipo-valor.enum";

export default class ReleaseService{

    //todo

    public getReleases() : LancamentoEntities{
        //fetch release
        let release = new LancamentoEntities();
        let entityValue = new ValorEntities();
        entityValue.codigo = 1;
        entityValue.codigoConta = 11;
        entityValue.codigoLancamento = 1;
        entityValue.data = new Date();
        entityValue.historico = "histórico 1";
        entityValue.saldoConta = 1000;
        entityValue.tipo = TipoValor.CREDITO;
        entityValue.valor = 1000;
        release.codigo = 1;
        release.codigoUsuario = "11";
        release.data = new Date();
        release.historico = "teste";
        release.valores = new Array(15).fill(entityValue);
        
        return release;
    }
}