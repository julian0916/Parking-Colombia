import {
  INFORME_MENSUAL,
  INFORME_MENSUAL_ERROR,
  INFORME_MENSUAL_SUCCESS,
  INFORME_SALDO_PROMOTOR,
  INFORME_SALDO_PROMOTOR_ERROR,
  INFORME_SALDO_PROMOTOR_SUCCESS,
  INFORME_SUPERVISION,
  INFORME_SUPERVISION_ERROR,
  INFORME_SUPERVISION_SUCCESS,
  INFORME_VEHICULO,
  INFORME_VEHICULO_ERROR,
  INFORME_VEHICULO_SUCCESS,
  INFORME_CARTERA,
  INFORME_CARTERA_SUCCESS,
  INFORME_CARTERA_ERROR,
  HACER_PAGO_EXTEMPORANEO_WEB,
  HACER_PAGO_EXTEMPORANEO_WEB_SUCCESS,
  HACER_PAGO_EXTEMPORANEO_ERROR,
  INFORME_HISTORICO,
  INFORME_HISTORICO_SUCCESS,
  INFORME_HISTORICO_ERROR,
  INFORME_HISTORICO_MENSUAL,
  INFORME_HISTORICO_MENSUAL_ERROR,
  INFORME_HISTORICO_MENSUAL_SUCCESS,
} from 'conten/informes/InformesActions';

const initialState = {
  isActivityIndicatorShown: false,
};

const informesReducer = (state = initialState, action) => {
  //console.log("--reducer",action);
  switch (action.type) {
    case HACER_PAGO_EXTEMPORANEO_WEB:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case HACER_PAGO_EXTEMPORANEO_WEB_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case HACER_PAGO_EXTEMPORANEO_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };

    case INFORME_SUPERVISION:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case INFORME_SUPERVISION_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case INFORME_SUPERVISION_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };

    case INFORME_SALDO_PROMOTOR:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case INFORME_SALDO_PROMOTOR_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case INFORME_SALDO_PROMOTOR_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case INFORME_MENSUAL:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case INFORME_MENSUAL_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case INFORME_MENSUAL_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };

    case INFORME_VEHICULO:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case INFORME_VEHICULO_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case INFORME_VEHICULO_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };

    case INFORME_CARTERA:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case INFORME_CARTERA_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case INFORME_CARTERA_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };

    case INFORME_HISTORICO:
      return {
        ...state,
        isActivityIndicatorShown: true,
      };
    case INFORME_HISTORICO_SUCCESS:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };
    case INFORME_HISTORICO_ERROR:
      return {
        ...state,
        isActivityIndicatorShown: false,
      };

    default:
      return state;
  }
};

export default informesReducer;
