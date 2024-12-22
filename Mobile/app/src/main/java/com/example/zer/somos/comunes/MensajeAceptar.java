package com.example.zer.somos.comunes;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.example.zer.somos.R;

public class MensajeAceptar extends DialogFragment {
    private String mensaje = "";
    private String textoBoton = "";
    private String titulo = "Información";
    private int estiloColor = R.style.cust_dialog_azul;
    private IRespuestaConfirmar iRespuestaConfirmar;

    public static class Colores{
        public  final static int AMARILLO=R.style.cust_dialog_amarillo;
        public  final static int AZUL=R.style.cust_dialog_azul;
        public  final static int ROJO=R.style.cust_dialog_rojo;
        public  final static int VERDE=R.style.cust_dialog_verde;
        public  final static int AZUL_CLARO=R.style.cust_dialog_azul_claro;
        private final static int getColor(int color){
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

    public MensajeAceptar(String mensaje, String textoBoton, int estiloColor) {
        this.mensaje = mensaje;
        this.textoBoton = textoBoton;
        this.estiloColor = Colores.getColor(estiloColor);
    }

    public MensajeAceptar(String mensaje, String textoBoton, String titulo,int estiloColor) {
        this.mensaje = mensaje;
        this.textoBoton = textoBoton;
        this.titulo = titulo;
        this.estiloColor = Colores.getColor(estiloColor);
    }

    public MensajeAceptar(
            String mensaje,
            String textoBoton,
            String titulo,
            IRespuestaConfirmar iRespuestaConfirmar,
            int estiloColor) {
        this.mensaje = mensaje;
        this.textoBoton = textoBoton;
        this.titulo = titulo;
        this.iRespuestaConfirmar = iRespuestaConfirmar;
        this.estiloColor = Colores.getColor(estiloColor);
    }

    public boolean getRespuesta(){
        return true;
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
        builder.setTitle(titulo);
        builder.setMessage(mensaje)
                .setPositiveButton(textoBoton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (iRespuestaConfirmar != null) {
                            iRespuestaConfirmar.respuestaSI();
                        }
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}