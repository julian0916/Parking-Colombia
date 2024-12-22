import Checkbox from '@material-ui/core/Checkbox';
import FormControl from '@material-ui/core/FormControl';
import Input from '@material-ui/core/Input';
import withStyles from '@material-ui/core/styles/withStyles';
import Check from '@material-ui/icons/Check';
import GridContainer from 'components/Grid/GridContainer.jsx';
import GridItem from 'components/Grid/GridItem.jsx';
import {
  FILA_BLANCA,
  FILA_GRIS,
  TEXTO_ROJO,
} from 'config/general/Configuracion';
import PropTypes from 'prop-types';
import React from 'react';
import { formatoDinero } from 'config/funciones/Comunes';
import { ID_PERFIL } from 'config/general/Configuracion';
import { NOMBRE_PERFIL } from 'config/general/Configuracion';
import { PERFIL_PROMOTOR } from 'config/general/Configuracion';

function pintarFila(
  key,
  colorFila,
  campo1,
  campo2,
  campo3,
  campo4,
  campo5,
  campo6,
  campo7,
  campo8
) {
  return (
    <>
      <GridItem
        key={key + '-campo1'}
        xs={12}
        sm={12}
        md={2}
        lg={2}
        style={{ backgroundColor: colorFila }}
      >
        {campo1}
      </GridItem>
      <GridItem
        key={key + '-campo2'}
        xs={12}
        sm={12}
        md={2}
        lg={2}
        style={{ backgroundColor: colorFila }}
      >
        {campo2}
      </GridItem>
      <GridItem
        key={key + '-campo3'}
        xs={12}
        sm={12}
        md={1}
        lg={1}
        style={{ backgroundColor: colorFila }}
      >
        {campo3}
      </GridItem>
      <GridItem
        key={key + '-campo4'}
        xs={12}
        sm={12}
        md={1}
        lg={1}
        style={{ backgroundColor: colorFila }}
      >
        {campo4}
      </GridItem>
      <GridItem
        key={key + '-campo5'}
        xs={12}
        sm={12}
        md={2}
        lg={2}
        style={{ backgroundColor: colorFila }}
      >
        {campo5}
      </GridItem>
      <GridItem
        key={key + '-campo6'}
        xs={12}
        sm={12}
        md={1}
        lg={1}
        style={{ backgroundColor: colorFila }}
      >
        {campo6}
      </GridItem>
      <GridItem
        key={key + '-campo7'}
        xs={12}
        sm={12}
        md={1}
        lg={1}
        style={{ backgroundColor: colorFila }}
      >
        {campo7}
      </GridItem>
      <GridItem
        key={key + '-campo8'}
        xs={12}
        sm={12}
        md={2}
        lg={2}
        style={{ backgroundColor: colorFila }}
      >
        {campo8}
      </GridItem>
    </>
  );
}

function pintarContenido(
  classes,
  key,
  contenido,
  eventoCambioValor,
  eventoCambioAbono,
  eventoCambioEstado,
  onBlurCambioValor
) {
  var resul = [];
  contenido.forEach((element, index) => {
    //console.log("--pago ", element);
    let diferencia = element.reportadoFormato - element.recaudado;
    const styles = {
      underline: {
        '&:before': {
          borderBottomColor: 'blue', // Cambia el color del subrayado antes del texto
        },
        '&:after': {
          borderBottomColor: 'blue', // Cambia el color del subrayado después del texto (en el estado de foco)
        },
      },
    };
    const StyledInput = withStyles(styles)(Input);

    if (element.mostrar) {
      resul.push(
        pintarFila(
          key + '-' + index,

          index % 2 === 0 ? FILA_GRIS : FILA_BLANCA,

          <div style={{ marginTop: '4px' }}>{element.nombrePromotor}</div>,

          <FormControl fullWidth>
            <Input
              style={{ color: 'rgba(0, 0, 0, 0.6)', fontWeight: 'bold' }}
              key={key + '-' + index + 'valorReportado'}
              value={formatoDinero(element.reportadoFormato)}
              inputProps={{ 'roboto-label': 'description' }}
              onChange={(evento) => eventoCambioValor(evento, element, index)}
              onBlur={(evento) => onBlurCambioValor(evento, element, index)}
              disabled={(sessionStorage.getItem('idPerfil') != 106 && sessionStorage.getItem('idPerfil') != 101)}
            />
          </FormControl>,

          <FormControl fullWidth>
            <Input
              style={{ color: 'rgba(0, 0, 0, 0.6)', fontWeight: 'bold' }}
              key={key + '-' + index + 'valorAbono'}
              value={formatoDinero(element.valorAbonoFormato)}
              inputProps={{ 'roboto-label': 'description' }}
              onChange={(evento) => eventoCambioAbono(evento, element, index)}
              onBlur={(evento) => onBlurCambioValor(evento, element, index)}
              disabled={(sessionStorage.getItem('idPerfil') != 106 && sessionStorage.getItem('idPerfil') != 101)}
            />
          </FormControl>,

          <Checkbox
            style={{ marginLeft: '-10px' }}
            key={key + '-' + index + 'controlCerrado'}
            checkedIcon={
              <Check
                className={classes.checkedIcon}
                style={{
                  color: 'rgb(84, 116, 74)',
                  borderColor: 'rgb(22, 47, 74)',
                }}
              />
            } // Cambiar color del icono de verificación cuando está seleccionado
            icon={<Check className={classes.uncheckedIcon} />}
            classes={{
              checked: classes.checked,
            }}
            checked={element.cerrada}
            onChange={(event) => eventoCambioEstado(event, element, index)}
            disabled={(sessionStorage.getItem('idPerfil') != 106 && sessionStorage.getItem('idPerfil') != 101)}
          />,

          element.cerrada && (
            <div style={element.recaudado < 0 ? { color: TEXTO_ROJO } : {}}>
              {formatoDinero(element.recaudado)}
            </div>
          ),

          element.cerrada && (
            <div style={diferencia < 0 ? { color: TEXTO_ROJO } : {}}>
              {formatoDinero(
                element.reportadoFormato - element.recaudado < 0
                  ? diferencia
                  : 0
              )}
            </div>
          ),

          element.cerrada && (
            <div style={diferencia > 0 ? { color: TEXTO_ROJO } : {}}>
              {formatoDinero(
                element.reportadoFormato - element.recaudado > 0
                  ? diferencia
                  : 0
              )}
            </div>
          ),

          element.cerrada && (
            <>
              <div style={element.saldo < 0 ? { color: TEXTO_ROJO } : {}}>
                {'Parcial: ' + formatoDinero(element.saldo)}
              </div>
              <div
                style={element.saldo_cuenta < 0 ? { color: TEXTO_ROJO } : {}}
              >
                {'Total: ' + formatoDinero(element.saldo_cuenta)}
              </div>
            </>
          )
        )
      );
    }
  });

  return resul;
}

function RecaudoListarContenido({ ...props }) {
  var {
    classes,
    key,
    contenido,
    eventoCambioValor,
    eventoCambioAbono,
    eventoCambioEstado,
    onBlurCambioValor,
  } = props;
  if (contenido === undefined) {
    contenido = [];
  }
  return (
    <GridContainer>
      {pintarContenido(
        classes,
        key,
        contenido,
        eventoCambioValor,
        eventoCambioAbono,
        eventoCambioEstado,
        onBlurCambioValor
      )}
    </GridContainer>
  );
}

RecaudoListarContenido.propTypes = {
  classes: PropTypes.object.isRequired,
  key: PropTypes.object.isRequired,
  contenido: PropTypes.object.isRequired,
  eventoCambioValor: PropTypes.object.isRequired,
  eventoCambioAbono: PropTypes.object.isRequired,
  eventoCambioEstado: PropTypes.object.isRequired,
  onBlurCambioValor: PropTypes.object.isRequired,
  color: PropTypes.oneOf([
    'primary',
    'warning',
    'danger',
    'success',
    'info',
    'rose',
    'gray',
  ]),
};

export default withStyles()(RecaudoListarContenido);
