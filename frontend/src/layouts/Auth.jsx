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
// @material-ui/core components
import withStyles from "@material-ui/core/styles/withStyles";
import login from "assets/img/login.jpeg";
//import register from "assets/img/register.jpeg";
import pagesStyle from "assets/jss/material-dashboard-pro-react/layouts/authStyle.jsx";
import PropTypes from "prop-types";
import React from "react";
import { Redirect, Route, Switch } from "react-router-dom";
import routes from "config/rutas/Auth";
import IntegrationNotistack from "conten/mensajes/Message.jsx";
import {
  IS_LOGGED,
  ID_PERFIL,
  PERFIL_CUENTAS,
} from "config/general/Configuracion";

class Auth extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      token: "",
      accountCode: ""
    };
  }
  wrapper = React.createRef();
  componentDidMount() {
    document.body.style.overflow = "unset";
    var urlParams = new URLSearchParams(this.props.location.search);
    if (urlParams.has("token")) {
      this.setState({ token: urlParams.get("token") });
    }
    if (urlParams.has("accountCode")) {
      this.setState({ accountCode: urlParams.get("accountCode") });
    }
  }
  getRoutes = routes => {
    return routes.map((prop, key) => {
      if (prop.collapse) {
        return this.getRoutes(prop.views);
      }
      if (prop.layout === "/auth") {
        return (
          <Route
            path={prop.layout + prop.path}
            component={prop.component}
            key={key}
          />
        );
      } else {
        return null;
      }
    });
  };
  getBgImage = () => {
    if (window.location.pathname.indexOf("/auth/register-page") !== -1) {
      //return register;
      return login;
    } else if (
      window.location.pathname.indexOf("/auth/ingreso-sistema") !== -1
    ) {
      return login;
    }
  };
  getActiveRoute = routes => {
    let activeRoute = "Cargando Z.E.R ...";
    for (let i = 0; i < routes.length; i++) {
      if (routes[i].collapse) {
        let collapseActiveRoute = this.getActiveRoute(routes[i].views);
        if (collapseActiveRoute !== activeRoute) {
          return collapseActiveRoute;
        }
      } else {
        if (
          window.location.href.indexOf(routes[i].layout + routes[i].path) !== -1
        ) {
          return routes[i].name;
        }
      }
    }
    return activeRoute;
  };
  render() {
    const { classes } = this.props;
    var uri = "/auth/ingreso-sistema";
    return (
      <div>
        <div className={classes.wrapper} ref={this.wrapper}>
        <IntegrationNotistack />
          <div
            className={classes.fullPage}
            style={{ backgroundImage: "url(" + this.getBgImage() + ")" }}
          >
            <Switch>
              {this.getRoutes(routes)}
              <Redirect from="/auth" to={uri} />
            </Switch>
          </div>
        </div>
      </div>
    );
  }
}

Auth.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withStyles(pagesStyle)(Auth);
