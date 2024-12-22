import FormControl from "@material-ui/core/FormControl";
import TextField from "@material-ui/core/TextField";

import withStyles from "@material-ui/core/styles/withStyles";
import AssessmentIcon from "@material-ui/icons/Assessment";
import extendedTablesStyle from "assets/jss/material-dashboard-pro-react/views/extendedTablesStyle.jsx";
import Card from "components/Card/Card.jsx";
import CardBody from "components/Card/CardBody.jsx";
import CardHeader from "components/Card/CardHeader.jsx";
import CardIcon from "components/Card/CardIcon.jsx";
import GridContainer from "components/Grid/GridContainer";
import GridItem from "components/Grid/GridItem.jsx";
import TituloPagina2 from "conten/components/TituloPagina2";
import WaitDialog from "conten/components/WaitDialog.jsx";
import React from "react";
import { connect } from "react-redux";
import {
  formatDate,
  formatNumberDate,
  formatDateTime,
  formatoDinero,
} from "config/funciones/Comunes";
import CustomSelect from "components/CustomSelect/CustomSelect.jsx";
import { listarUsuario } from "conten/usuarios/UsuarioActions";
import {
  OK,
  TODOS_LOS_REGISTROS,
  PERFIL_PROMOTOR,
  LISTADO_PAGINACION_REPORTES,
  FILA_BLANCA,
  FILA_GRIS,
  CONFIGURACION_TIPOGRAFIA_14,
} from "config/general/Configuracion";
import {
  primaryColor,
  warningColor,
  dangerColor,
  successColor,
  infoColor,
  roseColor,
  grayColor,
  greenColor,
  whiteColor,
  blackColor,
} from "assets/jss/material-dashboard-pro-react.jsx";
import MenuItem from "@material-ui/core/MenuItem";
import { informeSaldoPromotor } from "conten/informes/InformesActions";
import EncabezadoGrid from "conten/components/EncabezadoGrid";
import TablePagination from "components/TablePagination/TablePagination.jsx";
import { ExportSheet } from "react-xlsx-sheet";
import * as XLSX from 'xlsx';

const columnasOrdenar2 = [
  {
    campos: [{ id: "", isSorted: false, label: "Promotor" }],
    xs: 12,
    sm: 12,
    md: 8,
    lg: 8,
  },
  {
    campos: [{ id: "", isSorted: false, label: "Saldo" }],
    xs: 12,
    sm: 12,
    md: 4,
    lg: 4,
  },
];

class InformeSupervision extends React.Component {
  procesarExcel = () => {};

  constructor(props) {
    super(props);
    const fechaActual = formatDate(new Date(Date.now()), "yyyy-mm-dd");
    this.state = {
      textAlertInfo: "Un momento mientras completamos su solicitud ;)",
      paginacionPromotor: {
        limite: TODOS_LOS_REGISTROS,
        actual: 1,
        filtro: PERFIL_PROMOTOR,
        orden: "nombre1, nombre2, apellido1, apellido2",
        sentido: "ASC",
      },
      fechaActual: fechaActual,
      datosConsulta: {
        activos: true,
        fnInicio: formatNumberDate(fechaActual),
        promotor: 0,
        paginacion: {
          limite: LISTADO_PAGINACION_REPORTES[0],
          traerTodo: false,
          actual: 1,
          filtro: null,
          orden: null,
          sentido: null,
        },
      },
      reporte: [],
    };
  }

  componentWillUnmount() {}

  componentDidMount = () => {
    this.traerDatosServidor();
  };

  cambioFechaInicial = (evento) => {
    var datosConsulta = this.state.datosConsulta;
    datosConsulta.fnInicio = formatNumberDate(evento.target.value);
    this.setState({ datosConsulta: datosConsulta }, this.recuperarReporte);
  };

  traerDatosServidor = () => {
    this.props.listarUsuario(this.state.paginacionPromotor);
    this.recuperarReporte();
  };

  recuperarReporte = () => {
    this.props.informeSaldoPromotor(
      this.state.datosConsulta,
      this.respuestaInformeSupervision
    );
  };

  respuestaInformeSupervision = (estado, contenido) => {
    if (estado !== OK) {
      return;
    }
    var datosConsulta = this.state.datosConsulta;
    datosConsulta.paginacion = contenido.paginacion;
    this.setState({ reporte: contenido.reporte, datosConsulta });
  };

  getEstados = (promotores) => {
    var content = [];
    content.push(
      <MenuItem key={"Activos"} value={true}>
        {"Activos"}
      </MenuItem>
    );
    content.push(
      <MenuItem key={"Borrados"} value={false}>
        {"Borrados"}
      </MenuItem>
    );
    return content;
  };

  getPromotores = (promotores) => {
    var content = [];
    content.push(
      <MenuItem key={"promotorTodos"} value={0}>
        {"Todos los promotores"}
      </MenuItem>
    );
    try {
      promotores
        .filter((promotor) => {
          return promotor.c_activo && promotor.perfil + "" === PERFIL_PROMOTOR;
        })
        .forEach((promotor) => {
          try {
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
          } catch (excep) {}
        });
    } catch (ex) {}
    return content;
  };

  cambioContenidoActivos = (activos) => {
    var datosConsulta = this.state.datosConsulta;
    datosConsulta.activos = activos;
    this.setState({ datosConsulta }, this.recuperarReporte);
  };

  cambioContenidoPromotor = (idPromotor) => {
    var datosConsulta = this.state.datosConsulta;
    datosConsulta.promotor = idPromotor;
    this.setState({ datosConsulta }, this.recuperarReporte);
  };

  funcionEnBlanco = () => {};

  getNombresReferenicasXLS = () => {
    var encabezado = [];
    encabezado.push({ title: "Promotor", dataIndex: "promotor" });
    encabezado.push({ title: "Saldo", dataIndex: "saldo" });
    return encabezado;
  };

  getContenidosXLS = (contenido) => {
    var resul = [];
    contenido.map((reg) => {
      var cont = {};
      cont["promotor"] = reg.promotor_nombre;
      cont["saldo"] = reg.saldo;
      resul.push(cont);
    });
    return resul;
  };

  cambioPagina = (page) => {
    var datosConsulta = this.state.datosConsulta;
    datosConsulta.paginacion.actual = page;
    this.setState({ datosConsulta: datosConsulta }, () => {
      this.traerDatosServidor();
    });
  };

  cambioLimite = (numFilas) => {
    var datosConsulta = this.state.datosConsulta;
    datosConsulta.paginacion.limite = numFilas;
    this.setState({ datosConsulta: datosConsulta }, () => {
      this.cambioPagina(1);
    });
  };

  render() {
    const { classes } = this.props;
    const { usuarios } = this.props.usuarioState;
    const { paginacion } = this.state.datosConsulta;
    const { isActivityIndicatorShown } = this.props.informesState;
    var reporte = this.state.reporte;
    const header = this.getNombresReferenicasXLS();
    const dataSource = this.getContenidosXLS(reporte);

    return (
      <GridItem xs={12}>
        {isActivityIndicatorShown && (
          <WaitDialog text={this.state.textAlertInfo} />
        )}
        <Card>
          <CardHeader icon>
            <CardIcon color="primary">
              <ExportSheet
                header={header}
                fileName={`Reporte_saldos`}
                dataSource={dataSource}
                xlsx={XLSX}
              >
                <button
                  style={{
                    cursor: "pointer",
                    backgroundColor: primaryColor[0],
                    color: whiteColor,
                    border: "0px",
                  }}
                >
                  <b>+ Descargar</b>
                </button>
              </ExportSheet>
              <AssessmentIcon />
            </CardIcon>
            <TituloPagina2
              texto="Informe de saldos por promotor"
              color="primary"
              classes={classes}
            />
          </CardHeader>
          <CardBody>
            <GridContainer
              key={"GridContainer-parametros"}
              container
              alignItems="center"
            >
              <GridItem xs={12} sm={12} md={6} lg={6}>
                <CustomSelect
                  key="activos"
                  success
                  labelText="* Estado promotor"
                  id="activos"
                  formControlProps={{
                    fullWidth: true,
                  }}
                  inputProps={{
                    name: "activos",
                    id: "activos",
                    value: this.state.datosConsulta.activos,
                    onChange: (event) =>
                      this.cambioContenidoActivos(event.target.value),
                  }}
                  selectContent={this.getEstados()}
                />
              </GridItem>
              <GridItem xs={12} sm={12} md={6} lg={6}>
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
                    value: this.state.datosConsulta.promotor,
                    onChange: (event) =>
                      this.cambioContenidoPromotor(event.target.value),
                  }}
                  selectContent={this.getPromotores(usuarios)}
                />
              </GridItem>
            </GridContainer>
            <GridContainer>
              <GridItem xs={12} sm={12} md={12} lg={12}>
                <EncabezadoGrid
                  contenidoColumnas={columnasOrdenar2}
                  eventoOrdenar={this.funcionEnBlanco}
                  ordenarPor={paginacion.orden}
                  sentidoOrden={paginacion.sentido}
                  colorFondo={successColor[0]}
                  colorTexto={whiteColor}
                  tamaLetra={"14px"}
                />
              </GridItem>
            </GridContainer>
            {reporte.map((registro, index) => {
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
                  <GridItem xs={12} sm={12} md={8} lg={8}>
                    {registro.promotor_nombre}
                  </GridItem>
                  <GridItem xs={12} sm={12} md={4} lg={4}>
                    <div
                      style={{
                        color:
                          registro.saldo < 0 ? dangerColor[0] : successColor[0],
                      }}
                    >
                      {formatoDinero(registro.saldo)}
                    </div>
                  </GridItem>
                </GridContainer>
              );
            })}
            {paginacion && (
              <TablePagination
                color="success"
                rowsPerPageOptions={LISTADO_PAGINACION_REPORTES}
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
    informesState: state.informesState,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    listarUsuario: (paginacion) =>
      dispatch(listarUsuario(paginacion, () => {})),

    informeSaldoPromotor: (parametros, onSuccess) =>
      dispatch(informeSaldoPromotor(parametros, onSuccess)),
  };
};

export default withStyles(extendedTablesStyle)(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(InformeSupervision)
);
