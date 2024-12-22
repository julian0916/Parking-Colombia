import axios from "axios";
import { addMessage } from "conten/mensajes/MessagesActions";
import {
  ingresoInicioSolicitudSuccess,
  ingresoInicioSolicitudError,
  INGRESO_INICIO_SOLICITUD,
  ingresoCompletarSolicitudSuccess,
  ingresoCompletarSolicitudError,
  INGRESO_COMPLETAR_SOLICITUD,
  salirSesionSuccess,
  salirSesionError,
  SALIR_SESION,
} from "conten/ingresoSistema/IngresoSistemaActions";
import {
  listarCuentaUsuario,
  guardarCuentasDesdeAcceso,
} from "conten/cuentas/CuentaActions";
import {
  ID_CUENTA,
  OK,
  ERROR,
  IS_LOGGED,
  ID_PERFIL,
  NOMBRE_PERFIL,
  NOMBRE_CUENTA,
  CORREO_USUARIO,
  SESION,
  CUENTAS_JSON,
  apiTimeout,
  PERMISO_ACCESO,
} from "config/general/Configuracion";
import IngresoSistemaApi from "conten/ingresoSistema/IngresoSistemaApiClient";
import { ofType } from "redux-observable";
import { Observable, of } from "rxjs";
import { catchError, mergeMap } from "rxjs/operators";

export const salirSesion = (action$, state$) =>
  action$.pipe(
    ofType(SALIR_SESION),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(IngresoSistemaApi.salirSesion())
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(salirSesionSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(salirSesionError(error));
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

export const ingresoCompletarSolicitud = (action$, state$) =>
  action$.pipe(
    ofType(INGRESO_COMPLETAR_SOLICITUD),
    mergeMap((action) =>
      new Observable((obs) => {
        axios.defaults.timeout = apiTimeout;
        axios(
          IngresoSistemaApi.ingresoCompletarSolicitud(
            action.idSolicitud,
            action.A,
            action.tokenAuth
          )
        )
          .then((response) => {
            if (response.status === 200) {
              var posIncial = 0;
              obs.next(guardarCuentasDesdeAcceso(response.data));
              sessionStorage[PERMISO_ACCESO] = "";
              sessionStorage[CUENTAS_JSON] = JSON.stringify(response.data);
              sessionStorage[IS_LOGGED] = "true";
              sessionStorage[CORREO_USUARIO] = response.data[posIncial].correo;
              sessionStorage[SESION] = response.data[posIncial].sesion;
              sessionStorage[NOMBRE_CUENTA] = response.data[posIncial].nombre;
              sessionStorage[ID_CUENTA] = response.data[posIncial].cuenta;
              sessionStorage[ID_PERFIL] = response.data[posIncial].idPerfil;
              sessionStorage[NOMBRE_PERFIL] = response.data[posIncial].perfil; //sessionStorage[PERFIL] = response.data[posIncial].perfil;
              obs.next(
                listarCuentaUsuario((estado, cuentas) => {
                  if (estado === OK) {
                    if (
                      cuentas !== undefined &&
                      cuentas != null &&
                      cuentas.length > 0
                    ) {
                      sessionStorage[NOMBRE_CUENTA] = cuentas[posIncial].nombre;
                      sessionStorage[ID_CUENTA] = cuentas[posIncial].cuenta;
                      sessionStorage[ID_PERFIL] = cuentas[posIncial].idPerfil;
                      sessionStorage[NOMBRE_PERFIL] = cuentas[posIncial].perfil; //sessionStorage[PERFIL] = cuentas[posIncial].perfil;
                      sessionStorage[CORREO_USUARIO] =
                        cuentas[posIncial].correo;
                    }
                  }
                  obs.next(ingresoCompletarSolicitudSuccess(response));
                  obs.complete();
                  action.onSuccess(OK, response.data);
                })
              );
            } else {
              obs.next(ingresoCompletarSolicitudError(ERROR));
              obs.complete();
              action.onSuccess(
                ERROR,
                "Verifique los datos y vuelva a intentarlo :("
              );
            }
          })
          .catch((error) => {
            obs.next(ingresoCompletarSolicitudError(error));
            obs.complete();
            action.onSuccess(
              ERROR,
              "Verifique que los datos sean correctos y tenga acceso a internet :("
            );
          });
      }).pipe(
        catchError((error) =>
          of(
            ingresoCompletarSolicitudError(error),
            addMessage({ variant: "error", message: "Error" })
          )
        )
      )
    )
  );

export const ingresoInicioSolicitud = (action$, state$) =>
  action$.pipe(
    ofType(INGRESO_INICIO_SOLICITUD),
    mergeMap((action) =>
      new Observable((obs) => {
        axios.defaults.timeout = apiTimeout;
        axios(IngresoSistemaApi.ingresoInicioSolicitud())
          .then((response) => {
            if (response.status === 200) {
              obs.next(ingresoInicioSolicitudSuccess(response));
              obs.complete();
              action.onSuccess(OK, response.data);
            } else {
              obs.next(ingresoInicioSolicitudError(ERROR));
              obs.complete();
              action.onSuccess(
                ERROR,
                "No fue posible iniciar el proceso solicitado. Intentenlo nuevamente"
              );
            }
          })
          .catch((error) => {
            obs.next(ingresoInicioSolicitudError(error));
            obs.complete();
            action.onSuccess(
              ERROR,
              "Verifique que los datos sean correctos y tenga acceso a internet :("
            );
          });
      }).pipe(
        catchError((error) =>
          of(
            ingresoInicioSolicitudError(error),
            addMessage({ variant: "error", message: "Error" })
          )
        )
      )
    )
  );
