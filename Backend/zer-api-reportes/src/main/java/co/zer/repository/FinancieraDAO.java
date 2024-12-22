package co.zer.repository;

import co.zer.model.Paginacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinancieraDAO implements IFinancieraDAO {


    @Override
    public List<Map<String, Object>> reporte(
            Connection connection,
            Boolean estado,
            Long promotor,
            Paginacion paginacion) throws Exception {

        List<Map<String, Object>> resul = new ArrayList<>();
        PreparedStatement pst = null;
        Connection conn = connection;
        try {
            final String SQL_FROM_WHERE = "    FROM estado_cuenta ec\n" +
                    "    INNER JOIN usuario u2 ON u2.activo = ? AND u2.id = ec.promotor\n" +
                    "    LEFT JOIN cuentas.tipo_identificacion ti ON ti.id = u2.tipo_identificacion\n" +
                    "    WHERE ec.promotor  = CASE WHEN 0=? THEN ec.promotor ELSE ? END \n";

            final String SQL_COUNT = "SELECT count(*)\n" +
                    SQL_FROM_WHERE;

            ResultSet rs;
            paginacion.setTotal(0L);
            pst = conn.prepareStatement(SQL_COUNT);
            pst.setBoolean(1, estado);
            pst.setLong(2, promotor);
            pst.setLong(3, promotor);
            rs = pst.executeQuery();
            while (rs.next()) {
                paginacion.setTotal(rs.getLong(1));
            }
            String SQL_SELECT = "SELECT \n" +
                    "    ti.nombre_corto tipo_documento,\n" +
                    "    u2.identificacion numero_documento,\n" +
                    "    u2.correo correo,\n" +
                    "    u2.celular celular,\n" +
                    "    u2.fijo fijo,\n" +
                    "    u2.nombre1||' '||u2.nombre2 ||' '||u2.apellido1 ||' '||u2.apellido2 promotor_nombre,\n" +
                    "    ec.saldo saldo,\n" +
                    "    ec.nota nota \n" +
                    SQL_FROM_WHERE +
                    "    ORDER BY 6 \n"+
                    "    LIMIT ? \n" +
                    "    OFFSET ? \n";
            if (paginacion.getTraerTodo()) {
                paginacion.setActual(1l);
                paginacion.setLimite(paginacion.getTotal() + 1);
            }
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setBoolean(1, estado);
            pst.setLong(2, promotor);
            pst.setLong(3, promotor);
            pst.setLong(4, paginacion.getLimite());
           pst.setLong(5, Math.max((paginacion.getActual() - 1) * paginacion.getLimite(), 0));
            rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, Object> conte = new HashMap<>();
                ResultSetMetaData dataRS = rs.getMetaData();
                for (int col = 1; col <= rs.getMetaData().getColumnCount(); col++) {
                    conte.put(dataRS.getColumnName(col), rs.getObject(col));
                }
                resul.add(conte);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return resul;
    }
}
