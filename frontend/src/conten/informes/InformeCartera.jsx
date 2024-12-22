import withStyles from '@material-ui/core/styles/withStyles';
import AssessmentIcon from '@material-ui/icons/Assessment';
import {
  primaryColor,
  successColor,
  whiteColor,
} from 'assets/jss/material-dashboard-pro-react.jsx';
import extendedTablesStyle from 'assets/jss/material-dashboard-pro-react/views/extendedTablesStyle.jsx';
import Card from 'components/Card/Card.jsx';
import CardBody from 'components/Card/CardBody.jsx';
import CardHeader from 'components/Card/CardHeader.jsx';
import CardIcon from 'components/Card/CardIcon.jsx';
import GridContainer from 'components/Grid/GridContainer';
import GridItem from 'components/Grid/GridItem.jsx';
import FormControl from '@material-ui/core/FormControl';
import CustomSelect from 'components/CustomSelect/CustomSelect.jsx';
import { Menu, MenuItem } from '@material-ui/core';
import TablePagination from 'components/TablePagination/TablePagination.jsx';
import {
  formatDate,
  formatDateTimeReporte,
  formatNumberDate,
  formatoDinero,
  getHorasMinutosTranscurridos,
} from 'config/funciones/Comunes';
import {
  CONFIGURACION_TIPOGRAFIA_14,
  FILA_BLANCA,
  FILA_GRIS,
  LISTADO_PAGINACION_CARTERA,
  OK,
} from 'config/general/Configuracion';
import EncabezadoGrid from 'conten/components/EncabezadoGrid';
import TituloPagina2 from 'conten/components/TituloPagina2';
import WaitDialog from 'conten/components/WaitDialog.jsx';
import { informeCartera } from 'conten/informes/InformesActions';
import { listarUsuario } from 'conten/usuarios/UsuarioActions';
import React from 'react';
import { connect } from 'react-redux';
import { ExportSheet } from 'react-xlsx-sheet';
import * as XLSX from 'xlsx';
import { TextField } from '@material-ui/core';
import Button from 'components/CustomButtons/Button';

const columnasOrdenar2 = [
  {
    campos: [{ id: '', isSorted: false, label: 'Vehiculo' }],
    xs: 12,
    sm: 12,
    md: 2,
    lg: 2,
  },
  {
    campos: [{ id: '', isSorted: false, label: 'Datos Ingreso' }],
    xs: 12,
    sm: 12,
    md: 5,
    lg: 5,
  },
  {
    campos: [{ id: '', isSorted: false, label: 'Datos Egreso' }],
    xs: 12,
    sm: 12,
    md: 5,
    lg: 5,
  },
];

class InformeCartera extends React.Component {
  constructor(props) {
    super(props);
    const fechaActual = formatDate(new Date(Date.now()), 'yyyy-mm-dd');
    this.state = {
      textAlertInfo: 'Un momento mientras completamos su solicitud ;)',
      fechaActual: fechaActual,
      datosConsulta: {
        nfInicial: formatNumberDate(fechaActual),
        nfFinal: formatNumberDate(fechaActual),
        placa: '',
        paginacion: {
          limite: 130000, //limite de consultas por pagina
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
    //this.traerDatosServidor();
  };

  cambioFechaInicial = (evento) => {
    var datosConsulta = this.state.datosConsulta;
    datosConsulta.nfInicial = formatNumberDate(evento.target.value);
    this.setState({ datosConsulta: datosConsulta }); //this.recuperarReporte);
  };

  cambioFechaFinal = (evento) => {
    var datosConsulta = this.state.datosConsulta;
    datosConsulta.nfFinal = formatNumberDate(evento.target.value);
    this.setState({ datosConsulta: datosConsulta }); //this.recuperarReporte);
  };

  /*getEstados = () => {
    var content = [];
    var todos = "Todos";
    var sinCobro = "Salio sin cobro"
    var reportada = "Reportada"
    var abierta = "Abierta"
    var prepago = "Prepago"
    var postpago = "Pagada"
    var extemporaneo = "Pago extemporaneo"
    for (var i = 0; i<7; i++) {
      content.push(
        <MenuItem key={estado} value={estado}>
          {todos}
        </MenuItem>
      );
    }
    return content;
  };*/

  traerDatosServidor = () => {
    this.recuperarReporte();
  };

  recuperarReporte = () => {
    this.props.informeCartera(
      this.state.datosConsulta,
      this.respuestaInformeCartera
    );
  };

  respuestaInformeCartera = (estado, contenido) => {
    if (estado !== OK) {
      return;
    }
    var datosConsulta = this.state.datosConsulta;
    datosConsulta.paginacion = contenido.paginacion;
    this.setState({ reporte: contenido.reporte, datosConsulta });
  };

  funcionEnBlanco = () => {};

  getNombresReferenicasXLS = () => {
    var encabezado = [];
    encabezado.push({ title: 'Placa', dataIndex: 'placa' });
    encabezado.push({ title: 'Vehículo', dataIndex: 'vehiculo' });
    encabezado.push({ title: 'Id cobro', dataIndex: 'idcobro' });
    encabezado.push({ title: 'Zona', dataIndex: 'zona' });
    encabezado.push({ title: 'Zona inicia', dataIndex: 'zonainicia' });
    encabezado.push({ title: 'Zona termina', dataIndex: 'zonatermina' });
    encabezado.push({ title: 'Zona valor hora', dataIndex: 'zonavalor' });
    encabezado.push({ title: 'Zona minutos gracia', dataIndex: 'zonamin' });
    encabezado.push({
      title: 'Zona minutos para nueva gracia',
      dataIndex: 'zonaminnueva',
    });
    encabezado.push({ title: 'Fecha hora ingreso', dataIndex: 'feingreso' });
    encabezado.push({ title: 'Promotor ingreso', dataIndex: 'proingreso' });
    encabezado.push({ title: 'Fecha hora egreso', dataIndex: 'feegreso' });
    encabezado.push({ title: 'Horas cobradas', dataIndex: 'hcobradas' });
    encabezado.push({ title: 'valor cobrado', dataIndex: 'vcobrado' });
    encabezado.push({ title: 'Promotor  reporta', dataIndex: 'proreporte' });
    return encabezado;
  };

  getContenidosXLS = (contenido) => {
    var resul = [];
    contenido.map((reg) => {
      var cont = {};
      cont['placa'] = reg.placa;
      cont['vehiculo'] = reg.es_carro === true ? 'Carro' : 'Moto';
      cont['idcobro'] = reg.id + '';
      cont['zona'] = reg.nombre_zona;
      cont['zonainicia'] = reg.h_inicia_zona;
      cont['zonatermina'] = reg.h_termina_zona;
      cont['zonavalor'] = reg.valor_h;
      cont['zonamin'] = reg.minutos_gracia_zona;
      cont['zonaminnueva'] = reg.minutos_para_nueva_gracia_zona;
      cont['feingreso'] = formatDateTimeReporte(reg.fh_ingreso);
      cont['proingreso'] = reg.nombre_promotor_ingreso;
      cont['feegreso'] = formatDateTimeReporte(reg.fh_egreso);
      cont['hcobradas'] = reg.h_cobradas;
      cont['vcobrado'] = reg.valor_cobrado;
      cont['proreporte'] = reg.nombre_promotor_reporta;
      if (reg.nombre_estado === 'Reportada') {
        resul.push(cont);
      }
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
            <CardIcon color='primary'>
              <ExportSheet
                header={this.getNombresReferenicasXLS()}
                fileName={`Informe_Cartera`}
                dataSource={this.getContenidosXLS(reporte)}
                xlsx={XLSX}
              >
                <button
                  style={{
                    cursor: 'pointer',
                    backgroundColor: primaryColor[0],
                    color: whiteColor,
                    border: '0px',
                  }}
                >
                  <b>+ Descargar</b>
                </button>
              </ExportSheet>
              <AssessmentIcon />
            </CardIcon>
            <TituloPagina2
              texto='Informe de cartera'
              color='primary'
              classes={classes}
            />
          </CardHeader>
          <CardBody>
            <GridContainer
              key={'GridContainer-parametros'}
              container
              alignItems='center'
            >
              <GridItem xs={11} sm={11} md={4} lg={4}>
                <FormControl fullWidth>
                  <TextField
                    id='fechaInicial'
                    label='Fecha Inicial'
                    type='date'
                    defaultValue={this.state.fechaActual}
                    className={classes.textField}
                    onChange={this.cambioFechaInicial}
                    InputLabelProps={{
                      shrink: true,
                    }}
                  />
                </FormControl>
              </GridItem>
              <GridItem xs={11} sm={11} md={4} lg={4}>
                <FormControl fullWidth>
                  <TextField
                    id='fechaFinal'
                    label='Fecha Final'
                    type='date'
                    defaultValue={this.state.fechaActual}
                    className={classes.textField}
                    onChange={this.cambioFechaFinal}
                    InputLabelProps={{
                      shrink: true,
                    }}
                  />
                </FormControl>
              </GridItem>
              <GridItem xs={1} sm={1} md={2} lg={2}>
                <CardIcon color='primary'>
                  <button
                    style={{
                      cursor: 'pointer',
                      backgroundColor: primaryColor[0],
                      color: whiteColor,
                      border: '0px',
                    }}
                    onClick={this.traerDatosServidor}
                  >
                    <b>Actualizar</b>
                  </button>
                </CardIcon>
              </GridItem>
            </GridContainer>

            <div>{''}</div>
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

    informeCartera: (parametros, onSuccess) =>
      dispatch(informeCartera(parametros, onSuccess)),
  };
};

export default withStyles(extendedTablesStyle)(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(InformeCartera)
);
