import axios from "axios";
import { addMessage } from "conten/mensajes/MessagesActions";
import {
  GUARDAR_PROMOTOR_ZONA,
  guardarPromotorZonaSuccess,
  guardarPromotorZonaError,
  PROMOTOR_ZONA_LISTAR,
  listarPromotorZonaSuccess,
  listarPromotorZonaError,
} from "conten/promotorZonas/PromotorZonaActions";
import PromotorZonaApi from "conten/promotorZonas/PromotorZonaApiClient";
import { ofType } from "redux-observable";
import { Observable } from "rxjs";
import { mergeMap } from "rxjs/operators";
import { OK, ERROR, apiTimeout } from "config/general/Configuracion";

export const listarPromotorZona = (action$, state$) =>
  action$.pipe(
    ofType(PROMOTOR_ZONA_LISTAR),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(PromotorZonaApi.listarPromotorZona(action.paginacion))
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(listarPromotorZonaSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(listarPromotorZonaError(error));
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

export const guardarPromotorZona = (action$, state$) =>
  action$.pipe(
    ofType(GUARDAR_PROMOTOR_ZONA),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(PromotorZonaApi.guardarPromotorZona(action.promotorZona))
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(guardarPromotorZonaSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(guardarPromotorZonaError(error));
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