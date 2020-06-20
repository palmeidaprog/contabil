import React from 'react';
import { Action, Void } from '../core/types/Functions';
import { Card, CardContent, TextField, CardActions, Button } from '@material-ui/core';
import Autocomplete from '@material-ui/lab/Autocomplete';

export function RegisterForm(props: { state: any, handleRegister: Void, isSubmitDisabled: boolean, handleEnterUsername: Action<any>, handleEnterPassword: Action<any>, handleEnterAccountNumber: Action<any>, handleEnterAccountName: Action<any>, handleKeyPress: Action<any>, handleEnterRootAccount: Action<any> }) {
    return (
        <form className="form-container" noValidate autoComplete="off">
            <Card className="card">
                <div className="header">
                    <span className="title">Registro</span>
                    <i className="lock"></i>
                </div>
                <CardContent>
                    <div className="row">
                        <div className="col">
                            <TextField
                                variant="outlined"
                                error={props.state.error}
                                fullWidth
                                id="id-nome-conta"
                                type="string"
                                label="Nome da Conta"
                                placeholder="Nome da Conta"
                                margin="normal"
                                onChange={e => props.handleEnterAccountName(e.target.value)}
                                onKeyPress={e => props.handleKeyPress(e)}
                            />
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-md-6">
                            <TextField
                                variant="outlined"
                                error={props.state.error}
                                fullWidth
                                id="id-numero-conta"
                                type="text"
                                label="Número da Conta"
                                placeholder="Número da Conta"
                                margin="normal"
                                helperText={props.state.helperText}
                                onChange={e => props.handleEnterAccountNumber(e.target.value)}
                                onKeyPress={e => props.handleKeyPress(e)}
                            />
                        </div>
                        <div className="col-md-6">
                            <Autocomplete
                                options={["1.Ativo", "1.1.Imediato", "1.1.1.Caixa"]}
                                getOptionLabel={option => option}
                                className="w-100"
                                noOptionsText={"Carregando..."}
                                multiple={false}
                                onChange={(e, v) => props.handleEnterRootAccount(v)}
                                onKeyPress={e => props.handleKeyPress(e)}
                                renderInput={params => (
                                    <TextField
                                        {...params}
                                        fullWidth
                                        variant="outlined"
                                        error={props.state.error}
                                        id="id-conta-pai"
                                        margin="normal"
                                        helperText={props.state.helperText}
                                        placeholder="Conta Pai"
                                    />
                                )}
                            />
                        </div>
                    </div>
                    <div className="row">
                        <div className="col">
                            <TextField
                                variant="outlined"
                                error={props.state.error}
                                fullWidth
                                id="username"
                                type="email"
                                label="Usuário"
                                placeholder="Usuário"
                                margin="normal"
                                onChange={e => props.handleEnterUsername(e.target.value)}
                                onKeyPress={e => props.handleKeyPress(e)}
                            />
                        </div>
                    </div>
                    <div className="row">
                        <div className="col">
                            <TextField
                                variant="outlined"
                                error={props.state.error}
                                fullWidth
                                id="password"
                                type="password"
                                label="Senha"
                                placeholder="Senha"
                                margin="normal"
                                helperText={props.state.helperText}
                                onChange={e => props.handleEnterPassword(e.target.value)}
                                onKeyPress={e => props.handleKeyPress(e)}
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
                        onClick={() => props.handleRegister()}
                        disabled={props.isSubmitDisabled}
                    >Cadastrar</Button>
                </CardActions>
            </Card>
        </form>
    );

}