package co.zer.repository;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;

@Service
public interface IAutorizarDAO {
    List<String> getRecursos(Connection connection,String usuario, String claveHash);
    List<String> getCuentas(Connection connection,String usuario, String claveHash);
    List<Object> getAccesoCuentaPerfil(Connection connection, String correoOrId, String claveHash) throws Exception;
}
