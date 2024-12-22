import { Checkbox, FormControlLabel, MenuItem } from '@material-ui/core';
import FormControl from '@material-ui/core/FormControl';
import withStyles from '@material-ui/core/styles/withStyles';
import TextField from '@material-ui/core/TextField';
import { Check } from '@material-ui/icons';
import AssessmentIcon from '@material-ui/icons/Assessment';
import {
  primaryColor,
  whiteColor,
} from 'assets/jss/material-dashboard-pro-react.jsx';
import extendedTablesStyle from 'assets/jss/material-dashboard-pro-react/views/extendedTablesStyle.jsx';
import Card from 'components/Card/Card.jsx';
import CardBody from 'components/Card/CardBody.jsx';
import CardHeader from 'components/Card/CardHeader.jsx';
import CardIcon from 'components/Card/CardIcon.jsx';
import CustomSelect from 'components/CustomSelect/CustomSelect';
import GridContainer from 'components/Grid/GridContainer';
import GridItem from 'components/Grid/GridItem.jsx';

import {
  formatDate,
  formatDateTimeReporte,
  formatNumberDate,
} from 'config/funciones/Comunes';
import {
  CONFIGURACION_TIPOGRAFIA_14,
  FILA_BLANCA,
  FILA_GRIS,
  OK,
} from 'config/general/Configuracion';
import TituloPagina2 from 'conten/components/TituloPagina2';
import WaitDialog from 'conten/components/WaitDialog.jsx';

import { informeHistorico } from 'conten/informes/InformesActions';

import { listarUsuario } from 'conten/usuarios/UsuarioActions';
import React from 'react';
import { connect } from 'react-redux';

class InformeHistorico extends React.Component {
  constructor(props) {
    super(props);
    const fechaActual = formatDate(new Date(Date.now()), 'yyyy-mm-dd');
    const fechaActualAnio = new Date(Date.now());
    this.state = {
      yearActual: fechaActualAnio.getFullYear(),
      contenidoAlerta: null,
      textAlertInfo: 'Un momento mientras completamos su solicitud.',
      fechaActual: fechaActual,
      zonaSeleccionada: '',
      datosConsulta: {
        fechaConsulta: formatNumberDate(fechaActual),
        year: fechaActualAnio.getFullYear(),
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
        mes: 0,
        paginacion: {
          limite: 130000, //limite de consultas por pagina
          actual: 1,
          filtro: null,
          orden: null,
          sentido: null,
        },
      },
      reporte: [],
      listaDeReportes: [], //Crea una lista de reportes con los meses y años seleccionados
    };
  }

  handleDateChange = (evento) => {
    const nuevaFecha = evento.target.value;
    var datosConsulta = { ...this.state.datosConsulta };
    datosConsulta.fechaConsulta = formatNumberDate(nuevaFecha);
    this.setState({ datosConsulta }, () => {});
  };

  cambioContenidoYear = (year) => {
    var datosConsulta = this.state.datosConsulta;
    datosConsulta.year = year;
    console.log('valor anio: ' + year);
    this.setState({ datosConsulta });
  };

  cambioContenidoMes = (valor, campo) => {
    const datosConsulta = { ...this.state.datosConsulta };
    const numeroMes = this.obtenerNumeroMes(campo);
    datosConsulta[campo] = valor ? numeroMes : null;
    datosConsulta.mes = valor ? numeroMes : null;

    if (!valor) {
      // Si el mes se deselecciona, borrar el informe correspondiente
      this.setState((prevState) => {
        const key = `${prevState.datosConsulta.year}${numeroMes}`;
        const nuevaListaDeReportes = { ...prevState.listaDeReportes };
        delete nuevaListaDeReportes[key];

        return {
          reporte: prevState.reporte.filter((item) => item.mes !== numeroMes),
          listaDeReportes: nuevaListaDeReportes,
          datosConsulta,
        };
      });
    } else {
      // Si el mes se selecciona, recuperar el informe para el mes seleccionado
      this.setState({ datosConsulta }, this.recuperarReporteHistoricoMensual);
    }
  };
  obtenerNumeroMes = (nombreMes) => {
    switch (nombreMes) {
      case 'enero':
        return 1;
      case 'febrero':
        return 2;
      case 'marzo':
        return 3;
      case 'abril':
        return 4;
      case 'mayo':
        return 5;
      case 'junio':
        return 6;
      case 'julio':
        return 7;
      case 'agosto':
        return 8;
      case 'septiembre':
        return 9;
      case 'octubre':
        return 10;
      case 'noviembre':
        return 11;
      case 'diciembre':
        return 12;
      default:
        return null;
    }
  };

  verificarMes = (fecha, mes) => {
    var result = false;
    try {
      result = fecha.getMonth() === mes - 1;
    } catch (excep) {}
    return result;
  };

  recuperarReporte = (descargarExcel = false) => {
    this.props.informeHistorico(
      this.state.datosConsulta,
      'consultaFecha',
      (estado, contenido) =>
        this.respuestaInformeHistorico(estado, contenido, descargarExcel)
    );
  };
  recuperarReporteHistoricoMensual = () => {
    this.props.informeHistorico(
      this.state.datosConsulta,
      'consultaMesYear',
      this.respuestaInformeHistorico
    );
  };
  respuestaInformeHistorico = (estado, contenido, descargarExcel = false) => {
    if (estado !== 'OK') {
      return;
    }

    const nuevoReporte = contenido.reporte;
    console.log(
      'Lista de reportes antes de la actualización:',
      this.state.listaDeReportes
    );

    this.setState(
      (prevState) => {
        const key = `${prevState.datosConsulta.year}${prevState.datosConsulta.mes}`;
        const nuevaListaDeReportes = {
          ...prevState.listaDeReportes,
          [key]: [...(prevState.listaDeReportes[key] || []), ...nuevoReporte],
        };

        console.log('Nueva lista de reportes:', nuevaListaDeReportes);
        const reporteConcatenado = Object.values(nuevaListaDeReportes).flat();

        return {
          listaDeReportes: nuevaListaDeReportes,
          reporte: reporteConcatenado,
          datosConsulta: {
            ...prevState.datosConsulta,
            paginacion: contenido.paginacion,
          },
        };
      },
      () => {
        if (descargarExcel) {
          const { listaDeReportes } = this.state;
          let reporte = this.unificarReportes(listaDeReportes);
          this.descargarCSV(this.getContenidosXLS(reporte));
        }
      }
    );
  };

  handleChangeZona = (event) => {
    const { listaDeReportes } = this.state;
    const zonaSeleccionada = event.target.value;
    this.setState({ zonaSeleccionada }, () => {
      let reporte = this.unificarReportes(listaDeReportes);

      this.setState(
        reporte.filter((item) => item.nombre_zona === zonaSeleccionada),
        this.descargarCSV(
          this.getContenidosXLS(
            reporte.filter((item) => item.nombre_zona === zonaSeleccionada)
          )
        )
      );
      console.log(
        'Reporte después de filtrar:',
        reporte.filter((item) => item.nombre_zona === zonaSeleccionada)
      );
    });
  };
  descargarCSV = (data) => {
    let reporte = [...data]; // Clonar datos para mantener los datos originales
    const keys = Object.keys(reporte[0]);
    const csvContent = [
      keys.join(','),
      ...reporte.map((item) => keys.map((key) => item[key]).join(',')),
    ].join('\n');

    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `Informe Historico.csv`;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
  };

  getContenidosXLS = (contenidos) => {
    var resul = [];
    contenidos.map((reg) => {
      var cont = {};
      cont['Estado'] = reg.nombre_estado;
      cont['Placa'] = reg.placa;
      cont['Vehiculo'] = reg.es_carro === true ? 'Carro' : 'Moto';
      cont['Id cobro'] = reg.id + '';
      cont['Zona'] = reg.nombre_zona;
      cont['Zona inicia'] = reg.h_inicia_zona;
      cont['Zona termina'] = reg.h_termina_zona;
      cont['Zona valor hora'] = reg.valor_h;
      cont['Zona minutos gracia'] = reg.minutos_gracia_zona;
      cont['Zona minutos para nueva gracia'] =
        reg.minutos_para_nueva_gracia_zona;
      cont['Fecha hora ingreso'] = formatDateTimeReporte(reg.fh_ingreso);
      cont['Promotor ingreso'] = reg.nombre_promotor_ingreso;
      cont['Fecha hora egreso'] = formatDateTimeReporte(reg.fh_egreso);
      cont['Promotor egreso'] = reg.nombre_promotor_egreso;
      cont['Horas cobradas'] = reg.h_cobradas;
      cont['Valor cobrado'] = reg.valor_cobrado;
      cont['Fecha hora recaudo'] = formatDateTimeReporte(reg.fh_recaudo);
      cont['Promotor recauda'] = reg.nombre_promotor_recauda;
      cont['Promotor reporta'] = reg.nombre_promotor_reporta;
      resul.push(cont);
    });

    return resul;
  };
  renderCheckMes = (campo, classes) => {
    const numeroMes = this.obtenerNumeroMes(campo);
    const chequeado = this.esMesChequeado(
      this.state.datosConsulta.year,
      numeroMes
    );

    return (
      <GridItem xs={12} sm={3} md={2} lg={2}>
        <FormControlLabel
          style={{ margin: '0px 0px 0px -10px' }}
          control={
            <Checkbox
              checkedIcon={<Check className={classes.checkedIcon} />}
              icon={<Check className={classes.uncheckedIcon} />}
              classes={{
                checked: classes.checked,
              }}
              checked={chequeado}
              onChange={(event) => this.cambioContenidoMes(!chequeado, campo)}
              disableRipple
            />
          }
          label={campo}
        />
      </GridItem>
    );
  };
  esMesChequeado = (year, mes) => {
    const key = `${year}${mes}`;
    return !!this.state.listaDeReportes[key];
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
  unificarReportes = (listaDeReportes) => {
    return Object.values(listaDeReportes).flat();
  };
  getZonas = () => {
    const { listaDeReportes, zonasUnicas } = this.state;

    // Verificar si ya se han obtenido las zonas únicas
    if (zonasUnicas && zonasUnicas.length > 0) {
      return;
    }
    // Obtener todas las zonas únicas de los reportes
    const zonasUnicasNuevas = [
      ...new Set(
        Object.values(listaDeReportes)
          .flat()
          .map((reporte) => {
            return reporte.nombre_zona;
          })
      ),
    ];

    // Actualiza el estado con las zonas únicas
    this.setState({ zonasUnicas: zonasUnicasNuevas });
  };

  render() {
    const { classes } = this.props;
    const { isActivityIndicatorShown } = this.props.informesState;
    const { listaDeReportes } = this.state;

    // Unificar reportes
    const reporte = this.unificarReportes(listaDeReportes);

    return (
      <GridItem xs={12}>
        {isActivityIndicatorShown && (
          <WaitDialog text={this.state.textAlertInfo} />
        )}
        {this.state.contenidoAlerta}
        <Card>
          <CardHeader icon>
            <CardIcon color='primary'>
              <button
                onClick={() =>
                  this.descargarCSV(this.getContenidosXLS(reporte))
                }
                style={{
                  cursor: 'pointer',
                  backgroundColor: primaryColor[0],
                  color: whiteColor,
                  border: '0px',
                }}
              >
                <b>+ Descargar CSV</b>
              </button>

              <AssessmentIcon />
            </CardIcon>
            <TituloPagina2
              texto='Informe Histórico'
              color='primary'
              classes={classes}
            />
          </CardHeader>
          <CardBody>
            <GridContainer key={'GridContainer-parametros'} container>
              <GridContainer
                key={'GridContainer-parametros'}
                container
                alignItems='center'
                justifyContent='center'
                style={{
                  fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
                  fontSize: '14px',
                  fontWeight: '400',
                  lineHeight: '1.42857',
                  opacity: '1',
                  weight: '100vh',
                  display: 'flex',
                  justifyContent: 'space-evenly',
                  margin: '0',
                }}
              >
                {this.renderCheckMes('enero', classes)}
                {this.renderCheckMes('febrero', classes)}
                {this.renderCheckMes('marzo', classes)}
                {this.renderCheckMes('abril', classes)}
                {this.renderCheckMes('mayo', classes)}
                {this.renderCheckMes('junio', classes)}
                {this.renderCheckMes('julio', classes)}
                {this.renderCheckMes('agosto', classes)}
                {this.renderCheckMes('septiembre', classes)}
                {this.renderCheckMes('octubre', classes)}
                {this.renderCheckMes('noviembre', classes)}
                {this.renderCheckMes('diciembre', classes)}

                <GridItem xs={4} sm={4} md={4} lg={4}>
                  <CustomSelect
                    key='year'
                    success
                    labelText='* Año'
                    id='year'
                    formControlProps={{
                      fullWidth: true,
                    }}
                    inputProps={{
                      name: 'year',
                      id: 'year',
                      value: this.state.datosConsulta.year,
                      onChange: (event) =>
                        this.cambioContenidoYear(event.target.value),
                    }}
                    selectContent={this.getYears()}
                  />
                </GridItem>
                <GridItem xs={4} sm={4} md={4} lg={4}>
                  <CustomSelect
                    key='zona'
                    success
                    labelText='Zona'
                    id='zona'
                    formControlProps={{
                      fullWidth: true,
                    }}
                    inputProps={{
                      name: 'zona',
                      id: 'zona',
                      value: this.state.zonaSeleccionada,
                      onChange: this.handleChangeZona,
                      onFocus: this.getZonas,
                    }}
                    selectContent={
                      !this.state.zonasUnicas
                        ? null
                        : this.state.zonasUnicas.map((zona) => (
                            <MenuItem key={zona} value={zona}>
                              {zona}
                            </MenuItem>
                          ))
                    }
                  />
                </GridItem>
                <GridItem
                  xs={6}
                  sm={6}
                  md={4}
                  lg={4}
                  style={{
                    display: 'flex',
                    flexDirection: 'row',
                    backgroundColor: 'rgb(112, 173, 162)',
                    padding: '5px',
                    borderRadius: '5px',
                  }}
                >
                  <FormControl fullWidth>
                    <TextField
                      id='fechaConsulta'
                      label='Fecha Consulta'
                      type='date'
                      defaultValue={this.state.fechaActual}
                      className={classes.textField}
                      onBlur={this.handleDateChange}
                      InputLabelProps={{
                        shrink: true,
                      }}
                    />
                  </FormControl>
                  <button
                    onClick={() => this.recuperarReporte(true)}
                    style={{
                      cursor: 'pointer',
                      backgroundColor: primaryColor[0],
                      color: whiteColor,
                      border: '0px',
                      borderRadius: '5px',
                      marginLeft: '10px',
                      marginTop: '5px',
                      marginBottom: '5px',
                      height: '45px',
                    }}
                  >
                    Descargar
                  </button>
                </GridItem>
              </GridContainer>
            </GridContainer>
            <GridContainer
              key={'GridContainer-encabezado'}
              container
              alignItems='center'
              style={CONFIGURACION_TIPOGRAFIA_14}
            ></GridContainer>
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

    informeHistorico: (parametros, tipoConsulta, onSuccess) =>
      dispatch(informeHistorico(parametros, tipoConsulta, onSuccess)),
  };
};

export default withStyles(extendedTablesStyle)(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(InformeHistorico)
);
