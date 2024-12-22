import axios from "axios";
import { addMessage } from "conten/mensajes/MessagesActions";
import {
  SUPERVISION_PREGUNTA_GRABAR,
  grabarSupervisionPreguntaSuccess,
  grabarSupervisionPreguntaError,
  SUPERVISION_PREGUNTA_LISTAR,
  listarSupervisionPreguntaSuccess,
  listarSupervisionPreguntaError,
  SUPERVISION_PREGUNTA_BORRAR,
  borrarSupervisionPreguntaSuccess,
  borrarSupervisionPreguntaError,
} from "conten/supervision/SupervisionActions";
import SupervisionPreguntaApi from "conten/supervision/SupervisionApiClient";
import { ofType } from "redux-observable";
import { Observable } from "rxjs";
import { mergeMap } from "rxjs/operators";
import { OK, ERROR, apiTimeout } from "config/general/Configuracion";

export const listarSupervisionPregunta = (action$, state$) =>
  action$.pipe(
    ofType(SUPERVISION_PREGUNTA_LISTAR),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(SupervisionPreguntaApi.listarSupervisionPregunta())
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(listarSupervisionPreguntaSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(listarSupervisionPreguntaError(error));
              var mensajeError = "Hubo un problema: " + error.message;
              if (error && error.response && error.response.data) {
                mensajeError = error.response.data;
              }
              action.onSuccess(ERROR, mensajeError + "");
              obs.next(
                addMessage({ variant: "error", message: mensajeError })
              );
              obs.complete();
            });
        })
    )
  );

export const grabarSupervisionPregunta = (action$, state$) =>
  action$.pipe(
    ofType(SUPERVISION_PREGUNTA_GRABAR),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(SupervisionPreguntaApi.grabarSupervisionPregunta(action.preguntaSupervision))
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(grabarSupervisionPreguntaSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(grabarSupervisionPreguntaError(error));
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

  export const borrarSupervisionPregunta = (action$, state$) =>
  action$.pipe(
    ofType(SUPERVISION_PREGUNTA_BORRAR),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(SupervisionPreguntaApi.borrarSupervisionPregunta(action.preguntaSupervision))
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(borrarSupervisionPreguntaSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(borrarSupervisionPreguntaError(error));
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
