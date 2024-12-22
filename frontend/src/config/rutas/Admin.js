/*!

=========================================================
* Material Dashboard PRO React - v1.7.0
=========================================================

* Product Page: https://www.creative-tim.com/product/material-dashboard-pro-react
* Copyright 2019 Creative Tim (https://www.creative-tim.com)

* Coded by Creative Tim

=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

*/

import BookmarkBorder from '@material-ui/icons/BookmarkBorder';
import HomeIcon from '@material-ui/icons/Home';
import BarChartIcon from '@material-ui/icons/BarChart';

import SettingsIcon from '@material-ui/icons/Settings';
import ViewHeadlineIcon from '@material-ui/icons/ViewHeadline';
import AccessibilityIcon from '@material-ui/icons/Accessibility';

import UsuarioListar from 'conten/usuarios/UsuarioListar';
import UsuarioAdministrar from 'conten/usuarios/UsuarioAdministrar';

import ZonaListar from 'conten/zonas/ZonaListar';
import ZonaAdministrar from 'conten/zonas/ZonaAdministrar';

import PromotorZonaListar from 'conten/promotorZonas/PromotorZonaListar';
import PromotorZonaAdministrar from 'conten/promotorZonas/PromotorZonaAdministrar';

import Principal from 'conten/principal/Principal';

import CuentaListar from 'conten/cuentas/CuentaListar';
import CuentaAdministrar from 'conten/cuentas/CuentaAdministrar';

import ConfiguracionAdministrar from 'conten/configuracion/ConfiguracionAdministrar';

import RecaudoListar from 'conten/financiera/RecaudoListar';

import MesaDeAyuda from 'conten/mesaDeAyuda/MesaDeAyuda';

import SupervisionPreguntas from 'conten/supervision/SupervisionPreguntas';

import InformeSupervision from 'conten/informes/InformeSupervision';

import InformeSaldosPromotores from 'conten/informes/InformeSaldosPromotores';

import InformeMensual from 'conten/informes/InformeMensual';

import InformeVehiculo from 'conten/informes/InformeVehiculo';

import InformeCartera from 'conten/informes/InformeCartera';

import InformeHistorico from 'conten/informes/InformeHistorico';

import { ID_PERFIL } from 'config/general/Configuracion';

export const routes = () => {
  return filterByProfile(getRutasPermisos());
};

const filterByProfile = (content) => {
  var filterContent = [];
  content.forEach((element) => {
    if (
      element.permition.indexOf(ALL) > -1 ||
      element.permition.indexOf(sessionStorage[ID_PERFIL]) > -1
    ) {
      if (element.views !== undefined) {
        element.views = filterByProfile(element.views);
      }
      filterContent.push(element);
    }
  });
  return filterContent;
};

const ALL = '*',
  CREADOR_CUENTA = '100',
  ADMINISTRADOR = '101',
  SUPERVISOR = '102',
  VISOR = '103',
  MUNICIPIO = '104',
  PROMOTOR = '105',
  AUXILIAR = '106';

export const getRutasPermisos = () => {
  return [
    {
      path: '/principal',
      name: 'Principal',
      icon: HomeIcon,
      component: Principal,
      layout: '/admin',
      permition: [ADMINISTRADOR, MUNICIPIO, VISOR],
    },
    {
      collapse: true,
      name: 'Financiera',
      icon: BarChartIcon,
      state: 'financieraCollapse',
      permition: [ADMINISTRADOR, AUXILIAR, MUNICIPIO, VISOR],
      views: [
        {
          path: '/recaudo',
          name: 'Recaudo',
          mini: 'RE',
          component: RecaudoListar,
          layout: '/admin',
          permition: [ADMINISTRADOR, AUXILIAR, MUNICIPIO, VISOR],
        },
      ],
    },
    {
      collapse: true,
      name: 'Administración',
      icon: SettingsIcon,
      state: 'administracionCollapse',
      permition: [
        ADMINISTRADOR,
        CREADOR_CUENTA,
        PROMOTOR,
        SUPERVISOR,
        MUNICIPIO,
      ],
      views: [
        {
          path: '/cuentas',
          name: 'Cuentas',
          mini: 'CU',
          component: CuentaListar,
          layout: '/admin',
          permition: [CREADOR_CUENTA],
        },
        {
          path: '/usuarios',
          name: 'Usuarios',
          mini: 'US',
          component: UsuarioListar,
          layout: '/admin',
          permition: [ADMINISTRADOR, CREADOR_CUENTA, MUNICIPIO, VISOR],
        },
        {
          path: '/zonas',
          name: 'Zonas',
          mini: 'ZO',
          component: ZonaListar,
          layout: '/admin',
          permition: [ADMINISTRADOR, MUNICIPIO, VISOR],
        },
        {
          path: '/promotores-zona',
          name: 'Promotores Zona',
          mini: 'PZ',
          component: PromotorZonaListar,
          layout: '/admin',
          permition: [ADMINISTRADOR, PROMOTOR, SUPERVISOR, MUNICIPIO, VISOR],
        },
        {
          path: '/configuracion',
          name: 'Configuración',
          mini: 'CO',
          component: ConfiguracionAdministrar,
          layout: '/admin',
          permition: [ADMINISTRADOR],
        },
        {
          path: '/listado-supervision',
          name: 'Listado Supervisión',
          mini: 'LS',
          component: SupervisionPreguntas,
          layout: '/admin',
          permition: [ADMINISTRADOR, MUNICIPIO, VISOR],
        },
      ],
    },
    {
      collapse: true,
      name: 'Informes',
      icon: ViewHeadlineIcon,
      state: 'informesCollapse',
      permition: [ADMINISTRADOR, AUXILIAR, MUNICIPIO, SUPERVISOR, VISOR],
      views: [
        {
          path: '/informe-supervision',
          name: 'Informe supervisión',
          mini: 'IS',
          component: InformeSupervision,
          layout: '/admin',
          permition: [ADMINISTRADOR, MUNICIPIO],
        },
        {
          path: '/informe-saldos-promotores',
          name: 'Informe saldo promotor',
          mini: 'ISP',
          component: InformeSaldosPromotores,
          layout: '/admin',
          permition: [ADMINISTRADOR, AUXILIAR, MUNICIPIO],
        },
        {
          path: '/informe-historico',
          name: 'Informe histórico',
          mini: 'IH',
          component: InformeHistorico,
          layout: '/admin',
          permition: [ADMINISTRADOR, MUNICIPIO, VISOR, AUXILIAR],
        },
        {
          path: '/informe-mensual',
          name: 'Informe mensual',
          mini: 'IM',
          component: InformeMensual,
          layout: '/admin',
          permition: [ADMINISTRADOR, MUNICIPIO, VISOR],
        },
        {
          path: '/informe-vehiculo',
          name: 'Informe vehículo',
          mini: 'IV',
          component: InformeVehiculo,
          layout: '/admin',
          permition: [ADMINISTRADOR, AUXILIAR, MUNICIPIO, SUPERVISOR, VISOR],
        },
        {
          path: '/informe-cartera',
          name: 'Informe cartera',
          mini: 'IC',
          component: InformeCartera,
          layout: '/admin',
          permition: [ADMINISTRADOR, AUXILIAR, MUNICIPIO, VISOR],
        },
      ],
    },
    {
      path: '/mesa-de-ayuda',
      name: 'Mesa de ayuda',
      icon: AccessibilityIcon,
      component: MesaDeAyuda,
      layout: '/admin',
      permition: [
        CREADOR_CUENTA,
        ADMINISTRADOR,
        AUXILIAR,
        MUNICIPIO,
        SUPERVISOR,
      ],
    },

    //Redirects
    {
      path: '/administrar-cuenta',
      name: 'Nueva cuenta',
      icon: BookmarkBorder,
      component: CuentaAdministrar,
      layout: '/admin',
      redirect: true,
      permition: [CREADOR_CUENTA],
    },
    {
      path: '/administrar-usuario',
      name: 'Nuevo usuario',
      icon: BookmarkBorder,
      component: UsuarioAdministrar,
      layout: '/admin',
      redirect: true,
      permition: [CREADOR_CUENTA, ADMINISTRADOR, MUNICIPIO],
    },
    {
      path: '/administrar-zona',
      name: 'Nueva zona',
      icon: BookmarkBorder,
      component: ZonaAdministrar,
      layout: '/admin',
      redirect: true,
      permition: [CREADOR_CUENTA, ADMINISTRADOR],
    },
    {
      path: '/administrar-promotor-zona',
      name: 'Nuevo promotor zona',
      icon: BookmarkBorder,
      component: PromotorZonaAdministrar,
      layout: '/admin',
      redirect: true,
      permition: [CREADOR_CUENTA, ADMINISTRADOR, SUPERVISOR, MUNICIPIO],
    },
  ];
};
