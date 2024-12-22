import { AddAlarmSharp } from "@material-ui/icons";
import { API_ZONA, HEADERS, ID_CUENTA } from "config/general/Configuracion";

class ApiClient {
  constructor() {
    this.url = API_ZONA;
  }

  listarZona = (paginacion) => {
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
      url: `${this.url}/v1/zona/listado/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}?${query}`,
      headers: HEADERS(),
    };
    return respuesta;
  };

  guardarZona = (zona) => {
    let respuesta = {
      method: "post",
      url: `${this.url}/v1/zona/zona/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}`,
      headers: HEADERS(),
      data: zona,
    };
    return respuesta;
  };
 
}
const ZonaApi = new ApiClient();
export default ZonaApi;
