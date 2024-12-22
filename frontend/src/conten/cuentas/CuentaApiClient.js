import {
  API_CUENTA,
  HEADERS,
  CORREO_USUARIO,
} from "config/general/Configuracion";

class ApiClient {
  constructor() {
    this.url = API_CUENTA;
  }

  listarCuentaUsuario = () => {
    let respuesta = {
      method: "get",
      url: `${this.url}/v1/cuenta/listado/${encodeURIComponent(
        sessionStorage[CORREO_USUARIO]
      )}`,
      headers: HEADERS(),
    };
    return respuesta;
  };

  listarCuenta = (paginacion) => {

    var query = "limite=" + encodeURIComponent(paginacion.limite);
    query += "&actual=" + encodeURIComponent(paginacion.actual);
    if (paginacion.filtro) {
      query += "&filtro=" + encodeURIComponent(paginacion.filtro);
    }
    if (paginacion.orden) {
      query += "&orden=" + encodeURIComponent(paginacion.orden);
    }
    if (paginacion.sentido) {
      query += "&sentido=" + encodeURIComponent(paginacion.sentido);
    }
    let respuesta = {
      method: "get",
      url: `${this.url}/v1/cuenta/listado?${query}`,
      headers: HEADERS(),
    };
    return respuesta;
  };

  grabarCuenta = (cuenta) => {

    let respuesta = {
      method: "post",
      url: `${this.url}/v1/cuenta/cuenta`,
      headers: HEADERS(),
      data: cuenta,
    };
    return respuesta;
  };
}
const CuentaApi = new ApiClient();
export default CuentaApi;
