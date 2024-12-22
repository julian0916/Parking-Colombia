package co.zer.service;

import co.zer.model.DiffRespuesta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gestiona en forma de cache las solicitudes realizadas
 */
public class CacheSolicitudService implements Serializable {

    private final static long TIEMPO_LIMITE = 300000;//5minutos 1000*60*5
    private static Map<String, DiffRespuesta> solicitud = new HashMap<>();
    private static Map<String, String> valorSecretoSolicitud = new HashMap<>();
    private static Map<String, Long> tiempoLimiteSolicitud = new HashMap<>();
    private static CacheSolicitudService cacheSolicitudService;

    /**
     * Constructor privado
     */
    private CacheSolicitudService(){ }

    /**
     * Retorna la única instancia del sistema disponible
     * @return
     */
    public static CacheSolicitudService getInstancia(){
        if(cacheSolicitudService ==null){
            cacheSolicitudService = new CacheSolicitudService();
        }
        return cacheSolicitudService;
    }
    /**
     * retorna el tiempo en el que una solicitud debe
     * dejar de estar disponible en el sistema.
     *
     * @return retorna el tiempo en milisegundos
     */
    private Long getTiempoLimite() {
        return System.currentTimeMillis() + TIEMPO_LIMITE;
    }

    /**
     * Remueve una solicitud del sistema de cache
     *
     * @param idSolicitud solictud que se desea remover
     */
    private void removerSolicitud(String idSolicitud) {
        recuperarRemoverSolicitud(idSolicitud);
        recuperarRemoverValorSecreto(idSolicitud);
        tiempoLimiteSolicitud.remove(idSolicitud);
    }

    /**
     * Retira las solicitudes que han cumplido su tiempo
     * y no fueron procesadas.
     */
    private void removerSolicitudTiempoLimiteCumplido() {
        try {
            final Long HORA_ACTUAL = System.currentTimeMillis();
            List<String> listadoBorrar = new ArrayList<>();
            for (Map.Entry<String, Long> contenido : tiempoLimiteSolicitud.entrySet()) {
                if (contenido.getValue() < HORA_ACTUAL) {
                    listadoBorrar.add(contenido.getKey());
                }
            }
            listadoBorrar.stream().forEach(content->{
                removerSolicitud(content);
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * almacena la solicitud realizada
     *
     * @param diffRespuesta valores de inicio de la solicitud
     * @param valorSecreto  valor secreto generado del lado de la solicitud
     */
    protected void guardarSolicitud(DiffRespuesta diffRespuesta, String valorSecreto) {
        removerSolicitudTiempoLimiteCumplido();
        solicitud.put(diffRespuesta.getIdSolicitud(), diffRespuesta);
        valorSecretoSolicitud.put(diffRespuesta.getIdSolicitud(), valorSecreto);
        tiempoLimiteSolicitud.put(diffRespuesta.getIdSolicitud(), getTiempoLimite());
    }

    /**
     * recupera el coentenido y remueve la solicitud
     *
     * @param idSolicitud solicitud que se desea gestionar
     * @return retorna el contenido si la solicitud aún existe
     */
    protected DiffRespuesta recuperarRemoverSolicitud(String idSolicitud) {
        return solicitud.remove(idSolicitud);
    }

    /**
     * recupera y remueve el valor secreo asociado a una solicitud
     *
     * @param idSolicitud soliictud que se desea gestionar
     * @return retorna el contenido si la solicitud existe
     */
    protected String recuperarRemoverValorSecreto(String idSolicitud) {
        return valorSecretoSolicitud.remove(idSolicitud);
    }
}