package co.zer.service;

import co.zer.model.Paginacion;
import co.zer.repository.ISupervisionDAO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SupervisionService extends BasicService {

    public Map<String, Object> reporte(
            String cuenta,
            ISupervisionDAO iSupervisionDAO,
            Long nFechaInicial,
            Long nFechaFinal,
            Long promotor,
            Paginacion paginacion) throws Exception {
        Map<String, Object> resul = new HashMap<>();
        resul.put("reporte",iSupervisionDAO.reporte(this.getConnect(cuenta), nFechaInicial, nFechaFinal, promotor, paginacion));
        resul.put("paginacion",paginacion);
        return resul;
    }
}
