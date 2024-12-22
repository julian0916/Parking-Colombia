package com.example.zer.somos.utilidades;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.util.Log;


import java.text.SimpleDateFormat;
import java.util.List;


import com.example.zer.somos.R;
import com.example.zer.somos.comunes.BluetoothPrint;
import com.example.zer.somos.utilidades.connection.DeviceConnection;
import com.example.zer.somos.utilidades.textparser.PrinterTextParserImg;

import java.io.OutputStream;

import java.util.Set;
import java.util.UUID;

/**
 * Clase que permite hacer la impresión de contenido
 * usando impresora Buetooth
 */
public class ConectorImpresoraBT {
    private static BluetoothDevice mmDevice = null;
    private static UUID lastUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//éste es el más genérico
    private BluetoothAdapter mBluetoothAdapter;
    private OutputStream mmOutputStream;
    private BluetoothSocket mmSocket;
    private IRespuestaImpresion iRespuestaImpresion;
    private ContenidoImpresoraBT contenidoImpresoraBT;
    private Context contexto;
    private String mensaje;
    private BluetoothPrint bluetoothPrint;



    /**
     * Asynchronous printing
     */
    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection deviceConnection) {
        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(deviceConnection, 203, 48f, 32);
        return printer.addTextToPrint(
                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.contexto.getDrawable(R.drawable.logo)) + "</img>\n" +
                        "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.contexto.getDrawable(R.drawable.logo2) )+ "</img>\n" +
                        "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.contexto.getDrawable(R.drawable.logo3))+ "</img>\n" +
                        "[L]\n" +
                        "[C]<qrcode size='20'>https://somosmovilidad.gov.co/</qrcode>\n"
        );
    }

    public ConectorImpresoraBT(Context contexto) {
        bluetoothPrint = new BluetoothPrint(contexto);
        // Opcional: inicializa la clase BluetoothPrint con el contexto si es necesario
        // bluetoothPrint.setContext(context);
    }


    public void imprimir() {
        ConectorImpresoraBT conectorImpresoraBT = this;
        Thread t = new Thread() {
            public void run() {
                try {
                    if (contenidoImpresoraBT == null) {
                        throw new Exception("Contenido para imprimir incorrecto o incompleto");
                    }
                    List<ContenidoImpresoraBT.Contenido> contenidos = contenidoImpresoraBT.getContenidos();
                    if (contenidos == null) {
                        throw new Exception("Contenido para imprimir incorrecto o incompleto");
                    }
                    if (contenidos.size() < 1) {
                        throw new Exception("Contenido para imprimir incorrecto o incompleto");
                    }
                    findBT(conectorImpresoraBT.contexto, contenidos);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    conectorImpresoraBT.setMensaje("Error intentando imprimir.\n" + ex.getMessage());
                   iRespuestaImpresion.impresionFallo(conectorImpresoraBT);

                }
            }
        };
        t.start();

    }

    private void mostrarDialogoReintentarImpresion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setMessage("La impresión no se completó en el tiempo límite. ¿Desea intentar nuevamente?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // El usuario eligió reintentar la impresión.
                        imprimir(contexto, contenidoImpresoraBT, iRespuestaImpresion);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // El usuario optó por no reintentar la impresión.
                        iRespuestaImpresion.impresionFallo(ConectorImpresoraBT.this);
                    }
                });

        // Muestra el diálogo.
        builder.create().show();
    }

    public void imprimir(Context pContext,
                         ContenidoImpresoraBT contenidoImpresoraBT,
                         IRespuestaImpresion iRespuestaImpresion) {
        try {
            this.contexto = pContext;
            this.contenidoImpresoraBT = contenidoImpresoraBT;
            this.iRespuestaImpresion = iRespuestaImpresion;
            imprimir();
        } catch (Exception ex) {
            ex.printStackTrace();
            this.setMensaje("Error intentando imprimir.\n" + ex.getMessage());
            iRespuestaImpresion.impresionFallo(this);
        }
    }

    private void findBT(Context pContext, List<ContenidoImpresoraBT.Contenido> contenidos) throws Exception {
        try {
            BluetoothManager bluetoothAdapter = (BluetoothManager)
                    pContext.getSystemService(Context.BLUETOOTH_SERVICE);
            this.mBluetoothAdapter = bluetoothAdapter.getAdapter();
            if (this.mBluetoothAdapter == null) {
                throw new Exception("El dispositivo no cuenta con bluetooth");
            }
            if (!this.mBluetoothAdapter.isEnabled()) {
                ((Activity) pContext).startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 0);
            }
            boolean detectoImpresora = false;
            boolean seImprimio = false;
            //Utiliza la ultima configuración que funciono
            //para hacer más eficiente el proceso
            if (mmDevice != null) {
                detectoImpresora = true;
                seImprimio = openBT(contenidos);
            }
            if (!seImprimio) {
                detectoImpresora = false;
                Set<BluetoothDevice> pairedDevices = this.mBluetoothAdapter.getBondedDevices();
                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {
                        if (!seImprimio && (BluetoothClass.Device.Major.IMAGING == device.getBluetoothClass().getMajorDeviceClass())) {
                            detectoImpresora = true;
                            mmDevice = device;
                            seImprimio = openBT(contenidos);
                        }
                    }
                }
            }
            if (!detectoImpresora) {
                throw new Exception("No se detectó ninguna impresora BlueTooth");
            }
            if (!seImprimio) {

                throw new Exception("No fue posible imprimir, revice que la impresora esté correctamente conectada");
            }
        } catch (Exception exImprimiendo) {
            exImprimiendo.printStackTrace();
            throw exImprimiendo;
        }
    }

    private boolean openBT(List<ContenidoImpresoraBT.Contenido> contenidos) throws Exception {
        boolean exitoEnOperacion = false;
        try {
            boolean conectoUltimoUUID = false;
            if (lastUUID != null) {
                mmSocket = mmDevice.createRfcommSocketToServiceRecord(lastUUID);
                try {
                    mmSocket.connect();
                    conectoUltimoUUID = mmSocket.isConnected();
                } catch (Exception exConn) {
                }
            }
            /*if (!conectoUltimoUUID) {
                ParcelUuid[] uuid = mmDevice.getUuids();
                int posUUID = 0;
                final int NUM_UUIDS = uuid.length;
                while (posUUID < NUM_UUIDS) {
                    lastUUID = uuid[posUUID].getUuid();
                    mmSocket = mmDevice.createRfcommSocketToServiceRecord(lastUUID);
                    try {
                        mmSocket.connect();
                        if (!mmSocket.isConnected()) {
                            posUUID++;
                        } else {
                            posUUID = NUM_UUIDS;
                        }
                    } catch (Exception exConn) {
                        posUUID++;
                    }
                }
            }*/
            if (!mmSocket.isConnected()) {
                Log.d("Conexión Bluetooth", "Buscando impresora... ");
                throw new Exception("No fue posible la conexión con la impresora");
            }
            Log.d("Conexión Bluetooth", "Conexión exitosa con impresora.");
            mmOutputStream = mmSocket.getOutputStream();
            imprimir(contenidos);
            exitoEnOperacion = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            exitoEnOperacion = false;
        }
        return exitoEnOperacion;
    }

    private void imprimir(List<ContenidoImpresoraBT.Contenido> contenidos) {
        ConectorImpresoraBT conectorImpresoraBT = this;
        try {
            Thread t = new Thread() {
                public void run() {
                    boolean imprimioTodo = true;
                    boolean entroImprimir = false;
                    try {
                        // Verificar si la conexión está activa antes de imprimir
                        if (!bluetoothPrint.isConnected()) {
                            // Intentar reconectar la impresora
                            bluetoothPrint.openBluetoothPrinter();
                        }
                        for (ContenidoImpresoraBT.Contenido contenido : contenidos) {
                            OutputStream os = mmSocket.getOutputStream();
                            os.write(contenido.getAlineacion());
                            os.write(contenido.getFormato());
                            byte[] buffer = contenido.getTexto();
                            os.write(buffer, 0, buffer.length);
                            os.flush();
                            entroImprimir = true;
                            imprimioTodo &= true;
                        }
                        if (mmOutputStream != null) {
                            mmOutputStream.close();
                        }
                        if (mmSocket != null && mmSocket.isConnected()) {
                            mmSocket.close();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        imprimioTodo = false;
                        // Manejar la pérdida de conexión durante la impresión
                        bluetoothPrint.disconnectPrinter();
                    }
                    if (!imprimioTodo || !entroImprimir) {
                        conectorImpresoraBT.setMensaje("No imprimió todo el contenido");
                        iRespuestaImpresion.impresionFallo(conectorImpresoraBT);
                    } else {
                        iRespuestaImpresion.impresionExitosa();
                    }
                }
            };
            t.start();
        } catch (Exception exception) {
            exception.printStackTrace();
            // Manejar la excepción
            bluetoothPrint.disconnectPrinter();
            iRespuestaImpresion.impresionFallo(this);
        }
    }


    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

}
