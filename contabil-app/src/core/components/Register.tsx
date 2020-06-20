import React from 'react';
import TextField from "@material-ui/core/TextField";
import Card from "@material-ui/core/Card";
import Autocomplete from "@material-ui/lab/Autocomplete"
import CardContent from "@material-ui/core/CardContent";
import CardActions from "@material-ui/core/CardActions";
import Button from "@material-ui/core/Button";
import RedirectService from '../common/Redirect';
import { Grid } from '@material-ui/core';
import '../../assets/css/components/Login.scss';
import { RegisterForm } from '../../components/FormRegister';

class Registry{
    username: string;
    password: string;
    accountNumber: string;
    accountName: string;
    rootAccount: string;
    constructor(){
        this.accountNumber = " ";
        this.accountName = "";
        this.rootAccount = "";
        this.username = "";
        this.password = "";
    }
}

class InternalState {
    registryUser : Registry
    isButtonDisabled: boolean;
    helperText: string;
    error: boolean;
    constructor() {
        this.registryUser = new Registry();
        this.isButtonDisabled = false;
        this.helperText = "";
        this.error = false;

    }
}
export default class RegisterPage extends React.Component {
    public state: InternalState = new InternalState();
    public redirectService: RedirectService = new RedirectService();
    private handleRegister() {
        
        if (this.state.registryUser.username !== "" && this.state.registryUser.password !== "" &&
            this.state.registryUser.accountName !== "" && this.state.registryUser.accountNumber !== "" &&
            this.state.registryUser.rootAccount !== "") {

            this.state.error = false;
            this.state.helperText = "Register Successfully";
            //console.log(this.state.registryUser)
            this.redirectService.redirect('/login');
        } else {
            this.state.error = true;
            this.state.helperText = "Incorrect data";
        }
        this.forceUpdate();

    }
    private handleKeyPress(e: any) {
        if (e.keyCode === 13 || e.which === 13) {
            this.state.isButtonDisabled && this.handleRegister();
        }
    }
    private handleEnterUsername(e: any) {
        this.state.registryUser.username = e;
        this.forceUpdate();
    
    }
    private handleEnterPassword(e: any) {
        this.state.registryUser.password = e;
        this.forceUpdate();

    }
    private handleEnterAccountNumber(e: any) {
        this.state.registryUser.accountNumber = e;
        this.forceUpdate();
    }
    private handleEnterAccountName(e: any) {
        this.state.registryUser.accountName = e;
        this.forceUpdate();
    }
    private handleEnterRootAccount(e: any) {
        this.state.registryUser.rootAccount = e;
        this.forceUpdate();
    }
    private get isSubmitDisabled() : boolean{
        return this.state.registryUser.username == "" || this.state.registryUser.password == "";
    }
    render() {
        return (
            <div className="register-container">
                <Grid container style={{ height: "100%" }}>
                    <Grid item xs={12} sm={12} md={12} lg={6} xl={6}>
                        <div className="login-banner"></div>
                    </Grid>
                    <Grid item xs={12} sm={12} md={12} lg={6} xl={6} className="d-flex">
                        <RegisterForm 
                            state={this.state}
                            handleEnterAccountName={(val) => this.handleEnterAccountName(val)}
                            handleEnterPassword={(val) => this.handleEnterPassword(val)}
                            handleEnterAccountNumber={(val) => this.handleEnterAccountNumber(val)}
                            handleEnterRootAccount={(val) => this.handleEnterRootAccount(val)}
                            handleEnterUsername={(val) => this.handleEnterUsername(val)}
                            handleKeyPress={(val) => this.handleKeyPress(val)}
                            handleRegister={this.handleRegister}
                            isSubmitDisabled={this.isSubmitDisabled}
                        />
                    </Grid>
                </Grid>

            </div>
        );
    }
}
