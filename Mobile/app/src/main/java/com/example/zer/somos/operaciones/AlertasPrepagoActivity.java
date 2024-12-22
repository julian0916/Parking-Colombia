package com.example.zer.somos.operaciones;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.example.zer.somos.R;
import com.example.zer.somos.comunes.ActivityBase;
import com.example.zer.somos.comunes.IRespuestaConfirmarAccion;
import com.example.zer.somos.comunes.MensajeAceptar;
import com.example.zer.somos.comunes.MensajeConfirmarAccion;
import com.example.zer.somos.comunicacion.IRespuestaHttpCliente;
import com.example.zer.somos.permisos.GlobalPermisos;
import com.example.zer.somos.utilidades.Configuracion;
import com.example.zer.somos.utilidades.DatosIngreso;
import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class AlertasPrepagoActivity extends ActivityBase
        implements IOperacionDatosRegistroPrepago,
        IRespuestaHttpCliente,
        IRespuestaConfirmarAccion {

    public static final String PROCESAR_PLACA = "PROCESAR_PLACA";
    public static final int CODIGO_RESPUESTA = 101;
    public static final int CODIGO_RESULTADO_INICIAR_POSTPAGO = 10;
    private static DialogFragment mensajeNoHayRegistros;
    private ListView listado;
    private Button salir;
    private AlertasPrepagoAPI alertasPrepagoAPI;
    private DialogFragment mensajeConfirmar;
    private AlertDialog.Builder builder;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_alertas_prepago);
            iniciarComponentes();
            programarEventos();
            cargarListadoAlertas();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void iniciarComponentes() {
        try {
            listado = findViewById(R.id.listado);
            salir = findViewById(R.id.salir);
            alertasPrepagoAPI = new AlertasPrepagoAPI();
            builder = new AlertDialog.Builder(this);
            builder.setView(R.layout.progress);
            builder.setCancelable(false);
            dialog = builder.create();
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void salir() {
        try {
            finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Mensaje para notificar que no hay datos que mostrar
     * para la placa que se ha ingresado.
     */
    private void mensajeNoHayDatosDeAlerta() {
        try {
            if (mensajeNoHayRegistros != null) {
                mensajeNoHayRegistros.dismiss();
            }
            mensajeNoHayRegistros = new MensajeAceptar(
                    "No hay alertas que mostrar.",
                    "Aceptar",
                    MensajeAceptar.Colores.AZUL);
            mensajeNoHayRegistros.setCancelable(false);
            mensajeNoHayRegistros.show(getSupportFragmentManager(), "OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void cargarListadoAlertas() {
        try {
            DatosRegistroAdaptador datosRegistroAdaptador = new DatosRegistroAdaptador(
                    GlobalPermisos.getListaAlertas(),
                    this,
                    this);
            try {
                if (datosRegistroAdaptador == null) {
                    mensajeNoHayDatosDeAlerta();
                } else if (datosRegistroAdaptador.getCount() < 1) {
                    mensajeNoHayDatosDeAlerta();
                }
                listado.setAdapter(datosRegistroAdaptador);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void cerrarIniciarPostpago(DatosRegistro datosRegistro) {
        try {
            if (datosRegistro == null) {
                return;
            }
            Intent replyIntent = new Intent();
            replyIntent.putExtra(PROCESAR_PLACA, datosRegistro.getPlaca());
            setResult(CODIGO_RESULTADO_INICIAR_POSTPAGO, replyIntent);
            salir();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void solicitarActualizarCupoPrepago(DatosRegistro datosRegistro) {
        try {
            setDialog(true);
            alertasPrepagoAPI.salioPrepagoActualizar(this,
                    this,
                    AlertasPrepagoAPI.Acciones.ACTUALIZAR_CUPO_DISPONIBLE_PREPAGO,
                    GlobalPermisos.getDatosSesionActual().getCuenta(),
                    datosRegistro.getId());
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

    private void mensajeErrorActualizar(String mensaje) {
        try {
            DialogFragment newFragment = new MensajeAceptar(
                    mensaje,
                    "Aceptar",
                    "Error al actualizar ",
                    MensajeAceptar.Colores.AZUL);
            newFragment.setCancelable(false);
            newFragment.show(getSupportFragmentManager(), "ERROR");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void mensajeConfirmarDatos(DatosRegistro datosRegistro) {
        try {
            final String ENTER = "\n";
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("Placa: ");
            mensaje.append(datosRegistro.getPlaca());
            mensaje.append(ENTER);
            mensaje.append("Vehículo: ");
            mensaje.append(DatosIngreso.getTipoPlaca(datosRegistro.getPlaca()) == DatosIngreso.PLACA_CARRO ? "Carro" : "Moto");
            mensaje.append(ENTER);
            mensaje.append("Fecha: ");
            mensaje.append(Configuracion.getFechaHoraActual());
            mensajeConfirmar = new MensajeConfirmarAccion(
                    AlertasPrepagoAPI.Acciones.ACTUALIZAR_CUPO_DISPONIBLE_PREPAGO,
                    mensaje.toString(),
                    "SI",
                    "NO",
                    "Confirmar Salida",
                    datosRegistro,
                    this,
                    MensajeConfirmarAccion.Colores.AMARILLO
            );
            mensajeConfirmar.setCancelable(false);
            mensajeConfirmar.show(getSupportFragmentManager(), "OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void botonReportarSalio(DatosRegistro datosRegistro) {
        try {
            mensajeConfirmarDatos(datosRegistro);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void botonIniciarPostpago(DatosRegistro datosRegistro) {
        try {
            cerrarIniciarPostpago(datosRegistro);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void respuestaJSON(String accion, int statusCode, Header[] headers, byte[] responseBytes, String responseString, JSONObject responseJSON, JSONArray responseArrayJSON) {
        switch (accion) {
            case AlertasPrepagoAPI.Acciones.ACTUALIZAR_CUPO_DISPONIBLE_PREPAGO:
                setDialog(false);
                salir();
                break;
        }
    }

    @Override
    public void errorJSON(String accion, int statusCode, Header[] headers, byte[] responseBytes, String responseString, JSONObject JSONResponse, JSONArray JSONArrayResponse, Throwable throwable) {
        switch (accion) {
            case AlertasPrepagoAPI.Acciones.ACTUALIZAR_CUPO_DISPONIBLE_PREPAGO:
                setDialog(false);
                mensajeErrorActualizar("Verique que tenga internet y vuelva a intentarlo.");
                break;
        }
    }

    @Override
    public void termino() {

    }

    @Override
    public void respuestaSI(String accion, Object contenido) {
        switch (accion) {
            case AlertasPrepagoAPI.Acciones.ACTUALIZAR_CUPO_DISPONIBLE_PREPAGO:
                solicitarActualizarCupoPrepago((DatosRegistro) contenido);
                break;
        }
    }

    @Override
    public void respuestaNO(String accion, Object contenido) {

    }
}