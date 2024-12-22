package co.zer.service;

import co.zer.model.Paginacion;
import co.zer.repository.IRegistroDAO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegistroService extends BasicService {
    public Map<String, Object> reporte(
            String cuenta,
            IRegistroDAO iRegistroDAO,
            Long nfYear,
            Boolean enero,
            Boolean febrero,
            Boolean marzo,
            Boolean abril,
            Boolean mayo,
            Boolean junio,
            Boolean julio,
            Boolean agosto,
            Boolean septiembre,
            Boolean octubre,
            Boolean noviembre,
            Boolean diciembre) throws Exception {

        Map<String, Object> resul = new HashMap<>();
        resul.put("reporte", iRegistroDAO.reporte(
                this.getConnect(cuenta),
                nfYear,
                enero,
                febrero,
                marzo,
                abril,
                mayo,
                junio,
                julio,
                agosto,
                septiembre,
                octubre,
                noviembre,
                diciembre));
        resul.put("paginacion", null);

        return resul;
    }

    public Map<String, Object> reporteVehiculo(
            String cuenta,
            IRegistroDAO iRegistroDAO,
            Long nfInicial,
            Long nfFinal,
            String placa,
            Paginacion paginacion) throws Exception {
        Map<String, Object> resul = new HashMap<>();
        resul.put("reporte", iRegistroDAO.reporteVehiculo(
                this.getConnect(cuenta),
                nfInicial,
                nfFinal,
                placa,
                paginacion
        ));
        resul.put("paginacion", paginacion);
        return resul;
    }

    public Map<String, Object> reporteCartera(
            String cuenta,
            IRegistroDAO iRegistroDAO,
            Long nfInicial,
            Long nfFinal,
            Paginacion paginacion) throws Exception {
        Map<String, Object> resul = new HashMap<>();
        resul.put("reporte", iRegistroDAO.reporteCartera(
                this.getConnect(cuenta),
                nfInicial,
                nfFinal,
                paginacion
        ));
        resul.put("paginacion", paginacion);
        return resul;
    }

    public Map<String, Object> reporteHistorico(String cuenta,
                                                IRegistroDAO iRegistroDAO,
                                                Long fechaConsulta) throws Exception {
        Map<String, Object> resul = new HashMap<>();
        resul.put("reporte", iRegistroDAO.reporteHistorico(
                this.getConnect(cuenta),
                fechaConsulta
        ));
        return resul;
    }

    public Map<String, Object> reporteHistoricoMensual(String cuenta,
                                                       IRegistroDAO iRegistroDAO,
                                                       Integer mes,
                                                       Integer year) throws Exception {

        Map<String, Object> resul = new HashMap<>();
        resul.put("reporte", iRegistroDAO.reporteHistoricoMensual(
                this.getConnect(cuenta),
                mes,
                year));

        return resul;
    }
}
