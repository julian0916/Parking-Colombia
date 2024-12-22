package co.zer.service;

import co.zer.model.Paginacion;
import co.zer.model.Zona;
import co.zer.repository.IZonaDAO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ZonaService extends BasicService {

    public Zona guardar(String cuenta, IZonaDAO iZonaDAO, Zona zona) throws Exception {
        return iZonaDAO.guardar(this.getConnect(cuenta), zona);
    }

    public Map<String,Object> listarZonas(String cuenta, IZonaDAO iZonaDAO, Paginacion paginacion) throws Exception {
        Map<String,Object> resultado = new HashMap<>();
        resultado.put("zonas", iZonaDAO.listarZonas(this.getConnect(cuenta), paginacion));
        resultado.put("paginacion",paginacion);
        return resultado;
    }
}
