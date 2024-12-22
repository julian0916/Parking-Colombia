package com.example.zer.somos.operaciones;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import com.example.zer.somos.R;
import com.example.zer.somos.comunes.*;
import com.example.zer.somos.comunicacion.IRespuestaHttpCliente;
import com.example.zer.somos.permisos.GlobalPermisos;
import com.example.zer.somos.permisos.PermisosActivity;
import com.example.zer.somos.utilidades.*;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrepagoActivity extends ActivityBase implements IRespuestaConfirmar,
        IRespuestaHttpCliente, IRespuestaImpresion, IRespuestaConfirmarAccion {

    public static final String REINTENTAR_IMPRIMIR = "REINTENTAR_IMPRIMIR";
    private static DialogFragment mensajeGeneral;
    protected AlertDialog.Builder builderImprimiendo;
    protected Dialog dialogImprimiendo;
    private PrepagoAPI prepagoAPI;
    private Spinner horas;
    private EditText placa;
    private Button ventaPrepago;
    private Button salir;
    private DialogFragment mensajeConfirmar;
    private AlertDialog.Builder builder;
    private Dialog dialog;
    private ImpresoraPos impresoraPos;
    private IRespuestaImpresion iRespuestaImpresion;
    private PreviewView previewView;
    private ImageCapture imageCapture;
    private ExecutorService cameraExecutor;
    private Button abrirCamara;
    private Button captureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_prepago);
            iniciarComponentes();
            programarEventos();
            pintarListadoHoras();
            impresoraPos = new ImpresoraPos(this); // Inicializar la impresora
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void iniciarComponentes() {
        try {
            salir = findViewById(R.id.salir);
            horas = findViewById(R.id.horas);
            placa = findViewById(R.id.placa);
            ventaPrepago = findViewById(R.id.ventaPrepago);
            prepagoAPI = new PrepagoAPI();
            builder = new AlertDialog.Builder(this);
            builder.setView(R.layout.progress);
            builder.setCancelable(false);
            dialog = builder.create();
            builderImprimiendo = new AlertDialog.Builder(this);
            builderImprimiendo.setView(R.layout.imprimiendo);
            builderImprimiendo.setCancelable(true);
            dialogImprimiendo = builderImprimiendo.create();
            previewView = findViewById(R.id.previewView);
            abrirCamara = findViewById(R.id.abrirCamara);
            captureButton = findViewById(R.id.captureButton);
            cameraExecutor = Executors.newSingleThreadExecutor();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void programarEventos() {
        try {
            ventaPrepago.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    grabarVentaPrepago();
                }
            });

            salir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    salir();
                }
            });

            abrirCamara.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startCamera();
                    captureButton.setVisibility(View.VISIBLE);
                    previewView.setVisibility(View.VISIBLE);
                    abrirCamara.setVisibility(View.GONE);
                    ventaPrepago.setVisibility(View.GONE);
                }
            });

            captureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    captureImage();
                    previewView.setVisibility(View.GONE);
                    captureButton.setVisibility(View.GONE);
                    ventaPrepago.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                cameraProvider.unbindAll();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Error starting camera: " + e.getMessage());
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        imageCapture = new ImageCapture.Builder().build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
    }

    private void captureImage() {
        if (imageCapture == null) {
            return;
        }
        imageCapture.takePicture(ContextCompat.getMainExecutor(this), new ImageCapture.OnImageCapturedCallback() {
            @Override
            public void onCaptureSuccess(@NonNull ImageProxy image) {
                processImageProxy(image);
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Log.e(TAG, "Photo capture failed: " + exception.getMessage());
            }
        });
    }

    private void processImageProxy(@NonNull ImageProxy image) {
        @SuppressLint("UnsafeOptInUsageError")
        InputImage inputImage = InputImage.fromMediaImage(image.getImage(), image.getImageInfo().getRotationDegrees());
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        recognizer.process(inputImage)
                .addOnSuccessListener(text -> {
                    String recognizedText = text.getText();
                    recognizedText = cleanRecognizedText(recognizedText); // Limpiar el texto obtenido

                    if (!recognizedText.equals("Formato de placa no reconocido")) {
                        // Actualizar el campo de texto 'placa'
                        placa.setText(recognizedText);
                        abrirCamara.setVisibility(View.VISIBLE);
                    } else {
                        abrirCamara.setVisibility(View.VISIBLE);
                        Toast.makeText(this, recognizedText, Toast.LENGTH_LONG).show();
                    }

                    Log.d(TAG, "Text recognized: " + recognizedText);

                    image.close();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Text recognition failed: " + e.getMessage());
                    image.close();
                });
    }

    // Método para limpiar el texto reconocido
    private String cleanRecognizedText(String text) {
        // Eliminar todos los caracteres que no sean letras o números
        text = text.replaceAll("[^a-zA-Z0-9]", "");

        // Crear patrones para los dos posibles formatos de placa
        Pattern pattern1 = Pattern.compile("([a-zA-Z]{3}[0-9]{3})|" +
                "([0-9]{3}[a-zA-Z]{3})|" +//caso raro
                "([a-zA-Z]{2}[0-9]{4})|" +//servicio diplomatico
                "([a-zA-Z][0-9]{4})"//transporte
        );
        Pattern pattern2 = Pattern.compile("[a-zA-Z]{3}[0-9]{2}[a-zA-Z]{1}|[a-zA-Z]{3}[0-9]{2}");

        // Buscar la primera ocurrencia que cumpla con alguno de los patrones
        Matcher matcher1 = pattern1.matcher(text);
        Matcher matcher2 = pattern2.matcher(text);

        if (matcher1.find()) {
            // Formato encontrado: 3 letras seguidas de 3 números
            return matcher1.group();
        } else if (matcher2.find()) {
            // Formato encontrado: 3 letras seguidas de 2 números y una letra
            return matcher2.group();
        } else {
            captureButton.setVisibility(View.GONE);
            // Si no se encuentra ninguno de los formatos esperados, devolver un mensaje indicativo
            return "Formato de placa no reconocido";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }

    private void salir() {
        try {
            finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void pintarListadoHoras() {
        try {
            List<Long> horasList = new ArrayList<>();
            horasList.add(1l);
            horasList.add(2l);
            horasList.add(3l);
            horasList.add(4l);
            horasList.add(5l);
            horasList.add(6l);
            horasList.add(7l);
            horasList.add(8l);
            horasList.add(9l);
            horas.setAdapter(new ArrayAdapter<Long>(getApplicationContext(), R.layout.lista_estilo, horasList));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void grabarVentaPrepago() {
        try {
            ocultarTeclado();
            limpiarDatosPlaca();
            int tipoPlaca = DatosIngreso.getTipoPlaca(placa.getText().toString());
            if (tipoPlaca == DatosIngreso.NO_ES_PLACA) {
                mensajeDatosPlacaIncorrectos();
                return;
            }
            if (tipoPlaca == DatosIngreso.PLACA_CARRO && GlobalPermisos.getDatosSesionActual().getCeldasCarro() < 1) {
                mensajeCarrosNoPermitidosEnZona();
                return;
            }
            if (tipoPlaca == DatosIngreso.PLACA_MOTO && GlobalPermisos.getDatosSesionActual().getCeldasMoto() < 1) {
                mensajeMotosNoPermitidasEnZona();
                return;
            }
            mensajeConfirmarDatos();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void limpiarDatosPlaca() {
        try {
            placa.setText(DatosIngreso.limpiarContenidos(placa.getText().toString()).toUpperCase());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setDialog(boolean show) {
        try {
            if (show) dialog.show();
            else dialog.dismiss();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void mensajeConfirmarDatos() {
        try {
            final String ENTER = "\n";
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("Fecha: ");
            mensaje.append(Configuracion.getFechaHoraActual());
            mensaje.append(ENTER);
            mensaje.append("Placa: ");
            mensaje.append(placa.getText());
            mensaje.append(ENTER);
            mensaje.append("Vehículo: ");
            mensaje.append(DatosIngreso.getTipoPlaca(
                    placa.getText().toString()) == DatosIngreso.PLACA_CARRO ? "Carro" : "Moto");
            mensaje.append(ENTER);
            mensaje.append("Tiempo: ");
            mensaje.append(horas.getSelectedItem());
            mensaje.append((Long) horas.getSelectedItem() == 1l ? " Hora" : " Horas");
            mensaje.append(ENTER);
            mensaje.append("Valor: ");
            Long valor = DatosIngreso.getTipoPlaca(
                    placa.getText().toString()) == DatosIngreso.PLACA_CARRO ?
                    GlobalPermisos.getDatosSesionActual().getValorHoraCarro() :
                    GlobalPermisos.getDatosSesionActual().getValorHoraMoto();
            valor = valor * (Long) horas.getSelectedItem();
            mensaje.append("$");
            mensaje.append(valor);
            mensaje.append(ENTER);
            mensajeConfirmar = new MensajeConfirmar(
                    mensaje.toString(),
                    "SI",
                    "NO",
                    "Confirmar Prepago",
                    this
            );
            mensajeConfirmar.setCancelable(false);
            mensajeConfirmar.show(getSupportFragmentManager(), "OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void mensajeDatosPlacaIncorrectos() {
        try {
            if (mensajeGeneral != null) {
                mensajeGeneral.dismiss();
            }
            mensajeGeneral = new MensajeAceptar(
                    "La placa ingresada no es correcta.\n\nCorrija y vuelva a intentarlo.",
                    "Aceptar",
                    MensajeAceptar.Colores.AZUL);
            mensajeGeneral.setCancelable(false);
            mensajeGeneral.show(getSupportFragmentManager(), "OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Se visualiza que los carros no son permitidos en esta zona
     */
    private void mensajeCarrosNoPermitidosEnZona() {
        try {
            if (mensajeGeneral != null) {
                mensajeGeneral.dismiss();
            }
            mensajeGeneral = new MensajeAceptar(
                    "En esta zona no se permite parquear carros.",
                    "Aceptar",
                    MensajeAceptar.Colores.AZUL);
            mensajeGeneral.setCancelable(false);
            mensajeGeneral.show(getSupportFragmentManager(), "OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Se muestra un mensaje que alerta que en
     * la zona no se permiten parquear motos
     */
    private void mensajeMotosNoPermitidasEnZona() {
        try {
            if (mensajeGeneral != null) {
                mensajeGeneral.dismiss();
            }
            mensajeGeneral = new MensajeAceptar(
                    "En esta zona no se permite parquear motos.",
                    "Aceptar",
                    MensajeAceptar.Colores.AZUL);
            mensajeGeneral.setCancelable(false);
            mensajeGeneral.show(getSupportFragmentManager(), "OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void mensajeConfirmarCerrar() {
        try {
            if (mensajeConfirmar == null) {
                return;
            }
            mensajeConfirmar.dismiss();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private JSONObject getData() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("esPrepago", true);
            jsonObject.put("horas", horas.getSelectedItem());
            jsonObject.put("placa", placa.getText());
            jsonObject.put("promotor", GlobalPermisos.getDatosSesionActual().getIdPromotor());
            jsonObject.put("zona", GlobalPermisos.getDatosSesionActual().getIdZona());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }

    private void solicitarRegistroPrepagoServidor() {
        try {
            prepagoAPI.ingresarPrepago(this,
                    this,
                    PrepagoAPI.Acciones.GRABAR_PREPAGO,
                    GlobalPermisos.getDatosSesionActual().getCuenta(),
                    getData());

            Long valor = DatosIngreso.getTipoPlaca(
                    placa.getText().toString()) == DatosIngreso.PLACA_CARRO ?
                    GlobalPermisos.getDatosSesionActual().getValorHoraCarro() :
                    GlobalPermisos.getDatosSesionActual().getValorHoraMoto();
            valor = valor * (Long) horas.getSelectedItem();

            DatabaseHelper db = SelectScanner.getDatabaseHelper();
          //  db.actualizarTotalToPay(valor, "");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void ocultarTeclado() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(placa.getWindowToken(), 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void imprimirReciboPrepago(JSONObject conte) {
        // Mostrar el mensaje "Imprimiendo"
        mensajeImprimiendo(true);

        try {
            // Conectar con la impresora
            impresoraPos.conectar();

            // Imprimir el recibo
            generarReciboPrepago(conte);

            // Esperar 2 segundos antes de desconectar la impresora
            try {
                Thread.sleep(2000); // Espera 2 segundos
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Desconectar la impresora
            impresoraPos.desconectar();

            // Mostrar el mensaje de impresión exitosa
            impresionExitosa();
        } catch (Exception ex) {
            // Mostrar el mensaje de error en caso de excepción
            ex.printStackTrace();
            // Cerrar el diálogo de "Imprimiendo" en caso de error
            mensajeImprimiendo(false);
        }
    }

    private void generarReciboPrepago(JSONObject conteJSON) {
        DatosSesion datosSesion = GlobalPermisos.getDatosSesionActual();
        String promotor= ": " +datosSesion.getNombrePromotor();

        if (conteJSON == null) {
            Toast.makeText(this, "No hay contenido para imprimir", Toast.LENGTH_SHORT).show();
        }
        try {

            String placa = conteJSON.getString("placa");
            //crearCodigoBarras(placa);
            impresoraPos.print(datosSesion.getLema(), true, false,false, false);
            impresoraPos.addEnter();
            impresoraPos.print(datosSesion.getNombreParaTiquete(), true, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print(("Nit: " + datosSesion.getNit()) ,true, true, false, false);
            impresoraPos.addEnter();
            impresoraPos.addEnter();
            impresoraPos.print("PREPAGO", true, true, true, false);
            impresoraPos.addEnter();
            impresoraPos.addEnter();
            impresoraPos.print("No: " + conteJSON.getLong("id"), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Placa: " + conteJSON.getString("placa"), false, true, true, false);
            impresoraPos.addEnter();
            impresoraPos.print("Vehiculo: " + (conteJSON.getBoolean("esCarro") ? "Carro" : "Moto"), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Inicia: " + Configuracion.getFechaHora(conteJSON.getString("fhingreso")), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Termina:" + Configuracion.getFechaHora(conteJSON.getString("fhegreso")), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Horas prepagadas: " + conteJSON.getLong("hcobradas"), false, false, false, false);
            impresoraPos.print(conteJSON.getLong("hcobradas") == 1 ? " hora" : " horas", false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Valor pagado: $" + conteJSON.getString("valorCobrado"), false, true, true, false);
            impresoraPos.addEnter();
            impresoraPos.print("Zona: " + datosSesion.getNombreZona(), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Horario: " +
                    Configuracion.getHoraAMPM(conteJSON.getString("hiniciaZona")) +
                    "- " +
                    Configuracion.getHoraAMPM(conteJSON.getString("hterminaZona")), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Valor hora-fraccion: $" + conteJSON.getLong("valorH"), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Promotor" + promotor, false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Terminos y condiciones", true, true, false, false);
            impresoraPos.addEnter();
            impresoraPos.printQrCode(impresoraPos.generarCodigoQR("https://somosmovilidad.gov.co/zonas-de-estacionamiento-regulado-z-e-r-de-rionegro/"));
            impresoraPos.print("GRACIAS", true, true, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Con tu pago aportas a los programas sociales del municipio de Rionegro.", false, true, false, false);
            impresoraPos.addEnter();
            impresoraPos.addEnter();
            impresoraPos.addEnter();
            DatabaseHelper dbHelper =  SelectScanner.getDatabaseHelper();
           // dbHelper.incrementarPrepago();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void mensajeErrorServidor(String mensaje) {
        try {
            DialogFragment newFragment = new MensajeAceptar(
                    mensaje,
                    "Aceptar",
                    "No se pudo grabar",
                    MensajeAceptar.Colores.AZUL);
            newFragment.setCancelable(false);
            newFragment.show(getSupportFragmentManager(), "ERROR");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void errorAlGrabarServidor(String mensaje) {
        try {
            mensajeErrorServidor(mensaje);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void mensajeImprimiendo(boolean mostrar) {
        try {
            if (dialogImprimiendo == null) {
                return;
            }
            if (mostrar) {
                dialogImprimiendo.show();
                return;
            }
            dialogImprimiendo.dismiss();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void mensajeReimprimir(ConectorImpresoraBT conectorImpresoraBT) {
        try {
            DialogFragment newFragment = new MensajeConfirmarAccion(
                    REINTENTAR_IMPRIMIR,
                    "¿Desea intentarlo nuevamente para generar el tiquete prepago?",
                    "Imprimir",
                    "Cancelar",
                    "Hubo un error al imprimir",
                    conectorImpresoraBT,
                    this,
                    MensajeConfirmarAccion.Colores.ROJO);
            newFragment.setCancelable(false);
            newFragment.show(getSupportFragmentManager(), "ERROR");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void reintentarImprimir(ConectorImpresoraBT conectorImpresoraBT) {
        try {
            if (conectorImpresoraBT == null) {
                salir();
            }
            mensajeImprimiendo(true);
            conectorImpresoraBT.imprimir();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void respuestaSI() {
        try {
            mensajeConfirmarCerrar();
            setDialog(true);
            solicitarRegistroPrepagoServidor();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void respuestaNO() {
        try {
            mensajeConfirmar.dismiss();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void respuestaJSON(String accion, int statusCode, Header[] headers, byte[] responseBytes, String responseString, JSONObject responseJSON, JSONArray responseArrayJSON) {
        switch (accion) {
            case PrepagoAPI.Acciones.GRABAR_PREPAGO:
                setDialog(false);
                imprimirReciboPrepago(responseJSON);
                break;
        }
    }

    @Override
    public void errorJSON(String accion, int statusCode, Header[] headers, byte[] responseBytes, String responseString, JSONObject JSONResponse, JSONArray JSONArrayResponse, Throwable throwable) {
        switch (accion) {
            case PrepagoAPI.Acciones.GRABAR_PREPAGO:
                setDialog(false);
                errorAlGrabarServidor(responseString);
                break;
        }
    }

    @Override
    public void termino() {

    }

    @Override
    public void impresionExitosa() {
        mensajeImprimiendo(false);
        salir();
    }

    @Override
    public void impresionFallo(ConectorImpresoraBT conectorImpresoraBT) {
        mensajeImprimiendo(false);
        salir();
        mensajeReimprimir(conectorImpresoraBT);
    }

    @Override
    public void respuestaSI(String accion, Object contenido) {
        switch (accion) {
            case REINTENTAR_IMPRIMIR:
                reintentarImprimir((ConectorImpresoraBT) contenido);
                break;
        }
    }

    @Override
    public void respuestaNO(String accion, Object contenido) {
        switch (accion) {
            case REINTENTAR_IMPRIMIR:
                salir();
                break;
        }
    }
}