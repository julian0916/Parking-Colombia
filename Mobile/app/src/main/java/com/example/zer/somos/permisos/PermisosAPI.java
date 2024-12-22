package com.example.zer.somos.permisos;

import android.content.Context;
import android.util.Log;

import com.example.zer.somos.comunicacion.HttpCliente;
import com.example.zer.somos.comunicacion.IRespuestaHttpCliente;
import com.example.zer.somos.utilidades.Configuracion;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class PermisosAPI {

    /**
     * Consulta en la cuenta el id enviado,
     * el sistema retorna true cuando ha superado el tope
     * el sistema retorna false cuando no se ha superado el tope
     * @param context
     * @param iRespuestaHttpCliente
     * @param accion
     * @param cuenta
     * @param idPromotor
     */
    public void verificarLimiteEndeudamiento(
            Context context,
            IRespuestaHttpCliente iRespuestaHttpCliente,
            String accion,
            String cuenta,
            Long idPromotor
    ) {
        HttpCliente httpCliente = new HttpCliente();
        String URL = Configuracion.getServerOperacion() +
                "/v1/operacion/get_supera_limite_endeudamiento/" +
                cuenta +
                "/" + encodeValue(idPromotor.toString());
        httpCliente.getFromApi(
                accion,
                URL,
                context,
                Configuracion.getEncabezados(),
                iRespuestaHttpCliente);
    }

    public void solicitarZonasPerfil(
            Context context,
            IRespuestaHttpCliente iRespuestaHttpCliente,
            String accion,
            String cuenta,
            String correoPromotor
    ) {
        HttpCliente httpCliente = new HttpCliente();
        String URL = Configuracion.getServerZonasPromotor() +
                "/v1/promotorZona/zonas_promotor/" +
                cuenta +
                "?correoPromotor=" + encodeValue(correoPromotor);
        httpCliente.getFromApi(
                accion,
                URL,
                context,
                Configuracion.getEncabezados(),
                iRespuestaHttpCliente);
    }

    public void solicitarDatosTiquete(
            Context context,
            IRespuestaHttpCliente iRespuestaHttpCliente,
            String accion,
            String cuenta
    ) {
        HttpCliente httpCliente = new HttpCliente();
        String URL = Configuracion.getServerConfiguracion() +
                "/v1/configuracion/get_tiquete/" +
                cuenta;
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

    public void consultarRecaudoDiario(
            Context context,
            IRespuestaHttpCliente iRespuestaHttpCliente,
            String accion,
            String cuenta,
            Long idPromotor
    ) {
        HttpCliente httpCliente = new HttpCliente();
        String URL = Configuracion.getServerOperacion() +
                "/v1/operacion/obtener_total_recaudado/" +
                cuenta + "/" + idPromotor;
        httpCliente.getFromApi(
                accion,
                URL,
                context,
                Configuracion.getEncabezados(),
                iRespuestaHttpCliente);
    }

    public void bloquearPromotor(
            Context context,
            IRespuestaHttpCliente iRespuestaHttpCliente,
            String accion,
            String cuenta,
            Long idPromotor
    ) {
        HttpCliente httpCliente = new HttpCliente();
        String URL = Configuracion.getServerOperacion() +
                "/v1/operacion/bloquear_promotor/" +
                cuenta + "/" + idPromotor;
        httpCliente.postFromApi(
                accion,
                URL,
                context,
                Configuracion.getEncabezados(),
                new JSONObject(),
                iRespuestaHttpCliente);
    }

    public void estaBloqueado(
            Context context,
            IRespuestaHttpCliente iRespuestaHttpCliente,
            String accion,
            String cuenta,
            Long idPromotor
    ) {
        HttpCliente httpCliente = new HttpCliente();
        String URL = Configuracion.getServerOperacion() +
                "/v1/operacion/esta_bloqueado/" +
                cuenta + "/" + idPromotor;

        httpCliente.getFromApi(
                accion,
                URL,
                context,
                Configuracion.getEncabezados(),
                iRespuestaHttpCliente);
    }

    public static class Acciones {
        public final static String SOLICITAR_ZONAS_PROMOTOR = "SOLICITAR_ZONAS_PROMOTOR";
        public final static String SOLICITAR_DATOS_TIQUETE = "SOLICITAR_DATOS_TIQUETE";
        public final static String VERIFICAR_TOPE_ENDEUDAMIENTO = "VERIFICAR_TOPE_ENDEUDAMIENTO";
        public final static String CONSULTAR_RECAUDO_DIARIO = "CONSULTAR_RECAUDO_DIARIO";
        public final static String BLOQUEAR_PROMOTOR = "BLOQUEAR_PROMOTOR";
        public final static String ESTA_BLOQUEADO = "ESTA_BLOQUEADO";
    }
}
