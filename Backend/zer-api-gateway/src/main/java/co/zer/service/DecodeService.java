package co.zer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecodeService {

    protected final String ALGORITMO = "AES/CBC/NoPadding";
    protected final String ALGORITMO_CLAVE = "AES";

    /**
     * Retorna el contenido decodificado en el map con las claves
     *
     * @param datosEncriptados datos codificados
     * @param claveSecreta     clave con la cual los datos se codificaron
     * @return Map<String, String> usuario y clave
     */
    protected Map<String, String> getContenido(List<String> camposBuscados, String datosEncriptados, String claveSecreta) {
        Map<String, String> mapContenido = new HashMap<>();
        try {
            String key = claveSecreta.substring(0, 32);//clave de 32 bytes
            String iv = claveSecreta.substring(32, 48);//iv de 16 bytes

            String[] caracteres = datosEncriptados.split(",");
            String cadena = "";
            for (String caracter : caracteres) {
                cadena += String.valueOf(((char) Integer.parseInt(caracter)));
            }
            byte[] encrypted1 = java.util.Base64.getDecoder().decode(cadena);
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), ALGORITMO_CLAVE);
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original).trim();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode payloadObj = mapper.readTree(originalString);
            camposBuscados.stream().forEach(campo -> {
                try {
                    mapContenido.put(campo, payloadObj.get(campo).asText());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapContenido;
    }
}
