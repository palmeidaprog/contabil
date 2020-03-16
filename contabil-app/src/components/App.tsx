import * as React from 'react';
import "../assets/css/App.scss";
import Navigation from '../core/components/Navigation';
import {
  BrowserRouter as Router,
  Switch,
  Route
} from "react-router-dom";
function Home() {
  return <h2>Home</h2>;
}

function About() {
  return <h2>About</h2>;
}
export default class App extends React.Component {
  render() {
    return (

      <div className="app-page">
        <Router>
          <Navigation />
          <Switch>
            <Route path="/about">
              <About />
            </Route>
            <Route path="/">
              <Home />
            </Route>
          </Switch>
        </Router>
      </div>
    );
  }
}
