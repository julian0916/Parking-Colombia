package com.example.zer.somos.autenticar;

import android.content.Context;
import com.example.zer.somos.comunicacion.HttpCliente;
import com.example.zer.somos.comunicacion.IRespuestaHttpCliente;
import com.example.zer.somos.utilidades.Configuracion;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class AutenticarAPI {

    public void ingresoInicioSolicitud(
            Context context,
            IRespuestaHttpCliente iRespuestaHttpCliente,
            String accion) {
        HttpCliente httpCliente = new HttpCliente();
        String URL = Configuracion.getServerAutenticar() + "/v1/acceso/authDiff";
        httpCliente.getFromApi(
                accion,
                URL,
                context,
                new HashMap<>(),//Configuracion.getEncabezados(),
                iRespuestaHttpCliente);
    }

    public void ingresoCompletarSolicitud(
            Context context,
            IRespuestaHttpCliente iRespuestaHttpCliente,
            String accion,
            String idSolicitud,
            String A,
            String contentAuth) {
        HttpCliente httpCliente = new HttpCliente();
        String URL = Configuracion.getServerAutenticar() +
                "/v1/acceso/auth?" +
                "idSolicitud=" + idSolicitud +
                "&A=" + A +
                "&contentAuth=" + contentAuth;
        httpCliente.getFromApi(
                accion,
                URL,
                context,
                Configuracion.getEncabezados(),
                iRespuestaHttpCliente);
    }

    private String encodeValue(String value) {
        String data = "";
        try {
            data = URLEncoder.encode(value, StandardCharsets.UTF_8.displayName());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static class Acciones {
        public final static String INICIO_SOLICITUD = "INICIO_SOLICITUD";
        public final static String COMPLETAR_SOLICITUD = "COMPLETAR_SOLICITUD";
    }
}
