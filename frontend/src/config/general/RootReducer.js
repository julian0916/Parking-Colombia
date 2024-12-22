import { applyMiddleware, combineReducers, compose, createStore } from "redux";
import { createEpicMiddleware } from "redux-observable";

import { rootEpic } from "config/general/EpicReducer";

import ingresoSistemaReducer from "conten/ingresoSistema/IngresoSistemaReducer";
import cuentaReducer from "conten/cuentas/CuentaReducer";
import messagesReducer from "conten/mensajes/MessagesReducer";
import usuarioReducer from "conten/usuarios/UsuarioReducer";
import zonaReducer from "conten/zonas/ZonaReducer";
import promotorZonaReducer from "conten/promotorZonas/PromotorZonaReducer";
import configuracionReducer from "conten/configuracion/ConfiguracionReducer";
import principalReducer from "conten/principal/PrincipalReducer";
import financieraReducer from "conten/financiera/FinancieraReducer";
import supervisionReducer from "conten/supervision/SupervisionReducer";
import informesReducer from "conten/informes/InformesReducer";

const epicMiddleware = createEpicMiddleware();

const rootReducer = combineReducers({
  cuentaState: cuentaReducer,
  messagesState: messagesReducer,
  ingresoSistemaState: ingresoSistemaReducer,
  usuarioState: usuarioReducer,
  zonaState: zonaReducer,
  promotorZonaState: promotorZonaReducer,
  configuracionState: configuracionReducer,
  principalState: principalReducer,
  financieraState: financieraReducer,
  supervisionState: supervisionReducer,
  informesState: informesReducer,
});

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const store = createStore(
  rootReducer,
  composeEnhancers(applyMiddleware(epicMiddleware))
);

epicMiddleware.run(rootEpic);

export default store;
