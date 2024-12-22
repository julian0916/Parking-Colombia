import {
  API_DASHBOARD,
  HEADERS,
  ID_CUENTA,
} from "config/general/Configuracion";

class ApiClient {
  constructor() {
    this.url = API_DASHBOARD;
  }

  getDatosConsulta = (datosConsulta) => {
    var query = "fnInicio=" + encodeURIComponent(datosConsulta.fnInicio);
    query += "&fnFin=" + encodeURIComponent(datosConsulta.fnFin);

    let respuesta = {
      method: "get",
      url: `${this.url}/v1/dashboard/datos/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}?${query}`,
      headers: HEADERS(),
    };
    return respuesta;
  };

  getDatosSolicitarListarOcupacionZona = (datosConsulta) => {
    var query = "fhConsulta=" + encodeURIComponent(datosConsulta.fhConsulta);

    let respuesta = {
      method: "get",
      url: `${this.url}/v1/dashboard/ocupacion_zona/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}?${query}`,
      headers: HEADERS(),
    };
    return respuesta;
  };
}
const PrincipalApi = new ApiClient();
export default PrincipalApi;
