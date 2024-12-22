import InputAdornment from "@material-ui/core/InputAdornment";
// @material-ui/core components
import withStyles from "@material-ui/core/styles/withStyles";
import InfoIcon from "@material-ui/icons/Info";
import KeyboardIcon from "@material-ui/icons/Keyboard";
import ReceiptIcon from "@material-ui/icons/Receipt";
import SaveIcon from "@material-ui/icons/Save";
import TitleIcon from "@material-ui/icons/Title";
import WorkOutlineIcon from "@material-ui/icons/WorkOutline";
import extendedTablesStyle from "assets/jss/material-dashboard-pro-react/views/extendedTablesStyle.jsx";
import Accordion from "components/Accordion/Accordion";
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
import {
  getLimiteEndeudamientoPromotor,
  grabarLimiteEndeudamientoPromotor,
  grabarTiquete,
  listarTiquete,
} from "conten/configuracion/ConfiguracionActions";
import { addMessage } from "conten/mensajes/MessagesActions";
import React from "react";
import { connect } from "react-redux";
import ExposureIcon from "@material-ui/icons/Exposure";
import {soloNumeros,formatoDinero} from "config/funciones/Comunes";

class ConfiguracionAdministrar extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      textoAlerta: "Estamos procesando su solicitud :)",
      tiquete: undefined,
      limiteEndeudamientoPromotor: formatoDinero(50000),
    };
  }

  componentDidMount = () => {
    this.props.listarTiquete(() => {
      this.setState({ tiquete: this.props.configuracionState.tiquete });
    });
    this.props.getLimiteEndeudamientoPromotor((estado, conte) => {
      if (estado === OK) {
        this.setState({
          limiteEndeudamientoPromotor: formatoDinero(conte),
        });
      }
    });
  };

  cambioContenidoTiquete = (valor, campo) => {
    var tiqueteCambio = this.state.tiquete;
    tiqueteCambio[campo] = valor;
    this.setState({ tiquete: tiqueteCambio });
    return true;
  };

  cambioContenidoLimiteEndeudamientoPromotor = (valor) => {
    this.setState({ limiteEndeudamientoPromotor: valor });
    return true;
  };

  grabarContenidoLimiteEndeudamientoPromotor = () => {
    var valor = soloNumeros(this.state.limiteEndeudamientoPromotor);
    this.props.grabarLimiteEndeudamientoPromotor(
      valor,
      this.resultadoGrabarContenidoLimiteEndeudamientoPromotor
    );
  };

  resultadoGrabarContenidoLimiteEndeudamientoPromotor = (resultado, cont) => {
    if (resultado === OK) {
      this.props.addMessage({
        variant: "success",
        message: "Los datos se almacenaron correctamente",
      });
    }
  };

  validarContenidos = () => {};

  resultadoGrabar = (resultado, data) => {
    if (resultado === OK) {
      this.props.addMessage({
        variant: "success",
        message: "Los datos del tiquete se almacenaron correctamente",
      });
    }
  };

  guardarCambios = () => {
    try {
      this.validarContenidos();
      this.props.grabarTiquete(this.state.tiquete, this.resultadoGrabar);
    } catch (msg) {
      this.abrirAlerta(msg);
    }
  };

  render() {
    const { classes } = this.props;
    const { tiquete } = this.state;
    const { isActivityIndicatorShown } = this.props.configuracionState;
    return (
      <GridItem xs={12}>
        {isActivityIndicatorShown && (
          <WaitDialog text={this.state.textoAlerta} />
        )}
        <AplicarSeguridad />
        <Card>
          <CardHeader icon>
            <CardIcon
              style={
                {
                  // cursor: "pointer",
                }
              }
              color="primary"
              onClick={() => {
                //this.props.history.goBack();
              }}
            >
              <b>{""}</b>
              <ReceiptIcon />
            </CardIcon>
            <TituloPagina2
              texto="Datos de configuración del sistema"
              color="primary"
              classes={classes}
            />
          </CardHeader>

          <CardBody>
            <Accordion
              active={0}
              collapses={[
                {
                  title: "Datos del tiquete",
                  content: (
                    <>
                      {tiquete && (
                        <GridContainer>
                          <GridItem xs={12} sm={6} md={6}>
                            <CustomInput
                              labelText="* Nit"
                              id="etiquetaNit"
                              success
                              formControlProps={{
                                fullWidth: true,
                              }}
                              inputProps={{
                                name: "nit",
                                id: "nit",
                                value: this.state.tiquete.nit,
                                onChange: (event) =>
                                  this.cambioContenidoTiquete(
                                    event.target.value,
                                    "nit"
                                  ),
                                endAdornment: (
                                  <InputAdornment position="start">
                                    <KeyboardIcon />
                                  </InputAdornment>
                                ),
                              }}
                            />
                          </GridItem>

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
                                value: this.state.tiquete.nombre,
                                onChange: (event) =>
                                  this.cambioContenidoTiquete(
                                    event.target.value,
                                    "nombre"
                                  ),
                                endAdornment: (
                                  <InputAdornment position="start">
                                    <WorkOutlineIcon />
                                  </InputAdornment>
                                ),
                              }}
                            />
                          </GridItem>

                          <GridItem xs={12} sm={6} md={6}>
                            <CustomInput
                              labelText="* Lema"
                              id="etiquetaLema"
                              success
                              formControlProps={{
                                fullWidth: true,
                              }}
                              inputProps={{
                                name: "lema",
                                id: "lema",
                                value: this.state.tiquete.lema,
                                onChange: (event) =>
                                  this.cambioContenidoTiquete(
                                    event.target.value,
                                    "lema"
                                  ),
                                endAdornment: (
                                  <InputAdornment position="start">
                                    <TitleIcon />
                                  </InputAdornment>
                                ),
                              }}
                            />
                          </GridItem>

                          <GridItem xs={12} sm={12}>
                            <CustomTextarea
                              labelText="* Términos"
                              id="terminos"
                              success
                              formControlProps={{
                                fullWidth: true,
                              }}
                              iconend={<InfoIcon />}
                              value={this.state.tiquete.terminos}
                              inputProps={{
                                value: this.state.tiquete.terminos,
                                onChange: (event) =>
                                  this.cambioContenidoTiquete(
                                    event.target.value,
                                    "terminos"
                                  ),
                              }}
                            />
                          </GridItem>

                          <GridItem
                            xs={12}
                            sm={12}
                            className={classes.gridItem}
                          >
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
                      )}
                    </>
                  ),
                },
                {
                  title: "Configuración limite endeudamiento ",
                  content: (
                    <GridContainer>
                      <GridItem xs={12} sm={8} md={9} lg={10}>
                        <CustomInput
                          labelText="* Limite deuda de un promotor"
                          id="etiquetaLimiteEndeudamientoPromotor"
                          success
                          formControlProps={{
                            fullWidth: true,
                          }}
                          inputProps={{
                            name: "limiteEndeudamientoPromotor",
                            id: "limiteEndeudamientoPromotor",
                            value: this.state.limiteEndeudamientoPromotor,
                            onChange: (event) =>
                              this.cambioContenidoLimiteEndeudamientoPromotor(
                                formatoDinero(soloNumeros(event.target.value))
                              ),
                            endAdornment: (
                              <InputAdornment position="start">
                                <ExposureIcon />
                              </InputAdornment>
                            ),
                          }}
                        />
                      </GridItem>

                      <GridItem xs={12} sm={4} md={3} lg={2}>
                        <Button
                          type="button"
                          onClick={(e) => {
                            this.grabarContenidoLimiteEndeudamientoPromotor();
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
    configuracionState: state.configuracionState,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    addMessage: (content) => dispatch(addMessage(content)),

    grabarTiquete: (tiquete, onSuccess) =>
      dispatch(grabarTiquete(tiquete, onSuccess)),

    listarTiquete: (onSuccess) => dispatch(listarTiquete(onSuccess)),

    getLimiteEndeudamientoPromotor: (onSuccess) =>
      dispatch(getLimiteEndeudamientoPromotor(onSuccess)),

    grabarLimiteEndeudamientoPromotor: (limite, onSuccess) =>
      dispatch(grabarLimiteEndeudamientoPromotor(limite, onSuccess)),
  };
};

export default withStyles(extendedTablesStyle)(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(ConfiguracionAdministrar)
);
