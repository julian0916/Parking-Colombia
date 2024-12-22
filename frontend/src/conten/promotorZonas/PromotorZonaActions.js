export const PROMOTOR_ZONA_LISTAR = "PROMOTOR_ZONA_LISTAR";
export const PROMOTOR_ZONA_LISTAR_SUCCESS = "PROMOTOR_ZONA_LISTAR_SUCCESS";
export const PROMOTOR_ZONA_LISTAR_ERROR = "PROMOTOR_ZONA_LISTAR_ERROR";

export const listarPromotorZona = (paginacion, onSuccess) => ({
  type: PROMOTOR_ZONA_LISTAR,
  paginacion,
  onSuccess,
});

export const listarPromotorZonaSuccess = (respuestaServidor) => ({
  type: PROMOTOR_ZONA_LISTAR_SUCCESS,
  respuestaServidor,
});

export const listarPromotorZonaError = (errorContenido) => ({
  type: PROMOTOR_ZONA_LISTAR_ERROR,
  errorContenido,
});

export const GUARDAR_PROMOTOR_ZONA = "GUARDAR_PROMOTOR_ZONA";
export const GUARDAR_PROMOTOR_ZONA_SUCCESS = "GUARDAR_PROMOTOR_ZONA_SUCCESS";
export const GUARDAR_PROMOTOR_ZONA_ERROR = "GUARDAR_PROMOTOR_ZONA_ERROR";

export const guardarPromotorZona = (promotorZona,onSuccess) => ({
  type: GUARDAR_PROMOTOR_ZONA,
  promotorZona,
  onSuccess,
});

export const guardarPromotorZonaSuccess = (respuestaServidor) => ({
  type: GUARDAR_PROMOTOR_ZONA_SUCCESS,
  respuestaServidor,
});

export const guardarPromotorZonaError = (errorContenido) => ({
  type: GUARDAR_PROMOTOR_ZONA_ERROR,
  errorContenido,
});