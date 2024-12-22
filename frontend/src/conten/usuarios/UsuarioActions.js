export const USUARIO_LISTAR = "USUARIO_LISTAR";
export const USUARIO_LISTAR_SUCCESS = "USUARIO_LISTAR_SUCCESS";
export const USUARIO_LISTAR_ERROR = "USUARIO_LISTAR_ERROR";

export const listarUsuario = (paginacion, onSuccess) => ({
  type: USUARIO_LISTAR,
  paginacion,
  onSuccess,
});

export const listarUsuarioSuccess = (respuestaServidor) => ({
  type: USUARIO_LISTAR_SUCCESS,
  respuestaServidor,
});

export const listarUsuarioError = (errorContenido) => ({
  type: USUARIO_LISTAR_ERROR,
  errorContenido,
});

export const GUARDAR_USUARIO = "GUARDAR_USUARIO";
export const GUARDAR_USUARIO_SUCCESS = "GUARDAR_USUARIO_SUCCESS";
export const GUARDAR_USUARIO_ERROR = "GUARDAR_USUARIO_ERROR";

export const guardarUsuario = (usuario,onSuccess) => ({
  type: GUARDAR_USUARIO,
  usuario,
  onSuccess,
});

export const guardarUsuarioSuccess = (respuestaServidor) => ({
  type: GUARDAR_USUARIO_SUCCESS,
  respuestaServidor,
});

export const guardarUsuarioError = (errorContenido) => ({
  type: GUARDAR_USUARIO_ERROR,
  errorContenido,
});

export const LISTAR_PERFILES = "LISTAR_PERFILES";
export const LISTAR_PERFILES_SUCCESS = "LISTAR_PERFILES_SUCCESS";
export const LISTAR_PERFILES_ERROR = "LISTAR_PERFILES_ERROR";

export const listarPerfiles = (onSuccess) => ({
  type: LISTAR_PERFILES,
  onSuccess,
});

export const listarPerfilesSuccess = (respuestaServidor) => ({
  type: LISTAR_PERFILES_SUCCESS,
  respuestaServidor,
});

export const listarPerfilesError = (errorContenido) => ({
  type: LISTAR_PERFILES_ERROR,
  errorContenido,
});

export const LISTAR_DOCUMENTOS = "LISTAR_DOCUMENTOS";
export const LISTAR_DOCUMENTOS_SUCCESS = "LISTAR_DOCUMENTOS_SUCCESS";
export const LISTAR_DOCUMENTOS_ERROR = "LISTAR_DOCUMENTOS_ERROR";

export const listarDocumentos = (onSuccess) => ({
  type: LISTAR_DOCUMENTOS,
  onSuccess,
});

export const listarDocumentosSuccess = (respuestaServidor) => ({
  type: LISTAR_DOCUMENTOS_SUCCESS,
  respuestaServidor,
});

export const listarDocumentosError = (errorContenido) => ({
  type: LISTAR_DOCUMENTOS_ERROR,
  errorContenido,
});