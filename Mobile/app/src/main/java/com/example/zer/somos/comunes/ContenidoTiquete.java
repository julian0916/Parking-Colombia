 package com.example.zer.somos.comunes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;

import com.example.zer.somos.operaciones.PostpagoActivity;
import com.example.zer.somos.permisos.GlobalPermisos;
import com.example.zer.somos.utilidades.Configuracion;
import com.example.zer.somos.utilidades.ContenidoImpresoraBT;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

 public class ContenidoTiquete {

     static boolean imprimio = false;
     private static boolean encontroBluetooth = false;
    // static BluetoothPrint  bluetoothPrinter =  new BluetoothPrint(this);
     private static BluetoothPrint bluetoothPrinter;

     public ContenidoTiquete(Context context) {
         bluetoothPrinter = new BluetoothPrint(context);
     }

    public static ContenidoImpresoraBT generarReciboPostpago(Context context,JSONObject conteJSON) {
        return generarReciboPostpago(context,conteJSON, false);
    }


    private static void crearCodigoBarras(String placa) {
        Bitmap bitmap;
        try {
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            Writer codeWriter;
            codeWriter = new Code128Writer();
            BitMatrix byteMatrix = codeWriter.encode(placa, BarcodeFormat.CODE_128,400, 200, hintMap);
            int width = byteMatrix.getWidth();
            int height = byteMatrix.getHeight();
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }
            storeImage(bitmap);
        } catch (Exception e) {
        }

    }
     private static void storeImage(Bitmap image) {
         File pictureFile = getOutputMediaFile();
         if (pictureFile == null) {

             return;
         }
         try {
             FileOutputStream fos = new FileOutputStream(pictureFile);
             image.compress(Bitmap.CompressFormat.PNG, 90, fos);
             fos.close();
         } catch (FileNotFoundException e) {

         } catch (IOException e) {

         }
     }
     private static File getOutputMediaFile(){
         // To be safe, you should check that the SDCard is mounted
         // using Environment.getExternalStorageState() before doing this.
         File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                 + "/Android/data/"
                 + "ZER"
                 + "/Files");

         // This location works best if you want the created images to be shared
         // between applications and persist after your app has been uninstalled.

         // Create the storage directory if it does not exist
         if (! mediaStorageDir.exists()){
             if (! mediaStorageDir.mkdirs()){
                 return null;
             }
         }
         // Create a media file name
         String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
         File mediaFile;
         String mImageName="MI_"+ timeStamp +".jpg";
         mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
         return mediaFile;
     }

     public static ContenidoImpresoraBT generarSaldoPromotor(JSONObject conteJSON) {
         ContenidoImpresoraBT conte = new ContenidoImpresoraBT();
         bluetoothPrinter.openBluetoothPrinter();
         bluetoothPrinter.beginListenData();
         if (conteJSON == null) {
             return conte;
         }
         DatosSesion datosSesion = GlobalPermisos.getDatosSesionActual();
         String promotor = datosSesion.getNombrePromotor();
         try {
             conte.addCentro(promotor,conte.getFormato().negrita());
             conte.addEnter();
             conte.addCentro(conteJSON.getString("mensaje"));
             conte.addEnter();
             conte.addCentro(conteJSON.getString("mensaje2"));
             conte.addEnter();
             //conte.addCentro(conteJSON.getString("dinero"), conte.getFormato().negrita().alto());
             conte.addEnter();
             conte.addEnter();
             conte.addEnter();
             conte.addEnter();
             conte.addCentro("                       ",conte.getFormato().subrayado());
             conte.addEnter();
             conte.addEnter();
             conte.addCentro(Configuracion.getFechaHoraActualAMPM(), conte.getFormato().negrita());
             conte.addDerecha(conteJSON.getString("freezeCount"), conte.getFormato().peque());
             conte.addEnter();
             conte.addEnter();
             conte.addEnter();
         } catch (Exception ex) {
             ex.printStackTrace();
         }
         bluetoothPrinter.disconnectBT();
         return conte;
     }

     public static ContenidoImpresoraBT generarReciboPostpago(Context context, JSONObject conteJSON, Boolean reImpreso) {
         ContenidoImpresoraBT conte = new ContenidoImpresoraBT();
         bluetoothPrinter.openBluetoothPrinter();
         bluetoothPrinter.beginListenData();

         if (conteJSON == null) {
             return conte;
         }

         DatosSesion datosSesion = GlobalPermisos.getDatosSesionActual();
         String promotor = ": " + datosSesion.getNombrePromotor();

         try {
             if (reImpreso) {
                 getEncabezadoReimpresion(conte);
                 promotor = " ingreso: " + conteJSON.getString("nombrePromotorIngreso") +
                         "\n" + " Promotor egreso: " + conteJSON.getString("nombrePromotorEgreso");
             }

             // Resto del código omitido por brevedad

         } catch (Exception ex) {
             ex.printStackTrace();
         }
         bluetoothPrinter.disconnectBT();
         return conte;
     }


    public static ContenidoImpresoraBT generarReciboPrepago(Context context, JSONObject conteJSON) {
        return generarReciboPrepago(context, conteJSON, false);
    }


    public static ContenidoImpresoraBT generarReciboPrepago(Context context, JSONObject conteJSON, Boolean reImpreso) {
        ContenidoImpresoraBT conte = new ContenidoImpresoraBT();
        PostpagoActivity postpagoActivity = new PostpagoActivity();
        bluetoothPrinter.openBluetoothPrinter();
        bluetoothPrinter.beginListenData();
        DatosSesion datosSesion = GlobalPermisos.getDatosSesionActual();
        String promotor= ": " +datosSesion.getNombrePromotor();
        if (conteJSON == null) {
            return conte;
        }
        try {
            if (reImpreso) {
                getEncabezadoReimpresion(conte);
                promotor = " ingreso: " + conteJSON.getString("nombrePromotorIngreso")+ "\n"
                        + " Promotor egreso: "+ conteJSON.getString("nombrePromotorEgreso");
            }
            String placa = conteJSON.getString("placa");
            crearCodigoBarras(placa);
            if(encontroBluetooth){
               // bluetoothPrinter.printImage();
               //bluetoothPrinter.printDataCentered("PRUEBA");
            }
           // bluetoothPrinter.printImage();
                conte.addCentro(datosSesion.getLema());
                conte.addEnter();
                conte.addCentro(datosSesion.getNombreParaTiquete());
                conte.addEnter();
                conte.addCentro("Nit: " + datosSesion.getNit(), conte.getFormato().negrita());
                conte.addEnter();
                conte.addEnter();
                conte.addCentro("PREPAGO", conte.getFormato().negrita().alto());
                conte.addEnter();
                conte.addEnter();
                conte.addIzquierda("No: " + conteJSON.getLong("id"));
                conte.addEnter();
                conte.addIzquierda("Placa: " + conteJSON.getString("placa"), conte.getFormato().negrita().alto());
                conte.addEnter();
                conte.addIzquierda("Vehículo: " + (conteJSON.getBoolean("esCarro") ? "Carro" : "Moto"));
                conte.addEnter();
                conte.addIzquierda("Inicia: " + Configuracion.getFechaHora(conteJSON.getString("fhingreso")));
                conte.addEnter();
                conte.addIzquierda("Termina:" + Configuracion.getFechaHora(conteJSON.getString("fhegreso")));
                conte.addEnter();
                conte.addIzquierda("Horas prepagadas: " + conteJSON.getLong("hcobradas"));
                conte.addIzquierda(conteJSON.getLong("hcobradas") == 1 ? " hora" : " horas");
                conte.addEnter();
                conte.addIzquierda("Valor pagado: $" + conteJSON.getString("valorCobrado"), conte.getFormato().negrita().alto());
                conte.addEnter();
                conte.addIzquierda("Zona: " + datosSesion.getNombreZona());
                conte.addEnter();
                conte.addIzquierda("Horario: " +
                        Configuracion.getHoraAMPM(conteJSON.getString("hiniciaZona")) +
                        "- " +
                        Configuracion.getHoraAMPM(conteJSON.getString("hterminaZona")));
                conte.addEnter();
                conte.addIzquierda("Valor hora-fracción: $" + conteJSON.getLong("valorH"));
                conte.addEnter();
                conte.addIzquierda("Promotor" + promotor);
                conte.addEnter();
                conte.addEnter();
                conte.addCentro("Recomendaciones", conte.getFormato().negrita());
                conte.addEnter();
                conte.addCentro("Y", conte.getFormato().negrita());
                conte.addEnter();
                conte.addCentro("condiciones", conte.getFormato().negrita());
                conte.addEnter();
                conte.addEnter();
                conte.addIzquierda(datosSesion.getTerminos(), conte.getFormato().peque());
                conte.addEnter();
                conte.addEnter();
                conte.addEnter();
           // }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        bluetoothPrinter.disconnectBT();
        return conte;
    }



    public static ContenidoImpresoraBT generarReciboPago(Context context,JSONObject conteJSON) {
        return generarReciboPago(context,conteJSON, false);
    }

    private static void getEncabezadoReimpresion(ContenidoImpresoraBT conte) {
            conte.addCentro("Reimpreso:" +
                    Configuracion.getFechaHoraActualAMPM(), conte.getFormato().peque().negrita());
            conte.addEnter();
            conte.addCentro("Por: " + GlobalPermisos.getDatosSesionActual().getNombrePromotor(),
                    conte.getFormato().peque().negrita());
            conte.addEnter();


    }

    public static ContenidoImpresoraBT generarReciboPago(Context context, JSONObject conteJSON, Boolean reImpreso) {
        ContenidoImpresoraBT conte = new ContenidoImpresoraBT();

        DatosSesion datosSesion = GlobalPermisos.getDatosSesionActual();
        String promotor= ": " +datosSesion.getNombrePromotor();

        if (conteJSON == null) {
            return conte;
        }

        try {
            if (reImpreso) {
                getEncabezadoReimpresion(conte);
                promotor = " ingreso: " + conteJSON.getString("nombrePromotorIngreso")  +
                        "\n" + " Promotor egreso: "+ conteJSON.getString("nombrePromotorEgreso");
            }
            //imprimirCodigoBarras(R.drawable.somos_blanco);
            if(encontroBluetooth){
                //bluetoothPrinter.printImage();
                //bluetoothPrinter.printDataCentered("PRUEBA");
            }

                conte.addCentro(datosSesion.getLema());
                conte.addEnter();
                conte.addCentro(datosSesion.getNombreParaTiquete());
                conte.addEnter();
                conte.addCentro("Nit: " + datosSesion.getNit(), conte.getFormato().negrita());
                conte.addEnter();
                conte.addEnter();
                conte.addCentro("SALIDA", conte.getFormato().negrita().alto());
                conte.addEnter();
                conte.addEnter();
                String titulo = "PAGO REALIZADO";
                if (conteJSON.getLong("estado") == 2) {
                    long minutosGracia = conteJSON.getLong("minutosGraciaZona");
                    titulo = minutosGracia + " MINUTOS-GRATIS";
                }
                if (conteJSON.getLong("estado") == 4) {
                    titulo = "PAGO-EXTEMPORANEO";
                }
                if (conteJSON.getLong("estado") == 3) {
                    titulo = "SIN PAGAR Y REPORTADO";
                }
                conte.addCentro(titulo, conte.getFormato().negrita().alto());
                conte.addEnter();
                conte.addEnter();
                conte.addIzquierda("No: " + conteJSON.getLong("id"));
                conte.addEnter();
                conte.addIzquierda("Placa: " + conteJSON.getString("placa"), conte.getFormato().negrita().alto());
                conte.addEnter();
                conte.addIzquierda("Vehículo: " + (conteJSON.getBoolean("esCarro") ? "Carro" : "Moto"));
                conte.addEnter();
                conte.addIzquierda("Inicia: " + Configuracion.getFechaHora(conteJSON.getString("fhingreso")));
                conte.addEnter();
                conte.addIzquierda("Termina:" + Configuracion.getFechaHora(conteJSON.getString("fhegreso")));
                conte.addEnter();
                conte.addIzquierda("Tiempo: " + Configuracion.getHorasMinutosTranscurridos(conteJSON.getString("fhingreso"), conteJSON.getString("fhegreso")));
                conte.addEnter();
                conte.addIzquierda("Paga: " + Configuracion.getFechaHora(conteJSON.getString("fhrecaudo")));
                conte.addEnter();
                conte.addIzquierda("Horas cobradas: " + conteJSON.getLong("hcobradas") + (conteJSON.getLong("hcobradas") == 1 ? " hora" : " horas"));
                conte.addEnter();
                if (conteJSON.getLong("estado") == 3) {
                    conte.addIzquierda("Valor adeudado: $" + conteJSON.getString("valorCobrado"), conte.getFormato().negrita().alto());
                } else {
                    conte.addIzquierda("Valor pagado: $" + conteJSON.getString("valorCobrado"), conte.getFormato().negrita().alto());
                }

                conte.addEnter();
                conte.addIzquierda("Zona: " + datosSesion.getNombreZona());
                conte.addEnter();
                conte.addIzquierda("Horario: " +
                        Configuracion.getHoraAMPM(conteJSON.getString("hiniciaZona")) +
                        "- " +
                        Configuracion.getHoraAMPM(conteJSON.getString("hterminaZona")));
                conte.addEnter();

                conte.addIzquierda("Promotor" + promotor);
                conte.addEnter();

                conte.addIzquierda("Para ver terminos de uso, condiciones y restricciones " +
                        "entre al siguiente enlace:  https://somosmovilidad.gov.co/zonas-de-estacion" +
                        "amiento-regulado-z-e-r/", conte.getFormato().peque());
                conte.addEnter();
                conte.addEnter();
                conte.addEnter();
            //}

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        bluetoothPrinter.disconnectBT();
        return conte;
    }

     public static ContenidoImpresoraBT generarCertificado(String placa) {
         ContenidoImpresoraBT conte = new ContenidoImpresoraBT();
        try {
            DatosSesion datosSesion = GlobalPermisos.getDatosSesionActual();
            conte.addCentro("LA EMPRESA SOMOS SISTEMA OPERATIVO DE MOVILIDAD ORIENTE " +
                    "SOSTENIBLE SOMOS RIONEGRO S.A.S. ", conte.getFormato().negrita());
            conte.addEnter();
            conte.addCentro("certifica que de conformidad con el decreto 453 de 2017, mediante" +
                    " el cual se reglamentó el acuerdo 006 de 2017 y, se estableció, en armonía con lo " +
                    "estipulado en el artículo segundo del acuerdo municipal 008 de 2016, adicionado" +
                    " por el artículo segundo del acuerdo municipal 014 de 2017, adicionado por el " +
                    "acuerdo 033 de 2017, el usuario propietario del vehículo con placa ");
            conte.addCentro(placa, conte.getFormato().negrita());
            conte.addCentro(", se encuentra a paz y salvo por concepto del pago de la tasa por " +
                    "estacionamiento en espacio público en el municipio de Rionegro, según lo " +
                    "señalado en la normatividad anterior.");
                    conte.addEnter();
                    conte.addCentro(
                    "para constancia se expide de manera electrónica en la ciudad de Rionegro, " +
                    "Antioquia en la fecha: ");
                    conte.addCentro(Configuracion.getFechaHoraActualAMPM(), conte.getFormato().negrita());
                    conte.addCentro(" por el promotor: ");
                    conte.addCentro(datosSesion.getNombrePromotor(), conte.getFormato().negrita());
            conte.addEnter();
            conte.addEnter();

        }catch (Exception e){
            e.printStackTrace();
        }

         return conte;
     }
}
