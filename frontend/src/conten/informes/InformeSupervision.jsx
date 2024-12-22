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
import { informeSupervision } from "conten/informes/InformesActions";
import EncabezadoGrid from "conten/components/EncabezadoGrid";
import TablePagination from "components/TablePagination/TablePagination.jsx";
import { ExportSheet } from "react-xlsx-sheet";
import * as XLSX from 'xlsx';

const columnasOrdenar2 = [
  {
    campos: [
      { id: "", isSorted: false, label: "Fecha Hora" },
      { id: "", isSorted: false, label: "Zona" },
    ],
    xs: 12,
    sm: 12,
    md: 2,
    lg: 2,
  },
  {
    campos: [{ id: "", isSorted: false, label: "Promotor" }],
    xs: 12,
    sm: 12,
    md: 3,
    lg: 3,
  },
  {
    campos: [{ id: "", isSorted: false, label: "Supervisor" }],
    xs: 12,
    sm: 12,
    md: 3,
    lg: 3,
  },
  {
    campos: [{ id: "", isSorted: false, label: "Firmado" }],
    xs: 12,
    sm: 12,
    md: 1,
    lg: 1,
  },
  {
    campos: [{ id: "", isSorted: false, label: "Información" }],
    xs: 12,
    sm: 12,
    md: 3,
    lg: 3,
  },
];

const columnasOrdenar3 = [
  {
    campos: [{ id: "", isSorted: false, label: "Cumple" }],
    xs: 12,
    sm: 12,
    md: 2,
    lg: 2,
  },
  {
    campos: [{ id: "", isSorted: false, label: "Pregunta" }],
    xs: 12,
    sm: 12,
    md: 7,
    lg: 7,
  },
  {
    campos: [{ id: "", isSorted: false, label: "Valor" }],
    xs: 12,
    sm: 12,
    md: 3,
    lg: 3,
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
        fnInicio: formatNumberDate(fechaActual),
        fnFin: formatNumberDate(fechaActual),
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
      ultimoContenido: undefined,
    };
  }

  componentWillUnmount() {}

  componentDidMount = () => {
    this.procesarExcel();
    this.traerDatosServidor();
  };

  cambioFechaInicial = (evento) => {
    const datosConsulta = { ...this.state.datosConsulta };
    datosConsulta.fnInicio = formatNumberDate(evento.target.value);
    this.setState({ datosConsulta }, this.recuperarReporte);
  };

  cambioFechaFinal = (evento) => {
    const datosConsulta = { ...this.state.datosConsulta };
    datosConsulta.fnFin = formatNumberDate(evento.target.value);
    this.setState({ datosConsulta }, this.recuperarReporte);
};

  traerDatosServidor = () => {
    this.props.listarUsuario(this.state.paginacionPromotor);
    this.recuperarReporte();
  };

  recuperarReporte = () => {
    this.props.informeSupervision(
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

  cambioContenidoPromotor = (idPromotor) => {
    var datosConsulta = this.state.datosConsulta;
    datosConsulta.promotor = idPromotor;
    this.setState({ datosConsulta }, this.recuperarReporte);
  };

  generarEstructuraImprimir = () => {
    var reporte = this.state.reporte;
    var registros = {};
    var resultado = [];
    reporte.forEach((reg) => {
      var encabezado = {
        id: reg.id,
        fecha_hora: formatDateTime(
          new Date(reg.fecha_hora),
          "yyyy-mm-ddTHH:MM"
        ).replace("T", " "),
        supervisor_nombre: reg.supervisor_nombre,
        informacion: reg.informacion,
        es_firmado: reg.es_firmado !== true ? "NO" : "SI",
        promotor_nombre: reg.promotor_nombre,
        zona: reg.zona,
        preguntas: [],
      };
      if (registros[reg.id] === undefined) {
        registros[reg.id] = encabezado;
        resultado.push(registros[reg.id]);
      }
      var fila = {
        contenido: reg.contenido,
        valor: reg.valor === undefined || reg.valor === null ? "" : reg.valor,
        cumple: reg.cumple !== true ? "NO" : "SI",
      };
      registros[reg.id].preguntas.push(fila);
    });
    return resultado;
  };

  funcionEnBlanco = () => {};

  renderFila = (pregunta) => {
    var res = [];
    pregunta.map((contenido, index) => {
      var colorFila = index % 2 == 0 ? FILA_BLANCA : FILA_GRIS;
      res.push(
        <>
          <GridItem
            xs={12}
            sm={12}
            md={2}
            lg={2}
            style={{ backgroundColor: colorFila }}
          >
            {contenido.cumple}
          </GridItem>
          <GridItem
            xs={12}
            sm={12}
            md={7}
            lg={7}
            style={{ backgroundColor: colorFila }}
          >
            {contenido.contenido}
          </GridItem>
          <GridItem
            xs={12}
            sm={12}
            md={3}
            lg={3}
            style={{ backgroundColor: colorFila }}
          >
            {(contenido.valor===""?"--":contenido.valor)}
          </GridItem>
        </>
      );
    });
    return res;
  };

  getListadoPreguntas = () => {
    var reporte = this.state.reporte;
    var preguntas = {};
    var resultado = [];
    reporte.forEach((reg) => {
      if (preguntas[reg.contenido] === undefined) {
        preguntas[reg.contenido] = reg.contenido;
        resultado.push(reg.contenido);
      }
    });
    return resultado;
  };

  getNombresReferenicasXLS = () => {
    var preguntas = this.getListadoPreguntas();
    var encabezado = [];
    encabezado.push({ title: "Zona", dataIndex: "zona" });
    encabezado.push({ title: "Promotor", dataIndex: "promotor" });
    encabezado.push({ title: "Fecha y hora", dataIndex: "fecha" });
    for (var pos = 0; pos < preguntas.length; pos++) {
      encabezado.push({ title: preguntas[pos], dataIndex: preguntas[pos] });
    }
    encabezado.push({ title: "Firma promotor", dataIndex: "firma promotor" });
    encabezado.push({ title: "Supervisor", dataIndex: "supervisor" });
    encabezado.push({
      title: "Observaciones generales",
      dataIndex: "observaciones",
    });
    return encabezado;
  };

  getContenidosXLS = (contenido) => {
    var resul = [];
    contenido.map((reg) => {
      var cont = {};
      cont["zona"] = reg.zona;
      cont["fecha"] = reg.fecha_hora;
      cont["promotor"] = reg.promotor_nombre;
      cont["supervisor"] = reg.supervisor_nombre;
      cont["observaciones"] = reg.informacion;
      cont["firma promotor"] = reg.es_firmado;
      reg.preguntas.map((preg) => {
        cont[preg.contenido] = preg.cumple + "\r\n" + preg.valor;
      });
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
    var reporte = this.generarEstructuraImprimir(this.state.reporte);

    return (
      <GridItem xs={12}>
        {isActivityIndicatorShown && (
          <WaitDialog text={this.state.textAlertInfo} />
        )}
        <Card>
          <CardHeader icon>
            <CardIcon color="primary">
              <ExportSheet
                header={this.getNombresReferenicasXLS()}
                fileName={`Reporte_Supervision`}
                dataSource={this.getContenidosXLS(reporte)}
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
              texto="Informe de supervisión"
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
              <FormControl fullWidth>
                  <TextField
                    id="fechaInicial"
                    label="Fecha inicio"
                    type="date"
                    defaultValue={this.state.fechaActual}
                    className={classes.textField}
                    onChange={this.cambioFechaInicial}
                    InputLabelProps={{
                      shrink: true,
                    }}
                  />
                </FormControl>
              </GridItem>
              <GridItem xs={12} sm={12} md={6} lg={6}>
                <FormControl fullWidth>
                  <TextField
                    id="fechaFinal"
                    label="Fecha fin"
                    type="date"
                    defaultValue={this.state.fechaActual}
                    className={classes.textField}
                    onChange={this.cambioFechaFinal}
                    InputLabelProps={{
                      shrink: true,
                    }}
                  />
                </FormControl>
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
            {reporte.map((registro, index) => {
              return (
                <GridContainer
                  key={"GridContainer-" + index}
                  container
                  alignItems="center"
                  style={Object.assign(
                    {
                      backgroundColor: FILA_BLANCA,
                    },
                    CONFIGURACION_TIPOGRAFIA_14
                  )}
                >
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
                  <GridItem xs={12} sm={12} md={2} lg={2}>
                    <div>{registro.fecha_hora}</div>
                    <div>{registro.zona}</div>
                  </GridItem>
                  <GridItem xs={12} sm={12} md={3} lg={3}>
                    {registro.promotor_nombre}
                  </GridItem>
                  <GridItem xs={12} sm={12} md={3} lg={3}>
                    {registro.supervisor_nombre}
                  </GridItem>
                  <GridItem xs={12} sm={12} md={1} lg={1}>
                    {registro.es_firmado}
                  </GridItem>
                  <GridItem xs={12} sm={12} md={3} lg={3}>
                    {registro.informacion}
                  </GridItem>

                  <GridItem xs={12} sm={12} md={12} lg={12}>
                    <EncabezadoGrid
                      contenidoColumnas={columnasOrdenar3}
                      eventoOrdenar={this.funcionEnBlanco}
                      ordenarPor={paginacion.orden}
                      sentidoOrden={paginacion.sentido}
                      colorFondo={grayColor[21]}
                      colorTexto={blackColor}
                      tamaLetra={"14px"}
                    />
                  </GridItem>
                  {this.renderFila(registro.preguntas)
                  }
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

    informeSupervision: (parametros, onSuccess) =>
      dispatch(informeSupervision(parametros, onSuccess)),
  };
};

export default withStyles(extendedTablesStyle)(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(InformeSupervision)
);
