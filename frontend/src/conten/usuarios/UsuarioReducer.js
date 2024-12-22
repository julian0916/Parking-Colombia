import { OK } from "config/general/Configuracion";
import {
  LISTAR_DOCUMENTOS,
  LISTAR_DOCUMENTOS_SUCCESS,
  LISTAR_DOCUMENTOS_ERROR,
  LISTAR_PERFILES,
  LISTAR_PERFILES_SUCCESS,
  LISTAR_PERFILES_ERROR,
  GUARDAR_USUARIO,
  GUARDAR_USUARIO_SUCCESS,
  GUARDAR_USUARIO_ERROR,
  USUARIO_LISTAR,
  USUARIO_LISTAR_SUCCESS,
  USUARIO_LISTAR_ERROR,
} from "conten/usuarios/UsuarioActions";

const initialState = {
  isActivityIndicatorShown: false,
  documentos: [],
  perfiles: [],
  usuarios: [],
  paginacion: {
    limite: 5,
    actual: 1,
    filtro: undefined,
    orden: "nombre",
    sentido: "ASC",
  },
};

const usuarioReducer = (state = initialState, action) => {
  switch (action.type) {
    case USUARIO_LISTAR:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case USUARIO_LISTAR_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
        usuarios: action.respuestaServidor.usuarios,
        paginacion: action.respuestaServidor.paginacion,
      };
    case USUARIO_LISTAR_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case GUARDAR_USUARIO:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case GUARDAR_USUARIO_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case GUARDAR_USUARIO_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case LISTAR_PERFILES:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case LISTAR_PERFILES_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
        perfiles: action.respuestaServidor.perfiles,
      };
    case LISTAR_PERFILES_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case LISTAR_DOCUMENTOS:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case LISTAR_DOCUMENTOS_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
        documentos: action.respuestaServidor.documentos,
      };
    case LISTAR_DOCUMENTOS_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };

    default:
      return state;
  }
};

export default usuarioReducer;
