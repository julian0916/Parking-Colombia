package co.zer.repository;

import java.sql.Connection;
import java.util.Map;

public interface ISesionDAO {
    Map<String,Object> getAcceso(Connection conn,String id)throws Exception;
}
