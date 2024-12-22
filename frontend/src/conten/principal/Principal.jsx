import FormControl from '@material-ui/core/FormControl';
import { ThemeProvider } from '@material-ui/core/styles';
import withStyles from '@material-ui/core/styles/withStyles';
import TextField from '@material-ui/core/TextField';
import HomeIcon from '@material-ui/icons/Home';
import MonetizationOnIcon from '@material-ui/icons/MonetizationOn';
import PieChartIcon from '@material-ui/icons/PieChart';
import PlaceIcon from '@material-ui/icons/Place';
import extendedTablesStyle from 'assets/jss/material-dashboard-pro-react/views/extendedTablesStyle.jsx';
import Accordion from 'components/Accordion/Accordion';
import Card from 'components/Card/Card.jsx';
import CardBody from 'components/Card/CardBody.jsx';
import CardHeader from 'components/Card/CardHeader.jsx';
import CardIcon from 'components/Card/CardIcon.jsx';
import GridContainer from 'components/Grid/GridContainer.jsx';
import GridItem from 'components/Grid/GridItem.jsx';
import {
  formatDate,
  formatDateTime,
  formatNumberDate,
  formatoDinero,
} from 'config/funciones/Comunes';
import { CAMPO_CALENDARIO_TEXTO_BLANCO } from 'config/general/Temas';
import TarjetaDashboard from 'conten/components/TarjetaDashboard';
import TituloPagina2 from 'conten/components/TituloPagina2';
import WaitDialog from 'conten/components/WaitDialog.jsx';
import Ocupacion from 'conten/principal/Ocupacion';
import {
  solicitarDatosDashboardPrincipal,
  solicitarListarOcupacionZona,
} from 'conten/principal/PrincipalActions';
import React from 'react';
import { connect } from 'react-redux';
var temporizador = null;
const MILI_SEGUNDOS_REFRESCAR = 1000 * 60 * 5;
class Principal extends React.Component {
  constructor(props) {
    super(props);
    const fechaActual = formatDate(new Date(Date.now()), 'yyyy-mm-dd');
    const fechaHoraActual = formatDateTime(
      new Date(Date.now()),
      'yyyy-mm-ddTHH:MM'
    );
    this.state = {
      textAlertInfo: 'Un momento mientras completamos su solicitud.',
      fechaActual: fechaActual,
      fechaHoraActual: fechaHoraActual,
      datosConsulta: {
        fnInicio: formatNumberDate(fechaActual),
        fnFin: formatNumberDate(fechaActual),
      },
      datosOcupacionActual: [],
    };
    this.traerDatos();
    this.solicitarOcupacionActual(fechaHoraActual.replace('T', ' '));
  }

  componentWillUnmount() {
    if (temporizador) {
      clearInterval(temporizador);
    }
    temporizador = null;
  }

  componentDidMount = () => {
    if (temporizador) {
      clearInterval(temporizador);
      temporizador = null;
    }
    temporizador = setInterval(() => {
      this.traerDatos();
    }, MILI_SEGUNDOS_REFRESCAR);
  };

  cambioFechaInicial = (evento) => {
    var datosConsulta = this.state.datosConsulta;
    datosConsulta.fnInicio = formatNumberDate(evento.target.value);
    this.setState({ datosConsulta: datosConsulta }, this.traerDatos);
  };

  cambioFechaHoraOcupacion = (evento) => {
    this.solicitarOcupacionActual(evento.target.value.replace('T', ' '));
  };

  cambioFechaFinal = (evento) => {
    var datosConsulta = this.state.datosConsulta;
    datosConsulta.fnFin = formatNumberDate(evento.target.value);
    this.setState({ datosConsulta: datosConsulta }, this.traerDatos);
  };

  traerDatos = () => {
    this.props.solicitarDatosDashboardPrincipal(
      this.state.datosConsulta,
      this.respuestaTraerdatos
    );
  };

  respuestaTraerdatos = (_resp, _cont) => {};

  solicitarOcupacionActual = (fechaHoraActual) => {
    this.state.fechaHoraActual;
    this.props.solicitarListarOcupacionZona(
      { fhConsulta: fechaHoraActual },
      this.respuestaSolicitarOcupacionActual
    );
  };

  respuestaSolicitarOcupacionActual = (_resp, cont) => {
    this.setState({ datosOcupacionActual: cont });
  };

  procesarContenidoPago = (conten, campo) => {
    var data = conten[campo];
    if (data.TOTAL < 1) {
      return '';
    }
    return (
      <>
        {data.GRATIS +
          ' Gratis ' +
          ((data.GRATIS / data.TOTAL) * 100).toFixed(2) +
          '%'}
        {<br />}
        {data.PAGO +
          ' Pagados ' +
          ((data.PAGO / data.TOTAL) * 100).toFixed(2) +
          '%'}
        {<br />}
        {data.REPORTADO +
          ' Reportados ' +
          ((data.REPORTADO / data.TOTAL) * 100).toFixed(2) +
          '%'}
      </>
    );
  };

  render() {
    const { classes } = this.props;
    const {
      RecaudoTotal,
      CarteraTotal,
      RecaudoExtemporaneo,
      RecaudoPostpago,
      RecaudoPrepago,
      ServiciosPeriodoGracia,
      VentaPostpago,
    } = this.props.principalState.datosDashboard;
    const { datosOcupacionActual } = this.state;
    const { isActivityIndicatorShown } = this.props.principalState;
    return (
      <GridItem xs={12}>
        {isActivityIndicatorShown && (
          <WaitDialog text={this.state.textAlertInfo} />
        )}
        <Card>
          <CardHeader icon>
            <CardIcon
              style={
                {
                  //cursor: "pointer",
                }
              }
              color='primary'
              onClick={() => {
                //this.props.history.push("../admin/administrar-usuario");
              }}
            >
              <b>{''}</b>
              <HomeIcon />
            </CardIcon>
            <TituloPagina2
              texto='Tablero principal'
              color='primary'
              classes={classes}
            />
          </CardHeader>
          <CardBody>
            <Accordion
              active={0}
              collapses={[
                {
                  title: 'Estado Recaudo',
                  content: (
                    <>
                      <GridContainer>
                        <GridItem xs={12} sm={6} md={6} lg={6}>
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
                                style: {
                                  color: 'black', // Cambiar el color del texto de la etiqueta
                                },
                              }}
                              InputProps={{
                                style: {
                                  color: 'black', // Cambiar el color del texto de la etiqueta
                                },
                              }}
                            />
                          </FormControl>
                        </GridItem>
                        <GridItem xs={12} sm={6} md={6} lg={6}>
                          <FormControl fullWidth>
                            <TextField
                              style={{ color: 'black' }}
                              id='fechaFinal'
                              label='Fecha Final'
                              type='date'
                              defaultValue={this.state.fechaActual}
                              className={classes.textField}
                              onChange={this.cambioFechaFinal}
                              InputLabelProps={{
                                shrink: true,
                                style: {
                                  color: 'black', // Cambiar el color del texto de la etiqueta
                                },
                              }}
                              InputProps={{
                                style: {
                                  color: 'black', // Cambiar el color del texto de la etiqueta
                                },
                              }}
                            />
                          </FormControl>
                        </GridItem>

                        <br />

                        {RecaudoTotal && (
                          <GridItem xs={12} sm={6} md={4} lg={3}>
                            <TarjetaDashboard
                              key={'RecaudoTotal'}
                              classCardIconTitle={classes.cardIconTitle}
                              titulo='Recaudo Total'
                              color='success'
                              icono={<MonetizationOnIcon />}
                              textoCarro={formatoDinero(RecaudoTotal.Carro)}
                              textoMoto={formatoDinero(RecaudoTotal.Moto)}
                              textoTotal={formatoDinero(
                                RecaudoTotal.Moto + RecaudoTotal.Carro
                              )}
                            />
                          </GridItem>
                        )}
                        {ServiciosPeriodoGracia && (
                          <GridItem xs={12} sm={6} md={4} lg={3}>
                            <TarjetaDashboard
                              key={'AnalisisPagos'}
                              classCardIconTitle={classes.cardIconTitle}
                              titulo='Porcentaje de pagos'
                              color='info'
                              icono={<PieChartIcon />}
                              textoCarro={this.procesarContenidoPago(
                                ServiciosPeriodoGracia,
                                'Carro'
                              )}
                              textoMoto={this.procesarContenidoPago(
                                ServiciosPeriodoGracia,
                                'Moto'
                              )}
                              mostrarTodo={false}
                              textoTotal={''}
                            />
                          </GridItem>
                        )}
                         {RecaudoExtemporaneo && (
                          <GridItem xs={12} sm={6} md={4} lg={3}>
                            <TarjetaDashboard
                              key={'RecaudoExtemporaneo'}
                              classCardIconTitle={classes.cardIconTitle}
                              titulo='Pagos Extemporaneos'
                              color='primary'
                              icono={<MonetizationOnIcon />}
                              textoCarro={formatoDinero(
                                RecaudoExtemporaneo.Carro
                              )}
                              textoMoto={formatoDinero(
                                RecaudoExtemporaneo.Moto
                              )}
                              textoTotal={formatoDinero(
                                RecaudoExtemporaneo.Moto +
                                  RecaudoExtemporaneo.Carro
                              )}
                            />
                          </GridItem>
                        )}
                        {RecaudoPrepago && (
                          <GridItem xs={12} sm={6} md={4} lg={3}>
                            <TarjetaDashboard
                              key={'RecaudoPrepago'}
                              classCardIconTitle={classes.cardIconTitle}
                              titulo='Recaudo Prepago'
                              color='warning'
                              icono={<MonetizationOnIcon />}
                              textoCarro={formatoDinero(RecaudoPrepago.Carro)}
                              textoMoto={formatoDinero(RecaudoPrepago.Moto)}
                              textoTotal={formatoDinero(
                                RecaudoPrepago.Moto + RecaudoPrepago.Carro
                              )}
                            />
                          </GridItem>
                        )}
                        {VentaPostpago && (
                          <GridItem xs={12} sm={6} md={4} lg={3}>
                            <TarjetaDashboard
                              key={'VentaPostpago'}
                              classCardIconTitle={classes.cardIconTitle}
                              titulo='Ventas (Cartera + PostPago)'
                              color='rose'
                              icono={<MonetizationOnIcon />}
                              textoCarro={formatoDinero(VentaPostpago.Carro)}
                              textoMoto={formatoDinero(VentaPostpago.Moto)}
                              textoTotal={formatoDinero(
                                VentaPostpago.Moto + VentaPostpago.Carro
                              )}
                            />
                          </GridItem>
                        )}
                        {CarteraTotal && (
                          <GridItem xs={12} sm={6} md={4} lg={3}>
                            <TarjetaDashboard
                              key={'CarteraTotal'}
                              classCardIconTitle={classes.cardIconTitle}
                              titulo='Cartera Total'
                              color='danger'
                              icono={<MonetizationOnIcon />}
                              textoCarro={formatoDinero(CarteraTotal.Carro)}
                              textoMoto={formatoDinero(CarteraTotal.Moto)}
                              textoTotal={formatoDinero(
                                CarteraTotal.Moto + CarteraTotal.Carro
                              )}
                            />
                          </GridItem>
                        )}
                       
                        {RecaudoPostpago && (
                          <GridItem xs={12} sm={6} md={4} lg={3}>
                            <TarjetaDashboard
                              key={'RecaudoPostpago'}
                              classCardIconTitle={classes.cardIconTitle}
                              titulo='Recaudo PostPago'
                              color='green'
                              icono={<MonetizationOnIcon />}
                              textoCarro={formatoDinero(RecaudoPostpago.Carro)}
                              textoMoto={formatoDinero(RecaudoPostpago.Moto)}
                              textoTotal={formatoDinero(
                                RecaudoPostpago.Moto + RecaudoPostpago.Carro
                              )}
                            />
                          </GridItem>
                        )}
                        

                        
                      </GridContainer>
                    </>
                  ),
                },
                {
                  title: 'Estado Ocupación',
                  content: (
                    <GridContainer>
                      <GridItem xs={12} sm={12} md={12} lg={12}>
                        <Ocupacion
                          color='primary'
                          key={'OcupacionActual'}
                          fechaControl={
                            <ThemeProvider
                              theme={CAMPO_CALENDARIO_TEXTO_BLANCO}
                            >
                              <FormControl>
                                <TextField
                                  id='fechaHoraOcupacion'
                                  label='Fecha Hora Ocupación'
                                  type='datetime-local'
                                  defaultValue={this.state.fechaHoraActual}
                                  className={classes.textField}
                                  onChange={this.cambioFechaHoraOcupacion}
                                  InputLabelProps={{
                                    shrink: true,
                                  }}
                                />
                              </FormControl>
                            </ThemeProvider>
                          }
                          classCardIconTitle={classes.cardIconTitle}
                          titulo='Ocupación en las zonas'
                          icono={<PlaceIcon />}
                          contenido={datosOcupacionActual}
                        />
                      </GridItem>
                    </GridContainer>
                  ),
                },
              ]}
            />
          </CardBody>
        </Card>
      </GridItem>
    );
  }
}

const mapStateToProps = (state) => {
  return {
    principalState: state.principalState,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    solicitarDatosDashboardPrincipal: (datosConsulta, onSuccess) =>
      dispatch(solicitarDatosDashboardPrincipal(datosConsulta, onSuccess)),

    solicitarListarOcupacionZona: (datosConsulta, onSuccess) =>
      dispatch(solicitarListarOcupacionZona(datosConsulta, onSuccess)),
  };
};

export default withStyles(extendedTablesStyle)(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(Principal)
);
