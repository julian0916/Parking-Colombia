import axios from "axios";
import { addMessage } from "conten/mensajes/MessagesActions";
import {
  CUENTA_GRABAR,
  grabarCuentaSuccess,
  grabarCuentaError,
  CUENTA_LISTAR,
  listarCuentaSuccess,
  listarCuentaError,
  CUENTA_PARA_EDITAR,
  CUENTA_USUARIO_LISTAR,
  listarCuentaUsuarioSuccess,
  listarCuentaUsuarioError,
} from "conten/cuentas/CuentaActions";
import CuentaApi from "conten/cuentas/CuentaApiClient";
import { ofType } from "redux-observable";
import { Observable } from "rxjs";
import { mergeMap } from "rxjs/operators";
import {
  OK,
  ERROR,
  apiTimeout,
  ID_PERFIL,
  PERFIL_CUENTAS,
} from "config/general/Configuracion";

export const listarCuentaUsuario = (action$, state$) =>
  action$.pipe(
    ofType(CUENTA_USUARIO_LISTAR),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          if (sessionStorage[ID_PERFIL] === PERFIL_CUENTAS) {
            axios.defaults.timeout = apiTimeout;
            axios(CuentaApi.listarCuentaUsuario())
              .then((response) => {
                if (response.status === 200 || response.status === 201) {
                  obs.next(listarCuentaUsuarioSuccess(response.data));
                  obs.complete();
                  action.onSuccess(OK, response.data);
                }
              })
              .catch((error) => {
                obs.next(listarCuentaUsuarioError(error));
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
          } else {
            obs.next(
              listarCuentaUsuarioSuccess(
                state$.value.cuentaState.cuentasDesdeAcceso
              )
            );
            obs.complete();
            action.onSuccess(OK, state$.value.cuentaState.cuentasDesdeAcceso);
          }
        })
    )
  );

export const editarCuenta = (action$, state$) =>
  action$.pipe(
    ofType(CUENTA_PARA_EDITAR),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          setTimeout(() => {
            if (action.onSuccess) {
              action.onSuccess(OK, "Listo para editar"); //clave para darle tiempo al sistema de actualizar bien el estado
            }
          }, 1000);
          obs.complete();
        })
    )
  );

export const listarCuenta = (action$, state$) =>
  action$.pipe(
    ofType(CUENTA_LISTAR),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(CuentaApi.listarCuenta(action.paginacion))
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(listarCuentaSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(listarCuentaError(error));
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

export const grabarCuenta = (action$, state$) =>
  action$.pipe(
    ofType(CUENTA_GRABAR),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(CuentaApi.grabarCuenta(action.cuenta))
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(grabarCuentaSuccess(response));
                action.onSuccess(OK, response.data);
                obs.complete();
              }
            })
            .catch((error) => {
              obs.next(grabarCuentaError(error));
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
