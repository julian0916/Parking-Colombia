package com.example.zer.somos.supervision;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;



import androidx.appcompat.app.AlertDialog;

import com.example.zer.somos.MainActivity;
import com.example.zer.somos.R;
import com.example.zer.somos.autenticar.EncodeService;
import com.example.zer.somos.comunes.ActivityBase;
import com.example.zer.somos.comunes.DatosSesion;
import com.example.zer.somos.comunes.IRespuestaConfirmar;
import com.example.zer.somos.comunes.MensajeAceptar;
import com.example.zer.somos.comunes.MensajeConfirmar;
import com.example.zer.somos.comunicacion.IRespuestaHttpCliente;

import com.example.zer.somos.permisos.GlobalPermisos;
import com.example.zer.somos.utilidades.Configuracion;
import com.example.zer.somos.utilidades.Hash;

import com.example.zer.somos.utilidades.qrCodeScanner.QRCodeScanDialogFragment;
import com.example.zer.somos.utilidades.qrCodeScanner.QRCodeScanListener;
import com.example.zer.somos.utilidades.qrCodeScanner.ScanCaptureAct;
import com.google.gson.Gson;


import cz.msebera.android.httpclient.Header;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class SupervisionActivity extends ActivityBase
        implements
        IRespuestaHttpCliente,
        IRespuestaConfirmar,
        ISupervisionPreguntas,
        QRCodeScanDialogFragment.QRCodeScanListener{

    private SupervisionAPI supervisionAPI;
    private Button salir;
    private Button grabar;
    private EditText zona;
    private EditText firma;
    private Spinner promotores;
    private EditText informacion;
    private List<PromotorZona> listadoPromotores;
    private Map<Long, Pregunta> listaChequeo;
    private ListView listado;
    private AlertDialog.Builder builderSupervision;
    private Dialog dialogSupervision;
    private MensajeConfirmar mensajeConfirmar;
    private MensajeAceptar mensajeAceptar;
    private PromotorZona promotorZona;
    private Long ultimoTiempo;
    public Button qrCodeButton;
    private static final int REQUEST_CODE = 1001;


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        try {
            ListAdapter mAdapter = listView.getAdapter();

            int totalHeight = 0;

            for (int i = 0; i < mAdapter.getCount(); i++) {
                View mView = mAdapter.getView(i, null, listView);

                mView.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));


                totalHeight += mView.getMeasuredHeight();

            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight
                    + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_supervision);
            iniciarComponentes();
            programarEventos();
            colocarDatosZona();
            recuperarPromotoresEnZona();
            recuperarPreguntasSupervision();
            String newString;
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if(extras == null) {
                    newString= null;
                } else {
                    newString= extras.getString("qrCode");
                    firma.setText(newString);
                    getValorFirma();

                }
            } else {
                newString= (String) savedInstanceState.getSerializable("qrCode");
                firma.setText(newString);
                getValorFirma();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void iniciarComponentes() {
        try {
            supervisionAPI = new SupervisionAPI();
            promotores = this.findViewById(R.id.promotores);
            zona = this.findViewById(R.id.zona);
            informacion = this.findViewById(R.id.informacion);
            firma = this.findViewById(R.id.firma);
            salir = this.findViewById(R.id.salir);
            grabar = this.findViewById(R.id.grabar);
            listado = this.findViewById(R.id.listado);
            ultimoTiempo = System.nanoTime();
            qrCodeButton =this.findViewById(R.id.qrCodeButton);

            builderSupervision = new AlertDialog.Builder(this);
            builderSupervision.setView(R.layout.progress);
            builderSupervision.setCancelable(false);
            dialogSupervision = builderSupervision.create();

            listadoPromotores = new ArrayList<>();
            listaChequeo = new HashMap<>();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void programarEventos() {
        try {
            grabar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    solicitarGrabar();
                }
            });

            salir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    salir();
                }
            });

            promotores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    cambioDePromotor(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });

            informacion.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    validarContenidoInformacion(s.toString());
                }
            });

            firma.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    controlarIngresoManualQR(s.toString(), System.nanoTime());
                }
            });
            qrCodeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scanQRCode();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void controlarIngresoManualQR(String contenido, Long tiempo) {
        try {
            if (tiempo - ultimoTiempo > Configuracion.QR.getLimiteVelocidadLectura()) {
                if (contenido.length() > Configuracion.QR.getLimiteCaracteresIngresoManualQR() && contenido.contains(Configuracion.QR.getSeparadorQR())) {
                    final Gson gson = new Gson();
                    MainActivity mainActivity = new MainActivity();
                   Configuracion.DatosQR datosQR = gson.fromJson(mainActivity.jsonQR, Configuracion.DatosQR.class);
                    firma.setText(datosQR.getC());
                }
            }
            ultimoTiempo = tiempo;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void scanQRCode() {
        QRCodeScanDialogFragment qrCodeScanDialog = new QRCodeScanDialogFragment();
        qrCodeScanDialog.setQRCodeScanListener(new QRCodeScanDialogFragment.QRCodeScanListener() {
            @Override
            public void onQRCodeScanned(String result) {
                // Maneja el resultado del escaneo aquí
//                Toast.makeText(getApplicationContext(), "Código escaneado: " + result, Toast.LENGTH_SHORT).show();
                firma.setText(result);
            }
        });
        qrCodeScanDialog.show(getSupportFragmentManager(), "QRScanDialog");
    }


    private void solicitarGrabar() {
        try {
            mensajeConfirmar = new MensajeConfirmar(
                    "¿Desea generar el reporte de supervisión?",
                    "Si",
                    "No",
                    "Informe de supervisión",
                    this
            );
            mensajeConfirmar.setCancelable(false);
            mensajeConfirmar.show(getSupportFragmentManager(), "OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void cambioDePromotor(int posicion) {
        try {
            promotorZona = listadoPromotores.get(posicion);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void salir() {
        finish();
    }
    private void salirAbrirQR() {
        finish();
    }

    private void colocarDatosZona() {
        try {
            zona.setText(GlobalPermisos.getDatosSesionActual().getNombreZona());
        } catch (Exception ex) {
            zona.setText("Sin datos de zona");
            ex.printStackTrace();
        }
    }

    private void recuperarPromotoresEnZona() {
        try {
            supervisionAPI.solicitarPromotoresZona(
                    this,
                    this,
                    SupervisionAPI.Acciones.SOLICITAR_PROMOTORES_EN_ZONA,
                    GlobalPermisos.getDatosSesionActual().getCuenta(),
                    GlobalPermisos.getDatosSesionActual().getIdZona()
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void recuperarPreguntasSupervision() {
        try {
            setDialogSupervision(true);
            supervisionAPI.solicitarPreguntasSupervision(
                    this,
                    this,
                    SupervisionAPI.Acciones.SOLICITAR_PREGUNTAS_SUPERVISION,
                    GlobalPermisos.getDatosSesionActual().getCuenta()
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void procesarJSONPromotoresZona(JSONArray dataArray) {
        try {
            listadoPromotores = cargarPromotoresZona(dataArray);
            printarListadoPromotoresZona();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<PromotorZona> cargarPromotoresZona(JSONArray jsonArray) {
        List<PromotorZona> listadoPromotores = new ArrayList<>();
        try {
            if (jsonArray == null) {
                return listadoPromotores;
            }
            for (int pos = 0; pos < jsonArray.length(); pos++) {
                try {
                    PromotorZona promotorZona = new PromotorZona(jsonArray.getJSONObject(pos));
                    listadoPromotores.add(promotorZona);
                } catch (Exception ex) {
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listadoPromotores;
    }

    private void printarListadoPromotoresZona() {
        try {
            List<String> nombresPromotores = new ArrayList<>();
            for (PromotorZona promotorZona : listadoPromotores) {
                nombresPromotores.add(promotorZona.getNombrePromotor());
            }
            promotores.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.lista_estilo, nombresPromotores));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<Pregunta> getListadoPreguntas() {
        List<Pregunta> res = new ArrayList<>();
        try {
            res = new ArrayList<>(listaChequeo.values());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }

    public int getAlturaLista() {
        int result = 0;
        try {
            for (Map.Entry<Long, Pregunta> cont : listaChequeo.entrySet()) {
                result += (cont.getValue().getPregunta().length() / 20 + 1) * 45;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public void actualizarAlturaListado(@NotNull ListView myListView) {
        try {
            ViewGroup.LayoutParams params = myListView.getLayoutParams();
            params.height = getAlturaLista();
            myListView.setLayoutParams(params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void pintarPreguntasSupervision() {
        try {
            SupervisionPreguntasAdaptador supervisionPreguntasAdaptador = new SupervisionPreguntasAdaptador(
                    getListadoPreguntas(),
                    this,
                    this);
            try {
                listado.setAdapter(supervisionPreguntasAdaptador);
                //actualizarAlturaListado(listado);
                setListViewHeightBasedOnChildren(listado);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void procesarJSONPreguntasSupervision(JSONArray jsonArray) {
        try {
            listaChequeo = new HashMap<>();
            if (jsonArray == null) {
                return;
            }
            for (int pos = 0; pos < jsonArray.length(); pos++) {
                try {
                    Pregunta pregunta = new Pregunta(jsonArray.getJSONObject(pos));
                    listaChequeo.put(pregunta.getId(), pregunta);
                } catch (Exception ex) {
                }
            }
            pintarPreguntasSupervision();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setDialogSupervision(boolean show) {
        try {
            if (show) dialogSupervision.show();
            else dialogSupervision.dismiss();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private JSONArray getPreguntasSupervision() {
        JSONArray lista = new JSONArray();
        try {
            for (Pregunta pregunta : getListadoPreguntas()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("pregunta", Configuracion.encodeTexto(pregunta.getPregunta()));
                jsonObject.put("cumple", pregunta.getCumple());
                jsonObject.put("id", pregunta.getId());
                jsonObject.put("valor", Configuracion.encodeTexto(pregunta.getValor()));
                lista.put(jsonObject);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    /**
     * Dado que se puede firmar con QR, se validan los datos
     * para saber si es el caso y determinar los datos de acceso
     *
     * @return
     */
    private String getFirma() {
        String contenido = "";
        try {
            contenido = firma.getText().toString();
            EncodeService encodeService = new EncodeService();
            final String separador = Configuracion.QR.getSeparadorQR();
            final int minTama = Configuracion.QR.getTamaHash();
            final String confirmado = Configuracion.QR.getTramaConfirmacion();
            String[] partes = contenido.split(separador);
            for (String cont : partes) {
                if (cont.length() > minTama) {
                    String clave = cont.substring(0, minTama);
                    String datos = cont.substring(minTama);
                    String result = encodeService.getDecode(datos, clave);
                    if (result.contains(confirmado)) {
                        final Gson gson = new Gson();
                        Configuracion.DatosQR datosQR = gson.fromJson(result, Configuracion.DatosQR.class);
                        if (datosQR.getI().equals(promotorZona.getIdPromotor())) {
                            return datosQR.getC();
                        } else {
                            return "*&^%$#@!.><;:'";//retorno basura,dado que los datos suministrados no son del promotor seleccionado
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return contenido;
    }

    private String getValorFirma() {
        try {
            String valor = getFirma();
            if (valor.isEmpty()) {
                return "";
            }
            return Hash.getHashStringHexa(valor);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    private JSONObject getEncabezado() {
        JSONObject cont = new JSONObject();
        try {
            cont.put("id", 0);
            cont.put("zona", GlobalPermisos.getDatosSesionActual().getIdZona());
            cont.put("supervisor", GlobalPermisos.getDatosSesionActual().getIdPromotor());
            cont.put("promotor", promotorZona.getIdPromotor());
            cont.put("informacion", Configuracion.encodeTexto(informacion.getText().toString()));
            cont.put("preguntas", getPreguntasSupervision());
            cont.put("firmaHash", getValorFirma());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cont;
    }

    /**
     * Dado que se pueden equivocar y tratar de leer el QR
     * en el campo de información se trata de identificar el
     * caso y  borrar el contenido desplegado.
     *
     * @param contenido
     */
    private void validarContenidoInformacion(String contenido) {
        try {
            if (contenido.contains(Configuracion.QR.getSeparadorQR())) {
                limpiarInformacion();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void limpiarInformacion() {
        try {
            informacion.setText("");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void limpiarFirma() {
        try {
            firma.setText("");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void iniciarGrabarServidor() {
        try {
            String mensaje = "El supervisor: "+
                    GlobalPermisos.getDatosSesionActual().getNombrePromotor()+
                    " ha realizado un informe en la zona: "+
                    GlobalPermisos.getDatosSesionActual().getNombreZona()+ " a el promotor: "+
                    promotorZona.getNombrePromotor();
            enviarSms(mensaje);
            setDialogSupervision(true);
            JSONObject cont = getEncabezado();
            limpiarFirma();
            supervisionAPI.solicitarGrabarRegistroSupervision(
                    this,
                    this,
                    SupervisionAPI.Acciones.GRABAR_REGISTRO_SUPERVISION,
                    GlobalPermisos.getDatosSesionActual().getCuenta(),
                    cont
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void mensajeErrorGrabar(String contenido) {
        try {
            mensajeAceptar = new MensajeAceptar(
                    "No fue posible grabar el reporte.\n" + contenido,
                    "Aceptar",
                    "Error al grabar supervisión",
                    MensajeAceptar.Colores.VERDE);
            mensajeAceptar.setCancelable(false);
            mensajeAceptar.show(getSupportFragmentManager(), "OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void respuestaSI() {
        iniciarGrabarServidor();
    }

    @Override
    public void respuestaNO() {

    }

    @Override
    public void respuestaJSON(String accion, int statusCode, Header[] headers, byte[] responseBytes, String responseString, JSONObject responseJSON, JSONArray responseArrayJSON) {
        switch (accion) {
            case SupervisionAPI.Acciones.SOLICITAR_PROMOTORES_EN_ZONA:
                procesarJSONPromotoresZona(responseArrayJSON);
                break;
            case SupervisionAPI.Acciones.SOLICITAR_PREGUNTAS_SUPERVISION:
                procesarJSONPreguntasSupervision(responseArrayJSON);
                setDialogSupervision(false);
                break;
            case SupervisionAPI.Acciones.GRABAR_REGISTRO_SUPERVISION:
                setDialogSupervision(false);
                salir();
                break;
        }
    }

    @Override
    public void errorJSON(String accion, int statusCode, Header[] headers, byte[] responseBytes, String responseString, JSONObject JSONResponse, JSONArray JSONArrayResponse, Throwable throwable) {
        switch (accion) {
            case SupervisionAPI.Acciones.SOLICITAR_PROMOTORES_EN_ZONA:
                break;
            case SupervisionAPI.Acciones.SOLICITAR_PREGUNTAS_SUPERVISION:
                setDialogSupervision(false);
                break;
            case SupervisionAPI.Acciones.GRABAR_REGISTRO_SUPERVISION:
                setDialogSupervision(false);
                try {
                    mensajeErrorGrabar(JSONResponse.getString("mensaje"));
                } catch (JSONException e) {
                    mensajeErrorGrabar("Verifique que los datos suministrados sean correctos");
                }
                break;
        }
    }

    @Override
    public void termino() {

    }
    private void enviarSms(String mensaje){
        try {
            SmsManager smsManager = SmsManager.getDefault();
           smsManager.sendTextMessage("+57 310 2211353" , null,
             mensaje, null, null);
            Toast.makeText(this, "Mensaje de texto enviado.", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, "Falló al enviar el mensaje de texto",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void cambioPregunta(Pregunta pregunta, long indice) {
        try {
            listaChequeo.put(pregunta.getId(), pregunta);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onQRCodeScanned(String result) {
        // Aquí puedes manejar el resultado del QR escaneado
        firma.setText(result);
        getValorFirma();  // Actualiza el valor de la firma basado en el QR escaneado
    }
}