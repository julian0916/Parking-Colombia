package co.zer.repository;

import co.zer.model.Indice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public class SesionDAO implements ISesionDAO {

    public final static ZoneId ZONA_HORARIA = ZoneId.of("America/Bogota");

    public static ZoneId getZonaHoraria() {
        return ZONA_HORARIA;
    }

    public static LocalDateTime getFechaHoraActual() {
        LocalDateTime dateTime = LocalDateTime.now(getZonaHoraria());
        return dateTime;
    }

    @Override
    public void guardarSesion(Connection connection, Map<String, Object> accesoCuentaPerfil) throws Exception {
        final String SQL_INSERT = "INSERT INTO sesiones\n" +
                "(id, cuenta, perfil, fh_ingreso, fh_actividad)\n" +
                "VALUES(?, ?, ?, ?, ? );";
        PreparedStatement pst = null;
        Connection conn = connection;
        try {
            pst = conn.prepareStatement(SQL_INSERT);
            Indice indice = new Indice();
            pst.setObject(indice.siguiente(), accesoCuentaPerfil.get("sesion"));
            pst.setObject(indice.siguiente(), accesoCuentaPerfil.get("cuenta"));
            pst.setObject(indice.siguiente(), accesoCuentaPerfil.get("idPerfil"));
            pst.setTimestamp(indice.siguiente(), Timestamp.valueOf(getFechaHoraActual()));
            pst.setTimestamp(indice.siguiente(), Timestamp.valueOf(getFechaHoraActual()));
            pst.execute();
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
    }

    @Override
    public void retirarSesion(Connection connection, String id) throws Exception {
        final String SQL_DELETE = "DELETE FROM sesiones\n" +
                "WHERE id=? ;";
        PreparedStatement pst = null;
        Connection conn = connection;
        try {
            pst = conn.prepareStatement(SQL_DELETE);
            Indice indice = new Indice();
            pst.setObject(indice.siguiente(), id);
            pst.execute();
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
    }

    @Override
    public Map<String, Object> getAcceso(Connection connection, String id) throws Exception {
        Map<String, Object> res = new HashMap<>();
        final String SQL_SELECT = "SELECT id, cuenta, perfil, fh_ingreso, fh_actividad\n" +
                "FROM sesiones\n" +
                "WHERE id=?";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setObject(1, id);
            rs = pst.executeQuery();
            while (rs.next()) {
                Indice indice = new Indice();
                res.put("id", rs.getObject(indice.siguiente()));
                res.put("cuenta", rs.getObject(indice.siguiente()));
                res.put("perfil", rs.getObject(indice.siguiente()));
                res.put("fh_ingreso", rs.getTimestamp(indice.siguiente()).toLocalDateTime());
                res.put("fh_actividad", rs.getTimestamp(indice.siguiente()).toLocalDateTime());
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }

        return res;
    }

    @Override
    public void retirarSesionInactiva(Connection connection) throws Exception {
        final String SQL_DELETE = "DELETE FROM sesiones\n" +
                "WHERE fh_actividad < ? ;";
        PreparedStatement pst = null;
        Connection conn = connection;
        try {
            pst = conn.prepareStatement(SQL_DELETE);
            Indice indice = new Indice();
            pst.setTimestamp(indice.siguiente(), Timestamp.valueOf(getFechaHoraActual().minusHours(10)));
            pst.execute();
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
    }
}
