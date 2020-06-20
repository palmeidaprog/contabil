import React from 'react';

interface IBalanceTable{
    labels : any[];
    datas : any[];
}
export default class BalanceTable extends React.Component<IBalanceTable>{

    public getItem(item : any){
        delete item.destacado;
        return Object.values(item);
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
                            <tr key={index} className={item.destacado ? "highlighted" : ""}>
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