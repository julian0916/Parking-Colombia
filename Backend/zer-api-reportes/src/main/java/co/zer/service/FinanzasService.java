package co.zer.service;

import co.zer.model.Paginacion;
import co.zer.repository.IFinancieraDAO;
import co.zer.repository.ISupervisionDAO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FinanzasService extends BasicService {

    public Map<String, Object> reporte(
            String cuenta,
            IFinancieraDAO iFinancieraDAO,
            Boolean estado,
            Long promotor,
            Paginacion paginacion) throws Exception {
        Map<String, Object> resul = new HashMap<>();
        resul.put("reporte",iFinancieraDAO.reporte(this.getConnect(cuenta), estado, promotor, paginacion));
        resul.put("paginacion",paginacion);
        return resul;
    }
}
