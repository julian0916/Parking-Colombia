package com.example.zer.somos.autenticar;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class EncodeService {

    protected final String ALGORITMO_NOPADDING = "AES/CBC/NoPadding";
    protected final String ALGORITMO = "AES/CBC/PKCS5Padding";
    protected final String ALGORITMO_CLAVE = "AES";

    /**
     * Reliza un complemento de componentes utilizando espaciador
     *
     * @param source
     * @return
     */
    private static String padString(String source) {
        char paddingChar = ' ';
        int size = 32;
        int x = source.length() % size;
        int padLength = size - x;

        for (int i = 0; i < padLength; i++) {
            source += paddingChar;
        }

        return source;
    }

    /**
     * Solicita la encripción de un contenido específico y retorna el texto
     * en formato de valor de byte para cada posición que lo conforma
     * y separado por coma
     *
     * @param contenido
     * @param clave
     * @return
     */
    public String encriptar(String contenido, String clave) {
        byte[] encrip = getContenido(contenido, clave);
        StringBuilder resp = new StringBuilder();
        String separador = "";
        for (byte local : encrip) {
            resp.append(separador);
            resp.append(local);
            separador = ",";
        }
        return resp.toString();
    }

    /**
     * Dados los datos y la clave el sistema retorna el contenido
     * en bytes que se ha encriptado
     *
     * @param datos
     * @param claveSecreta
     * @return
     */
    public byte[] getContenido(String datos, String claveSecreta) {
        try {
            String key = claveSecreta.substring(0, 32);//clave de 32 bytes
            String iv = claveSecreta.substring(32, 48);//iv de 16 bytes

            Cipher cipher = Cipher.getInstance(ALGORITMO);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), ALGORITMO_CLAVE);
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] res = cipher.doFinal(datos.getBytes("UTF-8"));

            byte[] cadena = android.util.Base64.encode(res, Base64.NO_WRAP);
            return cadena;
        } catch (Exception e) {
        }
        return null;
    }

    public String getDecode(String datos, String claveSecreta) {
        try {
            String key = claveSecreta.substring(0, 32);//clave de 32 bytes
            String iv = claveSecreta.substring(32, 48);//iv de 16 bytes

            Cipher cipher = Cipher.getInstance(ALGORITMO);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), ALGORITMO_CLAVE);
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            byte[] cadena = datos.getBytes(StandardCharsets.UTF_8);
            byte[] encrypted1 = android.util.Base64.decode(cadena, Base64.NO_WRAP);

            cipher = Cipher.getInstance("AES/CBC/NoPadding");
            keyspec = new SecretKeySpec(key.getBytes(), ALGORITMO_CLAVE);
            ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original).trim();
            return originalString;
        } catch (Exception e) {
        }
        return "";
    }
}
