package com.example.zer.somos.comunicacion;

import android.content.Context;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpCliente {
    private final int TIME_OUT_CONNECTION_MILIS = 30000;
    private final int TIME_OUT_MILIS = 30000;
    private final int NUMBER_OF_RETRIES = 2;
    private final int TIME_OUT_MILIS_BETWEEN_RETIRES = 1;
    private AsyncHttpClient client;

    public HttpCliente() {
        try {
            this.client = new AsyncHttpClient();
            this.client.setTimeout(TIME_OUT_MILIS);
            this.client.setConnectTimeout(TIME_OUT_CONNECTION_MILIS);
            this.client.setMaxRetriesAndTimeout(NUMBER_OF_RETRIES, TIME_OUT_MILIS_BETWEEN_RETIRES);
            this.client.setMaxConnections(6);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void validateHeader(Map<String, String> encabezados) {
        try {
            for (Map.Entry<String, String> data : encabezados.entrySet()) {
                try {
                    client.addHeader(data.getKey(), data.getValue());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Header[] getHeaders(Map<String, String> encabezados) {
        List<Header> headers = new ArrayList<>();
        Header[] resp = null;
        try {
            for (Map.Entry<String, String> data : encabezados.entrySet()) {
                try {
                    Header header = new BasicHeader(data.getKey(), data.getValue());
                    headers.add(header);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            resp = headers.toArray(new Header[0]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resp;
    }

    private StringEntity getHttpEntity(JSONObject jsonParams) {
        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonParams.toString());
        } catch (Exception ex) {
        }
        return entity;
    }

    //-----------------------------------basic_request----------------------------------------------
    private void get(
            final String url,
            final Map<String, String> encabezados,
            final JsonHttpResponseHandler responseHandler) {
        try {
            validateHeader(encabezados);
            client.get(url, responseHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void post(String url,
                      Context contexto,
                      JSONObject jsonParams,
                      final Map<String, String> encabezados,
                      JsonHttpResponseHandler responseHandler) {
        try {
            StringEntity stringEntity = getHttpEntity(jsonParams);
            stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING, "UTF-8"));
            stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json; charset=UTF-8"));
            responseHandler.setCharset("UTF-8");
            client.post(contexto, url, getHeaders(encabezados), stringEntity, null, responseHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void put(String url,
                     Context contexto,
                     JSONObject jsonParams,
                     final Map<String, String> encabezados,
                     JsonHttpResponseHandler responseHandler) {
        try {
            client.put(contexto, url, getHeaders(encabezados), getHttpEntity(jsonParams), "application/json", responseHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void delete(
            final String url,
            final Map<String, String> encabezados,
            final JsonHttpResponseHandler responseHandler) {
        try {
            validateHeader(encabezados);
            client.delete(url, responseHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //---------------------------------END_basic_request--------------------------------------------
    //---------------------------------Personalizadas
    public void getFromApi(
            final String accion,
            final String url,
            final Context context,
            Map<String, String> encabezados,
            final IRespuestaHttpCliente iRespuestaHttpCliente) {
        try {
            get(url, encabezados, new JsonHttpResponseHandler() {

                @Override
                public void onFinish() {
                    try {
                        iRespuestaHttpCliente.termino();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onSuccess(int statusCode,
                                      Header[] headers,
                                      String responseString) {
                    try {
                        iRespuestaHttpCliente.respuestaJSON(
                                accion,
                                statusCode,
                                headers,
                                null,
                                responseString,
                                null,
                                null);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onSuccess(int statusCode,
                                      Header[] headers,
                                      JSONObject response) {
                    try {
                        iRespuestaHttpCliente.respuestaJSON(
                                accion,
                                statusCode,
                                headers,
                                null,
                                null,
                                response,
                                null);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onSuccess(int statusCode,
                                      Header[] headers,
                                      JSONArray responseArrayJSON) {
                    try {
                        iRespuestaHttpCliente.respuestaJSON(
                                accion,
                                statusCode,
                                headers,
                                null,
                                null,
                                null,
                                responseArrayJSON);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(
                        int statusCode,
                        Header[] headers,
                        Throwable throwable,
                        JSONObject errorResponse) {
                    try {
                        iRespuestaHttpCliente.errorJSON(
                                accion,
                                statusCode,
                                headers,
                                null,
                                null,
                                errorResponse,
                                null,
                                throwable);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode,
                                      Header[] headers,
                                      String responseString,
                                      Throwable throwable) {
                    try {
                        iRespuestaHttpCliente.errorJSON(
                                accion,
                                statusCode,
                                headers,
                                null,
                                responseString,
                                null,
                                null,
                                throwable);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode,
                                      Header[] headers,
                                      Throwable throwable,
                                      JSONArray errorResponse) {
                    try {
                        iRespuestaHttpCliente.errorJSON(
                                accion,
                                statusCode,
                                headers,
                                null,
                                null,
                                null,
                                errorResponse,
                                throwable);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //----------
    public void postFromApi(
            final String accion,
            final String url,
            final Context contexto,
            Map<String, String> encabezados,
            JSONObject data,
            final IRespuestaHttpCliente iRespuestaHttpCliente) {
        try {
            post(url,
                    contexto,
                    data,
                    encabezados,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onFinish() {
                            try {
                                iRespuestaHttpCliente.termino();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                                              Header[] headers,
                                              String responseString) {
                            try {
                                iRespuestaHttpCliente.respuestaJSON(
                                        accion,
                                        statusCode,
                                        headers,
                                        null,
                                        responseString,
                                        null,
                                        null);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                                              Header[] headers,
                                              JSONObject response) {
                            try {
                                iRespuestaHttpCliente.respuestaJSON(
                                        accion,
                                        statusCode,
                                        headers,
                                        null,
                                        null,
                                        response,
                                        null);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                                              Header[] headers,
                                              JSONArray responseArrayJSON) {
                            try {
                                iRespuestaHttpCliente.respuestaJSON(
                                        accion,
                                        statusCode,
                                        headers,
                                        null,
                                        null,
                                        null,
                                        responseArrayJSON);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(
                                int statusCode,
                                Header[] headers,
                                Throwable throwable,
                                JSONObject errorResponse) {
                            try {
                                iRespuestaHttpCliente.errorJSON(
                                        accion,
                                        statusCode,
                                        headers,
                                        null,
                                        null,
                                        errorResponse,
                                        null,
                                        throwable);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode,
                                              Header[] headers,
                                              String responseString,
                                              Throwable throwable) {
                            try {
                                iRespuestaHttpCliente.errorJSON(
                                        accion,
                                        statusCode,
                                        headers,
                                        null,
                                        responseString,
                                        null,
                                        null,
                                        throwable);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode,
                                              Header[] headers,
                                              Throwable throwable,
                                              JSONArray errorResponse) {
                            try {
                                iRespuestaHttpCliente.errorJSON(
                                        accion,
                                        statusCode,
                                        headers,
                                        null,
                                        null,
                                        null,
                                        errorResponse,
                                        throwable);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void puttFromApi(
            final String accion,
            final String url,
            final Context contexto,
            Map<String, String> encabezados,
            JSONObject data,
            final IRespuestaHttpCliente iRespuestaHttpCliente) {
        try {
            put(url,
                    contexto,
                    data,
                    encabezados,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onFinish() {
                            try {
                                iRespuestaHttpCliente.termino();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                                              Header[] headers,
                                              String responseString) {
                            try {
                                iRespuestaHttpCliente.respuestaJSON(
                                        accion,
                                        statusCode,
                                        headers,
                                        null,
                                        responseString,
                                        null,
                                        null);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                                              Header[] headers,
                                              JSONObject response) {
                            try {
                                iRespuestaHttpCliente.respuestaJSON(
                                        accion,
                                        statusCode,
                                        headers,
                                        null,
                                        null,
                                        response,
                                        null);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                                              Header[] headers,
                                              JSONArray responseArrayJSON) {
                            try {
                                iRespuestaHttpCliente.respuestaJSON(
                                        accion,
                                        statusCode,
                                        headers,
                                        null,
                                        null,
                                        null,
                                        responseArrayJSON);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(
                                int statusCode,
                                Header[] headers,
                                Throwable throwable,
                                JSONObject errorResponse) {
                            try {
                                iRespuestaHttpCliente.errorJSON(
                                        accion,
                                        statusCode,
                                        headers,
                                        null,
                                        null,
                                        errorResponse,
                                        null,
                                        throwable);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode,
                                              Header[] headers,
                                              String responseString,
                                              Throwable throwable) {
                            try {
                                iRespuestaHttpCliente.errorJSON(
                                        accion,
                                        statusCode,
                                        headers,
                                        null,
                                        responseString,
                                        null,
                                        null,
                                        throwable);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode,
                                              Header[] headers,
                                              Throwable throwable,
                                              JSONArray errorResponse) {
                            try {
                                iRespuestaHttpCliente.errorJSON(
                                        accion,
                                        statusCode,
                                        headers,
                                        null,
                                        null,
                                        null,
                                        errorResponse,
                                        throwable);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*private AsyncHttpClient client;
    private Alerts alerts;
    private DataBase dataBase;

    public HttpCliente(GlobalState globalState, Context context) {
        this.client = new AsyncHttpClient();
        this.alerts = globalState.getAlerts();
        this.dataBase = globalState.getDataBase();

        if (alerts == null) {
            alerts = new Alerts();
            globalState.setAlerts(alerts);
        }

        if (dataBase == null) {
            dataBase = Room.databaseBuilder(
                    context,
                    DataBase.class,
                    context.getString(R.string.data_base_name)
            ).allowMainThreadQueries().build();

            globalState.setDataBase(dataBase);
        }

    }

    private String getAbsoluteUrl(String relativeUrl) {

        Setting endPoint;
        switch (relativeUrl) {
            case Url.LOGIN:
                endPoint = dataBase.getSettingDao().getSetting("end_point_auth");
                break;
            default:
                endPoint = dataBase.getSettingDao().getSetting("end_point");
                break;
        }

        if (endPoint != null) {
            return endPoint.getValue() + relativeUrl;
        } else {
            return "";
        }

    }

    private void validateHeader(String[] headersKey, String[] headersIn) {
        if (headersKey != null) {
            for (int i = 0; i < headersIn.length; i++) {
                client.addHeader(headersKey[i], headersIn[i]);
            }
        } else if (headersIn != null) {
            client.setBasicAuth(headersIn[0], "");
        }
    }


    //-----------------------------------basic_request----------------------------------------------
    private void get(
            final String url,
            final String[] headersKey,
            final String[] headersIn,
            final JsonHttpResponseHandler responseHandler) {

        validateHeader(headersKey, headersIn);
        client.get(getAbsoluteUrl(url), responseHandler);
    }


    private void post(String url,
                      RequestParams params,
                      String[] headersKey,
                      String[] headersIn,
                      JsonHttpResponseHandler responseHandler) {

        validateHeader(headersKey, headersIn);
        params.setUseJsonStreamer(true);
        client.post(getAbsoluteUrl(url), params, responseHandler);

    }

    private void put(String url,
                     RequestParams params,
                     String[] headersKey,
                     String[] headersIn,
                     JsonHttpResponseHandler responseHandler) {

        if (headersKey != null) {
            for (int i = 0; i < headersIn.length; i++) {
                client.addHeader(headersKey[i], headersIn[i]);
            }
        } else if (headersIn != null) {
            client.setBasicAuth(headersIn[0], "");
        }

        params.setUseJsonStreamer(true);

        client.put(getAbsoluteUrl(url), params, responseHandler);

    }

    private void delete(
            final String url,
            final String[] headersKey,
            final String[] headersIn,
            final JsonHttpResponseHandler responseHandler) {

        if (headersKey != null) {
            for (int i = 0; i < headersIn.length; i++) {
                client.addHeader(headersKey[i], headersIn[i]);
            }
        } else if (headersIn != null) {

            String password = "";

            if (headersIn.length > 1) {
                password = headersIn[1];
            }

            client.setBasicAuth(headersIn[0], password);
        }

        client.delete(getAbsoluteUrl(url), responseHandler);
    }
    //---------------------------------END_basic_request--------------------------------------------


    //-----------------------------------custom_request---------------------------------------------
    public void getFromApi(
            final String relativeUrl,
            String data,
            final Context context,
            String[] headersKey,
            final String[] headersIn,
            final IRespuestaHttpCliente callback) {

        String url = relativeUrl;
        if (!data.equals("")) {
            url = url + "/" + data;
        }

        get(url, headersKey, headersIn, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode,
                                  Header[] headers,
                                  JSONObject response) {
                if (statusCode == 200 || statusCode == 201) {

                    try {
                        JSONObject apiResponse = response.getJSONObject("apiResponse");
                        if (apiResponse.getInt("code") == 200) {
                            callback.onJSONResponse(response);
                        } else {
                            manageError(apiResponse, context, callback, null, relativeUrl);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(
                    int statusCode,
                    Header[] headers,
                    Throwable throwable,
                    JSONObject errorResponse) {

                super.onFailure(statusCode, headers, throwable, errorResponse);

                manageError(errorResponse, context, callback, null, relativeUrl);
            }
        });
    }

    public void postFromApi(
            final String relativeUrl,
            String data,
            final RequestParams params,
            final String[] headersKey,
            final String[] headersIn,
            final Context context,
            final Dialog dialog,
            final IRespuestaHttpCliente callback) {

        String url = relativeUrl;
        if (!data.equals("")) {
            url = url + "/" + data;
        }

        post(url, params, headersKey, headersIn, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200 || statusCode == 201) {

                    try {
                        JSONObject apiResponse = response.getJSONObject("apiResponse");
                        if (apiResponse.getInt("code") == 200) {
                            callback.onJSONResponse(response);
                        } else {
                            manageError(apiResponse, context, callback, dialog, relativeUrl);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(
                    int statusCode,
                    Header[] headers,
                    Throwable throwable,
                    JSONObject errorResponse) {

                super.onFailure(statusCode, headers, throwable, errorResponse);

                manageError(errorResponse, context, callback, dialog, relativeUrl);
            }
        });
    }


    public void putFromApi(
            final String relativeUrl,
            String data,
            final RequestParams params,
            final String[] headersKey,
            final String[] headersIn,
            final Context context,
            final IRespuestaHttpCliente callback) {

        String url = relativeUrl;
        if (!data.equals("")) {
            url = url + "/" + data;
        }

        put(
                url,
                params,
                headersKey,
                headersIn,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (statusCode == 200 || statusCode == 201) {
                            try {
                                JSONObject apiResponse = response.getJSONObject("apiResponse");
                                if (apiResponse.getInt("code") == 200) {
                                    callback.onJSONResponse(response);
                                }
//                            else {
//                                manageError(apiResponse, context, callback);
//                            }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(
                            int statusCode,
                            Header[] headers,
                            Throwable throwable,
                            JSONObject errorResponse) {

                        super.onFailure(statusCode, headers, throwable, errorResponse);

                        manageError(errorResponse, context, callback, null, relativeUrl);
                    }
                }
        );
    }

    public void deleteFromApi(
            final String relativeUrl,
            String data,
            String APIVersion,
            final Context context,
            String[] headersKey,
            final String[] headersIn,
            final IRespuestaHttpCliente callback) {

        String url = relativeUrl;
        if (!data.equals("")) {
            url = url + "/" + data;
        }

        delete(APIVersion + url, headersKey, headersIn, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    callback.onJSONResponse(response);
                }
            }

            @Override
            public void onFailure(
                    int statusCode,
                    Header[] headers,
                    Throwable throwable,
                    JSONObject errorResponse) {

                super.onFailure(statusCode, headers, throwable, errorResponse);

                manageError(errorResponse, context, callback, null, relativeUrl);
            }
        });
    }
    //-----------------------------------custom_request---------------------------------------------


    //---------------------------------error_controller---------------------------------------------
    private void manageError(
            final JSONObject reponseError,
            final Context context,
            IRespuestaHttpCliente callback,
            Dialog dialog,
            String relativeUrl) {

        if (reponseError == null) {
            showConnectionFailedAlert(relativeUrl, context);
        } else {
            try {

                String title = "";
                String message = "";

                switch (reponseError.getString("message")) {
                    case "NOT_FOUND":
                        showConnectionFailedAlert(relativeUrl, context);
                        break;

                    case "Internal Server Error":
                        showConnectionFailedAlert(relativeUrl, context);
                        break;

                    default:
                        message = reponseError.getString("message");
                        break;
                }

                if (!title.equals("") || !message.equals("")) {

                    if (dialog != null) {
                        dialog.dismiss();
                    }

                    alerts.showCustomDialog(
                            context,
                            false,
                            title,
                            message,
                            context.getResources().getString(R.string.OK),
                            context.getResources().getString(R.string.CANCEL),
                            Alerts.ERROR_TYPE,
                            null
                    );
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void showConnectionFailedAlert(String relativeUrl, Context context) {
        if (!relativeUrl.equals(Url.SEND_BEACONS)) {
            alerts.showConnectionFailedAlert(context);
        }
    }
    //-------------------------------END_error_controller-------------------------------------------
*/
}
