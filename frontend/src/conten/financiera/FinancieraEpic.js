import axios from "axios";
import { addMessage } from "conten/mensajes/MessagesActions";
import {
  LISTAR_RECAUDO_REPORTE,
  solicitarListarRecaudoReporteSuccess,
  solicitarListarRecaudoReporteError,
  GUARDAR_RECAUDO_REPORTE,
  solicitarGuardarRecaudoReporteSuccess,
  solicitarGuardarRecaudoReporteError,
  GUARDAR_PAGO_MUNICIPIO,
  solicitarGuardarPagoMunicipioSuccess,
  solicitarGuardarPagoMunicipioError,
  LISTAR_PAGO_MUNICIPIO,
  solicitarListarPagoMunicipioSuccess,
  solicitarListarPagoMunicipioError,
  LISTAR_FECHA_PAGOS,
  solicitarListarFechaPagoSuccess,
  solicitarListarFechaPagoError,
} from "conten/financiera/FinancieraActions";

import FinancieraApi from "conten/financiera/FinancieraApiClient";
import { ofType } from "redux-observable";
import { Observable } from "rxjs";
import { mergeMap } from "rxjs/operators";
import { OK, ERROR, apiTimeout } from "config/general/Configuracion";

export const solicitarListarFechaPago = (action$, state$) =>
  action$.pipe(
    ofType(LISTAR_FECHA_PAGOS),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(
            FinancieraApi.getDatosSolicitarListarFechaPago(
              action.datosConsulta
            )
          )
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(solicitarListarFechaPagoSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(solicitarListarFechaPagoError(error));
              var mensajeError = "Hubo un problema: " + error.message;
              if (error && error.response && error.response.data) {
                mensajeError = error.response.data;
              }
              action.onSuccess(ERROR, mensajeError + "");
              obs.next(
                addMessage({ variant: "error", message: mensajeError + "" })
              );
              obs.complete();
            });
        })
    )
  );


export const solicitarListarPagoMunicipio = (action$, state$) =>
  action$.pipe(
    ofType(LISTAR_PAGO_MUNICIPIO),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(
            FinancieraApi.getDatosConsultaListarPagoMunicipio(
              action.datosConsulta
            )
          )
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(solicitarListarPagoMunicipioSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(solicitarListarPagoMunicipioError(error));
              var mensajeError = "Hubo un problema: " + error.message;
              if (error && error.response && error.response.data) {
                mensajeError = error.response.data;
              }
              action.onSuccess(ERROR, mensajeError + "");
              obs.next(
                addMessage({ variant: "error", message: mensajeError + "" })
              );
              obs.complete();
            });
        })
    )
  );

export const solicitarGuardarPagoMunicipio = (action$, state$) =>
  action$.pipe(
    ofType(GUARDAR_PAGO_MUNICIPIO),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(
            FinancieraApi.getDatosGuardarPagoMunicipio(action.pagoMunicipio)
          )
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(solicitarGuardarPagoMunicipioSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(solicitarGuardarPagoMunicipioError(error));
              var mensajeError = "Hubo un problema: " + error.message;
              if (error && error.response && error.response.data) {
                mensajeError = error.response.data;
              }
              action.onSuccess(ERROR, mensajeError + "");
              obs.next(
                addMessage({ variant: "error", message: mensajeError + "" })
              );
              obs.complete();
            });
        })
    )
  );

export const solicitarGuardarRecaudoReporte = (action$, state$) =>
  action$.pipe(
    ofType(GUARDAR_RECAUDO_REPORTE),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(
            FinancieraApi.getDatosGuardarRecaudoReporte(action.datosConsulta)
          )
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(solicitarGuardarRecaudoReporteSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(solicitarGuardarRecaudoReporteError(error));
              var mensajeError = "Hubo un problema: " + error.message;
              if (error && error.response && error.response.data) {
                mensajeError = error.response.data;
              }
              action.onSuccess(ERROR, mensajeError + "");
              obs.next(
                addMessage({ variant: "error", message: mensajeError + "" })
              );
              obs.complete();
            });
        })
    )
  );

export const solicitarListarRecaudoReporte = (action$, state$) =>
  action$.pipe(
    ofType(LISTAR_RECAUDO_REPORTE),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(
            FinancieraApi.getDatosConsultaListarRecaudoReporte(
              action.datosConsulta
            )
          )
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(solicitarListarRecaudoReporteSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(solicitarListarRecaudoReporteError(error));
              var mensajeError = "Hubo un problema: " + error.message;
              if (error && error.response && error.response.data) {
                mensajeError = error.response.data;
              }
              action.onSuccess(ERROR, mensajeError + "");
              obs.next(
                addMessage({ variant: "error", message: mensajeError + "" })
              );
              obs.complete();
            });
        })
    )
  );
