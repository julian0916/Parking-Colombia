package co.zer.service;

import co.zer.repository.ISesionDAO;

import java.io.Serializable;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gestiona en forma de cache las solicitudes realizadas
 */
public class CacheSesionService implements Serializable {

    private static CacheSesionService cacheSesionService;
    private Map<String, Map<String, Object>> sesiones = new HashMap<>();
    private Map<String, LocalDateTime> tiempoSesiones = new HashMap<>();

    /**
     * s
     * Constructor privado
     */
    private CacheSesionService() {
    }

    /**
     * Retorna la única instancia del sistema disponible
     *
     * @return
     */
    private static CacheSesionService getInstancia() {
        if (cacheSesionService == null) {
            cacheSesionService = new CacheSesionService();
        }
        return cacheSesionService;
    }

    /**
     * Verifica que la sesión tenga el permiso indicado a la cuenta
     * con el perfil de referencia.
     *
     * @param conn
     * @param iSesionDAO
     * @param idSesion
     * @param cuenta
     * @param perfiles
     * @throws Exception
     */
    public static void validarAcceso(Connection conn, ISesionDAO iSesionDAO, String idSesion, String cuenta, int... perfiles) throws Exception {
        if (!getInstancia().getSesiones().containsKey(idSesion)) {
            getInstancia().subirDatosSesion(conn, iSesionDAO, idSesion);
        }
        boolean encontrado = false;
        if (getInstancia().getSesiones().containsKey(idSesion)) {
            Map<String, Object> contenido = getInstancia().getSesiones().get(idSesion);
            int perfilSesion = (int) contenido.get("perfil");
            int pos = 0;
            int tama = perfiles.length;
            while (!encontrado && pos < tama) {
                encontrado = perfiles[pos] == perfilSesion;
                pos++;
            }
            encontrado = encontrado && contenido.get("cuenta").toString().equals(cuenta);
            if (encontrado) {
                getInstancia().actualizarTiempo(idSesion);
            }
        }
        if (!encontrado) {
            throw new Exception("El usuario no tiene permisos para realizar esta acción :(");
        }
    }

    /**
     * Hilo para limpiar el cache
     */
    private static void iniciarHiloLimpiarCache() {
        Thread t = new Thread() {
            public void run() {
                CacheSesionService.limpiarCache();
            }
        };
        t.start();
    }

    /**
     * Limpia el cache de cada 60 minutos para borrar las no usadas
     * en un periodo de una hora
     */
    private static void limpiarCache() {
        List<String> borrar = new ArrayList<>();
        LocalDateTime actual = LocalDateTime.now();
        final int minutos = 60;
        for (Map.Entry<String, LocalDateTime> data : getInstancia().getTiempoSesiones().entrySet()) {
            if (data.getValue().plusMinutes(minutos).isBefore(actual)) {
                borrar.add(data.getKey());
            }
        }
        for (String item : borrar) {
            getInstancia().getSesiones().remove(item);
            getInstancia().getTiempoSesiones().remove(item);
        }
    }

    /**
     * Retornar las sesiones del cache
     *
     * @return
     */
    private Map<String, Map<String, Object>> getSesiones() {
        return sesiones;
    }

    /**
     * Retorna los tiempos de las sesiones
     *
     * @return
     */
    private Map<String, LocalDateTime> getTiempoSesiones() {
        return tiempoSesiones;
    }

    /**
     * Controla los tiempos de uso de los llamados de las sesiones
     *
     * @param idSesion
     */
    private void actualizarTiempo(String idSesion) {
        getInstancia().getTiempoSesiones().put(idSesion, LocalDateTime.now());
    }

    /**
     * Agrega al cache el dato de la sesión si se encuentra en la base de datos
     *
     * @param conn
     * @param iSesionDAO
     * @param idSesion
     */
    private void subirDatosSesion(Connection conn, ISesionDAO iSesionDAO, String idSesion) {
        try {
            Map<String, Object> acceso = iSesionDAO.getAcceso(conn, idSesion);
            if (acceso.size() > 0) {
                getInstancia().getSesiones().put(idSesion, acceso);
                getInstancia().actualizarTiempo(idSesion);
            }
        } catch (Exception ex) {
        }
        iniciarHiloLimpiarCache();
    }
}