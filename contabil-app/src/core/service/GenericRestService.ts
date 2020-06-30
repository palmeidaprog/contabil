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
    private getOption(method: string, object?: any) {
        let options = {
            method: method,
            header: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }

        if (method == 'POST') {
            options['body'] = JSON.stringify(object ? object : {});
            //options['body'] = object ? object : {};
        }

        return options;
    }


    async postMethod(postPath: string, object: any) {
        const path = this.restPath + (postPath.startsWith('/') ? '' : '/') + postPath;
        const options = this.getOption('POST', object);
        try {
            const response = await fetch(path, options);
            return await response.json();
        } catch (e) {
            throw e;
        }
    }

    async getMethod(postPath: string): Promise<any> {
        const path = this.restPath + (postPath.startsWith('/') ? '' : '/') + postPath;
        const options = this.getOption('GET');
        const response = await fetch(path, options);

        return await response.json();
    }
}