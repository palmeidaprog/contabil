import React from 'react';
import '../../assets/css/components/Search.scss';
import Autocomplete from '@material-ui/lab/Autocomplete';
import { TextField, Button, Card, CardContent } from '@material-ui/core';
import GenericTable from '../../components/GenericTable';
import SearchService from '../service/SearchService';
import {ContaService} from "../service/ContaService";
import {Simulate} from "react-dom/test-utils";
import error = Simulate.error;
import {Conta} from "../../entities/conta";
interface IOptionType{
    key : number,
    value : string
}
class InternalState{
    option : IOptionType | null;
    searchStr : string;
    hasError : boolean;
    private contaService: ContaService;
    contas: Array<any> = [];
    loadingActive = false;

    constructor() {
        this.option = null;
        this.searchStr = "";
        this.hasError = false;
        this.contaService = new ContaService();
    }

    async listConta(): Promise<void> {
        let params = {};
        if (this.option && this.option.key == 0 && this.searchStr) {
            params['numero'] = this.searchStr;
        }

        if (this.option && this.option.key == 1 && this.searchStr) {
            params['nome'] = this.searchStr;
        }

        await this.contaService.listar(46, params).then(lista => {
            this.contas = lista.map(conta => {
                return {
                    noConta : conta.numero,
                    nomeConta : conta.nome
                };
            })
            this.loadingActive = false;
        }, error => {
            throw error;
        });
    }
}
export default class SearchPage extends React.Component{
    public state : InternalState = new InternalState();
    private searchService : SearchService = new SearchService();
    public options : IOptionType[] = [{key : 0, value : "Número da Conta"}, {key : 1, value : "Nome da Conta"}]
    public handleKeyPress(evt : any){

    }
    public handleInputOption(value : any){
        this.state.option = value;
        this.forceUpdate();
    }
    public handleInputText(value : any){
        this.state.searchStr = value;
        this.forceUpdate();
    }
    public onSearch(){
        try {
            this.state.listConta();
        } catch (e) {
            console.log(e);
        } finally {
            console.log('ok');
        }

        console.log(this.state.option, this.state.searchStr);
    }

    public render(){
        return (
            <div className="search-container">
                <div className="search-header">
                    <div className="search-field">
                        <span className="search-title">Buscar Conta</span>
                        <Card className="card col col-lg-12">
                            <CardContent className="center">
                                <form onSubmit={(e) => {
                                    e.preventDefault();
                                    this.onSearch();
                                }}>

                                    <Autocomplete
                                        className="search-options"
                                        options={this.options}
                                        getOptionLabel={option => option.value}
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
                                        id="id-busca-texto"
                                        type="string"
                                        label="Busca"
                                        placeholder="Ex: 123456"
                                        margin="normal"
                                        onChange={e => this.handleInputText(e.target.value)}
                                        onKeyPress={e => this.handleKeyPress(e)}
                                    />
                                    <Button className="search-btn" type="submit">
                                        <i className="fas fa-search"></i>
                                        <span>Pesquisar</span>
                                    </Button>
                                </form>
                            </CardContent>
                        </Card>
                    </div>
                </div>
                <div className="search-content">
                        <GenericTable labels={["Nº da Conta", "Nome da Conta"]} datas={this.state.contas}/>
                    </div>
                
            </div>
        );
    }
}