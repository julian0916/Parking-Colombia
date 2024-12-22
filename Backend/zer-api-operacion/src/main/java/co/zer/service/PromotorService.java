package co.zer.service;

import co.zer.model.PromotorDTO;
import co.zer.repository.IPromotorDAO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PromotorService extends BasicService {

    public Map<String, Object> superaLimiteEndeudamiento(
            String cuenta,
            IPromotorDAO iPromotorDAO,
            Long idPromotor
    ) throws Exception {
        Map<String, Object> cont = new HashMap<>();
        cont.put("superaLimiteDeuda", iPromotorDAO.superaLimiteEndeudamiento(this.getConnect(cuenta), idPromotor));
        return cont;
    }

    public Long obtenerTotalRecaudado(
            String cuenta,
            IPromotorDAO iPromotorDAO,
            Long promotor
    ) throws Exception {
        return iPromotorDAO.obtenerTotalRecaudado(this.getConnect(cuenta), promotor);
    }

    public Boolean estaBloqueado(String cuenta,
                                 IPromotorDAO iPromotorDAO,
                                 Long promotor
    ) throws Exception {
        return iPromotorDAO.estaBloqueado(this.getConnect(cuenta), promotor);
    }

    public void bloquearPromotor(
            String cuenta,
            IPromotorDAO iPromotorDAO,
            Long promotor) throws Exception {

        // Lógica para bloquear al promotor
        iPromotorDAO.bloquearPromotor(this.getConnect(cuenta), promotor);
    }

    public void desbloquearPromotor(
            String cuenta,
            IPromotorDAO iPromotorDAO,
            Long promotor,
            Long supervisor) throws Exception {
         iPromotorDAO.desbloquearPromotor(this.getConnect(cuenta), promotor, supervisor);
    }
    public List<PromotorDTO> listarBloqueados(
            String cuenta,IPromotorDAO ipromotorDAO
    ) throws Exception {
        return ipromotorDAO.listarBloqueados(this.getConnect(cuenta));
    }


}
