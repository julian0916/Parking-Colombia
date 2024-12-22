package com.example.zer.somos.autenticar;


import com.example.zer.somos.utilidades.Hash;
import com.example.zer.somos.utilidades.Uuid;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase encargada del tema de ingreso al sistema
 * permite
 */
public class Autenticar implements Serializable {

    private final static char[] hexArray = "0123456789abcdef".toCharArray();

    private static String encodeHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Retorna dado un número el siguiente número impar de este
     *
     * @param numero numero actual de referencia
     * @return siguiente número impar al dado
     */
    private long siguienteImpar(long numero) {
        if (numero % 2 == 0) {
            return numero + 1;
        }
        return numero + 2;
    }

    /**
     * verifica la primaridad de un número daddo de una forma más
     * rápida que la realización de todas las divisiones sobre el número dado
     *
     * @param numero numero que se desea verificar para saber si es primo
     * @return true cuando se encuentra que el número es primo
     */
    private boolean esPrimo(long numero) {
        if (numero < 4) {
            return numero > 1;
        }
        if (numero % 2 == 0 || numero % 3 == 0) {
            return false;
        }
        long indice = 5;
        while (indice * indice <= numero) {
            if (numero % indice == 0 || numero % (indice + 2) == 0) {
                return false;
            }
            indice += 6;
        }
        return true;
    }

    /**
     * retorna un valor aleatorio dado un valor de referencia
     *
     * @param max valor dado como referencia
     * @return retorna un valor aleatorio
     */
    private long getValor(long max) {
        return (long) (Math.random() * max);
    }

    /**
     * retorna el término p del algoritmo de intercambio de claves
     *
     * @param max valor de referencia para generar p
     * @return retorna un valor de p que es primo
     */
    private long getP(long max) {
        long ref = getValor(max);
        ref = siguienteImpar(ref);
        while (!esPrimo(ref)) {
            ref = siguienteImpar(ref);
        }
        return ref;
    }

    /**
     * calcula la operción de b^e % m de una forna eficiente usando
     * exponentes binarios
     *
     * @param base base
     * @param exp  exponente
     * @param m    modulo
     * @return retorna el resultado de ejecutar base ^ exp % modulo
     */
    private long moduloPotencia(long base, long exp, long m) {
        long result = 1;
        while (exp > 0) {
            if ((exp & 1) > 0) {
                result = (result * base) % m;
            }
            exp >>= 1;
            base = (base * base) % m;
            int r = 0;
        }
        return result;
    }

    /**
     * retorna un valor hash para referenciar un valor de la solicitud realizada
     *
     * @return
     */
    private String getIdSolicitud() {
        return Hash.getHashStringHexa(Uuid.getUuidTexto());
    }

    /**
     * Permite iniciar el proceso de autenticación mediante
     * el intercambio de claves para proteger la información
     * que será suministrada
     *
     * @return un objecto de DiffRespuesta con los valores de
     * g, p, B, sesion
     */
    public DiffRespuesta getAuthInicial() {
        DiffRespuesta diffRespuesta = new DiffRespuesta();
        try {
            long p = getP(4775807L);
            long g = 2L;
            long b = getValor(((long) (Math.log(p) / Math.log(g))) * 10000);
            long B = moduloPotencia(g, b, p);
            diffRespuesta.setB(String.valueOf(B));
            diffRespuesta.setP(String.valueOf(p));
            diffRespuesta.setG(String.valueOf(g));
            diffRespuesta.setIdSolicitud(getIdSolicitud());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return diffRespuesta;
    }

    public Map<String, Object> getAuthCompletar(long p, long g, long b) {
        Map<String, Object> cont = new HashMap<>();
        try {
            long a = getValor(((long) (Math.log(p) / Math.log(g))) * 10000);
            long A = moduloPotencia(g, a, p);
            long sa = moduloPotencia(b, a, p);
            // String claveHash = Hashing.sha256()
            //         .hashString(String.valueOf(sa), StandardCharsets.UTF_8)
            //        .toString();
            //String claveHash = encodeHexString(String.valueOf(sa).getBytes());
            String claveHash = Hash.getHashStringHexa(String.valueOf(sa));
            cont.put("sa", sa);
            cont.put("a", a);
            cont.put("A", A);
            cont.put("claveHash", claveHash);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cont;
    }
}
