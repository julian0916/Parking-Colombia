package co.zer.service;

import co.zer.model.EstadoVehiculo;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CacheEstadoVehiculoService {

    private static CacheEstadoVehiculoService cacheEstadoVehiculoService = null;
    private Map<String, Map<String, EstadoVehiculo>> mapEstado = null;

    private CacheEstadoVehiculoService() {
    }

    private static CacheEstadoVehiculoService getInstancia() {
        if (cacheEstadoVehiculoService == null) {
            cacheEstadoVehiculoService = new CacheEstadoVehiculoService();
        }
        return cacheEstadoVehiculoService;
    }

    private static Map<String, EstadoVehiculo> getMapEstado(String cuenta) {
        if (getInstancia().mapEstado == null) {
            getInstancia().mapEstado = new HashMap<>();
        }
        if (!getInstancia().mapEstado.containsKey(cuenta)) {
            getInstancia().mapEstado.put(cuenta, new HashMap<>());
        }
        return getInstancia().mapEstado.get(cuenta);
    }

    public static EstadoVehiculo getEstado(String cuenta, String placa) {
        final EstadoVehiculo actual = getMapEstado(cuenta).get(placa);
        return actual;
    }

    public static void ingresarZonaPostpago(String cuenta, String placa, Long zona, Long idCuenta, Boolean esCarro) throws Exception {
        if (!getMapEstado(cuenta).containsKey(placa)) {
            getMapEstado(cuenta).put(placa, new EstadoVehiculo());
        }
        getMapEstado(cuenta).get(placa).setPlaca(placa);
        getMapEstado(cuenta).get(placa).setZona(zona);
        getMapEstado(cuenta).get(placa).setIdCuenta(idCuenta);
        getMapEstado(cuenta).get(placa).setEsCarro(esCarro);
        getMapEstado(cuenta).get(placa).setEsPrepago(false);
        getMapEstado(cuenta).get(placa).setEstaAbierta(true);
    }

    public static void ingresarZonaPrepago(String cuenta, String placa, Long zona, Long idCuenta, Boolean esCarro, LocalDateTime FHDExpiraCredito) throws Exception {
        if (!getMapEstado(cuenta).containsKey(placa)) {
            getMapEstado(cuenta).put(placa, new EstadoVehiculo());
        }
        getMapEstado(cuenta).get(placa).setPlaca(placa);
        getMapEstado(cuenta).get(placa).setZona(zona);
        getMapEstado(cuenta).get(placa).setIdCuenta(idCuenta);
        getMapEstado(cuenta).get(placa).setEsCarro(esCarro);
        getMapEstado(cuenta).get(placa).setEsPrepago(true);
        getMapEstado(cuenta).get(placa).setEstaAbierta(false);
        getMapEstado(cuenta).get(placa).setFHExpiraCredito(FHDExpiraCredito);
    }

    public static void salirZonaPostpago(String cuenta, String placa, Long zona, Long idCuenta, Boolean esCarro, LocalDateTime FHDExpiraCredito, LocalDateTime FHDUltimoPeriodoGracia) {
        if (!getMapEstado(cuenta).containsKey(placa)) {
            getMapEstado(cuenta).put(placa, new EstadoVehiculo());
        }
        getMapEstado(cuenta).get(placa).setPlaca(placa);
        getMapEstado(cuenta).get(placa).setZona(zona);
        getMapEstado(cuenta).get(placa).setIdCuenta(idCuenta);
        getMapEstado(cuenta).get(placa).setEsCarro(esCarro);
        getMapEstado(cuenta).get(placa).setEsPrepago(false);
        getMapEstado(cuenta).get(placa).setEstaAbierta(false);
        getMapEstado(cuenta).get(placa).setFHExpiraCredito(FHDExpiraCredito);
        getMapEstado(cuenta).get(placa).setFHUltimoPeriodoGracia(FHDUltimoPeriodoGracia);
    }

    public static void eliminarContenidos(){
        cacheEstadoVehiculoService = null;
        getInstancia();
    }
}
