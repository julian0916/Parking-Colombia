import {
  API_FINANCIERA,
  HEADERS,
  ID_CUENTA,
} from "config/general/Configuracion";

class ApiClient {
  constructor() {
    this.url = API_FINANCIERA;
  }

  getDatosConsultaListarRecaudoReporte = (datosConsulta) => {
    var query = "fnRecaudo=" + encodeURIComponent(datosConsulta.fnRecaudo);

    let respuesta = {
      method: "get",
      url: `${this.url}/v1/financiera/recaudos_reportes/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}?${query}`,
      headers: HEADERS(),
    };
    return respuesta;
  };

  getDatosGuardarRecaudoReporte = (datosConsulta) => {
    let respuesta = {
      method: "post",
      url: `${this.url}/v1/financiera/recaudar/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}`,
      headers: HEADERS(),
      data: datosConsulta.recaudo,
    };
    return respuesta;
  };

  getDatosConsultaListarPagoMunicipio = (datosConsulta) => {
    var query = "fnPago=" + encodeURIComponent(datosConsulta.fnRecaudo);

    let respuesta = {
      method: "get",
      url: `${this.url}/v1/financiera/get_pago_municipio/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}?${query}`,
      headers: HEADERS(),
    };
    return respuesta;
  };

  getDatosGuardarPagoMunicipio = (pagoMunicipio) => {
    let respuesta = {
      method: "post",
      url: `${this.url}/v1/financiera/pago_municipio/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}`,
      headers: HEADERS(),
      data: pagoMunicipio,
    };
    return respuesta;
  };

  getDatosSolicitarListarFechaPago = (datosConsulta) => {
    var query = "fPago=" + encodeURIComponent(datosConsulta.fnRecaudo);

    let respuesta = {
      method: "get",
      url: `${this.url}/v1/financiera/validar_fecha/${encodeURIComponent(
        sessionStorage[ID_CUENTA]
      )}?${query}`,
      headers: HEADERS(),
    };
    return respuesta;
  };
}
const FinancieraApi = new ApiClient();
export default FinancieraApi;
