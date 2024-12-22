import FormControl from "@material-ui/core/FormControl";
import InputAdornment from "@material-ui/core/InputAdornment";
import withStyles from "@material-ui/core/styles/withStyles";
import TextField from "@material-ui/core/TextField";
import AssessmentIcon from "@material-ui/icons/Assessment";
import DirectionsCarIcon from "@material-ui/icons/DirectionsCar";
import SaveIcon from "@material-ui/icons/Save";
import MonetizationOnIcon from '@material-ui/icons/MonetizationOn';
import {
  primaryColor,
  successColor,
  whiteColor,
} from "assets/jss/material-dashboard-pro-react.jsx";
import extendedTablesStyle from "assets/jss/material-dashboard-pro-react/views/extendedTablesStyle.jsx";
import Card from "components/Card/Card.jsx";
import CardBody from "components/Card/CardBody.jsx";
import CardHeader from "components/Card/CardHeader.jsx";
import CardIcon from "components/Card/CardIcon.jsx";
import CustomInput from "components/CustomInput/CustomInput.jsx";
import GridContainer from "components/Grid/GridContainer";
import GridItem from "components/Grid/GridItem.jsx";
import TablePagination from "components/TablePagination/TablePagination.jsx";
import Button from "components/CustomButtons/Button.jsx";
import Alerta2Botones from "conten/components/Alerta2Botones";
import CheckCircleIcon from "@material-ui/icons/CheckCircle";
import CancelIcon from "@material-ui/icons/Cancel";


import {
  formatDate,
  formatDateTimeReporte,
  formatNumberDate,
  formatoDinero,
  getHorasMinutosTranscurridos,
} from "config/funciones/Comunes";
import {
  CONFIGURACION_TIPOGRAFIA_14,
  FILA_BLANCA,
  FILA_GRIS,
  LISTADO_PAGINACION_REPORTES,
  OK,
} from "config/general/Configuracion";
import EncabezadoGrid from "conten/components/EncabezadoGrid";
import TituloPagina2 from "conten/components/TituloPagina2";
import WaitDialog from "conten/components/WaitDialog.jsx";
import { informeVehiculo, hacerPagoExtemporaneoWEB } from "conten/informes/InformesActions";
import { listarUsuario } from "conten/usuarios/UsuarioActions";
import React from "react";
import { connect } from "react-redux";
import { ExportSheet } from "react-xlsx-sheet";
import * as XLSX from 'xlsx';

const ESTADO_REPORTADO = 3;
const PAGO_EXTEMPORANEO = 4;
const columnasOrdenar2 = [
  {
    campos: [{ id: "", isSorted: false, label: "Vehiculo" }],
    xs: 12,
    sm: 12,
    md: 2,
    lg: 2,
  },
  {
    campos: [{ id: "", isSorted: false, label: "Datos Ingreso" }],
    xs: 12,
    sm: 12,
    md: 5,
    lg: 5,
  },
  {
    campos: [{ id: "", isSorted: false, label: "Datos Egreso" }],
    xs: 12,
    sm: 12,
    md: 5,
    lg: 5,
  },
];

class InformeHistorico extends React.Component {
  constructor(props) {
    super(props);
    const fechaActual = formatDate(new Date(Date.now()), "yyyy-mm-dd");
    this.state = {
      contenidoAlerta: null,
      textAlertInfo: "Un momento mientras completamos su solicitud ;)",
      fechaActual: fechaActual,
      datosConsulta: {
        nfInicial: formatNumberDate(fechaActual),
        nfFinal: formatNumberDate(fechaActual),
        placa: "",
        paginacion: {
          limite: LISTADO_PAGINACION_REPORTES[0],
          actual: 1,
          filtro: null,
          orden: null,
          sentido: null,
        },
      },
      reporte: [],
    };
  }

  componentWillUnmount() { }

  componentDidMount = () => {
    this.traerDatosServidor();
  };

  cambioFechaInicial = (evento) => {
    var datosConsulta = this.state.datosConsulta;
    datosConsulta.nfInicial = formatNumberDate(evento.target.value);
    this.setState({ datosConsulta: datosConsulta }, this.recuperarReporte);
  };

  cambioFechaFinal = (evento) => {
    var datosConsulta = this.state.datosConsulta;
    datosConsulta.nfFinal = formatNumberDate(evento.target.value);
    this.setState({ datosConsulta: datosConsulta }, this.recuperarReporte);
  };

  cambioContenidoPlaca = (valor) => {
    var datosConsulta = this.state.datosConsulta;
    datosConsulta.placa = (valor + "").trim().toUpperCase();
    this.setState({ datosConsulta: datosConsulta }, this.recuperarReporte);
  };

  traerDatosServidor = () => {
    this.recuperarReporte();
  };

  recuperarReporte = () => {
    var placa = this.state.datosConsulta.placa;
    if (placa.trim().length < 5) {
      return;
    }
    this.props.informeVehiculo(
      this.state.datosConsulta,
      this.respuestaInformeVehiculo
    );
  };

  respuestaInformeVehiculo = (estado, contenido) => {
    if (estado !== OK) {
      return;
    }
    var datosConsulta = this.state.datosConsulta;
    datosConsulta.paginacion = contenido.paginacion;
    this.setState({ reporte: contenido.reporte, datosConsulta });
  };

  cambioContenidoPromotor = (idPromotor) => {
    var datosConsulta = this.state.datosConsulta;
    datosConsulta.promotor = idPromotor;
    this.setState({ datosConsulta }, this.recuperarReporte);
  };

  funcionEnBlanco = () => { };

  respuestaProcesarPagoExtemporaneo=(estado,respuesta)=>{
    this.traerDatosServidor();
  }

  continuarCerrarProcesarPagoExtemporaneo = (contenido) => {
    this.cerrarProcesarPagoExtemporaneo();
    var localContenido={
      idPago:contenido.id,
      placa:contenido.placa
    }
    this.props.hacerPagoExtemporaneoWEB(
      localContenido,
      this.respuestaProcesarPagoExtemporaneo
    );
  };

  cerrarProcesarPagoExtemporaneo = () => {
    this.setState({ contenidoAlerta: null });
  };

  cancelarCerrarProcesarPagoExtemporaneo = () => {
    this.cerrarProcesarPagoExtemporaneo();
  };

  procesarPagoExtemporaneo = (contenido) => {
    var contenidoAlerta = (
      <Alerta2Botones
        key="preguntarProcesarPagoExtemporaneo"
        titulo="Registrar pago"
        mensaje={"¿Desea realizar el pago extemporaneo?"}
        contenido={contenido}
        textoBotonAceptar="Aceptar"
        textoBotonCancelar="Cancelar"
        eventoBotonAceptar={this.continuarCerrarProcesarPagoExtemporaneo}
        eventoBotonCancelar={this.cancelarCerrarProcesarPagoExtemporaneo}
        iconoAceptar={<CheckCircleIcon />}
        iconoCancelar={<CancelIcon />}
      />
    );
    this.setState({ contenidoAlerta: contenidoAlerta });
  };

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
            {contenido.valor === "" ? "--" : contenido.valor}
          </GridItem>
        </>
      );
    });
    return res;
  };

  getNombresReferenicasXLS = () => {
    var encabezado = [];
    encabezado.push({ title: "Estado", dataIndex: "estado" });
    encabezado.push({ title: "Placa", dataIndex: "placa" });
    encabezado.push({ title: "Vehículo", dataIndex: "vehiculo" });
    encabezado.push({ title: "Id cobro", dataIndex: "idcobro" });
    encabezado.push({ title: "Zona", dataIndex: "zona" });
    encabezado.push({ title: "Zona inicia", dataIndex: "zonainicia" });
    encabezado.push({ title: "Zona termina", dataIndex: "zonatermina" });
    encabezado.push({ title: "Zona valor hora", dataIndex: "zonavalor" });
    encabezado.push({ title: "Zona minutos gracia", dataIndex: "zonamin" });
    encabezado.push({
      title: "Zona minutos para nueva gracia",
      dataIndex: "zonaminnueva",
    });
    encabezado.push({ title: "Fecha hora ingreso", dataIndex: "feingreso" });
    encabezado.push({ title: "Promotor ingreso", dataIndex: "proingreso" });
    encabezado.push({ title: "Fecha hora egreso", dataIndex: "feegreso" });
    encabezado.push({ title: "Promotor egreso", dataIndex: "proegreso" });
    encabezado.push({ title: "Horas cobradas", dataIndex: "hcobradas" });
    encabezado.push({ title: "valor cobrado", dataIndex: "vcobrado" });
    encabezado.push({ title: "Fecha hora recaudo", dataIndex: "ferecaudo" });
    encabezado.push({ title: "Promotor recauda", dataIndex: "prorecaudo" });
    encabezado.push({ title: "Promotor  reporta", dataIndex: "proreporte" });
    return encabezado;
  };

  getContenidosXLS = (contenido) => {
    var resul = [];
    contenido.map((reg) => {
      var cont = {};
      cont["estado"] = reg.nombre_estado;
      cont["placa"] = reg.placa;
      cont["vehiculo"] = reg.es_carro === true ? "Carro" : "Moto";
      cont["idcobro"] = reg.id + "";
      cont["zona"] = reg.nombre_zona;
      cont["zonainicia"] = reg.h_inicia_zona;
      cont["zonatermina"] = reg.h_termina_zona;
      cont["zonavalor"] = reg.valor_h;
      cont["zonamin"] = reg.minutos_gracia_zona;
      cont["zonaminnueva"] = reg.minutos_para_nueva_gracia_zona;
      cont["feingreso"] = formatDateTimeReporte(reg.fh_ingreso);
      cont["proingreso"] = reg.nombre_promotor_ingreso;
      cont["feegreso"] = formatDateTimeReporte(reg.fh_egreso);
      cont["proegreso"] = reg.nombre_promotor_egreso;
      cont["hcobradas"] = reg.h_cobradas;
      cont["vcobrado"] = reg.valor_cobrado;
      cont["ferecaudo"] = formatDateTimeReporte(reg.fh_recaudo);
      cont["prorecaudo"] = reg.nombre_promotor_recauda;
      cont["proreporte"] = reg.nombre_promotor_reporta;
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
    const { paginacion } = this.state.datosConsulta;
    const { isActivityIndicatorShown } = this.props.informesState;
    var reporte = this.state.reporte;
    return (
      <GridItem xs={12}>
        {isActivityIndicatorShown && (
          <WaitDialog text={this.state.textAlertInfo} />
        )}
        {this.state.contenidoAlerta}
        <Card>
          <CardHeader icon>
            <CardIcon color="primary">
              <ExportSheet
                header={this.getNombresReferenicasXLS()}
                fileName={`Vehiculo_${this.state.datosConsulta.placa}`}
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
              texto="Informe de vehículo"
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
              <GridItem xs={12} sm={12} md={4} lg={4}>
                <FormControl fullWidth>
                  <TextField
                    id="fechaInicial"
                    label="Fecha inicial"
                    type="date"
                    defaultValue={
                      this.state.fechaActual
                      /*formatDate(
                      new Date(2020, 1, 1),
                      "yyyy-mm-dd")*/
                    }
                    className={classes.textField}
                    onChange={this.cambioFechaInicial}
                    InputLabelProps={{
                      shrink: true,
                    }}
                  />
                </FormControl>
              </GridItem>
              <GridItem xs={12} sm={12} md={4} lg={4}>
                <FormControl fullWidth>
                  <TextField
                    id="fechaFinal"
                    label="Fecha final"
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
              <GridItem xs={12} sm={12} md={4} lg={4}>
                <CustomInput
                  labelText="* Placa"
                  id="etiquetaPlaca"
                  success
                  formControlProps={{
                    fullWidth: true,
                  }}
                  inputProps={{
                    name: "placa",
                    id: "placa",
                    value: this.state.datosConsulta.placa,
                    onChange: (event) =>
                      this.cambioContenidoPlaca(event.target.value),
                    endAdornment: (
                      <InputAdornment position="start">
                        <DirectionsCarIcon />
                      </InputAdornment>
                    ),
                  }}
                />
              </GridItem>
            </GridContainer>
            <GridContainer
              key={"GridContainer-encabezado"}
              container
              alignItems="center"
              style={CONFIGURACION_TIPOGRAFIA_14}
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
            </GridContainer>
            {reporte.map((registro, index) => {
              //console.log("--",registro)
              return (
                <GridContainer
                  key={"GridContainer-" + index}
                  container
                  alignItems="center"
                  style={Object.assign(
                    {
                      backgroundColor: index % 2 == 0 ? FILA_BLANCA : FILA_GRIS,
                      marginTop: "2px",
                    },
                    CONFIGURACION_TIPOGRAFIA_14
                  )}
                >
                  <GridItem xs={12} sm={12} md={2} lg={2}>
                    <div>{registro.nombre_estado}</div>
                    <div>{registro.es_carro === true ? "Carro" : "Moto"}</div>
                    <div>
                      <b>{registro.placa}</b>
                    </div>
                    <div>
                      {registro.estado === ESTADO_REPORTADO && <Button
                        key={"button-pago-extemporaneo" + index}
                        type="button"
                        onClick={(e) => {
                          this.procesarPagoExtemporaneo(registro);
                          e.preventDefault();
                        }}
                        color="success"
                      >
                        <MonetizationOnIcon />
                              Pagar
                            </Button>}
                    </div>
                    <div>
                      {registro.estado === PAGO_EXTEMPORANEO && registro.promotor_recauda===null ? "Pago WEB":"" }
                    </div>

                  </GridItem>

                  <GridItem xs={12} sm={12} md={5} lg={5}>
                    <div>
                      <b>{"id: "}</b>
                      {registro.id}
                    </div>
                    <div>
                      <b>{"Fecha y hora: "}</b>
                      {formatDateTimeReporte(registro.fh_ingreso)}
                    </div>
                    <div>
                      <b>{"Promotor: "}</b>
                      {registro.nombre_promotor_ingreso}
                    </div>
                    <div>
                      <b>{"Zona: "}</b>
                      {registro.nombre_zona}
                    </div>
                    <div>
                      <b>{"Valor hora: "}</b>
                      {formatoDinero(registro.valor_h)}
                    </div>
                  </GridItem>

                  <GridItem xs={12} sm={12} md={5} lg={5}>
                    <div>
                      <b>{"Fecha y hora: "}</b>
                      {formatDateTimeReporte(registro.fh_egreso)}
                    </div>
                    <div>
                      <b>{"Tiempo: "}</b>
                      {getHorasMinutosTranscurridos(registro.fh_ingreso, registro.fh_egreso)}
                    </div>
                    <div>
                      <b>{"Horas cobradas: "}</b>
                      {registro.h_cobradas +
                        (registro.h_cobradas === 1 ? " hora" : " horas")}
                    </div>
                    <div>
                      <b>{"Valor cobrado: "}</b>
                      {formatoDinero(registro.valor_cobrado)}
                    </div>
                    <div>
                      <b>{"Promotor: "}</b>
                      {registro.nombre_promotor_egreso === null
                        ? "Sistema"
                        : registro.nombre_promotor_egreso}
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

    hacerPagoExtemporaneoWEB: (contenido, onSuccess) =>
      dispatch(hacerPagoExtemporaneoWEB(contenido, onSuccess)),

    listarUsuario: (paginacion) =>
      dispatch(listarUsuario(paginacion, () => { })),

    informeVehiculo: (parametros, onSuccess) =>
      dispatch(informeVehiculo(parametros, onSuccess)),
  };
};

export default withStyles(extendedTablesStyle)(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(InformeHistorico)
);
