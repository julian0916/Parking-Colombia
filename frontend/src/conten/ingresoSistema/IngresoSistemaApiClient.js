import { API_GATEWAY_URL, HEADERS, SESION } from "config/general/Configuracion";

class ApiClient {
  constructor() {
    this.url = API_GATEWAY_URL;
  }

  ingresoInicioSolicitud = () => {
    return {
      method: "get",
      url: `${this.url}/v1/acceso/authDiff`,
      headers: HEADERS(),
    };
  };

  ingresoCompletarSolicitud = (idSolicitud, A, tokenAuth) => {
    //auth?idSolicitud=w&A=w&contentAuth=w
    var para1 = "idSolicitud=" + encodeURIComponent(idSolicitud);
    var para2 = "A=" + encodeURIComponent(A);
    var para3 = "contentAuth=" + encodeURIComponent(tokenAuth);
    let respuesta = {
      method: "get",
      url: `${this.url}/v1/acceso/auth?${para1}&${para2}&${para3}`,
      headers: HEADERS(),
    };
    return respuesta;
  };

  salirSesion = () => {
    let respuesta = {
      method: "post",
      url: `${this.url}/v1/acceso/salir/${sessionStorage[SESION]}`,
      headers: HEADERS(),
    };
    return respuesta;
  };
}
const IngresoSistemaApi = new ApiClient();
export default IngresoSistemaApi;
