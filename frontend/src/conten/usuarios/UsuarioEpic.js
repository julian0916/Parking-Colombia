import axios from "axios";
import { addMessage } from "conten/mensajes/MessagesActions";
import {
  LISTAR_DOCUMENTOS,
  listarDocumentosSuccess,
  listarDocumentosError,
  LISTAR_PERFILES,
  listarPerfilesSuccess,
  listarPerfilesError,
  GUARDAR_USUARIO,
  guardarUsuarioSuccess,
  guardarUsuarioError,
  USUARIO_LISTAR,
  listarUsuarioSuccess,
  listarUsuarioError,
} from "conten/usuarios/UsuarioActions";
import UsuarioApi from "conten/usuarios/UsuarioApiClient";
import { ofType } from "redux-observable";
import { Observable } from "rxjs";
import { mergeMap } from "rxjs/operators";
import { OK, ERROR, apiTimeout } from "config/general/Configuracion";

export const listarUsuario = (action$, state$) =>
  action$.pipe(
    ofType(USUARIO_LISTAR),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(UsuarioApi.listarUsuario(action.paginacion))
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(listarUsuarioSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(listarUsuarioError(error));
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

export const guardarUsuario = (action$, state$) =>
  action$.pipe(
    ofType(GUARDAR_USUARIO),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(UsuarioApi.guardarUsuario(action.usuario))
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(guardarUsuarioSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(guardarUsuarioError(error));
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

export const listarPerfiles = (action$, state$) =>
  action$.pipe(
    ofType(LISTAR_PERFILES),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(UsuarioApi.listarPerfiles())
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(listarPerfilesSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(listarPerfilesError(error));
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

export const listarDocumentos = (action$, state$) =>
  action$.pipe(
    ofType(LISTAR_DOCUMENTOS),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(UsuarioApi.listarDocumentos())
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(listarDocumentosSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(listarDocumentosError(error));
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
