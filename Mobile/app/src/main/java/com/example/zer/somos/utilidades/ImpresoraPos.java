package com.example.zer.somos.utilidades;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.widget.Toast;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class ImpresoraPos {

    private static final UUID UUID_SPP = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // UUID para SPP
    private Context context;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;




    public ImpresoraPos(Context context) {
        this.context = context;
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        this.bluetoothAdapter = bluetoothManager.getAdapter();

    }

    public boolean isConnected() {
        return bluetoothSocket != null && bluetoothSocket.isConnected();
    }
    private void mostrarDialogoConexion(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Conexión a la impresora");
        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acción cuando el usuario presiona "Aceptar"
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }




    public void conectar() {

        if (bluetoothAdapter == null) {
            mostrarDialogoConexion("Bluetooth no es soportado en este dispositivo");
            return;
        }

        // Obtener la lista de dispositivos Bluetooth emparejados
        Set<BluetoothDevice> dispositivosEmparejados = bluetoothAdapter.getBondedDevices();
        if (dispositivosEmparejados.isEmpty()) {
            mostrarDialogoConexion("No hay dispositivos Bluetooth emparejados");
            return;
        }

        // Seleccionar el primer dispositivo Bluetooth de la lista
        BluetoothDevice dispositivo = dispositivosEmparejados.iterator().next();
        if (dispositivo == null) {
            mostrarDialogoConexion("No se pudo seleccionar el dispositivo Bluetooth");
            return;
        }

        try {
            bluetoothSocket = dispositivo.createRfcommSocketToServiceRecord(UUID_SPP);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
            Toast.makeText(context, "Conexión establecida con la impresora", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarDialogoConexion("Por favor encienda o conecte la impresora");
        }
    }

    public void desconectar() {
        if (bluetoothSocket != null) {
            try {
                outputStream.close();
                bluetoothSocket.close();
                Toast.makeText(context, "Desconexión exitosa", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error al desconectar la impresora", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void print(String text, boolean centerText,  boolean boldText, boolean bigText, boolean smallText) {
        if (outputStream != null) {
            try {
                // Comando para centrar el texto
                if (centerText) {
                    byte[] comandoCentrado = new byte[]{0x1B, 'a', 0x01};
                    outputStream.write(comandoCentrado);
                }
                if (!centerText) {
                    byte[] comandoIzquierda = new byte[]{0x1B, 'a', 0x00};
                    outputStream.write(comandoIzquierda);
                }
                if(boldText){
                    byte[] comandoNegrita = new byte[]{0x1B, 0x45, 0x01};
                    outputStream.write(comandoNegrita);
                }

                // Comando para tamaño de fuente grande
                if (bigText) {
                   byte[] comandoTamanioGrande = new byte[]{0x1B, 0x21, 0x12};
                    outputStream.write(comandoTamanioGrande);
                }

                // Comando para tamaño de fuente pequeño
                if (smallText) {
                    byte[] comandoTamanioPequeno = new byte[]{0x1B, 0x21, 0x01};
                    outputStream.write(comandoTamanioPequeno);
                }

                // Escribir el texto
                outputStream.write(text.getBytes());

                // Comando para alinear a la izquierda (para volver a la alineación predeterminada después de imprimir)
                byte[] comandoIzquierda = new byte[]{0x1B, 'a', 0x00};
                outputStream.write(comandoIzquierda);

                // Comando para tamaño de fuente predeterminado (para volver a la configuración de tamaño de fuente predeterminada después de imprimir)
                byte[] comandoTamanioPredeterminado = new byte[]{0x1B, 0x21, 0x00};
                outputStream.write(comandoTamanioPredeterminado);

                outputStream.flush();
                //Toast.makeText(context, "Impresión exitosa", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                //Toast.makeText(context, "Error al imprimir", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Toast.makeText(context, "No hay conexión con la impresora", Toast.LENGTH_SHORT).show();
        }
    }
    public void printUnderline(String text, boolean centerText) {
        if (outputStream != null) {
            try {
                // Comando para centrar el texto
                if (centerText) {
                    byte[] comandoCentrado = new byte[]{0x1B, 'a', 0x01};
                    outputStream.write(comandoCentrado);
                } else {
                    byte[] comandoIzquierda = new byte[]{0x1B, 'a', 0x00};
                    outputStream.write(comandoIzquierda);
                }

                // Comando para subrayar el texto
                byte[] comandoSubrayado = new byte[]{0x1B, 0x2D, 0x01};
                outputStream.write(comandoSubrayado);

                // Escribir el texto
                outputStream.write(text.getBytes());

                // Comando para desactivar el subrayado después de imprimir
                byte[] comandoSinSubrayado = new byte[]{0x1B, 0x2D, 0x00};
                outputStream.write(comandoSinSubrayado);

                // Comando para alinear a la izquierda (para volver a la alineación predeterminada después de imprimir)
                byte[] comandoIzquierda = new byte[]{0x1B, 'a', 0x00};
                outputStream.write(comandoIzquierda);

                outputStream.flush();
                //Toast.makeText(context, "Impresión exitosa", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                //Toast.makeText(context, "Error al imprimir", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Toast.makeText(context, "No hay conexión con la impresora", Toast.LENGTH_SHORT).show();
        }
    }
    public void addEnter() {
        if (outputStream != null) {
            try {
                outputStream.write("\n".getBytes());
                outputStream.flush();
               // Toast.makeText(context, "Salto de línea agregado", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
               // Toast.makeText(context, "Error al agregar salto de línea", Toast.LENGTH_SHORT).show();
            }
        } else {
            //Toast.makeText(context, "No hay conexión con la impresora", Toast.LENGTH_SHORT).show();
        }
    }
    public Bitmap generarCodigoQR(String texto) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(texto, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            return bitmap;

        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void printQR(String texto) {
        Bitmap bitmap = generarCodigoQR(texto.toString());
        if (bitmap != null) {
            // Convertir el bitmap en un arreglo de bytes en formato PNG
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] imagenBytes = stream.toByteArray();

            // Imprimir los bytes de la imagen
            try {
                outputStream.write(imagenBytes);
                outputStream.flush();
                Toast.makeText(context, "Código QR impreso correctamente", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error al imprimir el código QR", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Error al generar el código QR", Toast.LENGTH_SHORT).show();
        }
    }

    public void printQrCode(Bitmap qRBit) {
        try {
            PrintPic printPic1 = PrintPic.getInstance();
            printPic1.init(qRBit);
            byte[] bitmapdata2 = printPic1.printDraw();
            outputStream.write(bitmapdata2);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
