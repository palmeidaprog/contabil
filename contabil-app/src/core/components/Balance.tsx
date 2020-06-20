import React from 'react';
import '../../assets/css/components/Balance.scss';
import BalanceTable from '../../components/BalanceTable';


class InternalBalanceData{
    labels : any[];
    datas : any[];
    constructor(){
        this.labels = [
            "Conta Contábil", "Descrição", "Débito", "Crédito", "Saldo"
        ]
        this.datas = [
            {
                "contaContabil" : 1,
                "descricao" : "Ativo",
                "debito" : "0,00",
                "credito" : "0,00",
                "saldo" : "-9.235,75",
                "destacado" : true
            },
            {
                "contaContabil" : 1.2,
                "descricao" : "Não Circulantes",
                "debito" : "0,00",
                "credito" : "0,00",
                "saldo" : "-9.235,75",
                "destacado" : false
            },
            {
                "contaContabil" : 1.2,
                "descricao" : "Não Circulantes",
                "debito" : "0,00",
                "credito" : "0,00",
                "saldo" : "-9.235,75",
                "destacado" : false
            },
            {
                "contaContabil" : 1.2,
                "descricao" : "Não Circulantes",
                "debito" : "0,00",
                "credito" : "0,00",
                "saldo" : "-9.235,75",
                "destacado" : false
            },
            {
                "contaContabil" : 2,
                "descricao" : "Passivo",
                "debito" : "179,75",
                "credito" : "0,00",
                "saldo" : "179,75",
                "destacado" : true
            },
            {
                "contaContabil" : 2.1,
                "descricao" : "Circulante",
                "debito" : "179,75",
                "credito" : "0,00",
                "saldo" : "179,75",
                "destacado" : false
            },
            {
                "contaContabil" : 2.1,
                "descricao" : "Circulante",
                "debito" : "179,75",
                "credito" : "0,00",
                "saldo" : "179,75",
                "destacado" : false
            },
            {
                "contaContabil" : 2.1,
                "descricao" : "Circulante",
                "debito" : "179,75",
                "credito" : "0,00",
                "saldo" : "179,75",
                "destacado" : false
            }
        ]
    }
}

export default class BalancePage extends React.Component{
    public state : InternalBalanceData = new InternalBalanceData();


    public render(){
        return(
            <div className="balance-container">
                <div className="balance-title">
                    Balancete
                </div>
                <div className="balance-content">
                    <BalanceTable labels={this.state.labels} datas={this.state.datas}/>
                </div>
            </div>
        );
    }
}