import {
  GRABAR_LIMITE_ENDEUDAMIENTO_PROMOTOR,
  GRABAR_LIMITE_ENDEUDAMIENTO_PROMOTOR_SUCCESS,
  GRABAR_LIMITE_ENDEUDAMIENTO_PROMOTOR_ERROR,
  LIMITE_ENDEUDAMIENTO_PROMOTOR,
  LIMITE_ENDEUDAMIENTO_PROMOTOR_ERROR,
  LIMITE_ENDEUDAMIENTO_PROMOTOR_SUCCESS,
  TIQUETE_GRABAR,
  TIQUETE_GRABAR_ERROR,
  TIQUETE_GRABAR_SUCCESS,
  TIQUETE_LISTAR,
  TIQUETE_LISTAR_ERROR,
  TIQUETE_LISTAR_SUCCESS,
} from "conten/configuracion/ConfiguracionActions";

const initialState = {
  isActivityIndicatorShown: false,
  tiquete: undefined,
};

const configuracionReducer = (state = initialState, action) => {
  switch (action.type) {
    case TIQUETE_LISTAR:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case TIQUETE_LISTAR_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
        tiquete: action.respuestaServidor,
      };
    case TIQUETE_LISTAR_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case TIQUETE_GRABAR:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case TIQUETE_GRABAR_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case TIQUETE_GRABAR_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case LIMITE_ENDEUDAMIENTO_PROMOTOR:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case LIMITE_ENDEUDAMIENTO_PROMOTOR_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case LIMITE_ENDEUDAMIENTO_PROMOTOR_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };

    case GRABAR_LIMITE_ENDEUDAMIENTO_PROMOTOR:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case GRABAR_LIMITE_ENDEUDAMIENTO_PROMOTOR_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case GRABAR_LIMITE_ENDEUDAMIENTO_PROMOTOR_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
      
    default:
      return state;
  }
};

export default configuracionReducer;
