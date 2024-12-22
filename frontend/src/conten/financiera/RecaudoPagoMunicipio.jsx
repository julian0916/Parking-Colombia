import Checkbox from "@material-ui/core/Checkbox";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import InputAdornment from "@material-ui/core/InputAdornment";
import withStyles from "@material-ui/core/styles/withStyles";
import AttachMoneyIcon from "@material-ui/icons/AttachMoney";
import Check from "@material-ui/icons/Check";
import LocalAtmIcon from "@material-ui/icons/LocalAtm";
import CustomInput from "components/CustomInput/CustomInput.jsx";
import CustomTextarea from "components/CustomTextarea/CustomTextarea";
import GridContainer from "components/Grid/GridContainer.jsx";
import GridItem from "components/Grid/GridItem.jsx";
import PropTypes from "prop-types";
import React from "react";

function pintarContenido(
  classes,
  key,
  contenido,
  eventoCambioCerrado,
  eventoCambioFactura,
  eventoCambioValorConsignado,
  eventoCambioInformacion,
  eventoInformarGrabar
) {


  const styleCheck = { margin: "25px 0px 0px -10px" };
  return (
    <>
      <GridItem xs={12} sm={5} md={5}>
        <CustomInput
          labelText="No. Factura"
          id="etiquetaNoFactura"
          
          success
          disabled={(sessionStorage.getItem('idPerfil') != 106 && sessionStorage.getItem('idPerfil') != 101)}
          formControlProps={{
            fullWidth: true,
          }}
          inputProps={{
            name: "NoFactura",
            id: "NoFactura",
            value:
              contenido.codigoFactura === null ? "" : contenido.codigoFactura,
            onChange: (event) => eventoCambioFactura(event.target.value),
            onBlur: (event) => eventoInformarGrabar(event),
            endAdornment: (
              <InputAdornment position="start">
                <LocalAtmIcon />
              </InputAdornment>
            ),
          }}
        />
      </GridItem>
      <GridItem xs={12} sm={5} md={5}>
        <CustomInput
          labelText="Valor consignado"
          id="etiquetaValorConsignado"
          success
          disabled={(sessionStorage.getItem('idPerfil') != 106 && sessionStorage.getItem('idPerfil') != 101)}
          formControlProps={{
            fullWidth: true,
          }}
          inputProps={{
            name: "valorConsignado",
            id: "valorConsignado",
            value:
              contenido.valorConsignado === null
                ? ""
                : contenido.valorConsignado,
            onChange: (event) =>
              eventoCambioValorConsignado(event.target.value),
            onBlur: (event) => eventoInformarGrabar(event),
            endAdornment: (
              <InputAdornment position="start">
                <AttachMoneyIcon />
              </InputAdornment>
            ),
          }}
        />
      </GridItem>
      <GridItem xs={12} sm={2} md={2}>
        <FormControlLabel
          style={styleCheck}
          control={
            <Checkbox
              checkedIcon={<Check className={classes.checkedIcon} />}
              icon={<Check className={classes.uncheckedIcon} />}
              classes={{
                checked: classes.checked,
              }}
              checked={contenido.cerrado !== true ? false : contenido.cerrado}
              onChange={eventoCambioCerrado}
              disableRipple
              disabled={(sessionStorage.getItem('idPerfil') != 106 && sessionStorage.getItem('idPerfil') != 101)}
            />
          }
          label="Confirmado"
        ></FormControlLabel>
      </GridItem>
      <GridItem xs={12} sm={12} md={12}>
        <CustomTextarea
          labelText="Información"
          id="masInformacion"
          success
          disabled={(sessionStorage.getItem('idPerfil') != 106 && sessionStorage.getItem('idPerfil') != 101)}
          formControlProps={{
            fullWidth: true,
          }}
          inputProps={{
            value: contenido.informacion === null ? "" : contenido.informacion,
            onChange: (event) => eventoCambioInformacion(event.target.value),
            onBlur: (event) => eventoInformarGrabar(event),
          }}
        />
      </GridItem>
    </>
  );
}



function RecaudoPagoMunicipio({ ...props }) {
  var {
    classes,
    key,
    contenido,
    eventoCambioCerrado,
    eventoCambioFactura,
    eventoCambioValorConsignado,
    eventoCambioInformacion,
    eventoInformarGrabar,
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
        eventoCambioCerrado,
        eventoCambioFactura,
        eventoCambioValorConsignado,
        eventoCambioInformacion,
        eventoInformarGrabar
      )}
    </GridContainer>
  );
}

RecaudoPagoMunicipio.propTypes = {
  classes: PropTypes.object.isRequired,
  key: PropTypes.object.isRequired,
  contenido: PropTypes.object.isRequired,
  eventoCambioFactura: PropTypes.func,
  eventoInformarGrabar: PropTypes.func,
  color: PropTypes.oneOf([
    "primary",
    "warning",
    "danger",
    "success",
    "info",
    "rose",
    "gray",
  ]),
};

export default withStyles()(RecaudoPagoMunicipio);
