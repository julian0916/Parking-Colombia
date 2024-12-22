export const LISTAR_RECAUDO_REPORTE = "LISTAR_RECAUDO_REPORTE";
export const LISTAR_RECAUDO_REPORTE_SUCCESS = "LISTAR_RECAUDO_REPORTE_SUCCESS";
export const LISTAR_RECAUDO_REPORTE_ERROR = "LISTAR_RECAUDO_REPORTE_ERROR";

export const solicitarListarRecaudoReporte = (datosConsulta, onSuccess) => ({
  type: LISTAR_RECAUDO_REPORTE,
  datosConsulta,
  onSuccess,
});

export const solicitarListarRecaudoReporteSuccess = (respuestaServidor) => ({
  type: LISTAR_RECAUDO_REPORTE_SUCCESS,
  respuestaServidor,
});

export const solicitarListarRecaudoReporteError = (errorContenido) => ({
  type: LISTAR_RECAUDO_REPORTE_ERROR,
  errorContenido,
});

export const GUARDAR_RECAUDO_REPORTE = "GUARDAR_RECAUDO_REPORTE";
export const GUARDAR_RECAUDO_REPORTE_SUCCESS = "GUARDAR_RECAUDO_REPORTE_SUCCESS";
export const GUARDAR_RECAUDO_REPORTE_ERROR = "GUARDAR_RECAUDO_REPORTE_ERROR";

export const solicitarGuardarRecaudoReporte = (datosConsulta, onSuccess) => ({
  type: GUARDAR_RECAUDO_REPORTE,
  datosConsulta,
  onSuccess,
});

export const solicitarGuardarRecaudoReporteSuccess = (respuestaServidor) => ({
  type: GUARDAR_RECAUDO_REPORTE_SUCCESS,
  respuestaServidor,
});

export const solicitarGuardarRecaudoReporteError = (errorContenido) => ({
  type: GUARDAR_RECAUDO_REPORTE_ERROR,
  errorContenido,
});

export const LISTAR_PAGO_MUNICIPIO = "LISTAR_PAGO_MUNICIPIO";
export const LISTAR_PAGO_MUNICIPIO_SUCCESS = "LISTAR_PAGO_MUNICIPIO_SUCCESS";
export const LISTAR_PAGO_MUNICIPIO_ERROR = "LISTAR_PAGO_MUNICIPIO_ERROR";

export const solicitarListarPagoMunicipio = (datosConsulta, onSuccess) => ({
  type: LISTAR_PAGO_MUNICIPIO,
  datosConsulta,
  onSuccess,
});

export const solicitarListarPagoMunicipioSuccess = (respuestaServidor) => ({
  type: LISTAR_PAGO_MUNICIPIO_SUCCESS,
  respuestaServidor,
});

export const solicitarListarPagoMunicipioError = (errorContenido) => ({
  type: LISTAR_PAGO_MUNICIPIO_ERROR,
  errorContenido,
});

export const GUARDAR_PAGO_MUNICIPIO = "GUARDAR_PAGO_MUNICIPIO";
export const GUARDAR_PAGO_MUNICIPIO_SUCCESS = "GUARDAR_PAGO_MUNICIPIO_SUCCESS";
export const GUARDAR_PAGO_MUNICIPIO_ERROR = "GUARDAR_PAGO_MUNICIPIO_ERROR";

export const solicitarGuardarPagoMunicipio = (pagoMunicipio, onSuccess) => ({
  type: GUARDAR_PAGO_MUNICIPIO,
  pagoMunicipio,
  onSuccess,
});

export const solicitarGuardarPagoMunicipioSuccess = (respuestaServidor) => ({
  type: GUARDAR_PAGO_MUNICIPIO_SUCCESS,
  respuestaServidor,
});

export const solicitarGuardarPagoMunicipioError = (errorContenido) => ({
  type: GUARDAR_PAGO_MUNICIPIO_ERROR,
  errorContenido,
});

export const LISTAR_FECHA_PAGOS = "LISTAR_FECHA_PAGOS";
export const LISTAR_FECHA_PAGOS_SUCCESS = "LISTAR_FECHA_PAGOS_SUCCESS";
export const LISTAR_FECHA_PAGOS_ERROR = "LISTAR_FECHA_PAGOS_ERROR";

export const solicitarListarFechaPago = (datosConsulta, onSuccess) => ({
  type: LISTAR_FECHA_PAGOS,
  datosConsulta,
  onSuccess,
});

export const solicitarListarFechaPagoSuccess = (respuestaServidor) => ({
  type: LISTAR_FECHA_PAGOS_SUCCESS,
  respuestaServidor,
});

export const solicitarListarFechaPagoError = (errorContenido) => ({
  type: LISTAR_FECHA_PAGOS_ERROR,
  errorContenido,
});