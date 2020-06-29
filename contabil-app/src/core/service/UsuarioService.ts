import {GenericRestService} from "./GenericRestService";
import {UsuarioEntities} from "../../entities/usuario.entities";

export class UsuarioService extends GenericRestService {

    constructor() {
        super("usuario");
    }


    /**
     * Adicionar novo usuários
     * @param usuario usuário a ser adicionado
     */
    async adicionar(usuario: UsuarioEntities): Promise<any> {
        this.postMethod("/adicionar", usuario);
    }

    async get(params: any): Promise<any> {
        let first = true;
        let path = '/get';

        if (params && params.codigo) {
            path += `${first ? '?' : '&'}codigo=${params.codigo}`;
            first = false;
        }

        if (params && params.login) {
            path += `${first ? '?' : '&'}login=${params.login}`;
        }

        return this.getMethod(path);
    }

}