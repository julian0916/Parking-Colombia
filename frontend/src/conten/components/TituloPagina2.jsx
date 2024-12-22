import withStyles from "@material-ui/core/styles/withStyles";
import extendedTablesStyle from "assets/jss/material-dashboard-pro-react/views/extendedTablesStyle";
import classNames from "classnames";
import PropTypes from "prop-types";
import React from "react";

function TituloPagina2({ ...props }) {
  const { classes, texto, color } = props;
  const tituloClasses = classNames({
    [classes[color + "TituloPagina2"]]: color,
  });

  return <div className={tituloClasses}>{texto}</div>;
}

TituloPagina2.defaultProps = {
  color: "info",
};

TituloPagina2.propTypes = {
  texto: PropTypes.string.isRequired,
  classes: PropTypes.object.isRequired,
  className: PropTypes.string,
  color: PropTypes.oneOf([
    "warning",
    "success",
    "danger",
    "info",
    "primary",
    "rose",
    "green",
  ]),
};

//export default TituloPagina2;
export default withStyles(extendedTablesStyle)(TituloPagina2);
