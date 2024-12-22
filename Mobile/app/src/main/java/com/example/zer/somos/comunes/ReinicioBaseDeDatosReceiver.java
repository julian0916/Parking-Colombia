package com.example.zer.somos.comunes;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

public class ReinicioBaseDeDatosReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Actualiza todos los registros en la tabla mi_tabla
        ContentValues values = new ContentValues();
        values.put("total_to_pay", 0);  // Establece total_to_pay a 0
        values.put("freeze", 0);        // Establece freeze a 0 (equivale a false en SQLite)

        int rowsUpdated = db.update("freeze_worker", values, null, null);

        // rowsUpdated contiene el número de registros actualizados
    }
}