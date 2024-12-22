export const DATOS_DASHBOARD_PRINCIPAL = "DATOS_DASHBOARD_PRINCIPAL";
export const DATOS_DASHBOARD_PRINCIPAL_SUCCESS = "DATOS_DASHBOARD_PRINCIPAL_SUCCESS";
export const DATOS_DASHBOARD_PRINCIPAL_ERROR = "DATOS_DASHBOARD_PRINCIPAL_ERROR";

export const solicitarDatosDashboardPrincipal = (datosConsulta, onSuccess) => ({
  type: DATOS_DASHBOARD_PRINCIPAL,
  datosConsulta,
  onSuccess,
});

export const solicitarDatosDashboardPrincipalSuccess = (respuestaServidor) => ({
  type: DATOS_DASHBOARD_PRINCIPAL_SUCCESS,
  respuestaServidor,
});

export const solicitarDatosDashboardPrincipalError = (errorContenido) => ({
  type: DATOS_DASHBOARD_PRINCIPAL_ERROR,
  errorContenido,
});

export const LISTAR_OCUPACION_ZONA = "LISTAR_OCUPACION_ZONA";
export const LISTAR_OCUPACION_ZONA_SUCCESS = "LISTAR_OCUPACION_ZONA_SUCCESS";
export const LISTAR_OCUPACION_ZONA_ERROR = "LISTAR_OCUPACION_ZONA_ERROR";

export const solicitarListarOcupacionZona = (datosConsulta, onSuccess) => ({
  type: LISTAR_OCUPACION_ZONA,
  datosConsulta,
  onSuccess,
});

export const solicitarListarOcupacionZonaSuccess = (respuestaServidor) => ({
  type: LISTAR_OCUPACION_ZONA_SUCCESS,
  respuestaServidor,
});

export const solicitarListarOcupacionZonaError = (errorContenido) => ({
  type: LISTAR_OCUPACION_ZONA_ERROR,
  errorContenido,
});