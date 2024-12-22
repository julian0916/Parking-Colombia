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

import withStyles from "@material-ui/core/styles/withStyles";
import appStyle from "assets/jss/material-dashboard-pro-react/layouts/adminStyle.jsx";
import cx from "classnames";
import Footer from "components/Footer/Footer.jsx";

import AdminNavbar from "components/Navbars/AdminNavbar.jsx";
import Sidebar from "components/Sidebar/Sidebar.jsx";
import { setMenuUsuario } from "conten/ingresoSistema/IngresoSistemaActions";
import IntegrationNotistack from "conten/mensajes/Message.jsx";

import PerfectScrollbar from "perfect-scrollbar";
import "perfect-scrollbar/css/perfect-scrollbar.css";
import PropTypes from "prop-types";
import React from "react";
import { connect } from "react-redux";
import { Redirect, Route, Switch } from "react-router-dom";
import { routes } from "config/rutas/Admin.js";
import {
  IS_LOGGED,
  ID_PERFIL,
  PERFIL_CUENTAS,
  LOGO_64,
} from "config/general/Configuracion";
import AplicarSeguridad from "conten/components/AplicarSeguridad";

var ps;
class Dashboard extends React.Component {
  state = {
    mobileOpen: false,
    miniActive: false,
    image: LOGO_64, //require("assets/img/login.jpeg"),
    color: "blue",
    bgColor: "blue",
    hasImage: true,
    fixedClasses: "dropdown",
    logo: LOGO_64, //require("assets/img/logo-white.svg"),
    //logo: require("assets/img/logo_somos.svg"),
  };
  mainPanel = React.createRef();
  componentDidMount() {
    this.props.setMenuUsuario(routes(), (success) => {});
    if (navigator.platform.indexOf("Win") > -1) {
      ps = new PerfectScrollbar(this.mainPanel.current, {
        suppressScrollX: true,
        suppressScrollY: false,
      });
      document.body.style.overflow = "hidden";
    }
    window.addEventListener("resize", this.resizeFunction);
  }
  componentWillUnmount() {
    if (navigator.platform.indexOf("Win") > -1) {
      ps.destroy();
    }
    window.removeEventListener("resize", this.resizeFunction);
  }
  componentDidUpdate(e) {
    if (e.history.location.pathname !== e.location.pathname) {
      this.mainPanel.current.scrollTop = 0;
      if (this.state.mobileOpen) {
        this.setState({ mobileOpen: false });
      }
    }
  }
  handleImageClick = (image) => {
    this.setState({ image: image });
  };
  handleColorClick = (color) => {
    this.setState({ color: color });
  };
  handleBgColorClick = (bgColor) => {
    switch (bgColor) {
      case "white":
        this.setState({ logo: LOGO_64 });
        break;
      default:
        this.setState({ logo: LOGO_64 });
        break;
    }
    this.setState({ bgColor: bgColor });
  };
  handleFixedClick = () => {
    if (this.state.fixedClasses === "dropdown") {
      this.setState({ fixedClasses: "dropdown show" });
    } else {
      this.setState({ fixedClasses: "dropdown" });
    }
  };
  handleDrawerToggle = () => {
    this.setState({ mobileOpen: !this.state.mobileOpen });
  };
  getRoute = () => {
    return window.location.pathname !== "/admin/full-screen-maps";
  };
  getActiveRoute = (routes) => {
    let activeRoute = "¡Seleccione una opción del menú!";
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

  getRoutes = (routes) => {
    return routes.map((prop, key) => {
      if (prop.collapse) {
        return this.getRoutes(prop.views);
      }
      if (prop.layout === "/admin") {
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

  sidebarMinimize = () => {
    this.setState({ miniActive: !this.state.miniActive });
  };

  resizeFunction = () => {
    if (window.innerWidth >= 960) {
      this.setState({ mobileOpen: false });
    }
  };

  render() {
    const { classes, ...rest } = this.props;
    const mainPanel =
      classes.mainPanel +
      " " +
      cx({
        [classes.mainPanelSidebarMini]: this.state.miniActive,
        [classes.mainPanelWithPerfectScrollbar]:
          navigator.platform.indexOf("Win") > -1,
      });
      
    var userMenu = this.props.ingresoSistemaState.contenidoMenu;
    if (userMenu === undefined) {
      userMenu = [];
    }
    if (userMenu === null) {
      userMenu = [];
    }

    return (
      <div className={classes.wrapper}>
        <AplicarSeguridad />
        <IntegrationNotistack />
        <Sidebar
          routes={userMenu}
          logoText={"Sistema"}
          logo={this.state.logo}
          handleDrawerToggle={this.handleDrawerToggle}
          open={this.state.mobileOpen}
          color={this.state.color}
          bgColor={this.state.bgColor}
          miniActive={this.state.miniActive}
          {...rest}
        />
        <div className={mainPanel} ref={this.mainPanel}>
          <AdminNavbar
            sidebarMinimize={this.sidebarMinimize.bind(this)}
            miniActive={this.state.miniActive}
            brandText={this.getActiveRoute(userMenu)}
            handleDrawerToggle={this.handleDrawerToggle}
            history={this.props.history}
            {...rest}
          />
          {/* On the /maps/full-screen-maps route we want the map to be on full screen - this is not possible if the content and conatiner classes are present because they have some paddings which would make the map smaller */}
          {this.getRoute() ? (
            <div className={classes.content}>
              <div className={classes.container}>
                <Switch>
                  {this.getRoutes(userMenu)
                  //this.getRoutes(routes)
                  }
                  <Redirect
                    push
                    to={
                      sessionStorage[ID_PERFIL] === PERFIL_CUENTAS
                        ? "../admin/cuentas"
                        : "../admin/mesa-de-ayuda"
                    }
                  />
                </Switch>
              </div>
            </div>
          ) : (
            <div className={classes.map}>
              <Switch>
                {this.getRoutes(userMenu)}
                <Redirect
                  push
                  to={
                    sessionStorage[ID_PERFIL] === PERFIL_CUENTAS
                      ? "../admin/cuentas"
                      : "../admin/mesa-de-ayuda"
                  }
                />
              </Switch>
            </div>
          )}
          {this.getRoute() ? <Footer fluid /> : null}
        </div>
        {(sessionStorage[IS_LOGGED] === undefined ||
          sessionStorage[IS_LOGGED] !== "true") && (
          <Redirect to={"../auth/ingreso-sistema"} />
        )}
      </div>
    );
  }
}

Dashboard.propTypes = {
  classes: PropTypes.object.isRequired,
};

const mapStateToProps = (state) => {
  return {
    ingresoSistemaState: state.ingresoSistemaState,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    setMenuUsuario: (contenidoMenu, onSuccess) =>
      dispatch(setMenuUsuario(contenidoMenu, onSuccess)),
  };
};

export default withStyles(appStyle)(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(Dashboard)
);