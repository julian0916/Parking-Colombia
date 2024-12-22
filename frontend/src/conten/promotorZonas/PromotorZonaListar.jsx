import withStyles from "@material-ui/core/styles/withStyles";
import DeleteIcon from "@material-ui/icons/Delete";
import DirectionsRunIcon from "@material-ui/icons/DirectionsRun";
import Edit from "@material-ui/icons/Edit";
import RestoreFromTrashIcon from "@material-ui/icons/RestoreFromTrash";
import {
  grayColor,
  successColor,
  whiteColor,
} from "assets/jss/material-dashboard-pro-react.jsx";
import extendedTablesStyle from "assets/jss/material-dashboard-pro-react/views/extendedTablesStyle.jsx";
import Badge from "components/Badge/Badge.jsx";
import Card from "components/Card/Card.jsx";
import CardBody from "components/Card/CardBody.jsx";
import CardHeader from "components/Card/CardHeader.jsx";
import CardIcon from "components/Card/CardIcon.jsx";
import Button from "components/CustomButtons/Button.jsx";
import GridContainer from "components/Grid/GridContainer.jsx";
import GridItem from "components/Grid/GridItem.jsx";
import TablePagination from "components/TablePagination/TablePagination.jsx";
import {
  CONFIGURACION_TIPOGRAFIA_14,
  FILA_BLANCA,
  FILA_GRIS,
  LISTADO_PAGINACION,
} from "config/general/Configuracion";
import CampoBadged from "conten/components/CampoBadged";
import EncabezadoGrid from "conten/components/EncabezadoGrid";
import FiltroGeneral from "conten/components/FiltroGeneral";
import TituloPagina2 from "conten/components/TituloPagina2";
import WaitDialog from "conten/components/WaitDialog.jsx";
import {
  guardarPromotorZona,
  listarPromotorZona,
} from "conten/promotorZonas/PromotorZonaActions";
import React from "react";
import { connect } from "react-redux";
import { Redirect } from "react-router-dom";

const columnasOrdenar2 = [
  {
    campos: [{ id: "", isSorted: false, label: "" }],
    xs: 12,
    sm: 12,
    md: 1,
    lg: 1,
  },
  {
    campos: [{ id: "nombre1", isSorted: true, label: "Promotor" }],
    xs: 12,
    sm: 12,
    md: 2,
    lg: 2,
  },
  {
    campos: [{ id: "z.nombre", isSorted: true, label: "Zona" }],
    xs: 12,
    sm: 12,
    md: 2,
    lg: 2,
  },
  {
    campos: [{ id: "", isSorted: false, label: "Día" }],
    xs: 12,
    sm: 12,
    md: 5,
    lg: 5,
  },
  {
    campos: [
      { id: "c.activo", isSorted: true, label: "Estado" },
      { id: "detalle", isSorted: true, label: "Info" },
    ],
    xs: 12,
    sm: 12,
    md: 2,
    lg: 2,
  },
];

class PromotorZonaListar extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      editarpromotorZona: false,
      promotorZonaEditar: undefined,
      textAlertInfo: "Un momento mientras completamos su solicitud ;)",
      paginacion: {
        limite: LISTADO_PAGINACION[0],
        actual: 1,
        filtro: undefined,
        orden: "nombre1",
        sentido: "ASC",
      },
    };
  }

  componentDidMount = () => {
    this.traerDatosServidor();
  };

  traerDatosServidor = () => {
    this.props.listarPromotorZona(this.state.paginacion);
  };

  cambioPagina = (page) => {
    var paginacionLocal = this.state.paginacion;
    paginacionLocal.actual = page;
    this.setState({ paginacion: paginacionLocal }, () => {
      this.traerDatosServidor();
    });
  };

  cambioLimite = (numFilas) => {
    var paginacionLocal = this.state.paginacion;
    paginacionLocal.limite = numFilas;
    this.setState({ paginacion: paginacionLocal }, () => {
      this.cambioPagina(1);
    });
  };

  cambioFiltro = (filtro) => {
    var paginacionLocal = this.state.paginacion;
    paginacionLocal.filtro = filtro;
    this.setState({ paginacion: paginacionLocal }, () => {
      this.cambioPagina(1);
    });
  };

  ordenar = (event, property) => {
    var sentido =
      this.state.paginacion.sentido === undefined
        ? "ASC"
        : this.state.paginacion.sentido !== "DESC"
        ? "DESC"
        : "ASC";
    var paginacionLocal = this.state.paginacion;
    paginacionLocal.orden = property;
    paginacionLocal.sentido = sentido;
    this.setState({ paginacion: paginacionLocal }, () => {
      this.cambioPagina(1);
    });
  };

  editarPromotorZona = (promotorZonaEditar) => {
    this.setState({ promotorZonaEditar: promotorZonaEditar });
  };

  resultadoBorrar = (resultado, mensaje) => {
    this.traerDatosServidor();
  };

  borrarPromotorZona = (promotorZonaLista) => {
    var promotorZonaBorrar = this.datosPromotorZonaParaServidor(
      promotorZonaLista
    );
    promotorZonaBorrar.activo = false;
    this.props.guardarPromotorZona(promotorZonaBorrar, this.resultadoBorrar);
  };

  activarPromotorZona = (promotorZonaLista) => {
    var promotorZonaBorrar = this.datosPromotorZonaParaServidor(
      promotorZonaLista
    );
    promotorZonaBorrar.activo = true;
    this.props.guardarPromotorZona(promotorZonaBorrar, this.resultadoBorrar);
  };

  datosPromotorZonaParaServidor = (promotorZonaConsulta) => {
    var usuario = {
      id: promotorZonaConsulta.c_id,
      promotor: promotorZonaConsulta.promotor,
      zona: promotorZonaConsulta.zona,
      lunes: promotorZonaConsulta.lunes,
      martes: promotorZonaConsulta.martes,
      miercoles: promotorZonaConsulta.miercoles,
      jueves: promotorZonaConsulta.jueves,
      viernes: promotorZonaConsulta.viernes,
      sabado: promotorZonaConsulta.sabado,
      domingo: promotorZonaConsulta.domingo,
      detalle: promotorZonaConsulta.detalle,
      activo: promotorZonaConsulta.c_activo,
    };
    return usuario;
  };

  render() {
    const { classes } = this.props;
    const {
      paginacion,
      promotorZonas,
      isActivityIndicatorShown,
    } = this.props.promotorZonaState;
    if (this.state.promotorZonaEditar !== undefined) {
      return (
        <Redirect
          push
          to={{
            pathname: "../admin/administrar-promotor-zona",
            search: "?editar=true",
            state: {
              promotorZona: this.datosPromotorZonaParaServidor(
                this.state.promotorZonaEditar
              ),
            },
          }}
        />
      );
    }
    return (
      <GridItem xs={12}>
        {isActivityIndicatorShown && (
          <WaitDialog text={this.state.textAlertInfo} />
        )}
        <Card>
          <CardHeader icon>
            <CardIcon
              style={{
                cursor: "pointer",
              }}
              color="primary"
              onClick={() => {
                this.props.history.push("../admin/administrar-promotor-zona");
              }}
            >
              <b>{"+ Promotor-Zona"}</b>
              <DirectionsRunIcon />
            </CardIcon>
            <TituloPagina2
              texto="Listado de usuarios"
              color="primary"
              classes={classes}
            />
          </CardHeader>

          <CardBody>
            <EncabezadoGrid
              contenidoColumnas={columnasOrdenar2}
              eventoOrdenar={this.ordenar}
              ordenarPor={paginacion.orden}
              sentidoOrden={paginacion.sentido}
              colorFondo={successColor[0]}
              colorTexto={whiteColor}
              tamaLetra={"14px"}
            />
            <FiltroGeneral
              keyFiltro={"FiltroZonas"}
              colorTexto={whiteColor}
              colorFondo={grayColor[21]}
              eventoFiltrar={this.cambioFiltro}
              tamaLetra={"14px"}
              texto={"Buscar"}
              state={this}
            />

            {promotorZonas.map((promotorZona, index) => {
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
                  <GridItem xs={12} sm={12} md={1} lg={1}>
                    {
                      <div>
                        <Button
                          color={promotorZona.c_activo ? "green" : "danger"}
                          simple
                          className={classes.actionButton}
                          key={"edit_" + promotorZona.id}
                          onClick={() => {
                            this.editarPromotorZona(promotorZona);
                          }}
                        >
                          <Edit />
                        </Button>
                        {promotorZona.c_activo && (
                          <Button
                            color="green"
                            simple
                            className={classes.actionButton}
                            key={"close_" + promotorZona.id}
                            onClick={(e) =>
                              this.borrarPromotorZona(promotorZona)
                            }
                          >
                            <DeleteIcon />
                          </Button>
                        )}
                        {!promotorZona.c_activo && (
                          <Button
                            color="danger"
                            simple
                            className={classes.actionButton}
                            key={"close_" + promotorZona.id}
                            onClick={(e) =>
                              this.activarPromotorZona(promotorZona)
                            }
                          >
                            <RestoreFromTrashIcon />
                          </Button>
                        )}
                      </div>
                    }
                  </GridItem>

                  <GridItem xs={12} sm={12} md={2} lg={2}>
                    {promotorZona.nombre1 +
                      " " +
                      promotorZona.nombre2 +
                      " " +
                      promotorZona.apellido1 +
                      " " +
                      promotorZona.apellido2}
                  </GridItem>

                  <GridItem xs={12} sm={12} md={2} lg={2}>
                    {promotorZona.z_nombre}
                  </GridItem>

                  <GridItem xs={12} sm={12} md={5} lg={5}>
                    <CampoBadged
                      mostrar={promotorZona.lunes}
                      color="primary"
                      ancho={"90px"}
                      texto="Lunes"
                    />
                    <CampoBadged
                      mostrar={promotorZona.martes}
                      color="primary"
                      ancho={"90px"}
                      texto="Martes"
                    />
                    <CampoBadged
                      mostrar={promotorZona.miercoles}
                      color="primary"
                      ancho={"90px"}
                      texto="Miércoles"
                    />
                    <CampoBadged
                      mostrar={promotorZona.jueves}
                      color="primary"
                      ancho={"90px"}
                      texto="Jueves"
                    />
                    <CampoBadged
                      mostrar={promotorZona.viernes}
                      color="primary"
                      ancho={"90px"}
                      texto="Viernes"
                    />
                    <CampoBadged
                      mostrar={promotorZona.sabado}
                      color="primary"
                      ancho={"90px"}
                      texto="Sábado"
                    />
                    <CampoBadged
                      mostrar={promotorZona.domingo}
                      color="primary"
                      ancho={"90px"}
                      texto="Domingo"
                    />
                  </GridItem>

                  <GridItem xs={12} sm={12} md={2} lg={2}>
                    <div>
                      {promotorZona.c_activo === true ? (
                        <Badge color="greenColor">Activa</Badge>
                      ) : (
                        <Badge color="danger">Borrada</Badge>
                      )}
                    </div>
                    <div>{promotorZona.detalle.substring(0, 200) + "..."}</div>
                  </GridItem>
                </GridContainer>
              );
            })}

            {paginacion && (
              <TablePagination
                color="success"
                rowsPerPageOptions={LISTADO_PAGINACION}
                pages={Math.ceil(paginacion.total / paginacion.limite)}
                rowsPerPage={paginacion.limite}
                page={paginacion.actual}
                onChangePage={this.cambioPagina}
                onChangeRowsPerPage={this.cambioLimite}
              />
            )}
          </CardBody>
        </Card>
      </GridItem>
    );
  }
}

const mapStateToProps = (state) => {
  return {
    promotorZonaState: state.promotorZonaState,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    listarPromotorZona: (paginacion) =>
      dispatch(listarPromotorZona(paginacion, () => {})),
    guardarPromotorZona: (promotorZona, onSuccess) =>
      dispatch(guardarPromotorZona(promotorZona, onSuccess)),
  };
};

export default withStyles(extendedTablesStyle)(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(PromotorZonaListar)
);
