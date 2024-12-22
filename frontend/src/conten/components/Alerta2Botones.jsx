import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import DialogTitle from "@material-ui/core/DialogTitle";
import withStyles from "@material-ui/core/styles/withStyles";
import Button from "components/CustomButtons/Button.jsx";
import PropTypes from "prop-types";
import React from "react";

function RecaudoListarContenido({ ...props }) {
  var {
    key,
    titulo,
    mensaje,
    textoBotonAceptar,
    textoBotonCancelar,
    eventoBotonAceptar,
    eventoBotonCancelar,
    iconoAceptar,
    iconoCancelar,
    contenido,
  } = props;

  return (
    <Dialog
      key={"Dialog" + key}
      open={true}
      onClose={eventoBotonCancelar}
      aria-labelledby="form-dialog-title"
    >
      <DialogTitle id="form-dialog-title">{titulo}</DialogTitle>
      <DialogContent>
        <DialogContentText>{mensaje}</DialogContentText>
      </DialogContent>
      <DialogActions>
        <Button
          type="button"
          onClick={(even) => eventoBotonAceptar(contenido)}
          color="green"
        >
          {iconoAceptar}
          {textoBotonAceptar}
        </Button>
        <Button
          type="button"
          onClick={(even) => eventoBotonCancelar(contenido)}
          color="danger"
        >
          {iconoCancelar}
          {textoBotonCancelar}
        </Button>
      </DialogActions>
    </Dialog>
  );
}

RecaudoListarContenido.propTypes = {
  key: PropTypes.object.isRequired,
  titulo: PropTypes.object.isRequired,
  mensaje: PropTypes.object.isRequired,
  contenido: PropTypes.object.isRequired,
  textoBotonAceptar: PropTypes.object.isRequired,
  textoBotonCancelar: PropTypes.object.isRequired,
  eventoBotonAceptar: PropTypes.object.isRequired,
  eventoBotonCancelar: PropTypes.object.isRequired,
  iconoAceptar: PropTypes.object.isRequired,
  iconoCancelar: PropTypes.object.isRequired,

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

export default withStyles()(RecaudoListarContenido);
