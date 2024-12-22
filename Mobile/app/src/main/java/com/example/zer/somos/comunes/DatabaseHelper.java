package com.example.zer.somos.comunes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.os.Build;
import android.util.Log;
import com.example.zer.somos.permisos.GlobalPermisos;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.opencsv.CSVWriter;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "zer_local_database";
    private static final int DATABASE_VERSION = 7;
    private static final String TABLE_NAME = "freeze_worker";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_TOTAL_TO_PAY = "total_to_pay";
    private static final String COLUMN_FREEZE = "freeze";
    private static final String COLUMN_CREATION_DATE = "creation_date";
    private static final String COLUMN_FREEZE_COUNT = "freeze_count";
    private static final String COLUMN_PLACA = "placa";
    private static final String COLUMN_ACUMULADO_ANTERIOR = "acumulado_anterior";
    private static final String COLUMN_ACUMULADO_ACTUAL = "acumulado_actual";
    private static final String COLUMN_PREPAGO = "tiquetes_prepago";
    private static final String COLUMN_POSTPAGO = "tiquetes_postpago";
    private static final String COLUMN_EGRESO = "tiquetes_egreso";
    private static final String COLUMN_REIMPRESO_EGRESO = "tiquetes_reimpreso_egreso";
    private static final String COLUMN_REIMPRESO_PREPAGO = "tiquetes_reimpreso_prepago";
    private static final String COLUMN_REIMPRESO_POSTPAGO = "tiquetes_reimpreso_postpago";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_TOTAL_TO_PAY + " INTEGER DEFAULT 0,"
                + COLUMN_FREEZE + " INTEGER DEFAULT 0,"
                + COLUMN_CREATION_DATE + " INTEGER,"
                + COLUMN_FREEZE_COUNT + " INTEGER DEFAULT 0,"
                + COLUMN_PREPAGO + " INTEGER DEFAULT 0,"  // Definición de la columna tiquetes_prepago
                + COLUMN_POSTPAGO + " INTEGER DEFAULT 0,"
                + COLUMN_EGRESO+ " INTEGER DEFAULT 0,"
                + COLUMN_REIMPRESO_EGRESO+ " INTEGER DEFAULT 0,"
                + COLUMN_REIMPRESO_PREPAGO+ " INTEGER DEFAULT 0,"
                + COLUMN_REIMPRESO_POSTPAGO+ " INTEGER DEFAULT 0"
                + ")";

        db.execSQL(CREATE_TABLE);
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        ContentValues valoresPorDefecto = new ContentValues();
        valoresPorDefecto.put(COLUMN_CREATION_DATE, currentDay);
        valoresPorDefecto.put(COLUMN_USER_ID, GlobalPermisos.getDatosSesionActual().getIdPromotor());
        valoresPorDefecto.put(COLUMN_TOTAL_TO_PAY, 0);
        valoresPorDefecto.put(COLUMN_FREEZE, 0);
        valoresPorDefecto.put(COLUMN_FREEZE_COUNT, 0);
        valoresPorDefecto.put(COLUMN_PREPAGO, 0);
        valoresPorDefecto.put(COLUMN_POSTPAGO, 0);
        valoresPorDefecto.put(COLUMN_EGRESO, 0);
        valoresPorDefecto.put(COLUMN_REIMPRESO_EGRESO, 0);
        valoresPorDefecto.put(COLUMN_REIMPRESO_PREPAGO, 0);
        valoresPorDefecto.put(COLUMN_REIMPRESO_POSTPAGO, 0);

        long newRowId = db.insert(TABLE_NAME, null, valoresPorDefecto);

        String CREATE_TABLE_VENTAS = "CREATE TABLE registro_ventas ("
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_CREATION_DATE + " INTEGER,"
                + COLUMN_PLACA + " TEXT,"
                + COLUMN_ACUMULADO_ANTERIOR + " INTEGER,"
                + COLUMN_ACUMULADO_ACTUAL + " INTEGER"
                + ")";
        db.execSQL(CREATE_TABLE_VENTAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Realiza actualizaciones desde la versión 1 a la versión 2
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_CREATION_DATE + " INTEGER");
        }

        if (oldVersion < 3) {
            // Agregar la nueva columna
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_FREEZE_COUNT + " INTEGER DEFAULT 0");
        }
        if (oldVersion < 4 ){
            String CREATE_TABLE_VENTAS = "CREATE TABLE registro_ventas ("
                    + COLUMN_USER_ID + " INTEGER,"
                    + COLUMN_CREATION_DATE + " INTEGER,"
                    + COLUMN_PLACA + " TEXT,"
                    + COLUMN_ACUMULADO_ANTERIOR + " INTEGER,"
                    + COLUMN_ACUMULADO_ACTUAL + " INTEGER"
                    + ")";
            db.execSQL(CREATE_TABLE_VENTAS);
        }
        if (oldVersion  <= 5) {
            // Agregar las nuevas columnas a la tabla existente
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_PREPAGO + " INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_POSTPAGO + " INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_EGRESO + " INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_REIMPRESO_EGRESO + " INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_REIMPRESO_PREPAGO + " INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_REIMPRESO_POSTPAGO + " INTEGER DEFAULT 0");
        }
    }

//    public boolean validarYCrearUsuario(long userId) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        boolean usuarioExistente = false;
//
//        // Verifica si el usuario existe en la tabla
//        String[] columns = {COLUMN_USER_ID};
//        String selection = COLUMN_USER_ID + " = ?";
//        String[] selectionArgs = {String.valueOf(userId)};
//
//        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
//
//        if (cursor.getCount() > 0) {
//            // El usuario ya existe en la tabla
//            usuarioExistente = true;
//        } else {
//            // El usuario no existe, crea un nuevo registro
//            ContentValues nuevoUsuario = new ContentValues();
//            nuevoUsuario.put(COLUMN_USER_ID, userId);
//            nuevoUsuario.put(COLUMN_TOTAL_TO_PAY, 0);
//            nuevoUsuario.put(COLUMN_FREEZE, 0);
//            nuevoUsuario.put(COLUMN_FREEZE_COUNT, 0);
//
//            long newRowId = db.insert(TABLE_NAME, null, nuevoUsuario);
//        }
//
//        cursor.close();
//        db.close();
//
//        return usuarioExistente;
//    }
//
//
//    public void actualizarTotalToPay(long nuevoTotalToPay, String placa) {
//        Log.d("DatabaseHelper", "actualizando total a pagar...");
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        long valorActual = consultarTotalToPay();
//        long nuevoValorTotal = valorActual + nuevoTotalToPay;
//
//        ContentValues values = new ContentValues();
//        // Primero se actualiza la fila nueva
//        agregarNuevaFila(db, nuevoTotalToPay, placa);
//        values.put(COLUMN_TOTAL_TO_PAY, nuevoValorTotal);
//        db.update(TABLE_NAME, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(GlobalPermisos.getDatosSesionActual().getIdPromotor())});
//        Log.d("DatabaseHelper", "total a pagar actualizado...");
//        db.close();
//        Log.d("DatabaseHelper", "base de datos cerrada en total a pagar");
//    }
//    private void agregarNuevaFila(SQLiteDatabase db, long nuevoTotalToPay, String placa) {
//
//        ContentValues nuevaFila = new ContentValues();
//        nuevaFila.put(COLUMN_USER_ID, GlobalPermisos.getDatosSesionActual().getIdPromotor());
//        nuevaFila.put(COLUMN_CREATION_DATE, String.valueOf(System.currentTimeMillis()));
//        nuevaFila.put(COLUMN_PLACA, placa);
//        nuevaFila.put(COLUMN_ACUMULADO_ANTERIOR, consultarTotalToPay());
//        nuevaFila.put(COLUMN_ACUMULADO_ACTUAL, nuevaFila.getAsLong(COLUMN_ACUMULADO_ANTERIOR) + nuevoTotalToPay);
//
//        db.insert("registro_ventas", null, nuevaFila);
//    }
//
//    public void incrementarPrepago() {
//        Log.d("DatabaseHelper", "Incrementando tiquetes de prepago...");
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_PREPAGO, getPrepago() + 1);
//        int rowsUpdated= db.update(TABLE_NAME, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(GlobalPermisos.getDatosSesionActual().getIdPromotor())});
//        Log.d("DatabaseHelper", "Contador de tiquetes prepago actualizado "+ rowsUpdated );
//        db.close();
//        Log.d("DatabaseHelper", "base de datos cerrada en actualizar tiquetes prepago");
//    }
//
//    public int getPrepago() {
//        Log.d("DatabaseHelper", "Obteniendo tiquetes de prepago para el usuario ");
//        SQLiteDatabase db = this.getReadableDatabase();
//        int prepago = 0;
//
//        String[] columns = {COLUMN_PREPAGO};
//        String selection = COLUMN_USER_ID + " = ?";
//        String[] selectionArgs = {String.valueOf(GlobalPermisos.getDatosSesionActual().getIdPromotor())};
//
//        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
//
//        if (cursor.moveToFirst()) {
//            prepago = cursor.getInt(cursor.getColumnIndex(COLUMN_PREPAGO));
//        }
//
//        cursor.close();
//
//        Log.d("DatabaseHelper", "Tiquetes prepago obtenidos para el usuario " + ": " + prepago);
//
//        return prepago;
//    }
//
//    public void incrementarPostpago() {
//        Log.d("DatabaseHelper", "Incrementando tiquetes de postpago...");
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_POSTPAGO, getPostpago() + 1);
//        int rowsUpdated = db.update(TABLE_NAME, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(GlobalPermisos.getDatosSesionActual().getIdPromotor())});
//        Log.d("DatabaseHelper", "Contador de tiquetes postpago actualizado "+ rowsUpdated );
//        db.close();
//        Log.d("DatabaseHelper", "base de datos cerrada en actualizar tiquetes postpago");
//    }
//
//    public int getPostpago() {
//        Log.d("DatabaseHelper", "Obteniendo tiquetes de postpago...");
//        SQLiteDatabase db = this.getReadableDatabase();
//        int postpago = 0;
//
//        String[] columns = {COLUMN_POSTPAGO};  // Corregir la columna a consultar
//        String selection = COLUMN_USER_ID + " = ?";
//        String[] selectionArgs = {String.valueOf(GlobalPermisos.getDatosSesionActual().getIdPromotor())};
//
//        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
//
//        if (cursor.moveToFirst()) {
//            postpago = cursor.getInt(cursor.getColumnIndex(COLUMN_POSTPAGO));
//        }
//
//        cursor.close();
//        Log.d("DatabaseHelper", "Tiquetes postpago obtenidos para el usuario: " + postpago);  // Corregir el mensaje de registro
//        return postpago;
//    }
//
//    public void incrementarEgreso() {
//        Log.d("DatabaseHelper", "Incrementando tiquetes de Egreso...");
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_EGRESO, getEgreso() + 1);
//       int rowsUpdated = db.update(TABLE_NAME, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(GlobalPermisos.getDatosSesionActual().getIdPromotor())});
//        Log.d("DatabaseHelper", "Contador de tiquetes egreso actualizado "+ rowsUpdated );
//        db.close();
//        Log.d("DatabaseHelper", "base de datos cerrada en actualizar tiquetes egreso");
//    }
//
//    public int getEgreso() {
//        Log.d("DatabaseHelper", "Obtenieendo tiquetes de egreso...");
//        SQLiteDatabase db = this.getReadableDatabase();
//        int egreso = 0;
//
//        String[] columns = {COLUMN_EGRESO};
//        String selection = COLUMN_USER_ID + " = ?";
//        String[] selectionArgs = {String.valueOf(GlobalPermisos.getDatosSesionActual().getIdPromotor())};
//
//        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
//
//        if (cursor.moveToFirst()) {
//            egreso = cursor.getInt(cursor.getColumnIndex(COLUMN_EGRESO));
//        }
//
//        cursor.close();
//        Log.d("DatabaseHelper", "base de datos cerrada en obtener tiquetes egreso");
//        return egreso;
//    }
//    public void incrementarReimpresoPrepago() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_REIMPRESO_PREPAGO, getReimpresoPrepago() + 1);
//        db.update(TABLE_NAME, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(GlobalPermisos.getDatosSesionActual().getIdPromotor())});
//        db.close();
//    }
//
//    public int getReimpresoPrepago() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        int prepago = 0;
//
//        String[] columns = {COLUMN_REIMPRESO_PREPAGO};
//        String selection = COLUMN_USER_ID + " = ?";
//        String[] selectionArgs = {String.valueOf(GlobalPermisos.getDatosSesionActual().getIdPromotor())};
//
//        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
//
//        if (cursor.moveToFirst()) {
//            prepago = cursor.getInt(cursor.getColumnIndex(COLUMN_REIMPRESO_PREPAGO));
//        }
//
//        cursor.close();
//        return prepago;
//    }
//    public void incrementarReimpresoPostpago() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_REIMPRESO_POSTPAGO, getReimpresoPostpago() + 1);
//        db.update(TABLE_NAME, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(GlobalPermisos.getDatosSesionActual().getIdPromotor())});
//        db.close();
//    }
//
//    public int getReimpresoPostpago() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        int reimpresoPostpago = 0;
//
//        String[] columns = {COLUMN_REIMPRESO_POSTPAGO};
//        String selection = COLUMN_USER_ID + " = ?";
//        String[] selectionArgs = {String.valueOf(GlobalPermisos.getDatosSesionActual().getIdPromotor())};
//
//        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
//
//        if (cursor.moveToFirst()) {
//            reimpresoPostpago = cursor.getInt(cursor.getColumnIndex(COLUMN_REIMPRESO_POSTPAGO));
//        }
//
//        cursor.close();
//        return reimpresoPostpago;
//    }
//    public void incrementarReimpresoEgreso() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_REIMPRESO_EGRESO, getReimpresoEgreso() + 1);
//        db.update(TABLE_NAME, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(GlobalPermisos.getDatosSesionActual().getIdPromotor())});
//        db.close();
//    }
//
//    public int getReimpresoEgreso() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        int reimpresoEgresoo = 0;
//
//        String[] columns = {COLUMN_REIMPRESO_EGRESO};
//        String selection = COLUMN_USER_ID + " = ?";
//        String[] selectionArgs = {String.valueOf(GlobalPermisos.getDatosSesionActual().getIdPromotor())};
//
//        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
//
//        if (cursor.moveToFirst()) {
//            reimpresoEgresoo = cursor.getInt(cursor.getColumnIndex(COLUMN_REIMPRESO_EGRESO));
//        }
//
//        cursor.close();
//        return reimpresoEgresoo;
//    }
//
//
//    public boolean actualizarFreeze(boolean nuevoFreeze) {
//        Log.d("DatabaseHelper", "Actualizando usuario congelado");
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_FREEZE, nuevoFreeze ? 1 : 0);
//        Calendar calendar = Calendar.getInstance();
//        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
//        values.put(COLUMN_CREATION_DATE, currentDay);
//        db.update(TABLE_NAME, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(GlobalPermisos.getDatosSesionActual().getIdPromotor())});
//        Log.d("DatabaseHelper", "Usuario congelado actualizado");
//        db.close();
//        Log.d("DatabaseHelper", "Base de datos cerrada en congelar usuario");
//        return nuevoFreeze;
//    }
//    public void incrementarFreezeCount() {
//        Log.d("DatabaseHelper", "Actualizando contador de usuario congelado...");
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_FREEZE_COUNT, consultarFreezeCount() + 1);
//        db.update(TABLE_NAME, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(GlobalPermisos.getDatosSesionActual().getIdPromotor())});
//        db.close();
//    }
//
//    public int consultarFreezeCount() {
//        Log.d("DatabaseHelper", "Consultando contador usuario congelado...");
//        SQLiteDatabase db = this.getReadableDatabase();
//        int freezeCount = 0;
//        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_FREEZE_COUNT}, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(GlobalPermisos.getDatosSesionActual().getIdPromotor())}, null, null, null);
//        if (cursor.moveToFirst()) {
//            freezeCount = cursor.getInt(cursor.getColumnIndex(COLUMN_FREEZE_COUNT));
//        }
//        Log.d("DatabaseHelper", "Contador de congelado actualizado");
//        cursor.close();
//        Log.d("DatabaseHelper", "Base de datos cerrada en contador de usuario congelado");
//        return freezeCount;
//    }
//
//    public long consultarTotalToPay() {
//        Log.d("DatabaseHelper", "Consultando total a pagar de usuario...");
//        SQLiteDatabase db = this.getReadableDatabase();
//        long totalToPay = 0;
//        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_TOTAL_TO_PAY}, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(GlobalPermisos.getDatosSesionActual().getIdPromotor())}, null, null, null);
//        if (cursor.moveToFirst()) {
//            totalToPay = cursor.getLong(cursor.getColumnIndex(COLUMN_TOTAL_TO_PAY));
//        }
//        Log.d("DatabaseHelper", "Total a pagar consultado "+ totalToPay);
//        cursor.close();
//        Log.d("DatabaseHelper", "Base de datos cerrada en consultar total a pagar");
//        return totalToPay;
//    }
//
//    public void reiniciarValoresSiNecesario() {
//        Log.d("DatabaseHelper", "Reiniciando datos si es necesario...");
//        SQLiteDatabase db = this.getWritableDatabase();
//        Calendar calendar = Calendar.getInstance();
//        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
//
//        // Verificar si COLUMN_CREATION_DATE es diferente al día actual
//        String whereClause = COLUMN_CREATION_DATE + " != ?";
//        String[] whereArgs = new String[] { String.valueOf(currentDay) };
//
//        ContentValues resetValues = new ContentValues();
//        resetValues.put(COLUMN_TOTAL_TO_PAY, 0);
//        resetValues.put(COLUMN_FREEZE, 0);
//        resetValues.put(COLUMN_CREATION_DATE, currentDay);
//        resetValues.put(COLUMN_FREEZE_COUNT, 0);
//        resetValues.put(COLUMN_POSTPAGO, 0);
//        resetValues.put(COLUMN_EGRESO, 0);
//        resetValues.put(COLUMN_PREPAGO, 0);
//        resetValues.put(COLUMN_REIMPRESO_EGRESO, 0);
//        resetValues.put(COLUMN_REIMPRESO_POSTPAGO, 0);
//        resetValues.put(COLUMN_REIMPRESO_PREPAGO, 0);
//        // Actualiza las filas que cumplan con la condición
//        int rowsUpdated = db.update(TABLE_NAME, resetValues, whereClause, whereArgs);
//        Log.d("DatabaseHelper", "Datos reiniciados "+ rowsUpdated);
//        db.close();
//        Log.d("DatabaseHelper", "Base de datos cerrada en reiniciar valores si es necesario");
//    }
//
//
//    public boolean consultarFreeze(Long userId) {
//        Log.d("DatabaseHelper", "Consultando usuarios congelados...");
//        SQLiteDatabase db = this.getReadableDatabase();
//        boolean freeze = false;
//        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_FREEZE}, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)}, null, null, null);
//        if (cursor.moveToFirst()) {
//            int freezeValue = cursor.getInt(cursor.getColumnIndex(COLUMN_FREEZE));
//            freeze = (freezeValue == 1);
//        }
//        Log.d("DatabaseHelper", "Usuarios congelados consultado "+ freeze);
//        cursor.close();
//        Log.d("DatabaseHelper", "Base de datos cerrada en consultar usuarios congelados");
//        return freeze;
//    }
//    public void eliminarTablaVentas() {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        // Verificar si la tabla existe antes de eliminarla
//        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='registro_ventas'", null);
//        if (cursor.getCount() > 0) {
//            // La tabla existe, eliminarla
//            db.execSQL("DROP TABLE IF EXISTS registro_ventas");
//
//            // Luego de eliminarla, la volvemos a crear para futuros registros
//            String CREATE_TABLE_VENTAS = "CREATE TABLE registro_ventas ("
//                    + COLUMN_USER_ID + " INTEGER,"
//                    + COLUMN_CREATION_DATE + " INTEGER,"
//                    + COLUMN_PLACA + " TEXT,"
//                    + COLUMN_ACUMULADO_ANTERIOR + " INTEGER,"
//                    + COLUMN_ACUMULADO_ACTUAL + " INTEGER"
//                    + ")";
//            db.execSQL(CREATE_TABLE_VENTAS);
//        }
//
//        cursor.close();
//        db.close();
//    }
//
//    public void exportarVentasAExcel(Context context) {
//        String deviceName = Build.MODEL;
//        Log.d("DeviceName", "Nombre del dispositivo: " + deviceName);
//        Log.d("data", "Intento de registro");
//
//        // Nombre del archivo
//        String fileName = deviceName + "_ventas_data.csv";
//
//        // Ruta del almacenamiento externo
//        File exportDir = new File(context.getExternalFilesDir(null), "MiAppData");
//
//        // Crear la carpeta si no existe
//        if (!exportDir.exists()) {
//            Log.d("data", "Carpeta no existe, creándola...");
//            if (!exportDir.mkdirs()) {
//                Log.e("DatabaseHelper", "Error al crear la carpeta");
//                return;
//            }
//        }
//
//        File file = new File(exportDir, fileName);
//
//        // Crear el archivo si no existe
//        try {
//            if (!file.exists()) {
//                Log.d("data", "Archivo no existe, creándolo...");
//                if (!file.createNewFile()) {
//                    Log.e("DatabaseHelper", "Error al crear el archivo");
//                    return;
//                }
//            }
//
//            SQLiteDatabase db = this.getReadableDatabase();
//            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
//
//            Cursor curCSV = db.rawQuery("SELECT * FROM registro_ventas", null);
//            csvWrite.writeNext(curCSV.getColumnNames());
//
//            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//
//            while (curCSV.moveToNext()) {
//                String[] arrStr = {
//                        "\"" + curCSV.getString(curCSV.getColumnIndex(COLUMN_USER_ID)) + "\"",
//                        "\"" + sdf.format(new Date(curCSV.getLong(curCSV.getColumnIndex(COLUMN_CREATION_DATE)))) + "\"",
//                        "\"" + curCSV.getString(curCSV.getColumnIndex(COLUMN_PLACA)) + "\"",
//                        "\"" + curCSV.getString(curCSV.getColumnIndex(COLUMN_ACUMULADO_ANTERIOR)) + "\"",
//                        "\"" + curCSV.getString(curCSV.getColumnIndex(COLUMN_ACUMULADO_ACTUAL)) + "\""
//                };
//                csvWrite.writeNext(arrStr);
//            }
//
//            csvWrite.close();
//            curCSV.close();
//            Log.d("data", "Archivo CSV creado con éxito en: " + file.getAbsolutePath());
//        } catch (IOException e) {
//            Log.e("DatabaseHelper", "Error al exportar datos a CSV: " + e.getMessage(), e);
//        } catch (SecurityException se) {
//            Log.e("DatabaseHelper", "Error de seguridad al crear el archivo: " + se.getMessage(), se);
//        }
//    }
}