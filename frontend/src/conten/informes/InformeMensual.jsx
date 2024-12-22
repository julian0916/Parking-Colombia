import FormControl from "@material-ui/core/FormControl";
import TextField from "@material-ui/core/TextField";
import Checkbox from "@material-ui/core/Checkbox";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Check from "@material-ui/icons/Check";

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
import { informeMensual } from "conten/informes/InformesActions";
import EncabezadoGrid from "conten/components/EncabezadoGrid";
import TablePagination from "components/TablePagination/TablePagination.jsx";
import { ExportSheet } from "react-xlsx-sheet";
import * as XLSX from 'xlsx';

var columnasOrdenar2 = [
  {
    campos: [{ id: "", isSorted: false, label: "" }],
    xs: 12,
    sm: 12,
    md: 3,
    lg: 3,
  },
  {
    campos: [{ id: "", isSorted: false, label: "Carros" }],
    xs: 12,
    sm: 12,
    md: 2,
    lg: 2,
  },
  {
    campos: [{ id: "", isSorted: false, label: " %" }],
    xs: 12,
    sm: 12,
    md: 1,
    lg: 1,
  },
  {
    campos: [{ id: "", isSorted: false, label: "Motos" }],
    xs: 12,
    sm: 12,
    md: 2,
    lg: 2,
  },
  {
    campos: [{ id: "", isSorted: false, label: " %" }],
    xs: 12,
    sm: 12,
    md: 1,
    lg: 1,
  },
  {
    campos: [{ id: "", isSorted: false, label: "Total" }],
    xs: 12,
    sm: 12,
    md: 2,
    lg: 2,
  },
  {
    campos: [{ id: "", isSorted: false, label: "%" }],
    xs: 12,
    sm: 12,
    md: 1,
    lg: 1,
  },
];

class InformeMensual extends React.Component {
  constructor(props) {
    super(props);
    const fechaActual = new Date(Date.now());
    this.state = {
      yearActual: fechaActual.getFullYear(),
      textAlertInfo: "Un momento mientras completamos su solicitud.",
      fechaActual: fechaActual,
      datosConsulta: {
        year: fechaActual.getFullYear(),
        enero: this.verificarMes(fechaActual, 1),
        febrero: this.verificarMes(fechaActual, 2),
        marzo: this.verificarMes(fechaActual, 3),
        abril: this.verificarMes(fechaActual, 4),
        mayo: this.verificarMes(fechaActual, 5),
        junio: this.verificarMes(fechaActual, 6),
        julio: this.verificarMes(fechaActual, 7),
        agosto: this.verificarMes(fechaActual, 8),
        septiembre: this.verificarMes(fechaActual, 9),
        octubre: this.verificarMes(fechaActual, 10),
        noviembre: this.verificarMes(fechaActual, 11),
        diciembre: this.verificarMes(fechaActual, 12),
        paginacion: {
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

  verificarMes = (fecha, mes) => {
    var result = false;
    try {
      result = fecha.getMonth() === mes - 1;
    } catch (excep) {}
    return result;
  };

  traerDatosServidor = () => {
    this.recuperarReporte();
  };

  recuperarReporte = () => {
    this.props.informeMensual(
      this.state.datosConsulta,
      this.respuestaInformeMensual
    );
  };

  respuestaInformeMensual = (estado, contenido) => {
    if (estado !== OK) {
      return;
    }
    var datosConsulta = this.state.datosConsulta;
    this.setState({ reporte: contenido.reporte, datosConsulta });
  };

  getYears = () => {
    var content = [];
    var inicial = 2020;
    var actual = this.state.yearActual;
    for (var year = actual; year >= inicial; year--) {
      content.push(
        <MenuItem key={year} value={year}>
          {year}
        </MenuItem>
      );
    }
    return content;
  };

  cambioContenidoYear = (year) => {
    var datosConsulta = this.state.datosConsulta;
    datosConsulta.year = year;
    this.setState({ datosConsulta }, this.recuperarReporte);
  };

  funcionEnBlanco = () => {};

  getNombresReferenicasXLS = () => {
    var encabezado = [];
    encabezado.push({ title: "Mes", dataIndex: "mes" });
    encabezado.push({ title: "Estacionamiento Carros", dataIndex: "c1" });
    encabezado.push({ title: "Estacionamiento Carros %", dataIndex: "c2" });
    encabezado.push({ title: "Estacionamiento Motos", dataIndex: "c3" });
    encabezado.push({ title: "Estacionamiento Motos %", dataIndex: "c4" });
    encabezado.push({ title: "Estacionamiento Total", dataIndex: "c5" });
    encabezado.push({ title: "Estacionamiento Carros $", dataIndex: "c6" });
    encabezado.push({ title: "Estacionamiento Carros $ %", dataIndex: "c7" });
    encabezado.push({ title: "Estacionamiento Motos $", dataIndex: "c8" });
    encabezado.push({ title: "Estacionamiento Motos $ %", dataIndex: "c9" });
    encabezado.push({ title: "Estacionamiento Total $", dataIndex: "c10" });
    encabezado.push({ title: "Estacionamiento Total $ %", dataIndex: "c11" });
    encabezado.push({
      title: "Estacionamiento Carros sin $",
      dataIndex: "c12",
    });
    encabezado.push({
      title: "Estacionamiento Carros sin $ %",
      dataIndex: "c13",
    });
    encabezado.push({ title: "Estacionamiento Motos sin $", dataIndex: "c14" });
    encabezado.push({
      title: "Estacionamiento Motos sin $ %",
      dataIndex: "c15",
    });
    encabezado.push({ title: "Estacionamiento Total sin $", dataIndex: "c16" });
    encabezado.push({
      title: "Estacionamiento Total sin $ %",
      dataIndex: "c17",
    });
    encabezado.push({ title: "Recaudo carro", dataIndex: "c18" });
    encabezado.push({ title: "Recaudo Moto", dataIndex: "c19" });
    encabezado.push({ title: "Recaudo Total", dataIndex: "c20" });
    encabezado.push({ title: "Recaudo Total %", dataIndex: "c21" });
    encabezado.push({ title: "Cartera carro", dataIndex: "c22" });
    encabezado.push({ title: "Cartera Moto", dataIndex: "c23" });
    encabezado.push({ title: "Cartera Total", dataIndex: "c24" });
    encabezado.push({ title: "Cartera Total %", dataIndex: "c25" });
    encabezado.push({ title: "Total carro", dataIndex: "c26" });
    encabezado.push({ title: "Total Moto", dataIndex: "c27" });
    encabezado.push({ title: "Total", dataIndex: "c28" });

    return encabezado;
  };

  ponerEnFormatoSinPorcentaje = (valor) => {
    try {
      if (valor === null) {
        valor = 0.0;
      }
      if (valor === undefined) {
        valor = 0.0;
      }
      return (valor.toFixed(2) + "%").replace(".", ",");
    } catch (exp) {
      return "0.0%";
    }

    //return (valor/100).toFixed(4)+"%";
  };

  getContenidosXLS = (contenido) => {
    var resul = [];
    contenido.map((reg) => {
      var cont = {};
      cont["mes"] = reg.nombre_mes;
      cont["c1"] = reg.estacionamiento_carros;
      cont["c2"] = this.ponerEnFormatoSinPorcentaje(
        reg.porce_estacionamiento_carros
      );
      cont["c3"] = reg.estacionamiento_motos;
      cont["c4"] = this.ponerEnFormatoSinPorcentaje(
        reg.porce_estacionamiento_motos
      );
      cont["c5"] = reg.total_estacionammientos;
      cont["c6"] = reg.estacionamiento_carros_con_cobro;
      cont["c7"] = this.ponerEnFormatoSinPorcentaje(
        reg.porce_estacionamiento_carros_con_cobro
      );
      cont["c8"] = reg.estacionamiento_motos_con_cobro;
      cont["c9"] = this.ponerEnFormatoSinPorcentaje(
        reg.porce_estacionamiento_motos_con_cobro
      );
      cont["c10"] = reg.total_estacionammientos_con_cobro;
      cont["c11"] = this.ponerEnFormatoSinPorcentaje(
        reg.porce_estacionamiento_carros_con_cobro +
          reg.porce_estacionamiento_motos_con_cobro
      );
      cont["c12"] = reg.estacionamiento_carros_sin_cobro;
      cont["c13"] = this.ponerEnFormatoSinPorcentaje(
        reg.porce_estacionamiento_carros_sin_cobro
      );
      cont["c14"] = reg.estacionamiento_motos_sin_cobro;
      cont["c15"] = this.ponerEnFormatoSinPorcentaje(
        reg.porce_estacionamiento_motos_sin_cobro
      );
      cont["c16"] = reg.total_estacionammientos_sin_cobro;
      cont["c17"] = this.ponerEnFormatoSinPorcentaje(
        reg.porce_estacionamiento_carros_sin_cobro +
          reg.porce_estacionamiento_motos_sin_cobro
      );
      cont["c18"] = reg.recaudo_carros;
      cont["c19"] = reg.recaudo_motos;
      cont["c20"] = reg.recaudo_total;
      cont["c21"] = this.ponerEnFormatoSinPorcentaje(reg.porce_recaudo_total);
      cont["c22"] = reg.cartera_carros;
      cont["c23"] = reg.cartera_motos;
      cont["c24"] = reg.cartera_total;
      cont["c25"] = this.ponerEnFormatoSinPorcentaje(reg.porce_cartera_total);
      cont["c26"] = reg.cobro_carros;
      cont["c27"] = reg.cobro_motos;
      cont["c28"] = reg.cobro_total;
      // cont["saldo"] = reg.saldo;
      resul.push(cont);
    });
    return resul;
  };

  cambioContenidoMes = (valor, campo) => {
    var datosConsulta = this.state.datosConsulta;
    datosConsulta[campo] = valor;
    this.setState({ datosConsulta }, this.recuperarReporte);
    return true;
  };

  renderCheckMes = (campo, classes) => {
    return (
      <GridItem xs={12} sm={3} md={2} lg={2}>
        <FormControlLabel
          style={{ margin: "0px 0px 0px -10px" }}
          control={
            <Checkbox
              checkedIcon={<Check className={classes.checkedIcon} />}
              icon={<Check className={classes.uncheckedIcon} />}
              classes={{
                checked: classes.checked,
              }}
              checked={this.state.datosConsulta[campo]}
              onChange={(event) =>
                this.cambioContenidoMes(!this.state.datosConsulta[campo], campo)
              }
              disableRipple
            />
          }
          label={campo}
        />
      </GridItem>
    );
  };

  renderFila = (
    columna1,
    columna2,
    columna3,
    columna4,
    columna5,
    columna6,
    columna7,
    color = FILA_BLANCA
  ) => {
    return (
      <GridItem
        xs={12}
        sm={12}
        md={12}
        lg={12}
        style={{ backgroundColor: color }}
      >
        <GridContainer>
          <GridItem xs={12} sm={12} md={3} lg={3}>
            {columna1}
          </GridItem>
          <GridItem xs={12} sm={12} md={2} lg={2}>
            {columna2}
          </GridItem>
          <GridItem xs={12} sm={12} md={1} lg={1}>
            {columna3}
          </GridItem>
          <GridItem xs={12} sm={12} md={2} lg={2}>
            {columna4}
          </GridItem>
          <GridItem xs={12} sm={12} md={1} lg={1}>
            {columna5}
          </GridItem>
          <GridItem xs={12} sm={12} md={2} lg={2}>
            {columna6}
          </GridItem>
          <GridItem xs={12} sm={12} md={1} lg={1}>
            {columna7}
          </GridItem>
        </GridContainer>
      </GridItem>
    );
  };

  render() {
    const { classes } = this.props;
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
                fileName={`Reporte_mensual`}
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
              texto="Informe mensual"
              color="primary"
              classes={classes}
            />
          </CardHeader>
          <CardBody>
            <GridContainer
              key={"GridContainer-parametros"}
              container
              alignItems="center"
              style={CONFIGURACION_TIPOGRAFIA_14}
            >
              {this.renderCheckMes("enero", classes)}
              {this.renderCheckMes("febrero", classes)}
              {this.renderCheckMes("marzo", classes)}
              {this.renderCheckMes("abril", classes)}
              {this.renderCheckMes("mayo", classes)}
              {this.renderCheckMes("junio", classes)}
              {this.renderCheckMes("julio", classes)}
              {this.renderCheckMes("agosto", classes)}
              {this.renderCheckMes("septiembre", classes)}
              {this.renderCheckMes("octubre", classes)}
              {this.renderCheckMes("noviembre", classes)}
              {this.renderCheckMes("diciembre", classes)}

              <GridItem xs={12} sm={12} md={6} lg={6}>
                <CustomSelect
                  key="year"
                  success
                  labelText="* Año"
                  id="year"
                  formControlProps={{
                    fullWidth: true,
                  }}
                  inputProps={{
                    name: "year",
                    id: "year",
                    value: this.state.datosConsulta.year,
                    onChange: (event) =>
                      this.cambioContenidoYear(event.target.value),
                  }}
                  selectContent={this.getYears()}
                />
              </GridItem>
            </GridContainer>

            {reporte.map((registro, index) => {
              var contenidoEncabezado = JSON.stringify(columnasOrdenar2);
              contenidoEncabezado = JSON.parse(contenidoEncabezado);
              contenidoEncabezado[0].campos[0].label = registro.nombre_mes;
              return (
                <>
                  <EncabezadoGrid
                    contenidoColumnas={contenidoEncabezado}
                    eventoOrdenar={this.funcionEnBlanco}
                    ordenarPor={paginacion.orden}
                    sentidoOrden={paginacion.sentido}
                    colorFondo={successColor[0]}
                    colorTexto={whiteColor}
                    tamaLetra={"14px"}
                  />
                  <GridContainer
                    key={"GridContainer-" + index}
                    container
                    alignItems="center"
                    style={CONFIGURACION_TIPOGRAFIA_14}
                  >
                    {this.renderFila(
                      "Estacionamientos",
                      registro.estacionamiento_carros,
                      registro.porce_estacionamiento_carros + "%",
                      registro.estacionamiento_motos,
                      registro.porce_estacionamiento_motos + "%",
                      registro.total_estacionammientos,
                      (
                        registro.porce_estacionamiento_carros +
                        registro.porce_estacionamiento_motos
                      ).toFixed(2) + "%",
                      FILA_BLANCA
                    )}

                    {this.renderFila(
                      "Estacionamientos cobrados",
                      registro.estacionamiento_carros_con_cobro,
                      registro.porce_estacionamiento_carros_con_cobro + "%",
                      registro.estacionamiento_motos_con_cobro,
                      registro.porce_estacionamiento_motos_con_cobro + "%",
                      registro.total_estacionammientos_con_cobro,
                      (
                        registro.porce_estacionamiento_carros_con_cobro +
                        registro.porce_estacionamiento_motos_con_cobro
                      ).toFixed(2) + "%",
                      FILA_GRIS
                    )}

                    {this.renderFila(
                      "Estacionamientos sin cobro",
                      registro.estacionamiento_carros_sin_cobro,
                      registro.porce_estacionamiento_carros_sin_cobro + "%",
                      registro.estacionamiento_motos_sin_cobro,
                      registro.porce_estacionamiento_motos_sin_cobro + "%",
                      registro.total_estacionammientos_sin_cobro,
                      (
                        registro.porce_estacionamiento_carros_sin_cobro +
                        registro.porce_estacionamiento_motos_sin_cobro
                      ).toFixed(2) + "%",
                      FILA_BLANCA
                    )}

                    {this.renderFila(
                      "Recaudo",
                      formatoDinero(registro.recaudo_carros),
                      "",
                      formatoDinero(registro.recaudo_motos),
                      "",
                      formatoDinero(registro.recaudo_total),
                      (registro.porce_recaudo_total === null ||
                      registro.porce_recaudo_total === undefined
                        ? 0
                        : registro.porce_recaudo_total) + "%",
                      FILA_GRIS
                    )}
                    {this.renderFila(
                      "Cartera",
                      formatoDinero(registro.cartera_carros),
                      "",
                      formatoDinero(registro.cartera_motos),
                      "",
                      formatoDinero(registro.cartera_total),
                      (registro.porce_cartera_total === null ||
                      registro.porce_cartera_total === undefined
                        ? 0
                        : registro.porce_cartera_total) + "%",
                      FILA_BLANCA
                    )}
                    {this.renderFila(
                      "Total",
                      formatoDinero(registro.cobro_carros),
                      "",
                      formatoDinero(registro.cobro_motos),
                      "",
                      formatoDinero(registro.cobro_total),
                      "99.99%",
                      FILA_GRIS
                    )}
                  </GridContainer>
                </>
              );
            })}
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

    informeMensual: (parametros, onSuccess) =>
      dispatch(informeMensual(parametros, onSuccess)),
  };
};

export default withStyles(extendedTablesStyle)(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(InformeMensual)
);
