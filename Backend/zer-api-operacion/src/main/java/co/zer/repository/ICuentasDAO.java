package co.zer.repository;

import java.sql.Connection;
import java.util.List;

public interface ICuentasDAO {
    List<String> getCuentas(Connection conn) throws Exception;
}
