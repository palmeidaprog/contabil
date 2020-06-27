const primitiveTypes = ['Number', ''];
export default class ObjectUtils{
    /**
     * Clone a Object from Reference, if need a deep clone
     * @param target Target Object Reference
     * @param deepClone If wan't a deep clone set this true.
     */
    public static clone<T>(target: Object, deepClone : boolean = false) : T{
        let deepCloneFn = (ref : any, target : any) : any => {
            let keys : string[] = Object.keys(ref);
            keys.forEach(key => {
                let type = typeof target[key];
                
                if(type == undefined){
                    ref[key] = null;
                    return;
                }
                // Check if as Date
                if(type == 'object'){
                    if(Object.prototype.toString.call(target[key]) === '[object Date]')
                        ref[key] = new Date((target[key] as Date).getTime());
                    else {
                        let isArray : boolean = Array.isArray(target[key]);
                        ref[key] = isArray ? {} : [];
                        if(isArray){
                            ref[key] = target[key].slice(0);
                        } else {
                            deepCloneFn(ref[key], target[key]);
                        }
                    }
                } else {
                    ref[key]=target[key];
                }
            });
        }
        let result : any = {};

        if(deepClone)
            result = deepCloneFn(target, {});
        else {
            let keys = Object.keys(target);
            keys.forEach(x => {
                result[x] = target[x];
            });
        }

        return result;
    }
}