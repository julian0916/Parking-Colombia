package co.zer.utils;

import co.zer.model.Paginacion;

public class Utilidades {
    public static final String SQL_EXCEPTION_TAG = "--SQLException: ";
    public static final String EXCEPTION_TAG = "--Exception: ";
    public static final String PRINT_2_P = "{} {}";
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
}
