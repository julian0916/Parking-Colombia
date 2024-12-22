import { validarSesion } from "config/funciones/Comunes";
import {
  INGRESO_COMPLETAR_SOLICITUD,
  INGRESO_COMPLETAR_SOLICITUD_ERROR,
  INGRESO_COMPLETAR_SOLICITUD_SUCCESS,
  INGRESO_INICIO_SOLICITUD,
  INGRESO_INICIO_SOLICITUD_ERROR,
  INGRESO_INICIO_SOLICITUD_SUCCESS,
  SALIR_SESION,
  SALIR_SESION_ERROR,
  SALIR_SESION_SUCCESS,
  SET_MENU_USUARIO,
} from "conten/ingresoSistema/IngresoSistemaActions";

const initialState = {
  isActivityIndicatorShown: false,
  contenidoMenu: [],
};

const ingresoSistemaReducer = (state = initialState, action) => {  
  
  validarSesion(action);

  switch (action.type) {
    case SALIR_SESION:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case SALIR_SESION_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case SALIR_SESION_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case INGRESO_INICIO_SOLICITUD:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case INGRESO_INICIO_SOLICITUD_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case INGRESO_INICIO_SOLICITUD_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case INGRESO_COMPLETAR_SOLICITUD:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case INGRESO_COMPLETAR_SOLICITUD_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case INGRESO_COMPLETAR_SOLICITUD_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case SET_MENU_USUARIO:
      return {
        ...state,
        contenidoMenu: action.contenidoMenu,
      };
    default:
      return state;
  }
};

export default ingresoSistemaReducer;
