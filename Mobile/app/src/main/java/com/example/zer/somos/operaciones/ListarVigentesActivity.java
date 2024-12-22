package com.example.zer.somos.operaciones;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.DialogFragment;
import com.example.zer.somos.R;
import com.example.zer.somos.comunes.IRespuestaConfirmar;
import com.example.zer.somos.comunes.MensajeAceptar;
import com.example.zer.somos.comunes.MensajeConfirmarAccion;
import com.example.zer.somos.permisos.GlobalPermisos;
import com.example.zer.somos.utilidades.Configuracion;
import com.example.zer.somos.utilidades.DatosIngreso;
import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListarVigentesActivity extends CierreVentas
        implements IOperacionDatosRegistroPrepago, IRespuestaConfirmar {

    public static final String PROCESAR_PLACA = "PROCESAR_PLACA";
    public static final int CODIGO_RESPUESTA = 101;
    public static final int CODIGO_RESULTADO_INICIAR_POSTPAGO = 10;
    private final static String ACCION_PROBLEMAS_CONEXION = "ACCION_PROBLEMAS_CONEXION";
    private static DialogFragment fragmentoMensajeSinRegistros;
    private static DialogFragment fragmentoProblemasConexion;
    private final int INTERVALO_TEMPORIZADO_BUSCAR_ZONA = 1000 * 60 * 10; // 10 minutos
    private EditText zona;
    private Button buscarPorZona;
    private AlertasPrepagoAPI alertasPrepagoAPI;
    private Handler handlerBuscarZona;
    private Runnable runnableBuscarZona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_buscar_por_zona);
            iniciarComponentes();
            programarEventos();
            programarTemporizadorBuscarZona();
            buscarPorZona();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void iniciarComponentes() {
        try {
            super.iniciarComponentes();
            zona = findViewById(R.id.zona);
            buscarPorZona = findViewById(R.id.buscarPorZona);
            zona.setText(GlobalPermisos.getDatosSesionActual().getNombreZona());
            alertasPrepagoAPI = new AlertasPrepagoAPI();
            handlerBuscarZona = new Handler();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void programarEventos() {
        try {
            super.programarEventos();
            buscarPorZona.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buscarPorZona();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void programarTemporizadorBuscarZona() {
        try {
            if (handlerBuscarZona == null) {
                detenerTemporizadorBuscarZona();
                return;
            }
            runnableBuscarZona = new Runnable() {
                public void run() {
                    try {
                        if (handlerBuscarZona == null) {
                            detenerTemporizadorBuscarZona();
                            return;
                        }
                        buscarPorZona();
                        handlerBuscarZona.postDelayed(runnableBuscarZona, INTERVALO_TEMPORIZADO_BUSCAR_ZONA);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };
            handlerBuscarZona.postDelayed(runnableBuscarZona, INTERVALO_TEMPORIZADO_BUSCAR_ZONA);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void detenerTemporizadorBuscarZona() {
        handlerBuscarZona = null;
        runnableBuscarZona = null;
    }

    /**
     *
     */
    private void buscarPorZona() {
        try {
            limpiarListado();
            closeMensajeNoHayDatosListadosZona();
            cerrarMensajeProblemasConConexion();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            solicitarDatosServidorZona();
            if (zona == null) {
                return;
            }
            ocultarTeclado(zona.getWindowToken());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void limpiarListado() {
        try {
            List<DatosRegistro> datosRegistros = new ArrayList<>();
            DatosRegistroAdaptador datosRegistroAdaptador = new DatosRegistroAdaptador(
                    datosRegistros,
                    this,
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

    private void solicitarDatosServidorZona() {
        try {
            setDialog(true);
            cierreVentasAPI.solicitarCarteraZona(this,
                    this,
                    CierreVentasAPI.Acciones.CARTERA_ZONA,
                    GlobalPermisos.getDatosSesionActual().getCuenta(),
                    GlobalPermisos.getDatosSesionActual().getIdZona());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<DatosRegistro> getListadoPorZona(JSONArray jsonArray) {
        List<DatosRegistro> result = new ArrayList<>();
        try {
            if (jsonArray == null) {
                return result;
            }
            try {
                JSONArray preli = jsonArray;
                for (int pos = 0; pos < preli.length(); pos++) {
                    DatosRegistro datosRegistro = new DatosRegistro(preli.getJSONObject(pos));
                    result.add(datosRegistro);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private void closeMensajeNoHayDatosListadosZona() {
        try {
            if (fragmentoMensajeSinRegistros != null) {
                fragmentoMensajeSinRegistros.dismiss();
            }
        } catch (Exception ex) {
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
                    MensajeAceptar.Colores.ROJO

            );
            fragmentoProblemasConexion.setCancelable(false);
            fragmentoProblemasConexion.show(getSupportFragmentManager(), "OK");
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
            closeMensajeNoHayDatosListadosZona();
            fragmentoMensajeSinRegistros = new MensajeAceptar(
                    "No hay vehículos en la zona.",
                    "Aceptar",
                    MensajeAceptar.Colores.ROJO);
            fragmentoMensajeSinRegistros.setCancelable(false);
            fragmentoMensajeSinRegistros.show(getSupportFragmentManager(), "OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Verifica si hay registros para listar, si no muestra un mensaje y retorna false
     * sino, retorna true.
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
            mensajeNoHayDatosListadosZona();
        }
        return continuar;
    }

    /**
     * Procesa los regitros de los vehículos de la zona
     * que van a ser listados.
     *
     * @param jsonArray
     */
    private void cargarListadoCarteraZona(JSONArray jsonArray) {
        try {
            List<DatosRegistro> datosRegistros = getListadoPorZona(jsonArray);
            if (!procesarMensajeSinRegistros(datosRegistros)) {
                return;
            }
            DatosRegistroAdaptador datosRegistroAdaptador = new DatosRegistroAdaptador(
                    datosRegistros,
                    this,
                    this,
                    this);
            try {
                listado.setAdapter(datosRegistroAdaptador);
                datosRegistroAdaptador.registerDataSetObserver(new DataSetObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Do something after 200ms
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

    //-------------------------------------------------------
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

    private void mensajeConfirmarDatos(DatosRegistro datosRegistro) {
        try {
            final String ENTER = "\n";
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("Fecha: ");
            mensaje.append(Configuracion.getFechaHoraActual());
            mensaje.append(ENTER);
            mensaje.append("Placa: ");
            mensaje.append(datosRegistro.getPlaca());
            mensaje.append(ENTER);
            mensaje.append("Vehículo: ");
            mensaje.append(DatosIngreso.getTipoPlaca(datosRegistro.getPlaca()) == DatosIngreso.PLACA_CARRO ? "Carro" : "Moto");
            mensaje.append(ENTER);
            long valorHora = (DatosIngreso.getTipoPlaca(datosRegistro.getPlaca()) == DatosIngreso.PLACA_CARRO) ? GlobalPermisos.getDatosSesionActual().getValorHoraCarro() : GlobalPermisos.getDatosSesionActual().getValorHoraMoto();
            mensaje.append("Valor hora: $");
            mensaje.append(valorHora);
            mensaje.append(ENTER);
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

    @Override
    protected void salir() {
        try {
            detenerTemporizadorBuscarZona();
            super.salir();
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

    //-------------------------------------------------------

    @Override
    public void respuestaJSON(String accion, int statusCode, Header[] headers, byte[] responseBytes, String responseString, JSONObject responseJSON, JSONArray responseArrayJSON) {
        try {
            super.respuestaJSON(accion, statusCode, headers, responseBytes, responseString, responseJSON, responseArrayJSON);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        switch (accion) {
            case CierreVentasAPI.Acciones.CARTERA_ZONA:
                setDialog(true);
                cargarListadoCarteraZona(responseArrayJSON);
                break;
            case CierreVentasAPI.Acciones.PAGO_EXTEMPORANEO:
            case CierreVentasAPI.Acciones.CONFIRMAR_PAGO:
                setDialog(false);
                limpiarListado();
                terminarPagoExitoso(responseJSON);
                break;
            case AlertasPrepagoAPI.Acciones.ACTUALIZAR_CUPO_DISPONIBLE_PREPAGO:
            case CierreVentasAPI.Acciones.REPORTAR_VEHICULO:
                setDialog(false);
                limpiarListado();
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
            case CierreVentasAPI.Acciones.CARTERA_ZONA:
                setDialog(false);
                limpiarListado();
                mensajeProblemasConConexion();
                break;
            case AlertasPrepagoAPI.Acciones.ACTUALIZAR_CUPO_DISPONIBLE_PREPAGO:
            case CierreVentasAPI.Acciones.REPORTAR_VEHICULO:
            case CierreVentasAPI.Acciones.PAGO_EXTEMPORANEO:
            case CierreVentasAPI.Acciones.CONFIRMAR_PAGO:
                setDialog(false);
                limpiarListado();
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
            case AlertasPrepagoAPI.Acciones.ACTUALIZAR_CUPO_DISPONIBLE_PREPAGO:
                solicitarActualizarCupoPrepago((DatosRegistro) contenido);
                break;
            case ACCION_PROBLEMAS_CONEXION:
                buscarPorZona();
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
            case AlertasPrepagoAPI.Acciones.ACTUALIZAR_CUPO_DISPONIBLE_PREPAGO:
                solicitarActualizarCupoPrepago((DatosRegistro) contenido);
                break;
            case ACCION_PROBLEMAS_CONEXION:
                salir();
                break;
        }
    }

    @Override
    public void respuestaSI() {
        buscarPorZona();
    }

    @Override
    public void respuestaNO() {
        buscarPorZona();
    }
}