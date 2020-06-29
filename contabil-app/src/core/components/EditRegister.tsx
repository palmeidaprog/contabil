import React from 'react';
import { Card, TextField, CardContent, CardActions, Button } from '@material-ui/core';
import Autocomplete from '@material-ui/lab/Autocomplete';
import '../../assets/css/components/Login.scss';
import RegisterService from '../service/RegisterService';
import GenericTable from '../../components/GenericTable';

interface IUser{
    nomeUsuario : string,
    numeroConta : string,
    contaPai : "1.Ativo"|"1.1.Imediato"|"1.1.1.Caixa"|null ,
}
export default class EditRegisterPage extends React.Component {
    public state: { error: boolean, helperText: string, editUser :  IUser} = { error: false, helperText: "", editUser : {nomeUsuario : "", numeroConta : "", contaPai : null} }
    private registerService : RegisterService = new RegisterService();
    public handleKeyPress(e: any) {
        if (e.keyCode === 13 || e.which === 13) {
            console.log(this.state.editUser);
            this.isSubmitDisabled && this.handleRegister();
        }
    }
    public handleEnterAccountName(value: any) {
        this.state.editUser.nomeUsuario = value;
        this.forceUpdate();
    }
    public handleEnterRootAccount(value: any) {
        this.state.editUser.contaPai = value;
        this.forceUpdate();
    }
    public handleEnterAccountNumber(value: any) {
        this.state.editUser.numeroConta = value;
        this.forceUpdate();
    }
    public handleRegister(){
        console.log(this.state.editUser);
    }
    private get isSubmitDisabled() : boolean{
        return this.state.editUser.nomeUsuario == "" || this.state.editUser.numeroConta == "";
    }
    public render() {
        return (
            <div className="edit-register-container">
                <div className="edit-form">
                    <form className="form-container" noValidate autoComplete="off">
                        <Card className="card">
                            <div className="header">
                                <span className="title">Editar Conta</span>
                                <i className="lock"></i>
                            </div>
                        </Card>
                        <CardContent>
                            <div className="row">
                                <div className="col">
                                    <TextField
                                        variant="outlined"
                                        error={this.state.error}
                                        fullWidth
                                        id="id-nome-conta"
                                        type="string"
                                        label="Nome da Conta"
                                        placeholder="Nome da Conta"
                                        margin="normal"
                                        onChange={e => this.handleEnterAccountName(e.target.value)}
                                        onKeyPress={e => this.handleKeyPress(e)}
                                    />
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-md-6">
                                    <TextField
                                        variant="outlined"
                                        error={this.state.error}
                                        fullWidth
                                        id="id-numero-conta"
                                        type="text"
                                        label="Número da Conta"
                                        placeholder="Número da Conta"
                                        margin="normal"
                                        helperText={this.state.helperText}
                                        onChange={e => this.handleEnterAccountNumber(e.target.value)}
                                        onKeyPress={e => this.handleKeyPress(e)}
                                    />
                                </div>
                                <div className="col-md-6">
                                    <Autocomplete
                                        options={["1.Ativo", "1.1.Imediato", "1.1.1.Caixa"]}
                                        getOptionLabel={option => option}
                                        className="w-100"
                                        noOptionsText={"Carregando..."}
                                        multiple={false}
                                        onChange={(e, v) => this.handleEnterRootAccount(v)}
                                        onKeyPress={e => this.handleKeyPress(e)}
                                        renderInput={params => (
                                            <TextField
                                                {...params}
                                                fullWidth
                                                variant="outlined"
                                                error={this.state.error}
                                                id="id-conta-pai"
                                                margin="normal"
                                                helperText={this.state.helperText}
                                                placeholder="Conta Pai"
                                            />
                                        )}
                                    />
                                </div>
                            </div>
                        </CardContent>
                        <CardContent>
                            <div className="edit-register-releases">
                                <div className="releases-title">
                                    Valores Lançamentos
                                </div>  
                                <div className="releases-section">
                                    <GenericTable labels={[]} datas={[]}/>
                                </div>
                            </div>
                        </CardContent>
                        <CardActions>
                            <Button
                                variant="contained"
                                size="large"
                                color="secondary"
                                className="loginBtn"
                                onClick={() => this.handleRegister()}
                                disabled={this.isSubmitDisabled}
                            >Atualizar</Button>
                        </CardActions>
                    </form>
                </div>
            </div>
        );
    }
}