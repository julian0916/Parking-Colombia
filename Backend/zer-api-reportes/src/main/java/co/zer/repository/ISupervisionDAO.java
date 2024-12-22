package co.zer.repository;

import co.zer.model.Paginacion;
import co.zer.model.PreguntaSupervision;
import co.zer.model.RegistroSupervision;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ISupervisionDAO {

    List<Map<String,Object>> reporte(Connection connection,
                                     Long nFechaInicial,
                                     Long nFechaFinal,
                                     Long promotor,
                                     Paginacion paginacion) throws Exception;
}
