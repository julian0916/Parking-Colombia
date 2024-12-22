import PropTypes from "prop-types";
import React from "react";
import { successColor } from "assets/jss/material-dashboard-pro-react";

function TituloPagina({ ...props }) {
  const { texto, clasesTituloPagina } = props;

  return (
    <h5 className={clasesTituloPagina} style={{ color: successColor[0] }}>
      <b>{texto}</b>
    </h5>
  );
}

TituloPagina.propTypes = {
  texto: PropTypes.string.isRequired,
  clasesTituloPagina:PropTypes.string.style,
};

export default TituloPagina;
