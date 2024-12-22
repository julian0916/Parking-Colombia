package co.zer.utils;

import co.zer.model.Paginacion;

import java.util.Calendar;
import java.util.TimeZone;

public class Utilidades {
    public static final String SQL_EXCEPTION_TAG = "--SQLException: ";
    public static final String EXCEPTION_TAG = "--Exception: ";
    public static final String PRINT_2_P = "{} {}";
    public static final long PERFIL_SUPERVISOR = 102;
    public static final long PERFIL_PROMOTOR = 105;

    public static final String PRINT_1_P = "{}";
    public static final int INITIAL = 1;
    public static final int NEXT = 2;
    public static final int PREVIOUS = 3;
    public static final int LAST = 4;
    public static final int MOVE_TO = 5;
    public static final int CURRENT = 6;
    public static final int LIMIT_DEFAULT = 20;
    public static final int ASC = 0;
    public static final int DESC = 1;
    public static final int ACTIVE = 1;
    public static final int INACTIVE = 0;
    private Utilidades() {
        throw new IllegalStateException("Utility class");
    }

    public static String filtroLike(Paginacion paginacion) {
        String result = "%";
        if (paginacion != null) {
            if (paginacion.getFiltro() != null) {
                result = "%" + paginacion.getFiltro().toLowerCase().trim() + "%";
            }
        }
        return result;
    }

    public static String sentenciaOrdenar(Paginacion paginacion) {
        if (paginacion != null) {
            String direction = "ASC";
            if (paginacion.getSentido() != null &&!paginacion.getSentido().toUpperCase().trim().equals(direction)) {
                direction = "DESC";
            }
            if (paginacion.getOrden() != null && paginacion.getOrden().trim().length() > 0) {
                return "ORDER BY " + paginacion.getOrden().trim() + " " + direction;
            }
        }
        return "";
    }

    public static int getDiaActualNumero() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return Dias.LUNES;
            case Calendar.TUESDAY:
                return Dias.MARTES;
            case Calendar.WEDNESDAY:
                return Dias.MIERCOLES;
            case Calendar.THURSDAY:
                return Dias.JUEVES;
            case Calendar.FRIDAY:
                return Dias.VIERNES;
            case Calendar.SATURDAY:
                return Dias.SABADO;
            default:
                return Dias.DOMINGO;
        }
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
}
