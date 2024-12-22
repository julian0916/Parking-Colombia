package com.example.zer.somos.operaciones;

import android.app.Dialog;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.example.zer.somos.R;
import com.example.zer.somos.comunes.*;
import com.example.zer.somos.comunicacion.IRespuestaHttpCliente;
import com.example.zer.somos.permisos.GlobalPermisos;
import com.example.zer.somos.utilidades.*;
import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class CierreVentas extends ActivityBase
        implements IRespuestaHttpCliente,
        IRespuestaImpresion,
        IOperacionDatosRegistro,
        IRespuestaConfirmarAccion {

    private static final String REINTENTAR_IMPRIMIR = "REINTENTAR_IMPRIMIR";

    protected ListView listado;
    protected Button salir;
    protected CierreVentasAPI cierreVentasAPI;
    protected DialogFragment mensajeConfirmar;
    protected DialogFragment mensajeError;
    protected AlertDialog.Builder builder;
    protected Dialog dialog;
    protected AlertDialog.Builder builderImprimiendo;
    protected Dialog dialogImprimiendo;
    private ImpresoraPos impresoraPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            impresoraPos = new ImpresoraPos(this);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    protected void iniciarComponentes() {
        try {
            listado = findViewById(R.id.listado);
            salir = findViewById(R.id.salir);
            cierreVentasAPI = new CierreVentasAPI();
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

    protected void programarEventos() {
        try {
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

    protected void salir() {
        try {
            finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void ocultarTeclado(IBinder elemento) {
        try {
            if (elemento == null) {
                return;
            }
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(elemento, 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void setDialog(boolean show) {
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


    protected void terminarPagoExitoso(JSONObject cont) {
        try {
            setDialog(true);

            imprimirReciboPago(cont);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private  void imprimirReciboPago(JSONObject conte) {
        // Mostrar el mensaje "Imprimiendo"
        mensajeImprimiendo(true);

        try {
            // Conectar con la impresora
            impresoraPos.conectar();

            // Imprimir el recibo
            generarReciboPago(conte);

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
    private void generarReciboPago(JSONObject conteJSON) {
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
            impresoraPos.print("SALIDA", true, true, true, false);
            impresoraPos.addEnter();
            String titulo = "PAGO REALIZADO";
            if (conteJSON.getLong("estado") == 2) {
                long minutosGracia = conteJSON.getLong("minutosGraciaZona");
                titulo = minutosGracia + " MINUTOS-GRATIS";
            }
            if (conteJSON.getLong("estado") == 4) {
                titulo = "PAGO-EXTEMPORANEO";
            }
            if (conteJSON.getLong("estado") == 3) {
                titulo = "SIN PAGAR Y REPORTADO";
            }
            impresoraPos.print(titulo, true, true, true, false);
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
            impresoraPos.print("Tiempo: " + Configuracion.getHorasMinutosTranscurridos(conteJSON.getString("fhingreso"), conteJSON.getString("fhegreso")), false, false, false, false);
            impresoraPos.addEnter();
            impresoraPos.print("Horas cobradas: " + conteJSON.getLong("hcobradas") + (conteJSON.getLong("hcobradas") == 1 ? " hora" : " horas"), false, false, false, false);
            impresoraPos.addEnter();
            if (conteJSON.getLong("estado") == 3) {
                impresoraPos.print("Valor adeudado: $" + conteJSON.getString("valorCobrado"), false, true, true, false);
            } else {
                impresoraPos.print("Valor pagado: $" + conteJSON.getString("valorCobrado"), false, true, true, false);
            }

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
            DatabaseHelper dbHelper = SelectScanner.getDatabaseHelper();
            //dbHelper.incrementarEgreso();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    protected void confirmarPagoServidor(DatosRegistro datosRegistro) {
        try {
            setDialog(true);
            cierreVentasAPI.confirmarPagoPlaca(this,
                    this,
                    CierreVentasAPI.Acciones.CONFIRMAR_PAGO,
                    GlobalPermisos.getDatosSesionActual().getCuenta(),
                    datosRegistro.getId(),
                    datosRegistro.getPlaca(),
                    datosRegistro.getEgreso(),
                    GlobalPermisos.getDatosSesionActual().getIdPromotor());

            DatabaseHelper db = SelectScanner.getDatabaseHelper();
          //  db.actualizarTotalToPay(datosRegistro.getValorCobro(), datosRegistro.getPlaca());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void pagoExtemporaneoServidor(DatosRegistro datosRegistro) {
        try {
            setDialog(true);
            cierreVentasAPI.hacerPagoExtemporaneo(this,
                    this,
                    CierreVentasAPI.Acciones.PAGO_EXTEMPORANEO,
                    GlobalPermisos.getDatosSesionActual().getCuenta(),
                    datosRegistro.getId(),
                    datosRegistro.getPlaca(),
                    GlobalPermisos.getDatosSesionActual().getIdPromotor());
            DatabaseHelper db = SelectScanner.getDatabaseHelper();
           // db.actualizarTotalToPay(datosRegistro.getValorCobro(), datosRegistro.getPlaca());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void reportarVehiculoServidor(DatosRegistro datosRegistro) {
        try {
            setDialog(true);
            cierreVentasAPI.reportarVehiculo(this,
                    this,
                    CierreVentasAPI.Acciones.REPORTAR_VEHICULO,
                    GlobalPermisos.getDatosSesionActual().getCuenta(),
                    datosRegistro.getId(),
                    datosRegistro.getPlaca(),
                    GlobalPermisos.getDatosSesionActual().getIdPromotor());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void confirmarPagoFallo(String mensaje) {
        try {
            if(mensaje==null){
                mensaje="";
            }
            if(mensaje.trim().isEmpty()){
                mensaje="Se presentó un inconveniete en la red, inténtelo nuevamente.";
            }
            if(mensajeError!=null){
                mensajeError.dismiss();
            }
            mensajeError = new MensajeAceptar(
                    mensaje,
                    "Aceptar",
                    "Error",
                    MensajeAceptar.Colores.ROJO);
            mensajeError.setCancelable(false);
            mensajeError.show(getSupportFragmentManager(), "OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void mensajeConfirmarReportar(DatosRegistro datosRegistro, String accion) {
        try {
            final String ENTER = "\n";
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("Placa: ");
            mensaje.append(datosRegistro.getPlaca());
            mensaje.append(ENTER);
            mensaje.append("Vehículo: ");
            mensaje.append(DatosIngreso.getTipoPlaca(datosRegistro.getPlaca()) == DatosIngreso.PLACA_CARRO ? "Carro" : "Moto");
            mensaje.append(ENTER);
            mensaje.append("Entrada: ");
            mensaje.append(Configuracion.getFechaHora(datosRegistro.getIngreso()));
            mensaje.append(ENTER);
            mensaje.append("Salida: ");
            mensaje.append(Configuracion.getFechaHora(datosRegistro.getEgreso()));
            mensajeConfirmar = new MensajeConfirmarAccion(
                    accion,
                    mensaje.toString(),
                    "SI",
                    "NO",
                    "Reportar Vehículo",
                    datosRegistro,
                    this,
                    MensajeConfirmarAccion.Colores.ROJO
            );
            mensajeConfirmar.setCancelable(false);
            mensajeConfirmar.show(getSupportFragmentManager(), "OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void mensajeConfirmarPago(DatosRegistro datosRegistro, String accion) {
        try {
            final String ENTER = "\n";
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("Placa: ");
            mensaje.append(datosRegistro.getPlaca());
            mensaje.append(ENTER);
            mensaje.append("Vehículo: ");
            mensaje.append(DatosIngreso.getTipoPlaca(datosRegistro.getPlaca()) == DatosIngreso.PLACA_CARRO ? "Carro" : "Moto");
            mensaje.append(ENTER);
            mensaje.append("Entrada: ");
            mensaje.append(Configuracion.getFechaHora(datosRegistro.getIngreso()));
            mensaje.append(ENTER);
            mensaje.append("Salida: ");
            mensaje.append(Configuracion.getFechaHora(datosRegistro.getEgreso()));
            mensaje.append(ENTER);
            mensaje.append("Horas: ");
            mensaje.append(datosRegistro.getHorasCobradas());
            mensaje.append(ENTER);
            mensaje.append("Valor: ");
            mensaje.append(datosRegistro.getValorCobro());
            mensajeConfirmar = new MensajeConfirmarAccion(
                    accion,
                    mensaje.toString(),
                    "SI",
                    "NO",
                    "Confirmar Pago",
                    datosRegistro,
                    this,
                    MensajeConfirmarAccion.Colores.VERDE
            );
            mensajeConfirmar.setCancelable(false);
            mensajeConfirmar.show(getSupportFragmentManager(), "OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void mensajeReimprimir(ConectorImpresoraBT conectorImpresoraBT) {
        try {
            DialogFragment newFragment = new MensajeConfirmarAccion(
                    REINTENTAR_IMPRIMIR,
                    "Desea intentarlo nuevamente?",
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

    protected void reintentarImprimir(ConectorImpresoraBT conectorImpresoraBT) {
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
    public void respuestaJSON(String accion, int statusCode, Header[] headers, byte[] responseBytes, String responseString, JSONObject responseJSON, JSONArray responseArrayJSON) {

    }

    @Override
    public void errorJSON(String accion, int statusCode, Header[] headers, byte[] responseBytes, String responseString, JSONObject JSONResponse, JSONArray JSONArrayResponse, Throwable throwable) {
        switch (accion) {
            case CierreVentasAPI.Acciones.CARTERA_PLACA:
            case CierreVentasAPI.Acciones.CARTERA_ZONA:
                setDialog(false);
                break;
            case CierreVentasAPI.Acciones.PAGO_EXTEMPORANEO:
            case CierreVentasAPI.Acciones.CONFIRMAR_PAGO:
                setDialog(false);
                confirmarPagoFallo(responseString);
                break;
            case CierreVentasAPI.Acciones.REPORTAR_VEHICULO:
                setDialog(false);
                break;
        }
    }

    @Override
    public void termino() {

    }

    @Override
    public void impresionExitosa() {
        try {
            mensajeImprimiendo(false);
            salir();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void impresionFallo(ConectorImpresoraBT conectorImpresoraBT) {
        try {
            mensajeImprimiendo(false);
            salir();
            mensajeReimprimir(conectorImpresoraBT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void botonReportarVehiculo(DatosRegistro datosRegistro) {
        try {
            mensajeConfirmarReportar(datosRegistro, CierreVentasAPI.Acciones.REPORTAR_VEHICULO);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void botonConfirmarPagoVehiculo(DatosRegistro datosRegistro) {
        try {
            mensajeConfirmarPago(datosRegistro, CierreVentasAPI.Acciones.CONFIRMAR_PAGO);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void botonPagoExtemporaneoVehiculo(DatosRegistro datosRegistro) {
        try {
            mensajeConfirmarPago(datosRegistro, CierreVentasAPI.Acciones.PAGO_EXTEMPORANEO);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void respuestaSI(String accion, Object contenido) {
        switch (accion) {
            case CierreVentasAPI.Acciones.CONFIRMAR_PAGO:
                setDialog(true);
                confirmarPagoServidor((DatosRegistro) contenido);
                break;
            case CierreVentasAPI.Acciones.PAGO_EXTEMPORANEO:
                setDialog(true);
                pagoExtemporaneoServidor((DatosRegistro) contenido);
                break;
            case CierreVentasAPI.Acciones.REPORTAR_VEHICULO:
                setDialog(true);
                reportarVehiculoServidor((DatosRegistro) contenido);
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

}
