import { OK } from "config/general/Configuracion";
import {
  GUARDAR_ZONA,
  GUARDAR_ZONA_SUCCESS,
  GUARDAR_ZONA_ERROR,
  ZONA_LISTAR,
  ZONA_LISTAR_SUCCESS,
  ZONA_LISTAR_ERROR,
} from "conten/zonas/ZonaActions";

const initialState = {
  isActivityIndicatorShown: false,
  zonas: [],
  paginacion: {
    limite: 5,
    actual: 1,
    filtro: undefined,
    orden: "nombre",
    sentido: "ASC",
  },
};

const zonaReducer = (state = initialState, action) => {
  switch (action.type) {
    case ZONA_LISTAR:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case ZONA_LISTAR_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
        zonas: action.respuestaServidor.zonas,
        paginacion: action.respuestaServidor.paginacion,
      };
    case ZONA_LISTAR_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case GUARDAR_ZONA:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case GUARDAR_ZONA_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case GUARDAR_ZONA_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };

    default:
      return state;
  }
};

export default zonaReducer;
