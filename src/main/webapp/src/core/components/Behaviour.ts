import React from "react";
import ObjectUtils from "../common/ObjectUtils";
import { Action } from "../types/Functions";

abstract class Behaviour<State = {}, Props = {}> extends React.Component<State,Props>{
    protected repaint(options? : {deepCloneState : boolean}) : void{
        this.setState(ObjectUtils.clone(this.state,options ? options.deepCloneState : false) as any);
    }
    //move this to an util class later
    public isNullOrEmpty(value : any){
        return value == null || value == "" || value == undefined || value?.length == 0;
    }
    protected setLinkedState<T = any>(fn : Action<T>){
        let props : any = this.props;
        if(props.linkedState)
            props.linkedState(fn);
    }
}
export default Behaviour;