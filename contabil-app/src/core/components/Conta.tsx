import React, { useRef } from 'react';
import '../../assets/css/components/Conta.scss';
import Autocomplete from '@material-ui/lab/Autocomplete';
import { Button, Card, CardContent, CircularProgress, Divider, TextField } from '@material-ui/core';
import GenericTable from '../../components/GenericTable';
//import SearchService from '../service/SearchService';
import { ContaService } from "../service/ContaService";
import { ContaEntities } from "../../entities/conta.entities";
import If from '../common/If';
import { ScreenState } from "../../entities/screen-state.enum";
import { Keys } from "../../entities/keys.enum";
import { UsuarioEntities } from "../../entities/usuario.entities";
import Snackbar from "@material-ui/core/Snackbar";
import MuiAlert from '@material-ui/lab/Alert';
import { UtilService } from "../service/util-service";

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
    private contaService: ContaService;
    tableContent: Array<any> = [];
    contas: Array<ContaEntities>;
    title: string;
    loading = false;
    contaSelecionada: ContaEntities
    screenState: ScreenState;
    contaCache: Array<ContaEntities>;
    contaOptions: Array<any> = [];
    nameConta: string = '';
    numberConta: string = '';
    optionSelected: any;
    valores: Array<any>;
    private _userCode: number;
    toastMsg: string;
    toastShow: boolean;
    toastType: string;

    constructor() {
        this.option = null;
        this.searchStr = "";
        this.hasError = false;
        this.contaService = new ContaService();
        this.title = 'Buscar Conta';
        this.screenState = ScreenState.SEARCH;
        this.optionSelected = { label: '-', numero: 0 };
        this.loadContaCache();
        this.toastShow = false;
        this.toastType = 'success';
        this.toastMsg = '';
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
     * Pega conta. Colocar try and catch no error pro modal 
     * @param codigoConta
     */
    async getConta(codigoConta: number): Promise<any> {
        return await this.contaService.get(codigoConta).then(response => response, error => { throw error; });
    }

    /**
     * Adiciona conta
     * @param conta conta a adicionar
     */
    async addConta(conta: ContaEntities): Promise<any> {
        return await this.contaService.adicionar(conta).then(response => response, error => { throw error; });
    }

    /**
     * Remove conta
     * @param conta conta a adicionar
     */
    async removeConta(conta: ContaEntities): Promise<any> {
        return await this.contaService.remover(conta).then(response => response, error => { throw error; });
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
}

export default class Conta extends React.Component {
    public state: InternalState = new InternalState();
    private contaService: ContaService = new ContaService();
    public options: IOptionType[] = [
        { key: 0, value: "Número da Conta" },
        { key: 1, value: "Nome da Conta" }
    ];
    public componentDidMount(){
        if(this.options.length > 0){
            this.state.option = this.options[0];
            this.forceUpdate();
        }
    }
    public handleKeyPress(evt: any) {

    }
    public handleInputOption(value: any) {
        debugger;
        this.state.option = value;
        this.forceUpdate();
    }
    public handleInputText(value: any) {
        this.state.searchStr = value;
        this.forceUpdate();
    }
    public async onSearch(): Promise<void> {
        this.clearConta();
        this.state.loading = true;
        this.forceUpdate();
        try {
            await this.state.listConta();
        } catch (e) {
            this.state.toastType = 'error';
            this.state.toastMsg = e?.message === 'Failed to fetch' ? 'Erro ao acessar o servidor' : e?.message;
            this.state.toastShow = true;
        } finally {
            this.state.loading = false;
        }
        this.forceUpdate();
    }

    clearConta(): void {
        this.state.contaSelecionada = new ContaEntities();
        this.state.nameConta = '';
        this.state.numberConta = '';
        this.state.valores = [];
        this.state.contas = [];
        this.state.option = { key: -1, value: '' };
        this.state.searchStr = '';
        this.state.tableContent = [];
    }

    handleVoltarClick(): void {
        this.state.screenState = ScreenState.SEARCH;
        this.forceUpdate();
    }

    loading(disable?: boolean): void {
        this.state.loading = !disable;
        this.forceUpdate();
    }

    getDebito(): string {
        return this.state.contaSelecionada && this.state.contaSelecionada.saldo ? this.state.contaSelecionada.saldo.totalDebito.toFixed(2) : '0.00';
    }

    getCredito(): string {
        return this.state.contaSelecionada && this.state.contaSelecionada.saldo ? this.state.contaSelecionada.saldo.totalCredito.toFixed(2) : '0.00';
    } ntaSelecionada

    handleEnterAccountName(event: any): void {
        // console.log(event);
        this.state.nameConta = event;
        this.forceUpdate();
    }

    changeContaPai(value: any): void {
        // console.log(value);
        this.state.optionSelected = value;
        this.forceUpdate();
    }

    getSaldo(): string {
        return this.state.contaSelecionada && this.state.contaSelecionada.saldo ? this.state.contaSelecionada.saldo.saldo.toFixed(2) + ' - '
            + this.state.contaSelecionada.saldo.tipoSaldo[0] : '0';
    }

    async handleSelect(conta: any): Promise<void> {
        this.state.loading = true;
        const foundConta = this.state.contas.find(contaEntity =>
            contaEntity.numero === this.state.tableContent[conta].noConta);
        if (foundConta) {
            this.loading();
            if (foundConta.codigo) {
                const response = await this.state.getConta(foundConta.codigo);
                if (response) {
                    this.state.contaSelecionada = response;
                }
            }

            this.loading(true);
            this.state.nameConta = this.state.contaSelecionada.nome ? this.state.contaSelecionada.nome : '';
            this.state.numberConta = this.state.contaSelecionada.numero ? this.state.contaSelecionada.numero : '';
            if (this.state.contaSelecionada.contaPaiCodigo) {
                const contaPai = this.state.contaCache.find(conta =>
                    this.state.contaSelecionada.contaPaiCodigo === conta.contaPaiCodigo);
                if (contaPai) {
                    this.state.optionSelected = {
                        label: `${contaPai.numero} - ${contaPai.nome}`,
                        numero: contaPai.numero
                    };
                }
            } else {
                this.state.optionSelected = '-';
            }

        }
        //`${valor.data?.getDay()}/${valor.data?.getMonth()}/${valor.data?.getFullYear()}`,
        if (this.state.contaSelecionada.valores) {
            this.state.valores = this.state.contaSelecionada.valores.map(valor => {
                let data: string = '';
                if (valor.data) {
                    data = UtilService.dataFormatada(valor.data);
                }

                return {
                    codigo: valor.codigo,
                    codigoLancamento: valor.codigoLancamento,
                    data: data,
                    historico: valor.historico,
                    tipo: valor.tipo,
                    valor: valor.valor.toFixed(2)
                }
            })
        }
        this.state.screenState = ScreenState.EDIT;
        this.state.tableContent = [];
        this.state.loading = false;
        this.forceUpdate();
    }

    async save(): Promise<void> {
        if (this.state.screenState === ScreenState.NEW) {
            this.state.loading = true;
            this.forceUpdate();
            const conta = new ContaEntities();
            conta.nome = this.state.nameConta;
            conta.codigoUsuario = await this.state.userCode();
            if (!conta.codigoUsuario) {
                conta.codigoUsuario = 46;
            }
            if (this.state.optionSelected && this.state.optionSelected.numero) {
                const contaPai = this.state.contaCache.find(contaInList =>
                    contaInList.numero === this.state.optionSelected.numero);
                if (contaPai && contaPai.codigo) {
                    conta.contaPaiCodigo = contaPai.codigo;
                }
            }
            try {
                await this.state.addConta(conta); // todo: try and catch
            } catch (e) {
                console.log(e);
            }

            this.state.toastMsg = `Conta ${conta.nome} adicionada com sucesso!`;
            this.state.loading = false;
            this.forceUpdate();
            this.handleVoltarClick();
            this.state.toastShow = true;
            this.forceUpdate();
        }

    }

    async removeConta(): Promise<void> {
        if (this.state.screenState === ScreenState.EDIT) {
            this.state.loading = true;
            // const conta = new ContaEntities();
            // conta.nome = this.state.nameConta;
            // conta.codigoUsuario = await this.state.userCode();
            // if (!conta.codigoUsuario) {
            //     conta.codigoUsuario = 46;
            // }
            // if (this.state.optionSelected && this.state.optionSelected.numero) {
            //     const contaPai = this.state.contaCache.find(contaInList =>
            //         contaInList.numero === this.state.optionSelected.numero);
            //     if (contaPai && contaPai.codigo) {
            //         conta.contaPaiCodigo = contaPai.codigo;
            //     }
            // }

            await this.state.removeConta(this.state.contaSelecionada); // todo: try and catch
            this.state.loading = false;
            this.forceUpdate();
            this.state.toastMsg = `Conta ${this.state.contaSelecionada.nome} removida com sucesso!`;
            this.clearConta();
            this.handleVoltarClick();
            this.state.toastShow = true;
            this.forceUpdate();
        }
    }

    onToastClick(): void {
        this.state.toastShow = false;
        this.forceUpdate();
    }

    newConta(): void {
        this.clearConta();
        this.state.screenState = ScreenState.NEW;
        this.forceUpdate();
    }

    private getPlaceHolderSearchInput(): string {
        return (this.state.option?.key == 0) ? 'Ex: 01.01' : 'Ex: Caixa';
    }

    public render() {
        return (
            <div className="search-page">
                <div className="search-container animacaoSlide">
                    <div className="search-header">
                        <div className="search-field">
                            <If test={!this.state.loading}>
                                <span className="search-title">{this.state.title}</span>


                                <Card className="card-generic col col-lg-12">
                                    <CardContent className="center">
                                        <Card className="card-mini col-lg-12"><CardContent>
                                            <form className="" onSubmit={(e) => {
                                                e.preventDefault();
                                                this.onSearch();
                                            }}>

                                                <Autocomplete
                                                    className="search-options"
                                                    value={this.state.option}
                                                    options={this.options}
                                                    getOptionLabel={option => option.value}
                                                    disabled={this.state.screenState != ScreenState.SEARCH}
                                                    noOptionsText={"Carregando..."}
                                                    multiple={false}
                                                    onChange={(e, v) => this.handleInputOption(v)}
                                                    onKeyPress={e => this.handleKeyPress(e)}
                                                    renderInput={params => (
                                                        <TextField
                                                            {...params}
                                                            fullWidth
                                                            variant="outlined"
                                                            error={this.state.hasError}
                                                            id="id-conta-pai"
                                                            margin="normal"
                                                            placeholder="Selecione uma Opção"
                                                        />
                                                    )}
                                                />
                                                <TextField
                                                    className="search-input"
                                                    variant="outlined"
                                                    error={this.state.hasError}
                                                    fullWidth
                                                    disabled={this.state.screenState != ScreenState.SEARCH}
                                                    id="id-busca-texto"
                                                    type="string"
                                                    label="Busca"
                                                    placeholder={this.getPlaceHolderSearchInput()}
                                                    margin="normal"
                                                    onChange={e => this.handleInputText(e.target.value)}
                                                    onKeyPress={e => this.handleKeyPress(e)}
                                                />

                                                <Button className={this.state.screenState != ScreenState.SEARCH ? "search-btn-off" : "search-btn"}
                                                    disabled={this.state.screenState != ScreenState.SEARCH}
                                                    type="submit">
                                                    <i className="bloom"></i>
                                                    <span>Pesquisar</span>
                                                </Button>
                                                <div className="col-lg-2 div25p">
                                                    <Button className={this.state.screenState != ScreenState.SEARCH ? "search-btn-off" : "search-btn"}
                                                        disabled={this.state.screenState != ScreenState.SEARCH}
                                                        onClick={evento => this.newConta()}>
                                                        <i className="bloom"></i>
                                                        <span>Novo</span>
                                                    </Button>
                                                </div>
                                            </form>
                                        </CardContent></Card>
                                    </CardContent>
                                </Card>
                            </If>
                        </div>
                    </div>
                    <If test={this.state.loading}>
                        <div className="empty"></div>
                        <div className="col-lg-12">
                            "                            <div className="padding">Carregando ...</div>
                            <div className="d-flex w-100 justify-content-center">
                                <CircularProgress color={"primary"} size={50} thickness={5} />
                            </div>
                        </div>
                    </If>
                    <If test={!this.state.loading && this.state.screenState == ScreenState.SEARCH
                        && this.state.tableContent.length > 0}>
                        <Card className="card-generic animacaoSlide">
                            <CardContent>
                                <GenericTable labels={["Nº da Conta", "Nome da Conta"]}
                                    datas={this.state.tableContent}
                                    onSelect={value => this.handleSelect(value)} />
                            </CardContent>
                        </Card>
                    </If>
                    <If test={this.state.screenState != ScreenState.SEARCH && !this.state.loading}>
                        <Card className="card-generic animacaoSlide">
                            <CardContent>
                                <div className="row">
                                    <div className="col">
                                        <TextField
                                            variant="outlined"
                                            value={this.state.nameConta}
                                            //error={this.state.error}
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
                                            // error={this.state.error}
                                            fullWidth
                                            value={this.state.numberConta}
                                            id="id-numero-conta"
                                            disabled={true}
                                            type="text"
                                            label="Número da Conta"
                                            placeholder="Número da Conta"
                                            margin="normal"
                                            // helperText={this.state.helperText}
                                            // onChange={e => this.handleEnterAccountNumber(e.target.value)}
                                            onKeyPress={e => this.handleKeyPress(e)}
                                        />
                                    </div>
                                    <div className="col-md-6">
                                        <Autocomplete
                                            options={this.state.contaOptions}
                                            disabled={this.state.screenState != ScreenState.NEW}
                                            getOptionLabel={option => option.label}
                                            className="w-100"
                                            noOptionsText={"Carregando..."}
                                            multiple={false}
                                            onChange={(e, v) => this.changeContaPai(v)}
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
                                    </div>
                                </div>
                                <div className="div-divider">
                                    <Divider className="divider" />
                                </div>

                                <Card className="card-generic ">
                                    <CardContent>
                                        <GenericTable
                                            labels={["Data", "Histórico", "Tipo", "Valor"]}
                                            datas={this.state.valores}
                                            ignoreColums={[0, 1]} />
                                        <If test={this.state.contaSelecionada}>
                                            {/*<Card className="card-internal col col-lg-12 margin-top">*/}
                                            {/*    <CardContent>*/}
                                            <Card className="total">
                                                <span
                                                    className="entire-line">Total Debitos: {this.getDebito()}</span>
                                                <span>Total Debitos: {this.getCredito()}</span>
                                                <span>Saldo: {this.getSaldo()}</span>
                                            </Card>
                                            {/*    </CardContent>*/}
                                            {/*</Card>*/}
                                        </If>

                                    </CardContent>
                                </Card>


                            </CardContent>
                        </Card>

                        <Card className="card-internal col col-lg-12 margin-top animacaoSlide">
                            <CardContent>
                                <Button className="generic-btn"
                                    onClick={evento => this.save()}>
                                    <span>{this.state.screenState == ScreenState.NEW ? "Adicionar" : "Atualizar"}</span>
                                </Button>
                                <Button className="generic-btn"
                                    onClick={evento => this.removeConta()}>
                                    <span>Apagar</span>
                                </Button>
                                <Button className="generic-btn"
                                    onClick={evento => this.handleVoltarClick()}>
                                    <span>Voltar</span>
                                </Button>

                            </CardContent>
                        </Card>
                    </If>
                    <If test={this.state.toastShow}>
                        <Snackbar open={this.state.toastShow} autoHideDuration={6000} onClick={e => this.onToastClick()}>
                            <Alert severity={this.state.toastType} onClick={e => this.onToastClick()}>
                                {this.state.toastMsg}
                            </Alert>
                        </Snackbar>
                    </If>
                </div>
            </div>
        );
    }
}