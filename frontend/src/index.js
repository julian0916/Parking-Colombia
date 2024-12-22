/*!

=========================================================
* Material Dashboard PRO React - v1.7.0
=========================================================

* Product Page: https://www.creative-tim.com/product/material-dashboard-pro-react
* Copyright 2019 Creative Tim (https://www.creative-tim.com)

* Coded by Creative Tim

=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

*/
import "assets/scss/material-dashboard-pro-react.scss?v=1.7.0";
import { createBrowserHistory } from "history";
import AdminLayout from "layouts/Admin.jsx";
import AuthLayout from "layouts/Auth.jsx";
import PageLayout from "layouts/Page.jsx";
import React from "react";
import ReactDOM from "react-dom";
import { Provider } from "react-redux";
import { Redirect, Route, Router, Switch } from "react-router-dom";
import store from "config/general/RootReducer";
import { ThemeProvider } from "@material-ui/core/styles";
import {PRINCIPAL,BUSCAR_TABLA} from "config/general/Temas";

const hist = createBrowserHistory();

ReactDOM.render(
  <Provider store={store}>
    <Router history={hist}>
      <Switch>
          <Route path="/page" component={PageLayout} />
          <Route path="/auth" component={AuthLayout} />
          <Route path="/admin" component={AdminLayout} />
        <Redirect from="/" to="/auth" />
        <Redirect exact from="/" to="/auth" />
        <Redirect to="/auth" />  {/* Captura las rutas y las redirecciona a AUTH */}
      </Switch>
    </Router>
  </Provider>,
  document.getElementById("root")
);
