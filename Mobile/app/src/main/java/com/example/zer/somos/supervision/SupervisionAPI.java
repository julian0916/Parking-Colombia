package com.example.zer.somos.supervision;

import android.content.Context;
import android.util.Log;

import com.example.zer.somos.comunicacion.HttpCliente;
import com.example.zer.somos.comunicacion.IRespuestaHttpCliente;
import com.example.zer.somos.utilidades.Configuracion;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SupervisionAPI {

    public void solicitarPreguntasSupervision(
            Context context,
            IRespuestaHttpCliente iRespuestaHttpCliente,
            String accion,
            String cuenta
    ) {
        HttpCliente httpCliente = new HttpCliente();
        String URL = Configuracion.getServerSupervision() +
                "/v1/supervision/get_preguntas/" +
                cuenta;
        httpCliente.getFromApi(
                accion,
                URL,
                context,
                Configuracion.getEncabezados(),
                iRespuestaHttpCliente);
    }

    public void solicitarPromotoresZona(
            Context context,
            IRespuestaHttpCliente iRespuestaHttpCliente,
            String accion,
            String cuenta,
            Long idZona
    ) {
        HttpCliente httpCliente = new HttpCliente();
        String URL = Configuracion.getServerZonasPromotor() +
                "/v1/promotorZona/promotores_en_zona/" +
                cuenta +
                "?zona=" + encodeValue(idZona + "");
        httpCliente.getFromApi(
                accion,
                URL,
                context,
                Configuracion.getEncabezados(),
                iRespuestaHttpCliente);
    }

    public void solicitarGrabarRegistroSupervision(
            Context context,
            IRespuestaHttpCliente iRespuestaHttpCliente,
            String accion,
            String cuenta,
            JSONObject registroSupervision
    ) {
        HttpCliente httpCliente = new HttpCliente();
        String URL = Configuracion.getServerSupervision() +
                "/v1/supervision/registro/" +
                cuenta;
        httpCliente.postFromApi(
                accion,
                URL,
                context,
                Configuracion.getEncabezados(),
                registroSupervision,
                iRespuestaHttpCliente);
    }

    public void desbloquearPromotor(
            Context context,
            IRespuestaHttpCliente iRespuestaHttpCliente,
            String accion,
            String cuenta,
            Long idPromotor,
            Long idSupervisor
    ) {
        HttpCliente httpCliente = new HttpCliente();
        String URL = Configuracion.getServerOperacion() +
                "/v1/operacion/desbloquear_promotor/" +
                cuenta + "/" + idPromotor + "/" + idSupervisor;
        Log.d("URL", "Url: " + URL);
        httpCliente.puttFromApi(
                accion,
                URL,
                context,
                Configuracion.getEncabezados(),
                new JSONObject(),
                iRespuestaHttpCliente);
    }
    public void listarBloqueados(
            Context context,
            IRespuestaHttpCliente iRespuestaHttpCliente,
            String accion,
            String cuenta
    ) {
        HttpCliente httpCliente = new HttpCliente();
        String URL = Configuracion.getServerOperacion() +
                "/v1/operacion/listar_bloqueados/" + cuenta;
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
        public final static String SOLICITAR_PROMOTORES_EN_ZONA = "SOLICITAR_PROMOTORES_EN_ZONA";
        public final static String SOLICITAR_PREGUNTAS_SUPERVISION = "SOLICITAR_PREGUNTAS_SUPERVISION";
        public final static String GRABAR_REGISTRO_SUPERVISION = "GRABAR_REGISTRO_SUPERVISION";
        public final static String DESBLOQUEAR_PROMOTOR = "DESBLOQUEAR_PROMOTOR";
        public final static String LISTAR_BLOQUEADOS = "LISTAR_BLOQUEADOS";

    }
}
