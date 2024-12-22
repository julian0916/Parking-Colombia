import { OK } from "config/general/Configuracion";
import {
  GUARDAR_PROMOTOR_ZONA,
  GUARDAR_PROMOTOR_ZONA_SUCCESS,
  GUARDAR_PROMOTOR_ZONA_ERROR,
  PROMOTOR_ZONA_LISTAR,
  PROMOTOR_ZONA_LISTAR_SUCCESS,
  PROMOTOR_ZONA_LISTAR_ERROR,
} from "conten/promotorZonas/PromotorZonaActions";

const initialState = {
  isActivityIndicatorShown: false,
  promotorZonas: [],
  paginacion: {
    limite: 5,
    actual: 1,
    filtro: undefined,
    orden: "nombre",
    sentido: "ASC",
  },
};

const promotorZonaReducer = (state = initialState, action) => {
  switch (action.type) {
    case PROMOTOR_ZONA_LISTAR:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case PROMOTOR_ZONA_LISTAR_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
        promotorZonas: action.respuestaServidor.promotorZonas,
        paginacion: action.respuestaServidor.paginacion,
      };
    case PROMOTOR_ZONA_LISTAR_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case GUARDAR_PROMOTOR_ZONA:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case GUARDAR_PROMOTOR_ZONA_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case GUARDAR_PROMOTOR_ZONA_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };

    default:
      return state;
  }
};

export default promotorZonaReducer;
