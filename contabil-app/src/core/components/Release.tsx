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
import {TipoValor} from "../../entities/tipo-valor.enum";
import {ValorEntities} from "../../entities/valor.entities";

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
    tiposOptions: Array<any> = [];
    tipoSelecionado: any;
    valorAdicionar: number | null;
    dataLancamento: Date | null;

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
        this.optionSelected = { label: '-', numero: 0 };
        this.tipoSelecionado = {label: "DEBITO", key: TipoValor.DEBITO}
        this.tiposOptions = [
            {label: "Débito", key: TipoValor.DEBITO},
            {label: "Crédito", key: TipoValor.CREDITO}
        ];
        this.loadContaCache();

    }

    async userCode(): Promise<number> {
        if (!this._userCode) {
            await this.updateUser();
        }
        return this._userCode ? this._userCode : 46;
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
     * Adiciona lancamento
     * @param lancamenoto conta a adicionar
     */
    async adicionarLancamento(lancamento: LancamentoEntities): Promise<any> {
        return await this.lancamentoService.adicionar(lancamento).then(response => response, error => { throw error; });
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
        await this.listConta(true).then(() => { }, error => { throw error });
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
                    noConta: conta.numero,
                    nomeConta: conta.nome
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
    public options: IOptionType[] = [{ key: 0, value: "Número da Conta" }, {
        key: 1,
        value: "Nome da Conta"
    }];
    public tableLabels = ["Data", "Historico"];

    public componentDidMount() {
        //this.state.releases = this.releaseService.getReleases();
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

    changeConta(value: any): void {
        console.log(value);
        this.state.optionSelected = value;
        this.forceUpdate();
    }

    changeTipo(value: any): void {
        console.log(value);
        this.state.tipoSelecionado = value;
        this.forceUpdate();
    }

    public handleSelect(selectId: number) {
        console.log(selectId);
    }

    changeDateInicio(e: any): void {
        let date = new Date(e.target.value);
        console.log(date.toLocaleString());
        this.state.dataInicio = date;
        this.forceUpdate();
    }

    handleDataChanged(e: any): void {
        let date = new Date(e.target.value);
        console.log(date.toLocaleString());
        this.state.selecionado.data = date;
        this.state.dataLancamento = date;
        this.forceUpdate();
    }

    changeDateFinal(e: any): void {
        let date = new Date(e.target.value);
        console.log(date.toLocaleString());
        this.state.dataFinal = date;
        this.forceUpdate();
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

    handleValorAdicionarChange(e: any): void {
        this.state.valorAdicionar = e;
        this.forceUpdate();
    }

    async save(): Promise<void> {
        if (this.getCredito() != this.getDebito()) {
            this.state.toastType = 'error';
            this.state.toastMsg = 'Total de débitos e créditos dos valores devem ser iguais';
            this.state.toastShow = true;
            this.forceUpdate();
        } else if (this.state.selecionado.historico == null) {
            this.state.toastType = 'error';
            this.state.toastMsg = 'É necessário preencher o histórico';
            this.state.toastShow = true;
            this.forceUpdate();
        } else {
            this.state.loading = true;
            this.forceUpdate();
            const historico = this.state.selecionado.historico;
            this.state.selecionado = new LancamentoEntities();
            this.state.selecionado.historico = historico;
            this.state.selecionado.data = this.state.dataLancamento ? this.state.dataLancamento : new Date();
            this.state.selecionado.codigoUsuario = await this.state.userCode();

            this.state.selecionado.valores = this.state.valores.map(valor => {
                const c = this.state.contaCache.find(c => c.numero == valor.numeroConta);
                const v = new ValorEntities();
                v.valor = valor.valor;
                v.data = valor.data;
                v.tipo = valor.tipo;
                v.codigoConta = c && c.codigo ? c.codigo : 0;

                return v;
            });

            try {
                await this.state.adicionarLancamento(this.state.selecionado);
                this.state.toastType = 'success';
                this.state.toastMsg = 'Lançamento adicionado com sucesso';
                this.state.toastShow = true
                this.state.screenState = ScreenState.SEARCH;
            } catch (e) {
                this.state.toastType = 'error';
                this.state.toastMsg = e?.message;
                this.state.toastShow = true;
            } finally {
                this.state.loading = false;
                this.forceUpdate();
            }
        }
    }

    remover(): void {

    }

    clear(): void {
        this.state.selecionado = new LancamentoEntities();

        this.state.valores = [];
        this.state.contas = [];
        this.state.option = { key: -1, value: '' };
        this.state.searchStr = '';
        this.state.tableContent = [];
    }

    voltar(): void {
        this.clear();
        this.state.screenState = ScreenState.SEARCH;
        this.forceUpdate();
    }

    getCredito(): number {
        let credito = 0;
        this.state.valores.forEach(valor => {
            if (valor.tipo == TipoValor.CREDITO) {
                credito += valor.valor;
            }
        });
        return credito;
    }

    getDebito(): number {
        let debito = 0;
        this.state.valores.forEach(valor => {
            if (valor.tipo == TipoValor.DEBITO) {
                debito += valor.valor;
            }
        });
        return debito;
    }

    cleanValorForm(): void {
        this.state.optionSelected = { label: '-', numero: 0 };
        this.state.tipoSelecionado = {label: "Débito", key: TipoValor.DEBITO};
        this.state.valorAdicionar = null;
    }

    adicionaValor(): void {
        if (this.state.valorAdicionar != null && this.state.optionSelected != null &&
            this.state.tipoSelecionado != null) {
            this.state.valores.push({
                numeroConta: this.state.optionSelected.numero,
                conta: this.state.optionSelected.label,
                tipo: this.state.tipoSelecionado.key,
                valor: this.state.valorAdicionar//.toFixed(2)
            });
            this.cleanValorForm();
            this.state.toastType = 'success';
            this.state.toastMsg = 'Novo valor adicionado'
        } else {
            this.state.toastType = 'error';
            this.state.toastMsg = 'Favor preencher todos campos para adicionar novo valor';
        }
        this.state.toastShow = true;
        this.forceUpdate();
    }




    handleHistoricoChange(e: any): void {
        this.state.selecionado.historico = e;
        this.forceUpdate();
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
                                                    className={this.state.screenState != ScreenState.SEARCH ? "generic-btn-off" : "search-btn"}
                                                    disabled={this.state.screenState != ScreenState.SEARCH}
                                                    type="submit">
                                                    <i className="bloom"></i>
                                                    <span>Pesquisar</span>
                                                </Button>
                                                <div className="col-lg-2 div25p">
                                                    <Button
                                                        className={this.state.screenState != ScreenState.SEARCH ? "generic-btn-off" : "search-btn"}
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
                                        onSelect={(value) => this.handleSelect(value)} />
                                </CardContent>
                            </Card>
                        </If>

                    </div>
                </div>
                <If test={this.state.loading}>

                    <div className="padding">Carregando ...</div>
                    <div className="d-flex w-100 justify-content-center">

                        <CircularProgress color={"primary"} size={50} thickness={5} />
                    </div>

                </If>
                <If test={this.state.screenState != ScreenState.SEARCH && !this.state.loading}>
                    <Card className="card-generic animacaoSlide">
                        <CardContent>
                            <div className="side-by-side">
                                <div className="col-md-4">
                                    <TextField
                                        variant="outlined"
                                        value={this.state.dataLancamento}
                                        disabled={this.state.screenState == ScreenState.EDIT}
                                        id="data-lanc"
                                        label="Data"
                                        type="datetime-local"
                                        defaultValue="2019-05-24T10:30"
                                        onChange={e => this.handleDataChanged(e)}
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
                                        value={this.state.selecionado.historico}
                                        id="id-historico"
                                        type="text"
                                        label="Histórico"
                                        placeholder="Histórico"
                                        //margin="normal"
                                        // helperText={this.state.helperText}
                                        onChange={e => this.handleHistoricoChange(e.target.value)}
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
                                                value={this.state.optionSelected}
                                                options={this.state.contaOptions}
                                                getOptionLabel={option => option.label}
                                                className="w-100"
                                                noOptionsText={"Carregando..."}
                                                multiple={false}
                                                onChange={(e, v) => this.changeConta(v)}
                                                onKeyPress={e => this.handleKeyPress(e)}
                                                renderInput={params => (
                                                    <TextField
                                                        {...params}
                                                        fullWidth
                                                        variant="outlined"
                                                        // error={this.state.error}
                                                        id="id-conta-valor"
                                                        margin="normal"
                                                        // helperText={this.state.helperText}
                                                        placeholder="Conta"
                                                    />
                                                )}
                                            />
                                            <Autocomplete
                                                value={this.state.tipoSelecionado}
                                                options={this.state.tiposOptions}
                                                getOptionLabel={option => option.label}
                                                className="w-100"
                                                noOptionsText={"Carregando..."}
                                                multiple={false}
                                                onChange={(e, v) => this.changeTipo(v)}
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
                                                    value={this.state.valorAdicionar}
                                                    id="id-valor"
                                                    type="number"
                                                    label="Valor"
                                                    placeholder="Valor"
                                                    //margin="normal"
                                                    // helperText={this.state.helperText}
                                                    onChange={e => this.handleValorAdicionarChange(e.target.value)}
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
                                                labels={["Conta", "Tipo", "Valor"]}
                                                datas={this.state.valores}
                                                ignoreColums={[0]} />
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
                            <div className="w-100 side-by-side-bt">
                                <Button className="generic-btn-no-icon"
                                    onClick={evento => this.save()}>
                                    <span>{this.state.screenState == ScreenState.NEW ? "Adicionar" : "Atualizar"}</span>
                                </Button>
                                <Button className="generic-btn-no-icon"
                                    onClick={evento => this.remover()}>
                                    <span>Apagar</span>
                                </Button>
                                <Button className="generic-btn-no-icon"
                                    onClick={evento => this.voltar()}>
                                    <span>Voltar</span>
                                </Button>
                            </div>
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