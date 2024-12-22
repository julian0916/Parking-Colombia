// @material-ui/core components
import withStyles from "@material-ui/core/styles/withStyles";
import AccountBalanceIcon from "@material-ui/icons/AccountBalance";
import DeleteIcon from "@material-ui/icons/Delete";
import Edit from "@material-ui/icons/Edit";
import RestoreFromTrashIcon from "@material-ui/icons/RestoreFromTrash";
import {
  grayColor,
  successColor,
  whiteColor,
} from "assets/jss/material-dashboard-pro-react.jsx";
// material-ui icons
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
  CUENTA_BLOQUEADA,
  FILA_BLANCA,
  FILA_GRIS,
  LISTADO_PAGINACION,
  OK,
} from "config/general/Configuracion";
import AplicarSeguridad from "conten/components/AplicarSeguridad";
import EncabezadoGrid from "conten/components/EncabezadoGrid";
import FiltroGeneral from "conten/components/FiltroGeneral";
import TituloPagina2 from "conten/components/TituloPagina2";
import WaitDialog from "conten/components/WaitDialog.jsx";
import {
  editarCuenta,
  grabarCuenta,
  listarCuenta,
  listarCuentaUsuario,
} from "conten/cuentas/CuentaActions";
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
    campos: [{ id: "id", isSorted: true, label: "Cuenta" }],
    xs: 12,
    sm: 12,
    md: 3,
    lg: 3,
  },
  {
    campos: [{ id: "nombre", isSorted: true, label: "Nombre" }],
    xs: 12,
    sm: 12,
    md: 3,
    lg: 3,
  },
  {
    campos: [{ id: "informacion", isSorted: true, label: "Información" }],
    xs: 12,
    sm: 12,
    md: 3,
    lg: 3,
  },
  {
    campos: [{ id: "activo", isSorted: true, label: "Estado" }],
    xs: 12,
    sm: 12,
    md: 2,
    lg: 2,
  },
];

class CuentaListar extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      editarCuenta: false,
      cuentaEditar: undefined,
      textAlertInfo: "Un momento mientras completamos su solicitud ;)",
      paginacion: {
        limite: LISTADO_PAGINACION[0],
        actual: 1,
        filtro: undefined,
        orden: "nombre",
        sentido: "ASC",
      },
    };
  }

  componentDidMount = () => {
    this.traerDatosServidor();
  };

  traerDatosServidor = () => {
    this.props.listarCuentaUsuario(() => {});
    this.props.listarCuenta(this.state.paginacion);
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

  ordenar = (_event, property) => {
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

  resultadoBorrar = (_resultado, _mensaje) => {
    this.traerDatosServidor();
  };

  borrarCuenta = (cuentaBorrar) => {
    cuentaBorrar.activo = false;
    this.props.grabarCuenta(cuentaBorrar, this.resultadoBorrar);
  };

  activarCuenta = (cuentaBorrar) => {
    cuentaBorrar.activo = true;
    this.props.grabarCuenta(cuentaBorrar, this.resultadoBorrar);
  };

  respuestaEditar = (respuesta) => {
    if (respuesta === OK) {
      this.props.history.push("../admin/administrar-cuenta?editar=true");
    }
  };

  editarCuenta = (cuentaEditar) => {
    this.setState({ cuentaEditar, editarCuenta: true });
  };

  render() {
    const { classes } = this.props;
    const {
      paginacion,
      cuentas,
      isActivityIndicatorShown,
    } = this.props.cuentaState;
    if (this.state.editarCuenta) {
      return (
        <Redirect
          push
          to={{
            pathname: "../admin/administrar-cuenta",
            search: "?editar=true",
            state: { cuenta: this.state.cuentaEditar },
          }}
        />
      );
    }
    return (
      <GridItem xs={12}>
        {isActivityIndicatorShown && (
          <WaitDialog text={this.state.textAlertInfo} />
        )}
        <AplicarSeguridad />
        <Card>
          <CardHeader color="primary" icon>
            <CardIcon
              style={{
                cursor: "pointer",
              }}
              color="primary"
              onClick={() => {
                this.props.history.push("../admin/administrar-cuenta");
              }}
            >
              <b>{"+ Cuenta"}</b>
              <AccountBalanceIcon />
            </CardIcon>
            <TituloPagina2
              texto="Listado de cuentas"
              color="primary"
              classes={classes}
            />
          </CardHeader>
          <CardBody style={{ overflow: "auto" }}>
            <EncabezadoGrid
              contenidoColumnas={columnasOrdenar2}
              eventoOrdenar={this.ordenar}
              ordenarPor={paginacion.orden}
              sentidoOrden={paginacion.sentido}
              colorFondo={successColor[0]}
              colorTexto={whiteColor}
              tamaLetra={"16px"}
            />
            <FiltroGeneral
              keyFiltro={"FiltroZonas"}
              colorTexto={whiteColor}
              colorFondo={grayColor[21]}
              eventoFiltrar={this.cambioFiltro}
              tamaLetra={"16px"}
              texto={"Buscar"}
              state={this}
            />

            {cuentas.map((cuenta, index) => {
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
                    {cuenta.id !== CUENTA_BLOQUEADA && (
                      <div>
                        <Button
                          color={cuenta.activo === true ? "green" : "danger"}
                          simple
                          className={classes.actionButton}
                          key={"edit_" + cuenta.id}
                          onClick={() => {
                            this.editarCuenta(cuenta);
                          }}
                        >
                          <Edit />
                        </Button>
                        {cuenta.activo && (
                          <Button
                            color="green"
                            simple
                            className={classes.actionButton}
                            key={"close_" + cuenta.id}
                            onClick={(_e) => this.borrarCuenta(cuenta)}
                          >
                            <DeleteIcon />
                          </Button>
                        )}
                        {!cuenta.activo && (
                          <Button
                            color="danger"
                            simple
                            className={classes.actionButton}
                            key={"close_" + cuenta.id}
                            onClick={(_e) => this.activarCuenta(cuenta)}
                          >
                            <RestoreFromTrashIcon />
                          </Button>
                        )}
                      </div>
                    )}
                  </GridItem>

                  <GridItem xs={12} sm={12} md={3} lg={3}>
                    {cuenta.id}
                  </GridItem>

                  <GridItem xs={12} sm={12} md={3} lg={3}>
                    {cuenta.nombre}
                  </GridItem>

                  <GridItem xs={12} sm={12} md={3} lg={3}>
                    <div>{cuenta.informacion.substring(0, 200) + "..."}</div>
                  </GridItem>

                  <GridItem xs={12} sm={12} md={2} lg={2}>
                    {cuenta.activo === true ? (
                      <Badge color="greenColor">Activa</Badge>
                    ) : (
                      <Badge color="danger">Borrada</Badge>
                    )}
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
    cuentaState: state.cuentaState,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    listarCuenta: (paginacion) => dispatch(listarCuenta(paginacion, () => {})),
    grabarCuenta: (cuenta, onSuccess) =>
      dispatch(grabarCuenta(cuenta, onSuccess)),
    editarCuenta: (cuenta, onSuccess) =>
      dispatch(editarCuenta(cuenta, onSuccess)),
    listarCuentaUsuario: (onSuccess) =>
      dispatch(listarCuentaUsuario(onSuccess)),
  };
};

export default withStyles(extendedTablesStyle)(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(CuentaListar)
);
