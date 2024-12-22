package co.zer.repository;

import co.zer.model.PromotorDTO;

import java.sql.Connection;
import java.util.List;

public interface IPromotorDAO {
    Boolean superaLimiteEndeudamiento(Connection connection, Long idPromotor) throws Exception;
    Long obtenerTotalRecaudado(Connection connection, Long idPromotor) throws Exception;
    Boolean estaBloqueado(Connection connection, Long promotor) throws Exception;
    void bloquearPromotor(Connection connection, Long promotor) throws Exception;
    void desbloquearPromotor(Connection connection, Long promotor, Long supervisor) throws Exception;
    List<PromotorDTO> listarBloqueados(Connection connection) throws Exception;
}
