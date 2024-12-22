import axios from "axios";
import { addMessage } from "conten/mensajes/MessagesActions";
import {
  TIQUETE_GRABAR,
  grabarTiqueteSuccess,
  grabarTiqueteError,
  TIQUETE_LISTAR,
  listarTiqueteSuccess,
  listarTiqueteError,
  LIMITE_ENDEUDAMIENTO_PROMOTOR,
  getLimiteEndeudamientoPromotorSuccess,
  getLimiteEndeudamientoPromotorError,
  GRABAR_LIMITE_ENDEUDAMIENTO_PROMOTOR,
  grabarLimiteEndeudamientoPromotorSuccess,
  grabarLimiteEndeudamientoPromotorError,
} from "conten/configuracion/ConfiguracionActions";
import ConfiguracionApi from "conten/configuracion/ConfiguracionApiClient";
import { ofType } from "redux-observable";
import { Observable } from "rxjs";
import { mergeMap } from "rxjs/operators";
import { OK, ERROR, apiTimeout } from "config/general/Configuracion";


export const grabarLimiteEndeudamientoPromotor = (action$, state$) =>
  action$.pipe(
    ofType(GRABAR_LIMITE_ENDEUDAMIENTO_PROMOTOR),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(ConfiguracionApi.grabarLimiteEndeudamientoPromotor(action.limite))
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(grabarLimiteEndeudamientoPromotorSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(grabarLimiteEndeudamientoPromotorError(error));
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


  export const getLimiteEndeudamientoPromotor = (action$, state$) =>
  action$.pipe(
    ofType(LIMITE_ENDEUDAMIENTO_PROMOTOR),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(ConfiguracionApi.getLimiteEndeudamientoPromotor())
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(getLimiteEndeudamientoPromotorSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(getLimiteEndeudamientoPromotorError(error));
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


export const listarTiquete = (action$, state$) =>
  action$.pipe(
    ofType(TIQUETE_LISTAR),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(ConfiguracionApi.listarTiquete(action.paginacion))
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(listarTiqueteSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(listarTiqueteError(error));
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

export const grabarTiquete = (action$, state$) =>
  action$.pipe(
    ofType(TIQUETE_GRABAR),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(ConfiguracionApi.grabarTiquete(action.tiquete))
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(grabarTiqueteSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(grabarTiqueteError(error));
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