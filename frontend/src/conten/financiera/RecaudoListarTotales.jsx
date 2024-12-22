import withStyles from "@material-ui/core/styles/withStyles";
import {
  whiteColor,
  infoColor,
  grayColor,
  successColor,
} from "assets/jss/material-dashboard-pro-react.jsx";
import GridContainer from "components/Grid/GridContainer.jsx";
import GridItem from "components/Grid/GridItem.jsx";
import { TEXTO_ROJO } from "config/general/Configuracion";
import PropTypes from "prop-types";
import React from "react";

function formatoDinero(contenido) {
  var local = ("" + contenido).replace(/[^\d]*/gi, "");
  var signo = "";
  try {
    if (("" + contenido).includes("-")) {
      signo = "-";
    }
  } catch (Exception) {}
  var resp = "";
  var contPos = 1;
  var tama = local.length - 1;
  for (var pos = tama; pos > -1; pos--) {
    resp = local.charAt(pos) + resp;
    if (contPos === 3 && pos - 1 > -1) {
      resp = "." + resp;
    }
    if (contPos === 6 && pos - 1 > -1) {
      resp = "'" + resp;
      contPos = 0;
    }
    contPos++;
  }

  return signo + "$" + resp;
}

function pintarFila(
  key,
  colorTexto,
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
        key={key + "-total1"}
        xs={12}
        sm={12}
        md={2}
        lg={2}
        style={{ backgroundColor: colorFila, color:colorTexto }}
      >
        {campo1}
      </GridItem>
      <GridItem
        key={key + "-total2"}
        xs={12}
        sm={12}
        md={2}
        lg={2}
        style={{ backgroundColor: colorFila, color:colorTexto }}
      >
        {campo2}
      </GridItem>
      <GridItem
        key={key + "-total3"}
        xs={12}
        sm={12}
        md={1}
        lg={1}
        style={{ backgroundColor: colorFila, color:colorTexto }}
      >
        {campo3}
      </GridItem>
      <GridItem
        key={key + "-total4"}
        xs={12}
        sm={12}
        md={1}
        lg={1}
        style={{ backgroundColor: colorFila, color:colorTexto }}
      >
        {campo4}
      </GridItem>
      <GridItem
        key={key + "-total5"}
        xs={12}
        sm={12}
        md={2}
        lg={2}
        style={{ backgroundColor: colorFila, color:colorTexto }}
      >
        {campo5}
      </GridItem>
      <GridItem
        key={key + "-total6"}
        xs={12}
        sm={12}
        md={2}
        lg={2}
        style={{ backgroundColor: colorFila, color:colorTexto }}
      >
        {campo6}
      </GridItem>
      <GridItem
        key={key + "-total7"}
        xs={12}
        sm={12}
        md={1}
        lg={1}
        style={{ backgroundColor: colorFila, color:colorTexto }}
      >
        {campo7}
      </GridItem>
      <GridItem
        key={key + "-total8"}
        xs={12}
        sm={12}
        md={1}
        lg={1}
        style={{ backgroundColor: colorFila, color:colorTexto }}
      >
        {campo8}
      </GridItem>
    </>
  );
}

function pintarContenido(classes, key, contenido) {
  var totalReportado = 0;
  var totalRecaudado = 0;
  var totalAbonado = 0;
  var diferencia = 0;
  var bolsa = 0;
  contenido.forEach((element, index) => {
    totalAbonado+= element.valorAbono;
    totalRecaudado += element.recaudado;
    if(element.reportado>= element.recaudado){
      bolsa += element.reportado - element.recaudado;
    }else{
      diferencia += element.reportado - element.recaudado;
    }
    if (element.cerrada) {
      totalReportado += element.reportado;
    }
  });
  return pintarFila(
    key + "-" + 1,
    whiteColor,
    successColor[0],
    <div style={{ marginTop: "4px" }}>
      {"Sub totales"}<br/>
      {"Totales"}
    </div>,
    <div style={{ marginTop: "4px" }}>
      {formatoDinero(totalReportado)}<br/>
      {formatoDinero(totalReportado+totalAbonado)}
    </div>,
    <div style={{ marginTop: "4px" }}>
    <br/>{formatoDinero(totalAbonado)} 
    </div>,
    <div style={{ marginTop: "4px" }}>{""}</div>,
    <div style={{ marginTop: "4px" }}>
      <br/>{formatoDinero(totalRecaudado)}
    </div>,
    <div
      style={ {marginTop: "4px"}}>
      <br/>{formatoDinero((diferencia))}
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      {formatoDinero((bolsa))}

    </div>,
    
    
    <div style={{ marginTop: "4px" }}>{""}</div>
  );
}

function RecaudoListarTotales({ ...props }) {
  var { classes, key, contenido } = props;
  if (contenido === undefined) {
    contenido = [];
  }
  return (
    <GridContainer style={{marginTop:"1px"}}>{pintarContenido(classes, key, contenido)}</GridContainer>
  );
}

RecaudoListarTotales.propTypes = {
  classes: PropTypes.object.isRequired,
  key: PropTypes.object.isRequired,
  contenido: PropTypes.object.isRequired,
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

export default withStyles()(RecaudoListarTotales);
