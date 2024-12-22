import Checkbox from "@material-ui/core/Checkbox";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import InputAdornment from "@material-ui/core/InputAdornment";
import MenuItem from "@material-ui/core/MenuItem";

import withStyles from "@material-ui/core/styles/withStyles";
import Check from "@material-ui/icons/Check";
import extendedTablesStyle from "assets/jss/material-dashboard-pro-react/views/extendedTablesStyle.jsx";
import SaveIcon from "@material-ui/icons/Save";
import Card from "components/Card/Card.jsx";
import CardBody from "components/Card/CardBody.jsx";
import CardHeader from "components/Card/CardHeader.jsx";
import CardIcon from "components/Card/CardIcon.jsx";

import Button from "components/CustomButtons/Button.jsx";
import CustomInput from "components/CustomInput/CustomInput.jsx";
import CustomSelect from "components/CustomSelect/CustomSelect.jsx";
import GridContainer from "components/Grid/GridContainer.jsx";
import GridItem from "components/Grid/GridItem.jsx";

import WaitDialog from "conten/components/WaitDialog.jsx";
import React from "react";
import { connect } from "react-redux";
import { OK } from "config/general/Configuracion";
import PeopleIcon from "@material-ui/icons/People";
import ContactMailIcon from "@material-ui/icons/ContactMail";
import ContactPhoneIcon from "@material-ui/icons/ContactPhone";
import PersonIcon from "@material-ui/icons/Person";
import EditLocationIcon from "@material-ui/icons/EditLocation";

import CustomTextarea from "components/CustomTextarea/CustomTextarea";
import RecentActorsIcon from "@material-ui/icons/RecentActors";

import VisibilityIcon from "@material-ui/icons/Visibility";
import VisibilityOffIcon from "@material-ui/icons/VisibilityOff";

import TituloPagina from "conten/components/TituloPagina";
import TituloPagina2 from "conten/components/TituloPagina2";
import Accordion from "components/Accordion/Accordion";

import {
  getHash,
  dencriptarContenido,
  encriptarContenido,
} from "conten/usuarios/UsuarioUtilidades";

import {
  listarDocumentos,
  listarPerfiles,
  guardarUsuario,
} from "conten/usuarios/UsuarioActions";

import {
  formatDate,
  formatDateTime,
  formatNumberDate,
  formatoDinero,
} from "config/funciones/Comunes";

import { addMessage } from "conten/mensajes/MessagesActions";
import AplicarSeguridad from "conten/components/AplicarSeguridad";
import { PDFViewer } from "@react-pdf/renderer";

import jsPDF from "jspdf";
import html2canvas from "html2canvas";
import GetAppIcon from "@material-ui/icons/GetApp";
import Printer, { print } from "react-pdf-print";

class UsuarioAdministrar extends React.Component {
  constructor(props) {
    super(props);
    const fechaHoraActual = formatDateTime(
      new Date(Date.now()),
      "yyyy-mm-ddTHH:MM"
    );
    this.state = {
      textoAlerta: "Un momento por favor ...",
      claveSimple: "",
      mostrarClave: false,
      fechaHoraActual: fechaHoraActual.replace("T", "_").replace(":", "-"),
      usuario: {
        id: 0,
        perfil: "",
        tipoIdentificacion: "",
        correo: "",
        identificacion: "",
        nombre1: "",
        nombre2: "",
        apellido1: "",
        apellido2: "",
        claveHash: "",
        claveEncriptada: "",
        celular: "",
        fijo: "",
        direccion: "",
        masInformacion: "",
        activo: true,
      },
    };
  }

  componentDidMount = () => {
    this.props.listarDocumentos(() => {});
    this.props.listarPerfiles(() => {});

    var urlParams = new URLSearchParams(this.props.location.search);
    if (urlParams.has("editar")) {
      if (urlParams.get("editar") === "true") {
        if (this.props.location.state.usuario) {
          var usuario = this.props.location.state.usuario;
          var valor = dencriptarContenido(
            usuario.claveEncriptada,
            usuario.claveHash
          );
          this.setState({
            claveSimple: valor,
          });
          this.setState({ usuario: usuario });
        }
      }
    }
  };

  cambioContenidoUsuario = (valor, campo) => {
    var usuario = this.state.usuario;
    usuario[campo] = valor;
    this.setState({ usuario: usuario });
    return true;
  };

  cambioActivo = () => {
    this.setState((prevState) => ({
      usuario: { ...prevState.usuario, activo: !prevState.usuario.activo },
    }));
  };

  validarContenidos = () => {
    this.procesarServidorClave();
  };

  resultadoGrabar = (resultado, data) => {
    if (resultado === OK) {
      this.props.history.goBack();
    }
  };

  guardarCambios = () => {
    try {
      this.validarContenidos();
      this.props.guardarUsuario(this.state.usuario, this.resultadoGrabar);
    } catch (msg) {
      this.abrirAlerta(msg);
    }
  };

  getDocumentos = (documentos) => {
    var content = [];
    try {
      documentos.forEach((documento) => {
        content.push(
          <MenuItem key={"documento" + documento.id} value={documento.id}>
            {documento.nombreCorto + " (" + documento.nombreLargo + ")"}
          </MenuItem>
        );
      });
    } catch (ex) {}
    return content;
  };

  getPerfiles = (perfiles) => {
    var content = [];
    try {
      perfiles.forEach((perfil) => {
        content.push(
          <MenuItem key={"perfil" + perfil.id} value={perfil.id}>
            {perfil.nombre}
          </MenuItem>
        );
      });
    } catch (ex) {}
    return content;
  };

  abrirAlerta = (text, contenidoTipo = "error") => {
    this.props.addMessage({ variant: contenidoTipo, message: text + "" });
  };

  validarClave = () => {
    var numeros = /.*[0-9]+.*[0-9]+/; //2 números
    var minusculas = /.*[a-z]+.*[a-z]+/; //2 letras minusculas
    var mayusculas = /.*[A-Z]+.*[A-Z]+/; //2 letras mayusculas
    var caracter = /\W+/; //1caracter especial
    var OK = numeros.exec(this.state.claveSimple);
    if (!OK) {
      throw "Debe ingresar una clave con mínimo 2 números";
    }
    OK = minusculas.exec(this.state.claveSimple);
    if (!OK) {
      throw "Debe ingresar una clave con mínimo 2 letras minúsculas";
    }
    OK = mayusculas.exec(this.state.claveSimple);
    if (!OK) {
      throw "Debe ingresar una clave con mínimo 2 letras mayúsculas";
    }
    OK = caracter.exec(this.state.claveSimple);
    if (!OK) {
      throw "Debe ingresar una clave con mínimo 1 caracter especial";
    }
  };

  procesarServidorClave = () => {
    try {
      this.validarClave();
      var local = this.state.claveSimple;
      var hash = getHash(local);
      var encriptada = encriptarContenido(local, hash);
      this.cambioContenidoUsuario(hash, "claveHash");
      this.cambioContenidoUsuario(encriptada, "claveEncriptada");
    } catch (exp) {
      throw exp;
    }
  };

  procesarDatosQR = () => {
    var acceso = {
      i: this.state.usuario.id,
      u: this.state.usuario.identificacion,
      c: this.state.claveSimple,
      confirmado: "OK",
    };
    var cont = JSON.stringify(acceso);
    var hash = getHash(Math.random() + "" + cont);
    var encriptada = encriptarContenido(cont, hash);
    const separador = "_@@";
    var result = separador + hash + encriptada + separador;
    return result;
  };

  printDocument(nombre) {
    const input = document.getElementById("divToPrint");
    html2canvas(input).then((canvas) => {
      const imgData = canvas.toDataURL("image/png");
      const pdf = new jsPDF();
      pdf.addImage(imgData, "JPEG", 0, 0);
      pdf.save(nombre + "_" + this.state.fechaHoraActual + ".pdf");
    });
  }

  render() {
    const { classes } = this.props;
    const styleCheck = { margin: "15px 0px 0px -10px" };
    const {
      documentos,
      perfiles,
      isActivityIndicatorShown,
    } = this.props.usuarioState;
    var QRCode = require("qrcode.react");
    const ids = ["1"];
    return (
      <GridItem xs={12}>
        {isActivityIndicatorShown && (
          <WaitDialog text={this.state.textoAlerta} />
        )}
        <AplicarSeguridad />
        <Card>
          <CardHeader icon>
            <CardIcon
              style={{
                cursor: "pointer",
              }}
              color="primary"
              onClick={() => {
                this.props.history.goBack();
              }}
            >
              <b>{"Ver listado"}</b>
              <PeopleIcon />
            </CardIcon>
            <TituloPagina2
              texto="Datos de usuario"
              color="primary"
              classes={classes}
            />
          </CardHeader>

          <CardBody>
            <Accordion
              active={0}
              collapses={[
                {
                  title: "Datos de usuario",
                  content: (
                    <GridContainer>
                      <GridItem xs={12} sm={6} md={6}>
                        <CustomInput
                          labelText="* Primer nombre"
                          id="etiquetaNombre1"
                          success
                          formControlProps={{
                            fullWidth: true,
                          }}
                          inputProps={{
                            name: "nombre1",
                            id: "nombre1",
                            value: this.state.usuario.nombre1,
                            onChange: (event) =>
                              this.cambioContenidoUsuario(
                                event.target.value,
                                "nombre1"
                              ),
                            endAdornment: (
                              <InputAdornment position="start">
                                <PersonIcon />
                              </InputAdornment>
                            ),
                          }}
                        />
                      </GridItem>

                      <GridItem xs={12} sm={6} md={6}>
                        <CustomInput
                          labelText="Segundo nombre"
                          id="etiquetaNombre2"
                          success
                          formControlProps={{
                            fullWidth: true,
                          }}
                          inputProps={{
                            name: "nombre2",
                            id: "nombre2",
                            value: this.state.usuario.nombre2,
                            onChange: (event) =>
                              this.cambioContenidoUsuario(
                                event.target.value,
                                "nombre2"
                              ),
                            endAdornment: (
                              <InputAdornment position="start">
                                <PersonIcon />
                              </InputAdornment>
                            ),
                          }}
                        />
                      </GridItem>

                      <GridItem xs={12} sm={6} md={6}>
                        <CustomInput
                          labelText="* Primer apellido"
                          id="etiquetaApellido1"
                          success
                          formControlProps={{
                            fullWidth: true,
                          }}
                          inputProps={{
                            name: "apellido1",
                            id: "apellido1",
                            value: this.state.usuario.apellido1,
                            onChange: (event) =>
                              this.cambioContenidoUsuario(
                                event.target.value,
                                "apellido1"
                              ),
                            endAdornment: (
                              <InputAdornment position="start">
                                <PersonIcon />
                              </InputAdornment>
                            ),
                          }}
                        />
                      </GridItem>

                      <GridItem xs={12} sm={6} md={6}>
                        <CustomInput
                          labelText="Segundo apellido"
                          id="etiquetaApellido2"
                          success
                          formControlProps={{
                            fullWidth: true,
                          }}
                          inputProps={{
                            name: "apellido2",
                            id: "apellido2",
                            value: this.state.usuario.apellido2,
                            onChange: (event) =>
                              this.cambioContenidoUsuario(
                                event.target.value,
                                "apellido2"
                              ),
                            endAdornment: (
                              <InputAdornment position="start">
                                <PersonIcon />
                              </InputAdornment>
                            ),
                          }}
                        />
                      </GridItem>

                      <GridItem xs={12} sm={6} md={6}>
                        <CustomSelect
                          key="tipoIdentificacion"
                          success
                          labelText="* Tipo identificacion"
                          id="tipoIdentificacion"
                          formControlProps={{
                            fullWidth: true,
                          }}
                          inputProps={{
                            name: "tipoIdentificacion",
                            id: "tipoIdentificacion",
                            value: this.state.usuario.tipoIdentificacion,
                            onChange: (event) =>
                              this.cambioContenidoUsuario(
                                event.target.value,
                                "tipoIdentificacion"
                              ),
                          }}
                          selectContent={this.getDocumentos(documentos)}
                        />
                      </GridItem>

                      <GridItem xs={12} sm={6} md={6}>
                        <CustomInput
                          labelText="* Número identificacion"
                          id="etiquetNumeroIdentificacion"
                          success
                          formControlProps={{
                            fullWidth: true,
                          }}
                          inputProps={{
                            name: "identificacion",
                            id: "identificacion",
                            value: this.state.usuario.identificacion,
                            onChange: (event) =>
                              this.cambioContenidoUsuario(
                                event.target.value,
                                "identificacion"
                              ),
                            endAdornment: (
                              <InputAdornment position="start">
                                <RecentActorsIcon />
                              </InputAdornment>
                            ),
                          }}
                        />
                      </GridItem>

                      <GridItem xs={12} sm={6} md={6}>
                        <CustomSelect
                          key="idPerfil"
                          success
                          labelText="* Perfil"
                          id="etiquetaPerfil"
                          formControlProps={{
                            fullWidth: true,
                          }}
                          inputProps={{
                            name: "perfil",
                            id: "perfil",
                            value: this.state.usuario.perfil,
                            onChange: (event) =>
                              this.cambioContenidoUsuario(
                                event.target.value,
                                "perfil"
                              ),
                          }}
                          selectContent={this.getPerfiles(perfiles)}
                        />
                      </GridItem>
                      <GridItem xs={12} sm={6} md={6}>
                        <CustomInput
                          labelText="* Correo"
                          id="etiquetCorreo"
                          success
                          formControlProps={{
                            fullWidth: true,
                          }}
                          inputProps={{
                            name: "correo",
                            id: "correo",
                            value: this.state.usuario.correo,
                            onChange: (event) =>
                              this.cambioContenidoUsuario(
                                event.target.value,
                                "correo"
                              ),
                            endAdornment: (
                              <InputAdornment position="start">
                                <ContactMailIcon />
                              </InputAdornment>
                            ),
                          }}
                        />
                      </GridItem>
                      <GridItem xs={12} sm={6} md={6}>
                        <form>
                          <CustomInput
                            name="claveSimpleCustom"
                            type={this.state.mostrarClave ? "text" : "password"}
                            labelText="* Clave"
                            id="claveSimpleCustom"
                            success
                            formControlProps={{
                              fullWidth: true,
                            }}
                            inputProps={{
                              name: "claveSimple",
                              id: "claveSimple",
                              value: this.state.claveSimple,
                              onChange: (event) => {
                                this.setState({
                                  claveSimple: event.target.value,
                                });
                              },
                              endAdornment: (
                                <InputAdornment
                                  style={{
                                    cursor: "pointer",
                                  }}
                                  position="start"
                                  onClick={(even) => {
                                    this.setState({
                                      mostrarClave: !this.state.mostrarClave,
                                    });
                                  }}
                                >
                                  {this.state.mostrarClave && (
                                    <VisibilityIcon />
                                  )}
                                  {!this.state.mostrarClave && (
                                    <VisibilityOffIcon />
                                  )}
                                </InputAdornment>
                              ),
                            }}
                          />
                        </form>
                      </GridItem>

                      <GridItem xs={12} sm={6} md={6}>
                        <CustomInput
                          labelText="Celular"
                          id="etiquetCelular"
                          success
                          formControlProps={{
                            fullWidth: true,
                          }}
                          inputProps={{
                            name: "celular",
                            id: "celular",
                            value: this.state.usuario.celular,
                            onChange: (event) =>
                              this.cambioContenidoUsuario(
                                event.target.value,
                                "celular"
                              ),
                            endAdornment: (
                              <InputAdornment position="start">
                                <ContactPhoneIcon />
                              </InputAdornment>
                            ),
                          }}
                        />
                      </GridItem>

                      <GridItem xs={12} sm={6} md={6}>
                        <CustomInput
                          labelText="Fijo"
                          id="etiquetaFijo"
                          success
                          formControlProps={{
                            fullWidth: true,
                          }}
                          inputProps={{
                            name: "fijo",
                            id: "fijo",
                            value: this.state.usuario.fijo,
                            onChange: (event) =>
                              this.cambioContenidoUsuario(
                                event.target.value,
                                "fijo"
                              ),
                            endAdornment: (
                              <InputAdornment position="start">
                                <ContactPhoneIcon />
                              </InputAdornment>
                            ),
                          }}
                        />
                      </GridItem>

                      <GridItem xs={12} sm={6} md={6}>
                        <CustomInput
                          labelText="Dirección"
                          id="etiquetaDireccion"
                          success
                          formControlProps={{
                            fullWidth: true,
                          }}
                          inputProps={{
                            name: "direccion",
                            id: "direccion",
                            value: this.state.usuario.direccion,
                            onChange: (event) =>
                              this.cambioContenidoUsuario(
                                event.target.value,
                                "direccion"
                              ),
                            endAdornment: (
                              <InputAdornment position="start">
                                <EditLocationIcon />
                              </InputAdornment>
                            ),
                          }}
                        />
                      </GridItem>

                      <GridItem xs={12} sm={6} md={6}>
                        <CustomTextarea
                          labelText="Información"
                          id="masInformacion"
                          success
                          formControlProps={{
                            fullWidth: true,
                          }}
                          inputProps={{
                            value: this.state.usuario.masInformacion,
                            onChange: (event) =>
                              this.cambioContenidoUsuario(
                                event.target.value,
                                "masInformacion"
                              ),
                          }}
                        />
                      </GridItem>

                      <GridItem xs={12} sm={6} md={6}>
                        <FormControlLabel
                          style={styleCheck}
                          control={
                            <Checkbox
                              checkedIcon={
                                <Check className={classes.checkedIcon} />
                              }
                              icon={<Check className={classes.uncheckedIcon} />}
                              classes={{
                                checked: classes.checked,
                              }}
                              checked={this.state.usuario.activo}
                              onChange={this.cambioActivo}
                              disableRipple
                            />
                          }
                          label="Activo"
                        ></FormControlLabel>
                      </GridItem>
                      <GridItem xs={12} sm={6} md={6}>
                        <Button
                          type="button"
                          onClick={(e) => {
                            this.guardarCambios();
                            e.preventDefault();
                          }}
                          color="success"
                        >
                          <SaveIcon />
                          Guardar
                        </Button>
                      </GridItem>
                    </GridContainer>
                  ),
                },
                {
                  title: "Codigo QR para acceso",
                  content: (
                    <GridContainer>
                      <GridItem xs={4} sm={4} md={4} lg={4}></GridItem>
                      <GridItem xs={4} sm={4} md={4} lg={4}>
                        <center>
                          <div id="divToPrint">
                            <br />
                            <QRCode value={this.procesarDatosQR()} size={128} />
                            <h5>
                              {this.state.usuario.nombre1 +
                                " " +
                                this.state.usuario.nombre2 +
                                " "}
                              <br />
                              {this.state.usuario.apellido1 +
                                " " +
                                this.state.usuario.apellido2}
                            </h5>
                          </div>
                          <Button
                            type="button"
                            onClick={(e) => {
                              this.printDocument(
                                this.state.usuario.nombre1 +
                                  "_" +
                                  this.state.usuario.nombre2 +
                                  "_" +
                                  this.state.usuario.apellido1 +
                                  "_" +
                                  this.state.usuario.apellido2
                              );
                              e.preventDefault();
                            }}
                            color="success"
                          >
                            <GetAppIcon />
                            Descargar PDF
                          </Button>
                        </center>
                      </GridItem>
                      <GridItem xs={4} sm={4} md={4} lg={4}></GridItem>
                    </GridContainer>
                  ),
                },
              ]}
            />
          </CardBody>
        </Card>
      </GridItem>
    );
  }
}

const mapStateToProps = (state) => {
  return {
    usuarioState: state.usuarioState,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    listarDocumentos: (onSuccess) => dispatch(listarDocumentos(onSuccess)),
    listarPerfiles: (onSuccess) => dispatch(listarPerfiles(onSuccess)),
    guardarUsuario: (usuario, onSuccess) =>
      dispatch(guardarUsuario(usuario, onSuccess)),
    addMessage: (content) => dispatch(addMessage(content)),
  };
};

export default withStyles(extendedTablesStyle)(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(UsuarioAdministrar)
);
