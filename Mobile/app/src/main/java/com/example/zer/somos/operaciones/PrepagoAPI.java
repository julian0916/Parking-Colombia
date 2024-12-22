package com.example.zer.somos.operaciones;

import android.content.Context;
import com.example.zer.somos.comunicacion.HttpCliente;
import com.example.zer.somos.comunicacion.IRespuestaHttpCliente;
import com.example.zer.somos.utilidades.Configuracion;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class PrepagoAPI {

    public void ingresarPrepago(
            Context contexto,
            IRespuestaHttpCliente iRespuestaHttpCliente,
            String accion,
            String cuenta,
            JSONObject datosPrepago
    ) {
        HttpCliente httpCliente = new HttpCliente();
        String URL = Configuracion.getServerOperacion() +
                "/v1/operacion/prepago/" +
                cuenta;
        httpCliente.postFromApi(
                accion,
                URL,
                contexto,
                Configuracion.getEncabezados(),
                datosPrepago,
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
        public final static String GRABAR_PREPAGO = "GRABAR_PREPAGO";
    }
}
