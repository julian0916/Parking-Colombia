package com.example.zer.somos.operaciones;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.example.zer.somos.R;
import com.example.zer.somos.autenticar.EncodeService;
import com.example.zer.somos.comunes.*;
import com.example.zer.somos.comunicacion.IRespuestaHttpCliente;
import com.example.zer.somos.permisos.GlobalPermisos;
import com.example.zer.somos.permisos.PermisosAPI;
import com.example.zer.somos.supervision.ListarBloqueados;
import com.example.zer.somos.supervision.SupervisionAPI;
import com.example.zer.somos.supervision.SupervisionActivity;
import com.example.zer.somos.utilidades.Configuracion;
import com.example.zer.somos.utilidades.HacerVibrar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import cz.msebera.android.httpclient.Header;
import papaya.in.sendmail.SendMail;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MenuActivity extends ActivityBase
        implements
        IRespuestaHttpCliente,
        IRespuestaConfirmar,
        IRespuestaConfirmarAccion {

    private final int INTERVALO_TEMPORIZADO = 1000 * 60 * 15; // 15 minutos
    private final int INTERVALO_VIBRAR_SI_ALERTA_TEMPORIZADO = 1000 * 60 * 10; // 10 minutos
    private PermisosAPI permisosAPI;
    private SupervisionAPI supervisionAPI;
    private TextView promotor;
    private TextView zona;
    private TextView cuenta;
    private TextView zona_titulo;
    private TextView cuenta_titulo;
    private DatosSesion datosSesion;
    private Button informacion;
    private Button informacion_logo;
    private Button ventaPrepago;
    private Button ventaPrepago_logo;
    private Button botonPanico;
    private Button ventaPostpago;
    private Button ventaPostpago_logo;
    private Button buscarPorPlaca;
    private Button buscarPorPlaca_logo;
    private Button listarVigentes;
    private Button listarVigentes_logo;
    private Button duplicados;
    private Button duplicados_logo;
    private Button supervision;
    private Button supervision_logo;
    private Button estadistica;
    private Button estadistica_logo;
    private Button bloqueados_btn;
    private Button salir;
    private Button alertas;
    private TextView tituloPerfil;
    private MenuAPI menuAPI;
    private Handler handler;
    private Runnable runnable;
    private HacerVibrar hacerVibrar;
    private Handler handlerVibrar;
    private Runnable runnableVibrar;
    private CodeScanner codeScanner;
    private int longClickDuration = 10000;
    private boolean isLongPress = false;
    private static final long LONG_PRESS_DURATION = 7000; // 7 segundos para activar boton panico
    private static final long DISABLE_DURATION = 10000; // 10 segundos para deshabilitar boton panico
    private MediaPlayer mediaPlayer;
    private boolean estadoProcesoQR;
    private Runnable runnableValidarContenido;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            DatabaseHelper dbHelper = SelectScanner.getDatabaseHelper();
            SQLiteDatabase db = dbHelper.getWritableDatabase();

           // dbHelper.validarYCrearUsuario(GlobalPermisos.getDatosSesionActual().getIdPromotor());

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_menu);
            iniciarComponentes();
            ventaPostpago.setVisibility(View.GONE);
            ocultarMostrarMenuSupervisor();
            cargarTitulos();
            programarEventos();
            programarTemporizador();
            programarTemporizadorVibrador();
            solicitarAlertasServidor();
            vibrarSiAlertas();
            validarLimiteDeudaPromotor();
            validarBloqueoPromotor();
            setupPanicoButton();
            mediaPlayer = MediaPlayer.create(this, R.raw.panic_sound);
            //validarUsuarioCongelado();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }


    private void playPanicSound() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    private void stopPanicSound() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
        }
    }

//    private void validarUsuarioCongelado() {
//
//        DatosSesion datosSesion = GlobalPermisos.getDatosSesionActual();
//        DatabaseHelper dbHelper = SelectScanner.getDatabaseHelper();
//        dbHelper.reiniciarValoresSiNecesario();
//
//        boolean validacion = dbHelper.consultarFreeze(datosSesion.getIdPromotor());
//
//        if(validacion){
//
//            final EditText passwordEditText = new EditText(this);
//            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Su usuario se encuentra congelado, por favor ingrese la contraseña: ");
//            builder.setView(passwordEditText);
//            builder.setCancelable(false); // Hacer que el AlertDialog no sea cancelable
//            final String[] contrasenasAdicionales = {"@Zer-1298.", ".Super!5633@", "@.9916Cdu", "!D7z3u2@"};
//            final String[] mensajesAviso = {"Contraseña 1 usada", "Contraseña 2 usada", "Contraseña 3 usada", "Contraseña 4 usada"};
//            builder.setPositiveButton("Ingresar", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    String enteredPassword = passwordEditText.getText().toString();
//                    int contrasenaUsada = -1; // Inicializar a un valor no válido
//                    // Verificar qué contraseña se usó
//                    for (int i = 0; i < contrasenasAdicionales.length; i++) {
//                        if (contrasenasAdicionales[i].equals(enteredPassword)) {
//                            contrasenaUsada = i;
//                            break;
//                        }
//                    }
//                    if (contrasenaUsada != -1) {
//                        String mensajeAEnviar = mensajesAviso[contrasenaUsada];
//                        // Contraseña válida, actualizar freeze y enviar aviso
//                        dbHelper.actualizarFreeze(false);
//                        enviarEmail(mensajeAEnviar);
//
//
//                    } else {
//                        // Contraseña incorrecta
//                        salirPantalla();
//                    }
//
//                    dialog.dismiss();
//                }
//            });
//
//// Mostrar el AlertDialog
//            AlertDialog dialog = builder.create();
//            dialog.show();
//        }
//    }

    @Override
    protected void onRestart() {
        try {
            super.onRestart();
            solicitarAlertasServidor();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == AlertasPrepagoActivity.CODIGO_RESPUESTA) {
                if (resultCode == AlertasPrepagoActivity.CODIGO_RESULTADO_INICIAR_POSTPAGO) {
                    Thread thrd = new Thread() {
                        public void run() {
                            long mLastClickTime = SystemClock.elapsedRealtime();
                            while (SystemClock.elapsedRealtime() - mLastClickTime < 100) {
                            }

                            abrirPostpagoConPlaca(data.getStringExtra(AlertasPrepagoActivity.PROCESAR_PLACA));
                        }
                    };
                    thrd.start();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void ocultarMostrarMenuSupervisor() {
        try {
            if (GlobalPermisos.getDatosSesionActual().getIdPerfil() != GlobalPermisos.Perfiles.SUPERVISOR) {
                supervision.setVisibility(View.GONE);
                supervision_logo.setVisibility(View.GONE);
                estadistica.setVisibility(View.GONE);
                estadistica_logo.setVisibility(View.GONE);
                bloqueados_btn.setVisibility(View.GONE);

            }
            if (GlobalPermisos.getDatosSesionActual().getIdPerfil() == GlobalPermisos.Perfiles.SUPERVISOR) {
                informacion.setVisibility(View.GONE);
                informacion_logo.setVisibility(View.GONE);
                botonPanico.setVisibility(View.GONE);
                tituloPerfil.setVisibility(View.GONE);
                promotor.setVisibility(View.GONE);
                zona_titulo.setVisibility(View.GONE);
                cuenta_titulo.setVisibility(View.GONE);
                cuenta.setVisibility(View.GONE);
                zona.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void abrirPostpagoConPlaca(String placa) {
        try {
            abrirActivity(PostpagoActivity.class, PostpagoActivity.ABRIR_CON_PLACA, placa);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
   /* private void reIniciarQR() {
        estadoProcesoQR = false;
    }*/

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
    /*private boolean getDetenerAccesoQR() {
        return estadoProcesoQR;
    }

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

    private void validarIngresoPorQR(String JSON) {
        try {
            //limpiarCamposUI();
            final Gson gson = new Gson();
            Configuracion.DatosQR datosQR = gson.fromJson(JSON, Configuracion.DatosQR.class);
            String datos = datosQR.getU() +datosQR.getC();
            Toast.makeText(this, datos, Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Exito", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } */


    private void programarEventos() {
        try {
            salir.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mensajePreguntarSalir();
                    return false;
                }
            });
            alertas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    abrirMostrarAlertas();
                }
            });

//            botonPanico.setOnTouchListener(new View.OnTouchListener() {
//                boolean botonPrecionado = false;
//                 Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                 @Override
//                 public boolean onTouch(View v, MotionEvent event) {
//                     vibrator.vibrate(1000);
//                     if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                         Handler handler = new Handler();
//                         handler.postDelayed(new Runnable() {
//                             @Override
//                             public void run() {
//                                 if (isLongPress) {
//                                        botonPrecionado= true;
//                                        botonPanico.setVisibility(View.GONE);
//                                        botonPanico.setEnabled(false);
//                                        botonPanico.postDelayed(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                if (!isDestroyed() && !isFinishing()) {
//                                                    botonPanico.setVisibility(View.VISIBLE);
//                                                    botonPanico.setEnabled(true);
//                                                    if (botonPrecionado) {
//                                                        setBotonPanico();
//                                                        botonPrecionado = false;
//                                                    }
//                                                }
//                                            }
//                                 },7000);
//                                 }
//                             }
//                         }, longClickDuration);
//                         isLongPress = true;
//                     } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                         isLongPress = false;
//                         botonPrecionado = false;
//                     }
//                     return true;
//                 }
//             });
            informacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mensajeInformacionActual();
                }
            });
            informacion_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mensajeInformacionActual();
                }
            });
            ventaPrepago.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    abrirVentaPrepago();
                }
            });
            ventaPrepago_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    abrirVentaPrepago();
                }
            });
            ventaPostpago.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    abrirVentaPostpago();
                }
            });
            ventaPostpago_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    abrirVentaPostpago();
                }
            });
            buscarPorPlaca.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    abrirBuscarPorPlaca();
                }
            });
            buscarPorPlaca_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    abrirBuscarPorPlaca();
                }
            });
            listarVigentes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    abrirBuscarPorZona();
                }
            });
            listarVigentes_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    abrirBuscarPorZona();
                }
            });
            duplicados.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    abrirDuplicados();
                }
            });
            duplicados_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    abrirDuplicados();
                }
            });
            supervision.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    abrirSupervision();
                }
            });
            supervision_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    abrirSupervision();
                }
            });
            bloqueados_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view){
                    abrirActivity(ListarBloqueados.class);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void ocultarBotonPanico(){
        botonPanico.setVisibility(View.GONE);
        try {
           wait(5000);
            botonPanico.setVisibility(View.VISIBLE);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void abrirDuplicados() {
        try {
            abrirActivity(DuplicadosActivity.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void abrirSupervision() {
        try {
            abrirActivity(SupervisionActivity.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void programarTemporizadorVibrador() {
        try {
            if (handlerVibrar == null) {
                detenerTemporizadorVibrar();
                return;
            }
            runnableVibrar = new Runnable() {
                public void run() {
                    try {
                        if (handlerVibrar == null) {
                            detenerTemporizadorVibrar();
                            return;
                        }
                        vibrarSiAlertas();
                        handlerVibrar.postDelayed(runnableVibrar, INTERVALO_VIBRAR_SI_ALERTA_TEMPORIZADO);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };
            handlerVibrar.postDelayed(runnableVibrar, INTERVALO_VIBRAR_SI_ALERTA_TEMPORIZADO);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void vibrarSiAlertas() {
        try {
            if (hacerVibrar == null) {
                return;
            }
            if (GlobalPermisos.getListaAlertas() == null) {
                return;
            }
            if (GlobalPermisos.getListaAlertas().size() > 0) {
                hacerVibrar.vibrar();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void programarTemporizador() {
        try {
            if (handler == null) {
                detenerTemporizador();
                return;
            }
            runnable = new Runnable() {
                public void run() {
                    try {
                        if (handler == null) {
                            detenerTemporizador();
                            return;
                        }
                        solicitarAlertasServidor();
                        handler.postDelayed(runnable, INTERVALO_TEMPORIZADO);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };
            handler.postDelayed(runnable, INTERVALO_TEMPORIZADO);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void detenerTemporizador() {
        handler = null;
        runnable = null;
    }

    private void detenerTemporizadorVibrar() {
        handlerVibrar = null;
        runnableVibrar = null;
    }

    private void iniciarComponentes() {
        try {
            permisosAPI = new PermisosAPI();
            supervisionAPI = new SupervisionAPI();
            datosSesion = GlobalPermisos.getDatosSesionActual();
            menuAPI = new MenuAPI();
            promotor = findViewById(R.id.promotor);
            tituloPerfil = findViewById(R.id.tituloPerfil);
            zona = findViewById(R.id.zona);
            cuenta = findViewById(R.id.cuenta);
            informacion = findViewById(R.id.informacion);
            informacion_logo = findViewById(R.id.informacion_logo);
            ventaPrepago = findViewById(R.id.ventaPrepago);
            ventaPrepago_logo = findViewById(R.id.ventaPrepago_logo);
            ventaPostpago = findViewById(R.id.ventaPostpago);
            ventaPostpago_logo = findViewById(R.id.ventaPostpago_logo);
            buscarPorPlaca = findViewById(R.id.buscarPorPlaca);
            buscarPorPlaca_logo = findViewById(R.id.buscarPorPlaca_logo);
            listarVigentes = findViewById(R.id.listarVigentes);
            listarVigentes_logo = findViewById(R.id.listarVigentes_logo);
            duplicados = findViewById(R.id.duplicados);
            duplicados_logo = findViewById(R.id.duplicados_logo);
            supervision = findViewById(R.id.supervision);
            supervision_logo = findViewById(R.id.supervision_logo);
            estadistica = findViewById(R.id.estadistica);
            estadistica_logo = findViewById(R.id.estadistica_logo);
            bloqueados_btn = findViewById(R.id.bloqueados_btn);
            zona_titulo = findViewById(R.id.textView3);
            cuenta_titulo = findViewById(R.id.textView6);

            botonPanico = findViewById(R.id.botonPanico);
            salir = findViewById(R.id.salir);
            alertas = findViewById(R.id.alertas);
            handler = new Handler();
            handlerVibrar = new Handler();
            hacerVibrar = new HacerVibrar(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void cargarTitulos() {
        try {
            promotor.setText(datosSesion.getNombrePromotor());
            zona.setText(datosSesion.getNombreZona());
            cuenta.setText(datosSesion.getNombreCuenta());
            if (GlobalPermisos.getDatosSesionActual().getIdPerfil() == GlobalPermisos.Perfiles.SUPERVISOR) {
                tituloPerfil.setText("Supervisor");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void abrirVentaPrepago() {
        try {
            abrirActivity(PrepagoActivity.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void abrirVentaPostpago() {
        try {
            abrirActivity(PostpagoActivity.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void leerBarCode(){


    }

    private void abrirBuscarPorPlaca() {
        try {
            abrirActivity(BuscarPorPlacaActivity.class);
            //abrirActivity(ListarVigentesActivity.class,
            //        ListarVigentesActivity.Tipo.TIPO_BUSQUEDA,
            //        ListarVigentesActivity.Tipo.BUSCAR_POR_PLACA);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void abrirBuscarPorZona() {
        try {
            permitirSalir();
            Intent intent = new Intent(this, ListarVigentesActivity.class);
            startActivityForResult(intent, ListarVigentesActivity.CODIGO_RESPUESTA);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void abrirActivity(Class<?> clase) {
        try {
            permitirSalir();
            Intent intent = new Intent(this, clase);
            startActivity(intent);
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

    private void abrirActivity(Class<?> clase, String nombre, int valor) {
        try {
            permitirSalir();
            Intent intent = new Intent(this, clase);
            intent.putExtra(nombre, valor);
            startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setBotonPanico() {
        String contenidoMensaje = "botón pánico pulsado por: " + datosSesion.getNombrePromotor()
                + " en la zona: " + GlobalPermisos.getDatosSesionActual().getNombreZona();

            enviarEmail(contenidoMensaje);
            enviarSms(contenidoMensaje);

    }

    private void enviarSms(String mensaje){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            //smsManager.sendTextMessage("+57 3195687005" , null,
                    //mensaje, null, null);
           smsManager.sendTextMessage("+57 3102211374" , null, mensaje,
                   null, null);
            Toast.makeText(this, "Mensaje de texto enviado.", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, "Falló al enviar el mensaje de texto",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void enviarEmail(String mensaje){
       String[] correos = {"sistemas@somosmovilidad.gov.co", "coordinador.zer@somosmovilidad.gov.co", "sistemassomos@outlook.com"};
        //String[] correos = {"programador.somos@outlook.com", "steven-coro@hotmail.com",
               //"stevencoropsvita@gmail.com", "Developerstevenmarin@gmail.com"};
        try {
            for (int i=0; i<=2; i++){
                //SendMail mail = new SendMail("botonpanicozer@gmail.com",
                SendMail mail = new SendMail("name7@outlook.com",
                        //"@botonPanico2021",
                        "password",
                        correos[i],
                        "ALERTA",
                        mensaje
                );
                mail.execute();
               /* SendMail mail = new SendMail("zer.somos@outlook.com",
                        "S0m0s2017",
                        correos[i],
                        "ALERTA",
                        mensaje
                );
                mail.execute();*/
            }
            Toast.makeText(this, "Email enviado.", Toast.LENGTH_SHORT).show();
        }catch (Exception e ){
            Toast.makeText(this, "Falló al enviar el mensaje por email",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void abrirMostrarAlertas() {
        try {
            permitirSalir();
            Intent intent = new Intent(this, AlertasPrepagoActivity.class);
            startActivityForResult(intent, AlertasPrepagoActivity.CODIGO_RESPUESTA);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void colocarNumeroAlertas() {
        try {
            if (alertas == null) {
                return;
            }
            alertas.setText("");
            if (GlobalPermisos.getListaAlertas() == null) {
                return;
            }
            long tama = GlobalPermisos.getListaAlertas().size();
            if (tama < 1) {
                return;
            }
            alertas.setText("" + tama);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<DatosRegistro> getListadoAlertas(JSONArray jsonArray) {
        List<DatosRegistro> listadoAlertas = new ArrayList<>();
        try {
            if (jsonArray == null) {
                return listadoAlertas;
            }
            for (int pos = 0; pos < jsonArray.length(); pos++) {
                try {
                    DatosRegistro datosRegistro = new DatosRegistro(jsonArray.getJSONObject(pos));
                    listadoAlertas.add(datosRegistro);
                } catch (Exception ex) {
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listadoAlertas;
    }

    private void procesarAlertas(JSONArray jsonArray) {
        try {
            GlobalPermisos.setListaAlertas(getListadoAlertas(jsonArray));
            colocarNumeroAlertas();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void solicitarAlertasServidor() {
        try {
            menuAPI.solicitarAlertasZona(this,
                    this,
                    MenuAPI.Acciones.BUSCAR_ALERTAS,
                    GlobalPermisos.getDatosSesionActual().getCuenta(),
                    GlobalPermisos.getDatosSesionActual().getIdZona());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Muestra la información de la configuración actual de la zona
     */
    private void mensajeInformacionActual() {
        try {
            final String ENTER = "\n";
            StringBuilder contenidoMensaje=new StringBuilder();
            contenidoMensaje.append("Zona: ");
            contenidoMensaje.append(GlobalPermisos.getDatosSesionActual().getNombreZona());
            contenidoMensaje.append(ENTER);
            contenidoMensaje.append("Celdas carro: ");
            contenidoMensaje.append(GlobalPermisos.getDatosSesionActual().getCeldasCarro());
            contenidoMensaje.append(ENTER);
            contenidoMensaje.append("Celdas moto: ");
            contenidoMensaje.append(GlobalPermisos.getDatosSesionActual().getCeldasMoto());
            contenidoMensaje.append(ENTER);
            contenidoMensaje.append("Hora carro: $");
            contenidoMensaje.append(GlobalPermisos.getDatosSesionActual().getValorHoraCarro());
            contenidoMensaje.append(ENTER);
            contenidoMensaje.append("Hora moto: $");
            contenidoMensaje.append(GlobalPermisos.getDatosSesionActual().getValorHoraMoto());
            contenidoMensaje.append(ENTER);
            contenidoMensaje.append("Minutos de gracia: ");
            contenidoMensaje.append(GlobalPermisos.getDatosSesionActual().getMinutosGracia());
            contenidoMensaje.append(ENTER);
            contenidoMensaje.append("Minutos nueva gracia: ");
            contenidoMensaje.append(GlobalPermisos.getDatosSesionActual().getMinutosParaNuevaGracia());
            contenidoMensaje.append(ENTER);

            MensajeAceptar mensajeAceptar = new MensajeAceptar(
                    contenidoMensaje.toString(),
                    "Aceptar",
                    "Información",
                    MensajeAceptar.Colores.AZUL_CLARO
            );
            mensajeAceptar.setCancelable(false);
            mensajeAceptar.show(getSupportFragmentManager(), "OK");

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    private void mensajePreguntarSalir() {
        try {
            MensajeConfirmar mensajeConfirmar = new MensajeConfirmar(
                    "¿Desea salir del menú principal?",
                    "SI",
                    "NO",
                    "Confirmar salir del menú principal",
                    this
            );
            mensajeConfirmar.setCancelable(false);
            mensajeConfirmar.show(getSupportFragmentManager(), "OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }




    private void salir() {
        try {
            detenerTemporizador();
            detenerTemporizadorVibrar();
            finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void validarLimiteDeudaPromotor() {
        try {
            DatosSesion datosSesion = GlobalPermisos.getDatosSesionActual();
            if (datosSesion == null) {
                return;
            }
            permisosAPI.verificarLimiteEndeudamiento(this,
                    this,
                    PermisosAPI.Acciones.VERIFICAR_TOPE_ENDEUDAMIENTO,
                    datosSesion.getCuenta(),
                    datosSesion.getIdPromotor()
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void controlLimiteDeudaPromotor(JSONObject responseJSON) {
        try {
            if (responseJSON.getBoolean("superaLimiteDeuda")) {
                DialogFragment newFragment = new MensajeConfirmarAccion(
                        PermisosAPI.Acciones.VERIFICAR_TOPE_ENDEUDAMIENTO,
                        "Su dinero de los recaudos no está al día y por eso no puede usar el sistema.",
                        "Aceptar",
                        "",
                        "Debe presentarse en la oficina",
                        null,
                        this);
                newFragment.setCancelable(false);
                newFragment.show(getSupportFragmentManager(), "SUPERA_LIMIE_DEUDA");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void cerrarPorTopeEndeudamientoSuperado() {
        try {
            finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void validarBloqueoPromotor() {
        try {
            DatosSesion datosSesion = GlobalPermisos.getDatosSesionActual();
            if (datosSesion == null) {
                return;
            }
            permisosAPI.estaBloqueado(this,
                    this,
                    PermisosAPI.Acciones.ESTA_BLOQUEADO,
                    datosSesion.getCuenta(),
                    datosSesion.getIdPromotor()
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void verificarEstadoBloqueo(Boolean response) {
        try {
            if (response) {
                DialogFragment newFragment = new MensajeConfirmarAccion(
                        PermisosAPI.Acciones.ESTA_BLOQUEADO,
                        "Su usuario esta bloqueado.",
                        "Aceptar",
                        "",
                        "Debe presentarse en la oficina",
                        null,
                        this);
                newFragment.setCancelable(false);
                newFragment.show(getSupportFragmentManager(), "ESTA_BLOQUEADO");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void respuestaJSON(String accion, int statusCode, Header[] headers, byte[] responseBytes, String responseString, JSONObject responseJSON, JSONArray responseArrayJSON) {
        switch (accion) {
            case MenuAPI.Acciones.BUSCAR_ALERTAS:
                procesarAlertas(responseArrayJSON);
                break;
            case PermisosAPI.Acciones.VERIFICAR_TOPE_ENDEUDAMIENTO:
                controlLimiteDeudaPromotor(responseJSON);
                break;
            case PermisosAPI.Acciones.ESTA_BLOQUEADO:
            try {
                boolean estaBloqueado = Boolean.parseBoolean(responseString);
                verificarEstadoBloqueo(estaBloqueado);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al procesar la respuesta del servidor.", Toast.LENGTH_LONG).show();
            }
            break;
        }
    }

    @Override
    public void errorJSON(String accion, int statusCode, Header[] headers, byte[] responseBytes, String responseString, JSONObject JSONResponse, JSONArray JSONArrayResponse, Throwable throwable) {
        if (statusCode == 200 && responseString != null) {
            boolean estaBloqueado = Boolean.parseBoolean(responseString);
            verificarEstadoBloqueo(estaBloqueado);
            return;
        }
        switch (accion) {
            case MenuAPI.Acciones.BUSCAR_ALERTAS:
                break;
            case PermisosAPI.Acciones.VERIFICAR_TOPE_ENDEUDAMIENTO:
                break;
            case PermisosAPI.Acciones.ESTA_BLOQUEADO:
                break;
        }
    }

    @Override
    public void termino() {

    } 

    @Override
    public void respuestaSI() {
        salir();
    }

    @Override
    public void respuestaNO() {

    }

    @Override
    public void respuestaSI(String accion, Object contenido) {
        switch (accion) {
            case PermisosAPI.Acciones.VERIFICAR_TOPE_ENDEUDAMIENTO:
                cerrarPorTopeEndeudamientoSuperado();
                break;
            case PermisosAPI.Acciones.ESTA_BLOQUEADO:
                cerrarPorTopeEndeudamientoSuperado();
                break;
        }
    }

    @Override
    public void respuestaNO(String accion, Object contenido) {
        switch (accion) {
            case PermisosAPI.Acciones.VERIFICAR_TOPE_ENDEUDAMIENTO:
                cerrarPorTopeEndeudamientoSuperado();
                break;
        }
    }
    private void onLongPress() {
        setBotonPanico();
    }

    private void disableButtonForDuration(long duration) {
        botonPanico.setEnabled(false);
        botonPanico.setVisibility(View.GONE);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                botonPanico.setEnabled(true);
                botonPanico.setVisibility(View.VISIBLE);
            }
        }, duration);
    }

    private void setupPanicoButton() {
        botonPanico.setOnTouchListener(new View.OnTouchListener() {
            private Handler handler = new Handler(Looper.getMainLooper());
            private Runnable longPressRunnable = new Runnable() {
                @Override
                public void run() {
                    if (isLongPress) {
                        onLongPress();
                        disableButtonForDuration(DISABLE_DURATION);
                    }
                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isLongPress = true;
                        handler.postDelayed(longPressRunnable, LONG_PRESS_DURATION);
                        playPanicSound();
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        isLongPress = false;
                        handler.removeCallbacks(longPressRunnable);
                        v.performClick();
                        stopPanicSound();
                        return true;
                }
                return false;
            }
        });
    }

}