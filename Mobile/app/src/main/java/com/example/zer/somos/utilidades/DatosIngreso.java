package com.example.zer.somos.utilidades;

import java.util.regex.Pattern;

/**
 * Valida los datos de ingreso de un vehículo en el sistema
 * se cotegan los datos de placa para constatar su estructura
 *
 * @author Somos Movilidad
 * @version 1.0
 * @since 2020/10/15
 */
public class DatosIngreso {

    public final static int PLACA_CARRO = 1;
    public final static int PLACA_MOTO = 2;
    public final static int NO_ES_PLACA = 0;

    /**
     * Permite eliminar los caracteres diferentes de letras y números
     * en un texto suministrado
     *
     * @param contenido texto al que se desea limpiar
     * @return texto solo con letras y números
     */
    public static String limpiarContenidos(String contenido) {
        String regexp = "[^a-zA-Z0-9]*";
        return contenido.replaceAll(regexp, "");
    }

    /**
     * Permite conocer si una placa suministrada es de carro
     *
     * @param placa texto con la placa formato CCC###
     * @return retorna true si cumple con el formato esperado
     * para la placa de un carro
     */
    public static boolean validarPlacaCarro(String placa) {
        String regexp = "([a-zA-Z]{3}[0-9]{3})|" +
                "([0-9]{3}[a-zA-Z]{3})|" +//caso raro
                "([a-zA-Z]{2}[0-9]{4})|" +//servicio diplomatico
                "([a-zA-Z][0-9]{4})";//transporte
        return Pattern.matches(regexp, placa);
    }

    /**
     * Verifica si la placa suministrada corresponde a la
     * de una moto
     *
     * @param placa texto con el contenido con formato
     *              CCC## o CCC##C
     * @return retorna true si la placa corresponde a la
     * de una moto
     */
    public static boolean validarPlacaMoto(String placa) {
        String regexp = "[a-zA-Z]{3}[0-9]{2}|[a-zA-Z]{3}[0-9]{2}[a-zA-Z]{1}";
        return Pattern.matches(regexp, placa);
    }

    /**
     * Retorna el tipo de placa que corresponde al texto enviado para verificar
     * 0:no es una placa
     * 1: placa de carro
     * 2: placa de moto
     *
     * @param placa texto con la placa que se desea verificar
     * @return retorna 0 si no una placa 1 si es de carro y 2 si es de moto
     */
    public static int getTipoPlaca(String placa) {
        if (validarPlacaCarro(placa)) {
            return PLACA_CARRO;
        }
        if (validarPlacaMoto(placa)) {
            return PLACA_MOTO;
        }
        return NO_ES_PLACA;
    }
}
