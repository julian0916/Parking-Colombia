import {
  GUARDAR_PAGO_MUNICIPIO,
  GUARDAR_PAGO_MUNICIPIO_ERROR,
  GUARDAR_PAGO_MUNICIPIO_SUCCESS,
  GUARDAR_RECAUDO_REPORTE,
  GUARDAR_RECAUDO_REPORTE_ERROR,
  GUARDAR_RECAUDO_REPORTE_SUCCESS,
  LISTAR_PAGO_MUNICIPIO,
  LISTAR_PAGO_MUNICIPIO_ERROR,
  LISTAR_PAGO_MUNICIPIO_SUCCESS,
  LISTAR_RECAUDO_REPORTE,
  LISTAR_RECAUDO_REPORTE_ERROR,
  LISTAR_RECAUDO_REPORTE_SUCCESS,
  LISTAR_FECHA_PAGOS,
  LISTAR_FECHA_PAGOS_SUCCESS,
  LISTAR_FECHA_PAGOS_ERROR,
} from "conten/financiera/FinancieraActions";

const initialState = {
  isActivityIndicatorShown: false,
};

const financieraReducer = (state = initialState, action) => {
  switch (action.type) {
    case LISTAR_RECAUDO_REPORTE:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case LISTAR_RECAUDO_REPORTE_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case LISTAR_RECAUDO_REPORTE_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case GUARDAR_RECAUDO_REPORTE:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case GUARDAR_RECAUDO_REPORTE_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case GUARDAR_RECAUDO_REPORTE_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case LISTAR_PAGO_MUNICIPIO:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case LISTAR_PAGO_MUNICIPIO_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case LISTAR_PAGO_MUNICIPIO_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case GUARDAR_PAGO_MUNICIPIO:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case GUARDAR_PAGO_MUNICIPIO_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case GUARDAR_PAGO_MUNICIPIO_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
      case LISTAR_FECHA_PAGOS:
        return {
          ...state,
          isActivityIndicatorShown: false,
        };
      case LISTAR_FECHA_PAGOS_SUCCESS:
        return {
          ...state,
          isActivityIndicatorShown: false,
        };
      case LISTAR_FECHA_PAGOS_ERROR:
        return {
          ...state,
          isActivityIndicatorShown: false,
        };
    default:
      return state;
  }
};

export default financieraReducer;
