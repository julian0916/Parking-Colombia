package co.zer.repository;

import co.zer.model.Estados;
import co.zer.model.Indice;
import co.zer.model.RegistroCompleto;
import co.zer.service.ComunRegistro;

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
     * recuperar datos de Timestamp to LocalDateTime
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


    @Override
    public RegistroCompleto guardar(Connection connection, RegistroCompleto registroCompleto) throws Exception {
        return null;
    }

    @Override
    public RegistroCompleto insertar(Connection connection, RegistroCompleto registroCompleto) throws Exception {
        final String SQL_INSERT = "INSERT INTO registro\n" +
                "(id, placa, es_carro, zona, h_inicia_zona, h_termina_zona, " +
                "valor_h, promotor_ingreso, nf_ingreso, fh_ingreso, estado, h_cobradas, valor_cobrado, " +
                "egresa_sistema, promotor_egreso, fh_egreso, nf_egreso, promotor_recauda, promotor_reporta, " +
                "fh_recaudo, nf_recaudo, minutos_gracia_zona, minutos_para_nueva_gracia_zona)\n" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?);\n";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_INSERT);
            Indice indice = new Indice();
            pst.setObject(indice.siguiente(), registroCompleto.getId());
            pst.setObject(indice.siguiente(), registroCompleto.getPlaca());
            pst.setObject(indice.siguiente(), registroCompleto.getEsCarro());
            pst.setObject(indice.siguiente(), registroCompleto.getZona());
            pst.setObject(indice.siguiente(), getHValue(registroCompleto.getHIniciaZona()));
            pst.setObject(indice.siguiente(), getHValue(registroCompleto.getHTerminaZona()));

            pst.setObject(indice.siguiente(), registroCompleto.getValorH());
            pst.setObject(indice.siguiente(), registroCompleto.getPromotorIngreso());
            pst.setObject(indice.siguiente(), registroCompleto.getNFIngreso());
            pst.setObject(indice.siguiente(), getFHValue(registroCompleto.getFHIngreso()));
            pst.setObject(indice.siguiente(), registroCompleto.getEstado());
            pst.setObject(indice.siguiente(), registroCompleto.getHCobradas());
            pst.setObject(indice.siguiente(), registroCompleto.getValorCobrado());

            pst.setObject(indice.siguiente(), registroCompleto.getEgresaSistema());
            pst.setObject(indice.siguiente(), registroCompleto.getPromotorEgreso());
            pst.setObject(indice.siguiente(), getFHValue(registroCompleto.getFHEgreso()));
            pst.setObject(indice.siguiente(), registroCompleto.getNFEgreso());
            pst.setObject(indice.siguiente(), registroCompleto.getPromotorRecauda());
            pst.setObject(indice.siguiente(), registroCompleto.getPromotorReporta());

            pst.setObject(indice.siguiente(), getFHValue(registroCompleto.getFHRecaudo()));
            pst.setObject(indice.siguiente(), registroCompleto.getNFRecaudo());

            pst.setObject(indice.siguiente(), registroCompleto.getMinutosGraciaZona());
            pst.setObject(indice.siguiente(), registroCompleto.getMinutosParaNuevaGraciaZona());
            pst.execute();
        } catch (Exception ex) {
            if (ex.getMessage().contains("unique")) {
                throw new Exception("Ya existe el id del registro");
            }
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return registroCompleto;
    }

    @Override
    public RegistroCompleto actualizar(Connection connection, RegistroCompleto registroCompleto) throws Exception {
        final String SQL_UPDATE = "UPDATE registro\n" +
                "SET placa=?, es_carro=?, zona=?, h_inicia_zona=?, h_termina_zona=?, valor_h=?, " +
                "promotor_ingreso=?, nf_ingreso=?, fh_ingreso=?, estado=?, h_cobradas=?, valor_cobrado=?, " +
                "egresa_sistema=?, promotor_egreso=?, fh_egreso=?, nf_egreso=?, promotor_recauda=?, promotor_reporta=?, " +
                "fh_recaudo=?, nf_recaudo=?, minutos_gracia_zona=?, minutos_para_nueva_gracia_zona=?\n" +
                "WHERE id=?;";
        PreparedStatement pst = null;
        Connection conn = connection;
        try {
            pst = conn.prepareStatement(SQL_UPDATE);
            Indice indice = new Indice();
            Long promotorReporta = null;
            try {
                promotorReporta = registroCompleto.getPromotorReporta().longValue() < 1 ? null : registroCompleto.getPromotorReporta();
            } catch (Exception ex) {
            }
            Long promotorEgreso = null;
            try {
                promotorEgreso = registroCompleto.getPromotorEgreso().longValue() < 1 ? null : registroCompleto.getPromotorEgreso();
            } catch (Exception ex) {
            }
            pst.setObject(indice.siguiente(), registroCompleto.getPlaca());
            pst.setObject(indice.siguiente(), registroCompleto.getEsCarro());
            pst.setObject(indice.siguiente(), registroCompleto.getZona());
            pst.setObject(indice.siguiente(), getHValue(registroCompleto.getHIniciaZona()));
            pst.setObject(indice.siguiente(), getHValue(registroCompleto.getHTerminaZona()));

            pst.setObject(indice.siguiente(), registroCompleto.getValorH());
            pst.setObject(indice.siguiente(), registroCompleto.getPromotorIngreso());
            pst.setObject(indice.siguiente(), registroCompleto.getNFIngreso());
            pst.setObject(indice.siguiente(), getFHValue(registroCompleto.getFHIngreso()));
            pst.setObject(indice.siguiente(), registroCompleto.getEstado());
            pst.setObject(indice.siguiente(), registroCompleto.getHCobradas());
            pst.setObject(indice.siguiente(), registroCompleto.getValorCobrado());

            pst.setObject(indice.siguiente(), registroCompleto.getEgresaSistema());
            pst.setObject(indice.siguiente(), promotorEgreso);
            pst.setObject(indice.siguiente(), getFHValue(registroCompleto.getFHEgreso()));
            pst.setObject(indice.siguiente(), registroCompleto.getNFEgreso());
            pst.setObject(indice.siguiente(), registroCompleto.getPromotorRecauda());
            pst.setObject(indice.siguiente(), promotorReporta);

            pst.setObject(indice.siguiente(), getFHValue(registroCompleto.getFHRecaudo()));
            pst.setObject(indice.siguiente(), registroCompleto.getNFRecaudo());
            pst.setObject(indice.siguiente(), registroCompleto.getMinutosGraciaZona());
            pst.setObject(indice.siguiente(), registroCompleto.getMinutosParaNuevaGraciaZona());

            pst.setObject(indice.siguiente(), registroCompleto.getId());
            long result = pst.executeUpdate();
            if (result < 1) {
                throw new Exception("El registro no fue actualizado");
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return registroCompleto;
    }

    private int siguiente(Integer valor) {
        valor = valor + 1;
        return Math.toIntExact(valor);
    }

    private String getBlancoSiNull(String da) {
        if (da == null) {
            return "";
        }
        if (da.trim().equals("")) {
            return "";
        }
        return da + " ";
    }

    private String getNombre(String n1, String n2, String a1, String a2) {
        return getBlancoSiNull(n1) + getBlancoSiNull(n2) + getBlancoSiNull(a1) + getBlancoSiNull(a2);
    }

    private List<RegistroCompleto> getContenidoFromRS(ResultSet rs) throws Exception {
        List<RegistroCompleto> resp = new ArrayList<>();
        while (rs.next()) {
            Indice indice = new Indice();
            RegistroCompleto reg = new RegistroCompleto();

            reg.setId(rs.getLong(indice.siguiente()));
            reg.setPlaca(rs.getString(indice.siguiente()));
            reg.setEsCarro(rs.getBoolean(indice.siguiente()));
            reg.setZona(rs.getLong(indice.siguiente()));
            reg.setHIniciaZona(getLT(rs.getTime(indice.siguiente())));
            reg.setHTerminaZona(getLT(rs.getTime(indice.siguiente())));

            reg.setValorH(rs.getLong(indice.siguiente()));
            reg.setPromotorIngreso(rs.getLong(indice.siguiente()));
            reg.setNFIngreso(rs.getLong(indice.siguiente()));
            reg.setFHIngreso(getLDT(rs.getTimestamp(indice.siguiente())));
            reg.setEstado(rs.getLong(indice.siguiente()));
            reg.setHCobradas(rs.getLong(indice.siguiente()));
            reg.setValorCobrado(rs.getLong(indice.siguiente()));

            reg.setEgresaSistema(rs.getBoolean(indice.siguiente()));
            reg.setPromotorEgreso(rs.getLong(indice.siguiente()));
            reg.setFHEgreso(getLDT(rs.getTimestamp(indice.siguiente())));
            reg.setNFEgreso(rs.getLong(indice.siguiente()));
            reg.setPromotorRecauda(rs.getLong(indice.siguiente()));
            reg.setPromotorReporta(rs.getLong(indice.siguiente()));

            reg.setFHRecaudo(getLDT(rs.getTimestamp(indice.siguiente())));
            reg.setNFRecaudo(rs.getLong(indice.siguiente()));

            reg.setNombrePromotorIngreso(getNombre(rs.getString(indice.siguiente()), rs.getString(indice.siguiente()), rs.getString(indice.siguiente()), rs.getString(indice.siguiente())));
            reg.setNombrePromotorEgreso(getNombre(rs.getString(indice.siguiente()), rs.getString(indice.siguiente()), rs.getString(indice.siguiente()), rs.getString(indice.siguiente())));
            reg.setNombrePromotorRecauda(getNombre(rs.getString(indice.siguiente()), rs.getString(indice.siguiente()), rs.getString(indice.siguiente()), rs.getString(indice.siguiente())));
            reg.setNombrePromotorReporta(getNombre(rs.getString(indice.siguiente()), rs.getString(indice.siguiente()), rs.getString(indice.siguiente()), rs.getString(indice.siguiente())));
            reg.setNombreZona(rs.getString(indice.siguiente()));
            reg.setMinutosGraciaZona(rs.getLong(indice.siguiente()));
            reg.setMinutosParaNuevaGraciaZona(rs.getLong(indice.siguiente()));
            resp.add(reg);
        }
        return resp;
    }

    private String getSelectFrom() {
        return "SELECT r.id, placa, es_carro, zona, h_inicia_zona, h_termina_zona, \n" +
                "valor_h, promotor_ingreso, nf_ingreso, fh_ingreso, estado, h_cobradas, \n" +
                "valor_cobrado, egresa_sistema, promotor_egreso, fh_egreso, nf_egreso, promotor_recauda, \n" +
                "promotor_reporta, fh_recaudo, nf_recaudo,\n" +
                "uingreso.nombre1, uingreso.nombre2, uingreso.apellido1, uingreso.apellido2, \n" +
                "uegreso.nombre1, uegreso.nombre2, uegreso.apellido1, uegreso.apellido2, \n" +
                "urecaudo.nombre1, urecaudo.nombre2, urecaudo.apellido1, urecaudo.apellido2, \n" +
                "ureporta.nombre1, ureporta.nombre2, ureporta.apellido1, ureporta.apellido2, \n" +
                "z.nombre, minutos_gracia_zona, minutos_para_nueva_gracia_zona \n" +
                "FROM registro as r\n" +
                "LEFT JOIN usuario as uingreso ON uingreso.id = r.promotor_ingreso \n" +
                "LEFT JOIN usuario as uegreso ON uegreso.id = r.promotor_egreso \n" +
                "LEFT JOIN usuario as urecaudo ON urecaudo.id = r.promotor_recauda \n" +
                "LEFT JOIN usuario as ureporta ON ureporta.id = r.promotor_reporta \n" +
                "LEFT JOIN zona as z ON z.id = r.zona \n";
    }

    @Override
    public RegistroCompleto recuperarPorId(Connection connection, Long id) throws Exception {
        RegistroCompleto res = null;
        final String SQL_SELECT = getSelectFrom() +
                "WHERE r.id = ? ;";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setLong(1, id);
            rs = pst.executeQuery();
            List<RegistroCompleto> resp = getContenidoFromRS(rs);
            res = resp.get(0);
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
    public List<RegistroCompleto> carteraPorPlaca(Connection connection, String placa) throws Exception {
        List<RegistroCompleto> res;
        final String SQL_SELECT = getSelectFrom() +
                "WHERE r.estado = ? \n" +
                "AND r.placa = ? ;";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setLong(1, Estados.Registro.SALIO_REPORTADO);
            pst.setString(2, placa);
            rs = pst.executeQuery();
            res = getContenidoFromRS(rs);
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
    public List<RegistroCompleto> abiertosZona(Connection connection, Long idZona) throws Exception {
        List<RegistroCompleto> res;
        final String SQL_SELECT = getSelectFrom() +
                "WHERE  r.zona = ?\n" +
                "AND r.estado = ? \n" +
                "ORDER BY r.placa;";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setLong(1, idZona);
            pst.setLong(2, Estados.Registro.ABIERTA);
            rs = pst.executeQuery();
            res = getContenidoFromRS(rs);
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
    public List<RegistroCompleto> abiertosSistema(Connection connection) throws Exception {
        List<RegistroCompleto> res;
        final String SQL_SELECT = getSelectFrom() +
                "WHERE r.estado = ? ;";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setLong(1, Estados.Registro.ABIERTA);
            rs = pst.executeQuery();
            res = getContenidoFromRS(rs);
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
    public List<RegistroCompleto> abiertosPlaca(Connection connection, String placa) throws Exception {
        List<RegistroCompleto> res;
        final String SQL_SELECT = getSelectFrom() +
                "WHERE r.estado = ? " +
                "AND r.placa = ? ;";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setLong(1, Estados.Registro.ABIERTA);
            pst.setString(2, placa);
            rs = pst.executeQuery();
            res = getContenidoFromRS(rs);
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
    public List<RegistroCompleto> getPrepagoActivosZona(
            Connection connection,
            Long idZona,
            LocalDateTime localDateTime) throws Exception {
        List<RegistroCompleto> res;
        final String SQL_SELECT = getSelectFrom() +
                "WHERE  \n" +
                "NOT EXISTS ( SELECT 1\n " +
                "FROM registro re \n" +
                "WHERE \n" +
                "re.fh_ingreso > r.fh_ingreso\n" +
                "AND re.nf_ingreso = r.nf_ingreso\n" +
                "AND re.placa = r.placa\n)" +
                "AND r.cupo_disponible  = false\n" +
                "AND r.zona = ?\n" +
                "AND r.estado = ?\n" +
                "AND r.nf_egreso = ? \n" +
                "ORDER BY r.placa";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setLong(1, idZona);
            pst.setLong(2, Estados.Registro.PREPAGO);
            pst.setLong(3, ComunRegistro.getFechaActualNumero(localDateTime.toLocalDate()));
            rs = pst.executeQuery();
            res = getContenidoFromRS(rs);
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


    /**
     * Se busca en la zona especificada si existen registros con estado prepago
     * que aún esten en la zona y que no tengan para la placa un resgitro posterior
     * al prepagado.
     *
     * @param connection
     * @param idZona
     * @param localDateTime
     * @return
     * @throws Exception
     */
    @Override
    public List<RegistroCompleto> getAlertasPrepagoZona(
            Connection connection,
            Long idZona,
            LocalDateTime localDateTime) throws Exception {
        List<RegistroCompleto> res;
        final String SQL_SELECT = getSelectFrom() +
                "WHERE  \n" +
                "NOT EXISTS ( SELECT 1\n " +
                "FROM registro re \n" +
                "WHERE \n" +
                "re.fh_ingreso > r.fh_ingreso\n" +
                "AND re.nf_ingreso = r.nf_ingreso\n" +
                "AND re.placa = r.placa\n)" +
                "AND r.cupo_disponible  = false\n" +
                "AND r.fh_egreso  < ?\n" +
                "AND r.zona = ?\n" +
                "AND r.estado = ?\n" +
                "AND r.nf_egreso = ? \n" +
                "ORDER BY r.placa;";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setTimestamp(1, Timestamp.valueOf(localDateTime));
            pst.setLong(2, idZona);
            pst.setLong(3, Estados.Registro.PREPAGO);
            pst.setLong(4, ComunRegistro.getFechaActualNumero(localDateTime.toLocalDate()));
            rs = pst.executeQuery();
            res = getContenidoFromRS(rs);
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
    public void salioPrepagoActualizar(Connection connection, Long idPago) throws Exception {
        final String SQL_UPDATE = "UPDATE registro\n" +
                "SET cupo_disponible=?\n" +
                "WHERE id=?;";
        PreparedStatement pst = null;
        Connection conn = connection;
        try {
            pst = conn.prepareStatement(SQL_UPDATE);
            Indice indice = new Indice();

            pst.setObject(indice.siguiente(), true);
            pst.setObject(indice.siguiente(), idPago);
            long result = pst.executeUpdate();
            if (result < 1) {
                throw new Exception("El registro no fue actualizado");
            }
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
    public List<RegistroCompleto> getUltimosMovimientosHoy(Connection connection, String placa) throws Exception {
        List<RegistroCompleto> res;
        final String SQL_SELECT = getSelectFrom() +
                "WHERE  \n" +
                "r.placa = ?\n" +
                "AND r.nf_ingreso = ?\n" +
                "ORDER BY r.fh_ingreso desc\n" +
                "LIMIT 2\n" +
                "OFFSET 0;";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setString(1, placa);
            pst.setLong(2, ComunRegistro.getFechaActualNumero());
            rs = pst.executeQuery();
            res = getContenidoFromRS(rs);
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
    public List<Map<String, Object>> ocupacionZona(Connection connection, Long idZona) throws Exception {
        List<Map<String, Object>> res = new ArrayList<>();
        final String SQL_SELECT = "SELECT COUNT(r.es_carro), r.es_carro, z.id, \n" +
                "z.nombre, z.celdas_carro, z.celdas_moto \n" +
                "FROM registro r \n" +
                "LEFT JOIN zona z ON z.id = r.zona \n" +
                "WHERE  r.zona = ?\n" +
                "AND r.estado = ? \n" +
                //"AND r.nf_ingreso = ? \n" +
                "GROUP BY r.es_carro, z.id, z.nombre, z.celdas_carro, z.celdas_moto \n" +
                "ORDER BY 1 ";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setLong(1, idZona);
            pst.setLong(2, Estados.Registro.ABIERTA);
            //pst.setLong(3, nfIngreso);
            rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, Object> conteido = new HashMap<>();
                Indice indice = new Indice();
                double cantidad = rs.getDouble(indice.siguiente());
                conteido.put("cantidad", cantidad);
                Boolean esCarro = rs.getBoolean(indice.siguiente());
                conteido.put("esCarro", esCarro);
                conteido.put("idZona", rs.getLong(indice.siguiente()));
                conteido.put("nombreZona", rs.getString(indice.siguiente()));
                double carros = rs.getDouble(indice.siguiente());
                conteido.put("celdasCarro", carros);
                double motos = rs.getDouble(indice.siguiente());
                conteido.put("celdasMoto", motos);
                double pocentaje = cantidad / motos;
                if (esCarro) {
                    pocentaje = cantidad / carros;
                }
                conteido.put("porcentaje", pocentaje * 100);
                res.add(conteido);
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
}
