package co.zer.repository;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AutorizarDAO implements IAutorizarDAO {

    @Override
    public List<String> getRecursos(Connection connection, String usuario, String claveHash) {
        return null;
    }

    @Override
    public List<String> getCuentas(Connection connection, String usuario, String claveHash) {
        return null;
    }

    /**
     * Retorna las cuentas con los perfiles a los cuales se
     * tiene acceso con los datos suministrados
     *
     * @param connection objeto de conexión DB
     * @param correoOrId correo o id del usuario que busca el acceso
     * @param claveHash  contenido de la clave de acceso en formato hash sha256
     * @return listado con datos de cada cuenta con su respectivo acceso
     * @throws Exception
     */
    @Override
    public List<Object> getAccesoCuentaPerfil(Connection connection, String correoOrId, String claveHash) throws Exception {
        List<Object> resultado = new ArrayList<>();

        String SQL_ACCESO_CORREO = "SELECT a.perfil, p.nombre, c.id, c.nombre, c.informacion, c.activo, a.correo\n" +
                "FROM\n" +
                "acceso a,\n" +
                "cuenta c,\n" +
                "perfil p\n" +
                "WHERE\n" +
                "p.id = a.perfil \n" +
                "AND c.activo = TRUE \n" +
                "AND c.id = a.cuenta \n" +
                "AND (a.correo = ?\n" +
                "OR a.identificacion = ?)\n" +
                "AND a.activo = TRUE \n" +
                "AND a.clave_hash = ?\n" +
                "ORDER BY 4";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_ACCESO_CORREO);
            pst.setString(1, correoOrId);
            pst.setString(2, correoOrId);
            pst.setString(3, claveHash);
            rs = pst.executeQuery();

            while (rs.next()) {
                Map<String, Object> acceso = new HashMap<>();
                acceso.put("idPerfil", rs.getLong(1));
                acceso.put("perfil", rs.getString(2));
                acceso.put("cuenta", rs.getString(3));
                acceso.put("nombre", rs.getString(4));
                acceso.put("informacion", rs.getString(5));
                acceso.put("activo", rs.getBoolean(6));
                acceso.put("correo", rs.getString(7));
                resultado.add(acceso);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return resultado;
    }

}
