package com.example.zer.somos.utilidades;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Clase creada para gestionar la firma única de un texto mediante hash.
 * Se trata de generar un valor de firma que no permite recuperar el texto
 * original y que evita la colisión de valores con similares.
 *
 * @version 1.0
 * @since 2020/10/12
 */
public class Hash {

    private static final String ALGORITMO_HASH = "SHA-256";
    private static final Charset CODIFICACION = StandardCharsets.UTF_8;
    private static final int FF_HEXA = 0xff;
    private static final String CERO = "0";
    private static final int UNO = 1;

    /**
     * Recupera el tipo de algoritmo hash usado
     *
     * @return texto con el nombre del algoritmo hash usado
     */
    private static String getAlgoritmoHash() {
        return ALGORITMO_HASH;
    }

    /**
     * Retorna la codificación de caracteres usados en los textos y os bytes
     *
     * @return Charset con la codificación usada
     */
    private static Charset getCodificacion() {
        return CODIFICACION;
    }

    /**
     * Retorna el valor entero que corresponde al valor hexadecimal de referencia dado
     *
     * @return entero con el valor correspondiente
     */
    private static int getFFHexa() {
        return FF_HEXA;
    }

    /**
     * retorna el texto que corresponde al número cero
     *
     * @return texto con el número cero
     */
    private static String getCero() {
        return CERO;
    }

    /**
     * Retorna el valor entero correspondiente a uno
     *
     * @return entero con el valor uno
     */
    private static int getUno() {
        return UNO;
    }

    /**
     * Retorna en formato byte un hexadecimal, completando con cero a la izquierda
     *
     * @param hexString texto con un contenido hexadecimal
     * @return retorna un texto formateado a byte, para el caso de un caracter le
     * completa con un cero a la izquierda, en cualquier otro caso retorna el mismo
     * texto recibido.
     */
    private static String getByteFormato(String hexString) {
        if (hexString.length() == getUno()) {
            return getCero() + hexString;
        }
        return hexString;
    }

    /**
     * Dado un texto el sistema le retorna un arreglo de bytes que corresponde a la firma
     * hash de dicho texto.
     *
     * @param data texto del que se desea conocer el hash correspondiente
     * @return retorna un arreglo de bytes con los caracteres de la firma hash para
     * el texto suministrado.
     */
    public static byte[] getHashBytes(String data) {
        byte[] encodedhash;
        try {
            MessageDigest digest = MessageDigest.getInstance(getAlgoritmoHash());
            encodedhash = digest.digest(data.getBytes(getCodificacion()));
        } catch (Exception ex) {
            return null;
        }
        return encodedhash;
    }

    /**
     * Convierte un grupo de bytes a su equivalente en hexadecimal retornando un texto
     * con dicho contenido.
     *
     * @param hash arreglo de bytes a los que se les desea generar su equivalente en hexadecimal.
     * @return retorna un texto con los equivalente de hexadecimal de los bytes suministrados en el arreglo de entrada.
     */
    private static String getBytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte byteActual : hash) {
            String hex = Integer.toHexString(getFFHexa() & byteActual);
            hexString.append(getByteFormato(hex));
        }
        return hexString.toString();
    }

    /**
     * Dado un texto, el sistema retorna su firma hash en formato texto hexadecimal.
     *
     * @param data texto al que se le desea calcular su firma hash.
     * @return texto con los caracteres hexadecimales de la firma hash para el texto
     * suministrado como parámetro.
     */
    public static String getHashStringHexa(String data) {
        byte[] contenido = getHashBytes(data);
        assert contenido != null;
        return getBytesToHex(contenido);
    }

}
