import React from 'react';
let data = {};
/**
 * Simple class that handle a Global State
 * @author Ruben Gomes
 * @author Bruno Henrique
 */
export default class SystemState {
    /**
     * Retrieve all Stored Keys
     * @return @type {Array<string>}
     */
    static get keys() {
        return Object.keys(data);
    }
    /**
     * Set a global state for specific key
     * Its work like @type {AsyncStorage} or HTML5 localStorage
     * @param {string} key 
     * @param {any} state 
     */
    static set(key, state) {
        data[key] = state;
    }
    /**
     * Retrieve a stored state by a specific key
     * @param {string} key 
     * @return {any}
     */
    static get(key) {
        return data[key];
    }
    /**
     * A React Hook for handle a Global State by a specific,
     * it works like methods 'get' and 'set' but in hook like.
     * This method will return a Tuple of @type {[any, (any)=> void]} likely
     * React.useState()
     * @param {string} key 
     * @param {any} defaultValue 
     * @returns @type {[any, (any)=> void]}
     */
    static useState(key, defaultValue) {
        if (!data[key])
            data[key] = defaultValue;
        const [state, setState] = React.useState(data[key]);
        return [state, (x) => {
            data[key] = x;
            setState(x);
        }];
    }
    /**
     * Remove a item from Global State
     */
    static remove(key) {
        if (data && data[key] != undefined)
            delete data[key];
    }
    /**
     * Clear all entries stored at Global State
     */
    static clear() {
        data = {};
    }
    /**
     * Dump entire global state in a console.log for Debug purposes
     */
    static dump() {
        console.trace();
        console.log(data);
    }

    static registerState(key : any){
        //it needs to be cryptographed
        sessionStorage.setItem(key, JSON.stringify(data[key]));
    }
    static unregisterState(key : any){
        sessionStorage.removeItem(key);
    }
    static unregisterAllStates(){
        sessionStorage.clear();
    }
    static getRegisteredState(key : any){
        return JSON.parse(sessionStorage.getItem(key) ?? "");
    }
}