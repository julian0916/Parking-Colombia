import {
  API_OPERACION,
  API_REPORTES,
  HEADERS,
  ID_CUENTA,
} from 'config/general/Configuracion';

class ApiClient {
  constructor() {
    this.url = API_REPORTES;
    this.url_operacion = API_OPERACION;
  }

  /**
   * Permite generar la consulta necesaria en el
   * backend para poder cambiar el estado de un
   * registro reportado y colocarlo como pagado
   * de forma extemporáneo.
   * @param {*} contenido
   * @returns
   */
  hacerPagoExtemporaneoWEB = (contenido) => {
    let respuesta = {
      method: 'put',
      url: `${
        this.url_operacion
      }/v1/operacion/extemporaneo_from_web/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}?idPago=${encodeURIComponent(
        contenido.idPago
      )}&placa=${encodeURIComponent(contenido.placa)}`,
      headers: HEADERS(),
      data: contenido,
    };
    return respuesta;
  };

  informeCartera = (parametros) => {
    var paginacion = parametros.paginacion;
    var query = 'limite=' + encodeURIComponent(paginacion.limite);
    query += '&actual=' + encodeURIComponent(paginacion.actual);
    if (paginacion.orden) {
      query += '&orden=' + encodeURIComponent(paginacion.orden);
    }
    if (paginacion.sentido) {
      query += '&sentido=' + encodeURIComponent(paginacion.sentido);
    }
    let respuesta = {
      method: 'get',
      url: `${this.url}/v1/reportes/reporte_cartera/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}/${encodeURIComponent(parametros.nfInicial)}/${encodeURIComponent(
        parametros.nfFinal
      )}?${query}`,
      headers: HEADERS(),
    };
    return respuesta;
  };

  informeHistorico = (parametros, tipoConsulta) => {
    let respuesta;
    if (tipoConsulta === 'consultaFecha') {
      respuesta = {
        method: 'get',
        url: `${this.url}/v1/reportes/reporte_historico/${encodeURIComponent(
          sessionStorage[ID_CUENTA]
        )}/${encodeURIComponent(parametros.fechaConsulta)}`,
        headers: HEADERS(),
      };
    } else if (tipoConsulta === 'consultaMesYear') {
      respuesta = {
        method: 'put',
        url: `${this.url}/v1/reportes/reporte_historico/${encodeURIComponent(
          sessionStorage[ID_CUENTA]
        )}/${encodeURIComponent(parametros.mes)}/${encodeURIComponent(parametros.year)}`,
        headers: HEADERS(),
      };
    }
    return respuesta;
  };

  informeVehiculo = (parametros) => {
    var paginacion = parametros.paginacion;
    var query = 'limite=' + encodeURIComponent(paginacion.limite);
    query += '&actual=' + encodeURIComponent(paginacion.actual);
    if (paginacion.orden) {
      query += '&orden=' + encodeURIComponent(paginacion.orden);
    }
    if (paginacion.sentido) {
      query += '&sentido=' + encodeURIComponent(paginacion.sentido);
    }
    let respuesta = {
      method: 'get',
      url: `${this.url}/v1/reportes/reporte_vehiculo/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}/${encodeURIComponent(parametros.nfInicial)}/${encodeURIComponent(
        parametros.nfFinal
      )}/${encodeURIComponent(parametros.placa)}?${query}`,
      headers: HEADERS(),
    };

    return respuesta;
  };

  informeMensual = (parametros) => {
    var query = 'year=' + encodeURIComponent(parametros.year);
    query += '&enero=' + encodeURIComponent(parametros.enero);
    query += '&febrero=' + encodeURIComponent(parametros.febrero);
    query += '&marzo=' + encodeURIComponent(parametros.marzo);
    query += '&abril=' + encodeURIComponent(parametros.abril);
    query += '&mayo=' + encodeURIComponent(parametros.mayo);
    query += '&junio=' + encodeURIComponent(parametros.junio);
    query += '&julio=' + encodeURIComponent(parametros.julio);
    query += '&agosto=' + encodeURIComponent(parametros.agosto);
    query += '&septiembre=' + encodeURIComponent(parametros.septiembre);
    query += '&octubre=' + encodeURIComponent(parametros.octubre);
    query += '&noviembre=' + encodeURIComponent(parametros.noviembre);
    query += '&diciembre=' + encodeURIComponent(parametros.diciembre);
    let respuesta = {
      method: 'get',
      url: `${this.url}/v1/reportes/reporte_mensual/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}?${query}`,
      headers: HEADERS(),
    };
    return respuesta;
  };

  informeSaldoPromotor = (parametros) => {
    var paginacion = parametros.paginacion;
    var query = 'limite=' + encodeURIComponent(paginacion.limite);
    query += '&actual=' + encodeURIComponent(paginacion.actual);
    query += '&traerTodo=' + encodeURIComponent(paginacion.traerTodo);
    query += '&promotor=' + encodeURIComponent(parametros.promotor);
    if (paginacion.orden) {
      query += '&orden=' + encodeURIComponent(paginacion.orden);
    }
    if (paginacion.sentido) {
      query += '&sentido=' + encodeURIComponent(paginacion.sentido);
    }
    let respuesta = {
      method: 'get',
      url: `${this.url}/v1/reportes/reporte_saldo_promotor/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}/${encodeURIComponent(parametros.activos)}?${query}`,
      headers: HEADERS(),
    };
    return respuesta;
  };

  informeSupervision = (parametros) => {
    var paginacion = parametros.paginacion;
    var query = 'limite=' + encodeURIComponent(paginacion.limite);
    query += '&actual=' + encodeURIComponent(paginacion.actual);
    query += '&traerTodo=' + encodeURIComponent(paginacion.traerTodo);
    query += '&promotor=' + encodeURIComponent(parametros.promotor);
    if (paginacion.orden) {
      query += '&orden=' + encodeURIComponent(paginacion.orden);
    }
    if (paginacion.sentido) {
      query += '&sentido=' + encodeURIComponent(paginacion.sentido);
    }
    if (parametros.fnInicio) {
      query += '&fechaInicio=' + encodeURIComponent(parametros.fnInicio);
    }
    if (parametros.fnFin) {
      query += '&fechaFin=' + encodeURIComponent(parametros.fnFin);
    }
    let respuesta = {
      method: 'get',
      url: `${this.url}/v1/reportes/reporte_supervision/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}?${query}`,
      headers: HEADERS(),
    };
    return respuesta;
  };
}
const InformesApiClient = new ApiClient();
export default InformesApiClient;
