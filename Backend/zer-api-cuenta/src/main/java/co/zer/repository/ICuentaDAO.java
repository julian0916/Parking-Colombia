package co.zer.repository;

import co.zer.model.Cuenta;
import co.zer.model.Paginacion;

import java.sql.Connection;
import java.util.List;

public interface ICuentaDAO {
    void ajustarContenido(Cuenta cuenta);
    void validarContenido(Cuenta cuenta) throws Exception;
    Cuenta guardar(Connection connection,Cuenta cuenta) throws Exception;
    Cuenta insertar(Connection connection,Cuenta cuenta) throws Exception;
    Cuenta actualizar(Connection connection,Cuenta cuenta) throws Exception;
    List<Cuenta> listarCuentas(Connection connection, Paginacion paginacion) throws Exception;
    List<Object> listarCuentasUsuario(Connection connection, String usuario) throws Exception;
}
