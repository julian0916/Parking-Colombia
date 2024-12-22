package co.zer.service;

import co.zer.repository.IAutorizarDAO;
import co.zer.repository.ISesionDAO;
import co.zer.utils.Hash;
import co.zer.utils.Uuid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AutorizacionService extends BasicService {

    private void crearSesion(Map<String, Object> accesoCuentaPerfil, ISesionDAO iSesionDAO) throws Exception {
        if (accesoCuentaPerfil == null) {
            return;
        }
        if (accesoCuentaPerfil.size() < 1) {
            return;
        }
        String idSesion = Hash.getHashStringHexa(Uuid.getUuidTexto());
        accesoCuentaPerfil.put("sesion", idSesion);
        iSesionDAO.guardarSesion(getConnect(SCHEMA_CUENTAS), accesoCuentaPerfil);
    }

    public List<Object> iniciarSesion(String usuario, String claveHash, IAutorizarDAO iAutorizarDAO, ISesionDAO iSesionDAO) {
        try {
            List<Object> respuesta = iAutorizarDAO.getAccesoCuentaPerfil(getConnect(SCHEMA_CUENTAS), usuario, claveHash);
            for (Object content : respuesta) {
                crearSesion((Map<String, Object>) content, iSesionDAO);
            }
            return respuesta;
        } catch (Exception ex) {
            return null;
        }
    }

    public void terminarSesion(String sessionId, ISesionDAO iSesionDAO) throws Exception {
        iSesionDAO.retirarSesion(getConnect(SCHEMA_CUENTAS),sessionId);
    }

    @Transactional
    public void terminarSesionTiempo(ISesionDAO iSesionDAO) throws Exception{
        iSesionDAO.retirarSesionInactiva(getConnect(SCHEMA_CUENTAS));
    }

    private static class CacheSesion {
        private static Map<String, List<String>> recurso = new HashMap<>();
        private static Map<String, List<String>> schema = new HashMap<>();
    }
}
