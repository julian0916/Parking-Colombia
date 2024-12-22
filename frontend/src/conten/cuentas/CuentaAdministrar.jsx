import Checkbox from "@material-ui/core/Checkbox";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import InputAdornment from "@material-ui/core/InputAdornment";
// @material-ui/core components
import withStyles from "@material-ui/core/styles/withStyles";
import Check from "@material-ui/icons/Check";
import InfoIcon from "@material-ui/icons/Info";
import PeopleIcon from "@material-ui/icons/People";
import PublicIcon from "@material-ui/icons/Public";
import SaveIcon from "@material-ui/icons/Save";
import extendedTablesStyle from "assets/jss/material-dashboard-pro-react/views/extendedTablesStyle.jsx";
import Card from "components/Card/Card.jsx";
import CardBody from "components/Card/CardBody.jsx";
import CardHeader from "components/Card/CardHeader.jsx";
import CardIcon from "components/Card/CardIcon.jsx";
// core components
import Button from "components/CustomButtons/Button.jsx";
import CustomInput from "components/CustomInput/CustomInput.jsx";
import CustomTextarea from "components/CustomTextarea/CustomTextarea";
import GridContainer from "components/Grid/GridContainer.jsx";
import GridItem from "components/Grid/GridItem.jsx";
import { OK } from "config/general/Configuracion";
import AplicarSeguridad from "conten/components/AplicarSeguridad";
import TituloPagina2 from "conten/components/TituloPagina2";
// module components
import WaitDialog from "conten/components/WaitDialog.jsx";
import { grabarCuenta } from "conten/cuentas/CuentaActions";
import { addMessage } from "conten/mensajes/MessagesActions";
import React from "react";
import { connect } from "react-redux";

class CuentaAdministrar extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      textoAlerta: "Estamos procesando los datos :)",
      cuenta: {
        schema: "",
        nombre: "",
        informacion: "",
        activo: true,
      },
    };
  }

  componentDidMount = () => {
    var urlParams = new URLSearchParams(this.props.location.search);
    if (urlParams.has("editar")) {
      if (urlParams.get("editar") === "true") {
        if (this.props.location.state.cuenta) {
          this.setState({ cuenta: this.props.location.state.cuenta });
        }
      }
    }
  };

  abrirAlerta = (text, contenidoTipo = "error") => {
    this.props.addMessage({ variant: contenidoTipo, message: text + "" });
  };

  cambioContenidoCuenta = (valor, campo) => {
    var cuentaCambio = this.state.cuenta;
    cuentaCambio[campo] = valor;
    this.setState({ cuenta: cuentaCambio });
    return true;
  };

  validarContenidos = () => {};

  resultadoGrabar = (resultado) => {
    if (resultado === OK) {
      this.props.history.goBack();
    }
  };

  guardarCambios = () => {
    try {
      this.validarContenidos();
      this.props.grabarCuenta(this.state.cuenta, this.resultadoGrabar);
    } catch (msg) {
      this.abrirAlerta(msg);
    }
  };

  render() {
    const { classes } = this.props;
    const { isActivityIndicatorShown } = this.props.cuentaState;
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
              texto="Datos de cuenta"
              color="primary"
              classes={classes}
            />
          </CardHeader>
          <CardBody>
            <GridContainer>
              <GridItem xs={12} sm={12}>
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
                    value: this.state.cuenta.nombre,
                    onChange: (event) =>
                      this.cambioContenidoCuenta(event.target.value, "nombre"),
                    endAdornment: (
                      <InputAdornment position="start">
                        <PublicIcon />
                      </InputAdornment>
                    ),
                  }}
                />
              </GridItem>

              <GridItem xs={12} sm={12}>
                <CustomTextarea
                  labelText="Información"
                  id="informacion"
                  success
                  formControlProps={{
                    fullWidth: true,
                  }}
                  iconend={<InfoIcon />}
                  value={this.state.cuenta.informacion}
                  inputProps={{
                    value: this.state.cuenta.informacion,
                    onChange: (event) =>
                      this.cambioContenidoCuenta(
                        event.target.value,
                        "informacion"
                      ),
                  }}
                />
              </GridItem>

              <GridItem xs={12} sm={12}>
                <FormControlLabel
                  control={
                    <Checkbox
                      checkedIcon={<Check className={classes.checkedIcon} />}
                      icon={<Check className={classes.uncheckedIcon} />}
                      classes={{
                        checked: classes.checked,
                      }}
                      checked={this.state.cuenta.activo}
                      onChange={() =>
                        this.cambioContenidoCuenta(
                          !this.state.cuenta.activo,
                          "activo"
                        )
                      }
                      disableRipple
                    />
                  }
                  label="Activa"
                ></FormControlLabel>
              </GridItem>

              <br />
              <GridItem xs={12} sm={12} className={classes.gridItem}>
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
    cuentaState: state.cuentaState,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    addMessage: (content) => dispatch(addMessage(content)),
    grabarCuenta: (cuenta, onSuccess) =>
      dispatch(grabarCuenta(cuenta, onSuccess)),
  };
};

export default withStyles(extendedTablesStyle)(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(CuentaAdministrar)
);
