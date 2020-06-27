import React from 'react';
import '../../assets/css/components/Search.scss';
import Autocomplete from '@material-ui/lab/Autocomplete';
import { TextField, Button } from '@material-ui/core';
import GenericTable from '../../components/GenericTable';
import SearchService from '../service/SearchService';
interface IOptionType{
    key : number,
    value : string
}
class InternalState{
    option : IOptionType | null;
    searchStr : string;
    hasError : boolean;
    constructor(){
        this.option = null;
        this.searchStr = "";
        this.hasError = false;
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
        console.log(this.state.option, this.state.searchStr);
    }

    public render(){
        return (
            <div className="search-container">
                <div className="search-header">
                    <div className="search-field">
                        <span className="search-title">Buscar Conta</span>
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
                <div className="search-content">
                        <GenericTable labels={["Nº da Conta", "Nome da Conta"]} datas={[{noConta : 1234, nomeConta : "teste"}]}/>
                    </div>
                
            </div>
        );
    }
}