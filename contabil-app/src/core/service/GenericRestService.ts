export class GenericRestService {
    protected restPath: string;

    constructor(restPath: string) {
        const apiPath = 'http://138.197.71.50:8080/contabil';
        this.restPath = apiPath + (restPath.startsWith('/') ? '' : '/') + restPath;
    }

    /**
     * Monta objeto de json para o fetch
     * @param method nome do method GET ou POST
     * @param object objeto que vai no body do post
     */
    private async getOption(method: string, object?: any): Promise<any> {
        let options = {
            method: method,
            header: {
                'Content-Type': 'application/json'
            }
        }

        if (method == 'POST') {
            options['body'] = JSON.stringify(object ? object : {});
        }

        return options;
    }


    async postMethod(postPath: string, object: any) {
        const path = this.restPath + (postPath.startsWith('/') ? '' : '/') + postPath;
        const options = await this.getOption('POST', object);
        const response = await fetch(path, options);

        return await response.json();
    }

    async getMethod(postPath: string): Promise<any> {
        const path = this.restPath + (postPath.startsWith('/') ? '' : '/') + postPath;
        const options = await this.getOption('GET');
        const response = await fetch(path, options);

        return await response.json();
    }
}