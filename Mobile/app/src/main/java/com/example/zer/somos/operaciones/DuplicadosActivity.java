package com.example.zer.somos.operaciones;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

public class DuplicadosActivity extends ActivityBase
        implements IRespuestaHttpCliente,
        IOperacionDuplicados, IRespuestaConfirmarAccion,
        IRespuestaImpresion {

    public static final String REINTENTAR_IMPRIMIR = "REINTENTAR_IMPRIMIR";

    private final boolean REIMPRESO = true;
    protected AlertDialog.Builder builderImprimiendo;
    protected Dialog dialogImprimiendo;
    private ListView listado;
    private EditText placa;
    private Button salir;
    private Button buscarPorPlaca;
    private AlertDialog.Builder builder;
    private Dialog dialog;
    private DuplicadosAPI duplicadosAPI;
    private static DialogFragment mensajeConfirmar;
    private static DialogFragment fragmentoMensajeSinRegistros;
    static BluetoothPrint  bluetoothPrinter;
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
            procesarLayout();
            iniciarComponentes();
            programarEventos();
            bluetoothPrinter =  new BluetoothPrint(getApplicationContext());
            impresoraPos = new ImpresoraPos(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void procesarLayout() {
        try {
            setContentView(R.layout.activity_duplicados);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void iniciarComponentes() {
        try {
            placa = findViewById(R.id.placa);
            buscarPorPlaca = findViewById(R.id.buscarPorPlaca);
            listado = findViewById(R.id.listado);
            salir = findViewById(R.id.salir);
            builder = new AlertDialog.Builder(this);
            builder.setView(R.layout.progress);
            builder.setCancelable(false);
            dialog = builder.create();
            duplicadosAPI = new DuplicadosAPI();
            builderImprimiendo = new AlertDialog.Builder(this);
            builderImprimiendo.setView(R.layout.imprimiendo);
            builderImprimiendo.setCancelable(false);
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
            salir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    salir();
                }
            });

            buscarPorPlaca.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buscarPorPlaca();
                }
            });

            abrirCamara.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startCamera();
                    placa.setVisibility(View.GONE);
                    buscarPorPlaca.setVisibility(View.GONE);
                    captureButton.setVisibility(View.VISIBLE);
                    previewView.setVisibility(View.VISIBLE);
                    abrirCamara.setVisibility(View.GONE);
                }
            });

            captureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    captureImage();
                    previewView.setVisibility(View.GONE);
                    captureButton.setVisibility(View.GONE);
                    placa.setVisibility(View.VISIBLE);
                    buscarPorPlaca.setVisibility(View.VISIBLE);
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

    private void buscarPorPlaca() {
        try {
            ocultarTeclado(placa.getWindowToken());
            limpiarDatosPlaca();
            limpiarListado();
            if (DatosIngreso.getTipoPlaca(placa.getText().toString()) == DatosIngreso.NO_ES_PLACA) {
                mensajeDatosPlacaIncorrectos();
                return;
            }
            solicitarDatosServidorPlaca();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void ocultarTeclado(IBinder elemento) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(elemento, 0);
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

    private void mensajeDatosPlacaIncorrectos() {
        try {
            DialogFragment newFragment = new MensajeAceptar(
                    "La placa ingresada no es correcta.\n\nCorrija y vuelva a intentarlo.",
                    "Aceptar",
                    MensajeAceptar.Colores.AZUL_CLARO);
            newFragment.setCancelable(false);
            newFragment.show(getSupportFragmentManager(), "OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void solicitarDatosServidorPlaca() {
        try {
            setDialog(true);
            duplicadosAPI.solicitarMovimientosPlacaHoy(this,
                    this,
                    DuplicadosAPI.Acciones.SOLICITAR_MOVIMIENTOS_PLACA,
                    GlobalPermisos.getDatosSesionActual().getCuenta(),
                    placa.getText().toString());
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

    private List<DatosRegistro> getListado(JSONArray jsonArray) {
        List<DatosRegistro> resp = new ArrayList<>();
        try {
            if (jsonArray == null) {
                return resp;
            }
            for (int pos = 0; pos < jsonArray.length(); pos++) {
                try {
                    DatosRegistro datosRegistro = new DatosRegistro(jsonArray.getJSONObject(pos));
                    resp.add(datosRegistro);
                } catch (Exception ex) {
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resp;
    }

    private void preguntarSiNo(String accion, String titulo, String mensaje, Object contenido) {
        try {
            mensajeConfirmar = new MensajeConfirmarAccion(
                    accion,
                    mensaje,
                    "SI",
                    "NO",
                    titulo,
                    contenido,
                    this
            );
            mensajeConfirmar.setCancelable(false);
            mensajeConfirmar.show(getSupportFragmentManager(), "OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Se limpia el contenido del listado a ser visualizado
     */
    private void limpiarListado() {
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

    private void confirmarReimprimirIngreso(DatosRegistro datosRegistro) {
        try {
            if (datosRegistro == null) {
                return;
            }
            preguntarSiNo(EstadosDuplicados.INGRESO,
                    "Reimprimir tiquete Entrada",
                    "Placa: " + datosRegistro.getPlaca() + "\n" +
                            "Entrada" + Configuracion.getFechaHora(datosRegistro.getIngreso()),
                    datosRegistro);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void confirmarReimprimirEgreso(DatosRegistro datosRegistro) {
        try {
            if (datosRegistro == null) {
                return;
            }
            preguntarSiNo(EstadosDuplicados.EGRESO,
                    "Reimprimir tiquete Salida",
                    "Placa: " + datosRegistro.getPlaca() + "\n" +
                            "Entrada: " + Configuracion.getFechaHora(datosRegistro.getIngreso()) + "\n" +
                            "Salida: " + Configuracion.getFechaHora(datosRegistro.getEgreso()),

                    datosRegistro);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void confirmarReimprimirPrepago(DatosRegistro datosRegistro) {
        try {
            if (datosRegistro == null) {
                return;
            }
            preguntarSiNo(EstadosDuplicados.PREPAGO,
                    "Reimprimir tiquete Prepago",
                    "Placa: " + datosRegistro.getPlaca() + "\n" +
                            "Entrada: " + Configuracion.getFechaHora(datosRegistro.getIngreso()) + "\n" +
                            "Salida: " + Configuracion.getFechaHora(datosRegistro.getEgreso()) + "\n" +
                            "Horas: " + datosRegistro.getHorasCobradas(),
                    datosRegistro);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Muestra un mensaje para notificar que no hay datos para
     * listar en la zona
     */
    private void mensajeNoHayDatosListadosZona() {
        try {
            if(fragmentoMensajeSinRegistros!=null){
                fragmentoMensajeSinRegistros.dismiss();
            }
            fragmentoMensajeSinRegistros = new MensajeAceptar(
                    "No hay datos recientes del vehículo para mostrar.",
                    "Aceptar",
                    MensajeAceptar.Colores.AZUL_CLARO);
            fragmentoMensajeSinRegistros.setCancelable(false);
            fragmentoMensajeSinRegistros.show(getSupportFragmentManager(), "OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Verifica si hay registros para listar, si no muestra un mensaje y retorna false
     * sino, retorna true.
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
            mensajeNoHayDatosListadosZona();
        }
        return continuar;
    }

    private void pintarListado(JSONArray jsonArray) {
        try {
            List<DatosRegistro> datosRegistros = getListado(jsonArray);
            if(!procesarMensajeSinRegistros(datosRegistros)){
                return;
            }
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


    private  void reimprimirIngreso(DatosRegistro conte) {
        // Mostrar el mensaje "Imprimiendo"
        mensajeImprimiendo(true);

        try {
            // Conectar con la impresora
            impresoraPos.conectar();

            // Imprimir el recibo
            generarReciboReimpresoIngreso(conte);

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
    private void generarReciboReimpresoIngreso(DatosRegistro datos) {
        DatosSesion datosSesion = GlobalPermisos.getDatosSesionActual();
        String promotor= datos.getPromotorIngreso();

        if (datos == null) {
            Toast.makeText(this, "No hay contenido para imprimir", Toast.LENGTH_SHORT).show();
        }
        try {

            String placa = datos.getPlaca();
            //crearCodigoBarras(placa);
            impresoraPos.print(datosSesion.getLema(), true, false,false, false);
            impresoraPos.addEnter();
            impresoraPos.print(datosSesion.getNombreParaTiquete(), true, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print(("Nit: " + datosSesion.getNit()) ,true, true, false, false);
            impresoraPos.addEnter();
            impresoraPos.addEnter();
            impresoraPos.print("REIMPRESO", true, true, true, false);
            impresoraPos.addEnter();
            impresoraPos.print("ENTRADA", true, true, true, false);
            impresoraPos.addEnter();
            impresoraPos.addEnter();
            impresoraPos.print("No: " + datos.getId(), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Placa: " + datos.getPlaca(), false, true, true, false);
            impresoraPos.addEnter();
            impresoraPos.print("Reimpreso:" +
                    Configuracion.getFechaHoraActualAMPM(), false, true, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Por: " + GlobalPermisos.getDatosSesionActual().getNombrePromotor(), false, true, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Vehiculo: " + (datos.getContenido().getBoolean("esCarro") ? "Carro" : "Moto"), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Inicia: " + Configuracion.getFechaHora(datos.getIngreso()), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Zona: " + datos.getNombreZona(), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Horario: " +
                    Configuracion.getHoraAMPM(datos.getContenido().getString("hiniciaZona")) +
                    "- " +
                    Configuracion.getHoraAMPM(datos.getContenido().getString("hterminaZona")), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Valor hora-fraccion: $" + datos.getContenido().getLong("valorH"), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Promotor ingreso: " + promotor, false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Terminos y condiciones", true, true, false, false);
            impresoraPos.addEnter();
            impresoraPos.printQrCode(impresoraPos.generarCodigoQR("https://somosmovilidad.gov.co/zonas-de-estacionamiento-regulado-z-e-r-de-rionegro/"));

            impresoraPos.print("ATENCION", true, true, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Solicita siempre tu recibo de salida o tu pago no sera aplicado.", false, true, false, false);
            impresoraPos.addEnter();
            impresoraPos.addEnter();
            impresoraPos.addEnter();
            DatabaseHelper dbHelper = SelectScanner.getDatabaseHelper();
           // dbHelper.incrementarReimpresoPostpago();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    private  void reimprimirEgreso(DatosRegistro conte) {
        // Mostrar el mensaje "Imprimiendo"
        mensajeImprimiendo(true);

        try {
            // Conectar con la impresora
            impresoraPos.conectar();

            // Imprimir el recibo
            generarReciboReimpresoEgreso(conte);

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
    private void generarReciboReimpresoEgreso(DatosRegistro datos) {
        DatosSesion datosSesion = GlobalPermisos.getDatosSesionActual();
        String promotor= datos.getPromotorEgreso();

        if (datos == null) {
            Toast.makeText(this, "No hay contenido para imprimir", Toast.LENGTH_SHORT).show();
        }
        try {

            String placa = datos.getPlaca();
            //crearCodigoBarras(placa);
            impresoraPos.print(datosSesion.getLema(), true, false,false, false);
            impresoraPos.addEnter();
            impresoraPos.print(datosSesion.getNombreParaTiquete(), true, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print(("Nit: " + datosSesion.getNit()) ,true, true, false, false);
            impresoraPos.addEnter();
            impresoraPos.addEnter();
            impresoraPos.print("REIMPRESO", true, true, true, false);
            impresoraPos.addEnter();
            impresoraPos.print("SALIDA", true, true, true, false);
            impresoraPos.addEnter();
            String titulo = "PAGO REALIZADO";
            if (datos.getContenido().getLong("estado") == 2) {
                long minutosGracia = datos.getContenido().getLong("minutosGraciaZona");
                titulo = minutosGracia + " MINUTOS-GRATIS";
            }
            if (datos.getContenido().getLong("estado") == 4) {
                titulo = "PAGO-EXTEMPORANEO";
            }
            if (datos.getContenido().getLong("estado") == 3) {
                titulo = "SIN PAGAR Y REPORTADO";
            }
            impresoraPos.print(titulo, true, true, true, false);
            impresoraPos.addEnter();
            impresoraPos.addEnter();
            impresoraPos.print("No: " + datos.getId(), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Placa: " + datos.getPlaca(), false, true, true, false);
            impresoraPos.addEnter();
            impresoraPos.print("Reimpreso:" +
                    Configuracion.getFechaHoraActualAMPM(), false, true, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Por: " + GlobalPermisos.getDatosSesionActual().getNombrePromotor(), false, true, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Vehiculo: " + (datos.getContenido().getBoolean("esCarro") ? "Carro" : "Moto"), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Inicia: " + Configuracion.getFechaHora(datos.getIngreso()), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Termina: " + Configuracion.getFechaHora(datos.getEgreso()), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Tiempo: " + Configuracion.getHorasMinutosTranscurridos(datos.getContenido().getString("fhingreso"), datos.getContenido().getString("fhegreso")), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Paga: " + Configuracion.getFechaHora(datos.getContenido().getString("fhrecaudo")), false, true, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Horas cobradas: " + datos.getContenido().getLong("hcobradas") + (datos.getContenido().getLong("hcobradas") == 1 ? " hora" : " horas"), false, false, false, false);
            impresoraPos.addEnter();
            if (datos.getContenido().getLong("estado") == 3) {
                impresoraPos.print("Valor adeudado: $" + datos.getContenido().getString("valorCobrado"), false, true, true, false);
            } else {
                impresoraPos.print("Valor pagado: $" + datos.getContenido().getString("valorCobrado"), false, true, true, false);
            }
            impresoraPos.addEnter();
            impresoraPos.print("Zona: " + datos.getNombreZona(), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Horario: " +
                    Configuracion.getHoraAMPM(datos.getContenido().getString("hiniciaZona")) +
                    "- " +
                    Configuracion.getHoraAMPM(datos.getContenido().getString("hterminaZona")), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Valor hora-fraccion: $" + datos.getContenido().getLong("valorH"), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Promotor egreso: " + promotor, false, false, false, false);
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
            DatabaseHelper dbHelper = SelectScanner.getDatabaseHelper();
          //  dbHelper.incrementarReimpresoEgreso();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    private  void reimprimirPrepago(DatosRegistro conte) {
        // Mostrar el mensaje "Imprimiendo"
        mensajeImprimiendo(true);

        try {
            // Conectar con la impresora
            impresoraPos.conectar();

            // Imprimir el recibo
            generarReciboReimpresoPrepago(conte);

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
    private void generarReciboReimpresoPrepago(DatosRegistro datos) {
        DatosSesion datosSesion = GlobalPermisos.getDatosSesionActual();
        String promotor= datos.getPromotorIngreso();

        if (datos == null) {
            Toast.makeText(this, "No hay contenido para imprimir", Toast.LENGTH_SHORT).show();
        }
        try {

            String placa = datos.getPlaca();
            //crearCodigoBarras(placa);
            impresoraPos.print(datosSesion.getLema(), true, false,false, false);
            impresoraPos.addEnter();
            impresoraPos.print(datosSesion.getNombreParaTiquete(), true, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print(("Nit: " + datosSesion.getNit()) ,true, true, false, false);
            impresoraPos.addEnter();
            impresoraPos.addEnter();
            impresoraPos.print("REIMPRESO", true, true, true, false);
            impresoraPos.addEnter();
            impresoraPos.print("PREPAGO", true, true, true, false);
            impresoraPos.addEnter();
            impresoraPos.addEnter();
            impresoraPos.print("No: " + datos.getId(), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Placa: " + datos.getPlaca(), false, true, true, false);
            impresoraPos.addEnter();
            impresoraPos.print("Reimpreso:" +
                    Configuracion.getFechaHoraActualAMPM(), false, true, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Por: " + GlobalPermisos.getDatosSesionActual().getNombrePromotor(), false, true, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Vehiculo: " + (datos.getContenido().getBoolean("esCarro") ? "Carro" : "Moto"), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Inicia: " + Configuracion.getFechaHora(datos.getIngreso()), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Termina: " + Configuracion.getFechaHora(datos.getEgreso()), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Horas prepagadas: " + datos.getContenido().getLong("hcobradas"), false, false, false, false);
            impresoraPos.print(datos.getContenido().getLong("hcobradas") == 1 ? " hora" : " horas", false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Valor pagado: $" + datos.getContenido().getString("valorCobrado"), false, true, true, false);
            impresoraPos.addEnter();
            impresoraPos.print("Zona: " + datos.getNombreZona(), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Horario: " +
                    Configuracion.getHoraAMPM(datos.getContenido().getString("hiniciaZona")) +
                    "- " +
                    Configuracion.getHoraAMPM(datos.getContenido().getString("hterminaZona")), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Valor hora-fraccion: $" + datos.getContenido().getLong("valorH"), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Promotor ingreso: " + promotor, false, false, false, false);
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
            DatabaseHelper dbHelper = SelectScanner.getDatabaseHelper();
            //dbHelper.incrementarReimpresoPrepago();

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
                    "¿Desea intentarlo nuevamente para generar el tiquete solicitado?",
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

    @Override
    public void respuestaJSON(String accion, int statusCode, Header[] headers, byte[] responseBytes, String responseString, JSONObject responseJSON, JSONArray responseArrayJSON) {
        switch (accion) {
            case DuplicadosAPI.Acciones.SOLICITAR_MOVIMIENTOS_PLACA:
                pintarListado(responseArrayJSON);
                setDialog(false);
                break;
        }
    }

    @Override
    public void errorJSON(String accion, int statusCode, Header[] headers, byte[] responseBytes, String responseString, JSONObject JSONResponse, JSONArray JSONArrayResponse, Throwable throwable) {
        switch (accion) {
            case DuplicadosAPI.Acciones.SOLICITAR_MOVIMIENTOS_PLACA:
                setDialog(false);
                break;
        }
    }

    @Override
    public void termino() {

    }

    @Override
    public void botonReimprimirIngreso(DatosRegistro datosRegistro) {
        confirmarReimprimirIngreso(datosRegistro);
    }

    @Override
    public void botonReimprimirEgreso(DatosRegistro datosRegistro) {
        confirmarReimprimirEgreso(datosRegistro);
    }

    @Override
    public void botonReimprimirPrepago(DatosRegistro datosRegistro) {
        confirmarReimprimirPrepago(datosRegistro);
    }

    @Override
    public void respuestaSI(String accion, Object contenido) {
        switch (accion) {
            case EstadosDuplicados.INGRESO:
                setDialog(false);
                reimprimirIngreso((DatosRegistro) contenido);
                break;
            case EstadosDuplicados.EGRESO:
                setDialog(false);
                reimprimirEgreso((DatosRegistro) contenido);
                break;
            case EstadosDuplicados.PREPAGO:
                setDialog(false);
                reimprimirPrepago((DatosRegistro) contenido);
                break;
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

    @Override
    public void impresionExitosa() {
        mensajeImprimiendo(false);
        salir();
    }

    @Override
    public void impresionFallo(ConectorImpresoraBT conectorImpresoraBT) {
        mensajeImprimiendo(false);
        salir();
        //mensajeReimprimir(conectorImpresoraBT);
    }

    public static class EstadosDuplicados {
        public static final String INGRESO = "INGRESO";
        public static final String EGRESO = "EGRESO";
        public static final String PREPAGO = "PREPAGO";
    }

}
