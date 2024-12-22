package co.zer.service;

import co.zer.repository.ISesionDAO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class SeguridadService extends BasicService {

    public static final int
            CREADOR_CUENTAS = 100,
            ADMINISTRADOR = 101,
            SUPERVISOR = 102,
            VISOR = 103,
            MUNICIPIO = 104,
            PROMOTOR = 105,
            AUXILIAR = 106;

    /**
     * Cuando no tiene permisos el sistema lanza una excepción
     * @param iSesionDAO
     * @param httpServletRequest
     * @param permisos
     * @throws Exception
     */
    public void validarPermiso(ISesionDAO iSesionDAO, String cuenta, HttpServletRequest httpServletRequest, int... permisos) throws Exception {
        String idSesionSistema = httpServletRequest.getHeader("idSesionSistema");
        CacheSesionService.validarAcceso(
                getConnect(SCHEMA_CUENTAS),
                iSesionDAO,
                idSesionSistema,
                cuenta,
                permisos
        );
    }
}
