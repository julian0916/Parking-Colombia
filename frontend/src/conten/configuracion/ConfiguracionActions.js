export const GRABAR_LIMITE_ENDEUDAMIENTO_PROMOTOR =
  "GRABAR_LIMITE_ENDEUDAMIENTO_PROMOTOR";
export const GRABAR_LIMITE_ENDEUDAMIENTO_PROMOTOR_SUCCESS =
  "GRABAR_LIMITE_ENDEUDAMIENTO_PROMOTOR_SUCCESS";
export const GRABAR_LIMITE_ENDEUDAMIENTO_PROMOTOR_ERROR =
  "GRABAR_LIMITE_ENDEUDAMIENTO_PROMOTOR_ERROR";

export const grabarLimiteEndeudamientoPromotor = (limite, onSuccess) => ({
  type: GRABAR_LIMITE_ENDEUDAMIENTO_PROMOTOR,
  limite,
  onSuccess,
});

export const grabarLimiteEndeudamientoPromotorSuccess = (
  respuestaServidor
) => ({
  type: GRABAR_LIMITE_ENDEUDAMIENTO_PROMOTOR_SUCCESS,
  respuestaServidor,
});

export const grabarLimiteEndeudamientoPromotorError = (errorContenido) => ({
  type: GRABAR_LIMITE_ENDEUDAMIENTO_PROMOTOR_ERROR,
  errorContenido,
});

export const LIMITE_ENDEUDAMIENTO_PROMOTOR = "LIMITE_ENDEUDAMIENTO_PROMOTOR";
export const LIMITE_ENDEUDAMIENTO_PROMOTOR_SUCCESS =
  "LIMITE_ENDEUDAMIENTO_PROMOTOR_SUCCESS";
export const LIMITE_ENDEUDAMIENTO_PROMOTOR_ERROR =
  "LIMITE_ENDEUDAMIENTO_PROMOTOR_ERROR";

export const getLimiteEndeudamientoPromotor = (onSuccess) => ({
  type: LIMITE_ENDEUDAMIENTO_PROMOTOR,
  onSuccess,
});

export const getLimiteEndeudamientoPromotorSuccess = (respuestaServidor) => ({
  type: LIMITE_ENDEUDAMIENTO_PROMOTOR_SUCCESS,
  respuestaServidor,
});

export const getLimiteEndeudamientoPromotorError = (errorContenido) => ({
  type: LIMITE_ENDEUDAMIENTO_PROMOTOR_ERROR,
  errorContenido,
});

export const TIQUETE_GRABAR = "TIQUETE_GRABAR";
export const TIQUETE_GRABAR_SUCCESS = "TIQUETE_GRABAR_SUCCESS";
export const TIQUETE_GRABAR_ERROR = "TIQUETE_GRABAR_ERROR";

export const grabarTiquete = (tiquete, onSuccess) => ({
  type: TIQUETE_GRABAR,
  tiquete,
  onSuccess,
});

export const grabarTiqueteSuccess = (respuestaServidor) => ({
  type: TIQUETE_GRABAR_SUCCESS,
  respuestaServidor,
});

export const grabarTiqueteError = (errorContenido) => ({
  type: TIQUETE_GRABAR_ERROR,
  errorContenido,
});

export const TIQUETE_LISTAR = "TIQUETE_LISTAR";
export const TIQUETE_LISTAR_SUCCESS = "TIQUETE_LISTAR_SUCCESS";
export const TIQUETE_LISTAR_ERROR = "TIQUETE_LISTAR_ERROR";

export const listarTiquete = (onSuccess) => ({
  type: TIQUETE_LISTAR,
  onSuccess,
});

export const listarTiqueteSuccess = (respuestaServidor) => ({
  type: TIQUETE_LISTAR_SUCCESS,
  respuestaServidor,
});

export const listarTiqueteError = (errorContenido) => ({
  type: TIQUETE_LISTAR_ERROR,
  errorContenido,
});
