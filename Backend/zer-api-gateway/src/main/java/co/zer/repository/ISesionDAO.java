package co.zer.repository;

import java.sql.Connection;
import java.util.Map;

public interface ISesionDAO {
    void guardarSesion(Connection conn, Map<String, Object> accesoCuentaPerfil) throws Exception;
    void retirarSesion(Connection conn, String id) throws Exception;
    Map<String,Object> getAcceso(Connection conn,String id)throws Exception;
    void retirarSesionInactiva(Connection conn) throws Exception;
}
