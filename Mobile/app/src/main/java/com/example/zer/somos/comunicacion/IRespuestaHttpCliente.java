package com.example.zer.somos.comunicacion;

import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public interface IRespuestaHttpCliente {
    void respuestaJSON(String accion,
                       int statusCode,
                       Header[] headers,
                       byte[] responseBytes,
                       String responseString,
                       JSONObject responseJSON,
                       JSONArray responseArrayJSON
    );

    void errorJSON(String accion,
                   int statusCode,
                   Header[] headers,
                   byte[] responseBytes,
                   String responseString,
                   JSONObject JSONResponse,
                   JSONArray JSONArrayResponse,
                   Throwable throwable
    );

    void termino();
}
