export const CUENTA_GRABAR = "CUENTA_GRABAR";
export const CUENTA_GRABAR_SUCCESS = "CUENTA_GRABAR_SUCCESS";
export const CUENTA_GRABAR_ERROR = "CUENTA_GRABAR_ERROR";

export const grabarCuenta = (cuenta, onSuccess) => ({
  type: CUENTA_GRABAR,
  cuenta,
  onSuccess,
});

export const grabarCuentaSuccess = (respuestaServidor) => ({
  type: CUENTA_GRABAR_SUCCESS,
  respuestaServidor,
});

export const grabarCuentaError = (errorContenido) => ({
  type: CUENTA_GRABAR_ERROR,
  errorContenido,
});

export const CUENTA_LISTAR = "CUENTA_LISTAR";
export const CUENTA_LISTAR_SUCCESS = "CUENTA_LISTAR_SUCCESS";
export const CUENTA_LISTAR_ERROR = "CUENTA_LISTAR_ERROR";

export const listarCuenta = (paginacion, onSuccess) => ({
  type: CUENTA_LISTAR,
  paginacion,
  onSuccess,
});

export const listarCuentaSuccess = (respuestaServidor) => ({
  type: CUENTA_LISTAR_SUCCESS,
  respuestaServidor,
});

export const listarCuentaError = (errorContenido) => ({
  type: CUENTA_LISTAR_ERROR,
  errorContenido,
});

export const CUENTA_USUARIO_LISTAR = "CUENTA_USUARIO_LISTAR";
export const CUENTA_USUARIO_LISTAR_SUCCESS = "CUENTA_USUARIO_LISTAR_SUCCESS";
export const CUENTA_USUARIO_LISTAR_ERROR = "CUENTA_USUARIO_LISTAR_ERROR";

export const listarCuentaUsuario = (onSuccess) => ({
  type: CUENTA_USUARIO_LISTAR,
  onSuccess,
});

export const listarCuentaUsuarioSuccess = (respuestaServidor) => ({
  type: CUENTA_USUARIO_LISTAR_SUCCESS,
  respuestaServidor,
});

export const listarCuentaUsuarioError = (errorContenido) => ({
  type: CUENTA_USUARIO_LISTAR_ERROR,
  errorContenido,
});

export const CUENTA_PARA_EDITAR = "CUENTA_PARA_EDITAR";
export const editarCuenta = (cuenta,onSuccess) => ({
  type: CUENTA_PARA_EDITAR,
  cuenta,
  onSuccess
});

export const CUENTAS_DESDE_ACCESO = "CUENTAS_DESDE_ACCESO";
export const guardarCuentasDesdeAcceso = (cuentasDesdeAcceso) => ({
  type: CUENTAS_DESDE_ACCESO,
  cuentasDesdeAcceso
});