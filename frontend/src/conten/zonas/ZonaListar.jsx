import withStyles from "@material-ui/core/styles/withStyles";
import DeleteIcon from "@material-ui/icons/Delete";
import DriveEtaIcon from "@material-ui/icons/DriveEta";
import Edit from "@material-ui/icons/Edit";
import MapIcon from "@material-ui/icons/Map";
import MotorcycleIcon from "@material-ui/icons/Motorcycle";
import RestoreFromTrashIcon from "@material-ui/icons/RestoreFromTrash";
import {
  grayColor,
  infoColor,
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
import { guardarZona, listarZona } from "conten/zonas/ZonaActions";
import React from "react";
import { connect } from "react-redux";
import { Redirect } from "react-router-dom";
import AplicarSeguridad from "conten/components/AplicarSeguridad";

const columnasOrdenar2 = [
  {
    campos: [{ id: "", isSorted: false, label: "" }],
    xs: 12,
    sm: 12,
    md: 1,
    lg: 1,
  },
  {
    campos: [
      { id: "id", isSorted: true, label: "Id" },
      { id: "nombre", isSorted: true, label: "Nombre" },
      { id: "direccion", isSorted: true, label: "Dirección" },
    ],
    xs: 12,
    sm: 12,
    md: 2,
    lg: 2,
  },
  {
    campos: [
      { id: "", isSorted: true, label: "Gracia" },
    ],
    xs: 12,
    sm: 12,
    md: 1,
    lg: 1,
  },
  {
    campos: [{ id: "celdas_carro", isSorted: true, label: "Celdas" }],
    xs: 12,
    sm: 12,
    md: 2,
    lg: 2,
  },
  {
    campos: [{ id: "valor_hora_carro", isSorted: true, label: "Valor" }],
    xs: 12,
    sm: 12,
    md: 2,
    lg: 2,
  },
  {
    campos: [{ id: "", isSorted: false, label: "Horario" }],
    xs: 12,
    sm: 12,
    md: 2,
    lg: 2,
  },
  {
    campos: [
      { id: "activo", isSorted: true, label: "Estado" },
      { id: "observacion", isSorted: true, label: "Info" },
    ],
    xs: 12,
    sm: 12,
    md: 2,
    lg: 2,
  },
];

class ZonaListar extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      editarzona: false,
      zonaEditar: undefined,
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
    this.props.listarZona(this.state.paginacion);
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

  editarZona = (zonaEditar) => {
    this.setState({ zonaEditar: zonaEditar });
  };

  resultadoBorrar = (resultado, mensaje) => {
    this.traerDatosServidor();
  };

  borrarZona = (zonaLista) => {
    var zonaBorrar = this.datosZonaParaServidor(zonaLista);
    zonaBorrar.activo = false;
    this.props.guardarZona(zonaBorrar, this.resultadoBorrar);
  };

  activarZona = (zonaLista) => {
    var zonaBorrar = this.datosZonaParaServidor(zonaLista);
    zonaBorrar.activo = true;
    this.props.guardarZona(zonaBorrar, this.resultadoBorrar);
  };

  datosZonaParaServidor = (zonaConsulta) => {
    var usuario = {
      id: zonaConsulta.id,
      nombre: zonaConsulta.nombre,
      direccion: zonaConsulta.direccion,
      observacion: zonaConsulta.observacion,
      latitud: zonaConsulta.latitud,
      longitud: zonaConsulta.longitud,
      celdasCarro: zonaConsulta.celdas_carro,
      celdasMoto: zonaConsulta.celdas_moto,
      valorHoraCarro: zonaConsulta.valor_hora_carro,
      valorHoraMoto: zonaConsulta.valor_hora_moto,
      entreSemanaInicia: zonaConsulta.entre_semana_inicia,
      entreSemanaTermina: zonaConsulta.entre_semana_termina,
      finSemanaInicia: zonaConsulta.fin_semana_inicia,
      finSemanaTermina: zonaConsulta.fin_semana_termina,
      minutosGracia: zonaConsulta.minutos_gracia,
      minutosParaNuevaGracia: zonaConsulta.minutos_para_nueva_gracia,
      activo: zonaConsulta.activo,
    };
    return usuario;
  };

  getHoraMinuto = (tiempo) => {
    var local = tiempo.split(":");
    return local[0] + ":" + local[1];
  };

  render() {
    const anchoBadged = "60px";
    const { classes } = this.props;
    const {
      paginacion,
      zonas,
      isActivityIndicatorShown,
    } = this.props.zonaState;

    if (this.state.zonaEditar !== undefined) {
      return (
        <Redirect
          push
          to={{
            pathname: "../admin/administrar-zona",
            search: "?editar=true",
            state: {
              zona: this.datosZonaParaServidor(this.state.zonaEditar),
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
        <AplicarSeguridad />
        <Card>
        <CardHeader icon>
            <CardIcon
              style={{
                cursor: "pointer",
              }}
              color="primary"
              onClick={() => {
                this.props.history.push("../admin/administrar-zona");
              }}
            >
              <b>{"+ Zona"}</b>
              <MapIcon />
            </CardIcon>
            <TituloPagina2
              texto="Listado de zonas"
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

            {zonas.map((zona, index) => {
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
                          color={zona.activo?"green":"danger"}
                          simple
                          className={classes.actionButton}
                          key={"edit_" + zona.id}
                          onClick={() => {
                            this.editarZona(zona);
                          }}
                        >
                          <Edit />
                        </Button>
                        {zona.activo && (
                          <Button
                            color="green"
                            simple
                            className={classes.actionButton}
                            key={"close_" + zona.id}
                            onClick={(e) => this.borrarZona(zona)}
                          >
                            <DeleteIcon />
                          </Button>
                        )}
                        {!zona.activo && (
                          <Button
                            color="danger"
                            simple
                            className={classes.actionButton}
                            key={"close_" + zona.id}
                            onClick={(e) => this.activarZona(zona)}
                          >
                            <RestoreFromTrashIcon />
                          </Button>
                        )}
                      </div>
                    }
                  </GridItem>

                  <GridItem xs={12} sm={12} md={2} lg={2}>
                    <div>{zona.id}</div>
                    <br/>
                    <div>{zona.nombre}</div>
                    <br/>
                    <div>{zona.direccion}</div>
                    <br/>
                    <div>
                      {zona.latitud !== undefined && zona.latitud !== "" && (
                        <CampoBadged
                          mostrar={true}
                          color="primary"
                          ancho={"120px"}
                          texto={"la: " + zona.latitud}
                          icono={<></>}
                        />
                      )}
                    </div>
                    <div style={{ marginTop: "3px" }}>
                      {zona.longitud !== undefined && zona.longitud !== "" && (
                        <CampoBadged
                          mostrar={true}
                          color="primary"
                          ancho={"120px"}
                          texto={"lo: " + zona.longitud}
                          icono={<></>}
                        />
                      )}
                    </div>
                  </GridItem>

                  <GridItem xs={12} sm={12} md={1} lg={1}>
                  {zona.minutos_gracia}{"' gracia"}<br/><br/>
                  {zona.minutos_para_nueva_gracia}{"' espera"}
                  </GridItem>      

                  <GridItem xs={12} sm={12} md={2} lg={2}>
                    <div>
                      <CampoBadged
                        mostrar={true}
                        color="primary"
                        ancho={anchoBadged}
                        texto={zona.celdas_carro}
                        icono={
                          <>
                            <br />
                            <DriveEtaIcon />
                          </>
                        }
                      />
                    </div>
                    <div style={{ marginTop: "2px" }}>
                      <CampoBadged
                        mostrar={true}
                        color="primary"
                        ancho={anchoBadged}
                        texto={zona.celdas_moto}
                        icono={
                          <>
                            <br />
                            <MotorcycleIcon />
                          </>
                        }
                      />
                    </div>
                  </GridItem>

                  <GridItem xs={12} sm={12} md={2} lg={2}>
                    <div>
                      <CampoBadged
                        mostrar={true}
                        color="primary"
                        ancho={anchoBadged}
                        texto={"$" + zona.valor_hora_carro}
                        icono={
                          <>
                            <br />
                            <DriveEtaIcon />
                          </>
                        }
                      />
                    </div>
                    <div style={{ marginTop: "2px" }}>
                      <CampoBadged
                        mostrar={true}
                        color="primary"
                        ancho={anchoBadged}
                        texto={"$" + zona.valor_hora_moto}
                        icono={
                          <>
                            <br />
                            <MotorcycleIcon />
                          </>
                        }
                      />
                    </div>
                  </GridItem>

                  <GridItem xs={12} sm={12} md={2} lg={2}>
                    <div style={{ marginTop: "2px" }}>
                      <CampoBadged
                        mostrar={true}
                        color="primary"
                        ancho={"120px"}
                        texto={
                          "Inicia semana: " +
                          this.getHoraMinuto(zona.entre_semana_inicia)
                        }
                        icono={<></>}
                      />
                    </div>
                    <div style={{ marginTop: "2px" }}>
                      <CampoBadged
                        mostrar={true}
                        color="primary"
                        ancho={"120px"}
                        texto={
                          "Termina semana " +
                          this.getHoraMinuto(zona.entre_semana_termina)
                        }
                        icono={<></>}
                      />
                    </div>
                    <div style={{ marginTop: "2px" }}>
                      <CampoBadged
                        mostrar={true}
                        color="success"
                        ancho={"120px"}
                        texto={
                          "Inicia finde: " +
                          this.getHoraMinuto(zona.fin_semana_inicia)
                        }
                        icono={<></>}
                      />
                    </div>
                    <div style={{ marginTop: "2px" }}>
                      <CampoBadged
                        mostrar={true}
                        color="success"
                        ancho={"120px"}
                        texto={
                          "Termina finde " +
                          this.getHoraMinuto(zona.fin_semana_termina)
                        }
                        icono={<></>}
                      />
                    </div>
                  </GridItem>

                  <GridItem xs={12} sm={12} md={2} lg={2}>
                    <br/>
                    <div>
                      {zona.activo === true ? (
                        <Badge color="greenColor">Activa</Badge>
                      ) : (
                        <Badge color="danger">Borrada</Badge>
                      )}
                    </div>
                    <div>{zona.observacion.substring(0, 200) + "..."} </div>
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
    zonaState: state.zonaState,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    listarZona: (paginacion) => dispatch(listarZona(paginacion, () => {})),
    guardarZona: (zona, onSuccess) => dispatch(guardarZona(zona, onSuccess)),
  };
};

export default withStyles(extendedTablesStyle)(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(ZonaListar)
);
