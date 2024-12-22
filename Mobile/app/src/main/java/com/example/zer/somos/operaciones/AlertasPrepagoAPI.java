package com.example.zer.somos.operaciones;

import android.content.Context;
import com.example.zer.somos.comunicacion.HttpCliente;
import com.example.zer.somos.comunicacion.IRespuestaHttpCliente;
import com.example.zer.somos.utilidades.Configuracion;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AlertasPrepagoAPI {

    public void salioPrepagoActualizar(
            Context contexto,
            IRespuestaHttpCliente iRespuestaHttpCliente,
            String accion,
            String cuenta,
            Long idPago
    ) {
        HttpCliente httpCliente = new HttpCliente();
        String URL = Configuracion.getServerOperacion() +
                "/v1/operacion/salio_prepago/" +
                cuenta +
                "?idPago=" + idPago;
        httpCliente.puttFromApi(
                accion,
                URL,
                contexto,
                Configuracion.getEncabezados(),
                new JSONObject(),
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
        public final static String ACTUALIZAR_CUPO_DISPONIBLE_PREPAGO = "ACTUALIZAR_CUPO_DISPONIBLE_PREPAGO";
    }
}
