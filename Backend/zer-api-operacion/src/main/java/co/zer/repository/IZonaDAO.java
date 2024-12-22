package co.zer.repository;

import co.zer.model.Zona;

import java.sql.Connection;

public interface IZonaDAO {
    Zona getZonaPorID(Connection connection, Long id) throws Exception;
}
