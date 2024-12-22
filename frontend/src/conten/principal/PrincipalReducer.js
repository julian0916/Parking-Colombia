import { OK } from "config/general/Configuracion";
import {
  DATOS_DASHBOARD_PRINCIPAL,
  DATOS_DASHBOARD_PRINCIPAL_SUCCESS,
  DATOS_DASHBOARD_PRINCIPAL_ERROR,
  LISTAR_OCUPACION_ZONA,
  LISTAR_OCUPACION_ZONA_SUCCESS,
  LISTAR_OCUPACION_ZONA_ERROR,
} from "conten/principal/PrincipalActions";

const initialState = {
  isActivityIndicatorShown: false,
  datosDashboard: {},
  listadoOcupacionZona:[]
};

const principalReducer = (state = initialState, action) => {
  switch (action.type) {
    case DATOS_DASHBOARD_PRINCIPAL:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case DATOS_DASHBOARD_PRINCIPAL_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
        datosDashboard: action.respuestaServidor,
      };
    case DATOS_DASHBOARD_PRINCIPAL_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };

    case LISTAR_OCUPACION_ZONA:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case LISTAR_OCUPACION_ZONA_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
        listadoOcupacionZona: action.respuestaServidor,
      };
    case LISTAR_OCUPACION_ZONA_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    default:
      return state;
  }
};

export default principalReducer;
