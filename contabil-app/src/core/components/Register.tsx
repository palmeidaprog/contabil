import React from 'react';
import TextField from "@material-ui/core/TextField";
import Card from "@material-ui/core/Card";
import Autocomplete from '@material-ui/lab/Autocomplete';
import CardContent from "@material-ui/core/CardContent";
import CardActions from "@material-ui/core/CardActions";
import Button from "@material-ui/core/Button";
import CardHeader from "@material-ui/core/CardHeader";
import RedirectService from '../common/Redirect';
import { Grid } from '@material-ui/core';
import '../../assets/css/components/Login.scss';


class InternalState {
    username: string;
    password: string;
    accountNumber: string;
    accountName: string;
    rootAccount: string;
    isButtonDisabled: boolean;
    helperText: string;
    error: boolean;
    constructor() {
        this.accountNumber = " ";
        this.accountName = "";
        this.rootAccount = "";
        this.username = "";
        this.password = "";
        this.isButtonDisabled = false;
        this.helperText = "";
        this.error = false;

    }
}
export default class RegisterPage extends React.Component {
    public state: InternalState = new InternalState();
    public redirectService: RedirectService = new RedirectService();
    private handleRegister() {
        
        if (this.state.username !== "" && this.state.password !== "" &&
            this.state.accountName !== "" && this.state.accountNumber !== "" &&
            this.state.rootAccount !== "") {

            this.state.error = false;
            this.state.helperText = "Register Successfully";
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
        this.state.username = e;
        this.forceUpdate();
    
    }
    private handleEnterPassword(e: any) {
        this.state.password = e;
        this.forceUpdate();

    }
    private handleEnterAccountNumber(e: any) {
        this.state.accountNumber = e;
        this.forceUpdate();
    }
    private handleEnterAccountName(e: any) {
        this.state.accountName = e;
        this.forceUpdate();
    }
    private handleEnterRootAccount(e: any) {
        this.state.rootAccount = e;
        this.forceUpdate();
    }
    private get isSubmitDisabled() {
        return this.state.username == "" || this.state.password == "";
    }
    render() {
        return (
            <div className="login-container">
                <Grid container style={{ height: "100%" }}>
                    <Grid item xs={12} sm={12} md={12} lg={6} xl={6}>
                        <div className="login-banner"></div>
                    </Grid>
                    <Grid item xs={12} sm={12} md={12} lg={6} xl={6} className="d-flex">
                        <form className="form-container" noValidate autoComplete="off">
                            <Card className="card">
                                <div className="header">
                                    <span className="title">Register</span>
                                    <i className="lock"></i>
                                </div>
                                <CardContent>
                                    <div className="row">
                                        <div className="col">
                                            <TextField
                                                variant="outlined"
                                                error={this.state.error}
                                                fullWidth
                                                id="accountName"
                                                type="string"
                                                label="Nome da conta"
                                                placeholder="Nome da conta"
                                                margin="normal"
                                                onChange={e => this.handleEnterUsername(e.target.value)}
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
                                                    id="accountNumber"
                                                    type="number"
                                                    label="Numero da conta"
                                                    placeholder="Numero da conta"
                                                    margin="normal"
                                                    helperText={this.state.helperText}
                                                    onChange={e => this.handleEnterPassword(e.target.value)}
                                                    onKeyPress={e => this.handleKeyPress(e)}
                                                />
                                        </div>
                                        <div className="col-md-6">
                                            <Autocomplete
                                                options={["1.Ativo", "1.1.Imediato", "1.1.1.Caixa"]}
                                                getOptionLabel={option => option}
                                                className="w-100"
                                                noOptionsText={"Loading..."}
                                                multiple={false}
                                                onChange={(e, v) => this.handleEnterPassword(v)}
                                                onKeyPress={e => this.handleKeyPress(e)}
                                                renderInput={() =>(
                                                    <TextField 
                                                    hiddenLabel 
                                                    fullWidth
                                                    variant="outlined"
                                                    error={this.state.error}
                                                    id="rootAccount"
                                                    margin="normal"
                                                    helperText={this.state.helperText}
                                                    placeholder="Conta pai"
                                                    />
                                                )}
                                            />
                                            {/* <TextField
                                                    variant="outlined"
                                                    error={this.state.error}
                                                    fullWidth
                                                    id="rootAccount"
                                                    type="number"
                                                    label="Conta pai"
                                                    placeholder="Conta pai"
                                                    margin="normal"
                                                    helperText={this.state.helperText}
                                                    onChange={e => this.handleEnterPassword(e.target.value)}
                                                    onKeyPress={e => this.handleKeyPress(e)}
                                                /> */}
                                        </div>
                                    </div>
                                    <div className="row">
                                        <div className="col">
                                            <TextField
                                                variant="outlined"
                                                error={this.state.error}
                                                fullWidth
                                                id="username"
                                                type="email"
                                                label="Username"
                                                placeholder="Username"
                                                margin="normal"
                                                onChange={e => this.handleEnterUsername(e.target.value)}
                                                onKeyPress={e => this.handleKeyPress(e)}
                                            />
                                        </div>
                                    </div>
                                    <div className="row">
                                        <div className="col">
                                            <TextField
                                                variant="outlined"
                                                error={this.state.error}
                                                fullWidth
                                                id="password"
                                                type="password"
                                                label="Password"
                                                placeholder="Password"
                                                margin="normal"
                                                helperText={this.state.helperText}
                                                onChange={e => this.handleEnterPassword(e.target.value)}
                                                onKeyPress={e => this.handleKeyPress(e)}
                                            />
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
                                    >Register</Button>
                                </CardActions>
                            </Card>
                        </form>
                    </Grid>
                </Grid>

            </div>
        );
    }
}
