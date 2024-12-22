package co.zer.service;

import co.zer.model.RegistroCompleto;
import co.zer.repository.IRegistroDAO;
import co.zer.repository.IZonaDAO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InformacionService extends BasicService {
    /**
     * Retorna la ocupación de zona por tipo de vehículo con el respectivo porcentaje.
     *
     * @param cuenta
     * @param idZona
     * @param iRegistroDAO
     * @param iZonaDAO
     * @return
     * @throws Exception
     */
    public Object ocupacionZona(String cuenta, Long idZona, IRegistroDAO iRegistroDAO, IZonaDAO iZonaDAO) throws Exception {
        return iRegistroDAO.ocupacionZona(this.getConnect(cuenta), idZona);
    }

    /**
     * Retorna el contenido de la cartera actual relacionada con la placa
     * dada como parámetro
     *
     * @param cuenta
     * @param placa
     * @param iRegistroDAO
     * @param iZonaDAO
     * @return
     * @throws Exception
     */
    public Object getCarteraPorPlaca(String cuenta, String placa, IRegistroDAO iRegistroDAO, IZonaDAO iZonaDAO, PreliquidarService preliquidarService) throws Exception {
        Map<String, Object> res = new HashMap<>();
        res.put("reportados", iRegistroDAO.carteraPorPlaca(this.getConnect(cuenta), ComunRegistro.limpiarContenidosPlaca(placa)));
        res.put("preliquidados", preliquidarService.preliquidarPlaca(cuenta, placa, iRegistroDAO, iZonaDAO));
        return res;
    }

    /**
     * Retorna para la fecha y hora actual +-7 minutos todos los registros
     * que se encuentran con estado prepago y que no tienen un nuevo registro
     * posterior a ese.
     *
     * @param cuenta
     * @param idZona
     * @param iRegistroDAO
     * @param iZonaDAO
     * @return
     * @throws Exception
     */
    public Object getAlertasPrepagoZona(
            String cuenta,
            Long idZona,
            IRegistroDAO iRegistroDAO,
            IZonaDAO iZonaDAO,
            LocalDateTime localDateTime) throws Exception {
        return iRegistroDAO.getAlertasPrepagoZona(
                this.getConnect(cuenta),
                idZona,
                localDateTime);
    }

    public List<RegistroCompleto> getUltimosMovimientosHoy(String cuenta,
                                                           String placa,
                                                           IRegistroDAO iRegistroDAO) throws Exception {
        return iRegistroDAO.getUltimosMovimientosHoy(this.getConnect(cuenta), placa);
    }
}