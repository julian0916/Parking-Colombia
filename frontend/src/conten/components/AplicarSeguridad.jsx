import { controlarAccceso } from "config/funciones/Comunes";
import React from "react";
import { Redirect } from "react-router-dom";
//--------------------------------------------
//           ESTO ES MUY IMPORTANTE
//--------------------------------------------
/**
 * Este componente se base en:
 * El Reduce de IngresoSistemaReducer con la función validarSesion(action)
 * y el uso de este componente en Message.jsx y Admin.jsx
 * @param {} param0 
 */
function AplicarSeguridad({ ...props }) {
  const {} = props;
  if (controlarAccceso() === true) {
    return (
      <Redirect
        push
        to={{
          pathname: "/auth/ingreso-sistema",
          search: "",
          state: {},
        }}
      />
    );
  }
  return <></>;
}

AplicarSeguridad.propTypes = {};

export default AplicarSeguridad;
