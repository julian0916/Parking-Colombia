import axios from "axios";
import { addMessage } from "conten/mensajes/MessagesActions";
import {
  GUARDAR_ZONA,
  guardarZonaSuccess,
  guardarZonaError,
  ZONA_LISTAR,
  listarZonaSuccess,
  listarZonaError,
} from "conten/zonas/ZonaActions";
import ZonaApi from "conten/zonas/ZonaApiClient";
import { ofType } from "redux-observable";
import { Observable } from "rxjs";
import { mergeMap } from "rxjs/operators";
import { OK, ERROR, apiTimeout } from "config/general/Configuracion";

export const listarZona = (action$, state$) =>
  action$.pipe(
    ofType(ZONA_LISTAR),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(ZonaApi.listarZona(action.paginacion))
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(listarZonaSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(listarZonaError(error));
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

export const guardarZona = (action$, state$) =>
  action$.pipe(
    ofType(GUARDAR_ZONA),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(ZonaApi.guardarZona(action.zona))
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(guardarZonaSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(guardarZonaError(error));
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