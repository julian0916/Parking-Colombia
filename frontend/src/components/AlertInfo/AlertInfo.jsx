import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';

import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';

// core components
import Button from "../CustomButtons/Button.jsx";

const styles = {
  root: {
    padding: '2px 4px',
    display: 'flex',
    alignItems: 'center',
    width: 400,
  },
  input: {
    marginLeft: 8,
    flex: 1,
  },
  iconButton: {
    padding: 10,
  },
  divider: {
    width: 1,
    height: 28,
    margin: 4,
  },
};

function AlertInfo(props) {
  const { text, title, open, onDoneClick} = props;

  return (
    <Dialog
        open={open}
        onClose={onDoneClick}
        aria-labelledby="form-dialog-title"
        fullWidth
        maxWidth="sm">
        <DialogTitle id="form-dialog-title">{title}</DialogTitle>
            <DialogContent>
                <DialogContentText>
                    {text}
                </DialogContentText>

            </DialogContent>
            <DialogActions>
            <Button  color="info" onClick={onDoneClick}>
                Aceptar
            </Button>
        </DialogActions>
    </Dialog>
  );
}

AlertInfo.defaultProps = {
    title: "Información",
};

AlertInfo.propTypes = {
  classes: PropTypes.object.isRequired,
  title: PropTypes.string,
  text: PropTypes.string.isRequired,
  open: PropTypes.bool,
  onDoneClick: PropTypes.func,
};

export default withStyles(styles)(AlertInfo);