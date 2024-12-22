import FormControl from "@material-ui/core/FormControl";
import { ThemeProvider } from "@material-ui/core/styles";
import withStyles from "@material-ui/core/styles/withStyles";
import TextField from "@material-ui/core/TextField";
import CancelIcon from "@material-ui/icons/Cancel";
import CheckCircleIcon from "@material-ui/icons/CheckCircle";
import extendedTablesStyle from "assets/jss/material-dashboard-pro-react/views/extendedTablesStyle.jsx";
import Card from "components/Card/Card.jsx";
import CardBody from "components/Card/CardBody.jsx";
import CardHeader from "components/Card/CardHeader.jsx";
import CardIcon from "components/Card/CardIcon.jsx";
import GridItem from "components/Grid/GridItem.jsx";
import {
  formatDate,
  formatNumberDate,
  formatoDinero,
  soloNumeros,
  sumarDias,
} from "config/funciones/Comunes";
import { OK } from "config/general/Configuracion";
import { CAMPO_CALENDARIO_TEXTO_BLANCO } from "config/general/Temas";
import Alerta2Botones from "conten/components/Alerta2Botones";
import TituloPagina2 from "conten/components/TituloPagina2";
import WaitDialog from "conten/components/WaitDialog.jsx";
import {
  solicitarGuardarPagoMunicipio,
  solicitarGuardarRecaudoReporte,
  solicitarListarPagoMunicipio,
  solicitarListarRecaudoReporte,
  solicitarListarFechaPago,
} from "conten/financiera/FinancieraActions";
import RecaudoListarEncabezado from "conten/financiera/RecaudoListarEncabezado";
import RecaudoListarTotales from "conten/financiera/RecaudoListarTotales";
import RecaudoPagoMunicipio from "conten/financiera/RecaudoPagoMunicipio";
import RecaudoListarContenido from "conten/financiera/RecudoListarContenido";
import "date-fns";
import React from "react";
import { connect } from "react-redux";
import AplicarSeguridad from "conten/components/AplicarSeguridad";
import { ExportSheet } from "react-xlsx-sheet";
import * as XLSX from 'xlsx';
import {
  primaryColor,
  successColor,
  whiteColor,
} from "assets/jss/material-dashboard-pro-react.jsx";
import AssessmentIcon from "@material-ui/icons/Assessment";


class RecaudoListar extends React.Component {
  constructor(props) {
    super(props);
    const fechaActual = formatDate(new Date(Date.now()), "yyyy-mm-dd");
    this.state = {
      textAlertInfo: "Un momento mientras completamos su solicitud.",
      fechaActual: fechaActual,
      filtro: {
        id: "",
        nombre: "",
      },
      datosConsulta: {
        fnRecaudo: formatNumberDate(fechaActual),
        recaudo: null,
      },
      listadoRecaudoReporte: [],
      contenidoAlerta: null,
      pagoMunicipio: {
        id: 0,
        nfPago: formatNumberDate(fechaActual),
        informacion: "",
        valorConsignado: 0,
        codigoFactura: "",
        cerrado: false,
      },
      fechaPermiteEditar: false,
    };
  }

  componentDidMount = () => {
    this.traerDatosServidor();
  };

  traerDatosServidor = () => {
    this.limpiarContenidoId();
    this.limpiarContenidoNombre();
    this.props.solicitarListarFechaPago(
      this.state.datosConsulta,
      this.respuestaSolicitarListarFechaPago
    )
    this.props.solicitarListarRecaudoReporte(
      this.state.datosConsulta,
      this.respuestaTraerdatos
    );
    this.props.solicitarListarPagoMunicipio(
      this.state.datosConsulta,
      this.respuestaTraerdatosPagoMunicipio
    );
  };

  respuestaTraerdatos = (estado, data) => {
    if (estado !== OK) {
      return;
    }
    this.setState({ listadoRecaudoReporte: data });
  };

  respuestaTraerdatosPagoMunicipio = (estado, data) => {
    if (estado !== OK) {
      return;
    }
    data.valorConsignado = formatoDinero(data.valorConsignado);
    this.setState({ pagoMunicipio: data });
  };

  respuestaSolicitarListarFechaPago = (estado, data) => {
    if (estado !== OK) {
      return;
    }
    this.setState({ fechaPermiteEditar: data });
  };

  cambioFechaRecaudo = (evento) => {
    var datosConsulta = this.state.datosConsulta;
    datosConsulta.fnRecaudo = formatNumberDate(evento.target.value);
    this.setState({ datosConsulta: datosConsulta }, this.traerDatosServidor);
  };

  cambioFiltroId = (event) => {
    var filtro = this.state.filtro;
    filtro.id = event.target.value;
    this.nuevoFiltro(filtro);
  };

  cambioFiltroNombre = (event) => {
    var filtro = this.state.filtro;
    filtro.nombre = event.target.value;
    this.nuevoFiltro(filtro);
  };

  nuevoFiltro = (filtro) => {
    this.setState({ filtro: filtro }, this.aplicarFiltro);
  };

  getFechaPermiteEditar = () => {
    return this.state.fechaPermiteEditar;
  };

  eventoCambioValor = (event, _element, index) => {
    if (this.getFechaPermiteEditar() !== true) {
      return;
    }
    var listadoRecaudoReporte = this.state.listadoRecaudoReporte;
    var contenido = listadoRecaudoReporte[index];
    if (contenido.cerrada) {
      return;
    }
    contenido.reportado = soloNumeros(event.target.value);

    contenido.reportadoFormato = formatoDinero(contenido.reportado);
    listadoRecaudoReporte[index] = contenido;
    this.setState({ listadoRecaudoReporte: listadoRecaudoReporte });
  };

  //Creado para gestionar el valor de los abonos
  eventoCambioAbono = (event, _element, index) => {
    if (this.getFechaPermiteEditar() !== true) {
      return;
    }
    var listadoRecaudoReporte = this.state.listadoRecaudoReporte;
    var contenido = listadoRecaudoReporte[index];
    if (contenido.cerrada) {
      return;
    }
    contenido.valorAbono = soloNumeros(event.target.value);

    contenido.valorAbonoFormato = formatoDinero(contenido.valorAbono);
    listadoRecaudoReporte[index] = contenido;
    this.setState({ listadoRecaudoReporte: listadoRecaudoReporte });
  };

  eventoCambioEstado = (_event, _element, index) => {
    if (this.getFechaPermiteEditar() !== true) {
      return;
    }
    var listadoRecaudoReporte = this.state.listadoRecaudoReporte;
    var contenido = listadoRecaudoReporte[index];
    if (contenido.cerrada) {
      return;
    }
    contenido["index"] = index;
    this.preguntarCerrarRecaudo(contenido);
  };

  aplicarFiltro = () => {
    var listadoRecaudoReporte = this.state.listadoRecaudoReporte;
    var id = this.state.filtro.id.toLowerCase();
    var nombre = this.state.filtro.nombre.toLowerCase();
    var filtrado = [];
    listadoRecaudoReporte.forEach((cont) => {
      cont.mostrar = false;
      if (
        cont.identificacionPromotor.toLowerCase().includes(id) &&
        cont.nombrePromotor.toLowerCase().includes(nombre)
      ) {
        cont.mostrar = true;
      }

      filtrado.push(cont);
    });
    this.setState({ listadoRecaudoReporte: filtrado });
  };

  cerrarRecaudoPromotor = (index) => {
    var listadoRecaudoReporte = this.state.listadoRecaudoReporte;
    var contenido = listadoRecaudoReporte[index];
    if (contenido.cerrada) {
      return;
    }
    contenido.cerrada = !contenido.cerrada;
    listadoRecaudoReporte[index] = contenido;
    this.setState(
      { listadoRecaudoReporte: listadoRecaudoReporte },
      this.grabarRecaudo(index)
    );
  };

  preguntarCerrarRecaudo = (contenido) => {
    var contenidoAlerta = (
      <Alerta2Botones
        key="preguntarCerrarRecaudo"
        titulo="Cerrar el recaudo"
        mensaje={
          "¿Desea cerrar el recaudo de: " + contenido.nombrePromotor + "?"
        }
        contenido={contenido}
        textoBotonAceptar="Aceptar"
        textoBotonCancelar="Cancelar"
        eventoBotonAceptar={this.continuarCerrarRecaudo}
        eventoBotonCancelar={this.cancelarCerrarRecaudo}
        iconoAceptar={<CheckCircleIcon />}
        iconoCancelar={<CancelIcon />}
      />
    );
    this.setState({ contenidoAlerta: contenidoAlerta });
  };

  preguntarCerrarPagoMunicipio = (contenido) => {
    var contenidoAlerta = (
      <Alerta2Botones
        key="preguntarCerrarPagoMunicipio"
        titulo="Cerrar la factura del municipio"
        mensaje={"¿Desea cerrar la factura asociada al municipio?"}
        contenido={contenido}
        textoBotonAceptar="Aceptar"
        textoBotonCancelar="Cancelar"
        eventoBotonAceptar={this.continuarCerrarPagoMunicipio}
        eventoBotonCancelar={this.cancelarCerrarPagoMunicipio}
        iconoAceptar={<CheckCircleIcon />}
        iconoCancelar={<CancelIcon />}
      />
    );
    this.setState({ contenidoAlerta: contenidoAlerta });
  };

  continuarCerrarRecaudo = (contenido) => {
    this.cerrarVentanaPregunta();
    this.cerrarRecaudoPromotor(contenido.index);
  };

  cerrarVentanaPregunta = () => {
    this.setState({ contenidoAlerta: null });
  };

  cancelarCerrarRecaudo = (_contenido) => {
    this.cerrarVentanaPregunta();
  };

  limpiarContenidoId = () => {
    var filtro = this.state.filtro;
    filtro.id = "";
    this.nuevoFiltro(filtro);
  };

  limpiarContenidoNombre = () => {
    var filtro = this.state.filtro;
    filtro.nombre = "";
    this.nuevoFiltro(filtro);
  };

  onBlurCambioValor = (_event, _element, index) => {
    if (this.getFechaPermiteEditar() !== true) {
      return;
    }
    this.grabarRecaudo(index);
  };

  grabarRecaudo = (index) => {
    var listadoRecaudoReporte = this.state.listadoRecaudoReporte;
    var contenido = listadoRecaudoReporte[index];
    var datosConsulta = {
      recaudo: {
        id: contenido.id_recaudo,
        promotor: contenido.idPromotor,
        nfRecaudo: this.state.datosConsulta.fnRecaudo,
        cantidadRecaudada: contenido.recaudado,
        cantidadReportada: contenido.reportado,
        valorAbono: contenido.valorAbono,
        saldoCuenta: contenido.saldo_cuenta,
        cerrada: contenido.cerrada,
        actualizadoSaldo: contenido.actualizo_cuenta,
        nota: "",
        posArray: index,
      },
    };
    this.props.solicitarGuardarRecaudoReporte(
      datosConsulta,
      this.respuestaGrabarRecaudo
    );
  };

  respuestaGrabarRecaudo = (resp, conte) => {
    if (resp !== OK) {
      return;
    }
    if (conte === undefined) {
      return;
    }
    if (conte === null) {
      return;
    }

    var index = conte.posArray;
    var listadoRecaudoReporte = this.state.listadoRecaudoReporte;
    listadoRecaudoReporte[index] = conte;
    this.setState({ listadoRecaudoReporte: listadoRecaudoReporte });
  };

  eventoCambioCerradoPagoMunicipio = () => {
    if (this.getFechaPermiteEditar() !== true) {
      return;
    }
    var pagoMunicipio = this.state.pagoMunicipio;
    if (pagoMunicipio.cerrado !== true) {
      this.preguntarCerrarPagoMunicipio(pagoMunicipio);
    }
  };

  continuarCerrarPagoMunicipio = () => {
    var pagoMunicipio = this.state.pagoMunicipio;
    pagoMunicipio.cerrado = true;
    this.setState({ pagoMunicipio }, () => {
      this.grabarPagoMunicipio();
      this.setState({ contenidoAlerta: null });
    });
  };

  cancelarCerrarPagoMunicipio = () => {
    this.setState({ contenidoAlerta: null });
  };

  eventoCambioFacturaPagoMunicipio = (data) => {
    if (this.getFechaPermiteEditar() !== true) {
      return;
    }
    var pagoMunicipio = this.state.pagoMunicipio;
    if (pagoMunicipio.cerrado === true) {
      return;
    }
    pagoMunicipio.codigoFactura = data;
    this.setState({ pagoMunicipio });
  };

  eventoCambioValorFacturaPagoMunicipio = (data) => {
    if (this.getFechaPermiteEditar() !== true) {
      return;
    }
    var pagoMunicipio = this.state.pagoMunicipio;
    if (pagoMunicipio.cerrado === true) {
      return;
    }
    pagoMunicipio.valorConsignado = formatoDinero(data);
    this.setState({ pagoMunicipio });
  };

  eventoCambioInformacionPagoMunicipio = (data) => {
    if (this.getFechaPermiteEditar() !== true) {
      return;
    }
    var pagoMunicipio = this.state.pagoMunicipio;
    if (pagoMunicipio.cerrado === true) {
      return;
    }
    pagoMunicipio.informacion = data;
    this.setState({ pagoMunicipio });
  };

  eventoInformarGrabarPagoMunicipio = () => {
    if (this.getFechaPermiteEditar() !== true) {
      return;
    }
    if (this.state.pagoMunicipio.cerrado === true) {
      return;
    }
    this.grabarPagoMunicipio();
  };

  grabarPagoMunicipio = () => {
    const data = this.state.pagoMunicipio;
    var pagoMunicipio = {
      id: data.id,
      nfPago: this.state.datosConsulta.fnRecaudo,
      informacion: data.informacion,
      valorConsignado: soloNumeros(data.valorConsignado),
      codigoFactura: data.codigoFactura,
      cerrado: data.cerrado !== true ? false : true,
    };

    this.props.solicitarGuardarPagoMunicipio(
      pagoMunicipio,
      this.respuestaGrabarPagoMunicipio
    );
  };

  respuestaGrabarPagoMunicipio = (estado, data) => {
    if (estado === OK) {
      data.valorConsignado = formatoDinero(data.valorConsignado);
      this.setState({ pagoMunicipio: data });
    }
  };

  render() {
    const { classes } = this.props;
    const { isActivityIndicatorShown } = this.props.financieraState;
    const { listadoRecaudoReporte, pagoMunicipio } = this.state;

    let encabezado = [];
    encabezado.push({ title: "Nombre", dataIndex: "nombre" });
    encabezado.push({ title: "Planilla", dataIndex: "planilla" });
    encabezado.push({ title: "Abono", dataIndex: "abono" });
    encabezado.push({ title: "Recaudado", dataIndex: "recaudado" });
    encabezado.push({ title: "Diferencia", dataIndex: "diferencia" });
    encabezado.push({ title: "A la bolsa", dataIndex: "a_bolsa" });
    encabezado.push({ title: "Saldo", dataIndex: "saldo" });

    let contenidoDescargable = (contenido) =>{
     let contenidoDescarga = [];
     contenido.map((reg) => {
      let diferencia = reg.reportadoFormato - reg.recaudado;
      var cont = {};
      cont["nombre"] = reg.nombrePromotor;
      cont["planilla"] = reg.reportado;
      cont["abono"] = reg.valorAbono;
      cont["recaudado"] = reg.recaudado;
      cont["diferencia"] = reg.reportadoFormato - reg.recaudado < 0 ? diferencia: 0;
      cont["a_bolsa"] = reg.reportadoFormato - reg.recaudado > 0 ? diferencia: 0;
      cont["saldo"] = reg.saldo;
      contenidoDescarga.push(cont);
    });

    return contenidoDescarga;
  };
    return (
      <GridItem xs={12}>
        {isActivityIndicatorShown && (
          <WaitDialog text={this.state.textAlertInfo} />
        )}
        {this.state.contenidoAlerta}
        <AplicarSeguridad />
        <Card>
          <CardHeader icon>
            <CardIcon
              color="primary">
              <ThemeProvider theme={CAMPO_CALENDARIO_TEXTO_BLANCO}>
                <FormControl>
                  <TextField
                    id="fechaRecaudo"
                    label="Fecha Recaudo"
                    type="date"
                    defaultValue={this.state.fechaActual}
                    className={classes.textField}
                    onChange={this.cambioFechaRecaudo}
                    InputLabelProps={{
                      shrink: true,
                    }}
                  />
                </FormControl>
              </ThemeProvider>
            </CardIcon>
            <TituloPagina2
              texto="Listado de recaudos"
              color="primary"
              classes={classes}
            />
          </CardHeader>

          <CardBody
            style={{
              overflow: "auto",
              fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
              fontSize: "14px",
              fontWeight: "400",
              lineHeight: "1.42857",
              opacity: "1",
            }}
          >
            <RecaudoListarEncabezado
              key="encabezado"
              controlId={this.state.filtro.id}
              onChangeId={this.cambioFiltroId}
              controlNombre={this.state.filtro.nombre}
              onChangeNombre={this.cambioFiltroNombre}
              limpiarContenidoId={this.limpiarContenidoId}
              limpiarContenidoNombre={this.limpiarContenidoNombre}
            />
            <RecaudoListarContenido
              classes={classes}
              key="contenidoListado"
              contenido={listadoRecaudoReporte}
              eventoCambioValor={this.eventoCambioValor}
              eventoCambioAbono={this.eventoCambioAbono}
              eventoCambioEstado={this.eventoCambioEstado}
              onBlurCambioValor={this.onBlurCambioValor}
            />
            <RecaudoListarTotales
              classes={classes}
              key="contenidoListadoTotales"
              contenido={listadoRecaudoReporte}
            />
            <RecaudoPagoMunicipio
              classes={classes}
              key="contenidoPagoMunicipio"
              contenido={pagoMunicipio}
              eventoCambioCerrado={this.eventoCambioCerradoPagoMunicipio}
              eventoCambioFactura={this.eventoCambioFacturaPagoMunicipio}
              eventoCambioValorConsignado={
                this.eventoCambioValorFacturaPagoMunicipio
              }
              eventoCambioInformacion={
                this.eventoCambioInformacionPagoMunicipio
              }
              eventoInformarGrabar={this.eventoInformarGrabarPagoMunicipio}
            />
            { pagoMunicipio.cerrado &&
              <Card >
                <CardHeader icon>
                  <CardIcon color="primary">
                    <ExportSheet
                      header={encabezado}
                      fileName={`Informe_Recaudo`}
                      dataSource={contenidoDescargable(listadoRecaudoReporte)}
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
                    texto="Informe Recaudo"
                    color="primary"
                    classes={classes}
                  />
                </CardHeader>
              </Card>
            }
          </CardBody>
        </Card>
      </GridItem>
    );
  }
}

const mapStateToProps = (state) => {
  return { financieraState: state.financieraState };
};

const mapDispatchToProps = (dispatch) => {
  return {
    solicitarListarRecaudoReporte: (datosConsulta, onSuccess) =>
      dispatch(solicitarListarRecaudoReporte(datosConsulta, onSuccess)),

    solicitarGuardarRecaudoReporte: (datosGrabar, onSuccess) =>
      dispatch(solicitarGuardarRecaudoReporte(datosGrabar, onSuccess)),

    solicitarListarPagoMunicipio: (datosConsulta, onSuccess) =>
      dispatch(solicitarListarPagoMunicipio(datosConsulta, onSuccess)),

    solicitarGuardarPagoMunicipio: (datosGrabar, onSuccess) =>
      dispatch(solicitarGuardarPagoMunicipio(datosGrabar, onSuccess)),

    solicitarListarFechaPago: (datosGrabar, onSuccess) =>
      dispatch(solicitarListarFechaPago(datosGrabar, onSuccess)),
  };
};

export default withStyles(extendedTablesStyle)(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(RecaudoListar)
);
