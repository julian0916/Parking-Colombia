/*!

=========================================================
* Material Dashboard PRO React - v1.7.0
=========================================================

* Product Page: https://www.creative-tim.com/product/material-dashboard-pro-react
* Copyright 2019 Creative Tim (https://www.creative-tim.com)

* Coded by Creative Tim

=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

*/
import React from "react";
// nodejs library to set properties for components
import PropTypes from "prop-types";
// nodejs library that concatenates classes
import classNames from "classnames";
// @material-ui/core components
import withStyles from "@material-ui/core/styles/withStyles";
import FormControl from "@material-ui/core/FormControl";
import InputLabel from "@material-ui/core/InputLabel";
import FormHelperText from "@material-ui/core/FormHelperText";
import TextField from "@material-ui/core/TextField";
import InputAdornment from "@material-ui/core/InputAdornment";

import customInputStyle from "assets/jss/material-dashboard-pro-react/components/customInputStyle.jsx";

function CustomTextaerea({ ...props }) {
  const {
    classes,
    formControlProps,
    labelText,
    id,
    labelProps,
    inputProps,
    error,
    white,
    inputRootCustomClasses,
    success,
    helperText,
    type,
    rows,
    disabled,
  } = props;

  const labelClasses = classNames({
    [" " + classes.labelRootError]: error,
    [" " + classes.labelRootSuccess]: success && !error,
  });
  const underlineClasses = classNames({
    [classes.underlineError]: error,
    [classes.underlineSuccess]: success && !error,
    [classes.underline]: true,
    [classes.whiteUnderline]: white,
  });
  const marginTop = classNames({
    [inputRootCustomClasses]: inputRootCustomClasses !== undefined,
  });
  var inputClasses = classNames({
    [classes.input]: true,
    [classes.whiteInput]: white,
  });

  if (inputClasses === null || inputClasses === undefined) {
    inputClasses = "";
  }

  var formControlClasses;
  if (formControlProps !== undefined) {
    formControlClasses = classNames(
      formControlProps.className,
      classes.formControl
    );
  } else {
    formControlClasses = classes.formControl;
  }
  var helpTextClasses = classNames({
    [classes.labelRootError]: error,
    [classes.labelRootSuccess]: success && !error,
  });
  return (
    <FormControl {...formControlProps} className={formControlClasses}>
      <TextField
        variant="outlined"
        multiline
        label={labelText !== undefined ? labelText : ""}
        rows={rows}
        type={type}
        id={id}
        disabled= {disabled}
        {...inputProps}
      />
      {helperText !== undefined ? (
        <FormHelperText id={id + "-text"} className={helpTextClasses}>
          {helperText}
        </FormHelperText>
      ) : null}
    </FormControl>
  );
}

CustomTextaerea.propTypes = {
  rows: PropTypes.number,
  classes: PropTypes.object.isRequired,
  labelText: PropTypes.node,
  labelProps: PropTypes.object,
  id: PropTypes.string,
  inputProps: PropTypes.object,
  formControlProps: PropTypes.object,
  inputRootCustomClasses: PropTypes.string,
  error: PropTypes.bool,
  success: PropTypes.bool,
  white: PropTypes.bool,
  helperText: PropTypes.node,
};

export default withStyles(customInputStyle)(CustomTextaerea);
