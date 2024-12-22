import InputAdornment from "@material-ui/core/InputAdornment";
import withStyles from "@material-ui/core/styles/withStyles";
import CancelIcon from "@material-ui/icons/Cancel";
import CheckCircleIcon from "@material-ui/icons/CheckCircle";
import ClearIcon from "@material-ui/icons/Clear";
import DeleteIcon from "@material-ui/icons/Delete";
import Edit from "@material-ui/icons/Edit";
import ListAltIcon from "@material-ui/icons/ListAlt";
import SaveIcon from "@material-ui/icons/Save";
import {
  successColor,
  whiteColor,
} from "assets/jss/material-dashboard-pro-react.jsx";
import extendedTablesStyle from "assets/jss/material-dashboard-pro-react/views/extendedTablesStyle.jsx";
import Card from "components/Card/Card.jsx";
import CardBody from "components/Card/CardBody.jsx";
import CardHeader from "components/Card/CardHeader.jsx";
import CardIcon from "components/Card/CardIcon.jsx";
import Button from "components/CustomButtons/Button.jsx";
import CustomInput from "components/CustomInput/CustomInput.jsx";
import GridContainer from "components/Grid/GridContainer.jsx";
import GridItem from "components/Grid/GridItem.jsx";
import { controlarAccceso } from "config/funciones/Comunes";
import {
  CONFIGURACION_TIPOGRAFIA_14,
  FILA_BLANCA,
  FILA_GRIS,
  LISTADO_PAGINACION,
  OK,
} from "config/general/Configuracion";
import Alerta2Botones from "conten/components/Alerta2Botones";
import EncabezadoGrid from "conten/components/EncabezadoGrid";
import TituloPagina2 from "conten/components/TituloPagina2";
import WaitDialog from "conten/components/WaitDialog.jsx";
import { addMessage } from "conten/mensajes/MessagesActions";
import {
  borrarSupervisionPregunta,
  grabarSupervisionPregunta,
  listarSupervisionPregunta,
} from "conten/supervision/SupervisionActions";
import React from "react";
import { connect } from "react-redux";
import { Redirect } from "react-router-dom";

var temporizador = null;
const MILI_SEGUNDOS_REFRESCAR = 1000 * 60 * 5;

const columnasOrdenar2 = [
  {
    campos: [{ id: "", isSorted: false, label: "Editar" }],
    xs: 12,
    sm: 12,
    md: 1,
    lg: 1,
  },
  {
    campos: [{ id: "", isSorted: false, label: "Borrar" }],
    xs: 12,
    sm: 12,
    md: 1,
    lg: 1,
  },
  {
    campos: [{ id: "", isSorted: false, label: "Elemento que se verifica" }],
    xs: 12,
    sm: 12,
    md: 10,
    lg: 10,
  },
];
class SupervisionPreguntas extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      textAlertInfo: "Un momento mientras completamos su solicitud ;)",
      paginacion: {
        limite: LISTADO_PAGINACION[0],
        actual: 1,
        filtro: undefined,
        orden: "",
        sentido: "ASC",
      },
      preguntaSupervision: {
        id: 0,
        pregunta: "",
      },
      contenidoAlerta: null,
      contenidoControlAcceso: null,
    };
  }

  controlarAccceso = () => {
    if (controlarAccceso() === true) {
      this.setState({
        contenidoControlAcceso: (
          <Redirect
            push
            to={{
              pathname: "/auth/ingreso-sistema",
              search: "",
              state: {},
            }}
          />
        ),
      });
    }
  };

  componentWillUnmount() {
    if (temporizador) {
      clearInterval(temporizador);
    }
    temporizador = null;
  }

  componentDidMount = () => {
    this.traerDatosServidor();
    if (temporizador) {
      clearInterval(temporizador);
      temporizador = null;
    }
    temporizador = setInterval(() => {
      //this.traerDatos();
    }, MILI_SEGUNDOS_REFRESCAR);
  };

  traerDatosServidor = () => {
    this.props.listarSupervisionPregunta(this.resultadoTraerDatosServidor);
  };

  resultadoTraerDatosServidor = (estado, cont) => {};

  cambioTextoPregunta = (pregunta) => {
    var preguntaSupervision = this.state.preguntaSupervision;
    preguntaSupervision.pregunta = pregunta.substring(0, 300);
    this.setState({ preguntaSupervision });
  };

  grabarPregunta = () => {
    var preguntaSupervision = this.state.preguntaSupervision;
    this.props.grabarSupervisionPregunta(
      preguntaSupervision,
      this.respuestaGrabarPregunta
    );
  };

  respuestaGrabarPregunta = (estado, cont) => {
    if (estado === OK) {
      this.borrarTextoPregunta();
    }
  };

  borrarTextoPregunta = () => {
    var preguntaSupervision = {
      id: 0,
      pregunta: "",
    };
    this.setState({ preguntaSupervision });
  };

  editarPregunta = (preguntaSupervision) => {
    this.setState({ preguntaSupervision });
  };

  preguntarBorrarPregunta = (preguntaSupervision) => {
    var contenidoAlerta = (
      <Alerta2Botones
        key="preguntarBorrarPregunta"
        titulo="Borrar el contenido"
        mensaje={
          '¿Desea borar el contenido: "' + preguntaSupervision.pregunta + '" ?'
        }
        contenido={preguntaSupervision}
        textoBotonAceptar="Aceptar"
        textoBotonCancelar="Cancelar"
        eventoBotonAceptar={this.continuarBorrarPregunta}
        eventoBotonCancelar={this.cancelarBorrarPregunta}
        iconoAceptar={<CheckCircleIcon />}
        iconoCancelar={<CancelIcon />}
      />
    );
    this.setState({ contenidoAlerta: contenidoAlerta });
  };

  continuarBorrarPregunta = (preguntaSupervision) => {
    this.props.borrarSupervisionPregunta(
      preguntaSupervision,
      this.respuestaBorrarPregunta
    );
    this.borrarTextoPregunta();
    this.setState({ contenidoAlerta: null });
  };

  respuestaBorrarPregunta = (estado, cont) => {};

  cancelarBorrarPregunta = () => {
    this.setState({ contenidoAlerta: null });
  };

  render() {
    const { classes } = this.props;
    const { preguntaSupervision } = this.state;
    const {
      listadoPreguntasSupervision,
      isActivityIndicatorShown,
    } = this.props.supervisionState;
    return (
      <GridItem xs={12}>
        {isActivityIndicatorShown && (
          <WaitDialog text={this.state.textAlertInfo} />
        )}
        {this.state.contenidoAlerta}

        <Card>
          <CardHeader icon>
            <CardIcon
              style={
                {
                  //cursor: "pointer",
                }
              }
              color="primary"
              onClick={() => {
                //this.props.history.goBack();
              }}
            >
              <b>{""}</b>
              <ListAltIcon />
            </CardIcon>
            <TituloPagina2
              texto="Lista de chequeo para supervisión."
              color="primary"
              classes={classes}
            />
          </CardHeader>

          <CardBody>
            <GridContainer>
              <GridItem xs={12} sm={12} md={12} lg={12}>
                <CustomInput
                  labelText="* Elemento que se verifica"
                  id="pregunta"
                  success
                  formControlProps={{
                    fullWidth: true,
                  }}
                  inputProps={{
                    name: "pregunta",
                    id: "pregunta",
                    value: preguntaSupervision.pregunta,
                    onChange: (event) =>
                      this.cambioTextoPregunta(event.target.value, "nit"),
                    endAdornment: (
                      <InputAdornment position="start">
                        <SaveIcon
                          style={{ color: successColor[0], cursor: "pointer" }}
                          onClick={(e) => {
                            this.grabarPregunta();
                            e.preventDefault();
                          }}
                        />
                        {preguntaSupervision.pregunta.trim() != "" && (
                          <ClearIcon
                            style={{
                              color: successColor[0],
                              cursor: "pointer",
                            }}
                            onClick={(e) => {
                              this.borrarTextoPregunta();
                              e.preventDefault();
                            }}
                          />
                        )}
                      </InputAdornment>
                    ),
                  }}
                />
              </GridItem>
            </GridContainer>
            <GridContainer>
              <GridItem xs={12}>
                <EncabezadoGrid
                  contenidoColumnas={columnasOrdenar2}
                  eventoOrdenar={this.ordenar}
                  ordenarPor={this.state.paginacion.orden}
                  sentidoOrden={this.state.paginacion.sentido}
                  colorFondo={successColor[0]}
                  colorTexto={whiteColor}
                  tamaLetra={"14px"}
                />
              </GridItem>
              <GridItem xs={12}>
                {listadoPreguntasSupervision.map((conte, index) => {
                  return (
                    <GridContainer
                      key={"GridContainer-" + index}
                      container
                      alignItems="center"
                      style={Object.assign(
                        {
                          backgroundColor:
                            index % 2 === 0 ? FILA_BLANCA : FILA_GRIS,
                        },
                        CONFIGURACION_TIPOGRAFIA_14
                      )}
                    >
                      <GridItem xs={1} sm={1} md={1} lg={1}>
                        <Button
                          color="green"
                          simple
                          className={classes.actionButton}
                          key={"edit_" + conte.id}
                          onClick={() => {
                            this.editarPregunta(conte);
                          }}
                        >
                          <Edit />
                        </Button>
                      </GridItem>
                      <GridItem xs={1} sm={1} md={1} lg={1}>
                        <Button
                          color="green"
                          simple
                          className={classes.actionButton}
                          key={"close_" + conte.id}
                          onClick={(e) => this.preguntarBorrarPregunta(conte)}
                        >
                          <DeleteIcon />
                        </Button>
                      </GridItem>
                      <GridItem xs={8} sm={8} md={8} lg={8}>
                        {conte.pregunta}
                      </GridItem>
                    </GridContainer>
                  );
                })}
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
    supervisionState: state.supervisionState,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    grabarSupervisionPregunta: (preguntaSupervision, onSuccess) =>
      dispatch(grabarSupervisionPregunta(preguntaSupervision, onSuccess)),

    borrarSupervisionPregunta: (preguntaSupervision, onSuccess) =>
      dispatch(borrarSupervisionPregunta(preguntaSupervision, onSuccess)),

    addMessage: (content) => dispatch(addMessage(content)),

    listarSupervisionPregunta: (onSuccess) =>
      dispatch(listarSupervisionPregunta(onSuccess)),

    addMessage: (content) => dispatch(addMessage(content)),
  };
};

export default withStyles(extendedTablesStyle)(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(SupervisionPreguntas)
);
