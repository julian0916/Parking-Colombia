package com.example.zer.somos.comunes;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;

import com.example.zer.somos.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import static com.example.zer.somos.comunes.PrinterCommands.*;
/**
 * Created by Steven Marín Vallejo
 */

public class BluetoothPrint {
    private static Context context;
    private Context mContext;
    PrinterCommands printerCommands;
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket bluetoothSocket;
    BluetoothDevice bluetoothDevice;

    OutputStream outputStream;
    InputStream inputStream;
    Thread thread;

    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;



    public BluetoothPrint(Context context) {
        this.context = context;

        //this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        this.bluetoothAdapter = bluetoothManager.getAdapter();
    }
    public static void setContext(Context cntxt) {
        context = cntxt;
    }

    public static Context getContext() {
        return context;
    }
    public boolean isConnected() {
        return bluetoothSocket != null && bluetoothSocket.isConnected();
    }
    public boolean FindBluetoothDevice() {
        String nombreImpresora = "";
        String direccionImpresora = "00:AA:11:BB:22:CC";
        try {

            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                Log.d("Bluetooth adapter", "No Bluetooth Adapter found");
            }
            if (bluetoothAdapter.isEnabled()) {
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                enableBT.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(enableBT);
            }

            Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();

            if (pairedDevice != null && pairedDevice.size() > 0) {
                for (BluetoothDevice pairedDev : pairedDevice) {

                    if (pairedDev.getName().length() > 5) {
                        nombreImpresora = pairedDev.getName().substring(0, 5);
                    } else {
                        nombreImpresora = pairedDev.getName();
                    }

                    if (nombreImpresora.equals("XXZTJ")) { //Aca se realiza la accion dependiendo de
                        // la impresora coextada, como son 3 impresoras diferentes entonces haremos
                        // la misma accion
                        bluetoothDevice = pairedDev;
                        return true;
                    }
                }
            }

            //lblPrinterName.setText("Bluetooth Printer Attached");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }


    // Open Bluetooth Printer

    public void openBluetoothPrinter() {
        try {
            //Standard uuid from string //
            UUID uuidSting = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuidSting);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();

            beginListenData();

        } catch (Exception ex) {

        }
    }


    public boolean checkConnection() {
        if (bluetoothSocket != null) {
            if (bluetoothSocket.isConnected()) {
                Toast.makeText(context, "Estoy aqui esta conectado", Toast.LENGTH_LONG).show();

                return true;
            }
        }
        return false;
    }

    public void beginListenData() {
        try {

            final Handler handler = new Handler();
            final byte delimiter = 10;
            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                        try {
                            int byteAvailable = inputStream.available();
                            if (byteAvailable > 0) {
                                byte[] packetByte = new byte[byteAvailable];
                                inputStream.read(packetByte);

                                for (int i = 0; i < byteAvailable; i++) {
                                    byte b = packetByte[i];
                                    if (b == delimiter) {
                                        byte[] encodedByte = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedByte, 0,
                                                encodedByte.length
                                        );
                                        final String data = new String(encodedByte, "US-ASCII");
                                        readBufferPosition = 0;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                //lblPrinterName.setText(data);
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            stopWorker = true;
                        }
                    }

                }
            });

            thread.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void disconnectPrinter() {
        try {
            stopWorker = true;
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
            }
            // Notificar que la impresora se ha desconectado
            if (!isConnected()) {
                Toast.makeText(context, "Impresora desconectada", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }







    // Printing Text to Bluetooth Printer //
    public void printData(String content) {
        try {
            if (!isConnected()) {
                // Si la conexión se ha perdido, intenta reconectar
                openBluetoothPrinter();
            }
            outputStream.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            // Manejo de la excepción
            Toast.makeText(context, "Error al imprimir: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            // Intenta reconectar la impresora
            openBluetoothPrinter();
        }
    }

    public void printDataCentered(String contenido) {
        try {
            //outputStream.write(ESC_HORIZONTAL_CENTERS);
            outputStream.write(SELECT_FONT_A[2]);
            String msg = "       " + contenido;
            outputStream.write(msg.getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    public void printDataBold(String contenido) {
        try {
            //outputStream.write(ESC_CANCEL_BOLD);
            outputStream.write(contenido.getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    // Disconnect Printer //
    public void disconnectBT() {
        try {
            stopWorker = true;
            outputStream.close();
            inputStream.close();
            bluetoothSocket.close();
            //lblPrinterName.setText("Printer Disconnected.");
        } catch (Exception e) {
        }
    }
    public void printPhoto(int img) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(getContext().getResources(),
                    img);
            if(bmp!=null){
                byte[] command = UtilPrinter.decodeBitmap(bmp);
                outputStream.write(ESC_ALIGN_CENTER);
                outputStream.write(command);

            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
            Toast.makeText(context,"Ahora estoy aca en imagen"+e,Toast.LENGTH_LONG).show();
        }
    }

    public static void printImage() {
        PrintHelper photoPrinter = new PrintHelper(context);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);

        // Cambia el nombre de la imagen si es necesario o usa otro método para obtener el recurso Drawable
        Drawable drawable = context.getResources().getDrawable(R.drawable.somos_blanco_bmp);
        Bitmap bitmap = drawableToBitmap(drawable);

        if (bitmap != null) {
            photoPrinter.printBitmap("Prueba", bitmap);
        } else {
            // Manejar el caso en que no se pudo cargar la imagen
            Toast.makeText(context, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para convertir un Drawable a un objeto Bitmap
    private static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}