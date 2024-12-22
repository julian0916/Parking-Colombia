export const SUPERVISION_PREGUNTA_GRABAR = "SUPERVISION_PREGUNTA_GRABAR";
export const SUPERVISION_PREGUNTA_GRABAR_SUCCESS = "SUPERVISION_PREGUNTA_GRABAR_SUCCESS";
export const SUPERVISION_PREGUNTA_GRABAR_ERROR = "SUPERVISION_PREGUNTA_GRABAR_ERROR";

export const grabarSupervisionPregunta = (preguntaSupervision, onSuccess) => ({
  type: SUPERVISION_PREGUNTA_GRABAR,
  preguntaSupervision,
  onSuccess,
});

export const grabarSupervisionPreguntaSuccess = (respuestaServidor) => ({
  type: SUPERVISION_PREGUNTA_GRABAR_SUCCESS,
  respuestaServidor,
});

export const grabarSupervisionPreguntaError = (errorContenido) => ({
  type: SUPERVISION_PREGUNTA_GRABAR_ERROR,
  errorContenido,
});

export const SUPERVISION_PREGUNTA_LISTAR = "SUPERVISION_PREGUNTA_LISTAR";
export const SUPERVISION_PREGUNTA_LISTAR_SUCCESS = "SUPERVISION_PREGUNTA_LISTAR_SUCCESS";
export const SUPERVISION_PREGUNTA_LISTAR_ERROR = "SUPERVISION_PREGUNTA_LISTAR_ERROR";

export const listarSupervisionPregunta = (onSuccess) => ({
  type: SUPERVISION_PREGUNTA_LISTAR,
  onSuccess,
});

export const listarSupervisionPreguntaSuccess = (respuestaServidor) => ({
  type: SUPERVISION_PREGUNTA_LISTAR_SUCCESS,
  respuestaServidor,
});

export const listarSupervisionPreguntaError = (errorContenido) => ({
  type: SUPERVISION_PREGUNTA_LISTAR_ERROR,
  errorContenido,
});

export const SUPERVISION_PREGUNTA_BORRAR = "SUPERVISION_PREGUNTA_BORRAR";
export const SUPERVISION_PREGUNTA_BORRAR_SUCCESS = "SUPERVISION_PREGUNTA_BORRAR_SUCCESS";
export const SUPERVISION_PREGUNTA_BORRAR_ERROR = "SUPERVISION_PREGUNTA_BORRAR_ERROR";

export const borrarSupervisionPregunta = (preguntaSupervision, onSuccess) => ({
  type: SUPERVISION_PREGUNTA_BORRAR,
  preguntaSupervision,
  onSuccess,
});

export const borrarSupervisionPreguntaSuccess = (respuestaServidor) => ({
  type: SUPERVISION_PREGUNTA_BORRAR_SUCCESS,
  respuestaServidor,
});

export const borrarSupervisionPreguntaError = (errorContenido) => ({
  type: SUPERVISION_PREGUNTA_BORRAR_ERROR,
  errorContenido,
});