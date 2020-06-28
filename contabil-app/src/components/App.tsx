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
import Search from "../core/components/Conta";

const RouteWrapper = ({component: Component, restricted, ...rest}) => {
  return (
      // restricted = false meaning public route
      // restricted = true meaning restricted route
      <Route {...rest} render={props => (
          restricted ?
              <Redirect to="/entrar" />
          : <Content><Component {...props} /></Content>
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
export default class App extends React.Component {

  private get isAuthenticated(){
    return false;
  }
  render() {
    return (
      <div className="app-page">
        <Router>
          <Navigation />
          <RouteWrapper exact path="/" component={Home} restricted={!this.isAuthenticated} />
          <RouteWrapper exact path="/sobre" component={About} restricted={!this.isAuthenticated} />
          <RouteWrapper exact path="/entrar" component={LoginPage} restricted={false} />
          <RouteWrapper exact path="/registrar" component={RegisterPage} restricted={false} />
          <RouteWrapper exact path="/conta" component={Search} restricted={false} />
          <RouteWrapper exact path="/editar" component={EditRegisterPage} restricted={false} />
          <RouteWrapper exact path="/balancete" component={BalancePage} restricted={false} />
          <RouteWrapper exact path="/lancamento" component={ReleasePage} restricted={false} />
        </Router>
      </div>
    );
  }
}
