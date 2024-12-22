package co.zer.repository;

import co.zer.model.Paginacion;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface IFinancieraDAO {
    List<Map<String,Object>> reporte(Connection connection,
                                     Boolean estado,
                                     Long promotor,
                                     Paginacion paginacion) throws Exception;
}
