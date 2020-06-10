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
                        <li>
                            <Link to="/">Home</Link>
                        </li>
                        <li>
                            <Link to="/login">Login</Link>
                        </li>
                        <li>
                            <Link to="/register">Register</Link>
                        </li>
                        <li>
                            <Link to="/about">About</Link>
                        </li>
                    </ul>
                </div>
            </div>
        );
    }
}
