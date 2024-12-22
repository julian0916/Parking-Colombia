package co.zer.service;

import co.zer.model.Paginacion;
import co.zer.model.PromotorZona;
import co.zer.repository.IPromotorZonaDAO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PromotorZonaService extends BasicService {

    public PromotorZona guardar(String cuenta, IPromotorZonaDAO iPromotorZonaDAO, PromotorZona promotorZona) throws Exception {
        return iPromotorZonaDAO.guardar(this.getConnect(cuenta), promotorZona);
    }

    public Map<String, Object> listarPromotoresZonas(String cuenta, IPromotorZonaDAO iPromotorZonaDAO, Paginacion paginacion) throws Exception {
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("promotorZonas", iPromotorZonaDAO.listarPromotoresZonas(this.getConnect(cuenta), paginacion));
        resultado.put("paginacion", paginacion);
        return resultado;
    }

    public List<Object> listarZonasAsignadasPromotor(String cuenta, IPromotorZonaDAO iPromotorZonaDAO, String correoPromotor) throws Exception {
        return iPromotorZonaDAO.listarZonasAsignadasPromotor(this.getConnect(cuenta), correoPromotor);
    }

    public List<Object> listarPromotoresAsignadosAZona(String cuenta, IPromotorZonaDAO iPromotorZonaDAO, Long zona) throws Exception {
        return iPromotorZonaDAO.listarPromotoresAsignadoAZona(this.getConnect(cuenta), zona);
    }



}
