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
import React from "react";
import cx from "classnames";
import PropTypes from "prop-types";
import { Switch, Route, Redirect } from "react-router-dom";
// creates a beautiful scrollbar
import PerfectScrollbar from "perfect-scrollbar";
import "perfect-scrollbar/css/perfect-scrollbar.css";

// @material-ui/core components
import withStyles from "@material-ui/core/styles/withStyles";

// core components
import AdminNavbar from "components/Navbars/AdminNavbar.jsx";
import Footer from "components/Footer/Footer.jsx";
import Sidebar from "components/Sidebar/Sidebar.jsx";
import FixedPlugin from "components/FixedPlugin/FixedPlugin.jsx";

import routes from "config/rutas/Pages";

import rtlStyle from "assets/jss/material-dashboard-pro-react/layouts/rtlStyle.jsx";

var ps;

class RTL extends React.Component {
  state = {
    mobileOpen: false,
    miniActive: false,
    //image: require("assets/img/sidebar-2.jpg"),
    color: "blue",
    bgColor: "black",
    hasImage: true,
    fixedClasses: "dropdown",
    logo: require("assets/img/logo-white.png")
  };
  mainPanel = React.createRef();
  handleImageClick = image => {
    //this.setState({ image: image });
  };
  handleColorClick = color => {
    this.setState({ color: color });
  };
  handleBgColorClick = bgColor => {
    switch (bgColor) {
      case "white":
        this.setState({ logo: require("assets/img/logo.svg") });
        break;
      default:
        this.setState({ logo: require("assets/img/logo-white.png") });
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
  componentDidMount() {
    if (navigator.platform.indexOf("Win") > -1) {
      ps = new PerfectScrollbar(this.mainPanel.current, {
        suppressScrollX: true,
        suppressScrollY: false
      });
    }
  }
  componentWillUnmount() {
    if (navigator.platform.indexOf("Win") > -1) {
      ps.destroy();
    }
  }
  componentDidUpdate(e) {
    if (e.history.location.pathname !== e.location.pathname) {
      this.mainPanel.current.scrollTop = 0;
      if (this.state.mobileOpen) {
        this.setState({ mobileOpen: false });
      }
    }
    if (navigator.platform.indexOf("Win") > -1) {
      ps = new PerfectScrollbar(this.mainPanel.current, {
        suppressScrollX: true,
        suppressScrollY: false
      });
    }
  }
  sidebarMinimize = () => {
    this.setState({ miniActive: !this.state.miniActive });
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
          return routes[i].rtlName;
        }
      }
    }
    return activeRoute;
  };
  getRoutes = routes => {
    return routes.map((prop, key) => {
      if (prop.collapse) {
        return this.getRoutes(prop.views);
      }
      if (prop.layout === "/rtl") {
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
  render() {
    const { classes, ...rest } = this.props;
    const mainPanel =
      classes.mainPanel +
      " " +
      cx({
        [classes.mainPanelSidebarMini]: this.state.miniActive
      });
    return (
      <div className={classes.wrapper}>
        <Sidebar
          routes={routes}
          logoText={"توقيت الإبداعية"}
          logo={this.state.logo}
          image={this.state.image}
          handleDrawerToggle={this.handleDrawerToggle}
          open={this.state.mobileOpen}
          color={this.state.color}
          bgColor={this.state.bgColor}
          miniActive={this.state.miniActive}
          rtlActive
          {...rest}
        />
        <div className={mainPanel} ref={this.mainPanel}>
          <AdminNavbar
            rtlActive
            sidebarMinimize={this.sidebarMinimize.bind(this)}
            miniActive={this.state.miniActive}
            handleDrawerToggle={this.handleDrawerToggle}
            brandText={this.getActiveRoute(routes)}
            {...rest}
          />
          <div className={classes.content}>
            <div className={classes.container}>
              <Switch>
                {this.getRoutes(routes)}
                <Redirect from="/rtl" to="/rtl/rtl-support-page" />
              </Switch>
            </div>
          </div>
          <Footer fluid rtlActive />
          <FixedPlugin
            handleImageClick={this.handleImageClick}
            handleColorClick={this.handleColorClick}
            handleBgColorClick={this.handleBgColorClick}
            handleHasImage={this.handleHasImage}
            color={this.state["color"]}
            bgColor={this.state["bgColor"]}
            bgImage={this.state["image"]}
            handleFixedClick={this.handleFixedClick}
            fixedClasses={this.state.fixedClasses}
            sidebarMinimize={this.sidebarMinimize.bind(this)}
            miniActive={this.state.miniActive}
            rtlActive={true}
          />
        </div>
      </div>
    );
  }
}

RTL.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withStyles(rtlStyle)(RTL);
