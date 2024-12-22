//=============================================
//         OPERACIONES DESDE LOS REPORTES
//=============================================
export const HACER_PAGO_EXTEMPORANEO_WEB = 'HACER_PAGO_EXTEMPORANEO_WEB';
export const HACER_PAGO_EXTEMPORANEO_WEB_SUCCESS =
  'HACER_PAGO_EXTEMPORANEO_WEB_SUCCESS';
export const HACER_PAGO_EXTEMPORANEO_ERROR = 'HACER_PAGO_EXTEMPORANEO_ERROR';

export const hacerPagoExtemporaneoWEB = (contenido, onSuccess) => ({
  type: HACER_PAGO_EXTEMPORANEO_WEB,
  contenido,
  onSuccess,
});

export const hacerPagoExtemporaneoWEBSuccess = (respuestaServidor) => ({
  type: HACER_PAGO_EXTEMPORANEO_WEB_SUCCESS,
  respuestaServidor,
});

export const hacerPagoExtemporaneoWEBError = (errorContenido) => ({
  type: HACER_PAGO_EXTEMPORANEO_ERROR,
  errorContenido,
});

//=============================================
export const INFORME_CARTERA = 'INFORME_CARTERA';
export const INFORME_CARTERA_SUCCESS = 'INFORME_CARTERA_SUCCESS';
export const INFORME_CARTERA_ERROR = 'INFORME_CARTERA_ERROR';

export const informeCartera = (parametros, onSuccess) => ({
  type: INFORME_CARTERA,
  parametros,
  onSuccess,
});

export const informeCarteraSuccess = (respuestaServidor) => ({
  type: INFORME_CARTERA_SUCCESS,
  respuestaServidor,
});

export const informeCarteraError = (errorContenido) => ({
  type: INFORME_CARTERA_ERROR,
  errorContenido,
});
//=============================================
export const INFORME_VEHICULO = 'INFORME_VEHICULO';
export const INFORME_VEHICULO_SUCCESS = 'INFORME_VEHICULO_SUCCESS';
export const INFORME_VEHICULO_ERROR = 'INFORME_VEHICULO_ERROR';

export const informeVehiculo = (parametros, onSuccess) => ({
  type: INFORME_VEHICULO,
  parametros,
  onSuccess,
});

export const informeVehiculoSuccess = (respuestaServidor) => ({
  type: INFORME_VEHICULO_SUCCESS,
  respuestaServidor,
});

export const informeVehiculoError = (errorContenido) => ({
  type: INFORME_VEHICULO_ERROR,
  errorContenido,
});
//=============================================
export const INFORME_MENSUAL = 'INFORME_MENSUAL';
export const INFORME_MENSUAL_SUCCESS = 'INFORME_MENSUAL_SUCCESS';
export const INFORME_MENSUAL_ERROR = 'INFORME_MENSUAL_ERROR';

export const informeMensual = (parametros, onSuccess) => ({
  type: INFORME_MENSUAL,
  parametros,
  onSuccess,
});

export const informeMensualSuccess = (respuestaServidor) => ({
  type: INFORME_MENSUAL_SUCCESS,
  respuestaServidor,
});

export const informeMensualError = (errorContenido) => ({
  type: INFORME_MENSUAL_ERROR,
  errorContenido,
});
//=============================================
export const INFORME_SALDO_PROMOTOR = 'INFORME_SALDO_PROMOTOR';
export const INFORME_SALDO_PROMOTOR_SUCCESS = 'INFORME_SALDO_PROMOTOR_SUCCESS';
export const INFORME_SALDO_PROMOTOR_ERROR = 'INFORME_SALDO_PROMOTOR_ERROR';

export const informeSaldoPromotor = (parametros, onSuccess) => ({
  type: INFORME_SALDO_PROMOTOR,
  parametros,
  onSuccess,
});

export const informeSaldoPromotorSuccess = (respuestaServidor) => ({
  type: INFORME_SALDO_PROMOTOR_SUCCESS,
  respuestaServidor,
});

export const informeSaldoPromotorError = (errorContenido) => ({
  type: INFORME_SALDO_PROMOTOR_ERROR,
  errorContenido,
});
//=============================================
export const INFORME_SUPERVISION = 'INFORME_SUPERVISION';
export const INFORME_SUPERVISION_SUCCESS = 'INFORME_SUPERVISION_SUCCESS';
export const INFORME_SUPERVISION_ERROR = 'INFORME_SUPERVISION_ERROR';

export const informeSupervision = (parametros, onSuccess) => ({
  type: INFORME_SUPERVISION,
  parametros,
  onSuccess,
});

export const informeSupervisionSuccess = (respuestaServidor) => ({
  type: INFORME_SUPERVISION_SUCCESS,
  respuestaServidor,
});

export const informeSupervisionError = (errorContenido) => ({
  type: INFORME_SUPERVISION_ERROR,
  errorContenido,
});
//=============================================
export const INFORME_HISTORICO = 'INFORME_HISTORICO';
export const INFORME_HISTORICO_SUCCESS = 'INFORME_HISTORICO_SUCCESS';
export const INFORME_HISTORICO_ERROR = 'INFORME_HISTORICO_ERROR';

export const informeHistorico = (parametros, tipoConsulta, onSuccess) => ({
  type: INFORME_HISTORICO,
  parametros,
  tipoConsulta,
  onSuccess,
});

export const informeHistoricoSuccess = (respuestaServidor) => ({
  type: INFORME_HISTORICO_SUCCESS,
  respuestaServidor,
});

export const informeHistoricoError = (errorContenido) => ({
  type: INFORME_HISTORICO_ERROR,
  errorContenido,
});
//=============================================
