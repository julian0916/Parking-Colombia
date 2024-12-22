import FormControl from "@material-ui/core/FormControl";
// @material-ui/core components
import TextField from "@material-ui/core/TextField";
import Search from "@material-ui/icons/Search";
import regularFormsStyle from "assets/jss/material-dashboard-pro-react/views/regularFormsStyle.jsx";
import Button from "components/CustomButtons/Button.jsx";
// core components
import GridContainer from "components/Grid/GridContainer.jsx";
import GridItem from "components/Grid/GridItem.jsx";
import PropTypes from "prop-types";
import React from "react";
import Datetime from "react-datetime";
import { connect } from "react-redux";
import InputAdornment from "@material-ui/core/InputAdornment";
import DeleteForeverIcon from "@material-ui/icons/DeleteForever";

import { withStyles } from "@material-ui/core/styles";

import IconButton from "@material-ui/core/IconButton";

const style = {
  gridContainer: {
    padding: "0px 30px 30px 40px",
  },

  gridItem: {
    margin: "20px 0px 10px 0px",
  },

  headerGridItem: {
    margin: "30px 0px 0px 0px",
  },
  ...regularFormsStyle,
};

class Filter extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      filter: {
        startDate: 0,
        endDate: 0,
        name: "",
        initialDateValue: 0,
        endDateValue: 0,
      },
    };
  }

  componentDidMount = () => {};

  handleStartDate = (time) => {
    var x = this.state.filter;
    x[["startDate"]] = time;
    this.setState({
      filter: x,
    });
  };

  handleEndDate = (time) => {
    var x = this.state.filter;
    x[["endDate"]] = time;
    this.setState({
      filter: x,
    });
  };

  handleInput = (event) => {
    var filter = this.state.filter;
    filter[[event.target.name]] = event.target.value;
    this.setState({
      filter: filter,
    });
  };

  componentWillReceiveProps = (nextProps) => {
    var filter = null;
    if (nextProps.initialDateValue !== this.state.initialDateValue) {
      this.setState({ initialDateValue: nextProps.initialDateValue });
      filter = this.state.filter;
      filter.startDate = nextProps.initialDateValue;
      this.setState({ filter });
    }

    if (nextProps.endDateValue !== this.state.endDateValue) {
      this.setState({ endDateValue: nextProps.endDateValue });
      filter = this.state.filter;
      filter.endDate = nextProps.endDateValue;
      this.setState({ filter });
    }
  };

  handleGroupByInput = (event) => {
    var groupBy = event.target.value;
    var local = this.state.filter;
    local["groupBy"] = groupBy;
    this.setState({ filter: local });
  };

  borrarFiltro = () => {
    var borrarFiltro = this.state.filter;
    borrarFiltro.name = ""
    this.setState({ filter: borrarFiltro }, () => {
      this.props.onFilter(this.state.filter);
    });
  };

  render() {
    const { classes } = this.props;
    const {
      initialDate,
      endDate,
      name,
      borderBottom,
      mostrarEtiqueta,
      colorBuscar,
    } = this.props;

    return (
      <GridContainer>
        {initialDate ? (
          <GridItem xs={6} sm={6} md={6}>
            <legend
              style={{ fontSize: 14, fontWeight: "bold", color: "#a9a9a9" }}
            >
              Fecha inicio
            </legend>
            <FormControl fullWidth>
              <Datetime
                value={this.state.filter.startDate}
                onChange={this.handleStartDate}
                inputProps={{
                  placeholder: "Ingrese la fecha inicial...",
                  name: "startDate",
                  id: "filter-startDate",
                }}
              />
            </FormControl>
          </GridItem>
        ) : null}

        {endDate ? (
          <GridItem xs={6} sm={6} md={6}>
            <legend
              style={{ fontSize: 14, fontWeight: "bold", color: "#a9a9a9" }}
            >
              Fecha fin
            </legend>
            <FormControl fullWidth>
              <Datetime
                value={this.state.filter.endDate}
                onChange={this.handleEndDate}
                inputProps={{
                  placeholder: "Ingrese la fecha final...",
                  name: "endDate",
                  id: "filter-endDate",
                }}
              />
            </FormControl>
          </GridItem>
        ) : null}

        {name ? (
          <GridItem xs={6} sm={6} md={6} className={classes.gridItem}>
            {mostrarEtiqueta && (
              <legend
                style={{ fontSize: 14, fontWeight: "bold", color: "#a9a9a9" }}
              >
                {this.props.nameText}
              </legend>
            )}
            <FormControl fullWidth style={{ marginTop: 0 }}>
              <TextField
                id="standard-search"
                //type="search"
                placeholder={this.props.placeholder}
                className={classes}
                name="name"
                value = {this.state.filter.name}
                InputProps={{
                  endAdornment: (
                    <InputAdornment
                      position="end"
                      style={{
                        cursor: "pointer",
                      }}
                    >
                      <DeleteForeverIcon onClick={this.borrarFiltro} />
                      <Search
                        onClick={(even) => {
                          this.props.onFilter(this.state.filter);
                        }}
                      />
                    </InputAdornment>
                  ),
                }}
                onChange={this.handleInput}
                onKeyPress={(ev) => {
                  if (ev.key === "Enter") {
                    this.props.onFilter(this.state.filter);
                    ev.preventDefault();
                  }
                }}
              />
            </FormControl>
          </GridItem>
        ) : null}

        {borderBottom ? (
          <GridItem xs={12} sm={12} md={12}>
            <div style={{ borderBottom: "1px solid#eee", marginTop: 26 }} />
          </GridItem>
        ) : null}
      </GridContainer>
    );
  }
}

Filter.defaultProps = {
  name: false,
  initialDate: false,
  endDate: false,
  initialDateValue: 0,
  endDateValue: 0,
  mostrarEtiqueta: false,
  colorBuscar: "warning",
};

Filter.propTypes = {
  name: PropTypes.bool,
  initialDate: PropTypes.bool,
  endDate: PropTypes.bool,
  onFilter: PropTypes.func.isRequired,
  borderBottom: PropTypes.bool,
  initialDateValue: PropTypes.number,
  endDateValue: PropTypes.number,
  mostrarEtiqueta: PropTypes.bool,
  colorBuscar: PropTypes.string,
};

const mapStateToProps = (state) => {
  return {};
};

const mapDispatchToProps = (dispatch) => {
  return {};
};

export default withStyles(style)(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(Filter)
);
