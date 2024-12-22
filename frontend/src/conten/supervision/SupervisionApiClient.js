import {
  API_SUPERVISION,
  HEADERS,
  ID_CUENTA,
} from "config/general/Configuracion";

class ApiClient {
  constructor() {
    this.url = API_SUPERVISION;
  }

  listarSupervisionPregunta = () => {
    let respuesta = {
      method: "get",
      url: `${this.url}/v1/supervision/get_preguntas/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}`,
      headers: HEADERS(),
    };
    return respuesta;
  };

  grabarSupervisionPregunta = (preguntaSupervision) => {
    let respuesta = {
      method: "post",
      url: `${this.url}/v1/supervision/pregunta/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}`,
      headers: HEADERS(),
      data: preguntaSupervision,
    };
    return respuesta;
  };

  borrarSupervisionPregunta = (preguntaSupervision) => {
    let respuesta = {
      method: "delete",
      url: `${this.url}/v1/supervision/pregunta_del/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}/${encodeURIComponent(preguntaSupervision.id)}`,
      headers: HEADERS(),
    };
    return respuesta;
  };
}
const SupervisionPreguntaApi = new ApiClient();
export default SupervisionPreguntaApi;
