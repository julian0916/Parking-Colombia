import axios from "axios";
import { addMessage } from "conten/mensajes/MessagesActions";
import {
  DATOS_DASHBOARD_PRINCIPAL,
  solicitarDatosDashboardPrincipalSuccess,
  solicitarDatosDashboardPrincipalError,
  LISTAR_OCUPACION_ZONA,
  solicitarListarOcupacionZonaSuccess,
  solicitarListarOcupacionZonaError,
} from "conten/principal/PrincipalActions";
import PrincipalApi from "conten/principal/PrincipalApiClient";
import { ofType } from "redux-observable";
import { Observable } from "rxjs";
import { mergeMap } from "rxjs/operators";
import { OK, ERROR, apiTimeout } from "config/general/Configuracion";

export const solicitarDatosDashboardPrincipal = (action$, state$) =>
  action$.pipe(
    ofType(DATOS_DASHBOARD_PRINCIPAL),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(PrincipalApi.getDatosConsulta(action.datosConsulta))
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(
                  solicitarDatosDashboardPrincipalSuccess(response.data)
                );
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(solicitarDatosDashboardPrincipalError(error));
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

export const solicitarListarOcupacionZona = (action$, state$) =>
  action$.pipe(
    ofType(LISTAR_OCUPACION_ZONA),
    mergeMap(
      (action) =>
        new Observable((obs) => {
          axios.defaults.timeout = apiTimeout;
          axios(
            PrincipalApi.getDatosSolicitarListarOcupacionZona(
              action.datosConsulta
            )
          )
            .then((response) => {
              if (response.status === 200 || response.status === 201) {
                obs.next(solicitarListarOcupacionZonaSuccess(response.data));
                obs.complete();
                action.onSuccess(OK, response.data);
              }
            })
            .catch((error) => {
              obs.next(solicitarListarOcupacionZonaError(error));
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
