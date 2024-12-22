import { API_PROMOTOR_ZONA, HEADERS, ID_CUENTA } from "config/general/Configuracion";

class ApiClient {
  constructor() {
    this.url = API_PROMOTOR_ZONA;
  }

  listarPromotorZona = (paginacion) => {
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
      url: `${this.url}/v1/promotorZona/listado/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}?${query}`,
      headers: HEADERS(),
    };
    return respuesta;
  };

  guardarPromotorZona = (promotorZona) => {
    let respuesta = {
      method: "post",
      url: `${this.url}/v1/promotorZona/promotorZona/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}`,
      headers: HEADERS(),
      data: promotorZona,
    };
    return respuesta;
  };
 
}
const PromotorZonaApi = new ApiClient();
export default PromotorZonaApi;
