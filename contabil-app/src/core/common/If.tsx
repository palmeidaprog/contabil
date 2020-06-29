import React from 'react';
export interface IfProps{
    test : any;
    children: any;
}
function If(props : IfProps){
    return props.test ? props.children : <></>;
}
export default If;