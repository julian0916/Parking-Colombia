package co.zer.service;

import co.zer.model.PreguntaSupervision;
import co.zer.model.RegistroSupervision;
import co.zer.model.Tiquete;
import co.zer.repository.ISupervisionDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupervisionService extends BasicService {

    public List<PreguntaSupervision> guardar(String cuenta, ISupervisionDAO iSupervisionDAO, PreguntaSupervision preguntaSupervision) throws Exception {
        return iSupervisionDAO.guardar(this.getConnect(cuenta), preguntaSupervision);
    }

    public List<PreguntaSupervision> consultar(String cuenta, ISupervisionDAO iSupervisionDAO) throws Exception {
        return iSupervisionDAO.consultar(this.getConnect(cuenta));
    }

    public List<PreguntaSupervision> borrar(String cuenta, ISupervisionDAO iSupervisionDAO,Long id) throws Exception {
        return iSupervisionDAO.borrar(this.getConnect(cuenta), id);
    }

    public void guardarRegistroSupervision(String cuenta, ISupervisionDAO iSupervisionDAO, RegistroSupervision registroSupervision) throws Exception {
        iSupervisionDAO.validarFirmaPromotor(this.getConnect(cuenta), registroSupervision);
        iSupervisionDAO.grabarEncabezadoSupervision(this.getConnect(cuenta), registroSupervision);
        iSupervisionDAO.grabarDetalleSupervision(this.getConnect(cuenta), registroSupervision);
    }
}
