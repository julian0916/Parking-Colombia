package co.zer.repository;

import co.zer.model.Paginacion;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface IRegistroDAO {

    List<Map<String,Object>> reporte(Connection connection,
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
                                     Boolean diciembre) throws Exception;

    List<Map<String,Object>> reporteVehiculo(Connection connection,
                                             Long nfInicial,
                                             Long nfFinal,
                                             String placa,
                                             Paginacion paginacion) throws Exception;

    List<Map<String,Object>> reporteCartera(Connection connection,
                                            Long nfInicial,
                                            Long nfFinal,
                                            Paginacion paginacion) throws Exception;

    List<Map<String,Object>> reporteHistorico(Connection connection,
                                              Long fechaConsulta) throws Exception;

    List<Map<String, Object>> reporteHistoricoMensual(
            Connection connection,
            Integer mes,
            Integer year) throws Exception;
}
