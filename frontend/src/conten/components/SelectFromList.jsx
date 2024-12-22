import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import DialogTitle from "@material-ui/core/DialogTitle";
import FormControl from "@material-ui/core/FormControl";
import InputAdornment from "@material-ui/core/InputAdornment";
import InputLabel from "@material-ui/core/InputLabel";
import Select from "@material-ui/core/Select";
import { withStyles } from "@material-ui/core/styles";
import BookmarkBorder from "@material-ui/icons/BookmarkBorder";
// core components
import Button from "conten/ingresoSistema/node_modules/components/CustomButtons/Button.jsx.js";
import PropTypes from "prop-types";
import React from "react";

const styles = {
  root: {
    padding: "2px 4px",
    display: "flex",
    alignItems: "center",
    width: 400
  },
  input: {
    marginLeft: 8,
    flex: 1
  },
  iconButton: {
    padding: 10
  },
  divider: {
    width: 1,
    height: 28,
    margin: 4
  }
};

function SelectFromList(props) {
  const {
    mainTitle,
    classes,
    open,
    onAccept,
    onCancell,
    handleTypeCancellationInput,
    value,
    listTypeCancellation,
    content
  } = props;

  return (
    <Dialog
      open={open}
      //onClose={onAccept}
      aria-labelledby="form-dialog-title"
      fullWidth
      maxWidth="sm"
    >
      <DialogTitle id="form-dialog-title">{mainTitle}</DialogTitle>
      <DialogContent>
        <DialogContentText>
          <FormControl fullWidth className={classes.selectFormControl}>
            <InputLabel htmlFor="simple-select" className={classes.selectLabel}>
              Tipo de cancelación
            </InputLabel>
            <Select
              value={value}
              onChange={handleTypeCancellationInput}
              startAdornment={
                <InputAdornment
                  position="start"
                  className={classes.inputAdornment}
                >
                  <BookmarkBorder className={classes.inputAdornmentIcon} />
                </InputAdornment>
              }
            >
              {content}
            </Select>
          </FormControl>
        </DialogContentText>
      </DialogContent>
      <div align="center">
        <Button style={{ width: 120 }} color="success" onClick={onAccept}>
          Aceptar
        </Button>
        <Button style={{ width: 120 }} color="info" onClick={onCancell}>
          Cancelar
        </Button>
      </div>
    </Dialog>
  );
}

SelectFromList.defaultProps = {
  title: "Seleccione el tipo de cancelación"
};

SelectFromList.propTypes = {
  classes: PropTypes.object.isRequired,
  title: PropTypes.string,
  text: PropTypes.string.isRequired,
  open: PropTypes.bool,
  onDoneClick: PropTypes.func
};

export default withStyles(styles)(SelectFromList);
