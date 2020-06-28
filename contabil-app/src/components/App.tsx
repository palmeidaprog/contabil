import * as React from 'react';
import "../assets/css/App.scss";

import Navigation from '../core/components/Navigation';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Redirect
} from "react-router-dom";
import LoginPage from '../core/components/Login';
import RegisterPage from '../core/components/Register';
import EditRegisterPage from '../core/components/EditRegister';
import BalancePage from '../core/components/Balance';
import ReleasePage from '../core/components/Release';
import { Button } from '@material-ui/core';
import If from '../core/common/If';
import { useAuth0 } from "@auth0/auth0-react";

import Search from "../core/components/Conta";

const RouteWrapper = ({component: Component, restricted, authProps, ...rest}) => {
  return (
      // restricted = false meaning public route
      // restricted = true meaning restricted route
      <Route {...rest} render={props => (
        <>
          <If test={restricted}>
            <Redirect to="/entrar" />
          </If>
          <If test={!restricted}>
            <Content><Component {...props} auth={authProps}/></Content>
          </If>
          </>
      )} />
  );
};


function Home() {
  return <h2>Inicio</h2>;
}

function About() {
  return <h2>Sobre</h2>;
}
function Content(props : {classProps? : any, children : any}){
  return (
    <div className={`content ${props.classProps}`}>
      {props.children}
    </div>
  );
}
export default function App() {
  const { loginWithRedirect, logout, isAuthenticated, getAccessTokenSilently, user } = useAuth0();
    return (
      <div className="app-page">
        <Router>
          <Navigation>
            <If test={isAuthenticated}>
              <span className="login-msg">{`Olá ${user?.name}!`}</span>
              <Button className="auth-btn" onClick={()=>logout()}>Logout</Button>
            </If>
            <If test={!isAuthenticated}>
              <Button className="auth-btn" onClick={()=>loginWithRedirect()}>Login</Button>
            </If>
          </Navigation>
          <RouteWrapper exact path="/" component={Home} authProps={{user : user, getToken : getAccessTokenSilently}} restricted={true} />
          <RouteWrapper exact path="/sobre" component={About} authProps={{user : user, getToken : getAccessTokenSilently}} restricted={!isAuthenticated} />
          <RouteWrapper exact path="/entrar" component={LoginPage} authProps={{user : user, getToken : getAccessTokenSilently}} restricted={false} />
          <RouteWrapper exact path="/registrar" component={RegisterPage} authProps={{user : user, getToken : getAccessTokenSilently}} restricted={!isAuthenticated} />
          <RouteWrapper exact path="/conta" component={Search} authProps={{user : user, getToken : getAccessTokenSilently}} restricted={!isAuthenticated} />
          <RouteWrapper exact path="/editar" component={EditRegisterPage} authProps={{user : user, getToken : getAccessTokenSilently}} restricted={!isAuthenticated} />
          <RouteWrapper exact path="/balancete" component={BalancePage} authProps={{user : user, getToken : getAccessTokenSilently}} restricted={!isAuthenticated} />
          <RouteWrapper exact path="/lancamento" component={ReleasePage} authProps={{user : user, getToken : getAccessTokenSilently}} restricted={!isAuthenticated} />
        </Router>
      </div>
  );
}

