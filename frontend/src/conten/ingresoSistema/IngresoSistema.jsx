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
import Checkbox from "@material-ui/core/Checkbox";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Icon from "@material-ui/core/Icon";
import InputAdornment from "@material-ui/core/InputAdornment";
// @material-ui/core components
import withStyles from "@material-ui/core/styles/withStyles";
import Email from "@material-ui/icons/Email";
import logo from "assets/img/logo-white.png";
import loginPageStyle from "assets/jss/material-dashboard-pro-react/views/loginPageStyle.jsx";
import AlertInfo from "components/AlertInfo/AlertInfo.jsx";
import Card from "components/Card/Card.jsx";
import CardBody from "components/Card/CardBody.jsx";
import CardHeader from "components/Card/CardHeader.jsx";
import Button from "components/CustomButtons/Button.jsx";
import CustomInput from "components/CustomInput/CustomInput.jsx";
// import LockOutline from "@material-ui/icons/LockOutline";
// core components
import GridContainer from "components/Grid/GridContainer.jsx";
import GridItem from "components/Grid/GridItem.jsx";
import {
  ingresoInicioSolicitud,
  ingresoCompletarSolicitud,
} from "conten/ingresoSistema/IngresoSistemaActions";
// module components
import WaitDialog from "conten/components/WaitDialog.jsx";
import PropTypes from "prop-types";
import React from "react";
import { connect } from "react-redux";
import { Redirect } from "react-router-dom";
import {
  generarValoresDiff,
  encriptarContenido,
} from "conten/ingresoSistema/Autenticar";
import { OK, IS_LOGGED } from "config/general/Configuracion";
import { addMessage } from "conten/mensajes/MessagesActions";

class IngresoSistema extends React.Component {
  constructor(props) {
    super(props);
    // we use this to make the card to appear after the page has been rendered
    this.state = {
      cardAnimaton: "cardHidden",
      toAdmin: false,
      openAlertInfo: false,
      textAlertInfo: "",
      usuario: "",
      clave: "",
    };
  }

  componentDidMount() {
    sessionStorage[IS_LOGGED] = "false";
    // we add a hidden class to the card and after 700 ms we delete it and the transition appears
    this.timeOutFunction = setTimeout(
      function() {
        this.setState({ cardAnimaton: "" });
      }.bind(this),
      700
    );
  }

  componentWillUnmount() {
    clearTimeout(this.timeOutFunction);
    this.timeOutFunction = null;
  }

  handleInput = (event) => {
    switch (event.target.name) {
      case "userName":
        this.setState({ usuario: event.target.value });
        break;
      case "password":
        this.setState({ clave: event.target.value });
        break;
    }
  };

  login = () => {
    if (this.state.usuario === "") {
      this.mostrarMensaje("Digite los datos del usuario");
      return;
    }

    if (this.state.clave === "") {
      this.mostrarMensaje("Digite su contraseña");
      return;
    }
    this.iniciarAutenticacion();
  };

  iniciarAutenticacion = () => {
    //this.props.ingresoCompletarSolicitud('1','2','3',(respuesta, contenidoRespuesta)=>{});
    this.props.ingresoInicioSolicitud((respuesta, contenidoRespuesta) => {
      if (respuesta === "OK") {
        this.completarAutenticacion(contenidoRespuesta);
      } else {
        this.mostrarMensaje(contenidoRespuesta);
      }
    });
  };

  completarAutenticacion = (valoresAutenticacion) => {
    var p = parseInt(valoresAutenticacion.p, 10);
    var g = parseInt(valoresAutenticacion.g, 10);
    var b = parseInt(valoresAutenticacion.b, 10);
    var contenido = generarValoresDiff(p, g, b);

    var autenticacionContenido = {
      usuario: this.state.usuario,
      clave: this.state.clave,
    };
    var webToken = encriptarContenido(
      JSON.stringify(autenticacionContenido),
      contenido.hash
    );
    this.completarAutenticacionServidor(
      valoresAutenticacion.idSolicitud,
      contenido.A,
      webToken
    );
  };

  completarAutenticacionServidor = (idSolicitud, A, tokenWeb) => {
    this.props.ingresoCompletarSolicitud(
      idSolicitud,
      A,
      tokenWeb,
      (resultado, contenido) => {
        if (resultado === OK) {
          this.mostrarMensaje("Bienvenido "+this.state.usuario,"success")
          this.setState({ toAdmin: true });
        } else {
          this.mostrarMensaje(contenido);
          return;
        }
      }
    );
  };

  mostrarMensaje = (text,contenidoTipo="error") => {
    this.props.addMessage({ variant: contenidoTipo, message: text + "" });
    //this.setState({
    //  openAlertInfo: true,
    //  textAlertInfo: text,
    //});
  };

  hideaAlertInfo = () => {
    this.setState({
      openAlertInfo: false,
    });
  };

  handleToggle = () => {
    if (this.state.rememberPassword === 0) {
      this.setState({ rememberPassword: 1 });
    } else {
      this.setState({ rememberPassword: 0 });
    }
  };

  emailToRemember = () => {
    if (this.state.userLogin.userName === "") {
      this.mostrarMensaje("Digite los datos del usuario");
      return;
    }
    var email = this.state.userLogin.userName;
    this.props.sendEmailRememberAccess(email, () => {
      var userLogin = this.state.userLogin;
      userLogin.userName = "";
      this.setState({ rememberPassword: 0, userLogin: userLogin });
    });
  };

  render() {
    const { classes } = this.props;

    const { isActivityIndicatorShown } =
      this.props.ingresoSistemaState || false;
    if (this.state.toAdmin === true) {
      return <Redirect to={"/admin"} />;
    }

    return (
      <div className={classes.container}>
        <GridContainer justifyContent="center">
          <AlertInfo
            text={this.state.textAlertInfo}
            open={this.state.openAlertInfo}
            onDoneClick={this.hideaAlertInfo}
          />
          <GridItem xs={12} sm={6} md={4}>
            {isActivityIndicatorShown && (
              <WaitDialog text={"Validando datos..."} />
            )}
            <form>
              <Card login className={classes[this.state.cardAnimaton]}>
                <CardHeader
                  className={`${classes.cardHeader} ${classes.textCenter}`}
                  color="success"
                >
                  <h4 className={classes.cardTitle}>
                    <img src={logo} alt="..." width="140" />
                  </h4>
                </CardHeader>
                <CardBody>
                  <CustomInput
                    labelText="Correo..."
                    id="email"
                    success
                    formControlProps={{
                      fullWidth: true,
                    }}
                    inputProps={{
                      name: "userName",
                      id: "userName",
                      value: this.state.usuario || "",
                      onChange: (event) => this.handleInput(event),
                      endAdornment: (
                        <InputAdornment position="end">
                          <Email className={classes.inputAdornmentIcon} />
                        </InputAdornment>
                      ),
                    }}
                  />
                  {this.state.rememberPassword !== 1 && (
                    <CustomInput
                      labelText="Clave..."
                      id="password"
                      success
                      formControlProps={{
                        fullWidth: true,
                      }}
                      inputProps={{
                        name: "password",
                        id: "password",
                        type: "password",
                        value: this.state.clave || "",
                        onChange: (event) => this.handleInput(event),
                        onKeyDown: (event) => {
                          if (event.keyCode === 13) {
                            this.login();
                          }
                        },
                        endAdornment: (
                          <InputAdornment position="end">
                            <Icon className={classes.inputAdornmentIcon}>
                              lock_outline
                            </Icon>
                          </InputAdornment>
                        ),
                        autoComplete: "off",
                      }}
                    />
                  )}
                  {/*
                  <FormControlLabel
                    control={
                      <Checkbox
                        checked={this.state.rememberPassword === 1}
                        onChange={() => this.handleToggle()}
                        color="info"
                      />
                    }
                    label={
                      this.state.rememberPassword !== 1
                        ? "¿Olvidó su clave?"
                        : "Recordar al correo"
                    }
                  />*/}

                  {this.state.rememberPassword !== 1 && (
                    <Button
                      color="primary"
                      //simple
                      size="lg"
                      block
                      onClick={() => {
                        this.login();
                      }}
                    >
                      Ingresar
                    </Button>
                  )}
                  {this.state.rememberPassword === 1 && (
                    <Button
                      color="success"
                      //simple
                      size="lg"
                      block
                      onClick={() => {
                        this.emailToRemember();
                      }}
                    >
                      Enviar
                    </Button>
                  )}
                </CardBody>
              </Card>
            </form>
          </GridItem>
        </GridContainer>
      </div>
    );
  }
}

IngresoSistema.propTypes = {
  classes: PropTypes.object.isRequired,
};

const mapStateToProps = (state) => {
  return {
    ingresoSistemaState: state.ingresoSistemaState,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    addMessage: (content) => dispatch(addMessage(content)),
    ingresoInicioSolicitud: (onSuccess) =>
      dispatch(ingresoInicioSolicitud(onSuccess)),
    ingresoCompletarSolicitud: (idSolicitud, A, tokenWeb, onSuccess) =>
      dispatch(ingresoCompletarSolicitud(idSolicitud, A, tokenWeb, onSuccess)),
  };
};

export default withStyles(loginPageStyle)(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(IngresoSistema)
);
