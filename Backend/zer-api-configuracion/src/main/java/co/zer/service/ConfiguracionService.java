package co.zer.service;

import co.zer.model.Tiquete;
import co.zer.repository.IConfiguracionDAO;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracionService extends BasicService {

    public Tiquete guardar(String cuenta, IConfiguracionDAO iConfiguracionDAO, Tiquete tiquete) throws Exception {
        return iConfiguracionDAO.guardar(this.getConnect(cuenta), tiquete);
    }

    public Tiquete consultar(String cuenta, IConfiguracionDAO iConfiguracionDAO) throws Exception {
        return iConfiguracionDAO.consultar(this.getConnect(cuenta));
    }

    public void guardarLimiteEndeudamientoPromotor(String cuenta, IConfiguracionDAO iConfiguracionDAO, Long valor) throws Exception {
        iConfiguracionDAO.guardarLimiteEndeudamientoPromotor(this.getConnect(cuenta), valor);
    }

    public Long consultarLimiteEndeudamientoPromotor(String cuenta, IConfiguracionDAO iConfiguracionDAO) throws Exception {
        return iConfiguracionDAO.getLimiteEndeudamientoPromotor(this.getConnect(cuenta));
    }
}
