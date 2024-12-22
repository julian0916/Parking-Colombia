package co.zer.repository;

import co.zer.model.PromotorDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PromotorDAO implements IPromotorDAO {
    @Override
    public Boolean superaLimiteEndeudamiento(Connection connection, Long idPromotor) throws Exception {
        Boolean res = false;
        final String SQL_SELECT = "select distinct true\n" +
                "from estado_cuenta ec ,\n" +
                "configuracion c\n" +
                "where\n" +
                "ec.saldo <= -abs(c.limite_endeudamiento_promotor) \n" +
                "and c.id = 101\n" +
                "and ec.promotor = ?";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setLong(1, idPromotor);
            rs = pst.executeQuery();
            while (rs.next()) {
                res = rs.getBoolean(1);
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
    public Long obtenerTotalRecaudado(Connection connection, Long idPromotor) throws Exception {
        Long res = 0L;
        LocalDateTime datetime = LocalDateTime.now();
        int year = datetime.getYear();
        int month = datetime.getMonthValue();
        int day = datetime.getDayOfMonth();
        int nfRecaudo = year * 10000 + month * 100 + day;
        final String SQL_SELECT = "SELECT SUM(valor_cobrado) AS total_recaudado FROM registro WHERE nf_recaudo = ? AND promotor_recauda = ?;";
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = connection.prepareStatement(SQL_SELECT);
            pst.setLong(1, nfRecaudo);
            pst.setLong(2, idPromotor);
            rs = pst.executeQuery();
            if (rs.next()) {
                res = rs.getLong("total_recaudado");
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ex) {
                    // Log the exception
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (Exception ex) {
                    // Log the exception
                }
            }
        }
        return res;
    }

    @Override
    public Boolean estaBloqueado(Connection connection, Long promotor) throws Exception {
        boolean bloqueado = false;
        final String SQL_SELECT = "SELECT bloqueo, hora_bloqueo FROM bloqueo_promotor WHERE promotor = ? ORDER BY hora_bloqueo DESC LIMIT 1";
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = connection.prepareStatement(SQL_SELECT);
            pst.setLong(1, promotor);
            rs = pst.executeQuery();
            if (rs.next()) {
                boolean ultimoBloqueo = rs.getBoolean("bloqueo");
                Timestamp fechaBloqueo = rs.getTimestamp("hora_bloqueo");

                // Obtener la fecha actual
                LocalDate fechaActual = LocalDate.now();
                LocalDate fechaUltimoBloqueo = fechaBloqueo.toLocalDateTime().toLocalDate();

                bloqueado = ultimoBloqueo;

                // Comparar las fechas
                if (!fechaUltimoBloqueo.isEqual(fechaActual)) {
                    bloqueado = false;
                }
            }
        } catch (Exception ex) {
            throw new Exception("Error al verificar si el promotor está bloqueado.", ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ex) {
                    // Log the exception
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (Exception ex) {
                    // Log the exception
                }
            }
        }
        return bloqueado;
    }

    @Override
    public void bloquearPromotor(Connection connection, Long promotor) throws Exception {
        final String SQL_SELECT = "SELECT COALESCE(MAX(contador_bloqueo), 0) FROM bloqueo_promotor WHERE promotor = ? AND DATE(hora_bloqueo) = CURRENT_DATE";
        final String SQL_INSERT = "INSERT INTO bloqueo_promotor (promotor, bloqueo, hora_bloqueo, contador_bloqueo) " +
                "VALUES (?, true, ?, ?)";

        try (PreparedStatement selectPst = connection.prepareStatement(SQL_SELECT);
             PreparedStatement insertPst = connection.prepareStatement(SQL_INSERT)) {

            // Obtener el mayor contador de bloqueo para el día actual
            selectPst.setLong(1, promotor);
            int contadorBloqueo = 1; // Comenzar el contador desde 1 por defecto
            try (ResultSet rs = selectPst.executeQuery()) {
                if (rs.next()) {
                    int maxContadorBloqueo = rs.getInt(1);
                    if (maxContadorBloqueo > 0) {
                        contadorBloqueo = maxContadorBloqueo + 1;
                    }
                }
            }

            // Insertar el nuevo bloqueo con el contador correspondiente
            insertPst.setLong(1, promotor);
            insertPst.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            insertPst.setInt(3, contadorBloqueo);
            insertPst.executeUpdate();

        } catch (Exception ex) {
            throw new Exception("Error al bloquear el promotor.", ex);
        }
    }

    @Override
    public void desbloquearPromotor(Connection connection, Long promotor, Long supervisor) throws Exception {
        final String SQL_UPDATE = "UPDATE bloqueo_promotor SET bloqueo = false, hora_desbloqueo = ?, supervisor = ? WHERE promotor = ?";
        try (PreparedStatement pst = connection.prepareStatement(SQL_UPDATE)) {
            pst.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            pst.setLong(2, supervisor);
            pst.setLong(3, promotor);
            pst.executeUpdate();
        } catch (Exception ex) {
            throw new Exception("Error al desbloquear el promotor.", ex);
        }
    }

    @Override
    public List<PromotorDTO> listarBloqueados(Connection connection) throws Exception {

        final String SQL_SELECT = "SELECT u.id, u.nombre1 AS nombre, u.apellido1 AS apellido, bp.hora_bloqueo " +
        "FROM ( SELECT *, ROW_NUMBER() OVER (PARTITION BY bp.promotor ORDER BY bp.hora_bloqueo DESC) AS rn " +
                "FROM bloqueo_promotor AS bp WHERE bp.bloqueo = true "+
        ") AS bp INNER JOIN usuario AS u ON bp.promotor = u.id WHERE bp.rn = 1 "+
        "ORDER BY bp.hora_bloqueo DESC";

        List<PromotorDTO> listaBloqueados = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement(SQL_SELECT);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Long id = rs.getLong("id");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                Timestamp horaBloqueo = rs.getTimestamp("hora_bloqueo");

                PromotorDTO promotor = new PromotorDTO(id, nombre, apellido, horaBloqueo);
                listaBloqueados.add(promotor);
            }
        } catch (Exception ex) {
            throw new Exception("Error al listar los promotores bloqueados.", ex);
        }
        return listaBloqueados;
    }
}
