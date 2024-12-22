package co.zer.repository;

import co.zer.model.Paginacion;
import co.zer.model.Zona;
import co.zer.model.Zona;

import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface IZonaDAO {
    void ajustarContenido(Zona zona) throws Exception;
    void validarContenido(Zona zona) throws Exception;
    Zona guardar(Connection connection, Zona zona) throws Exception;
    Zona insertar(Connection connection,Zona zona) throws Exception;
    Zona actualizar(Connection connection,Zona zona) throws Exception;
    List<Object> listarZonas(Connection connection, Paginacion paginacion) throws Exception;
}
