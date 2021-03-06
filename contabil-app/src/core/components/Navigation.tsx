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
                            <Link to="/conta">Conta</Link>
                        </li>
                        <li>
                            <Link to="/balancete">Balancete</Link>
                        </li>
                        <li>
                            <Link to="/lancamento">Lançamento</Link>
                        </li>
                        <li>
                            <Link to="/sobre">Sobre nós</Link>
                        </li>
                    </ul>
                </div>
                <div className="login-controls-section">
                    {this.props.children}
                </div>
            </div>
        );
    }
}
