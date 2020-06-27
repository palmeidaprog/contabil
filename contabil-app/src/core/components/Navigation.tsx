import * as React from 'react';
import { Link } from "react-router-dom";
import "../../assets/css/components/Navigation.scss";

export default class Navigation extends React.Component {

    public render() {
        return (
            <div className="navigation">
                <div className="logo">
                    <div className="nav-logo"></div>
                </div>
            
                <div className ="row links">
                    <ul>
                        {/* <li>
                            <Link to="/">Início</Link>
                        </li> */}
                        <li>
                            <Link to="/entrar">Entrar</Link>
                        </li>
                        <li>
                            <Link to="/editar">Editar Conta</Link>
                        </li>
                        <li>
                            <Link to="/balancete">Balancete</Link>
                        </li>
                        <li>
                            <Link to="/buscar">Buscar</Link>
                        </li>
                        <li>
                            <Link to="/sobre">Sobre nós</Link>
                        </li>
                    </ul>
                </div>
            </div>
        );
    }
}
