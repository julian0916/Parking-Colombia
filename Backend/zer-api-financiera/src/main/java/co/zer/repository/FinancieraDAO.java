package co.zer.repository;

import co.zer.model.Indice;
import co.zer.model.PagoMunicipio;
import co.zer.model.Recaudo;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FinancieraDAO implements IFinancieraDAO {

    private static String FECHA = "FECHA";
    private static String FECHA_PROMOTOR = "FECHA_PROMOTOR";


    /**
     * Completa con ceros a la izquierda
     *
     * @param valor
     * @param dig
     * @return
     */
    private static String completarCeroIzquierda(int valor, int dig) {
        String val = String.valueOf(valor).trim();
        while (val.length() < dig) {
            val = "0" + val;
        }
        return val;
    }

    /**
     * retorna el valor numerico YYYYMMDD
     *
     * @param date
     * @return
     */
    private static Long getFechaActualNumero(LocalDate date) {
        String fechaString = completarCeroIzquierda(date.getYear(), 4);
        fechaString += completarCeroIzquierda(date.getMonthValue(), 2);
        fechaString += completarCeroIzquierda(date.getDayOfMonth(), 2);
        return Long.parseLong(fechaString, 10);
    }

    /**
     * retorna un valor de tipo Timestamp dado el
     * LocalDateTime controlando para cuando este es null
     *
     * @param localDateTime
     * @return
     */
    private Timestamp getFHValue(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Timestamp.valueOf(localDateTime);
    }

    private Time getHValue(LocalTime localTime) {
        if (localTime == null) {
            return null;
        }
        return Time.valueOf(localTime);
    }

    /**
     * recuperar datos de Tiemstmp to LocalDateTime
     *
     * @param tiempo
     * @return
     */
    private LocalDateTime getLDT(Timestamp tiempo) {
        if (tiempo == null) {
            return null;
        }
        return tiempo.toLocalDateTime();
    }

    /**
     * De time hacia LocalTime
     *
     * @param tiempo
     * @return
     */
    private LocalTime getLT(Time tiempo) {
        if (tiempo == null) {
            return null;
        }
        return tiempo.toLocalTime();
    }

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
        return contenido.trim().replaceAll("[\\s]{2,}", " ");
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

    private String getSQL(String tipo) {
        String sql = "SELECT \n" +
                "SUM(COALESCE(r.valor_cobrado,0)) recaudado,\n" +
                "COALESCE(r2.cantidad_reportada,0) reportado,\n" +
                "COALESCE(ec.saldo,0) saldo_cuenta,\n" +
                "COALESCE(r2.id,0) id_recaudo, \n" +
                "COALESCE(r2.cerrada,false) cerrada, \n" +
                "COALESCE(r2.actualizo_cuenta,false) actualizo_cuenta, \n" +
                "u.id, " +
                "u.nombre1||' '||u.nombre2||' '||u.apellido1 ||' '||u.apellido2 as nombre, \n" +
                "u.identificacion, \n" +
                "CASE WHEN COALESCE(ec.promotor,0)<1 then false else true end tiene_estado_cuenta, \n" +
                "CASE WHEN COALESCE(r2.actualizo_cuenta,false)=false then 0 else r2.saldo end ,\n" +
                "COALESCE(r2.valor_abono,0) valor_abono\n" +
                "FROM usuario u \n" +
                "LEFT JOIN registro r on r.promotor_recauda = u.id AND r.nf_recaudo = ?\n" +
                "LEFT JOIN recaudo r2 on r2.promotor = u.id AND r2.nf_recaudo = ?\n" +
                "LEFT JOIN estado_cuenta ec on ec.promotor = u.id\n" +
                "WHERE\n" +
                "(u.activo = TRUE OR r.promotor_recauda = u.id OR r2.promotor = u.id) \n";

        if (tipo.equals(FECHA)) {
            sql += "AND u.perfil in(102,105)\n";
        }
        if (tipo.equals(FECHA_PROMOTOR)) {
            sql += "AND u.id = ?\n";
        }
        sql += "GROUP BY 2,3,4,5,6,7,8,9,10,11,12\n" +
                "ORDER BY 8";
        return sql;
    }

    private List<Map<String, Object>> getContenido(ResultSet rs) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> nuevo = new HashMap<>();
            Indice indice = new Indice();
            Long recaudado = rs.getLong(indice.siguiente());
            nuevo.put("recaudado", recaudado);
            Long reportado = rs.getLong(indice.siguiente());
            nuevo.put("reportado", reportado);
            nuevo.put("reportadoFormato", reportado);

            nuevo.put("saldo_cuenta", rs.getLong(indice.siguiente()));
            nuevo.put("id_recaudo", rs.getLong(indice.siguiente()));
            nuevo.put("cerrada", rs.getObject(indice.siguiente()));
            nuevo.put("actualizo_cuenta", rs.getObject(indice.siguiente()));
            nuevo.put("idPromotor", rs.getObject(indice.siguiente()));
            nuevo.put("nombrePromotor", rs.getObject(indice.siguiente()));
            nuevo.put("identificacionPromotor", rs.getObject(indice.siguiente()));
            nuevo.put("mostrar", true);
            nuevo.put("tieneEstadoCuenta", rs.getObject(indice.siguiente()));
            nuevo.put("saldo", rs.getObject(indice.siguiente()));
            Long valorAbono = rs.getLong(indice.siguiente());
            nuevo.put("valorAbono", valorAbono);
            nuevo.put("valorAbonoFormato", valorAbono);
            nuevo.put("diferencia", 0);
            result.add(nuevo);
        }
        return result;
    }

    @Override
    public PagoMunicipio getPagoMunicipio(Connection connection, Long fnPago) throws Exception {
        PagoMunicipio result = new PagoMunicipio();
        final String SQL_SELECT = "SELECT \n" +
                "id," +
                "nf_pago, \n" +
                "informacion, \n" +
                "valor_consignado, \n" +
                "codigo_factura, \n" +
                "cerrado\n" +
                "FROM pago_municipio\n" +
                "WHERE nf_pago = ?;";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setLong(1, fnPago);
            rs = pst.executeQuery();
            while (rs.next()) {
                Indice indice = new Indice();
                result.setId(rs.getLong(indice.siguiente()));
                result.setNfPago(rs.getLong(indice.siguiente()));
                result.setInformacion(rs.getString(indice.siguiente()));
                result.setValorConsignado(rs.getLong(indice.siguiente()));
                result.setCodigoFactura(rs.getString(indice.siguiente()));
                result.setCerrado(rs.getBoolean(indice.siguiente()));
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return result;
    }


    @Override
    public PagoMunicipio guardarPagoMunicipio(Connection connection,PagoMunicipio pagoMunicipio) throws Exception {
        if(pagoMunicipio==null){
            throw new Exception("Los datos suministrados están incompletos");
        }
        if (pagoMunicipio.getId() == null || pagoMunicipio.getId() < 0) {
            return insertarPagoMunicipio(connection,pagoMunicipio);
        }
        return actualizarPagoMunicipio(connection,pagoMunicipio);
    }

    private PagoMunicipio insertarPagoMunicipio(Connection connection,
                                                      PagoMunicipio pagoMunicipio) throws Exception {
        final String SQL_INSERT = "INSERT INTO pago_municipio\n" +
                "(nf_pago, informacion, valor_consignado, codigo_factura, cerrado)\n" +
                "VALUES(?, ?, ?, ?, ? ) RETURNING id;";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_INSERT);
            Indice indice = new Indice();
            pst.setObject(indice.siguiente(), pagoMunicipio.getNfPago());
            pst.setObject(indice.siguiente(), pagoMunicipio.getInformacion());
            pst.setObject(indice.siguiente(), pagoMunicipio.getValorConsignado());
            pst.setObject(indice.siguiente(), pagoMunicipio.getCodigoFactura());
            pst.setObject(indice.siguiente(), pagoMunicipio.isCerrado());
            rs = pst.executeQuery();
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return getPagoMunicipio(connection, pagoMunicipio.getNfPago());
    }

    private PagoMunicipio actualizarPagoMunicipio(Connection connection,
                                                        PagoMunicipio pagoMunicipio) throws Exception {
        final String SQL_INSERT = "UPDATE pago_municipio\n" +
                "SET \n" +
                "informacion = ? , \n" +
                "valor_consignado = ? , \n" +
                "codigo_factura = ? , \n" +
                "cerrado = ?\n" +
                "WHERE cerrado = false\n" +
                "AND id = ?;";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_INSERT);
            Indice indice = new Indice();
            pst.setObject(indice.siguiente(), pagoMunicipio.getInformacion());
            pst.setObject(indice.siguiente(), pagoMunicipio.getValorConsignado());
            pst.setObject(indice.siguiente(), pagoMunicipio.getCodigoFactura());
            pst.setObject(indice.siguiente(), pagoMunicipio.isCerrado());
            pst.setObject(indice.siguiente(), pagoMunicipio.getId());
            pst.executeUpdate();
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return getPagoMunicipio(connection, pagoMunicipio.getNfPago());
    }

    @Override
    public List<Map<String, Object>> getListadoRecaudoReportadoFecha(Connection connection, Long fnRecaudo) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        final String SQL_SELECT = getSQL(FECHA);
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setLong(1, fnRecaudo);
            pst.setLong(2, fnRecaudo);
            rs = pst.executeQuery();
            result = getContenido(rs);
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return result;
    }

    public Map<String, Object> getRecaudoReportadoFechaPromotor(
            Connection connection,
            Long fnRecaudo,
            Long promotor) throws Exception {
        Map<String, Object> nuevo = new HashMap<>();
        final String SQL_SELECT = getSQL(FECHA_PROMOTOR);
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setLong(1, fnRecaudo);
            pst.setLong(2, fnRecaudo);
            pst.setLong(3, promotor);
            rs = pst.executeQuery();
            List<Map<String, Object>> result = getContenido(rs);
            if (result.size() > 0) {
                return result.get(0);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return nuevo;
    }

    private Map<String, Object> insertarRecaudo(Connection connection, Recaudo recaudo) throws Exception {
        final String SQL_INSERT = "INSERT INTO recaudo\n" +
                "(promotor, nf_recaudo, cantidad_recaudada, cantidad_reportada, \n" +
                "saldo, diferencia, nota, cerrada, actualizo_cuenta, valor_abono)\n" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ? , ? ) RETURNING id;";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_INSERT);
            Indice indice = new Indice();
            pst.setObject(indice.siguiente(), recaudo.getPromotor());
            pst.setObject(indice.siguiente(), recaudo.getNfRecaudo());
            pst.setObject(indice.siguiente(), recaudo.getCantidadRecaudada());
            pst.setObject(indice.siguiente(), recaudo.getCantidadReportada());
            pst.setObject(indice.siguiente(), recaudo.getSaldo());
            pst.setObject(indice.siguiente(), recaudo.getDiferencia());
            pst.setObject(indice.siguiente(), recaudo.getNota());
            pst.setObject(indice.siguiente(), recaudo.isCerrada());
            pst.setObject(indice.siguiente(), recaudo.isActualizadoSaldo());
            pst.setObject(indice.siguiente(), recaudo.getValorAbono());
            rs = pst.executeQuery();
            while (rs.next()) {
                recaudo.setId(rs.getLong(1));
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return getRecaudoReportadoFechaPromotor(connection, recaudo.getNfRecaudo(), recaudo.getPromotor());
    }

    public Map<String, Object> actualizarRecaudo(Connection connection, Recaudo recaudo) throws Exception {
        final String SQL_UPDATE = "UPDATE recaudo \n" +
                "SET promotor=?, nf_recaudo=?, " +
                "cantidad_recaudada=?, cantidad_reportada=?, \n" +
                "saldo=?, diferencia=?, \n" +
                "nota=?, cerrada=?, actualizo_cuenta=? , valor_abono=? \n" +
                "WHERE \n" +
                "cerrada = false \n" +
                "AND id=?;\n";
        PreparedStatement pst = null;
        Connection conn = connection;
        try {
            pst = conn.prepareStatement(SQL_UPDATE);
            Indice indice = new Indice();
            pst.setObject(indice.siguiente(), recaudo.getPromotor());
            pst.setObject(indice.siguiente(), recaudo.getNfRecaudo());
            pst.setObject(indice.siguiente(), recaudo.getCantidadRecaudada());
            pst.setObject(indice.siguiente(), recaudo.getCantidadReportada());
            pst.setObject(indice.siguiente(), recaudo.getSaldo());
            pst.setObject(indice.siguiente(), recaudo.getDiferencia());

            pst.setObject(indice.siguiente(), recaudo.getNota());
            pst.setObject(indice.siguiente(), recaudo.isCerrada());
            pst.setObject(indice.siguiente(), recaudo.isActualizadoSaldo());
            pst.setObject(indice.siguiente(), recaudo.getValorAbono());
            pst.setObject(indice.siguiente(), recaudo.getId());
            long result = pst.executeUpdate();
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return getRecaudoReportadoFechaPromotor(connection, recaudo.getNfRecaudo(), recaudo.getPromotor());
    }

    @Override
    public Map<String, Object> guardarRecaudo(Connection connection, Recaudo recaudo) throws Exception {
        if (recaudo == null) {
            throw new Exception("Datos incorrectos");
        }
        if (recaudo.getId() == null) {
            return insertarRecaudo(connection, recaudo);
        }
        if (recaudo.getId() < 1) {
            return insertarRecaudo(connection, recaudo);
        }
        return actualizarRecaudo(connection, recaudo);
    }


    @Override
    public Map<String, Object> guardarEstadoCuenta(Connection connection,
                                                   Long promotor,
                                                   Long nuevoValor,
                                                   boolean actualizar,
                                                   Long fnRecaudo) throws Exception {
        if (actualizar) {
            actualizarEstadoCuenta(connection, promotor, nuevoValor);
        } else {
            insertarEstadoCuenta(connection, promotor, nuevoValor);
        }
        return getRecaudoReportadoFechaPromotor(connection, fnRecaudo, promotor);

    }

    public long recaudoActualizadoEnEstadoCuenta(Connection connection,
                                                 Long idPromotor,
                                                 Long saldo) throws Exception {
        long result = 0;
        final String SQL_UPDATE = "UPDATE recaudo r1\n" +
                "SET actualizo_cuenta = TRUE, \n" +
                "saldo = ?  \n" +
                "WHERE \n" +
                "r1.cerrada = TRUE\n" +
                "AND r1.id = ?; \n";
        PreparedStatement pst = null;
        Connection conn = connection;
        try {
            pst = conn.prepareStatement(SQL_UPDATE);
            Indice indice = new Indice();
            pst.setObject(indice.siguiente(), saldo);
            pst.setObject(indice.siguiente(), idPromotor);
            result = pst.executeUpdate();
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return result;
    }

    @Override
    public long recaudoActualizarSaldosPosteriores(Connection connection,
                                                   Long idPromotor,
                                                   Long nfRecaudo) throws Exception {
        long result = 0;
        final String SQL_UPDATE = "UPDATE recaudo r1\n" +
                "SET saldo = \n" +
                "\tCOALESCE((SELECT SUM(CASE WHEN (r.cantidad_reportada - r.cantidad_recaudada)<0 then (r.cantidad_reportada - r.cantidad_recaudada) ELSE 0 END +r.valor_abono)\n" +
                "\tFROM recaudo r\n" +
                "\tWHERE\n" +
                "\tr.cerrada = TRUE \n" +
                "\tAND r.nf_recaudo <= r1.nf_recaudo\n" +
                "\tAND r.promotor = ? ),0) \n" +
                "WHERE \n" +
                "r1.nf_recaudo >= \n? " +
                "AND r1.promotor = ?\n" +
                "AND EXISTS(\n" +
                "\t\tSELECT 1 \n" +
                "\t\tFROM recaudo r2\n" +
                "\t\tWHERE\n" +
                "\t\tr2.cerrada = TRUE\n" +
                "\t\tAND r2.nf_recaudo > ?\n" +
                "\t\tAND r2.promotor = ? \n" +
                "\t\t) \n" +
                "\t;";
        PreparedStatement pst = null;
        Connection conn = connection;
        try {
            pst = conn.prepareStatement(SQL_UPDATE);
            Indice indice = new Indice();
            pst.setObject(indice.siguiente(), idPromotor);
            pst.setObject(indice.siguiente(), nfRecaudo);
            pst.setObject(indice.siguiente(), idPromotor);
            pst.setObject(indice.siguiente(), nfRecaudo);
            pst.setObject(indice.siguiente(), idPromotor);
            result = pst.executeUpdate();
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return result;
    }

    private void insertarEstadoCuenta(Connection connection, Long promotor, Long nuevoValor) throws Exception {
        final String SQL_INSERT = "INSERT INTO estado_cuenta\n" +
                "(promotor, saldo, nota)\n" +
                "VALUES(?, ?, ? );";
        PreparedStatement pst = null;
        Connection conn = connection;
        try {
            pst = conn.prepareStatement(SQL_INSERT);
            Indice indice = new Indice();
            pst.setObject(indice.siguiente(), promotor);
            pst.setObject(indice.siguiente(), nuevoValor);
            pst.setObject(indice.siguiente(), "");
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

    private void actualizarEstadoCuenta(Connection connection,
                                        Long promotor,
                                        Long nuevoValor) throws Exception {
        final String SQL_UPDATE = "UPDATE estado_cuenta\n" +
                "SET saldo= (saldo + ?), nota=?\n" +
                "WHERE promotor=?;\n";
        PreparedStatement pst = null;
        Connection conn = connection;
        try {
            pst = conn.prepareStatement(SQL_UPDATE);
            Indice indice = new Indice();
            pst.setObject(indice.siguiente(), nuevoValor);
            pst.setObject(indice.siguiente(), "");
            pst.setObject(indice.siguiente(), promotor);
            pst.executeUpdate();
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
