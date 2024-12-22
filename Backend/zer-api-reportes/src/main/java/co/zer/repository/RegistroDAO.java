package co.zer.repository;

import co.zer.model.Paginacion;
import co.zer.utils.Utilidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistroDAO implements IRegistroDAO {

    @Override
    public List<Map<String, Object>> reporte(Connection connection,
                                             Long nfYear,
                                             Boolean enero,
                                             Boolean febrero,
                                             Boolean marzo,
                                             Boolean abril,
                                             Boolean mayo,
                                             Boolean junio,
                                             Boolean julio,
                                             Boolean agosto,
                                             Boolean septiembre,
                                             Boolean octubre,
                                             Boolean noviembre,
                                             Boolean diciembre) throws Exception {

        List<Map<String, Object>> resul = new ArrayList<>();
        PreparedStatement pst = null;
        Connection conn = connection;
        try {
            String SQL_SELECT = "select \n" +
                    "mes,\n" +
                    "case mes \n" +
                    "when 1 then 'enero'\n" +
                    "when 2 then 'febrero'\n" +
                    "when 3 then 'marzo'\n" +
                    "when 4 then 'abril'\n" +
                    "when 5 then 'mayo'\n" +
                    "when 6 then 'junio'\n" +
                    "when 7 then 'julio'\n" +
                    "when 8 then 'agosto'\n" +
                    "when 9 then 'septiembre'\n" +
                    "when 10 then 'octubre'\n" +
                    "when 11 then 'noviembre'\n" +
                    "else 'diciembre'\n" +
                    "end nombre_mes,\n" +
                    "total_estacionammientos,\n" +
                    "estacionamiento_carros,\n" +
                    "estacionamiento_motos,\n" +
                    "case when total_estacionammientos>0 then trunc(estacionamiento_carros::decimal/total_estacionammientos*100,2) else 0 end porce_estacionamiento_carros,\n" +
                    "case when total_estacionammientos>0 then trunc(estacionamiento_motos::decimal/total_estacionammientos*100,2) else 0 end porce_estacionamiento_motos,\n" +
                    "total_estacionammientos_sin_cobro,\n" +
                    "estacionamiento_carros_sin_cobro,\n" +
                    "estacionamiento_motos_sin_cobro,\n" +
                    "case when total_estacionammientos>0 then trunc(estacionamiento_carros_sin_cobro::decimal/total_estacionammientos*100,2) else 0 end porce_estacionamiento_carros_sin_cobro,\n" +
                    "case when total_estacionammientos>0 then trunc(estacionamiento_motos_sin_cobro::decimal/total_estacionammientos*100,2) else 0 end porce_estacionamiento_motos_sin_cobro,\n" +
                    "total_estacionammientos_con_cobro,\n" +
                    "estacionamiento_carros_con_cobro,\n" +
                    "estacionamiento_motos_con_cobro,\n" +
                    "case when total_estacionammientos>0 then trunc(estacionamiento_carros_con_cobro::decimal/total_estacionammientos*100,2) else 0 end porce_estacionamiento_carros_con_cobro,\n" +
                    "case when total_estacionammientos>0 then trunc(estacionamiento_motos_con_cobro::decimal/total_estacionammientos*100,2) else 0 end porce_estacionamiento_motos_con_cobro,\n" +
                    "cartera_total,\n" +
                    "cartera_carros,\n" +
                    "cartera_motos,\n" +
                    "recaudo_total,\n" +
                    "recaudo_carros,\n" +
                    "recaudo_motos,\n" +
                    "cobro_total,\n" +
                    "cobro_carros,\n" +
                    "cobro_motos,\n" +
                    "case when cobro_total>0 then trunc(cartera_total::decimal/cobro_total*100,2) else 0 end porce_cartera_total,\n" +
                    "case when cobro_total>0 then trunc(recaudo_total::decimal/cobro_total*100,2) else 0 end porce_recaudo_total\n" +
                    "from \n" +
                    "(\n" +
                    "select distinct\n" +
                    "r1.numero_mes mes,\n" +
                    "count(*) over(partition by r1.numero_mes ) total_estacionammientos,\n" +
                    "count(*) FILTER (WHERE r1.es_carro=true) over(partition by r1.numero_mes ) estacionamiento_carros,\n" +
                    "count(*) FILTER (WHERE r1.es_carro=false) over(partition by r1.numero_mes ) estacionamiento_motos,\n" +
                    "count(*) FILTER (WHERE r1.valor_cobrado= 0) over(partition by r1.numero_mes ) total_estacionammientos_sin_cobro,\n" +
                    "count(*) FILTER (WHERE r1.es_carro=true and r1.valor_cobrado= 0) over(partition by r1.numero_mes ) estacionamiento_carros_sin_cobro,\n" +
                    "count(*) FILTER (WHERE r1.es_carro=false and r1.valor_cobrado= 0) over(partition by r1.numero_mes ) estacionamiento_motos_sin_cobro,\n" +
                    "count(*) FILTER (WHERE r1.valor_cobrado> 0) over(partition by r1.numero_mes ) total_estacionammientos_con_cobro,\n" +
                    "count(*) FILTER (WHERE r1.es_carro=true and r1.valor_cobrado> 0) over(partition by r1.numero_mes ) estacionamiento_carros_con_cobro,\n" +
                    "count(*) FILTER (WHERE r1.es_carro=false and r1.valor_cobrado> 0) over(partition by r1.numero_mes ) estacionamiento_motos_con_cobro,\n" +
                    "sum(r1.valor_cobrado) FILTER (WHERE r1.estado=3) over(partition by r1.numero_mes ) cartera_total,\n" +
                    "sum(r1.valor_cobrado) FILTER (WHERE r1.es_carro=true and r1.estado=3) over(partition by r1.numero_mes ) cartera_carros,\n" +
                    "sum(r1.valor_cobrado) FILTER (WHERE r1.es_carro=false and r1.estado=3) over(partition by r1.numero_mes ) cartera_motos,\n" +
                    "sum(r1.valor_cobrado) FILTER (WHERE r1.estado<>3) over(partition by r1.numero_mes ) recaudo_total,\n" +
                    "sum(r1.valor_cobrado) FILTER (WHERE r1.es_carro=true and r1.estado<>3) over(partition by r1.numero_mes ) recaudo_carros,\n" +
                    "sum(r1.valor_cobrado) FILTER (WHERE r1.es_carro=false and r1.estado<>3) over(partition by r1.numero_mes ) recaudo_motos,\n" +
                    "sum(r1.valor_cobrado) over(partition by r1.numero_mes ) cobro_total,\n" +
                    "sum(r1.valor_cobrado) FILTER (WHERE r1.es_carro=true) over(partition by r1.numero_mes ) cobro_carros,\n" +
                    "sum(r1.valor_cobrado) FILTER (WHERE r1.es_carro=false) over(partition by r1.numero_mes ) cobro_motos\n" +
                    "from \n" +
                    "(select r1.*,\n" +
                    "--cast(substring(cast(r1.nf_ingreso as text),5,2) as int4) numero_mes\n" +
                    "mod(r1.nf_ingreso,10000)/100 numero_mes \n" +
                    "--EXTRACT(MONTH FROM r1.fh_ingreso) numero_mes \n" +
                    "from registro r1\n" +
                    "where \n" +
                    "r1.nf_ingreso < ? \n" +
                    "and r1.nf_ingreso > ?) r1\n" +
                    "where \n" +
                    "r1.numero_mes in (?,?,?,?,?,?,?,?,?,?,?,?)\n" +
                    "order by 1\n" +
                    ") registro\n";
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setLong(1, Long.parseLong(nfYear + "1232"));//significa hasta el 31 de diciembre
            pst.setLong(2, Long.parseLong(nfYear + "0100"));//significa desde el 1 de enero
            pst.setLong(3, enero == true ? 1 : 0);
            pst.setLong(4, febrero == true ? 2 : 0);
            pst.setLong(5, marzo == true ? 3 : 0);
            pst.setLong(6, abril == true ? 4 : 0);
            pst.setLong(7, mayo == true ? 5 : 0);
            pst.setLong(8, junio == true ? 6 : 0);
            pst.setLong(9, julio == true ? 7 : 0);
            pst.setLong(10, agosto == true ? 8 : 0);
            pst.setLong(11, septiembre == true ? 9 : 0);
            pst.setLong(12, octubre == true ? 10 : 0);
            pst.setLong(13, noviembre == true ? 11 : 0);
            pst.setLong(14, diciembre == true ? 12 : 0);

            ResultSet rs = pst.executeQuery();
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

    @Override
    public List<Map<String, Object>> reporteVehiculo(
            Connection connection,
            Long nfInicial,
            Long nfFinal,
            String placa,
            Paginacion paginacion) throws Exception {
        List<Map<String, Object>> resul = new ArrayList<>();
        PreparedStatement pst = null;
        Connection conn = connection;
        try {
            ResultSet rs;
            paginacion.setTotal(0L);
            String SQL_SELECT = "select \n" +
                    "count(*) over(partition by '' ) total_filas,\n" +
                    "r.*,\n" +
                    "case r.estado \n" +
                    "when 0 then 'Abierta'\n" +
                    "when 1 then 'Pagada'\n" +
                    "when 2 then 'Salio sin cobro'\n" +
                    "when 3 then 'Reportada'\n" +
                    "when 4 then 'Pago extemporaneo'\t\n" +
                    "when 5 then 'Prepago'\n" +
                    "end nombre_estado,\n" +
                    "u1.nombre1||' '||u1.nombre2 ||' '||u1.apellido1 ||' '|| u1.apellido2 nombre_promotor_ingreso,\n" +
                    "u2.nombre1||' '||u2.nombre2 ||' '||u2.apellido1 ||' '|| u2.apellido2 nombre_promotor_egreso,\n" +
                    "u3.nombre1||' '||u3.nombre2 ||' '||u3.apellido1 ||' '|| u3.apellido2 nombre_promotor_recauda,\n" +
                    "u4.nombre1||' '||u4.nombre2 ||' '||u4.apellido1 ||' '|| u4.apellido2 nombre_promotor_reporta,\n" +
                    "z1.nombre nombre_zona\n" +
                    "from \n" +
                    "registro r\n" +
                    "left join usuario u1 on u1.id=r.promotor_ingreso \n" +
                    "left join usuario u2 on u2.id=r.promotor_egreso\n" +
                    "left join usuario u3 on u3.id=r.promotor_recauda \n" +
                    "left join usuario u4 on u4.id=r.promotor_reporta \n" +
                    "left join zona z1 on z1.id = r.zona \n" +
                    "where\n" +
                    " \tr.nf_ingreso > ?\n" +
                    "and r.nf_ingreso < ?\n" +
                    "and r.placa = ?\n" +
                    "order by r.fh_ingreso \n" +
                    "limit ?\n" +
                    "offset ?";

            pst = conn.prepareStatement(SQL_SELECT);
            pst.setLong(1, nfInicial - 1);
            pst.setLong(2, nfFinal + 1);
            pst.setString(3, placa);
            pst.setLong(4, paginacion.getLimite());
            pst.setLong(5, paginacion.getActual() - 1);
            rs = pst.executeQuery();
            while (rs.next()) {
                paginacion.setTotal(rs.getLong(1));
                Map<String, Object> conte = new HashMap<>();
                ResultSetMetaData dataRS = rs.getMetaData();
                for (int col = 1; col <= rs.getMetaData().getColumnCount(); col++) {
                    String columnTypeName = dataRS.getColumnTypeName(col);
                    String nombreColumna = dataRS.getColumnName(col);
                    //los timestamp se recuperan con la zona horaria del sistema
                    if (columnTypeName.equals(Utilidades.STRING_TIME_STAMP)) {
                        conte.put(nombreColumna, Utilidades.getTimeStamp(rs, nombreColumna));
                    } else {
                        if (nombreColumna.equals("id")) {
                            conte.put(nombreColumna, rs.getString(col));
                        } else {
                            conte.put(nombreColumna, rs.getObject(col));
                        }
                    }
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

    @Override
    public List<Map<String, Object>> reporteCartera(
            Connection connection,
            Long nfInicial,
            Long nfFinal,
            Paginacion paginacion) throws Exception {
        List<Map<String, Object>> resul = new ArrayList<>();
        PreparedStatement pst = null;
        Connection conn = connection;
        try {
            ResultSet rs;
            paginacion.setTotal(0L);
            String SQL_SELECT = "select \n" +
                    "count(*) over(partition by '' ) total_filas,\n" +
                    "r.*,\n" +
                    "case r.estado \n" +
                    "when 0 then 'Abierta'\n" +
                    "when 1 then 'Pagada'\n" +
                    "when 2 then 'Salio sin cobro'\n" +
                    "when 3 then 'Reportada'\n" +
                    "when 4 then 'Pago extemporaneo'\t\n" +
                    "when 5 then 'Prepago'\n" +
                    "end nombre_estado,\n" +
                    "u1.nombre1||' '||u1.nombre2 ||' '||u1.apellido1 ||' '|| u1.apellido2 nombre_promotor_ingreso,\n" +
                    "u2.nombre1||' '||u2.nombre2 ||' '||u2.apellido1 ||' '|| u2.apellido2 nombre_promotor_egreso,\n" +
                    "u3.nombre1||' '||u3.nombre2 ||' '||u3.apellido1 ||' '|| u3.apellido2 nombre_promotor_recauda,\n" +
                    "u4.nombre1||' '||u4.nombre2 ||' '||u4.apellido1 ||' '|| u4.apellido2 nombre_promotor_reporta,\n" +
                    "z1.nombre nombre_zona\n" +
                    "from \n" +
                    "registro r\n" +
                    "left join usuario u1 on u1.id=r.promotor_ingreso \n" +
                    "left join usuario u2 on u2.id=r.promotor_egreso\n" +
                    "left join usuario u3 on u3.id=r.promotor_recauda \n" +
                    "left join usuario u4 on u4.id=r.promotor_reporta \n" +
                    "left join zona z1 on z1.id = r.zona \n" +
                    "where\n" +
                    " \tr.nf_ingreso > ?\n" +
                    "and r.nf_ingreso < ?\n" +
                    "and r.estado = 3\n" +
                    "order by r.fh_ingreso \n" +
                    "limit ?\n" +
                    "offset ?";

            pst = conn.prepareStatement(SQL_SELECT);
            pst.setLong(1, nfInicial - 1);
            pst.setLong(2, nfFinal + 1);
            pst.setLong(3, paginacion.getLimite());
            pst.setLong(4, paginacion.getActual() - 1);
            rs = pst.executeQuery();
            while (rs.next()) {
                paginacion.setTotal(rs.getLong(1));
                Map<String, Object> conte = new HashMap<>();
                ResultSetMetaData dataRS = rs.getMetaData();
                for (int col = 1; col <= rs.getMetaData().getColumnCount(); col++) {
                    String columnTypeName = dataRS.getColumnTypeName(col);
                    String nombreColumna = dataRS.getColumnName(col);
                    //los timestamp se recuperan con la zona horaria del sistema
                    if (columnTypeName.equals(Utilidades.STRING_TIME_STAMP)) {
                        conte.put(nombreColumna, Utilidades.getTimeStamp(rs, nombreColumna));
                    } else {
                        if (nombreColumna.equals("id")) {
                            conte.put(nombreColumna, rs.getString(col));
                        } else {
                            conte.put(nombreColumna, rs.getObject(col));
                        }
                    }
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

    @Override
    public List<Map<String, Object>> reporteHistorico(Connection connection, Long fechaConsulta) throws Exception {
        List<Map<String, Object>> resul = new ArrayList<>();
        PreparedStatement pst = null;
        Connection conn = connection;
        try {
            ResultSet rs;
            String SQL_SELECT = "SELECT \n" +
                    "    COUNT(*) OVER(PARTITION BY '') AS total_filas,\n" +
                    "    r.*,\n" +
                    "    CASE r.estado \n" +
                    "        WHEN 0 THEN 'Abierta'\n" +
                    "        WHEN 1 THEN 'PostPago'\n" +
                    "        WHEN 2 THEN 'Salio sin cobro'\n" +
                    "        WHEN 3 THEN 'Cartera'\n" +
                    "        WHEN 4 THEN 'Pago extemporaneo'\n" +
                    "        WHEN 5 THEN 'Prepago'\n" +
                    "    END AS nombre_estado,\n" +
                    "    u1.nombre1 || ' ' || u1.nombre2 || ' ' || u1.apellido1 || ' ' || u1.apellido2 AS nombre_promotor_ingreso,\n" +
                    "    u2.nombre1 || ' ' || u2.nombre2 || ' ' || u2.apellido1 || ' ' || u2.apellido2 AS nombre_promotor_egreso,\n" +
                    "    u3.nombre1 || ' ' || u3.nombre2 || ' ' || u3.apellido1 || ' ' || u3.apellido2 AS nombre_promotor_recauda,\n" +
                    "    u4.nombre1 || ' ' || u4.nombre2 || ' ' || u4.apellido1 || ' ' || u4.apellido2 AS nombre_promotor_reporta,\n" +
                    "    z1.nombre AS nombre_zona\n" +
                    "FROM \n" +
                    "    registro r\n" +
                    "LEFT JOIN \n" +
                    "    usuario u1 ON u1.id = r.promotor_ingreso \n" +
                    "LEFT JOIN \n" +
                    "    usuario u2 ON u2.id = r.promotor_egreso\n" +
                    "LEFT JOIN \n" +
                    "    usuario u3 ON u3.id = r.promotor_recauda \n" +
                    "LEFT JOIN \n" +
                    "    usuario u4 ON u4.id = r.promotor_reporta \n" +
                    "LEFT JOIN \n" +
                    "    zona z1 ON z1.id = r.zona \n" +
                    "WHERE \n" +
                    "    (r.estado = 4 AND r.nf_recaudo = ?) OR (r.estado != 4 AND r.nf_ingreso = ?)\n" +
                    "ORDER BY \n" +
                    "    r.fh_ingreso";

            pst = conn.prepareStatement(SQL_SELECT);
            pst.setLong(1, fechaConsulta);
            pst.setLong(2, fechaConsulta); // Estableciendo el segundo parámetro
            rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, Object> conte = new HashMap<>();
                ResultSetMetaData dataRS = rs.getMetaData();
                for (int col = 1; col <= rs.getMetaData().getColumnCount(); col++) {
                    String columnTypeName = dataRS.getColumnTypeName(col);
                    String nombreColumna = dataRS.getColumnName(col);
                    //los timestamp se recuperan con la zona horaria del sistema
                    if (columnTypeName.equals(Utilidades.STRING_TIME_STAMP)) {
                        conte.put(nombreColumna, Utilidades.getTimeStamp(rs, nombreColumna));
                    } else {
                        if (nombreColumna.equals("id")) {
                            conte.put(nombreColumna, rs.getString(col));
                        } else {
                            conte.put(nombreColumna, rs.getObject(col));
                        }
                    }
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

    @Override
    public List<Map<String, Object>> reporteHistoricoMensual(Connection connection,
                                                             Integer mes,
                                                             Integer year) throws Exception {
        List<Map<String, Object>> resul = new ArrayList<>();
        PreparedStatement pst = null;
        Connection conn = connection;
        try {
            String SQL_SELECT = "SELECT \n" +
                    "    COUNT(*) OVER(PARTITION BY '') AS total_filas,\n" +
                    "    r.*,\n" +
                    "    CASE r.estado \n" +
                    "        WHEN 0 THEN 'Abierta'\n" +
                    "        WHEN 1 THEN 'PostPago'\n" +
                    "        WHEN 2 THEN 'Salio sin cobro'\n" +
                    "        WHEN 3 THEN 'Cartera'\n" +
                    "        WHEN 4 THEN 'Pago extemporaneo'\n" +
                    "        WHEN 5 THEN 'Prepago'\n" +
                    "    END AS nombre_estado,\n" +
                    "    u1.nombre1 || ' ' || u1.nombre2 || ' ' || u1.apellido1 || ' ' || u1.apellido2 AS nombre_promotor_ingreso,\n" +
                    "    u2.nombre1 || ' ' || u2.nombre2 || ' ' || u2.apellido1 || ' ' || u2.apellido2 AS nombre_promotor_egreso,\n" +
                    "    u3.nombre1 || ' ' || u3.nombre2 || ' ' || u3.apellido1 || ' ' || u3.apellido2 AS nombre_promotor_recauda,\n" +
                    "    u4.nombre1 || ' ' || u4.nombre2 || ' ' || u4.apellido1 || ' ' || u4.apellido2 AS nombre_promotor_reporta,\n" +
                    "    z1.nombre AS nombre_zona\n" +
                    "FROM \n" +
                    "    registro r\n" +
                    "LEFT JOIN \n" +
                    "    usuario u1 ON u1.id = r.promotor_ingreso \n" +
                    "LEFT JOIN \n" +
                    "    usuario u2 ON u2.id = r.promotor_egreso\n" +
                    "LEFT JOIN \n" +
                    "    usuario u3 ON u3.id = r.promotor_recauda \n" +
                    "LEFT JOIN \n" +
                    "    usuario u4 ON u4.id = r.promotor_reporta \n" +
                    "LEFT JOIN \n" +
                    "    zona z1 ON z1.id = r.zona \n" +
                    "WHERE \n" +
                    "    EXTRACT(YEAR FROM TO_DATE(CASE WHEN r.estado = 4 THEN CAST(r.nf_recaudo AS TEXT) ELSE CAST(r.nf_ingreso AS TEXT) END, 'YYYYMMDD')) = ? \n"+
                    "    AND EXTRACT(MONTH FROM TO_DATE(CASE WHEN r.estado = 4 THEN CAST(r.nf_recaudo AS TEXT) ELSE CAST(r.nf_ingreso AS TEXT) END, 'YYYYMMDD')) = ? \n" +
                    "ORDER BY \n" +
                    "    r.fh_ingreso";

            pst = conn.prepareStatement(SQL_SELECT);

            // Configurar el año para la condición WHERE
            pst.setLong(1, year);

            // Configurar el mes para la condición WHERE
            pst.setInt(2, mes);

            ResultSet rs = pst.executeQuery();
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
                // Manejo de excepciones
            }
        }
        return resul;
    }


}
