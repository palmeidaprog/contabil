import React from 'react';
import '../../assets/css/components/Release.scss';
import '../../assets/css/components/Conta.scss';
import {Button, Card, CardContent, CircularProgress, Divider, TextField} from '@material-ui/core';
import GenericTable from '../../components/GenericTable';
import ReleaseService from '../service/ReleaseService';
import {LancamentoEntities} from '../../entities/lancamento.entities';
import {ScreenState} from "../../entities/screen-state.enum";
import If from "../common/If";
import {LancamentoService} from "../service/LancamentoService";
import {Keys} from "../../entities/keys.enum";
import {UsuarioEntities} from "../../entities/usuario.entities";
import Snackbar from "@material-ui/core/Snackbar";
import MuiAlert from "@material-ui/lab/Alert";
import {UtilService} from "../service/util-service";
import Autocomplete from "@material-ui/lab/Autocomplete";
import {ContaEntities} from "../../entities/conta.entities";
import {ContaService} from "../service/ContaService";

function Alert(props) {
    return <MuiAlert elevation={6} variant="filled" {...props} />;
}

interface IOptionType {
    key: number,
    value: string
}

class InternalState {
    option: IOptionType | null;
    searchStr: string;
    hasError: boolean;
    releases: LancamentoEntities;
    tableValues: Array<any> = [];
    screenState: ScreenState;
    dataInicio: Date;
    dataFinal: Date;
    lancamentoService: LancamentoService;
    private _userCode: number;
    toastMsg: string;
    toastShow: boolean;
    toastType: string;
    loading = false;
    valores: Array<any> = [];
    selecionado: LancamentoEntities;
    adicionandoValor = false;
    contaCache: Array<ContaEntities> = [];
    optionSelected: any | null;
    contaOptions: Array<any> = [];
    tableContent: Array<any> = [];
    contaService: ContaService;
    contas: Array<ContaEntities> = [];

    constructor() {
        this.releases = new LancamentoEntities();
        this.option = null;
        this.searchStr = "";
        this.hasError = false;
        this.screenState = ScreenState.SEARCH;
        this.contaService = new ContaService();
        this.lancamentoService = new LancamentoService();
        this.dataInicio = new Date('2019-01-01');
        this.dataFinal = new Date('2029-07-01');
        this.loading = false;
        this.selecionado = new LancamentoEntities();
        this.optionSelected = { label: '-', numero: 0};
        this.loadContaCache();

    }

    async userCode(): Promise<number> {
        if (!this._userCode) {
            await this.updateUser();
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

    /**
     * Carrega o cache de lojas no local storage
     */
    async loadContaCache(): Promise<void> {
        let error = true;
        const object = await localStorage.getItem(Keys.CONTAS);

        if (object) {
            const contaCache = JSON.parse(object);
            if (contaCache) {
                error = false;
                this.contaCache = contaCache;
                this.contaOptions = this.contaCache.map(conta => {
                    return {
                        label: `${conta.numero} - ${conta.nome}`,
                        numero: conta.numero
                    }
                });
            }
        }

        if (error) {
            await this.updateContaCache();
        }
    }

    /**
     * Atualiza cache de contas no state e no localstorage do banco
     */
    async updateContaCache(): Promise<void> {
        await this.listConta(true).then(() => {}, error => { throw error });
        if (this.contaCache) {
            localStorage.setItem(Keys.CONTAS, JSON.stringify(this.contaCache));
            this.contaOptions = this.contaCache.map(conta => {
                return {
                    label: `${conta.numero} - ${conta.nome}`,
                    numero: conta.numero
                };

            });
        }
    }

    /**
     * Listar contas
     */
    async listConta(cache?: boolean): Promise<void> {
        let params = {};
        if (this.option && this.option.key == 0 && this.searchStr) {
            params['numero'] = this.searchStr;
        }

        if (this.option && this.option.key == 1 && this.searchStr) {
            params['nome'] = this.searchStr;
        }

        await this.contaService.listar(46, params).then(lista => {
            if (cache) {
                this.contaCache = lista;
            } else {
                this.contas = lista;
            }

            this.tableContent = lista.map(conta => {
                return {
                    noConta : conta.numero,
                    nomeConta : conta.nome
                };
            })
            this.tableContent.sort((c1, c2) => c1.numero > c2.numero ? -1 : 1);
        }, error => {
            throw error;
        });
    }

    async list(): Promise<void> {
        let usuario = await this.userCode();
        usuario = usuario ? usuario : 46;

        this.lancamentoService.listar(usuario, this.dataInicio.toDateString(), this.dataFinal.toDateString()).then(lista => {
            if (lista) {
                this.tableValues = (lista as Array<LancamentoEntities>).map(lancamento => {
                    let data: string = '';
                    if (lancamento.data) {
                        data = UtilService.dataFormatada(lancamento.data);
                    }

                    return {
                        codigo: lancamento.codigo,
                        data: data,
                        historico: lancamento.historico
                    }
                });
            }
        }, error => {
            this.toastType = 'error';
            this.toastMsg = error?.message;
            this.toastShow = true;
        });
    }
}

export default class ReleasePage extends React.Component {
    public state: InternalState = new InternalState();
    private releaseService: ReleaseService = new ReleaseService();
    public options: IOptionType[] = [{key: 0, value: "Número da Conta"}, {
        key: 1,
        value: "Nome da Conta"
    }];
    public tableLabels = ["Data", "Historico"];

    public componentDidMount() {
        this.state.releases = this.releaseService.getReleases();
        this.forceUpdate();
    }

    newLancamento(): void {
        this.state.screenState = ScreenState.NEW;
    }

    public handleKeyPress(evt: any) {

    }

    public handleInputOption(value: any) {
        this.state.option = value;
        this.forceUpdate();
    }

    public handleInputText(value: any) {
        this.state.searchStr = value;
        this.forceUpdate();
    }

    changeConta(evento: any): void {
        console.log(evento);
        this.state.optionSelected = evento;
        this.forceUpdate();
    }

    public handleSelect(selectId: number) {
        console.log(selectId);
    }

    changeDateInicio(e: any): void {
        let date = new Date(e.target.value);
        console.log(date.toLocaleString());
        this.state.dataInicio = date;
    }

    changeDateFinal(e: any): void {
        let date = new Date(e.target.value);
        console.log(date.toLocaleString());
        this.state.dataFinal = date;
    }

    onToastClick(): void {
        this.state.toastShow = false;
        this.forceUpdate();
    }

    async onSearch(): Promise<void> {
        this.state.loading = true;
        this.forceUpdate();
        await this.state.list();
        this.state.loading = false;
        this.forceUpdate();
    }

    novo(): void {
        this.state.screenState = ScreenState.NEW;
        this.forceUpdate();
    }

    save(): void {

    }

    remover(): void {

    }

    voltar(): void {

    }

    getCredito(): number {
        return 0.00;
    }

    getDebito(): number {
        return 0.00;
    }

    adicionaValor(): void {

    }

    public render() {
        return (
            <div className="release-page animacaoSlide col-lg-12">

                <div className="search-container">

                    <div className="search-header">
                        <If test={!this.state.loading}>
                            <div className="search-field">
                                <span className="search-title">Lançamentos</span>

                                <Card className="card-generic col col-lg-12">
                                    <CardContent className="center">
                                        <div className="search-title-area">
                                            <form onSubmit={(e) => {
                                                e.preventDefault();
                                                this.onSearch();
                                            }}>

                                                <TextField
                                                    variant="outlined"
                                                    disabled={this.state.screenState != ScreenState.SEARCH}
                                                    id="datetime-local"
                                                    label="Data Inicial"
                                                    type="datetime-local"
                                                    defaultValue="2019-05-24T10:30"
                                                    onChange={e => this.changeDateInicio(e)}
                                                    InputLabelProps={{
                                                        shrink: true,
                                                    }}
                                                />

                                                <TextField
                                                    variant="outlined"
                                                    disabled={this.state.screenState != ScreenState.SEARCH}
                                                    id="datetime-local-2"
                                                    label="Data Final"
                                                    type="datetime-local"
                                                    defaultValue="2020-07-01T10:30"
                                                    onChange={e => this.changeDateFinal(e)}
                                                    InputLabelProps={{
                                                        shrink: true,
                                                    }}
                                                />


                                                <Button
                                                    className={this.state.screenState != ScreenState.SEARCH ? "search-btn-off" : "search-btn"}
                                                    disabled={this.state.screenState != ScreenState.SEARCH}
                                                    type="submit">
                                                    <i className="bloom"></i>
                                                    <span>Pesquisar</span>
                                                </Button>
                                                <div className="col-lg-2 div25p">
                                                    <Button
                                                        className={this.state.screenState != ScreenState.SEARCH ? "search-btn-off" : "search-btn"}
                                                        disabled={this.state.screenState != ScreenState.SEARCH}
                                                        onClick={evento => this.novo()}>
                                                        <i className="bloom"></i>
                                                        <span>Novo</span>
                                                    </Button>
                                                </div>
                                            </form>

                                        </div>
                                    </CardContent>
                                </Card>
                            </div>
                        </If>
                        <If test={this.state.screenState == ScreenState.SEARCH && this.state.tableValues.length > 0}>
                            <Card className="card-table col-lg-12 animacaoSlide">
                                <CardContent>
                                    <GenericTable labels={this.tableLabels}
                                                  datas={this.state.tableValues}
                                                  ignoreColums={[0]}
                                                  onSelect={(value) => this.handleSelect(value)}/>
                                </CardContent>
                            </Card>
                        </If>

                    </div>
                </div>
                <If test={this.state.loading}>

                    <div className="padding">Carregando ...</div>
                    <div className="d-flex w-100 justify-content-center">

                        <CircularProgress color={"primary"} size={50} thickness={5}/>
                    </div>

                </If>
                <If test={this.state.screenState != ScreenState.SEARCH && !this.state.loading}>
                    <Card className="card-generic animacaoSlide">
                        <CardContent>
                            <div className="side-by-side">
                                <div className="col-md-4">
                                    <TextField
                                        variant="outlined"
                                        value={this.state.selecionado.data}
                                        disabled={this.state.screenState == ScreenState.EDIT}
                                        id="data-lanc"
                                        label="Data"
                                        type="datetime-local"
                                        defaultValue=""
                                        //onChange={e => this.changeDateInicio(e)}
                                        InputLabelProps={{
                                            shrink: true,
                                        }}
                                    />
                                </div>

                                <div className="col-md-8">
                                    <TextField
                                        variant="outlined"
                                        // error={this.state.error}
                                        fullWidth
                                        //value={this.state.numberConta}
                                        id="id-historico"
                                        type="text"
                                        label="Histórico"
                                        placeholder="Histórico"
                                        //margin="normal"
                                        // helperText={this.state.helperText}
                                        // onChange={e => this.handleEnterAccountNumber(e.target.value)}
                                        onKeyPress={e => this.handleKeyPress(e)}
                                    />
                                </div>
                            </div>

                            <div className="div-divider">
                                <Divider className="divider" />
                            </div>
                            <div className="side-by-side">
                                <div className="col-md-5 center">
                                    <Card className="card-generic ">
                                        <CardContent>
                                            <Autocomplete
                                                options={this.state.contaOptions}
                                                value={this.state.optionSelected.label}
                                                getOptionSelected={option => this.state.optionSelected.numero = option.numero}
                                                getOptionLabel={option => option.label}
                                                className="w-100"
                                                noOptionsText={"Carregando..."}
                                                multiple={false}
                                                onChange={(evento) => this.changeConta(evento)}
                                                onKeyPress={e => this.handleKeyPress(e)}
                                                renderInput={params => (
                                                    <TextField
                                                        {...params}
                                                        fullWidth
                                                        variant="outlined"
                                                        // error={this.state.error}
                                                        id="id-conta-pai"
                                                        margin="normal"
                                                        // helperText={this.state.helperText}
                                                        placeholder="Conta Pai"
                                                    />
                                                )}
                                            />
                                            <Autocomplete
                                                options={this.state.contaOptions}
                                                value={this.state.optionSelected.label}
                                                getOptionSelected={option => this.state.optionSelected.numero = option.numero}
                                                getOptionLabel={option => option.label}
                                                className="w-100"
                                                noOptionsText={"Carregando..."}
                                                multiple={false}
                                                onChange={(evento) => this.changeConta(evento)}
                                                onKeyPress={e => this.handleKeyPress(e)}
                                                renderInput={params => (
                                                    <TextField
                                                        {...params}
                                                        fullWidth
                                                        variant="outlined"
                                                        // error={this.state.error}
                                                        id="id-conta-pai"
                                                        margin="normal"
                                                        // helperText={this.state.helperText}
                                                        placeholder="Conta Pai"
                                                    />
                                                )}
                                            />

                                            <div className="padding-top">
                                                    <TextField
                                                        variant="outlined"
                                                        // error={this.state.error}
                                                        fullWidth
                                                        className="w-100 padding"
                                                        //value={this.state.numberConta}
                                                        id="id-valor"
                                                        type="number"
                                                        label="Valor"
                                                        placeholder="Valor"
                                                        //margin="normal"
                                                        // helperText={this.state.helperText}
                                                        // onChange={e => this.handleEnterAccountNumber(e.target.value)}
                                                        onKeyPress={e => this.handleKeyPress(e)}
                                                    />
                                            </div>
                                            <div className={"padding-top"}>
                                                <Button className="generic-btn"
                                                        onClick={evento => this.adicionaValor()}>
                                                    <span>Adicionar Valor</span>
                                                </Button>
                                            </div>
                                        </CardContent>
                                    </Card>

                                </div>


                                <div className="col-lg-7">
                                <Card className="card-generic ">
                                    <CardContent>
                                        <GenericTable
                                            labels={["Data", "Histórico", "Tipo", "Valor"]}
                                            datas={this.state.valores}
                                            ignoreColums={[0, 1]}/>
                                        <If test={this.state.selecionado}>
                                            {/*<Card className="card-internal col col-lg-12 margin-top">*/}
                                            {/*    <CardContent>*/}
                                            <Card className="total">
                                                <span className="entire-line">Total Debitos: {this.getDebito()}</span>
                                                <span>Total Debitos: {this.getCredito()}</span>

                                            </Card>
                                            {/*    </CardContent>*/}
                                            {/*</Card>*/}
                                        </If>

                                    </CardContent>
                                </Card>
                                </div>

                            </div>



                        </CardContent>
                    </Card>

                    <Card className="card-internal col col-lg-12 margin-top animacaoSlide">
                        <CardContent>
                            <Button className="generic-btn"
                                    onClick={evento => this.save()}>
                                <span>{this.state.screenState == ScreenState.NEW ? "Adicionar" : "Atualizar"}</span>
                            </Button>
                            <Button className="generic-btn"
                                    onClick={evento => this.remover()}>
                                <span>Apagar</span>
                            </Button>1
                            <Button className="generic-btn"
                                    onClick={evento => this.voltar()}>
                                <span>Voltar</span>
                            </Button>

                        </CardContent>
                    </Card>
                </If>
                <If test={!this.state.loading && this.state.toastShow}>
                    <Snackbar open={this.state.toastShow} autoHideDuration={6000}
                              onClick={e => this.onToastClick()}>
                        <Alert severity={this.state.toastType} onClick={e => this.onToastClick()}>
                            {this.state.toastMsg}
                        </Alert>
                    </Snackbar>
                </If>
            </div>
        );
    }
}