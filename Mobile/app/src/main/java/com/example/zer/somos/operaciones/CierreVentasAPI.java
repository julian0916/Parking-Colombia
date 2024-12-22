package com.example.zer.somos.operaciones;

import android.content.Context;
import com.example.zer.somos.comunicacion.HttpCliente;
import com.example.zer.somos.comunicacion.IRespuestaHttpCliente;
import com.example.zer.somos.utilidades.Configuracion;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CierreVentasAPI {

    public void solicitarCarteraPlaca(
            Context contexto,
            IRespuestaHttpCliente iRespuestaHttpCliente,
            String accion,
            String cuenta,
            String placa
    ) {
        HttpCliente httpCliente = new HttpCliente();
        String URL = Configuracion.getServerOperacion() +
                "/v1/operacion/cartera_placa/" +
                cuenta +
                "?placa=" + encodeValue(placa);
        httpCliente.getFromApi(
                accion,
                URL,
                contexto,
                Configuracion.getEncabezados(),
                iRespuestaHttpCliente);
    }

    public void solicitarCarteraZona(
            Context contexto,
            IRespuestaHttpCliente iRespuestaHttpCliente,
            String accion,
            String cuenta,
            Long zona
    ) {
        HttpCliente httpCliente = new HttpCliente();
        String URL = Configuracion.getServerOperacion() +
                "/v1/operacion/preliquidar_zona/" +
                cuenta +
                "?zona=" + zona;
        httpCliente.getFromApi(
                accion,
                URL,
                contexto,
                Configuracion.getEncabezados(),
                iRespuestaHttpCliente);
    }

    public void confirmarPagoPlaca(
            Context contexto,
            IRespuestaHttpCliente iRespuestaHttpCliente,
            String accion,
            String cuenta,
            Long idPago,
            String placa,
            String fhliquidacion,
            Long promotor
    ) {
        HttpCliente httpCliente = new HttpCliente();
        String URL = Configuracion.getServerOperacion() +
                "/v1/operacion/confirmar/" +
                cuenta +
                "?idPago=" + idPago +
                "&placa=" + encodeValue(placa) +
                "&fhliquidacion=" + encodeValue(fhliquidacion) +
                "&promotor=" + promotor;
        httpCliente.puttFromApi(
                accion,
                URL,
                contexto,
                Configuracion.getEncabezados(),
                new JSONObject(),
                iRespuestaHttpCliente);
    }

    public void hacerPagoExtemporaneo(
            Context contexto,
            IRespuestaHttpCliente iRespuestaHttpCliente,
            String accion,
            String cuenta,
            Long idPago,
            String placa,
            Long promotor
    ) {
        HttpCliente httpCliente = new HttpCliente();
        String URL = Configuracion.getServerOperacion() +
                "/v1/operacion/extemporaneo/" +
                cuenta +
                "?idPago=" + idPago +
                "&placa=" + encodeValue(placa) +
                "&promotor=" + promotor;
        httpCliente.puttFromApi(
                accion,
                URL,
                contexto,
                Configuracion.getEncabezados(),
                new JSONObject(),
                iRespuestaHttpCliente);
    }

    public void reportarVehiculo(
            Context contexto,
            IRespuestaHttpCliente iRespuestaHttpCliente,
            String accion,
            String cuenta,
            Long idCuenta,
            String placa,
            Long promotor
    ) {
        HttpCliente httpCliente = new HttpCliente();
        String URL = Configuracion.getServerOperacion() +
                "/v1/operacion/reportar/" +
                cuenta +
                "?idCuenta=" + idCuenta +
                "&placa=" + encodeValue(placa) +
                "&promotor=" + promotor;
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
        public final static String CARTERA_PLACA = "CARTERA_PLACA";
        public final static String CONFIRMAR_PAGO = "CONFIRMAR_PAGO";
        public final static String REPORTAR_VEHICULO = "REPORTAR_VEHICULO";
        public final static String PAGO_EXTEMPORANEO = "PAGO_EXTEMPORANEO";
        public final static String CARTERA_ZONA = "CARTERA_ZONA";
    }
}
