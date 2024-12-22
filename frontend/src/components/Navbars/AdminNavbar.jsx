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
import AppBar from "@material-ui/core/AppBar";
import Hidden from "@material-ui/core/Hidden";
// @material-ui/core components
import withStyles from "@material-ui/core/styles/withStyles";
import Toolbar from "@material-ui/core/Toolbar";
// material-ui icons
import Menu from "@material-ui/icons/Menu";
import MoreVert from "@material-ui/icons/MoreVert";
import ViewList from "@material-ui/icons/ViewList";
import adminNavbarStyle from "assets/jss/material-dashboard-pro-react/components/adminNavbarStyle.jsx";
import cx from "classnames";
import Button from "components/CustomButtons/Button.jsx";
// core components
import AdminNavbarLinks from "components/Navbars/AdminNavbarLinks.jsx";
// nodejs library to set properties for components
import PropTypes from "prop-types";
import React from "react";

function AdminNavbar({ ...props }) {
  const { classes, color, rtlActive, brandText, history } = props;
  const appBarClasses = cx({
    [" " + classes[color]]: color
  });
  const sidebarMinimize =
    classes.sidebarMinimize +
    " " +
    cx({
      [classes.sidebarMinimizeRTL]: rtlActive
    });
  return (
    <AppBar className={classes.appBar + appBarClasses}>
      <Toolbar className={classes.container}>
        <Hidden smDown implementation="css">
          <div className={sidebarMinimize}>
            {props.miniActive ? (
              <Button
                justIcon
                round
                color="white"
                onClick={props.sidebarMinimize}
              >
                <ViewList className={classes.sidebarMiniIcon} />
              </Button>
            ) : (
                <Button
                  justIcon
                  round
                  color="white"
                  onClick={props.sidebarMinimize}
                >
                  <MoreVert className={classes.sidebarMiniIcon} />
                </Button>
              )}
          </div>
        </Hidden>
        <div className={classes.flex}>
          {/* Here we create navbar brand, based on route name */}
          {/*<Button href="#" className={classes.title} color="transparent">
            {brandText}
            </Button>*/}
        </div>
        <Hidden smDown implementation="css">
          <AdminNavbarLinks rtlActive={rtlActive} history={history} />
        </Hidden>
        <Hidden mdUp implementation="css">
          <Button
            className={classes.appResponsive}
            color="transparent"
            justIcon
            aria-label="open drawer"
            onClick={props.handleDrawerToggle}
          >
            <Menu />
          </Button>
        </Hidden>
      </Toolbar>
    </AppBar>
  );
}

AdminNavbar.propTypes = {
  classes: PropTypes.object.isRequired,
  color: PropTypes.oneOf(["primary", "info", "success", "warning", "danger"]),
  rtlActive: PropTypes.bool,
  brandText: PropTypes.string,
  miniActive: PropTypes.bool,
  handleDrawerToggle: PropTypes.func,
  sidebarMinimize: PropTypes.func
};

export default withStyles(adminNavbarStyle)(AdminNavbar);
