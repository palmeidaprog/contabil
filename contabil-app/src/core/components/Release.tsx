import React from 'react';
import '../../assets/css/components/Release.scss';
import Autocomplete from '@material-ui/lab/Autocomplete';
import { TextField, Button } from '@material-ui/core';
import GenericTable from '../../components/GenericTable';
import ReleaseService from '../service/ReleaseService';
import { LancamentoEntities, LancamentoMapper } from '../../entities/lancamento.entities';
interface IOptionType{
    key : number,
    value : string
}
class InternalState{
    option : IOptionType | null;
    searchStr : string;
    hasError : boolean;
    releases : LancamentoEntities;
    tableValues : LancamentoMapper[];
    constructor(){
        this.releases = new LancamentoEntities();
        this.tableValues = [] as any;
        this.option = null;
        this.searchStr = "";
        this.hasError = false;
    }
}
export default class ReleasePage extends React.Component{
    public state : InternalState = new InternalState();
    private releaseService : ReleaseService = new ReleaseService();
    public options : IOptionType[] = [{key : 0, value : "Número da Conta"}, {key : 1, value : "Nome da Conta"}];
    public tableLabels = ["Número da Conta", "Nome da Conta", "Tipo do Valor", "Valor"];

    public componentDidMount(){
        this.state.releases = this.releaseService.getReleases();
        this.state.tableValues = this.state.releases.valores.map(item =>{
            let map : LancamentoMapper = new LancamentoMapper();
            map.noConta = item.codigo ? item.codigo : 0;
            map.nomeConta = item.historico ? item.historico : "S/N";
            map.tipoValor = item.tipo;
            map.valor = item.valor;
            return map;
        })
        this.forceUpdate();
    }
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
    public handleSelect(selectId : number){
        console.log(selectId);
    }
    public onSearch(){
        console.log(this.state.option, this.state.searchStr);
    }

    public render(){
        return (
            <div className="release-page">
                <div className="search-container">
                    <div className="search-header">
                        <div className="search-field">
                            <span className="search-title">Lançamentos</span>
                            <div className="search-title-area">
                                <form onSubmit={(e)=>{e.preventDefault(); this.onSearch();}}>

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
                            </div>
                        </div>
                    </div>
                    <div className="search-content"></div>
                    <GenericTable labels={this.tableLabels} datas={this.state.tableValues} onSelect={(value)=>this.handleSelect(value)}/>


                </div>
            </div>
        );
    }
}