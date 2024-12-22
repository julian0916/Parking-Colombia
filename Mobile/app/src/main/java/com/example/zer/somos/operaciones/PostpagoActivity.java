package com.example.zer.somos.operaciones;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.example.zer.somos.R;
import com.example.zer.somos.comunes.*;
import com.example.zer.somos.comunicacion.IRespuestaHttpCliente;
import com.example.zer.somos.permisos.GlobalPermisos;
import com.example.zer.somos.permisos.PermisosActivity;
import com.example.zer.somos.utilidades.*;
import com.example.zer.somos.utilidades.connection.DeviceConnection;
import com.example.zer.somos.utilidades.connection.bluetooth.BluetoothConnection;
import com.example.zer.somos.utilidades.connection.bluetooth.BluetoothPrintersConnections;

import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class PostpagoActivity extends ActivityBase implements
        IRespuestaConfirmar, IRespuestaHttpCliente,
        IRespuestaImpresion, IRespuestaConfirmarAccion {

    public static final String ABRIR_CON_PLACA = "ABRIR_CON_PLACA";
    public static final String REINTENTAR_IMPRIMIR = "REINTENTAR_IMPRIMIR";
    protected AlertDialog.Builder builderImprimiendo;
    protected Dialog dialogImprimiendo;
    private static DialogFragment mensajeGeneral;
    private EditText placa;
    private Button ventaPostpago;
    private Button salir;
    private PostpagoAPI postpagoAPI;
    private DialogFragment mensajeConfirmar;
    private AlertDialog.Builder builder;
    private Dialog dialog;
    private int numeroDePulsacionesBotonConfirmar = 0;
    private  ImpresoraPos impresoraPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_postpago);
            iniciarComponentes();
            programarEventos();
            procesarApertura();
            BluetoothPrint.setContext(getApplicationContext());
            impresoraPos = new ImpresoraPos(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void procesarApertura() {
        try {
            Bundle b = getIntent().getExtras();
            if (b == null) {
                return;
            }
            String placaEnviada = b.getString(ABRIR_CON_PLACA);
            placa.setText(placaEnviada);
            grabarVentaPostpago();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void iniciarComponentes() {
        try {
            salir = findViewById(R.id.salir);
            placa = findViewById(R.id.placa);
            ventaPostpago = findViewById(R.id.ventaPostpago);
            postpagoAPI = new PostpagoAPI();
            builder = new AlertDialog.Builder(this);
            builder.setView(R.layout.progress);
            builder.setCancelable(false);
            dialog = builder.create();
            builderImprimiendo = new AlertDialog.Builder(this);
            builderImprimiendo.setView(R.layout.imprimiendo);
            builderImprimiendo.setCancelable(true);
            dialogImprimiendo = builderImprimiendo.create();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void programarEventos() {
        try {
            ventaPostpago.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    grabarVentaPostpago();
                }
            });

            salir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    salir();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void grabarVentaPostpago() {
        try {
            ocultarTeclado();
            limpiarDatosPlaca();
            int tipoPlaca = DatosIngreso.getTipoPlaca(placa.getText().toString());
            if (tipoPlaca == DatosIngreso.NO_ES_PLACA) {
                mensajeDatosPlacaIncorrectos();
                return;
            }
            if(tipoPlaca == DatosIngreso.PLACA_CARRO &&  GlobalPermisos.getDatosSesionActual().getCeldasCarro()<1){
                mensajeCarrosNoPermitidosEnZona();
                return;
            }
            if(tipoPlaca == DatosIngreso.PLACA_MOTO &&  GlobalPermisos.getDatosSesionActual().getCeldasMoto()<1){
                mensajeMotosNoPermitidasEnZona();
                return;
            }
            mensajeConfirmarDatos();
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

    private void ocultarTeclado() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(placa.getWindowToken(), 0);
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

    public void mensajeImprimiendo(boolean mostrar) {
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

    private void mensajeDatosPlacaIncorrectos() {
        try {
            if(mensajeGeneral!=null){
                mensajeGeneral.dismiss();
            }
            mensajeGeneral = new MensajeAceptar(
                    "La placa ingresada no es correcta.\n\nCorrija y vuelva a intentarlo.",
                    "Aceptar",
                    "Accion no permitida",
                    new IRespuestaConfirmar() {
                        @Override
                        public void respuestaSI() {
                            salir();
                        }

                        @Override
                        public void respuestaNO() {
                            salir();
                        }
                    },
                    MensajeAceptar.Colores.AMARILLO);
            mensajeGeneral.setCancelable(false);
            mensajeGeneral.show(getSupportFragmentManager(), "OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *Se visualiza que los carros no son permitidos en esta zona
     */
    private void mensajeCarrosNoPermitidosEnZona() {
        try {
            if(mensajeGeneral!=null){
                mensajeGeneral.dismiss();
            }
            mensajeGeneral = new MensajeAceptar(
                    "En esta zona no se permite parquear carros.",
                    "Aceptar",
                    "Accion no permitida",
                    new IRespuestaConfirmar() {
                        @Override
                        public void respuestaSI() {
                            salir();
                        }

                        @Override
                        public void respuestaNO() {
                            salir();
                        }
                    },
                    MensajeAceptar.Colores.AMARILLO);
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
            if(mensajeGeneral!=null){
                mensajeGeneral.dismiss();
            }
            mensajeGeneral = new MensajeAceptar(
                    "En esta zona no se permite parquear motos.",
                    "Aceptar",
                    "Accion no permitida",
                    new IRespuestaConfirmar() {
                        @Override
                        public void respuestaSI() {
                            salir();
                        }

                        @Override
                        public void respuestaNO() {
                            salir();
                        }
                    },  MensajeAceptar.Colores.AMARILLO);
            mensajeGeneral.setCancelable(false);
            mensajeGeneral.show(getSupportFragmentManager(), "OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    private void mensajeConfirmarDatos() {
       numeroDePulsacionesBotonConfirmar ++;
        if(numeroDePulsacionesBotonConfirmar < 2 ){
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
                mensaje.append(DatosIngreso.getTipoPlaca(placa.getText().toString()) == DatosIngreso.PLACA_CARRO ? "Carro" : "Moto");
                mensaje.append(ENTER);
                long valorHora = DatosIngreso.getTipoPlaca(placa.getText().toString()) == DatosIngreso.PLACA_CARRO ? GlobalPermisos.getDatosSesionActual().getValorHoraCarro() : GlobalPermisos.getDatosSesionActual().getValorHoraMoto();
                mensaje.append("Valor hora: $");
                mensaje.append(valorHora);
                mensaje.append(ENTER);
                mensajeConfirmar = new MensajeConfirmar(
                        mensaje.toString(),
                        "SI",
                        "NO",
                        "Confirmar Postpago",
                        this,
                        MensajeConfirmar.Colores.VERDE
                );
                mensajeConfirmar.setCancelable(false);
                mensajeConfirmar.show(getSupportFragmentManager(), "OK");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
       }
        else{
            salir();
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

    private void setDialog(boolean show) {
        try {
            if (show) dialog.show();
            else dialog.dismiss();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void solicitarRegistroPostpagoServidor() {
        try {
            postpagoAPI.ingresarPostpago(this,
                    this,
                    PostpagoAPI.Acciones.GRABAR_POSTPAGO,
                    GlobalPermisos.getDatosSesionActual().getCuenta(),
                    getData());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private JSONObject getData() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("esPrepago", false);
            jsonObject.put("horas", 0);
            jsonObject.put("placa", placa.getText());
            jsonObject.put("promotor", GlobalPermisos.getDatosSesionActual().getIdPromotor());
            jsonObject.put("zona", GlobalPermisos.getDatosSesionActual().getIdZona());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }

    private  void imprimirReciboPostpago(JSONObject conte) {
        // Mostrar el mensaje "Imprimiendo"
        mensajeImprimiendo(true);

        try {
            // Conectar con la impresora
            impresoraPos.conectar();

            // Imprimir el recibo
            generarReciboPostpago(conte);

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

    private void generarReciboPostpago(JSONObject conteJSON) {
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
            impresoraPos.print("ENTRADA", true, true, true, false);
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

            impresoraPos.print("ATENCION", true, true, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Solicita siempre tu recibo de salida o tu pago no sera aplicado.", false, true, false, false);
            impresoraPos.addEnter();
            impresoraPos.addEnter();
            impresoraPos.addEnter();
            DatabaseHelper dbHelper = SelectScanner.getDatabaseHelper();
           // dbHelper.incrementarPostpago();
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

    private void mensajeReimprimir(ConectorImpresoraBT conectorImpresoraBT) {
        try {
            DialogFragment newFragment = new MensajeConfirmarAccion(
                    REINTENTAR_IMPRIMIR,
                    "¿Desea intentarlo nuevamente para generar el tiquete de entrada?",
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

    private void mensajeErrorServidor(String mensaje) {
        try {
            DialogFragment newFragment = new MensajeAceptar(
                    mensaje,
                    "Aceptar",
                    "No se pudo grabar",
                    MensajeAceptar.Colores.ROJO);
            newFragment.setCancelable(false);
            newFragment.show(getSupportFragmentManager(), "ERROR");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        salir();
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
        numeroDePulsacionesBotonConfirmar = 0;
        try {
            mensajeConfirmarCerrar();
            setDialog(true);
            solicitarRegistroPostpagoServidor();
            mensajeImprimiendo(true);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void respuestaNO() {
        numeroDePulsacionesBotonConfirmar = 0;
        try {
            mensajeConfirmar.dismiss();
            salir();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void respuestaJSON(String accion, int statusCode, Header[] headers, byte[] responseBytes, String responseString, JSONObject responseJSON, JSONArray responseArrayJSON) {
        switch (accion) {
            case PostpagoAPI.Acciones.GRABAR_POSTPAGO:
                setDialog(false);
                imprimirReciboPostpago(responseJSON);
                break;
        }
    }

    @Override
    public void errorJSON(String accion, int statusCode, Header[] headers, byte[] responseBytes, String responseString, JSONObject JSONResponse, JSONArray JSONArrayResponse, Throwable throwable) {
        switch (accion) {
            case PostpagoAPI.Acciones.GRABAR_POSTPAGO:
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
        salir();
        mensajeImprimiendo(false);
        mensajeConfirmar.dismiss();

    }

    @Override
    public void impresionFallo(ConectorImpresoraBT conectorImpresoraBT) {
        mensajeImprimiendo(false);
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