package co.zer.repository;

import co.zer.model.Paginacion;
import co.zer.model.Usuario;

import java.sql.Connection;
import java.util.List;

public interface IUsuarioDAO {
    void ajustarContenido(Usuario usuario);
    void validarContenido(Usuario usuario) throws Exception;
    Usuario guardar(Connection connection, Usuario usuario) throws Exception;
    Usuario insertar(Connection connection,Usuario usuario) throws Exception;
    Usuario actualizar(Connection connection,Usuario usuario) throws Exception;
    List<Object> listarUsuarios(Connection connection, Paginacion paginacion) throws Exception;
    List<Object> listarDocumentos(Connection connection) throws Exception;
    List<Object> listarPerfiles(Connection connection) throws Exception;
}
