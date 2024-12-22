import { API_USUARIO, HEADERS, ID_CUENTA } from "config/general/Configuracion";

class ApiClient {
  constructor() {
    this.url = API_USUARIO;
  }

  listarUsuario = (paginacion) => {
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
      url: `${this.url}/v1/usuario/listado/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}?${query}`,
      headers: HEADERS(),
    };
    return respuesta;
  };

  guardarUsuario = (usuario) => {
    let respuesta = {
      method: "post",
      url: `${this.url}/v1/usuario/usuario/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}`,
      headers: HEADERS(),
      data: usuario,
    };
    return respuesta;
  };

  listarPerfiles = () => {
    let respuesta = {
      method: "get",
      url: `${this.url}/v1/usuario/perfil/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}`,
      headers: HEADERS(),
    };
    return respuesta;
  };

  listarDocumentos = () => {
    let respuesta = {
      method: "get",
      url: `${this.url}/v1/usuario/tipo-documento/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}`,
      headers: HEADERS(),
    };
    return respuesta;
  };
}
const UsuarioApi = new ApiClient();
export default UsuarioApi;
