package com.example.zer.somos.permisos;

import com.example.zer.somos.comunes.DatosSesion;
import com.example.zer.somos.operaciones.DatosRegistro;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class GlobalPermisos {

    private static JSONArray cuentasJSONArray;
    private static JSONArray zonasJSONArray;
    private static List<JSONObject> listadoCuentasValidas = null;
    private static List<String> listaNombresCuentas = null;
    private static List<JSONObject> listadoZonas = null;
    private static List<String> listaNombresZonas = null;
    private static List<DatosRegistro> listaAlertas = null;

    private static DatosSesion datosSesionActual = null;

    public static JSONArray getCuentasJSONArray() {
        return cuentasJSONArray;
    }

    public static void setCuentasJSONArray(JSONArray cuentasJSONArrayV) {
        cuentasJSONArray = cuentasJSONArrayV;
    }

    public static List<JSONObject> getListadoCuentasValidas() {
        return listadoCuentasValidas;
    }

    public static void setListadoCuentasValidas(List<JSONObject> listadoCuentasValidas) {
        GlobalPermisos.listadoCuentasValidas = listadoCuentasValidas;
    }

    public static List<String> getListaNombresCuentas() {
        return listaNombresCuentas;
    }

    public static void setListaNombresCuentas(List<String> listaNombresCuentas) {
        GlobalPermisos.listaNombresCuentas = listaNombresCuentas;
    }


    public static DatosSesion getDatosSesionActual() {
        return datosSesionActual;
    }

    public static void setDatosSesionActual(DatosSesion datosSesionActual) {
        GlobalPermisos.datosSesionActual = datosSesionActual;
    }

    public static JSONArray getZonasJSONArray() {
        return zonasJSONArray;
    }

    public static void setZonasJSONArray(JSONArray zonasJSONArray) {
        GlobalPermisos.zonasJSONArray = zonasJSONArray;
    }

    public static List<JSONObject> getListadoZonas() {
        return listadoZonas;
    }

    public static void setListadoZonas(List<JSONObject> listadoZonas) {
        GlobalPermisos.listadoZonas = listadoZonas;
    }

    public static List<String> getListaNombresZonas() {
        return listaNombresZonas;
    }

    public static void setListaNombresZonas(List<String> listaNombresZonas) {
        GlobalPermisos.listaNombresZonas = listaNombresZonas;
    }

    public static List<DatosRegistro> getListaAlertas() {
        return listaAlertas;
    }

    public static void setListaAlertas(List<DatosRegistro> listaAlertas) {
        GlobalPermisos.listaAlertas = listaAlertas;
    }


    public static void limpiarTodo() {
        GlobalPermisos.cuentasJSONArray = null;
        GlobalPermisos.listadoCuentasValidas = null;
        GlobalPermisos.listaNombresCuentas = null;
        GlobalPermisos.datosSesionActual = null;
        GlobalPermisos.listaAlertas = null;
    }

    public class Perfiles {
        public static final long PROMOTOR = 105l;
        public static final long SUPERVISOR = 102l;
    }
}
