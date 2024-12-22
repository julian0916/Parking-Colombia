import IconButton from "@material-ui/core/IconButton";
import DeleteIcon from "@material-ui/icons/Delete";
import AplicarSeguridad from "conten/components/AplicarSeguridad";
import { removerMessage } from "conten/mensajes/MessagesActions";
import { SnackbarProvider, withSnackbar } from "notistack";
import PropTypes from "prop-types";
import React from "react";
import { connect } from "react-redux";

class Message extends React.Component {
  borrarMensajes = (mensaje, padre) => {
    this.props.removerMessage(mensaje);
    this.props.closeSnackbar(padre);
  };

  render() {
    const { messages } = this.props.messagesState;
    messages.forEach((message) => {
      var keySnackbar = this.props.enqueueSnackbar(message.message, {
        variant: message.variant,
        preventDuplicate: true,
        action: (
          <IconButton
            onClick={(even) => {
              even.preventDefault();
              this.borrarMensajes(message, keySnackbar);
            }}
            aria-label="Delete"
          >
            <DeleteIcon fontSize="small" style={{ color: "white" }} />
          </IconButton>
        ),
        //onClickAction: this.borrarMensajes(message, keySnackbar),
        onClose: this.borrarMensajes(message, keySnackbar),
      });
    });

    return (
      <>
        <AplicarSeguridad />
        <React.Fragment></React.Fragment>
      </>
    );
  }
}

Message.propTypes = {
  enqueueSnackbar: PropTypes.func.isRequired,
  deleteMessage: PropTypes.func,
};

const mapStateToProps = (state) => {
  return {
    messagesState: state.messagesState,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    removerMessage: (message) => dispatch(removerMessage(message)),
  };
};

//Conectar con el estado de los mensajes y habilitar para manejar Snackbar
const MessageApp = connect(
  mapStateToProps,
  mapDispatchToProps
)(withSnackbar(Message));

class IntegrationNotistack extends React.Component {
  render() {
    return (
      <SnackbarProvider
        maxSnack={3}
        anchorOrigin={{
          vertical: "top",
          horizontal: "right",
        }}
      >
        <MessageApp />
      </SnackbarProvider>
    );
  }
}

export default IntegrationNotistack;
