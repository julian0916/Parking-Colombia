import { OK } from "config/general/Configuracion";
import {
  SUPERVISION_PREGUNTA_GRABAR,
  SUPERVISION_PREGUNTA_GRABAR_SUCCESS,
  SUPERVISION_PREGUNTA_GRABAR_ERROR,
  SUPERVISION_PREGUNTA_LISTAR,
  SUPERVISION_PREGUNTA_LISTAR_SUCCESS,
  SUPERVISION_PREGUNTA_LISTAR_ERROR,
  SUPERVISION_PREGUNTA_BORRAR,
  SUPERVISION_PREGUNTA_BORRAR_SUCCESS,
  SUPERVISION_PREGUNTA_BORRAR_ERROR,
} from "conten/supervision/SupervisionActions";

const initialState = {
  isActivityIndicatorShown: false,
  listadoPreguntasSupervision: [],
};

const supervisionReducer = (state = initialState, action) => {
  switch (action.type) {
    case SUPERVISION_PREGUNTA_LISTAR:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case SUPERVISION_PREGUNTA_LISTAR_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
        listadoPreguntasSupervision: action.respuestaServidor,
      };
    case SUPERVISION_PREGUNTA_LISTAR_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case SUPERVISION_PREGUNTA_GRABAR:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case SUPERVISION_PREGUNTA_GRABAR_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
        listadoPreguntasSupervision: action.respuestaServidor,
      };
    case SUPERVISION_PREGUNTA_GRABAR_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
      case SUPERVISION_PREGUNTA_BORRAR:
        return {
          ...state,
          isActivityIndicatorShown: true,
        };
      case SUPERVISION_PREGUNTA_BORRAR_SUCCESS:
        return {
          ...state,
          isActivityIndicatorShown: false,
          listadoPreguntasSupervision: action.respuestaServidor,
        };
      case SUPERVISION_PREGUNTA_BORRAR_ERROR:
        return {
          ...state,
          isActivityIndicatorShown: false,
        };
    default:
      return state;
  }
};

export default supervisionReducer;
