package co.zer.repository;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IRegistroDAO {

    Map<String,Double> getRecaudoTotal(Connection connection, Long fnInicio, Long fnFin) throws Exception;

    Map<String,Double> getRecaudoPrepago(Connection connection, Long fnInicio, Long fnFin) throws Exception;

    Map<String,Double> getRecaudoPostpago(Connection connection, Long fnInicio, Long fnFin) throws Exception;

    Map<String,Double> getRecaudoExtemporaneo(Connection connection, Long fnInicio, Long fnFin) throws Exception;

    Map<String,Double> getCarteraTotal(Connection connection, Long fnInicio, Long fnFin) throws Exception;

    Map<String,Double> getVentaPostpago(Connection connection, Long fnInicio, Long fnFin) throws Exception;

    Map<String, Map<String, Object>> getServiciosPeriodoGracia(Connection connection, Long fnInicio, Long fnFin) throws Exception;

    List<Map<String,Object>> getOcupacionZona(Connection connection, LocalDateTime lDTConsulta) throws Exception;


}
