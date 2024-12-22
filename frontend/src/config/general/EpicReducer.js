import { combineEpics } from "redux-observable";

import {
  grabarCuenta,
  listarCuenta,
  editarCuenta,
  listarCuentaUsuario,
} from "conten/cuentas/CuentaEpic";

import {
  ingresoInicioSolicitud,
  ingresoCompletarSolicitud,
  salirSesion,
} from "conten/ingresoSistema/IngresoSistemaEpic";

import {
  listarDocumentos,
  listarPerfiles,
  guardarUsuario,
  listarUsuario,
} from "conten/usuarios/UsuarioEpic";

import { guardarZona, listarZona } from "conten/zonas/ZonaEpic";
import {
  guardarPromotorZona,
  listarPromotorZona,
} from "conten/promotorZonas/PromotorZonaEpic";

import{
  listarTiquete,
  grabarTiquete,
  grabarLimiteEndeudamientoPromotor,
  getLimiteEndeudamientoPromotor,
}from "conten/configuracion/ConfiguracionEpic";

import{
  solicitarDatosDashboardPrincipal,
  solicitarListarOcupacionZona
}from "conten/principal/PrincipalEpic";

import{
  solicitarListarRecaudoReporte,
  solicitarGuardarRecaudoReporte,
  solicitarListarPagoMunicipio,
  solicitarGuardarPagoMunicipio,
  solicitarListarFechaPago,
}from "conten/financiera/FinancieraEpic";

import{
  listarSupervisionPregunta,
  grabarSupervisionPregunta,
  borrarSupervisionPregunta,
}from "conten/supervision/SupervisionEpic";

import{
  hacerPagoExtemporaneoWEB,
  informeSupervision,
  informeSaldoPromotor,
  informeMensual,
  informeVehiculo,
  informeCartera,
  informeHistorico,
}from "conten/informes/InformesEpic";

export const rootEpic = combineEpics(
  grabarCuenta,
  listarCuenta,
  editarCuenta,
  listarCuentaUsuario,

  ingresoInicioSolicitud,
  ingresoCompletarSolicitud,
  salirSesion,
  
  listarDocumentos,
  listarPerfiles,
  guardarUsuario,
  listarUsuario,

  guardarZona,
  listarZona,

  guardarPromotorZona,
  listarPromotorZona,

  listarTiquete,
  grabarTiquete,
  grabarLimiteEndeudamientoPromotor,
  getLimiteEndeudamientoPromotor,

  solicitarDatosDashboardPrincipal,
  solicitarListarOcupacionZona,

  solicitarListarRecaudoReporte,
  solicitarGuardarRecaudoReporte,
  solicitarListarPagoMunicipio,
  solicitarGuardarPagoMunicipio,
  solicitarListarFechaPago,
  
  listarSupervisionPregunta,
  grabarSupervisionPregunta,
  borrarSupervisionPregunta,

  hacerPagoExtemporaneoWEB,
  informeSupervision,
  informeSaldoPromotor,
  informeMensual,
  informeVehiculo,
  informeCartera,
  informeHistorico,
);
