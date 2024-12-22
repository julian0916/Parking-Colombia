package com.example.zer.somos.permisos;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.example.zer.somos.R;
import com.example.zer.somos.comunes.ActivityBase;
import com.example.zer.somos.comunes.DatosSesion;
import com.example.zer.somos.comunes.IRespuestaConfirmar;
import com.example.zer.somos.comunes.MensajeAceptar;
import com.example.zer.somos.comunicacion.IRespuestaHttpCliente;
import com.example.zer.somos.operaciones.MenuActivity;
import com.example.zer.somos.utilidades.Configuracion;
import com.example.zer.somos.utilidades.ImpresoraPos;

import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PermisosActivity extends ActivityBase implements
        IRespuestaHttpCliente,
        IRespuestaConfirmar {
    private PermisosAPI permisosAPI;
    private Spinner cuentas;
    private Spinner zonas;
    private Button ingresar;
    private Button salir;
    private AlertDialog.Builder builderZona;
    private Dialog dialogZona;
    private AlertDialog.Builder builderTiquete;
    private Dialog dialogTiquete;
    private Intent intentMenuPrincipal;
    public JSONObject contenido;
    private ImpresoraPos impresoraPos;
    private String recaudoDiarioPromotor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_permisos);
            iniciarMensajes();
            iniciarDatos();
            validarPerfilInicial();
            iniciarComponentes();
            programarEventosClick();
            impresoraPos = new ImpresoraPos(this);
            DatosSesion datosSesion = GlobalPermisos.getDatosSesionActual();
            Toast.makeText(this, "Datos: "+ datosSesion, Toast.LENGTH_LONG);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        try {
            super.onRestart();
            validarSiEsUnicaZonaUnicaCuentaSalir();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void iniciarMensajes() {
        try {
            builderZona = new AlertDialog.Builder(this);
            builderZona.setView(R.layout.progress);
            builderZona.setCancelable(false);
            dialogZona = builderZona.create();

            builderTiquete = new AlertDialog.Builder(this);
            builderTiquete.setView(R.layout.progress);
            builderTiquete.setCancelable(false);
            dialogTiquete = builderTiquete.create();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void iniciarComponentes() {
        try {
            permisosAPI = new PermisosAPI();
            cuentas = this.findViewById(R.id.horas);
            zonas = this.findViewById(R.id.zonas);
            ingresar = this.findViewById(R.id.ingresar);
            salir = this.findViewById(R.id.salir);
            intentMenuPrincipal = new Intent(this, MenuActivity.class);
            pintarListadoCuentas();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setDialogZona(boolean show) {
        try {
            if (show) dialogZona.show();
            else dialogZona.dismiss();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setDialogTiquete(boolean show) {
        try {
            if (show) dialogTiquete.show();
            else dialogTiquete.dismiss();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void validarPerfilInicial() {
        try {
            if (GlobalPermisos.getListadoCuentasValidas() == null) {
                terminarUsuarioNoAutorizado();
            }
            if (GlobalPermisos.getListadoCuentasValidas().size() < 1) {
                terminarUsuarioNoAutorizado();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void terminarUsuarioNoAutorizado() {
        try {
            mensajePantalla("Debes ser un promotor o un supervisor",
                    "Cerrar",
                    "TERMINAR_NO_AUTORIZADO",
                    "Usuario no autorizado",
                    this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void iniciarDatos() {
        try {
            if (GlobalPermisos.getListadoCuentasValidas() == null) {
                GlobalPermisos.setListadoCuentasValidas(getCuentasValidas());
            }
            if (GlobalPermisos.getListaNombresCuentas() == null) {
                GlobalPermisos.setListaNombresCuentas(getNombreCuentas());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void pintarListadoCuentas() {
        try {
            cuentas.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.lista_estilo, GlobalPermisos.getListaNombresCuentas()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void pintarListadoZonas() {
        try {
            zonas.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.lista_estilo, GlobalPermisos.getListaNombresZonas()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void programarEventosClick() {
        try {
            cuentas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    cambioDeCuenta(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });

            zonas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    cambioDeZona(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });

            ingresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    continuar();
                }
            });

            salir.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    cerrarNotificarAcceso(false);
                    return false;
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void cambioDeCuenta(int posicion) {
        try {
            cargarCuentaActual(posicion);

            solicitarZonaSegunPerfil();
            solicitarDatosTiquete();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void cambioDeZona(int posicion) {
        try {
            cargarZonaActual(posicion);
            //cuando es una cuenta y una zona es decir solo una de cada una
            //se permite el acceso sin necesidad de continuar selecionando más.
            validarSiEsUnicaZonaUnicaCuentaEntrar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void validarSiEsUnicaZonaUnicaCuentaEntrar() {
        try {
            if (GlobalPermisos.getListadoCuentasValidas().size() != 1) {
                return;
            }
            if (GlobalPermisos.getListadoZonas().size() != 1) {
                return;
            }
            cerrarNotificarAcceso(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void validarSiEsUnicaZonaUnicaCuentaSalir() {
        try {
            if (GlobalPermisos.getListadoCuentasValidas() == null) {
                return;
            }
            if (GlobalPermisos.getListadoZonas() == null) {
                return;
            }
            if (GlobalPermisos.getListadoCuentasValidas().size() != 1) {
                return;
            }
            if (GlobalPermisos.getListadoZonas().size() != 1) {
                return;
            }
            salir();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void openMenuPrincipal() {
        try {
            startActivity(intentMenuPrincipal);
            permitirSalir();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void salir() {
        try {
            preguntarCongelarUsuario();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void preguntarCongelarUsuario() {
        // Mostrar un diálogo de confirmación
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Desea ver el recaudo a entregar y congelar el usuario?");
        // Botón "Sí"
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fetchRecaudo();
                bloqueoPromotor();
                // Realizar la consulta al recaudo del promotor
                //aumentarFreezeCount();
                //seConfirmaCongelarUsuario();
            }
        });

        // Botón "No" con una acción
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               // aumentarFreezeCount();
                GlobalPermisos.limpiarTodo();
                finish();
            }
        });

        builder.show();
    }

    private void fetchRecaudo(){
        permisosAPI.consultarRecaudoDiario(this, this, PermisosAPI.Acciones.CONSULTAR_RECAUDO_DIARIO, GlobalPermisos.getDatosSesionActual().getCuenta(), GlobalPermisos.getDatosSesionActual().getIdPromotor());
    }

    private void bloqueoPromotor(){
        permisosAPI.bloquearPromotor(this, this, PermisosAPI.Acciones.BLOQUEAR_PROMOTOR, GlobalPermisos.getDatosSesionActual().getCuenta(), GlobalPermisos.getDatosSesionActual().getIdPromotor());
    }

    private void recibirDatoConsultaRecaudoDiario(String datos){

            Toast.makeText(this, "El valor del recaudo es: " + datos, Toast.LENGTH_LONG).show();
        recaudoDiarioPromotor = datos;
        imprimirCuentaPromotor(getData());
    }

    private JSONObject getData() {
        JSONObject jsonObject = null;
        try {
            //DatabaseHelper dbHelper =  SelectScanner.getDatabaseHelper();
            //int cuentaPrepago = dbHelper.getPrepago();
            //int cuentaPostpago = dbHelper.getPostpago();
            //int cuentaEgreso = dbHelper.getEgreso();
            //int cuentaReimpresoEgreso = dbHelper.getReimpresoEgreso();
           // int cuentaReimpresoPrepago = dbHelper.getReimpresoPrepago();
           // int cuentaReimpresoPostpago = dbHelper.getReimpresoPostpago();
            //int totalCuentasTiquetes = cuentaPrepago+cuentaPostpago+cuentaEgreso+cuentaReimpresoEgreso+cuentaReimpresoPrepago+cuentaReimpresoPostpago;
           // String idGenerado = "P"+cuentaPrepago+"V"+cuentaPostpago+"S"+cuentaEgreso+"RP"+cuentaReimpresoPrepago+"RV"+cuentaReimpresoPostpago+"RS"+cuentaReimpresoEgreso+"T"+totalCuentasTiquetes;
           // long totalToPay = dbHelper.consultarTotalToPay(); // Consultar el total a pagar desde la base de datos
            //int freezeCount = dbHelper.consultarFreezeCount();
            jsonObject = new JSONObject();
            jsonObject.put("mensaje", "Muchas gracias por cumplir\n con  su labor, desde ZER te\n deseamos  un grandioso dia.");
            jsonObject.put("mensaje2", "Usted debe entregar: ");
           // jsonObject.put("mensaje3", idGenerado);
            jsonObject.put("dinero", recaudoDiarioPromotor+" pesos.");
           // jsonObject.put("freezeCount", "  "+freezeCount);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }

//    private void aumentarFreezeCount(){
//        DatabaseHelper dbHelper = SelectScanner.getDatabaseHelper();
//        dbHelper.incrementarFreezeCount();
//    }
//    private JSONObject getData() {
//        JSONObject jsonObject = null;
//        try {
//            DatabaseHelper dbHelper =  SelectScanner.getDatabaseHelper();
//            int cuentaPrepago = dbHelper.getPrepago();
//            int cuentaPostpago = dbHelper.getPostpago();
//            int cuentaEgreso = dbHelper.getEgreso();
//            int cuentaReimpresoEgreso = dbHelper.getReimpresoEgreso();
//            int cuentaReimpresoPrepago = dbHelper.getReimpresoPrepago();
//            int cuentaReimpresoPostpago = dbHelper.getReimpresoPostpago();
//            int totalCuentasTiquetes = cuentaPrepago+cuentaPostpago+cuentaEgreso+cuentaReimpresoEgreso+cuentaReimpresoPrepago+cuentaReimpresoPostpago;
//            String idGenerado = "P"+cuentaPrepago+"V"+cuentaPostpago+"S"+cuentaEgreso+"RP"+cuentaReimpresoPrepago+"RV"+cuentaReimpresoPostpago+"RS"+cuentaReimpresoEgreso+"T"+totalCuentasTiquetes;
//            long totalToPay = dbHelper.consultarTotalToPay(); // Consultar el total a pagar desde la base de datos
//            int freezeCount = dbHelper.consultarFreezeCount();
//            jsonObject = new JSONObject();
//            jsonObject.put("mensaje", "Muchas gracias por cumplir con  su labor, desde ZER te deseamos  un grandioso día.");
//            jsonObject.put("mensaje2", "Usted debe entregar: ");
//            jsonObject.put("mensaje3", idGenerado);
//            jsonObject.put("dinero", totalToPay+" pesos.");
//            jsonObject.put("freezeCount", "  "+freezeCount);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return jsonObject;
//    }


//    private void seConfirmaCongelarUsuario() {
//        try {
//            final String ENTER = "\n";
//            DatabaseHelper dbHelper = SelectScanner.getDatabaseHelper();
//
//            long totalToPay = dbHelper.consultarTotalToPay(); // Consultar el total a pagar desde la base de datos
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//            builder.setTitle("Alerta");
//            builder.setMessage("Muchas gracias por cumplir con su labor, desde ZER te deseamos un grandioso día." + " Usted debe entregar " +totalToPay + " pesos.");
//            dbHelper.actualizarFreeze(true); // Congela al usuario
//            // Botón "Aceptar"
//            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    try {
//                        IRespuestaImpresion iRespuestaImpresion = new IRespuestaImpresion() {
//                            @Override
//                            public void impresionExitosa() {
//                                // Acciones a realizar en caso de impresión exitosa
//
//                                GlobalPermisos.limpiarTodo();
//                                finish(); // Cierra la actividad
//                                salir();
//                            }
//
//                            @Override
//                            public void impresionFallo(ConectorImpresoraBT conectorImpresoraBT) {
//                                // Manejar la impresión fallida si es necesario
//                                GlobalPermisos.limpiarTodo();
//                                finish(); // Cierra la actividad independientemente de la impresión
//                            }
//                        };
//
//
//                        imprimirCuentaPromotor(getData());
//
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                }
//            });
//
//            // Mostrar el diálogo de alerta
//            AlertDialog dialog = builder.create();
//            dialog.show();
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
    private  void imprimirCuentaPromotor(JSONObject conte) {
        try {
            // Conectar con la impresora
            impresoraPos.conectar();

            // Imprimir el recibo
            generarSaldoPromotor(conte);

            // Esperar 2 segundos antes de desconectar la impresora
            try {
                Thread.sleep(2000); // Espera 2 segundos
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Desconectar la impresora
            impresoraPos.desconectar();
            GlobalPermisos.limpiarTodo();
            finish();
            // Mostrar el mensaje de impresión exitosa

        } catch (Exception ex) {
            // Mostrar el mensaje de error en caso de excepción
            ex.printStackTrace();
            // Cerrar el diálogo de "Imprimiendo" en caso de error

        }
    }
    private void generarSaldoPromotor(JSONObject conteJSON) {

        DatosSesion datosSesion = GlobalPermisos.getDatosSesionActual();
        String promotor = datosSesion.getNombrePromotor();
        try {
            impresoraPos.print(promotor, true, true, false, false);
            impresoraPos.addEnter();
            impresoraPos.print(conteJSON.getString("mensaje"), true, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print(conteJSON.getString("mensaje2"), true, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("$ " + recaudoDiarioPromotor + ".", true, true, true, false);
            impresoraPos.addEnter();
            impresoraPos.addEnter();
            impresoraPos.printUnderline("                       ", true);
            impresoraPos.addEnter();
            impresoraPos.addEnter();
            //impresoraPos.print(conteJSON.getString("mensaje3"), true, false, false, true);
            //impresoraPos.addEnter();
            impresoraPos.print(Configuracion.getFechaHoraActual(),true, true, false, false);
            //impresoraPos.print(conteJSON.getString("freezeCount"), false, false, false, true);
            impresoraPos.addEnter();
            impresoraPos.addEnter();
            impresoraPos.addEnter();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    private void cerrarNotificarAcceso(boolean acceso) {
        try {
            if (acceso) {
                openMenuPrincipal();
            } else {
                salir();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void mensajePantalla(String mensaje, String textoBoton, String tagUsado) {
        try {
            DialogFragment newFragment = new MensajeAceptar(mensaje, textoBoton, MensajeAceptar.Colores.AMARILLO);
            newFragment.setCancelable(false);
            newFragment.show(getSupportFragmentManager(), tagUsado);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void mensajePantalla(String mensaje,
                                 String textoBoton,
                                 String tagUsado,
                                 String titulo,
                                 IRespuestaConfirmar iRespuestaConfirmar) {
        try {
            DialogFragment newFragment = new MensajeAceptar(mensaje,
                    textoBoton,
                    titulo,
                    iRespuestaConfirmar,
                    MensajeAceptar.Colores.AMARILLO);
            newFragment.setCancelable(false);
            newFragment.show(getSupportFragmentManager(), tagUsado);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void continuar() {
        try {
            DatosSesion datosSesion = GlobalPermisos.getDatosSesionActual();
            if (datosSesion == null) {
                mensajePantalla("Debes ser un promotor o un supervisor", "Aceptar", "OK");
                return;
            }
            if (datosSesion.getCuenta().trim().length() < 1) {
                mensajePantalla("Debes ser un promotor o un supervisor", "Aceptar", "OK");
                return;
            }
            if (datosSesion.getIdZona() == null) {
                mensajePantalla("No tiene zona asignada para hoy", "Aceptar", "OK");
                return;
            }
            if (datosSesion.getIdZona() < 1) {
                mensajePantalla("No tiene zona asignada para hoy", "Aceptar", "OK");
                return;
            }
            cerrarNotificarAcceso(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void cargarCuentaActual(int posicion) {
        try {
            List<JSONObject> listado = GlobalPermisos.getListadoCuentasValidas();
            if (listado == null) {
                return;
            }
            if (posicion < 0 || posicion >= listado.size()) {
                return;
            }
            DatosSesion datosSesion = new DatosSesion(listado.get(posicion));
            GlobalPermisos.setDatosSesionActual(datosSesion);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void cargarZonaActual(int posicion) {
        try {
            List<JSONObject> listado = GlobalPermisos.getListadoZonas();
            if (listado == null) {
                return;
            }
            if (posicion < 0 || posicion >= listado.size()) {
                return;
            }
            GlobalPermisos.getDatosSesionActual().setDatosZona(listado.get(posicion));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void solicitarZonaSegunPerfil() {
        try {
            DatosSesion datosSesion = GlobalPermisos.getDatosSesionActual();
            if (datosSesion == null) {
                return;
            }
            if (datosSesion.getIdPerfil() == GlobalPermisos.Perfiles.PROMOTOR) {
                solicitarZonaPromotor();
            }
            if (datosSesion.getIdPerfil() == GlobalPermisos.Perfiles.SUPERVISOR) {
                solicitarZonaPromotor();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void solicitarDatosTiquete() {
        try {
            DatosSesion datosSesion = GlobalPermisos.getDatosSesionActual();
            if (datosSesion == null) {
                return;
            }
            setDialogTiquete(true);
            permisosAPI.solicitarDatosTiquete(this,
                    this,
                    PermisosAPI.Acciones.SOLICITAR_DATOS_TIQUETE,
                    datosSesion.getCuenta()
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void solicitarZonaPromotor() {
        try {
            DatosSesion datosSesion = GlobalPermisos.getDatosSesionActual();
            if (datosSesion == null) {
                return;
            }
            setDialogZona(true);
            permisosAPI.solicitarZonasPerfil(this,
                    this,
                    PermisosAPI.Acciones.SOLICITAR_ZONAS_PROMOTOR,
                    datosSesion.getCuenta(),
                    datosSesion.getCorreoPromotor()
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<JSONObject> getCuentasValidas() {
        List<JSONObject> cuentas = new ArrayList<>();
        try {
            JSONArray jsonArray = GlobalPermisos.getCuentasJSONArray();
            if (jsonArray == null) {
                return cuentas;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    Long perfilLocal = jsonData.getLong("idPerfil");
                    if (perfilLocal == GlobalPermisos.Perfiles.PROMOTOR || perfilLocal == GlobalPermisos.Perfiles.SUPERVISOR) {
                        cuentas.add(jsonData);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cuentas;
    }

    private List<JSONObject> getZonasValidasPromotor() {
        List<JSONObject> zonas = new ArrayList<>();
        try {
            JSONArray jsonArray = GlobalPermisos.getZonasJSONArray();
            if (jsonArray == null) {
                return zonas;
            }
            final String nombreDiaActual = Configuracion.getDiaActualNombre();

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    boolean diaHabilitado = jsonData.getBoolean(nombreDiaActual);
                    if (diaHabilitado) {
                        zonas.add(jsonData);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return zonas;
    }

    private List<String> getNombreCuentas() {
        List<String> nombresCuentas = new ArrayList<>();
        try {
            List<JSONObject> listadoLocal = GlobalPermisos.getListadoCuentasValidas();

            for (JSONObject objLocal : listadoLocal) {
                try {
                    nombresCuentas.add(objLocal.getString("nombre"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return nombresCuentas;
    }

    private List<String> getNombreZonas(String nombreCampo) {
        List<String> nombresZonas = new ArrayList<>();
        try {
            List<JSONObject> listadoLocal = GlobalPermisos.getListadoZonas();
            for (JSONObject objLocal : listadoLocal) {
                try {
                    nombresZonas.add(objLocal.getString(nombreCampo));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return nombresZonas;
    }

    private void solicitudZonasPromotorExitoso(JSONArray responseArrayJSON) {
        try {
            GlobalPermisos.setZonasJSONArray(responseArrayJSON);
            GlobalPermisos.setListadoZonas(getZonasValidasPromotor());
            GlobalPermisos.setListaNombresZonas(getNombreZonas("z_nombre"));
            pintarListadoZonas();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void solicitudDatosTiqueteExitoso(JSONObject responseJSON) {
        try {
            GlobalPermisos.getDatosSesionActual().setDatosTiquete(responseJSON);
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
            case PermisosAPI.Acciones.SOLICITAR_ZONAS_PROMOTOR:
                solicitudZonasPromotorExitoso(responseArrayJSON);
                setDialogZona(false);
                break;
            case PermisosAPI.Acciones.SOLICITAR_DATOS_TIQUETE:
                solicitudDatosTiqueteExitoso(responseJSON);
                setDialogTiquete(false);
                break;
            case PermisosAPI.Acciones.CONSULTAR_RECAUDO_DIARIO:
                recibirDatoConsultaRecaudoDiario(responseString);
                setDialogTiquete(false);
                break;
        }
    }

    @Override
    public void errorJSON(String accion, int statusCode, Header[] headers, byte[] responseBytes, String responseString, JSONObject JSONResponse, JSONArray JSONArrayResponse, Throwable throwable) {
        // Si el código de estado es 200 pero no se puede parsear como JSON
        if (statusCode == 200 && responseString != null) {
            recibirDatoConsultaRecaudoDiario(responseString);
            return;
        }
        switch (accion) {
            case PermisosAPI.Acciones.SOLICITAR_ZONAS_PROMOTOR:
                mensajePantalla("Cierre y vuelva a entrar al sistema, dado que hubo un error.", "Aceptar", "OK");
                setDialogZona(false);
                break;
            case PermisosAPI.Acciones.SOLICITAR_DATOS_TIQUETE:
                mensajePantalla("Cierre y vuelva a entrar al sistema, dado que hubo un error.", "Aceptar", "OK");
                setDialogTiquete(false);
                break;
            case PermisosAPI.Acciones.CONSULTAR_RECAUDO_DIARIO:
                mensajePantalla("Cierre y vuelva a entrar al sistema, dado que hubo un error.", "Aceptar", "OK");
                setDialogTiquete(false);
                break;
        }
    }

    @Override
    public void termino() {

    }

    @Override
    public void respuestaSI() {
        cerrarNotificarAcceso(false);
    }

    @Override
    public void respuestaNO() {

    }

}