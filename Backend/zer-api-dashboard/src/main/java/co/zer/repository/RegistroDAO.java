package co.zer.repository;

import co.zer.model.Estados;
import co.zer.model.Indice;
import co.zer.utils.Utilidades;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroDAO implements IRegistroDAO {

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

    private Map<String, Double> getRecaudoPorVehiculoEstado(Connection connection, Long fnInicio, Long fnFin, List<Long> estados) throws Exception {
        Map<String, Double> res = new HashMap<>();
        final String SQL_SELECT = "SELECT SUM(r.valor_cobrado), r.es_carro \n" +
                "FROM registro r \n" +
                "WHERE\n" +
                "r.estado = any(?)\n" +
                "AND r.nf_recaudo <= ?\n" +
                "AND r.nf_recaudo >= ? \n" +
                "GROUP BY r.es_carro";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            Array array = conn.createArrayOf("int2", estados.toArray());
            pst.setArray(1, array);
            pst.setLong(2, fnFin);
            pst.setLong(3, fnInicio);
            rs = pst.executeQuery();
            res.put(Estados.TipoVehiculo.CARRO, 0.0);
            res.put(Estados.TipoVehiculo.MOTO, 0.0);
            while (rs.next()) {
                Indice indice = new Indice();
                Double valor = rs.getDouble(indice.siguiente());
                Boolean esCarro = rs.getBoolean(indice.siguiente());
                String clave = esCarro ? Estados.TipoVehiculo.CARRO : Estados.TipoVehiculo.MOTO;
                res.put(clave, valor);
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

    private Map<String, Double> getCarteraPorVehiculoEstado(Connection connection, Long fnInicio, Long fnFin, List<Long> estados) throws Exception {
        Map<String, Double> res = new HashMap<>();
        final String SQL_SELECT = "SELECT SUM(r.valor_cobrado), r.es_carro \n" +
                "FROM registro r \n" +
                "WHERE\n" +
                "r.estado = any(?)\n" +
                "AND r.nf_ingreso <= ?\n" +
                "AND r.nf_ingreso >= ? \n" +
                "GROUP BY r.es_carro";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            Array array = conn.createArrayOf("int2", estados.toArray());
            pst.setArray(1, array);
            pst.setLong(2, fnFin);
            pst.setLong(3, fnInicio);
            rs = pst.executeQuery();
            res.put(Estados.TipoVehiculo.CARRO, 0.0);
            res.put(Estados.TipoVehiculo.MOTO, 0.0);
            while (rs.next()) {
                Indice indice = new Indice();
                Double valor = rs.getDouble(indice.siguiente());
                Boolean esCarro = rs.getBoolean(indice.siguiente());
                String clave = esCarro ? Estados.TipoVehiculo.CARRO : Estados.TipoVehiculo.MOTO;
                res.put(clave, valor);
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
    public Map<String, Double> getRecaudoTotal(Connection connection, Long fnInicio, Long fnFin) throws Exception {
        List<Long> estados = new ArrayList<>();
        estados.add(Estados.Registro.PAGO_EXTEMPORANEO);
        estados.add(Estados.Registro.SALIO_PAGO);
        estados.add(Estados.Registro.PREPAGO);
        return getRecaudoPorVehiculoEstado(connection, fnInicio, fnFin, estados);
    }

    @Override
    public Map<String, Double> getRecaudoExtemporaneo(Connection connection, Long fnInicio, Long fnFin) throws Exception {
        List<Long> estados = new ArrayList<>();
        estados.add(Estados.Registro.PAGO_EXTEMPORANEO);
        return getRecaudoPorVehiculoEstado(connection, fnInicio, fnFin, estados);
    }

    @Override
    public Map<String, Double> getRecaudoPrepago(Connection connection, Long fnInicio, Long fnFin) throws Exception {
        List<Long> estados = new ArrayList<>();
        estados.add(Estados.Registro.PREPAGO);
        return getRecaudoPorVehiculoEstado(connection, fnInicio, fnFin, estados);
    }

    @Override
    public Map<String, Double> getRecaudoPostpago(Connection connection, Long fnInicio, Long fnFin) throws Exception {
        List<Long> estados = new ArrayList<>();
        estados.add(Estados.Registro.SALIO_PAGO);
        return getRecaudoPorVehiculoEstado(connection, fnInicio, fnFin, estados);
    }

    @Override
    public Map<String, Double> getCarteraTotal(Connection connection, Long fnInicio, Long fnFin) throws Exception {
        List<Long> estados = new ArrayList<>();
        estados.add(Estados.Registro.SALIO_REPORTADO);
        return getCarteraPorVehiculoEstado(connection, fnInicio, fnFin, estados);
    }

    @Override
    public Map<String, Double> getVentaPostpago(Connection connection, Long fnInicio, Long fnFin) throws Exception {
        List<Long> estados = new ArrayList<>();
        estados.add(Estados.Registro.SALIO_PAGO);
        estados.add(Estados.Registro.SALIO_REPORTADO);
        return getCarteraPorVehiculoEstado(connection, fnInicio, fnFin, estados);
    }

    @Override
    public Map<String, Map<String, Object>> getServiciosPeriodoGracia(Connection connection, Long fnInicio, Long fnFin) throws Exception {
        Map<String, Map<String, Object>> res = new HashMap<>();
        final String TOTAL = "TOTAL";
        final String GRATIS = "GRATIS";
        final String REPORTADO = "REPORTADO";
        final String PAGO = "PAGO";
        final String SQL_SELECT = "SELECT COUNT(1), r.es_carro, CASE WHEN r.estado=2 THEN '" + GRATIS + "' WHEN r.estado=3 THEN '" + REPORTADO + "' ELSE '" + PAGO + "' END tipo\n" +
                "FROM registro r \n" +
                "WHERE\n" +
                "r.estado > 0\n" +
                "AND r.nf_egreso <= ?\n" +
                "AND r.nf_egreso >= ? \n" +
                "GROUP BY r.es_carro,tipo \n" +
                "ORDER BY 2,3 ";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setLong(1, fnFin);
            pst.setLong(2, fnInicio);
            rs = pst.executeQuery();

            Map<String, Object> contenCarro = new HashMap<>();
            contenCarro.put(GRATIS, 0);
            contenCarro.put(REPORTADO, 0);
            contenCarro.put(PAGO, 0);
            contenCarro.put(TOTAL, 0.0);

            Map<String, Object> contenMoto = new HashMap<>();
            contenMoto.put(GRATIS, 0);
            contenMoto.put(REPORTADO, 0);
            contenMoto.put(PAGO, 0);
            contenMoto.put(TOTAL, 0.0);

            res.put(Estados.TipoVehiculo.CARRO, contenCarro);
            res.put(Estados.TipoVehiculo.MOTO, contenMoto);
            while (rs.next()) {
                Map<String, Object> local = new HashMap<>();
                Indice indice = new Indice();
                Double cantidad = rs.getDouble(indice.siguiente());
                Boolean esCarro = rs.getBoolean(indice.siguiente());
                String tipo = rs.getString(indice.siguiente());
                String clave = esCarro ? Estados.TipoVehiculo.CARRO : Estados.TipoVehiculo.MOTO;
                Double totalActual = (Double) res.get(clave).get(TOTAL) + cantidad;
                res.get(clave).put(TOTAL, totalActual);
                res.get(clave).put(tipo, cantidad);
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
    public List<Map<String, Object>> getOcupacionZona(Connection connection,
                                                      LocalDateTime lDTConsulta) throws Exception {
        Map<String, Map<String, Object>> res = new HashMap<>();
        List<Map<String, Object>> result = new ArrayList<>();
        final String SQL_SELECT = "SELECT COUNT(r.id) cantidad, \n" +
                "r.es_carro tipoVehiculo, \n" +
                "z.nombre zona, \n" +
                "z.celdas_carro CapacidadCarro,\n" +
                "z.celdas_moto CapacidadMoto\n" +
                "FROM registro r\n" +
                "LEFT JOIN zona z ON z.id = r.zona\n" +
                "WHERE\n" +
                "(r.estado = 0 or (r.fh_egreso >= ? and r.cupo_disponible = false))\n" +
                "AND r.fh_ingreso <= ?\n" +
                "AND r.nf_ingreso = ?\n" +
                "GROUP BY r.es_carro, z.nombre, \n" +
                "CapacidadCarro, CapacidadMoto\n" +
                "ORDER BY zona, tipoVehiculo;";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setTimestamp(1, Timestamp.valueOf(lDTConsulta));
            pst.setTimestamp(2, Timestamp.valueOf(lDTConsulta));
            pst.setLong(3, Utilidades.getFechaNumero(lDTConsulta.toLocalDate()));
            rs = pst.executeQuery();
            while (rs.next()) {
                Indice indice = new Indice();
                long cantidad = rs.getLong(indice.siguiente());
                Boolean esCarro = rs.getBoolean(indice.siguiente());
                String clave = esCarro ? Estados.TipoVehiculo.CARRO : Estados.TipoVehiculo.MOTO;
                String zona = rs.getString(indice.siguiente());
                Long capacidadCarro = rs.getLong(indice.siguiente());
                Long capacidadMoto = rs.getLong(indice.siguiente());
                Map<String, Object> actual = res.get(zona);
                if (actual == null) {
                    actual = new HashMap<>();
                }
                actual.put(clave, cantidad);
                actual.put("CapacidadCarro", capacidadCarro);
                actual.put("CapacidadMoto", capacidadMoto);
                actual.put("zona", zona);
                res.put(zona, actual);
            }
            res.entrySet().stream().forEach((contLocal) -> {
                result.add(contLocal.getValue());
            });
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

}
