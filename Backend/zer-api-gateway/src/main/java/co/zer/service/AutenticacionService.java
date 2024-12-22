package co.zer.service;

import co.zer.model.DiffRespuesta;
import co.zer.utils.Hash;
import co.zer.utils.Uuid;
import com.google.common.hash.Hashing;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase encargada del tema de ingreso al sistema
 * permite
 */
@Service
public class AutenticacionService implements Serializable {

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
            CacheSolicitudService.getInstancia().guardarSolicitud(diffRespuesta, String.valueOf(b));
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return diffRespuesta;
    }

    /**
     * Completa el proceso de autenticación en su etapa de recuperar
     * datos de ingreso al sistema de una forma segura
     *
     * @param idSolicitud         valor de la solicitud iniciada que se desea completar
     * @param valorA              valor que calculó el cliente para A
     * @param contenidoEncriptado contenido de acceso que tiene los datos encriptados
     * @return
     */
    public Map<String, String> getAuthFinal(List<String> camposBuscados, String idSolicitud, String valorA, String contenidoEncriptado) {
        DiffRespuesta diffRespuesta = CacheSolicitudService.getInstancia().recuperarRemoverSolicitud(idSolicitud);
        if (diffRespuesta == null) {
            return new HashMap<>();
        }
        long p = Long.parseLong(diffRespuesta.getP());
        long A = Long.parseLong(valorA);
        long b = Long.parseLong(CacheSolicitudService.getInstancia().recuperarRemoverValorSecreto(diffRespuesta.getIdSolicitud()));
        long sb = moduloPotencia(A, b, p);
        String claveHash = Hashing.sha256()
                .hashString(String.valueOf(sb), StandardCharsets.UTF_8)
                .toString();

        DecodeService decodeService = new DecodeService();
        return decodeService.getContenido(camposBuscados, contenidoEncriptado, claveHash);
    }


}
