import React from 'react';
import '../assets/css/components/GenericTable.scss';
import { Action } from '../core/types/Functions';
interface IBalanceTable{
    labels : any[];
    datas : any[];
    onSelect? : Action<number>;
}
export default class GenericTable extends React.Component<IBalanceTable>{

    public getItem(item : any){
        delete item.destacado;
        return Object.values(item);
    }
    public handleSelect(index : number){
        if(this.props.onSelect)
            this.props.onSelect(index);
    }
    public render(){
        return (
            <table className="balance-table">
                <thead className="table-head">
                    <tr>
                        {
                            this.props.labels.map((item, index)=>(
                                <td key={index}>{item}</td>
                            ))
                        }
                    </tr>
                </thead>
                <tbody className="table-body">
                    {
                        this.props.datas.map((item, index)=>(
                            <tr key={index} onClick={()=>{this.handleSelect(index)}} className={item.destacado ? "highlighted" : ""}>
                                {
                                    this.getItem(item).map((value, index)=>(
                                        <td key={index}>{value as any}</td>
                                    ))
                                }
                            </tr>
                        ))
                    }
                </tbody>
            </table>
        
        );
    }
}