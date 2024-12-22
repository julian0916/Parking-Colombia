package co.zer.repository;

import co.zer.model.Paginacion;
import co.zer.model.Tiquete;

import java.sql.Connection;
import java.util.List;

public interface IConfiguracionDAO {

    final static Long ID = 101L;
    void ajustarContenido(Tiquete tiquete);
    void validarContenido(Tiquete tiquete) throws Exception;
    Tiquete guardar(Connection connection, Tiquete tiquete) throws Exception;
    Tiquete insertar(Connection connection, Tiquete tiquete) throws Exception;
    Tiquete actualizar(Connection connection, Tiquete tiquete) throws Exception;
    Tiquete consultar(Connection connection) throws Exception;

    void guardarLimiteEndeudamientoPromotor(Connection connection, Long valor) throws Exception;
    Long getLimiteEndeudamientoPromotor(Connection connection) throws Exception;
}
