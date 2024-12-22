package co.zer.repository;

import co.zer.model.PreguntaSupervision;
import co.zer.model.RegistroSupervision;

import java.sql.Connection;
import java.util.List;

public interface ISupervisionDAO {
    void ajustarContenido(PreguntaSupervision preguntaSupervision);
    void validarContenido(PreguntaSupervision preguntaSupervision) throws Exception;
    List<PreguntaSupervision> guardar(Connection connection, PreguntaSupervision preguntaSupervision) throws Exception;
    List<PreguntaSupervision> insertar(Connection connection, PreguntaSupervision preguntaSupervision) throws Exception;
    List<PreguntaSupervision> actualizar(Connection connection, PreguntaSupervision preguntaSupervision) throws Exception;
    List<PreguntaSupervision> consultar(Connection connection) throws Exception;
    List<PreguntaSupervision> borrar(Connection connection,Long id) throws Exception;
    Long grabarEncabezadoSupervision(Connection connection,RegistroSupervision registroSupervision) throws Exception;
    void grabarDetalleSupervision(Connection connection,RegistroSupervision registroSupervision) throws Exception;
    void validarFirmaPromotor(Connection connection,RegistroSupervision registroSupervision) throws Exception;
}
