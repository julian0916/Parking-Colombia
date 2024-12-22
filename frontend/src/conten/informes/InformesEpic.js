import axios from 'axios';
import { addMessage } from 'conten/mensajes/MessagesActions';
import {
  INFORME_SUPERVISION,
  informeSupervisionSuccess,
  informeSupervisionError,
  INFORME_SALDO_PROMOTOR,
  informeSaldoPromotorSuccess,
  informeSaldoPromotorError,
  INFORME_MENSUAL,
  informeMensualSuccess,
  informeMensualError,
  INFORME_VEHICULO,
  informeVehiculoSuccess,
  informeVehiculoError,
  INFORME_CARTERA,
  informeCarteraSuccess,
  informeCarteraError,
  HACER_PAGO_EXTEMPORANEO_WEB,
  hacerPagoExtemporaneoWEBSuccess,
  hacerPagoExtemporaneoWEBError,
  INFORME_HISTORICO,
  informeHistoricoSuccess,
  informeHistoricoError,
} from 'conten/informes/InformesActions';
import InformesApiClient from 'conten/informes/InformesApiClient';
import { ofType } from 'redux-observable';
import { Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { OK, ERROR, apiTimeout } from 'config/general/Configuracion';

export const hacerPagoExtemporaneoWEB = (action$, state$) =>
  action$.pipe(
    ofType(HACER_PAGO_EXTEMPORANEO_WEB),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(InformesApiClient.hacerPagoExtemporaneoWEB(action.contenido))
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(hacerPagoExtemporaneoWEBSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(hacerPagoExtemporaneoWEBError(error));
              var mensajeError = 'Hubo un problema: ' + error.message;
              if (error && error.response && error.response.data) {
                mensajeError = error.response.data;
              }
              action.onSuccess(ERROR, error);
              obs.next(
                addMessage({ variant: 'error', message: mensajeError + '' })
              );
              obs.complete();
            });
        })
    )
  );

export const informeCartera = (action$, state$) =>
  action$.pipe(
    ofType(INFORME_CARTERA),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(InformesApiClient.informeCartera(action.parametros))
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(informeCarteraSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(informeCarteraError(error));
              var mensajeError = 'Hubo un problema: ' + error.message;
              if (error && error.response && error.response.data) {
                mensajeError = error.response.data;
              }
              action.onSuccess(ERROR, error);
              obs.next(
                addMessage({ variant: 'error', message: mensajeError + '' })
              );
              obs.complete();
            });
        })
    )
  );

  export const informeHistorico = (action$, state$) =>
    action$.pipe(
      ofType(INFORME_HISTORICO),
      mergeMap((action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(InformesApiClient.informeHistorico(action.parametros, action.tipoConsulta))
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(informeHistoricoSuccess(response.data));
                obs.complete();
                if (typeof action.onSuccess === 'function') {
                  action.onSuccess(OK, response.data);
                }
              }
            })
            .catch((error) => {
              obs.next(informeHistoricoError(error));
              var mensajeError = 'Hubo un problema: ' + error.message;
              if (error && error.response && error.response.data) {
                mensajeError = error.response.data;
              }
              if (typeof action.onSuccess === 'function') {
                action.onSuccess(ERROR, error);
              }
              obs.next(
                addMessage({ variant: 'error', message: mensajeError + '' })
              );
              obs.complete();
            });
        })
      )
    );

export const informeVehiculo = (action$, state$) =>
  action$.pipe(
    ofType(INFORME_VEHICULO),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(InformesApiClient.informeVehiculo(action.parametros))
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(informeVehiculoSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(informeVehiculoError(error));
              var mensajeError = 'Hubo un problema: ' + error.message;
              if (error && error.response && error.response.data) {
                mensajeError = error.response.data;
              }
              action.onSuccess(ERROR, error);
              obs.next(
                addMessage({ variant: 'error', message: mensajeError + '' })
              );
              obs.complete();
            });
        })
    )
  );

export const informeMensual = (action$, state$) =>
  action$.pipe(
    ofType(INFORME_MENSUAL),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(InformesApiClient.informeMensual(action.parametros))
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(informeMensualSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(informeMensualError(error));
              var mensajeError = 'Hubo un problema: ' + error.message;
              if (error && error.response && error.response.data) {
                mensajeError = error.response.data;
              }
              action.onSuccess(ERROR, error);
              obs.next(
                addMessage({ variant: 'error', message: mensajeError + '' })
              );
              obs.complete();
            });
        })
    )
  );

export const informeSaldoPromotor = (action$, state$) =>
  action$.pipe(
    ofType(INFORME_SALDO_PROMOTOR),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(InformesApiClient.informeSaldoPromotor(action.parametros))
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(informeSaldoPromotorSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(informeSaldoPromotorError(error));
              var mensajeError = 'Hubo un problema: ' + error.message;
              if (error && error.response && error.response.data) {
                mensajeError = error.response.data;
              }
              action.onSuccess(ERROR, error);
              obs.next(
                addMessage({ variant: 'error', message: mensajeError + '' })
              );
              obs.complete();
            });
        })
    )
  );

export const informeSupervision = (action$, state$) =>
  action$.pipe(
    ofType(INFORME_SUPERVISION),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(InformesApiClient.informeSupervision(action.parametros))
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(informeSupervisionSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(informeSupervisionError(error));
              var mensajeError = 'Hubo un problema: ' + error.message;
              if (error && error.response && error.response.data) {
                mensajeError = error.response.data;
              }
              action.onSuccess(ERROR, error);

              obs.next(
                addMessage({ variant: 'error', message: mensajeError + '' })
              );
              obs.complete();
            });
        })
    )
  );
