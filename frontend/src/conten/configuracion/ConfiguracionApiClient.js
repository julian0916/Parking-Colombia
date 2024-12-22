import { API_CONFIGURACION, HEADERS, ID_CUENTA } from "config/general/Configuracion";

class ApiClient {
  constructor() {
    this.url = API_CONFIGURACION;
  }

  grabarLimiteEndeudamientoPromotor = (limite) => {
    let respuesta = {
      method: "put",
      url: `${this.url}/v1/configuracion/limite_endeudamiento_promotor/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}/${encodeURIComponent(limite)}`,
      headers: HEADERS(),
    };
    return respuesta;
  };

  getLimiteEndeudamientoPromotor = () => {
    let respuesta = {
      method: "get",
      url: `${this.url}/v1/configuracion/get_limite_endeudamiento_promotor/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}`,
      headers: HEADERS(),
    };
    return respuesta;
  };

  listarTiquete = () => {
    let respuesta = {
      method: "get",
      url: `${this.url}/v1/configuracion/get_tiquete/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}`,
      headers: HEADERS(),
    };
    return respuesta;
  };

  grabarTiquete = (tiquete) => {
    let respuesta = {
      method: "post",
      url: `${this.url}/v1/configuracion/tiquete/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}`,
      headers: HEADERS(),
      data: tiquete,
    };
    return respuesta;
  };
}
const ConfiguracionApi = new ApiClient();
export default ConfiguracionApi;
