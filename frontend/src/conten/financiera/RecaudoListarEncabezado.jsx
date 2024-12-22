import InputAdornment from "@material-ui/core/InputAdornment";
import withStyles from "@material-ui/core/styles/withStyles";
import KeyboardIcon from "@material-ui/icons/Keyboard";
import CustomInput from "components/CustomInput/CustomInput.jsx";
import GridContainer from "components/Grid/GridContainer.jsx";
import GridItem from "components/Grid/GridItem.jsx";
import PropTypes from "prop-types";
import React from "react";
import ClearIcon from "@material-ui/icons/Clear";
import {
  CAMPO_CALENDARIO_TEXTO_BLANCO,
  BUSCAR_TABLA,
} from "config/general/Temas";
import { ThemeProvider } from "@material-ui/core/styles";
import FormControl from "@material-ui/core/FormControl";
import Input from "@material-ui/core/Input";
import InputLabel from "@material-ui/core/InputLabel";
import Divider from '@material-ui/core/Divider';

import {
  grayColor,
  roseColor,
  primaryColor,
  infoColor,
  successColor,
  warningColor,
  dangerColor,
  whiteColor,
  blackColor,
  twitterColor,
  facebookColor,
  googleColor,
  linkedinColor,
  pinterestColor,
  youtubeColor,
  tumblrColor,
  behanceColor,
  dribbbleColor,
  redditColor,
  hexToRgb,
} from "assets/jss/material-dashboard-pro-react.jsx";

function getTipoIcono(contenido, limpiarContenido) {
  var resultado = (
    <InputAdornment position="start">
      <KeyboardIcon />
    </InputAdornment>
  );
  if (contenido === undefined) {
    return resultado;
  }
  if (contenido === "") {
    return resultado;
  }
  resultado = (
    <InputAdornment
      position="start"
      style={{
        cursor: "pointer",
      }}
      onClick={() => {
        limpiarContenido();
      }}
    >
      <ClearIcon />
    </InputAdornment>
  );
  return resultado;
}

function getEstilo() {
  return {
    backgroundColor: successColor[0],
    color: whiteColor,
    fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
    fontSize: "16px",
    fontWeight: "400",
    lineHeight: "1.42857",
    opacity: "1",
    borderRadius: 3,
  };
}

function RecaudoListarEncabezado({ ...props }) {
  var {
    key,
    controlId,
    onChangeId,
    controlNombre,
    onChangeNombre,
    limpiarContenidoId,
    limpiarContenidoNombre,
  } = props;
  const xs_sm = 12;
  return (
    <GridContainer  container alignItems="center" style={getEstilo()}>
      {/*<GridItem xs={12} sm={6} md={1} lg={1}>
        <CustomInput
          labelText="Id"
          id="idPromotor"
          success
          formControlProps={{
            fullWidth: true,
          }}
          inputProps={{
            key: "PomotorTitulo" + key,
            name: "idPromotor",
            id: "idPromotor",
            value: controlId,
            onChange: onChangeId,
            endAdornment: (
              getTipoIcono(controlId,limpiarContenidoId)
            ),
          }}
        />
      </GridItem>*/}
      <GridItem xs={xs_sm} sm={xs_sm} md={2} lg={2} >
        {/*<ThemeProvider theme={BUSCAR_TABLA}>
        <CustomInput
          labelText="Nombre"
          id="nombrePromotor"
          formControlProps={{
            fullWidth: true,
          }}
          inputProps={{
            key: "NombreTitulo" + key,
            name: "nombrePromotor",
            id: "nombrePromotor",
            value: controlNombre,
            onChange: onChangeNombre,
            endAdornment: (
              getTipoIcono(controlNombre,limpiarContenidoNombre)
            ),
          }}
        />
        </ThemeProvider> */}
        <ThemeProvider theme={CAMPO_CALENDARIO_TEXTO_BLANCO}>
          <FormControl fullWidth>
            <InputLabel style={{ fontSize: "16px", color: whiteColor }}>
              {"Nombre"}
            </InputLabel>
            <Input
              style={{ fontSize: "16px", color: whiteColor }}
              key={"filtrarPorNombre"}
              value={controlNombre}
              inputProps={{ "roboto-label": "description" }}
              onChange={onChangeNombre}
              endAdornment={getTipoIcono(controlNombre, limpiarContenidoNombre)}
            />
          </FormControl>
        </ThemeProvider>
        <Divider orientation="vertical" flexItem />
      </GridItem>
      <Divider style={{backgroundColor:whiteColor}} orientation="vertical" flexItem />
      <GridItem xs={xs_sm} sm={xs_sm} md={2} lg={2} style={getEstilo()}>
      
        <div
          style={{
            marginTop: "32px",
          }}
        >
          {"Planilla"}
        </div>
      </GridItem>
      <Divider style={{backgroundColor:whiteColor}} orientation="vertical" flexItem />

      <GridItem xs={xs_sm} sm={xs_sm} md={1} lg={1} style={getEstilo()}>
      
      <div
        style={{
          marginTop: "32px",
        }}
      >
        {"Abono"}
      </div>
    </GridItem>
    

      <Divider style={{backgroundColor:whiteColor}} orientation="vertical" flexItem />
      <GridItem xs={xs_sm} sm={xs_sm} md={1} lg={1}>
        <div style={{ marginTop: "32px" }}>{"OK"}</div>
      </GridItem>
      <Divider style={{backgroundColor:whiteColor}} orientation="vertical" flexItem />
      <GridItem xs={xs_sm} sm={xs_sm} md={2} lg={2}>
        <div style={{ marginTop: "32px" }}>{"Recaudado"}</div>
      </GridItem>
      <Divider style={{backgroundColor:whiteColor}} orientation="vertical" flexItem />
      <GridItem xs={xs_sm} sm={xs_sm} md={1} lg={1}>
        <div style={{ marginTop: "32px" }}>{"Diferencia"}</div>
      </GridItem>
      <GridItem xs={xs_sm} sm={xs_sm} md={1} lg={1}>
        <div style={{ marginTop: "32px" }}>{"A la bolsa"}</div>
      </GridItem>
      <Divider style={{backgroundColor:whiteColor}} orientation="vertical" flexItem />
      <GridItem xs={xs_sm} sm={xs_sm} md={1} lg={1} >
        <div style={{ marginTop: "32px" }}><Divider orientation="vertical" flexItem />{"Saldo"}</div>
      </GridItem>
    </GridContainer>
  );
}

RecaudoListarEncabezado.propTypes = {
  controlId: PropTypes.object.isRequired,
  onChangeId: PropTypes.object.isRequired,
  controlNombre: PropTypes.object.isRequired,
  onChangeNombre: PropTypes.object.isRequired,
  key: PropTypes.object.isRequired,
  limpiarContenidoId: PropTypes.object.isRequired,
  limpiarContenidoNombre: PropTypes.object.isRequired,
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

export default withStyles()(RecaudoListarEncabezado);
