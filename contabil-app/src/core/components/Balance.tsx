import React from 'react';
import '../../assets/css/components/Balance.scss';
import GenericTable from '../../components/GenericTable';
import BalanceService from '../service/BalanceService';
import {ContaService} from "../service/ContaService";
import {Keys} from "../../entities/keys.enum";
import {UsuarioEntities} from "../../entities/usuario.entities";
import Snackbar from "@material-ui/core/Snackbar";
import If from "../common/If";
import MuiAlert from "@material-ui/lab/Alert";
import {ContaEntities} from "../../entities/conta.entities";
import {Card, CardContent, CircularProgress} from "@material-ui/core";

function Alert(props) {
    return <MuiAlert elevation={6} variant="filled" {...props} />;
}

class InternalBalanceData{
    labels : any[];
    datas: Array<any> = [];
    contaService: ContaService;
    private _userCode: number;
    toastMsg: string;
    toastShow: boolean = false;
    toastType: string;
    loading = false;

    constructor(){
        this.contaService = new ContaService();
        this.labels = [
            "Conta", "Nome", "Débito", "Crédito", "Saldo"
        ];
    }

    async userCode(): Promise<number> {
        if (!this._userCode) {
            await this.updateUser();
            if (!this._userCode) this._userCode = 46;
        }
        return this._userCode;
    }

    async updateUser(): Promise<void> {
        if (!this._userCode) {
            const userObj = localStorage.getItem(Keys.USUARIO);
            if (userObj) {
                const user = JSON.parse(userObj);
                if (user && (user as UsuarioEntities).codigo) {
                    this._userCode = user.codigo;
                }
            }
        }
    }

    async loadBalanceReport(): Promise<void> {
        const codigoUsuario = await this.userCode();
        await this.contaService.balancete(codigoUsuario).then(response => {
            const balanceData: Array<ContaEntities> = response;
            if (balanceData) {
                this.datas = balanceData?.map(conta => {
                    return {
                        conta: conta.numero,
                        nome: conta.nome,
                        debito: conta.saldo ? conta.saldo.totalDebito.toFixed(2) : '0.00',
                        credito: conta.saldo ? conta.saldo.totalCredito.toFixed(2) : '0.00',
                        saldo: conta.saldo ? `${conta.saldo.tipoSaldo[0]} - ${conta.saldo.saldo.toFixed(2)}` : '0.00',
                        destacado: conta.nivel === 1
                    };
                });
            }
        }, error => {
            this.toastType = 'error';
            this.toastMsg = error?.message;
            this.toastShow = true;
        });
    }


}

export default class BalancePage extends React.Component{
    public state : InternalBalanceData = new InternalBalanceData();


    onToastClick(): void {
        this.state.toastShow = false;
        this.forceUpdate();
    }

    async componentDidMount(): Promise<void> {
        this.state.loading = true;
        this.forceUpdate();
        await this.state.loadBalanceReport();
        this.state.loading = false;
        this.forceUpdate();
    }


    public render(){
        return(
            <div className={this.state.loading ? "center-horz" : "balance-container"}>
                <If test={!this.state.loading}>
                    <div className="header-div">
                        <Card className="card-generic title">
                            <CardContent>
                                Balancete
                            </CardContent>
                        </Card>
                    </div>
                    <Card className="card-generic">
                        <CardContent>
                            <GenericTable labels={this.state.labels} datas={this.state.datas}/>
                        </CardContent>
                    </Card>
                </If>
                <If test={this.state.loading}>

                    <div className="padding">Carregando ...</div>
                    <div className="d-flex w-100 justify-content-center">

                        <CircularProgress color={"primary"} size={50} thickness={5}/>
                    </div>

                </If>
                <If test={!this.state.loading && this.state.toastShow}>
                    <Snackbar open={this.state.toastShow} autoHideDuration={6000} onClick={e => this.onToastClick ()}>
                        <Alert severity={this.state.toastType} onClick={e => this.onToastClick()}>
                            {this.state.toastMsg}
                        </Alert>
                    </Snackbar>
                </If>
            </div>

        );
    }
}