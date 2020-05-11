import * as React from 'react';
import "../assets/css/App.scss";
import Navigation from '../core/components/Navigation';
import {
  BrowserRouter as Router,
  Switch,
  Route
} from "react-router-dom";
import LoginPage from '../core/components/Login';
import RegisterPage from '../core/components/Register';

function Home() {
  return <h2>Home</h2>;
}

function About() {
  return <h2>About</h2>;
}
function Content(props : {classProps? : any, children : any}){
  return (
    <div className={`content ${props.classProps}`}>
      {props.children}
    </div>
  );
}
export default class App extends React.Component {
  render() {
    return (
      <div className="app-page">
        <Router>
        <Navigation />
          <Route exact path="/" component={Home}>
            <Content>
              <Home />
            </Content>
          </Route>
          <Route exact path="/about" component={About}>
            <Content>
              <About />
            </Content>
          </Route>
          <Route exact path="/login" component={LoginPage}>
            <Content classProps="full-content">
              <LoginPage />
            </Content>
          </Route>
          <Route exact path="/register" component={RegisterPage}>
            <Content classProps="full-content">
              <RegisterPage />
            </Content>
          </Route>
        </Router>
      </div>
    );
  }
}
