package co.zer.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Clase especializada para la gestión de valores uuid,
 * usados para la identificación de registros.
 *
 * @author Somos Movilidad
 * @version 1.0
 * @since 2020/10/09
 */
public class Uuid {

    private final static String FORMATO_FECHA_UUID = "yyMMddHHmmss";
    private final static double NUMERO_DIGITOS_ALEATORIOS_UUID = 6d;
    private final static String CERO = "0";

    /**
     * Retona un valor entero largo único conformado por la fecha y hora de generación y un
     * valor aleatorio. Es posible la presencia de colisión pero es una probabilidad
     * calculada como de 1/1000000
     *
     * @return un entero largo con un valor único conformado por yyMMddHHmmss######
     */
    public static long getUuidLong() {
        String uuidTexto = getUuidTexto();
        return Long.parseLong(uuidTexto);
    }

    /**
     * Retona un valor texto único conformado por la fecha y hora de generación y un
     * valor aleatorio. Es posible la presencia de colisión pero es una probabilidad
     * calculada como de 1/100000
     *
     * @return texto con un valor único conformado por yyMMddHH24mmss#####
     */
    public static String getUuidTexto() {
        String formatoFechaUuid = getFormatoFechaUuid();
        DateFormat formateadorFecha = new SimpleDateFormat(formatoFechaUuid, Locale.US);
        Date fechaHoraActual = new Date();
        double valorAleatorio = Math.random() * getValorCalculoDigitosAleatoriosUuid();
        String textoConValorAleatorio = String.valueOf(Math.round(valorAleatorio));
        return formateadorFecha.format(fechaHoraActual) +
                completarConCerosIzquierda(textoConValorAleatorio, getNumeroDigitosAleatoriosUuid());
    }

    /**
     * Retorna el formato de fecha necesario en el proceso de generación
     * de un UUID en el sistema.
     *
     * @return texto que contiene el formato usado en el UUID para la fecha
     */
    private static String getFormatoFechaUuid() {
        return FORMATO_FECHA_UUID;
    }

    /**
     * Retorna el número de digitos aleatorios a ser usados en la generación del uuid
     *
     * @return valor doble con el número de digitos usados para generar la parte
     * aleatoria del uuid
     */
    private static double getNumeroDigitosAleatoriosUuid() {
        return NUMERO_DIGITOS_ALEATORIOS_UUID;
    }

    /**
     * Este método recupera el valor por el cual debe ser
     * multiplicado un double aleatorio para recuperar
     * n digitos enteros del valor.
     *
     * @return valor double que se usa para obtener los digitos
     */
    private static double getValorCalculoDigitosAleatoriosUuid() {
        return Math.pow(10, getNumeroDigitosAleatoriosUuid());
    }

    /**
     * Completa con ceros a la izquierda la longitud deseada para un texto.
     * Importante si la longitud del texto desde un comienzo excede la deseada,
     * el resutado será el mismo texto enviado originalmente.
     *
     * @param texto           contenido al que se le desea agregar los ceros a su izquierda
     * @param longitudDeseada número de caracteres que debe tener la respuesta
     *                        de esta manera solo se llena con ceros los faltanes
     *                        para llegar a dicha longitud
     * @return retorna el texto con los ceros a la izquierda hasta completar
     * el número de caracteres deseados en la longitud
     */
    private static String completarConCerosIzquierda(String texto, double longitudDeseada) {
        StringBuilder textoBuilder = new StringBuilder(texto);
        while (textoBuilder.length() < longitudDeseada) {
            textoBuilder.insert(0, CERO);
        }
        return textoBuilder.toString();
    }
}
