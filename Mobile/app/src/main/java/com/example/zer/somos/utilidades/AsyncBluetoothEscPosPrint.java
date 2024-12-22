package com.example.zer.somos.utilidades;

import android.content.Context;

import com.example.zer.somos.utilidades.connection.DeviceConnection;
import com.example.zer.somos.utilidades.connection.bluetooth.BluetoothConnection;
import com.example.zer.somos.utilidades.connection.bluetooth.BluetoothPrintersConnections;
import com.example.zer.somos.utilidades.exceptions.EscPosConnectionException;


public class AsyncBluetoothEscPosPrint extends com.example.zer.somos.utilidades.AsyncEscPosPrint{
    public AsyncBluetoothEscPosPrint(Context context) {
        super(context);
    }

    public AsyncBluetoothEscPosPrint(Context context, OnPrintFinished onPrintFinished) {
        super(context, onPrintFinished);
    }

    protected PrinterStatus doInBackground(AsyncEscPosPrinter... printersData) {
        if (printersData.length == 0) {
            return new PrinterStatus(null, AsyncEscPosPrint.FINISH_NO_PRINTER);
        }

        AsyncEscPosPrinter printerData = printersData[0];
        DeviceConnection deviceConnection = printerData.getPrinterConnection();

        this.publishProgress(AsyncEscPosPrint.PROGRESS_CONNECTING);

        if (deviceConnection == null) {
            try {
                printersData[0] = new AsyncEscPosPrinter(
                        BluetoothPrintersConnections.selectFirstPaired(),
                        printerData.getPrinterDpi(),
                        printerData.getPrinterWidthMM(),
                        printerData.getPrinterNbrCharactersPerLine()
                );
            } catch (EscPosConnectionException escPosConnectionException) {
                escPosConnectionException.printStackTrace();
            }
            printersData[0].setTextsToPrint(printerData.getTextsToPrint());
        } else {
            try {
                deviceConnection.connect();
            } catch (EscPosConnectionException escPosConnectionException) {
                escPosConnectionException.printStackTrace();
            }
        }

        return super.doInBackground(printersData);
    }
}
