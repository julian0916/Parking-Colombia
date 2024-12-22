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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SupervisionDAO implements ISupervisionDAO {

    /**
     * Coloca en tipo título un texto
     *
     * @param contenido
     * @return
     */
    private String capitalizar(String contenido) {
        if (contenido == null)
            return "";
        contenido = corregirEspacios(contenido);
        if (contenido.length() < 2)
            return contenido.toUpperCase();
        return contenido.substring(0, 1).toUpperCase() + contenido.substring(1).toLowerCase();
    }

    /**
     * Elimina todos los espacio en blanco
     *
     * @param contenido
     * @return
     */
    private String quitarEspacios(String contenido) {
        if (contenido == null)
            return "";
        return contenido.replaceAll("\\s", "");
    }

    /**
     * Deja un solo espacio en blanco
     *
     * @param contenido
     * @return
     */
    private String corregirEspacios(String contenido) {
        if (contenido == null)
            return "";
        return contenido.trim().replaceAll("[\\s]{2,1000}", " ");
    }

    /**
     * Valida la expresión del correo
     * para que sea lógica con la estructura
     * caracteres@caracteres usando .-_ en el
     * nombre y el dominio
     *
     * @param contenido
     * @return
     */
    private boolean validarCorreo(String contenido) {
        Pattern pat = Pattern.compile("[\\w.-_]+@[\\w.-_]+.[\\w-_]+");
        Matcher mat = pat.matcher(contenido);
        return mat.matches();
    }

    @Override
    public List<Map<String, Object>> reporte(
            Connection connection,
            Long nFechaInicial,
            Long nFechaFinal,
            Long promotor,
            Paginacion paginacion) throws Exception {
        List<Map<String, Object>> resul = new ArrayList<>();
        PreparedStatement pst = null;
        Connection conn = connection;
        try {
            final String SQL_COUNT = "SELECT count(*)\n" +
                    "FROM  encabezado_supervision es1\n" +
                    "WHERE \n" +
                    "es1.promotor = CASE WHEN 0=? THEN es1.promotor ELSE ? END\n" +
                    "AND es1.nf_registro BETWEEN ? AND ?";
            ResultSet rs;
            paginacion.setTotal(0L);
            pst = conn.prepareStatement(SQL_COUNT);
            pst.setLong(1, promotor);
            pst.setLong(2, promotor);
            pst.setLong(3, nFechaInicial);
            pst.setLong(4, nFechaFinal);
            rs = pst.executeQuery();
            while (rs.next()) {
                paginacion.setTotal(rs.getLong(1));
            }
            String SQL_SELECT = "SELECT \n" +
                    "es.id id,\n" +
                    "es.fh_registro fecha_hora,\n" +
                    "z1.nombre zona,\n" +
                    "u2.identificacion promotor_documento, \n" +
                    "u2.nombre1||' '||u2.nombre2 ||' '||u2.apellido1 ||' '||u2.apellido2 promotor_nombre,\n" +
                    "u3.identificacion supervisor_documento,\n" +
                    "u3.nombre1||' '||u3.nombre2 ||' '||u3.apellido1 ||' '||u3.apellido2 supervisor_nombre,\n" +
                    "es.firmado es_firmado,\n" +
                    "es.informacion informacion, \n" +
                    "ds.pregunta contenido,\n" +
                    "ds.valor valor,\n" +
                    "ds.cumple cumple\n" +
                    "FROM \n" +
                    "encabezado_supervision es \n" +
                    "LEFT JOIN detalle_supervision ds ON ds.id_supervision = es.id \n" +
                    "LEFT JOIN usuario u2 ON u2.id = es.promotor\n" +
                    "LEFT JOIN usuario u3 ON u3.id = es.supervisor\n" +
                    "LEFT JOIN zona z1 ON z1.id = es.zona \n" +
                    "WHERE\n" +
                    "es.id in \n" +
                    "(SELECT es1.id\n" +
                    "FROM encabezado_supervision es1\n" +
                    "WHERE \n" +
                    "es1.promotor = CASE WHEN 0=? THEN es1.promotor ELSE ? END\n" +
                    "AND es1.nf_registro BETWEEN ? AND ?\n" +
                    "LIMIT ?\n" +
                    "OFFSET ?)\n" +
                    "ORDER BY 2,4,3,10";
            if (paginacion.getTraerTodo()) {
                paginacion.setActual(1l);
                paginacion.setLimite(paginacion.getTotal() + 1);
            }
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setLong(1, promotor);
            pst.setLong(2, promotor);
            pst.setLong(3, nFechaInicial);
            pst.setLong(4, nFechaFinal);
            pst.setLong(5, paginacion.getLimite());
            pst.setLong(6, Math.max((paginacion.getActual() - 1) * paginacion.getLimite(), 0));
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
