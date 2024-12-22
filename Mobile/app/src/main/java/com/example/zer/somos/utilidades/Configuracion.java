package com.example.zer.somos.utilidades;

import com.example.zer.somos.comunes.DatosSesion;
import com.example.zer.somos.permisos.GlobalPermisos;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class Configuracion {

    public static final String ZONA_HORARIA = "GMT-5";
    private static final String PRODUCCION = "PRODUCCION";
    private static final String TEST = "TEST";
    private static final String DESARROLLO = "DESARROLLO";
    //---------------------------------------------
    //    SE DEBE CAMBIAR SEGUN SEA EL CASO
    //---------------------------------------------
    private static String PERFIL_USADO = PRODUCCION;
    //----------------------------------------------

    private static String SERVER_AUTENTICAR = "";
    private static String SERVER_ZONAS_PROMOTOR = "";
    private static String SERVER_OPERACION = "";
    private static String SERVER_CONFIGURACION = "";
    private static String SERVER_SUPERVISION = "";


    private static int procesarPerfil() {

        if (PERFIL_USADO.equals(PRODUCCION)) {
            final String SERVIDOR = "https://sistemaszer365.com:8443";
            SERVER_AUTENTICAR = SERVIDOR + "/zer-api-gateway";
            SERVER_ZONAS_PROMOTOR = SERVIDOR + "/zer-api-promotor-zona";
            SERVER_OPERACION = SERVIDOR + "/zer-api-operacion";
            SERVER_CONFIGURACION = SERVIDOR + "/zer-api-configuracion";
            SERVER_SUPERVISION = SERVIDOR + "/zer-api-supervision";

        }

        if (PERFIL_USADO.equals(TEST)) {
            final String SERVIDOR = "https://microservicios.sistemaszer365.com:8443";
            SERVER_AUTENTICAR = SERVIDOR + "/zer-api-gateway";
            SERVER_ZONAS_PROMOTOR = SERVIDOR + "/zer-api-gateway";
            SERVER_OPERACION = SERVIDOR + "/zer-api-gateway";
            SERVER_CONFIGURACION = SERVIDOR + "/zer-api-gateway";
            SERVER_SUPERVISION = SERVIDOR + "/zer-api-gateway";
        }

        if (PERFIL_USADO.equals(DESARROLLO)) {
            final String SERVIDOR = "http://localhost:";
            SERVER_AUTENTICAR = SERVIDOR + "8080/zer-api-gateway";
            SERVER_ZONAS_PROMOTOR = SERVIDOR + "/zer-api-promotor-zona";
            SERVER_OPERACION = SERVIDOR + "8087/zer-api-operacion";
            SERVER_CONFIGURACION = SERVIDOR + "/zer-api-configuracion";
            SERVER_SUPERVISION = SERVIDOR + "/zer-api-supervision";

        }
        return 0;
    }

    public static String getServerAutenticar() {
        procesarPerfil();
        return SERVER_AUTENTICAR;
    }

    public static String getServerZonasPromotor() {
        procesarPerfil();
        return SERVER_ZONAS_PROMOTOR;
    }

    public static String getServerOperacion() {
        procesarPerfil();
        return SERVER_OPERACION;
    }

    public static String getServerConfiguracion() {
        procesarPerfil();
        return SERVER_CONFIGURACION;
    }

    public static String getServerSupervision() {
        procesarPerfil();
        return SERVER_SUPERVISION;
    }


    public static Map<String, String> getEncabezados() {
        Map<String, String> encabezado = new HashMap<>();
        DatosSesion datosActuales = GlobalPermisos.getDatosSesionActual();
        encabezado.put("Content-Type", "application/json; charset=UTF-8");
        encabezado.put("Content-Encoding", "UTF-8");
        if (datosActuales != null) {
            encabezado.put("idSesionSistema", datosActuales.getSesion());
        }

        return encabezado;
    }

    public static String getFechaHoraActual() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZONA_HORARIA));
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String formatted = format1.format(calendar.getTime());
        return formatted;
    }

    public static String getFechaHoraActualAMPM() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZONA_HORARIA));
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        String formatted = format1.format(calendar.getTime());
        return formatted;
    }

    /**
     * Retorna las horas y minutos transcurridos entre dos fechas
     *
     * @param fechaHoraInicial
     * @param fechaHoraFinal
     * @return
     */
    public static String getHorasMinutosTranscurridos(String fechaHoraInicial, String fechaHoraFinal) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date dateInicial = format1.parse(fechaHoraInicial);
            Date dateFinal = format1.parse(fechaHoraFinal);
            long milisegundos = dateFinal.getTime() - dateInicial.getTime();
            long segundos = milisegundos / 1000;
            long minutos = segundos / 60;
            long horas = minutos / 60;
            long horasEntero = (int) Math.floor(horas);
            long minutosEntero = minutos - horasEntero * 60;

            String contenidoResultado = "";

            if (horasEntero > 0) {
                contenidoResultado = horasEntero + " hora";
            }
            if (horasEntero > 1) {
                contenidoResultado += "s";
            }

            if (minutosEntero > 0) {
                contenidoResultado += " " + minutosEntero + " minuto";
            }
            if (minutosEntero > 1) {
                contenidoResultado += "s";
            }
            contenidoResultado = contenidoResultado.trim();
            //si no se han registrado horas y minutos se ponen los segundos
            if (contenidoResultado.equals("")) {
                if (segundos > 0) {
                    contenidoResultado = segundos + " segundo";
                }
                if (segundos > 1) {
                    contenidoResultado += "s";
                }
            }

            return contenidoResultado;

        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Recibe el formato "yyyy-MM-dd'T'HH:mm:ss.SSS"
     * retorna el formato "yyyy-MM-dd hh:mm:a"
     *
     * @param fechaHora
     * @return
     */
    public static String getFechaHora(String fechaHora) {
        boolean formato1Correcto = true;
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date date = format1.parse(fechaHora);
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            String formatted = format2.format(date);
            return formatted;
        } catch (Exception ex) {
            formato1Correcto = false;
        }
        if (!formato1Correcto) {
            return getFechaHoraF2(fechaHora);
        }
        return fechaHora;
    }

    public static String getHoraAMPM(String hora) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
            Date date = format1.parse(hora);
            SimpleDateFormat format2 = new SimpleDateFormat("hh:mma");
            String formatted = format2.format(date);
            return formatted;
        } catch (Exception ex) {
            return hora;
        }
    }

    public static String getFechaHoraF2(String fechaHora) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = format1.parse(fechaHora);
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            String formatted = format2.format(date);
            return formatted;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return fechaHora;
    }

    public static String getDiaActualNombre() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return Dias.T_LUNES;
            case Calendar.TUESDAY:
                return Dias.T_MARTES;
            case Calendar.WEDNESDAY:
                return Dias.T_MIERCOLES;
            case Calendar.THURSDAY:
                return Dias.T_JUEVES;
            case Calendar.FRIDAY:
                return Dias.T_VIERNES;
            case Calendar.SATURDAY:
                return Dias.T_SABADO;
            default:
                return Dias.T_DOMINGO;
        }
    }

    public static String encodeUTF8(String value) {
        String data = "";
        try {
            return new String(value.getBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String encodeTexto(String value) {
        String data = "";
        try {
            data = URLEncoder.encode(value, StandardCharsets.UTF_8.displayName());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static class Dias {
        public final static int LUNES = 1;
        public final static int MARTES = 2;
        public final static int MIERCOLES = 3;
        public final static int JUEVES = 4;
        public final static int VIERNES = 5;
        public final static int SABADO = 6;
        public final static int DOMINGO = 7;

        public final static String T_LUNES = "lunes";
        public final static String T_MARTES = "martes";
        public final static String T_MIERCOLES = "miercoles";
        public final static String T_JUEVES = "jueves";
        public final static String T_VIERNES = "viernes";
        public final static String T_SABADO = "sabado";
        public final static String T_DOMINGO = "domingo";
    }

    public static class QR {
        public static String getSeparadorQR() {
            return "_@@";
        }

        public static int getTamaHash() {
            return 64;
        }

        public static String getTramaConfirmacion() {
            return "\"confirmado\":\"OK\"";
        }

        public static long getLimiteVelocidadLectura() {
            return 100000000;
        }

        public static long getLimiteCaracteresIngresoManualQR() {
            return 20;
        }
    }

    public class DatosQR implements Serializable {
        private String u;
        private String c;
        private Long i;

        public String getU() {
            return u;
        }

        public void setU(String u) {
            this.u = u;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        public Long getI() {
            return i;
        }

        public void setI(Long id) {
            this.i = id;
        }
    }
}
