import { OK } from "config/general/Configuracion";
import {
  CUENTA_GRABAR,
  CUENTA_GRABAR_SUCCESS,
  CUENTA_GRABAR_ERROR,
  CUENTA_LISTAR,
  CUENTA_LISTAR_SUCCESS,
  CUENTA_LISTAR_ERROR,
  CUENTA_PARA_EDITAR,
  CUENTA_USUARIO_LISTAR,
  CUENTA_USUARIO_LISTAR_SUCCESS,
  CUENTA_USUARIO_LISTAR_ERROR,
  CUENTAS_DESDE_ACCESO,
} from "conten/cuentas/CuentaActions";

const initialState = {
  isActivityIndicatorShown: false,
  cuentasDesdeAcceso: [],
  cuentasUsuario: [],
  cuenta: undefined,
  cuentaEditar: undefined,
  cuentas: [],
  paginacion: {
    limite: 5,
    actual: 1,
    filtro: undefined,
    orden: "nombre",
    sentido: "ASC",
  },
};

const cuentaReducer = (state = initialState, action) => {
  
  switch (action.type) {
    case CUENTA_GRABAR:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case CUENTA_GRABAR_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
        cuenta: action.data,
      };
    case CUENTA_GRABAR_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case CUENTA_LISTAR:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case CUENTA_LISTAR_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
        cuentas: action.respuestaServidor.cuentas,
        paginacion: action.respuestaServidor.paginacion,
      };
    case CUENTA_LISTAR_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case CUENTA_PARA_EDITAR:
      return {
        ...state,
        cuentaEditar: action.cuenta,
      };
    case CUENTA_USUARIO_LISTAR:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case CUENTA_USUARIO_LISTAR_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
        cuentasUsuario: action.respuestaServidor.cuentas,
      };
    case CUENTA_USUARIO_LISTAR_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case CUENTAS_DESDE_ACCESO:
      return {
        ...state,
        cuentasDesdeAcceso: action.cuentasDesdeAcceso,
      };

    default:
      return state;
  }
};

export default cuentaReducer;
