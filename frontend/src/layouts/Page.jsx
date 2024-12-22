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
import pagesStyle from "assets/jss/material-dashboard-pro-react/layouts/adminStyle.jsx";
import PropTypes from "prop-types";
import React from "react";
import { Redirect, Route, Switch } from "react-router-dom";
import routes from "config/rutas/Pages";

class Pages extends React.Component {
  wrapper = React.createRef();
  componentDidMount() {
    document.body.style.overflow = "unset";
  }
  getRoutes = routes => {
    return routes.map((prop, key) => {
      if (prop.collapse) {
        return this.getRoutes(prop.views);
      }
      if (prop.layout === "/page") {
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

  getActiveRoute = routes => {
    let activeRoute = "Default Brand Text";
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
    return (
      <div>
        <div className={classes.wrapper} ref={this.wrapper}>
          <div className={classes.fullPage}>
            <Switch>
              {this.getRoutes(routes)}
              <Redirect from="/page" to="/page/admin-request" />
            </Switch>
          </div>
        </div>
      </div>
    );
  }
}

Pages.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withStyles(pagesStyle)(Pages);
