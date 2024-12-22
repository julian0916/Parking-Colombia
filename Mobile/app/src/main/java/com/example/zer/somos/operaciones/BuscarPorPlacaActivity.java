package com.example.zer.somos.operaciones;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import com.example.zer.somos.comunes.DatosSesion;
import com.example.zer.somos.comunes.IRespuestaConfirmar;
import com.example.zer.somos.comunes.MensajeAceptar;
import com.example.zer.somos.comunes.MensajeConfirmarAccion;
import com.example.zer.somos.permisos.GlobalPermisos;
import com.example.zer.somos.utilidades.Configuracion;
import com.example.zer.somos.utilidades.ContenidoImpresoraBT;
import com.example.zer.somos.utilidades.DatosIngreso;
import com.example.zer.somos.utilidades.ImpresoraPos;
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

public class BuscarPorPlacaActivity extends CierreVentas implements IRespuestaConfirmar{

    private final static String ACCION_PROBLEMAS_CONEXION = "ACCION_PROBLEMAS_CONEXION";
    private static DialogFragment mensajeNoHayRegistros;
    private static DialogFragment fragmentoProblemasConexion;
    private static DialogFragment mensajeGeneral;
    private EditText placa;
    private Button buscarPorPlaca;
    private Button ventaPostpago;
    private Button ventapostpagoLogo;
    private Button imprimirCertificado;
    private ImpresoraPos impresoraPos;
    private PreviewView previewView;
    private ImageCapture imageCapture;
    private ExecutorService cameraExecutor;
    private Button abrirCamara;
    private Button captureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_buscar_por_placa);

            iniciarComponentes();
            programarEventos();

            impresoraPos = new ImpresoraPos(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void iniciarComponentes() {
        try {
            super.iniciarComponentes();
            placa = findViewById(R.id.placa);
            placa.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            placa.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(6) });
            buscarPorPlaca = findViewById(R.id.buscarPorPlaca);
            ventaPostpago = findViewById(R.id.ventaPostpago);
            ventapostpagoLogo = findViewById(R.id.ventaPostpago_logo);
            imprimirCertificado = findViewById(R.id.imprimirCertificado);
            previewView = findViewById(R.id.previewView);
            abrirCamara = findViewById(R.id.abrirCamara);
            captureButton = findViewById(R.id.captureButton);
            cameraExecutor = Executors.newSingleThreadExecutor();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void programarEventos() {
        try {
            ventaPostpago.setVisibility(View.GONE);
            ventapostpagoLogo.setVisibility(View.GONE);
            imprimirCertificado.setVisibility(View.GONE);
            super.programarEventos();
            buscarPorPlaca.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buscarPorPlaca();
                    abrirCamara.setVisibility(View.GONE);
                }
            });
            ventaPostpago.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    salir();
                    abrirPostpagoConPlaca(placa.getText().toString());

                }
            });
            ventapostpagoLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    salir();
                    abrirPostpagoConPlaca(placa.getText().toString());

                }
            });
            imprimirCertificado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imprimir();
                }
            });

            abrirCamara.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startCamera();
                    captureButton.setVisibility(View.VISIBLE);
                    previewView.setVisibility(View.VISIBLE);
                    buscarPorPlaca.setVisibility(View.GONE);
                    abrirCamara.setVisibility(View.GONE);
                }
            });

            captureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    captureImage();
                    previewView.setVisibility(View.GONE);
                    captureButton.setVisibility(View.GONE);
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

                            // Llamar a la función buscarPorPlaca
                            //buscarPorPlaca();
                            buscarPorPlaca.setVisibility(View.VISIBLE);
                        } else {
                            buscarPorPlaca.setVisibility(View.VISIBLE);
                            abrirCamara.setVisibility(View.VISIBLE);
                            captureButton.setVisibility(View.GONE);
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
            captureButton.setVisibility(View.VISIBLE);
            // Si no se encuentra ninguno de los formatos esperados, devolver un mensaje indicativo
            return "Formato de placa no reconocido";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }

    private  void imprimir() {
        // Mostrar el mensaje "Imprimiendo"
        mensajeImprimiendo(true);

        try {
            // Conectar con la impresora
            impresoraPos.conectar();

            // Imprimir el recibo
            generarCertificado(placa.getText().toString());

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

    private ContenidoImpresoraBT generarCertificado(String placa) {
        ContenidoImpresoraBT conte = new ContenidoImpresoraBT();
        try {
            DatosSesion datosSesion = GlobalPermisos.getDatosSesionActual();
            impresoraPos.print("LA EMPRESA SOMOS SISTEMA OPERATIVO DE MOVILIDAD ORIENTE " +
                    "SOSTENIBLE SOMOS RIONEGRO S.A.S. ", true, true, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("certifica que de conformidad con el decreto 453 de 2017, mediante" +
                    " el cual se reglamento el acuerdo 006 de 2017 y, se establecio, en armonia con lo " +
                    "estipulado en el articulo segundo del acuerdo municipal 008 de 2016, adicionado" +
                    " por el articulo segundo del acuerdo municipal 014 de 2017, adicionado por el " +
                    "acuerdo 033 de 2017, el usuario propietario del vehiculo con placa ", true, false, false, false);
            impresoraPos.print(placa, true, true, false, false);
            impresoraPos.print(", se encuentra a paz y salvo por concepto del pago de la tasa por " +
                    "estacionamiento en espacio publico en el municipio de Rionegro, segun lo " +
                    "señalado en la normatividad anterior.", true, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print(
                    "para constancia se expide de manera electronica en la ciudad de Rionegro, " +
                            "Antioquia en la fecha: ", true, false, false, false);
            impresoraPos.print(Configuracion.getFechaHoraActualAMPM(),true, true, false, false);
            impresoraPos.print(" por el supervisor: ", true, false, false, false);
            impresoraPos.print(datosSesion.getNombrePromotor(), true, true, false, false);
            impresoraPos.addEnter();
            impresoraPos.addEnter();
            impresoraPos.addEnter();

        }catch (Exception e){
            e.printStackTrace();
        }

        return conte;
    }

    private void buscarPorPlaca() {
        try {
            limpiarDatosPlaca();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            limpiarListadoCarteraPlaca();
            if (placa == null) {
                return;
            }
            ocultarTeclado(placa.getWindowToken());
            int tipoPlaca = DatosIngreso.getTipoPlaca(placa.getText().toString());
            if ( tipoPlaca == DatosIngreso.NO_ES_PLACA) {
                mensajeDatosPlacaIncorrectos();
                return;
            }

            solicitarDatosServidorPlaca();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ventaPostpago.setVisibility(View.VISIBLE);
        ventapostpagoLogo.setVisibility(View.VISIBLE);
        buscarPorPlaca.setVisibility(View.GONE);
    }

    private void abrirPostpagoConPlaca(String placa) {
        try {
            abrirActivity(PostpagoActivity.class, PostpagoActivity.ABRIR_CON_PLACA, placa);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void abrirActivity(Class<?> clase, String nombre, String valor) {
        try {
            permitirSalir();
            Intent intent = new Intent(this, clase);
            intent.putExtra(nombre, valor);
            startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void salir() {
        try {
            finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void limpiarDatosPlaca() {
        try {
            if (placa == null) {
                return;
            }
            placa.setText(DatosIngreso.limpiarContenidos(placa.getText().toString()).toUpperCase());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void solicitarDatosServidorPlaca() {
        try {
            setDialog(true);
            cierreVentasAPI.solicitarCarteraPlaca(this,
                    this,
                    CierreVentasAPI.Acciones.CARTERA_PLACA,
                    GlobalPermisos.getDatosSesionActual().getCuenta(),
                    placa.getText().toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Mensaje que los datos de la placa son incorrectos
     * según las reglas
     */
    private void mensajeDatosPlacaIncorrectos() {
        try {
            if(mensajeGeneral!=null){
                mensajeGeneral.dismiss();
            }
            mensajeGeneral = new MensajeAceptar(
                    "La placa ingresada no es correcta.\n\nCorrija y vuelva a intentarlo.",
                    "Aceptar",
                    MensajeAceptar.Colores.AMARILLO);
            mensajeGeneral.setCancelable(false);
            mensajeGeneral.show(getSupportFragmentManager(), "OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Mensaje para notificar que no hay datos que mostrar
     * para la placa que se ha ingresado.
     */
    private void mensajeNoHayDatosParaPlacaIngresada() {
        try {
            if (mensajeNoHayRegistros != null) {
                mensajeNoHayRegistros.dismiss();
            }
            mensajeNoHayRegistros = new MensajeAceptar(
                    "La placa ingresada no tiene cuentas que mostrar.",
                    "Aceptar",
                    MensajeAceptar.Colores.VERDE);
            mensajeNoHayRegistros.setCancelable(false);
            mensajeNoHayRegistros.show(getSupportFragmentManager(), "OK");
            if (GlobalPermisos.getDatosSesionActual().getIdPerfil() == GlobalPermisos.Perfiles.SUPERVISOR){
                imprimirCertificado.setVisibility(View.VISIBLE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Toma los datos de los JSON para pasarlos a
     * una list de contenidos a ser visualizados
     * para el usuario.
     *
     * @param jsonObject
     * @return
     */
    private List<DatosRegistro> getListadoPorPlaca(JSONObject jsonObject) {
        List<DatosRegistro> result = new ArrayList<>();
        try {
            if (jsonObject == null) {
                return result;
            }
            try {
                JSONArray preli = jsonObject.getJSONArray("preliquidados");
                for (int pos = 0; pos < preli.length(); pos++) {
                    DatosRegistro datosRegistro = new DatosRegistro(preli.getJSONObject(pos));
                    result.add(datosRegistro);
                }
            } catch (Exception ex) {
            }
            try {
                JSONArray reportados = jsonObject.getJSONArray("reportados");
                for (int pos = 0; pos < reportados.length(); pos++) {
                    DatosRegistro datosRegistro = new DatosRegistro(reportados.getJSONObject(pos));
                    result.add(datosRegistro);
                }
            } catch (Exception ex) {
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * Verifica el contenido de la lista si no tiene elementos invoca un mensaje
     * de notificación y retorna false.
     * Si tiene elementos retorna true y no genera ningún mensaje.
     *
     * @param datosRegistros
     * @return
     */
    private boolean procesarMensajeSinRegistros(List<DatosRegistro> datosRegistros) {
        boolean continuar = true;
        try {
            continuar = datosRegistros.size() > 0;
        } catch (Exception ex) {
            continuar = false;
        }
        if (!continuar) {
            setDialog(false);
            mensajeNoHayDatosParaPlacaIngresada();
        }
        return continuar;
    }

    /**
     * Se encarga de generar los contenidos de registros
     * recuperados que se deben mostrar al usuario.
     *
     * @param jsonObject
     */
    private void cargarListadoCarteraPlaca(JSONObject jsonObject) {
        try {
            List<DatosRegistro> datosRegistros = getListadoPorPlaca(jsonObject);
            if (!procesarMensajeSinRegistros(datosRegistros)) {
                return;
            }
            DatosRegistroAdaptador datosRegistroAdaptador = new DatosRegistroAdaptador(
                    datosRegistros,
                    this,
                    this);
            try {
                listado.setAdapter(datosRegistroAdaptador);
                //En datosRegistroAdaptador que es el adaptador
                //en el método getView se invoca el método
                //notifyDataSetChanged() y es el que dispara este
                //observador que espera 200 milisegundos para quitar
                //el ícono de procesando.
                datosRegistroAdaptador.registerDataSetObserver(new DataSetObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setDialog(false);
                            }
                        }, 200);

                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Limpia el ListView con los contenidos disponibles
     */
    private void limpiarListadoCarteraPlaca() {
        try {
            List<DatosRegistro> datosRegistros = new ArrayList<>();
            DatosRegistroAdaptador datosRegistroAdaptador = new DatosRegistroAdaptador(
                    datosRegistros,
                    this,
                    this);
            try {
                listado.setAdapter(datosRegistroAdaptador);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Permite cerrar la ventana que contiene la
     * información sobre problemas con la conexión
     */
    private void cerrarMensajeProblemasConConexion() {
        try {
            if (fragmentoProblemasConexion != null) {
                fragmentoProblemasConexion.dismiss();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Mensaje especializado en dar aviso sobre
     * problemas con la conexión.
     */
    private void mensajeProblemasConConexion() {
        try {
            cerrarMensajeProblemasConConexion();
            fragmentoProblemasConexion = new MensajeConfirmarAccion(
                    ACCION_PROBLEMAS_CONEXION,
                    "La conexión, está un poco lenta :(\n\n¿Desea intentarlo nuevamente?",
                    "Si",
                    "No",
                    "Error en la conexión",
                    null,
                    this,
                    MensajeAceptar.Colores.AMARILLO

            );
            fragmentoProblemasConexion.setCancelable(false);
            fragmentoProblemasConexion.show(getSupportFragmentManager(), "OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Mensaje de confirmación cuando se presenta un fallo
     *
     * @param mensaje
     */
    protected void confirmarPagoFalloEsperaAceptar(String mensaje) {
        try {
            if (mensaje == null) {
                mensaje = "";
            }
            if (mensaje.trim().isEmpty()) {
                mensaje = "Se presentó un inconveniente, intentelo nuevamente por favor :)";
            }
            if (mensajeError != null) {
                mensajeError.dismiss();
            }

            mensajeError = new MensajeAceptar(
                    mensaje,
                    "Aceptar",
                    "Error",
                    this,
                    MensajeAceptar.Colores.ROJO);
            mensajeError.setCancelable(false);
            mensajeError.show(getSupportFragmentManager(), "OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void respuestaJSON(String accion, int statusCode, Header[] headers, byte[] responseBytes, String responseString, JSONObject responseJSON, JSONArray responseArrayJSON) {
        try {
            super.respuestaJSON(accion, statusCode, headers, responseBytes, responseString, responseJSON, responseArrayJSON);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        switch (accion) {
            case CierreVentasAPI.Acciones.CARTERA_PLACA:
                cargarListadoCarteraPlaca(responseJSON);
                break;
            case CierreVentasAPI.Acciones.PAGO_EXTEMPORANEO:
            case CierreVentasAPI.Acciones.CONFIRMAR_PAGO:
                limpiarListadoCarteraPlaca();
                terminarPagoExitoso(responseJSON);
                break;
            case CierreVentasAPI.Acciones.REPORTAR_VEHICULO:
                limpiarListadoCarteraPlaca();
                salir();
                break;
        }
    }

    @Override
    public void errorJSON(String accion, int statusCode, Header[] headers, byte[] responseBytes, String responseString, JSONObject JSONResponse, JSONArray JSONArrayResponse, Throwable throwable) {
        try {
            super.errorJSON(accion, statusCode, headers, responseBytes, responseString, JSONResponse, JSONArrayResponse, throwable);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        switch (accion) {
            case CierreVentasAPI.Acciones.CARTERA_PLACA:
            case CierreVentasAPI.Acciones.CARTERA_ZONA:
                setDialog(false);
                mensajeProblemasConConexion();
                break;
            case CierreVentasAPI.Acciones.PAGO_EXTEMPORANEO:
            case CierreVentasAPI.Acciones.CONFIRMAR_PAGO:
            case CierreVentasAPI.Acciones.REPORTAR_VEHICULO:
                setDialog(false);
                limpiarListadoCarteraPlaca();
                confirmarPagoFalloEsperaAceptar(responseString);
                break;
        }
    }

    @Override
    public void respuestaSI(String accion, Object contenido) {
        try {
            super.respuestaSI(accion, contenido);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        switch (accion) {
            case ACCION_PROBLEMAS_CONEXION:
                buscarPorPlaca();
                break;
        }
    }

    @Override
    public void respuestaNO(String accion, Object contenido) {
        try {
            super.respuestaNO(accion, contenido);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        switch (accion) {
            case ACCION_PROBLEMAS_CONEXION:
                salir();
                break;
        }
    }

    @Override
    public void respuestaSI() {
        buscarPorPlaca();
    }

    @Override
    public void respuestaNO() {
        buscarPorPlaca();
    }
}