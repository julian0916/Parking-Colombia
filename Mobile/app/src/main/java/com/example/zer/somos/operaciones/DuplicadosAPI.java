package com.example.zer.somos.operaciones;

import android.content.Context;
import com.example.zer.somos.comunicacion.HttpCliente;
import com.example.zer.somos.comunicacion.IRespuestaHttpCliente;
import com.example.zer.somos.utilidades.Configuracion;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class DuplicadosAPI {

    public void solicitarMovimientosPlacaHoy(
            Context contexto,
            IRespuestaHttpCliente iRespuestaHttpCliente,
            String accion,
            String cuenta,
            String placa
    ) {
        HttpCliente httpCliente = new HttpCliente();
        String URL = Configuracion.getServerOperacion() +
                "/v1/operacion/ultimos_movimientos_hoy/" +
                cuenta +
                "?placa=" + encodeValue(placa);
        httpCliente.getFromApi(
                accion,
                URL,
                contexto,
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
        public final static String SOLICITAR_MOVIMIENTOS_PLACA = "SOLICITAR_MOVIMIENTOS_PLACA";
    }
}
