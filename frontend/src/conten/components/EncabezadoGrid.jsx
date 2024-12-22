import Divider from "@material-ui/core/Divider";
import ArrowDropDownIcon from "@material-ui/icons/ArrowDropDown";
import ArrowDropUpIcon from "@material-ui/icons/ArrowDropUp";
import {
  successColor,
  whiteColor,
} from "assets/jss/material-dashboard-pro-react.jsx";
import GridContainer from "components/Grid/GridContainer.jsx";
import GridItem from "components/Grid/GridItem.jsx";
import PropTypes from "prop-types";
import React from "react";

function getEstilo(colorFondo, colorTexto, tamaLetra) {
  if (colorFondo === undefined) {
    colorFondo = successColor[0];
  }
  if (colorTexto === undefined) {
    colorTexto = whiteColor;
  }
  if (tamaLetra === undefined) {
    tamaLetra = "16px";
  }
  return {
    backgroundColor: colorFondo,
    color: colorTexto,
    fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
    fontSize: tamaLetra,
    fontWeight: "400",
    lineHeight: "1.42857",
    opacity: "1",
    borderRadius: 3,
  };
}

function getIconoPorOrden(element, sentidoOrden, ordenarPor) {
  if (element === undefined) {
    return <></>;
  }
  if (element.isSorted === false) {
    return <></>;
  }
  if (element.id !== ordenarPor) {
    return <></>;
  }
  if (sentidoOrden === undefined) {
    return <ArrowDropDownIcon />;
  }
  if (sentidoOrden === "ASC") {
    return <ArrowDropDownIcon />;
  }
  return <ArrowDropUpIcon />;
}

function getCursor(element) {
  if (element == undefined) {
    return {};
  }
  if (element.isSorted === false) {
    return {};
  }
  return {
    cursor: "pointer",
  };
}

function pintarColumn(
  key,
  contenido,
  index,
  colorFondo,
  colorTexto,
  eventoOrdenar,
  ordenarPor,
  sentidoOrden
) {
  return (
    <GridItem
      key={"GridItem" + key}
      xs={contenido.xs}
      sm={contenido.sm}
      md={contenido.md}
      lg={contenido.lg}
    >
      {contenido.campos.map((elem, index) => {
        return (
          <div
            key={"DivGridItem" + key + "-" + index}
            style={getCursor(elem)}
            onClick={(event) => {
              elem.isSorted ? eventoOrdenar(event, elem.id) : "";
            }}
          >
            {elem.label}
            {getIconoPorOrden(elem, sentidoOrden, ordenarPor)}
          </div>
        );
      })}
    </GridItem>
  );
}

function EncabezadoGrid({ ...props }) {
  const {
    contenidoColumnas,
    eventoOrdenar,
    ordenarPor,
    sentidoOrden,
    colorFondo,
    colorTexto,
    tamaLetra,
  } = props;
  const ultimo = contenidoColumnas.length-1;
  return (
    <GridContainer
      key={"GridContainer-Encabezado"}
      container
      alignItems="center"
      style={getEstilo(colorFondo, colorTexto, tamaLetra)}
    >
      {contenidoColumnas.map((elemento, index) => {
        return (
          <>
            {pintarColumn(
              "ColumnaOrdenar-" + index,
              elemento,
              index,
              colorFondo,
              colorTexto,
              eventoOrdenar,
              ordenarPor,
              sentidoOrden
            )}
            {index < ultimo && (
              <Divider
                key={"divider-" + index}
                style={{ 
                  backgroundColor: colorTexto, 
                  color: colorTexto,
                  marginLeft:"-1px"
                 }}
                orientation="vertical"
                flexItem
              />
            )}
          </>
        );
      })}
    </GridContainer>
  );
}

EncabezadoGrid.propTypes = {
  contenidoColumnas: PropTypes.array.isRequired,
  eventoOrdenar: PropTypes.func.isRequired,
  ordenarPor: PropTypes.string.isRequired,
  sentidoOrden: PropTypes.string.isRequired,
  colorFondo: PropTypes.string.isRequired,
  colorTexto: PropTypes.string.isRequired,
  tamaLetra: PropTypes.string.isRequired,
};

export default EncabezadoGrid;
