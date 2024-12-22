import Checkbox from "@material-ui/core/Checkbox";
import FormControlLabel from "@material-ui/core/FormControlLabel";
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
import GridContainer from "components/Grid/GridContainer.jsx";
import GridItem from "components/Grid/GridItem.jsx";

import WaitDialog from "conten/components/WaitDialog.jsx";
import React from "react";
import { connect } from "react-redux";
import {
  OK,
  TODOS_LOS_REGISTROS,
  PERFIL_PROMOTOR,
} from "config/general/Configuracion";

import { guardarPromotorZona } from "conten/promotorZonas/PromotorZonaActions";

import { addMessage } from "conten/mensajes/MessagesActions";

import { listarUsuario } from "conten/usuarios/UsuarioActions";
import { listarZona } from "conten/zonas/ZonaActions";

import CustomSelect from "components/CustomSelect/CustomSelect.jsx";

import DirectionsRunIcon from "@material-ui/icons/DirectionsRun";
import CustomTextarea from "components/CustomTextarea/CustomTextarea";
import TituloPagina2 from "conten/components/TituloPagina2";
import AplicarSeguridad from "conten/components/AplicarSeguridad";


class PromotorZonaAdministrar extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      textAlertInfo: "Estamos procesando su solicitud un momento por favor :)",
      paginacionPromotor: {
        limite: TODOS_LOS_REGISTROS,
        actual: 1,
        filtro: PERFIL_PROMOTOR,
        orden: "nombre1",
        sentido: "ASC",
      },
      paginacionZona: {
        limite: TODOS_LOS_REGISTROS,
        actual: 1,
        filtro: "activ",
        orden: "nombre",
        sentido: "ASC",
      },
      promotorZona: {
        id: 0,
        promotor: "",
        zona: "",
        lunes: true,
        martes: true,
        miercoles: true,
        jueves: true,
        viernes: true,
        sabado: true,
        domingo: true,
        detalle: "",
        activo: true,
      },
    };
  }

  componentDidMount = () => {
    this.traerDatosServidor();
    var urlParams = new URLSearchParams(this.props.location.search);
    if (urlParams.has("editar")) {
      if (urlParams.get("editar") === "true") {
        if (this.props.location.state.promotorZona) {
          this.setState({
            promotorZona: this.props.location.state.promotorZona,
          });
        }
      }
    }
  };

  traerDatosServidor = () => {
    this.props.listarUsuario(this.state.paginacionPromotor);
    this.props.listarZona(this.state.paginacionZona);
  };

  soloNumeros = (contenido) => {
    var numeros = /\d*/; //2 números
    var resultado = contenido.match(numeros)[0];
    return resultado === "" ? 0 : parseInt(resultado, 10);
  };

  soloDecimales = (contenido) => {
    var decimales = /-{0,1}\d+\.{0,1}\d*|-{0,1}\d*\.{0,1}\d*/;
    var resultado = contenido.match(decimales);
    return resultado === "" || resultado === NaN || resultado === null
      ? ""
      : resultado[0];
  };

  cambioContenidoPromotorZona = (valor, campo) => {
    var promotorZona = this.state.promotorZona;
    promotorZona[campo] = valor;
    this.setState({ promotorZona: promotorZona });
    return true;
  };

  cambioActivo = () => {
    this.setState((prevState) => ({
      promotorZona: {
        ...prevState.promotorZona,
        activo: !prevState.promotorZona.activo,
      },
    }));
  };

  validarContenidos = () => {};

  resultadoGrabar = (resultado, data) => {
    if (resultado === OK) {
      this.props.history.goBack();
    }
  };

  guardarCambios = () => {
    try {
      this.validarContenidos();
      this.props.guardarPromotorZona(
        this.state.promotorZona,
        this.resultadoGrabar
      );
    } catch (msg) {
      this.abrirAlerta(msg);
    }
  };

  abrirAlerta = (text, contenidoTipo = "error") => {
    this.props.addMessage({ variant: contenidoTipo, message: text + "" });
  };

  getZonas = (zonas) => {
    var content = [];
    try {
      zonas
        .filter((zona) => {
          return zona.activo;
        })
        .forEach((zona) => {
          content.push(
            <MenuItem key={"zona" + zona.id} value={zona.id}>
              {zona.nombre}
            </MenuItem>
          );
        });
    } catch (ex) {}
    return content;
  };

  getPromotores = (promotores) => {
    var content = [];
    try {
      promotores
        .filter((promotor) => {
          return promotor.c_activo && promotor.perfil + "" === PERFIL_PROMOTOR;
        })
        .forEach((promotor) => {
          content.push(
            <MenuItem key={"promotor" + promotor.c_id} value={promotor.c_id}>
              {promotor.nombre1 +
                " " +
                promotor.nombre2 +
                " " +
                promotor.apellido1 +
                " " +
                promotor.apellido2 +
                " " +
                " (" +
                promotor.identificacion +
                ")"}
            </MenuItem>
          );
        });
    } catch (ex) {}
    return content;
  };

  render() {
    const { classes } = this.props;

    let { isActivityIndicatorShown } = this.props.promotorZonaState;
    const { usuarios } = this.props.usuarioState;
    const { zonas } = this.props.zonaState;

    const styleCheck = {
      margin: "0px 0px 0px -10px",
    };
    return (
      <GridItem xs={12}>
        {isActivityIndicatorShown && (
          <WaitDialog text={this.state.textAlertInfo} />
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
              <DirectionsRunIcon />
            </CardIcon>
            <TituloPagina2
              texto="Datos de promotor y la zona"
              color="primary"
              classes={classes}
            />
          </CardHeader>

          <CardBody>
            <GridContainer>
              <GridItem xs={12} sm={12} md={12}>
                <CustomSelect
                  key="promotor"
                  success
                  labelText="* Promotor"
                  id="promotor"
                  formControlProps={{
                    fullWidth: true,
                  }}
                  inputProps={{
                    name: "promotor",
                    id: "promotor",
                    value: this.state.promotorZona.promotor,
                    onChange: (event) =>
                      this.cambioContenidoPromotorZona(
                        event.target.value,
                        "promotor"
                      ),
                  }}
                  selectContent={this.getPromotores(usuarios)}
                />
              </GridItem>

              <GridItem xs={12} sm={12} md={12}>
                <CustomSelect
                  key="zona"
                  success
                  labelText="* Zona"
                  id="zona"
                  formControlProps={{
                    fullWidth: true,
                  }}
                  inputProps={{
                    name: "zona",
                    id: "zona",
                    value: this.state.promotorZona.zona,
                    onChange: (event) =>
                      this.cambioContenidoPromotorZona(
                        event.target.value,
                        "zona"
                      ),
                  }}
                  selectContent={this.getZonas(zonas)}
                />
              </GridItem>

              <GridItem xs={4} sm={2} md={2} lg={1}>
                <FormControlLabel
                  style={styleCheck}
                  control={
                    <Checkbox
                      checkedIcon={<Check className={classes.checkedIcon} />}
                      icon={<Check className={classes.uncheckedIcon} />}
                      classes={{
                        checked: classes.checked,
                      }}
                      checked={this.state.promotorZona.lunes}
                      onChange={(event) =>
                        this.cambioContenidoPromotorZona(
                          !this.state.promotorZona.lunes,
                          "lunes"
                        )
                      }
                      disableRipple
                    />
                  }
                  label="Lun"
                ></FormControlLabel>
              </GridItem>

              <GridItem xs={4} sm={2} md={2} lg={1}>
                <FormControlLabel
                  style={styleCheck}
                  control={
                    <Checkbox
                      checkedIcon={<Check className={classes.checkedIcon} />}
                      icon={<Check className={classes.uncheckedIcon} />}
                      classes={{
                        checked: classes.checked,
                      }}
                      checked={this.state.promotorZona.martes}
                      onChange={(event) =>
                        this.cambioContenidoPromotorZona(
                          !this.state.promotorZona.martes,
                          "martes"
                        )
                      }
                      disableRipple
                    />
                  }
                  label="Mar"
                ></FormControlLabel>
              </GridItem>

              <GridItem xs={4} sm={2} md={2} lg={1}>
                <FormControlLabel
                  style={styleCheck}
                  control={
                    <Checkbox
                      checkedIcon={<Check className={classes.checkedIcon} />}
                      icon={<Check className={classes.uncheckedIcon} />}
                      classes={{
                        checked: classes.checked,
                      }}
                      checked={this.state.promotorZona.miercoles}
                      onChange={(event) =>
                        this.cambioContenidoPromotorZona(
                          !this.state.promotorZona.miercoles,
                          "miercoles"
                        )
                      }
                      disableRipple
                    />
                  }
                  label="Mie"
                ></FormControlLabel>
              </GridItem>

              <GridItem xs={4} sm={2} md={2} lg={1}>
                <FormControlLabel
                  style={styleCheck}
                  control={
                    <Checkbox
                      checkedIcon={<Check className={classes.checkedIcon} />}
                      icon={<Check className={classes.uncheckedIcon} />}
                      classes={{
                        checked: classes.checked,
                      }}
                      checked={this.state.promotorZona.jueves}
                      onChange={(event) =>
                        this.cambioContenidoPromotorZona(
                          !this.state.promotorZona.jueves,
                          "jueves"
                        )
                      }
                      disableRipple
                    />
                  }
                  label="Jue"
                ></FormControlLabel>
              </GridItem>

              <GridItem xs={4} sm={2} md={2} lg={1}>
                <FormControlLabel
                  style={styleCheck}
                  control={
                    <Checkbox
                      checkedIcon={<Check className={classes.checkedIcon} />}
                      icon={<Check className={classes.uncheckedIcon} />}
                      classes={{
                        checked: classes.checked,
                      }}
                      checked={this.state.promotorZona.viernes}
                      onChange={(event) =>
                        this.cambioContenidoPromotorZona(
                          !this.state.promotorZona.viernes,
                          "viernes"
                        )
                      }
                      disableRipple
                    />
                  }
                  label="Vie"
                ></FormControlLabel>
              </GridItem>

              <GridItem xs={4} sm={2} md={2} lg={1}>
                <FormControlLabel
                  style={styleCheck}
                  control={
                    <Checkbox
                      checkedIcon={<Check className={classes.checkedIcon} />}
                      icon={<Check className={classes.uncheckedIcon} />}
                      classes={{
                        checked: classes.checked,
                      }}
                      checked={this.state.promotorZona.sabado}
                      onChange={(event) =>
                        this.cambioContenidoPromotorZona(
                          !this.state.promotorZona.sabado,
                          "sabado"
                        )
                      }
                      disableRipple
                    />
                  }
                  label="Sab"
                ></FormControlLabel>
              </GridItem>

              <GridItem xs={4} sm={2} md={2} lg={1}>
                <FormControlLabel
                  style={styleCheck}
                  control={
                    <Checkbox
                      checkedIcon={<Check className={classes.checkedIcon} />}
                      icon={<Check className={classes.uncheckedIcon} />}
                      classes={{
                        checked: classes.checked,
                      }}
                      checked={this.state.promotorZona.domingo}
                      onChange={(event) =>
                        this.cambioContenidoPromotorZona(
                          !this.state.promotorZona.domingo,
                          "domingo"
                        )
                      }
                      disableRipple
                    />
                  }
                  label="Dom"
                ></FormControlLabel>
              </GridItem>

              <GridItem xs={12} sm={12} md={12}>
                <CustomTextarea
                  labelText="Detalle"
                  id="detalle"
                  success
                  formControlProps={{
                    fullWidth: true,
                  }}
                  inputProps={{
                    value: this.state.promotorZona.detalle,
                    onChange: (event) =>
                      this.cambioContenidoPromotorZona(
                        event.target.value,
                        "detalle"
                      ),
                  }}
                />
              </GridItem>

              <GridItem xs={12} sm={12} md={12}>
                <FormControlLabel
                  style={styleCheck}
                  control={
                    <Checkbox
                      checkedIcon={<Check className={classes.checkedIcon} />}
                      icon={<Check className={classes.uncheckedIcon} />}
                      classes={{
                        checked: classes.checked,
                      }}
                      checked={this.state.promotorZona.activo}
                      onChange={(event) =>
                        this.cambioContenidoPromotorZona(
                          !this.state.promotorZona.activo,
                          "activo"
                        )
                      }
                      disableRipple
                    />
                  }
                  label="Activo"
                ></FormControlLabel>
              </GridItem>

              <GridItem xs={12} sm={12} md={12}>
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
          </CardBody>
        </Card>
      </GridItem>
    );
  }
}

const mapStateToProps = (state) => {
  return {
    promotorZonaState: state.promotorZonaState,
    usuarioState: state.usuarioState,
    zonaState: state.zonaState,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    guardarPromotorZona: (promotorZona, onSuccess) =>
      dispatch(guardarPromotorZona(promotorZona, onSuccess)),
    addMessage: (content) => dispatch(addMessage(content)),
    listarUsuario: (paginacion) =>
      dispatch(listarUsuario(paginacion, () => {})),
    listarZona: (paginacion) => dispatch(listarZona(paginacion, () => {})),
  };
};

export default withStyles(extendedTablesStyle)(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(PromotorZonaAdministrar)
);
