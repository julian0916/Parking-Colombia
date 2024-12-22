package com.example.zer.somos.comunes;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.example.zer.somos.R;

public class MensajeConfirmarAccion extends DialogFragment {

    private String mensaje = "";
    private String textoBotonSI = "";
    private String textoBotonNO = "";
    private String titulo = "Información";
    private IRespuestaConfirmarAccion iRespuestaConfirmarAccion;
    private String accion;
    private Object contenido;
    private int estiloColor = R.style.cust_dialog_azul;


    public MensajeConfirmarAccion(String accion,
                                  String mensaje,
                                  String textoBotonSI,
                                  String textoBotonNO,
                                  String titulo,
                                  Object contenido,
                                  IRespuestaConfirmarAccion iRespuestaConfirmarAccion) {
        iniciarVariables(
                accion,
                mensaje,
                textoBotonSI,
                textoBotonNO,
                titulo,
                contenido,
                iRespuestaConfirmarAccion,
                Colores.AZUL);
    }

    public MensajeConfirmarAccion(String accion,
                                  String mensaje,
                                  String textoBotonSI,
                                  String textoBotonNO,
                                  String titulo,
                                  Object contenido,
                                  IRespuestaConfirmarAccion iRespuestaConfirmarAccion,
                                  int estiloColor) {
        iniciarVariables(
                accion,
                mensaje,
                textoBotonSI,
                textoBotonNO,
                titulo,
                contenido,
                iRespuestaConfirmarAccion,
                estiloColor);
    }

    private void iniciarVariables(String accion,
                                  String mensaje,
                                  String textoBotonSI,
                                  String textoBotonNO,
                                  String titulo,
                                  Object contenido,
                                  IRespuestaConfirmarAccion iRespuestaConfirmarAccion,
                                  int estiloColor) {
        this.contenido = contenido;
        this.accion = accion;
        this.mensaje = mensaje;
        this.textoBotonSI = textoBotonSI;
        this.textoBotonNO = textoBotonNO;
        this.titulo = titulo;
        this.iRespuestaConfirmarAccion = iRespuestaConfirmarAccion;
        this.estiloColor = Colores.getColor(estiloColor);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, this.estiloColor);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), this.estiloColor);
        builder.setMessage(mensaje)
                .setPositiveButton(textoBotonSI, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (iRespuestaConfirmarAccion == null) {
                            return;
                        }
                        iRespuestaConfirmarAccion.respuestaSI(accion, contenido);
                    }
                })
                .setNegativeButton(textoBotonNO, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (iRespuestaConfirmarAccion == null) {
                            return;
                        }
                        iRespuestaConfirmarAccion.respuestaNO(accion, contenido);
                    }
                })
                .setCancelable(false)
                .setTitle(titulo);
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public static class Colores {
        public final static int AMARILLO = R.style.cust_dialog_amarillo;
        public final static int AZUL = R.style.cust_dialog_azul;
        public final static int ROJO = R.style.cust_dialog_rojo;
        public final static int VERDE = R.style.cust_dialog_verde;
        public final static int AZUL_CLARO = R.style.cust_dialog_azul_claro;

        private final static int getColor(int color) {
            switch (color) {
                case AMARILLO:
                    return AMARILLO;
                case AZUL:
                    return AZUL;
                case ROJO:
                    return ROJO;
                case VERDE:
                    return VERDE;
                case AZUL_CLARO:
                    return AZUL_CLARO;
                default:
                    return AZUL;
            }
        }
    }
}