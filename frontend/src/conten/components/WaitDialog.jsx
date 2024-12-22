import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";
import wait from "assets/img/wait_1.gif";
import PropTypes from "prop-types";
import React from "react";

function WaitDialog({ ...props }) {
  const { text, hideDialog } = props;

  return (
    <Dialog
      open={true}
      onClose={hideDialog}
      aria-labelledby="form-dialog-title"
    >
      <DialogContent>
        <div
          style={{
            display: "flex",
            flexDirection: "row",
            alignContent: "center",
            alignItems: "center"
          }}
        >
          <img src={wait} alt="..." width="40" />
          <span
            style={{
              color: "#a7a7a7",
              fontWeight: "bold",
              marginLeft: 6,
              alignContent: "center",
              display: "inline-flex",
              alignItems: "center"
            }}
          >
            {text}
          </span>
        </div>
      </DialogContent>
    </Dialog>
  );
}

WaitDialog.propTypes = {
  text: PropTypes.string.isRequired,
  hideDialog: PropTypes.func
};

export default WaitDialog;
