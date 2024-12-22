import withStyles from "@material-ui/core/styles/withStyles";
import DeleteIcon from "@material-ui/icons/Delete";
import Edit from "@material-ui/icons/Edit";
import PeopleIcon from "@material-ui/icons/People";
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
import AplicarSeguridad from "conten/components/AplicarSeguridad";
import EncabezadoGrid from "conten/components/EncabezadoGrid";
import FiltroGeneral from "conten/components/FiltroGeneral";
import TituloPagina2 from "conten/components/TituloPagina2";
import WaitDialog from "conten/components/WaitDialog.jsx";
import { guardarUsuario, listarUsuario } from "conten/usuarios/UsuarioActions";
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
    campos: [{ id: "p.nombre", isSorted: true, label: "Perfil" }],
    xs: 12,
    sm: 12,
    md: 2,
    lg: 2,
  },
  {
    campos: [
      { id: "identificacion", isSorted: true, label: "Id" },
      { id: "nombre1", isSorted: true, label: "Nombre" },
      { id: "correo", isSorted: true, label: "Correo" },
    ],
    xs: 12,
    sm: 12,
    md: 3,
    lg: 3,
  },
  {
    campos: [
      { id: "celular", isSorted: true, label: "Celular" },
      { id: "fijo", isSorted: true, label: "Fijo" },
      { id: "direccion", isSorted: true, label: "Dirección" },
    ],
    xs: 12,
    sm: 12,
    md: 2,
    lg: 2,
  },
  {
    campos: [{ id: "activo", isSorted: true, label: "Estado" }],
    xs: 12,
    sm: 12,
    md: 2,
    lg: 2,
  },
  {
    campos: [{ id: "mas_informacion", isSorted: true, label: "Info" }],
    xs: 12,
    sm: 12,
    md: 2,
    lg: 2,
  },
];

class UsuarioListar extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      editarusuario: false,
      usuarioEditar: undefined,
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
    this.props.listarUsuario(this.state.paginacion);
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

  datosUsuarioParaServidor = (usuarioConsulta) => {
    var usuario = {
      id: usuarioConsulta.c_id,
      perfil: usuarioConsulta.perfil,
      tipoIdentificacion: usuarioConsulta.tipo_identificacion,
      correo: usuarioConsulta.correo,
      identificacion: usuarioConsulta.identificacion,
      nombre1: usuarioConsulta.nombre1,
      nombre2: usuarioConsulta.nombre2,
      apellido1: usuarioConsulta.apellido1,
      apellido2: usuarioConsulta.apellido2,
      claveHash: usuarioConsulta.clave_hash,
      claveEncriptada: usuarioConsulta.clave_encriptada,
      celular: usuarioConsulta.celular,
      fijo: usuarioConsulta.fijo,
      direccion: usuarioConsulta.direccion,
      masInformacion: usuarioConsulta.mas_informacion,
      activo: usuarioConsulta.c_activo,
    };
    return usuario;
  };

  editarUsuario = (usuarioEditar) => {
    this.setState({ usuarioEditar: usuarioEditar });
  };

  resultadoBorrar = (_resultado, _mensaje) => {
    this.traerDatosServidor();
  };

  borrarusuario = (usuarioLista) => {
    var usuarioBorrar = this.datosUsuarioParaServidor(usuarioLista);
    usuarioBorrar.activo = false;
    this.props.guardarUsuario(usuarioBorrar, this.resultadoBorrar);
  };

  activarusuario = (usuarioLista) => {
    var usuarioBorrar = this.datosUsuarioParaServidor(usuarioLista);
    usuarioBorrar.activo = true;
    this.props.guardarUsuario(usuarioBorrar, this.resultadoBorrar);
  };

  render() {
    const { classes } = this.props;
    const {
      paginacion,
      usuarios,
      isActivityIndicatorShown,
    } = this.props.usuarioState;

    if (this.state.usuarioEditar !== undefined) {
      return (
        <Redirect
          push
          to={{
            pathname: "../admin/administrar-usuario",
            search: "?editar=true",
            state: {
              usuario: this.datosUsuarioParaServidor(this.state.usuarioEditar),
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
                this.props.history.push("../admin/administrar-usuario");
              }}
            >
              <b>{"+ Usuario"}</b>
              <PeopleIcon />
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
              keyFiltro={"FiltroUsuarios"}
              colorTexto={whiteColor}
              colorFondo={grayColor[21]}
              eventoFiltrar={this.cambioFiltro}
              tamaLetra={"14px"}
              texto={"Buscar"}
              state={this}
            />

            {usuarios.map((usuario, index) => {
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
                          color={usuario.c_activo ? "green" : "danger"}
                          simple
                          className={classes.actionButton}
                          key={"edit_" + usuario.id}
                          onClick={() => {
                            this.editarUsuario(usuario);
                          }}
                        >
                          <Edit />
                        </Button>
                        {usuario.c_activo && (
                          <Button
                            color="green"
                            simple
                            className={classes.actionButton}
                            key={"close_" + usuario.id}
                            onClick={(_e) => this.borrarusuario(usuario)}
                          >
                            <DeleteIcon />
                          </Button>
                        )}
                        {!usuario.c_activo && (
                          <Button
                            color="danger"
                            simple
                            className={classes.actionButton}
                            key={"close_" + usuario.id}
                            onClick={(_e) => this.activarusuario(usuario)}
                          >
                            <RestoreFromTrashIcon />
                          </Button>
                        )}
                      </div>
                    }
                  </GridItem>

                  <GridItem
                    xs={12}
                    sm={12}
                    md={2}
                    lg={2}
                    style={{
                      backgroundColor:
                        index % 2 === 0 ? FILA_BLANCA : FILA_GRIS,
                    }}
                  >
                    {usuario.p_nombre}
                  </GridItem>

                  <GridItem
                    xs={12}
                    sm={12}
                    md={3}
                    lg={3}
                    style={{
                      backgroundColor:
                        index % 2 === 0 ? FILA_BLANCA : FILA_GRIS,
                    }}
                  >
                    <div>
                      {usuario.t_nombre_corto + " " + usuario.identificacion}
                    </div>
                    <div>
                      {usuario.nombre1 +
                        " " +
                        usuario.nombre2 +
                        " " +
                        usuario.apellido1 +
                        " " +
                        usuario.apellido2}
                    </div>

                    <div>{usuario.correo}</div>
                  </GridItem>
                  <GridItem
                    xs={12}
                    sm={12}
                    md={2}
                    lg={2}
                    style={{
                      backgroundColor:
                        index % 2 === 0 ? FILA_BLANCA : FILA_GRIS,
                    }}
                  >
                    <div>{usuario.celular}</div>

                    <div>{usuario.fijo}</div>

                    <div>{usuario.direccion}</div>
                  </GridItem>

                  <GridItem
                    xs={12}
                    sm={12}
                    md={2}
                    lg={2}
                    style={{
                      backgroundColor:
                        index % 2 === 0 ? FILA_BLANCA : FILA_GRIS,
                    }}
                  >
                    {usuario.c_activo === true ? (
                      <Badge color="greenColor">Activo</Badge>
                    ) : (
                      <Badge color="danger">Borrado</Badge>
                    )}
                  </GridItem>

                  <GridItem
                    xs={12}
                    sm={12}
                    md={2}
                    lg={2}
                    style={{
                      backgroundColor:
                        index % 2 === 0 ? FILA_BLANCA : FILA_GRIS,
                    }}
                  >
                    {usuario.mas_informacion.substring(0, 200) + "..."}
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
    usuarioState: state.usuarioState,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    listarUsuario: (paginacion) =>
      dispatch(listarUsuario(paginacion, () => {})),
    guardarUsuario: (usuario, onSuccess) =>
      dispatch(guardarUsuario(usuario, onSuccess)),
  };
};

export default withStyles(extendedTablesStyle)(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(UsuarioListar)
);
