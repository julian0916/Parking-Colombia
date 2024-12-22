import PropTypes from "prop-types";
import React from "react";
import withStyles from "@material-ui/core/styles/withStyles";

import badgeStyle from "assets/jss/material-dashboard-pro-react/components/badgeStyle.jsx";

function CampoTablaTexarea({ ...props }) {
  const { classes,mostrar, texto, color, ancho, icono } = props;

  return mostrar ? (
    <span>
      {icono}
      <span style={{ width: ancho }} className={classes.badge + " " + classes[color]}>{texto}</span>
    </span>
  ) : (
    <></>
  );
}

CampoTablaTexarea.propTypes = {
  icono: PropTypes.node,
  mostrar: PropTypes.bool.isRequired,
  texto: PropTypes.string.isRequired,
  color: PropTypes.string,
  ancho: PropTypes.string,
  classes: PropTypes.object.isRequired,
  color: PropTypes.oneOf([
    "primary",
    "warning",
    "danger",
    "success",
    "info",
    "rose",
    "gray",
  ]),
  children: PropTypes.node,
};

export default withStyles(badgeStyle)(CampoTablaTexarea);
