// src/index.js

import React from "react";
import ReactDOM from "react-dom";
import App from './components/App';
import './assets/config.scss';
import { Auth0Provider } from "@auth0/auth0-react";

ReactDOM.render(
  <Auth0Provider
  domain="dev-220bzycl.us.auth0.com"
  clientId="k1D7JSRUbSMS4j7VuDbuKuiXAwFpnG4S"
  redirectUri={`${window.location.origin}/entrar`}
>
  <App />
</Auth0Provider>,
  document.getElementById("root-app")
);

