package co.zer.service;

import co.zer.repository.IRegistroDAO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService extends BasicService {
    public Map<String, Object> getDatosDashboard(String cuenta,
                                                              Long fNInicia,
                                                              Long fNTermina,
                                                              IRegistroDAO iRegistroDAO) throws Exception {
        Map<String, Object> res = new HashMap();
        res.put("RecaudoTotal", iRegistroDAO.getRecaudoTotal(this.getConnect(cuenta), fNInicia, fNTermina));
        res.put("RecaudoPrepago", iRegistroDAO.getRecaudoPrepago(this.getConnect(cuenta), fNInicia, fNTermina));
        res.put("RecaudoPostpago", iRegistroDAO.getRecaudoPostpago(this.getConnect(cuenta), fNInicia, fNTermina));
        res.put("RecaudoExtemporaneo", iRegistroDAO.getRecaudoExtemporaneo(this.getConnect(cuenta), fNInicia, fNTermina));
        res.put("CarteraTotal", iRegistroDAO.getCarteraTotal(this.getConnect(cuenta), fNInicia, fNTermina));
        res.put("VentaPostpago", iRegistroDAO.getVentaPostpago(this.getConnect(cuenta), fNInicia, fNTermina));
        res.put("ServiciosPeriodoGracia", iRegistroDAO.getServiciosPeriodoGracia(this.getConnect(cuenta), fNInicia, fNTermina));
        return res;
    }

    public List<Map<String, Object>> getOcupacionZona(String cuenta,
                                                      LocalDateTime lDTConsulta,
                                                      IRegistroDAO iRegistroDAO) throws Exception {
        return iRegistroDAO.getOcupacionZona(this.getConnect(cuenta),
                lDTConsulta);
    }
}