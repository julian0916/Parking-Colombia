import Checkbox from "@material-ui/core/Checkbox";
import FormControl from "@material-ui/core/FormControl";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import InputAdornment from "@material-ui/core/InputAdornment";
import withStyles from "@material-ui/core/styles/withStyles";
import TextField from "@material-ui/core/TextField";
import AccessAlarmIcon from "@material-ui/icons/AccessAlarm";
import AccessTimeIcon from "@material-ui/icons/AccessTime";
import AttachMoneyIcon from "@material-ui/icons/AttachMoney";
import Check from "@material-ui/icons/Check";
import DriveEtaIcon from "@material-ui/icons/DriveEta";
import EditLocationIcon from "@material-ui/icons/EditLocation";
import GpsFixedIcon from "@material-ui/icons/GpsFixed";
import MapIcon from "@material-ui/icons/Map";
import MotorcycleIcon from "@material-ui/icons/Motorcycle";
import PersonIcon from "@material-ui/icons/Person";
import SaveIcon from "@material-ui/icons/Save";
import extendedTablesStyle from "assets/jss/material-dashboard-pro-react/views/extendedTablesStyle.jsx";
import Card from "components/Card/Card.jsx";
import CardBody from "components/Card/CardBody.jsx";
import CardHeader from "components/Card/CardHeader.jsx";
import CardIcon from "components/Card/CardIcon.jsx";
import Button from "components/CustomButtons/Button.jsx";
import CustomInput from "components/CustomInput/CustomInput.jsx";
import CustomTextarea from "components/CustomTextarea/CustomTextarea";
import GridContainer from "components/Grid/GridContainer.jsx";
import GridItem from "components/Grid/GridItem.jsx";
import { OK } from "config/general/Configuracion";
import AplicarSeguridad from "conten/components/AplicarSeguridad";
import TituloPagina2 from "conten/components/TituloPagina2";
import WaitDialog from "conten/components/WaitDialog.jsx";
import { addMessage } from "conten/mensajes/MessagesActions";
import { guardarZona } from "conten/zonas/ZonaActions";
import React from "react";
import { connect } from "react-redux";

class ZonaAdministrar extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      textAlertInfo: "Estamos procesando su solicitud un momento por favor :)",
      zona: {
        id: 0,
        nombre: "",
        direccion: "",
        observacion: "",
        latitud: "",
        longitud: "",
        celdasCarro: 0,
        celdasMoto: 0,
        valorHoraCarro: 0,
        valorHoraMoto: 0,
        entreSemanaInicia: "06:00:00",
        entreSemanaTermina: "20:00:00",
        finSemanaInicia: "06:00:00",
        finSemanaTermina: "22:00:00",
        activo: true,
        minutosGracia: 15,
        minutosParaNuevaGracia: 60,
      },
    };
  }

  componentDidMount = () => {
    var urlParams = new URLSearchParams(this.props.location.search);
    if (urlParams.has("editar")) {
      if (urlParams.get("editar") === "true") {
        if (this.props.location.state.zona) {
          this.setState({ zona: this.props.location.state.zona });
        }
      }
    }
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

  cambioContenidoZona = (valor, campo) => {
    var zona = this.state.zona;
    zona[campo] = valor;
    this.setState({ zona: zona });
    return true;
  };

  cambioActivo = () => {
    this.setState((prevState) => ({
      zona: { ...prevState.zona, activo: !prevState.zona.activo },
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
      this.props.guardarZona(this.state.zona, this.resultadoGrabar);
    } catch (msg) {
      this.abrirAlerta(msg);
    }
  };

  abrirAlerta = (text, contenidoTipo = "error") => {
    this.props.addMessage({ variant: contenidoTipo, message: text + "" });
  };

  getParteHora = (tiempo) => {
    return tiempo.split(":")[0];
  };

  getParteMinuto = (tiempo) => {
    var contenido = tiempo.split(":");
    return contenido[1] + ":" + contenido[2];
  };

  render() {
    const { classes } = this.props;

    let { isActivityIndicatorShown } = this.props.zonaState;
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
              <MapIcon />
            </CardIcon>
            <TituloPagina2
              texto="Datos de zona"
              color="primary"
              classes={classes}
            />
          </CardHeader>

          <CardBody>
            <GridContainer>
              <GridItem xs={12} sm={6} md={6}>
                <CustomInput
                  labelText="* Nombre"
                  id="etiquetaNombre"
                  success
                  formControlProps={{
                    fullWidth: true,
                  }}
                  inputProps={{
                    name: "nombre",
                    id: "nombre",
                    value: this.state.zona.nombre,
                    onChange: (event) =>
                      this.cambioContenidoZona(event.target.value, "nombre"),
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
                  labelText="* Dirección"
                  id="etiquetaDireccion"
                  success
                  formControlProps={{
                    fullWidth: true,
                  }}
                  inputProps={{
                    name: "direccion",
                    id: "direccion",
                    value: this.state.zona.direccion,
                    onChange: (event) =>
                      this.cambioContenidoZona(event.target.value, "direccion"),
                    endAdornment: (
                      <InputAdornment position="start">
                        <EditLocationIcon />
                      </InputAdornment>
                    ),
                  }}
                />
              </GridItem>

              <GridItem xs={12} sm={6} md={6}>
                <CustomInput
                  labelText="Latitud"
                  id="etiquetaLatitud"
                  success
                  formControlProps={{
                    fullWidth: true,
                  }}
                  inputProps={{
                    name: "latitud",
                    id: "latitud",
                    value: this.state.zona.latitud,
                    onChange: (event) =>
                      this.cambioContenidoZona(
                        this.soloDecimales(event.target.value),
                        "latitud"
                      ),
                    endAdornment: (
                      <InputAdornment position="start">
                        <GpsFixedIcon />
                      </InputAdornment>
                    ),
                  }}
                />
              </GridItem>

              <GridItem xs={12} sm={6} md={6}>
                <CustomInput
                  labelText="Longitud"
                  id="etiquetLongitud"
                  success
                  formControlProps={{
                    fullWidth: true,
                  }}
                  inputProps={{
                    name: "longitud",
                    id: "longitud",
                    value: this.state.zona.longitud,
                    onChange: (event) =>
                      this.cambioContenidoZona(
                        this.soloDecimales(event.target.value),
                        "longitud"
                      ),
                    endAdornment: (
                      <InputAdornment position="start">
                        <GpsFixedIcon />
                      </InputAdornment>
                    ),
                  }}
                />
              </GridItem>

              <GridItem xs={12} sm={6} md={6}>
                <FormControl
                  fullWidth
                  style={{ marginTop: "15px", marginBottom: "5px" }}
                >
                  <TextField
                    id="horaIniciaOperacionSemana"
                    label="* Horario inicia en semana"
                    type="time"
                    value={this.state.zona.entreSemanaInicia}
                    className={classes.textField}
                    onChange={(event) => {
                      this.cambioContenidoZona(
                        event.target.value + ":00",
                        "entreSemanaInicia"
                      );
                    }}
                    InputLabelProps={{
                      shrink: true,
                    }}
                  />
                </FormControl>
              </GridItem>

              <GridItem xs={12} sm={6} md={6}>
                <FormControl
                  fullWidth
                  style={{ marginTop: "15px", marginBottom: "5px" }}
                >
                  <TextField
                    id="horaTerminaOperacionSemana"
                    label="* Horario finaliza en semana"
                    type="time"
                    value={this.state.zona.entreSemanaTermina}
                    className={classes.textField}
                    onChange={(event) => {
                      this.cambioContenidoZona(
                        event.target.value + ":00",
                        "entreSemanaTermina"
                      );
                    }}
                    InputLabelProps={{
                      shrink: true,
                    }}
                  />
                </FormControl>
              </GridItem>

              <GridItem xs={12} sm={6} md={6}>
                <FormControl
                  fullWidth
                  style={{ marginTop: "15px", marginBottom: "5px" }}
                >
                  <TextField
                    id="horaIniciaOperacionFinDeSemana"
                    label="* Horario inicia en fin de semana"
                    type="time"
                    value={this.state.zona.finSemanaInicia}
                    className={classes.textField}
                    onChange={(event) => {
                      this.cambioContenidoZona(
                        event.target.value + ":00",
                        "finSemanaInicia"
                      );
                    }}
                    InputLabelProps={{
                      shrink: true,
                    }}
                  />
                </FormControl>
              </GridItem>

              <GridItem xs={12} sm={6} md={6}>
                <FormControl
                  fullWidth
                  style={{ marginTop: "15px", marginBottom: "5px" }}
                >
                  <TextField
                    id="horaTerminaOperacionFinDeSemana"
                    label="* Horario termina en fin de semana"
                    type="time"
                    value={this.state.zona.finSemanaTermina}
                    className={classes.textField}
                    onChange={(event) => {
                      this.cambioContenidoZona(
                        event.target.value + ":00",
                        "finSemanaTermina"
                      );
                    }}
                    InputLabelProps={{
                      shrink: true,
                    }}
                  />
                </FormControl>
              </GridItem>

              <GridItem xs={12} sm={6} md={6}>
                <CustomInput
                  labelText="* Minutos de gracias"
                  id="etiquetMInutosDeGracia"
                  success
                  formControlProps={{
                    fullWidth: true,
                  }}
                  inputProps={{
                    name: "minutosDeGracia",
                    id: "minutosDeGracia",
                    value: this.state.zona.minutosGracia,
                    onChange: (event) =>
                      this.cambioContenidoZona(
                        this.soloNumeros(event.target.value),
                        "minutosGracia"
                      ),
                    endAdornment: (
                      <InputAdornment position="start">
                        <AccessTimeIcon />
                      </InputAdornment>
                    ),
                  }}
                />
              </GridItem>

              <GridItem xs={12} sm={6} md={6}>
                <CustomInput
                  labelText="* Minutos para nuevo periodo de gracia"
                  id="etiquetaMinutosParaNuevaGracia"
                  success
                  formControlProps={{
                    fullWidth: true,
                  }}
                  inputProps={{
                    name: "minutosParaNuevaGracia",
                    id: "minutosParaNuevaGracia",
                    value: this.state.zona.minutosParaNuevaGracia,
                    onChange: (event) =>
                      this.cambioContenidoZona(
                        this.soloNumeros(event.target.value),
                        "minutosParaNuevaGracia"
                      ),
                    endAdornment: (
                      <InputAdornment position="start">
                        <AccessAlarmIcon />
                      </InputAdornment>
                    ),
                  }}
                />
              </GridItem>

              <GridItem xs={12} sm={6} md={6}>
                <CustomInput
                  labelText="* Celdas carro"
                  id="etiquetCeldasCarro"
                  success
                  formControlProps={{
                    fullWidth: true,
                  }}
                  inputProps={{
                    name: "celdasCarro",
                    id: "celdasCarro",
                    value: this.state.zona.celdasCarro,
                    onChange: (event) =>
                      this.cambioContenidoZona(
                        this.soloNumeros(event.target.value),
                        "celdasCarro"
                      ),
                    endAdornment: (
                      <InputAdornment position="start">
                        <DriveEtaIcon />
                      </InputAdornment>
                    ),
                  }}
                />
              </GridItem>

              <GridItem xs={12} sm={6} md={6}>
                <CustomInput
                  labelText="* Celdas moto"
                  id="etiquetCeldasMoto"
                  success
                  formControlProps={{
                    fullWidth: true,
                  }}
                  inputProps={{
                    name: "celdasMoto",
                    id: "celdasMoto",
                    value: this.state.zona.celdasMoto,
                    onChange: (event) =>
                      this.cambioContenidoZona(
                        this.soloNumeros(event.target.value),
                        "celdasMoto"
                      ),
                    endAdornment: (
                      <InputAdornment position="start">
                        <MotorcycleIcon />
                      </InputAdornment>
                    ),
                  }}
                />
              </GridItem>

              <GridItem xs={12} sm={6} md={6}>
                <CustomInput
                  labelText="* Valor hora carro"
                  id="ValorHoraCarro"
                  success
                  formControlProps={{
                    fullWidth: true,
                  }}
                  inputProps={{
                    name: "valorHoraCarro",
                    id: "valorHoraCarro",
                    value: this.state.zona.valorHoraCarro,
                    onChange: (event) =>
                      this.cambioContenidoZona(
                        this.soloNumeros(event.target.value),
                        "valorHoraCarro"
                      ),
                    endAdornment: (
                      <InputAdornment position="start">
                        <AttachMoneyIcon />
                      </InputAdornment>
                    ),
                  }}
                />
              </GridItem>

              <GridItem xs={12} sm={6} md={6}>
                <CustomInput
                  labelText="* Valor hora moto"
                  id="valorHoraMoto"
                  success
                  formControlProps={{
                    fullWidth: true,
                  }}
                  inputProps={{
                    name: "valorHoraMoto",
                    id: "valorHoraMoto",
                    value: this.state.zona.valorHoraMoto,
                    onChange: (event) =>
                      this.cambioContenidoZona(
                        this.soloNumeros(event.target.value),
                        "valorHoraMoto"
                      ),
                    endAdornment: (
                      <InputAdornment position="start">
                        <AttachMoneyIcon />
                      </InputAdornment>
                    ),
                  }}
                />
              </GridItem>

              <GridItem xs={12} sm={6} md={6}>
                <CustomTextarea
                  labelText="Observación"
                  id="observacion"
                  success
                  formControlProps={{
                    fullWidth: true,
                  }}
                  inputProps={{
                    value: this.state.zona.observacion,
                    onChange: (event) =>
                      this.cambioContenidoZona(
                        event.target.value,
                        "observacion"
                      ),
                  }}
                />
              </GridItem>

              <GridItem xs={12} sm={6} md={6}>
                <FormControlLabel
                  style={{ margin: "15px 0px 0px -10px" }}
                  control={
                    <Checkbox
                      checkedIcon={<Check className={classes.checkedIcon} />}
                      icon={<Check className={classes.uncheckedIcon} />}
                      classes={{
                        checked: classes.checked,
                      }}
                      checked={this.state.zona.activo}
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
          </CardBody>
        </Card>
      </GridItem>
    );
  }
}

const mapStateToProps = (state) => {
  return {
    zonaState: state.zonaState,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    guardarZona: (zona, onSuccess) => dispatch(guardarZona(zona, onSuccess)),
    addMessage: (content) => dispatch(addMessage(content)),
  };
};

export default withStyles(extendedTablesStyle)(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(ZonaAdministrar)
);
