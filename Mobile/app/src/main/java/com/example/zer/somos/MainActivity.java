package com.example.zer.somos;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import com.example.zer.somos.autenticar.Autenticar;
import com.example.zer.somos.autenticar.AutenticarAPI;
import com.example.zer.somos.autenticar.EncodeService;
import com.example.zer.somos.comunes.ActivityBase;
import com.example.zer.somos.comunes.DatabaseHelper;
import com.example.zer.somos.comunes.IRespuestaConfirmar;
import com.example.zer.somos.comunes.MensajeAceptar;
import com.example.zer.somos.comunes.MensajeConfirmar;
import com.example.zer.somos.comunes.ReinicioBaseDeDatosReceiver;
import com.example.zer.somos.comunicacion.IRespuestaHttpCliente;
import com.example.zer.somos.operaciones.BuscarPorPlacaActivity;
import com.example.zer.somos.operaciones.MenuActivity;
import com.example.zer.somos.permisos.GlobalPermisos;
import com.example.zer.somos.permisos.PermisosActivity;
import com.example.zer.somos.utilidades.Configuracion;
import com.example.zer.somos.utilidades.qrCodeScanner.ScanCaptureAct;
import com.google.gson.Gson;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity  extends ActivityBase implements IRespuestaHttpCliente, IRespuestaConfirmar {

    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private final int PUEDE_CONTNUAR = 101;
    private EditText usuario;
    private EditText clave;
    private Button ingresar;
    private Button salir;
    public Button qrCodeButton;
    private AutenticarAPI autenticarAPI;
    private AlertDialog.Builder builder;
    private Dialog dialog;
    private long mLastClickTime;
    private Runnable runnableValidarContenido;
    private JSONObject datosAcceso;
    private boolean estadoProcesoQR;
    private Long ultimoTiempo;
    private Intent intentPermisos;
    public String jsonQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    250);
        }

        try {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            requestCamera();
            initComponents();
            programarEventos();
            ocultarTeclado();
            String newString;

            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if(extras == null) {
                    newString= null;
                } else {
                    newString= extras.getString("qrCode");
                    procesarIngreso(newString);
                }
            } else {
                newString= (String) savedInstanceState.getSerializable("qrCode");
                procesarIngreso(newString);
            }
       /* androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder1.setTitle("Resultado:");
        Bundle extras = getIntent().getExtras();
        builder1.setMessage(newString);
        builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          dialog.dismiss();
         }
        }).show();*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    private void requestCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void startCamera() {

    }


    @Override
    protected void salirPantalla() {
        mensajeConfirmarDatos();
    }

    /**
     * Dado el contenido string del JSON que se extrajo de
     * procesar el QR, el sistema coloca los datos de
     * usurio y clave para procesar el ingreso
     *
     * @param JSON
     */private void validarIngresoPorQR(String JSON) {
        try {
            limpiarCamposUI();
            final Gson gson = new Gson();
            Configuracion.DatosQR datosQR = gson.fromJson(JSON, Configuracion.DatosQR.class);
            iniciarIngreso(datosQR.getU(), datosQR.getC());
            jsonQR = JSON;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void scanQRCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Usa la tecla Volumen para activar el Flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(false);
        options.setCaptureActivity(ScanCaptureAct.class);
        barLauncher.launch(new ScanOptions());
    }
    private final ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(),
            result -> {
        if(result.getContents() != null){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
            builder1.setTitle("Resultado:");
            builder1.setMessage(result.getContents());
            builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
        else {
            Toast toast =Toast.makeText(this, "Actividad Scanner Fallida: "+ result, Toast.LENGTH_LONG);
            toast.show();
        }
    });


    /**
     * se envía el texto que se está ingresando y el sistema
     * determna si es un contenido de QR valido porque inicia y termina
     * _@@ a demás que su contenido debe ser mayor a 64 caracteres
     *
     * @param contenido
     */
    private void procesarIngreso(String contenido) {
        try {
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
                        detenerQR();
                        validarIngresoPorQR(result);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * habilta el regstro por QR
     */
    private void reIniciarQR() {
        estadoProcesoQR = false;
    }

    /**
     * Deshabilita el regisyro por QR
     */
    private void detenerQR() {
        estadoProcesoQR = true;
    }

    /**
     * Retorna si se detiene o no el procesamiento
     * de datos QR.
     *
     * @return
     */
    private boolean getDetenerAccesoQR() {
        return estadoProcesoQR;
    }

    /**
     * Mediante un hilo se procesa el texto que se está
     * ingresando en los campos de usuario y clave, en éste
     * se reciben los contenidos y se hace un hilo para enviarlos
     * al procesamiento de contenido y determinar si es un
     * contenido de ingreso válido.
     *
     * @param contenido
     */
    private void ingresoTextoUsuarioClave(String contenido) {
        try {
            if (getDetenerAccesoQR()) {
                return;
            }
            runnableValidarContenido = new Runnable() {
                public void run() {
                    procesarIngreso(contenido);
                }
            };
            runnableValidarContenido.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void controlarIngresoManualQR(String contenido, Long tiempo) {
        try {
            if (tiempo - ultimoTiempo > Configuracion.QR.getLimiteVelocidadLectura()) {
                if (contenido.length() > Configuracion.QR.getLimiteCaracteresIngresoManualQR() && contenido.contains(Configuracion.QR.getSeparadorQR())) {
                    limpiarCamposUI();
                }
            }
            ultimoTiempo = tiempo;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Se registran todos los eventos del sistema
     */
    private void programarEventos() {
        try {
            /**
             * Evento que detecta click para abrir la cámara para el QR
             */
            qrCodeButton.setOnClickListener(v -> {
                Intent intent = new Intent(this, ScanCaptureAct.class);
                finish();
                startActivity(intent);
            });

            /**
             * Evento que detecta el click en el botón ingresar
             */
            ingresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    iniciarIngreso();
                }
            });


            /**
             * Evento que detecta el click en el botón salir
             */
            salir.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    salirPantalla();
                    return false;
                }
            });

            /**
             * Se detecta el ingreso de caracteres en el campo de usuario
             * para enviarlo a procesar pos si se trata de una cadena válida
             * para procesar ingreso por contenido de un QR
             */
            usuario.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    controlarIngresoManualQR(s.toString(), System.nanoTime());
                    ingresoTextoUsuarioClave(s.toString());
                }
            });

            /**
             * Con este evento se controla el menú de seleccionar, cortar y copiar
             * para evitar el copiado de los datos de acceso
             */
            usuario.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                public void onDestroyActionMode(ActionMode mode) {
                }

                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }
            });

            /**
             * Se monitorea el contenido ingresado en la clave y se envía a
             * procesar, para determinar si se trata de un acceso por datos
             * QR.
             */
            clave.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    controlarIngresoManualQR(s.toString(), System.nanoTime());
                    ingresoTextoUsuarioClave(s.toString());
                }
            });

            /**
             * Con este evento se controla el menú de seleccionar, cortar y copiar
             * para evitar el copiado de los datos de acceso
             */
            clave.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                public void onDestroyActionMode(ActionMode mode) {
                }

                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Se inicializan las instacias y los
     * estados de los elementos del sistema
     */
    private void initComponents() {
        try {
            intentPermisos = new Intent(this, PermisosActivity.class);
            GlobalPermisos.limpiarTodo();
            reIniciarQR();
            usuario = findViewById(R.id.usuario);
            usuario.setLongClickable(false);
            clave = findViewById(R.id.clave);
            clave.setLongClickable(false);
            ingresar = findViewById(R.id.ingresar);
            salir = findViewById(R.id.salir);
            qrCodeButton = findViewById(R.id.qrCodeButton);
            autenticarAPI = new AutenticarAPI();
            datosAcceso = new JSONObject();
            ultimoTiempo = System.nanoTime();

            builder = new AlertDialog.Builder(this);
            builder.setView(R.layout.progress);
            builder.setCancelable(false);
            dialog = builder.create();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setDialog(boolean show) {
        try {
            if (dialog == null) {
                return;
            }
            if (show) dialog.show();
            else dialog.dismiss();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void ocultarTeclado() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(usuario.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(clave.getWindowToken(), 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Este es un método sobre cargado que hace uso de
     * los datos digitados en el campo usuario y clave
     * para validar el ingreso al sistema
     */
    private void iniciarIngreso() {
        try {
            iniciarIngreso(usuario.getText().toString(), clave.getText().toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Este método inicia la validación de los datos,
     * a él debe llegar el usuario y la clave con la
     * cual se pretende ingresar al sistema.
     *
     * @param usuario
     * @param clave
     */
    private void iniciarIngreso(String usuario, String clave) {
        try {
            setDialog(true);
            GlobalPermisos.limpiarTodo();
            setDatosAcceso(usuario, clave);
            autenticarAPI.ingresoInicioSolicitud(this, this, AutenticarAPI.Acciones.INICIO_SOLICITUD);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Se colocan los datos de acceso clave y conraseña en un
     * objeto JSON que es usado posteriomente para validar
     * los contenidos
     *
     * @param usuario
     * @param clave
     */
    private void setDatosAcceso(String usuario, String clave) {
        try {
            datosAcceso.put("usuario", usuario);
            datosAcceso.put("clave", clave);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void seConfirmaSalirPantalla() {
        try {
            System.exit(0);
            super.salirPantalla();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getUsuarioClave() {
        String result = "";
        try {
            result = datosAcceso.toString();
            datosAcceso = new JSONObject();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private Map<String, Object> generarDataDiffie(long p, long g, long b) {
        Map<String, Object> res = new HashMap<>();
        try {
            Autenticar autenticar = new Autenticar();
            res = autenticar.getAuthCompletar(p, g, b);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }

    private void completarIngreso(JSONObject response) {
        try {
            setDialog(true);
            Map<String, Object> diff = generarDataDiffie(response.getLong("p"), response.getLong("g"), response.getLong("b"));
            String jsonConte = getUsuarioClave();
            EncodeService encodeService = new EncodeService();
            String contentAuth = encodeService.encriptar(jsonConte, diff.get("claveHash").toString());
            autenticarAPI.ingresoCompletarSolicitud(
                    this,
                    this,
                    AutenticarAPI.Acciones.COMPLETAR_SOLICITUD,
                    response.getString("idSolicitud"),
                    diff.get("A").toString(),
                    contentAuth);
        } catch (Exception ex) {
            ex.printStackTrace();
            setDialog(false);
        }
    }

    private void ingresoExitoso(JSONArray response) {
        try {
            limpiarCamposUI();
            reIniciarQR();
            GlobalPermisos.setCuentasJSONArray(response);
            permitirSalir();
            startActivity(intentPermisos);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        setDialog(false);
    }

    private void ingresoFallo() {
        try {
            reIniciarQR();
            limpiarCamposUI();
            DialogFragment newFragment = new MensajeAceptar(
                    "No se pudo ingresar al sistema.\n" +
                            "Inténtelo nuevamente :)", "Aceptar",
                    MensajeAceptar.Colores.AMARILLO);
            newFragment.show(getSupportFragmentManager(), "OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void limpiarCamposUI() {
        try {
            clave.setText("");
            usuario.setText("");
            usuario.requestFocus();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void mensajeConfirmarDatos() {
        try {

            MensajeConfirmar mensajeConfirmar = new MensajeConfirmar(
                    "¿Desea cerrar la aplicación?",
                    "SI",
                    "NO",
                    "Confirmar salir del sistema",
                    this
            );
            mensajeConfirmar.setCancelable(false);
            mensajeConfirmar.show(getSupportFragmentManager(), "OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void respuestaJSON(String accion,
                              int statusCode,
                              Header[] headers,
                              byte[] responseBytes,
                              String responseString,
                              JSONObject responseJSON,
                              JSONArray responseArrayJSON) {
        switch (accion) {
            case AutenticarAPI.Acciones.INICIO_SOLICITUD:
                completarIngreso(responseJSON);
                //setDialog(false);
                break;
            case AutenticarAPI.Acciones.COMPLETAR_SOLICITUD:
                ingresoExitoso(responseArrayJSON);
                //setDialog(false);
                break;
        }
    }

    @Override
    public void errorJSON(String accion,
                          int statusCode,
                          Header[] headers,
                          byte[] responseBytes,
                          String responseString,
                          JSONObject JSONResponse,
                          JSONArray JSONArrayResponse,
                          Throwable throwable) {
        switch (accion) {
            case AutenticarAPI.Acciones.COMPLETAR_SOLICITUD:
            case AutenticarAPI.Acciones.INICIO_SOLICITUD:
                ingresoFallo();
                setDialog(false);
                break;
        }
    }

    @Override
    public void termino() {
    }

    @Override
    public void respuestaSI() {
        seConfirmaSalirPantalla();
    }

    @Override
    public void respuestaNO() {

    }
}