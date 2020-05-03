import React, { Component } from 'react';
import TextField from "@material-ui/core/TextField";
import Card from "@material-ui/core/Card";
import CardContent from "@material-ui/core/CardContent";
import CardActions from "@material-ui/core/CardActions";
import Button from "@material-ui/core/Button";
import RedirectService from '../common/Redirect';
import { Grid } from '@material-ui/core';
import '../../assets/css/components/Login.scss';
import autobind from 'autobind-decorator';
import { Auth0Authentication } from './auth/Auth0Authentication';


export interface HomeProps {
    auth: Auth0Authentication;
  }

class InternalState {
    username: string;
    password: string;
    isButtonDisabled: boolean;
    helperText: string;
    error: boolean;
    constructor() {
        this.username = "";
        this.password = "";
        this.isButtonDisabled = false;
        this.helperText = "";
        this.error = false;

    }
}
export default class LoginPage extends Component<HomeProps, {}> {
    public state: InternalState = new InternalState();
    public redirectService: RedirectService = new RedirectService();


    @autobind
    login() {
        this.props.auth.login();
    }

    /**
    private handleLogin() {
        
        if (this.state.username === "abc@email.com" && this.state.password === "password") {
            this.state.error = false;
            this.state.helperText = "Login Successfully";
            this.redirectService.redirect('/home');
        } else {
            this.state.error = true;
            this.state.helperText = "Incorrect username or password";
        }
        this.forceUpdate();

    }
    */
    private handleKeyPress(e: any) {
        if (e.keyCode === 13 || e.which === 13) {
            this.state.isButtonDisabled && this.login();
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
                                    <span className="title">Sign in</span>
                                    <i className="lock"></i>
                                </div>
                                <CardContent>
                                    <div>
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
                                </CardContent>
                                <CardActions>
                                    <Button
                                        variant="contained"
                                        size="large"
                                        color="secondary"
                                        className="loginBtn"
                                        onClick={this.login}
                                        disabled={this.isSubmitDisabled}
                                    >Login</Button>
                                </CardActions>
                            </Card>
                        </form>
                    </Grid>
                </Grid>

            </div>
        );
    }
}
