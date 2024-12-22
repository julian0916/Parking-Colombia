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
import ClickAwayListener from "@material-ui/core/ClickAwayListener";
import Grow from "@material-ui/core/Grow";
import List from "@material-ui/core/List";
import MenuItem from "@material-ui/core/MenuItem";
import Paper from "@material-ui/core/Paper";
import Popper from "@material-ui/core/Popper";

import withStyles from "@material-ui/core/styles/withStyles";
import LanguageIcon from "@material-ui/icons/Language";

import adminNavbarLinksStyle from "assets/jss/material-dashboard-pro-react/components/adminNavbarLinksStyle.jsx";
import classNames from "classnames";
import Button from "components/CustomButtons/Button.jsx";

import { listarCuentaUsuario } from "conten/cuentas/CuentaActions";
import { setMenuUsuario } from "conten/ingresoSistema/IngresoSistemaActions";
import PropTypes from "prop-types";
import React from "react";
import { connect } from "react-redux";
import { Redirect } from "react-router-dom";
import { routes } from "config/rutas/Admin";
import {
  ID_CUENTA,
  NOMBRE_PERFIL,
  ID_PERFIL,
  NOMBRE_CUENTA,
  CUENTAS_JSON,
  SESION,
} from "config/general/Configuracion";

var temporizador = null;
const MILI_SEGUNDOS_REFRESCAR = 300 * 1;
class HeaderLinks extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      openNotification: false,
      openProfile: false,
      Admin: false,
      history: props.history,
      cuentas: [],
    };
  }

  cargarCuentas = () => {
    var cuentasLocal = this.props.cuentaState.cuentasUsuario;
    if (cuentasLocal === undefined || cuentasLocal.length === 0) {
      cuentasLocal = this.props.cuentaState.cuentasDesdeAcceso;
    }
    if (cuentasLocal === undefined || cuentasLocal.length === 0) {
      cuentasLocal = JSON.parse(sessionStorage[CUENTAS_JSON]);
    }
    this.setState({ cuentas: cuentasLocal });
  };

  componentDidMount = () => {
    this.props.listarCuentaUsuario(() => {
      this.cargarCuentas();
    });
  };

  UNSAFE_componentWillReceiveProps = () => {
    this.cargarCuentas();
  };
  //componentWillReceiveProps = (nextProps) => {};

  handleClickNotification = () => {
    this.setState({ openNotification: !this.state.openNotification });
  };

  handleCloseNotification = () => {
    this.setState({ openNotification: false });
  };

  handleClickProfile = () => {
    this.setState({ openProfile: !this.state.openProfile });
  };

  handleCloseProfile = () => {
    this.setState({ openProfile: false });
  };

  handleClose = (item) => {
    //Recordar que al autenticar se traen las cuentas con cada sesion
    //pero si el perfil de alguna de esas cuentas es de administrador de cuentas
    //100 entonces al acceder a ese puede tener acceso a todas las cuentas
    //incluso inactivas y se trabajarán bajo la sesion de la cuenta que
    //con perfil 100
    if (item !== undefined) {
      sessionStorage[NOMBRE_CUENTA] = item.nombre;
      sessionStorage[ID_CUENTA] = item.cuenta;
      sessionStorage[ID_PERFIL] = item.idPerfil;
      sessionStorage[NOMBRE_PERFIL] = item.perfil;//sessionStorage[PERFIL] = item.perfil;
      if (
        item.sesion !== undefined &&
        item.sesion !== null &&
        item.sesion.trim().length > 0
      ) {
        sessionStorage[SESION] = item.sesion;
      }
      this.setState({ openProfile: false }, () => {});
    }
    this.props.setMenuUsuario(routes(), () => {});
  };

  render() {
    const { classes, rtlActive } = this.props;
    const { openNotification, openProfile } = this.state;
    const dropdownItem = classNames(
      classes.dropdownItem,
      classes.primaryHover,
      { [classes.dropdownItemRTL]: rtlActive }
    );
    const wrapper = classNames({
      [classes.wrapperRTL]: rtlActive,
    });
    const managerClasses = classNames({
      [classes.managerClasses]: true,
    });

    const { cuentas } = this.state;

    var accountsMenuItem = [];
    accountsMenuItem = cuentas.map((cuentaLocal) => {
      return (
        <MenuItem
          key={cuentaLocal.cuenta}
          onClick={() => {
            this.handleClose(cuentaLocal);
          }}
          className={dropdownItem}
        >
          {cuentaLocal.nombre} ({cuentaLocal.perfil})
        </MenuItem>
      );
    });
    var lastPath = "";
    try {
      lastPath = this.props.history.location.pathname;
    } catch (Except) {
      lastPath = "";
    }

    return (
      <>
        <div className={wrapper}>
          {cuentas.length > 0 && (
            <div className={managerClasses}>
              <Button
                color="success"
                aria-label="Notifications"
                aria-owns={openNotification ? "profile-menu-list" : null}
                aria-haspopup="true"
                onClick={() => {
                  this.handleClickProfile();
                }}
                onBlur={() => {
                  temporizador = setInterval(() => {
                    this.handleCloseProfile();
                    if (temporizador) {
                      clearInterval(temporizador);
                    }
                    temporizador = null;
                  }, MILI_SEGUNDOS_REFRESCAR);
                }}
                className={
                  rtlActive ? classes.buttonLinkRTL : classes.buttonLink
                }
                muiClasses={{
                  label: rtlActive ? classes.labelRTL : "",
                }}
                buttonRef={(node) => {
                  this.anchorProfile = node;
                }}
              >
                {sessionStorage[NOMBRE_CUENTA]}
                <LanguageIcon
                  className={
                    classes.headerLinksSvg +
                    " " +
                    (rtlActive
                      ? classes.links + " " + classes.linksRTL
                      : classes.links)
                  }
                  style={{ marginLeft: 4 }}
                />
              </Button>
              {
                <Popper
                  open={openProfile}
                  anchorEl={this.anchorProfile}
                  transition
                  disablePortal
                  placement="bottom"
                  className={classNames({
                    [classes.popperClose]: !openProfile,
                    [classes.popperResponsive]: true,
                    [classes.popperNav]: true,
                  })}
                >
                  {({ TransitionProps }) => (
                    <Grow
                      {...TransitionProps}
                      id="profile-menu-list"
                      style={{ transformOrigin: "0 0 0" }}
                    >
                      <Paper
                        className={classes.dropdown}
                        style={{ maxHeight: 300, overflow: "auto" }}
                      >
                        <ClickAwayListener
                          onClickAway={() => {
                            this.handleCloseProfile;
                          }}
                        >
                          <List
                            role="menu"
                            onClick={() => {
                              this.setState({ Admin: true }, () => {
                                this.setState({ Admin: false });
                              });
                            }}
                          >
                            {accountsMenuItem}
                          </List>
                        </ClickAwayListener>
                      </Paper>
                    </Grow>
                  )}
                </Popper>
              }
            </div>
          )}
        </div>
        {this.state.Admin && (
          <Redirect to={"/admin/admin-change?lastURL=" + lastPath} />
        )}
      </>
    );
  }
}

HeaderLinks.propTypes = {
  classes: PropTypes.object.isRequired,
  rtlActive: PropTypes.bool,
};

const mapStateToProps = (state) => {
  return {
    cuentaState: state.cuentaState,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    setMenuUsuario: (contenidoMenu, onSuccess) =>
      dispatch(setMenuUsuario(contenidoMenu, onSuccess)),
    listarCuentaUsuario: (onSUccess) =>
      dispatch(listarCuentaUsuario(onSUccess)),
  };
};

export default withStyles(adminNavbarLinksStyle)(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(HeaderLinks)
);
