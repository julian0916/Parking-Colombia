import FormControl from "@material-ui/core/FormControl";
import Input from "@material-ui/core/Input";
import InputAdornment from "@material-ui/core/InputAdornment";
import InputLabel from "@material-ui/core/InputLabel";
import { ThemeProvider } from "@material-ui/core/styles";
import ClearIcon from "@material-ui/icons/Clear";
import SearchIcon from "@material-ui/icons/Search";
import GridContainer from "components/Grid/GridContainer.jsx";
import GridItem from "components/Grid/GridItem.jsx";
import { CAMPO_CALENDARIO_TEXTO_BLANCO } from "config/general/Temas";
import PropTypes from "prop-types";
import React from "react";

function cambioFiltroLocal(even, state) {
  state.setState({ filtroLocal: even.target.value });
}

function borrarFiltroLocal(state) {
  state.setState({ filtroLocal: "" });
}

function getTipoIcono(state, buscarContendio) {
  var resultado = (
    <InputAdornment
      position="start"
      style={{
        cursor: "pointer",
      }}
    >
      <ClearIcon
        onClick={() => {
          borrarFiltroLocal(state), buscarContendio("");
        }}
      />
      <SearchIcon
        onClick={() => {
          buscarContendio(state.state.filtroLocal);
        }}
      />
    </InputAdornment>
  );
  return resultado;
}

function FiltroGeneral({ ...props }) {
  const {
    keyFiltro,
    colorTexto,
    colorFondo,
    eventoFiltrar,
    tamaLetra,
    texto,
    state,
  } = props;

  return (
    <GridContainer
      key={keyFiltro + "-GridContainer"}
      container
      alignItems="center"
      style={{ marginBottom: "5px", borderRadius: 3 }}
    >
      <GridItem
        xs={12}
        sm={12}
        md={12}
        lg={12}
        style={{ backgroundColor: colorFondo, borderRadius: 3 }}
      >
        <div style={{ marginBottom: "5px" }}>
          <ThemeProvider theme={CAMPO_CALENDARIO_TEXTO_BLANCO}>
            <FormControl fullWidth>
              <InputLabel style={{ fontSize: tamaLetra, color: colorTexto }}>
                {texto}
              </InputLabel>
              <Input
                style={{ fontSize: tamaLetra, color: colorTexto }}
                key={keyFiltro + "Filtrar"}
                value={
                  state.state.filtroLocal === undefined
                    ? ""
                    : state.state.filtroLocal
                }
                onChange={(event) => {
                  cambioFiltroLocal(event, state);
                }}
                endAdornment={getTipoIcono(state, eventoFiltrar)}
              />
            </FormControl>
          </ThemeProvider>
        </div>
      </GridItem>
    </GridContainer>
  );
}

FiltroGeneral.propTypes = {
  keyFiltro: PropTypes.string.isRequired,
  colorTexto: PropTypes.string.isRequired,
  colorFondo: PropTypes.string.isRequired,
  eventoFiltrar: PropTypes.func.isRequired,
  tamaLetra: PropTypes.string.isRequired,
  texto: PropTypes.string.isRequired,
  state: PropTypes.object.isRequired,
};

export default FiltroGeneral;
