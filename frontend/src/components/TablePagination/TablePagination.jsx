import MenuItem from "@material-ui/core/MenuItem";
import Select from "@material-ui/core/Select";
// @material-ui/core components
import withStyles from "@material-ui/core/styles/withStyles";
import { LISTADO_PAGINACION } from "config/general/Configuracion";
import PropTypes from "prop-types";
//import PropTypes from 'prop-types';
import React from "react";
import regularFormsStyle from "../../assets/jss/material-dashboard-pro-react/views/regularFormsStyle.jsx";
import Pagination from "../Pagination/Pagination.jsx";
function TablePagination({ ...props }) {
  const {
    color,
    rowsPerPageOptions,
    pages,
    rowsPerPage,
    page,
    onChangePage,
    onChangeRowsPerPage,
  } = props;

  const handleClickPage = (pageEnviada) => {
    if (pageEnviada === "ANT") {
      if (page > 1) {
        onChangePage(page - 1);
      }
      return;
    }
    if (pageEnviada === "SIG") {
      if (page < pages) {
        onChangePage(page + 1);
      }
      return;
    }
    onChangePage(pageEnviada);
  };

  const handleChange = (event) => {
    //let rowsPerPage = event.target.value;
    //setState({ rowsPerPage });
    onChangeRowsPerPage(event.target.value);
  };

  const { classes } = props;
  //const { pages, page, rowsPerPageOptions } = props;
  var pageList = [{ text: "ANT", onClick: () => handleClickPage("ANT") }];

  for (var i = 1; i <= pages; i++) {
    let currentPage = i;
    if (i !== page) {
      pageList.push({
        text: i,
        onClick: () => handleClickPage(currentPage),
      });
    } else {
      pageList.push({
        active: true,
        text: i,
        onClick: () => handleClickPage(currentPage),
      });
    }
  }
  pageList.push({ text: "SIG", onClick: () => handleClickPage("SIG") });

  const rowsPerPageMenuItem = rowsPerPageOptions.map((object) => {
    return (
      <MenuItem
        key={object}
        classes={{
          root: classes.selectMenuItem,
        }}
        value={object}
      >
        {object}
      </MenuItem>
    );
  });

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "row",
        alignContent: "center",
        alignItems: "center",
      }}
    >
      <Select
        value={rowsPerPage}
        onChange={handleChange}
        inputProps={{
          name: "rows",
          id: "rows",
        }}
      >
        {rowsPerPageMenuItem}
      </Select>

      <Pagination pages={pageList} color={color} />
    </div>
  );
}

TablePagination.defaultProps = {
  rowsPerPageOptions: LISTADO_PAGINACION,
  color:"primary",
};

TablePagination.propTypes = {
  rowsPerPageOptions: PropTypes.array,
  pages: PropTypes.number,
  rowsPerPage: PropTypes.number,
  page: PropTypes.number,
  onChangePage: PropTypes.func.isRequired,
  onChangeRowsPerPage: PropTypes.func,
};

export default withStyles(regularFormsStyle)(TablePagination);
