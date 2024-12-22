export const INGRESO_INICIO_SOLICITUD = "INGRESO_INICIO_SOLICITUD";
export const INGRESO_INICIO_SOLICITUD_SUCCESS =
  "INGRESO_INICIO_SOLICITUD_SUCCESS";
export const INGRESO_INICIO_SOLICITUD_ERROR = "INGRESO_INICIO_SOLICITUD_ERROR";

export const ingresoInicioSolicitud = (onSuccess) => ({
  type: INGRESO_INICIO_SOLICITUD,
  onSuccess,
});

export const ingresoInicioSolicitudSuccess = (respuestaServidor) => ({
  type: INGRESO_INICIO_SOLICITUD_SUCCESS,
  respuestaServidor,
});

export const ingresoInicioSolicitudError = (errorContenido) => ({
  type: INGRESO_INICIO_SOLICITUD_ERROR,
  errorContenido,
});

export const INGRESO_COMPLETAR_SOLICITUD = "INGRESO_COMPLETAR_SOLICITUD";
export const INGRESO_COMPLETAR_SOLICITUD_SUCCESS =
  "INGRESO_COMPLETAR_SOLICITUD_SUCCESS";
export const INGRESO_COMPLETAR_SOLICITUD_ERROR = 
  "INGRESO_COMPLETAR_SOLICITUD_ERROR";

export const ingresoCompletarSolicitud = (
  idSolicitud,
  A,
  tokenAuth,
  onSuccess
) => ({
  type: INGRESO_COMPLETAR_SOLICITUD,
  idSolicitud,
  A,
  tokenAuth,
  onSuccess,
});

export const ingresoCompletarSolicitudSuccess = (respuestaServidor) => ({
  type: INGRESO_COMPLETAR_SOLICITUD_SUCCESS,
  respuestaServidor,
});

export const ingresoCompletarSolicitudError = (errorContenido) => ({
  type: INGRESO_COMPLETAR_SOLICITUD_ERROR,
  errorContenido,
});

export const SET_MENU_USUARIO = 'SET_MENU_USUARIO';
export const setMenuUsuario = (contenidoMenu, onSuccess) => (
  {
    type: SET_MENU_USUARIO
    , onSuccess
    , contenidoMenu
  }
);

export const SALIR_SESION = "SALIR_SESION";
export const SALIR_SESION_SUCCESS =
  "SALIR_SESION_SUCCESS";
export const SALIR_SESION_ERROR = "SALIR_SESION_ERROR";

export const salirSesion = (onSuccess) => ({
  type: SALIR_SESION,
  onSuccess,
});

export const salirSesionSuccess = (respuestaServidor) => ({
  type: SALIR_SESION_SUCCESS,
  respuestaServidor,
});

export const salirSesionError = (errorContenido) => ({
  type: SALIR_SESION_ERROR,
  errorContenido,
});