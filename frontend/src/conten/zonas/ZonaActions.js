export const ZONA_LISTAR = "ZONA_LISTAR";
export const ZONA_LISTAR_SUCCESS = "ZONA_LISTAR_SUCCESS";
export const ZONA_LISTAR_ERROR = "ZONA_LISTAR_ERROR";

export const listarZona = (paginacion, onSuccess) => ({
  type: ZONA_LISTAR,
  paginacion,
  onSuccess,
});

export const listarZonaSuccess = (respuestaServidor) => ({
  type: ZONA_LISTAR_SUCCESS,
  respuestaServidor,
});

export const listarZonaError = (errorContenido) => ({
  type: ZONA_LISTAR_ERROR,
  errorContenido,
});

export const GUARDAR_ZONA = "GUARDAR_ZONA";
export const GUARDAR_ZONA_SUCCESS = "GUARDAR_ZONA_SUCCESS";
export const GUARDAR_ZONA_ERROR = "GUARDAR_ZONA_ERROR";

export const guardarZona = (zona,onSuccess) => ({
  type: GUARDAR_ZONA,
  zona,
  onSuccess,
});

export const guardarZonaSuccess = (respuestaServidor) => ({
  type: GUARDAR_ZONA_SUCCESS,
  respuestaServidor,
});

export const guardarZonaError = (errorContenido) => ({
  type: GUARDAR_ZONA_ERROR,
  errorContenido,
});