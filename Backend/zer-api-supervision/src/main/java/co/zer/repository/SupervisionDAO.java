package co.zer.repository;

import co.zer.model.PreguntaSupervision;
import co.zer.model.RegistroSupervision;
import co.zer.utils.Utilidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
    public void ajustarContenido(PreguntaSupervision preguntaSupervision) {
    }

    @Override
    public void validarContenido(PreguntaSupervision preguntaSupervision) throws Exception {
        if (preguntaSupervision == null) {
            throw new Exception("Debe ingresar los datos de la pregunta");
        }
        if (preguntaSupervision.getPregunta().length() < 3 || preguntaSupervision.getPregunta().length() > 300) {
            throw new Exception("La pregunta debe contener como máximo 300 caracteres y mínimo 3");
        }
    }

    @Override
    public List<PreguntaSupervision> guardar(Connection connection, PreguntaSupervision preguntaSupervision) throws Exception {
        if (connection == null) {
            throw new Exception("La conexión no está disponible");
        }
        boolean esActualizar = preguntaSupervision.getId() != null && preguntaSupervision.getId() > 0;
        validarContenido(preguntaSupervision);
        ajustarContenido(preguntaSupervision);
        if (esActualizar) {
            return actualizar(connection, preguntaSupervision);
        }
        return insertar(connection, preguntaSupervision);
    }

    @Override
    public List<PreguntaSupervision> insertar(Connection connection, PreguntaSupervision preguntaSupervision) throws Exception {
        final String SQL_INSERT = "INSERT INTO preguntas_supervision\n" +
                "(pregunta)\n" +
                "VALUES(?) RETURNING id;\n";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_INSERT);
            pst.setString(1, preguntaSupervision.getPregunta());
            rs = pst.executeQuery();
            while (rs.next()) {
                preguntaSupervision.setId(rs.getLong(1));
            }
        } catch (Exception ex) {
            if (ex.getMessage().contains("unique")) {
                throw new Exception("Ya existe la pregunta ingresada");
            }
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return consultar(conn);
    }

    @Override
    public List<PreguntaSupervision> actualizar(Connection connection, PreguntaSupervision preguntaSupervision) throws Exception {
        final String SQL_UPDATE = "UPDATE preguntas_supervision\n" +
                "SET pregunta= ? \n" +
                "WHERE id = ? ";
        PreparedStatement pst = null;
        Connection conn = connection;
        try {
            pst = conn.prepareStatement(SQL_UPDATE);
            pst.setString(1, preguntaSupervision.getPregunta());
            pst.setLong(2, preguntaSupervision.getId());
            long result = pst.executeUpdate();
            if (result < 1) {
                throw new Exception("La pregunta no fue actualizada");
            }
        } catch (Exception ex) {
            if (ex.getMessage().contains("unique")) {
                throw new Exception("La pregunta ya existe");
            }
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return consultar(conn);
    }

    @Override
    public List<PreguntaSupervision> consultar(Connection connection) throws Exception {
        List<PreguntaSupervision> resul = new ArrayList<>();
        final String SQL_SELECT = "SELECT id, pregunta\n" +
                "FROM preguntas_supervision\n" +
                "ORDER BY 2 DESC;\n";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            rs = pst.executeQuery();
            while (rs.next()) {
                PreguntaSupervision preguntaSupervision = new PreguntaSupervision();
                preguntaSupervision.setId(rs.getLong(1));
                preguntaSupervision.setPregunta(rs.getString(2));
                resul.add(preguntaSupervision);
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
    public List<PreguntaSupervision> borrar(Connection connection, Long id) throws Exception {
        final String SQL_INSERT = "DELETE FROM preguntas_supervision\n" +
                "WHERE id = ?;\n";
        PreparedStatement pst = null;
        Connection conn = connection;
        try {
            pst = conn.prepareStatement(SQL_INSERT);
            pst.setLong(1, id);
            pst.execute();
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return consultar(conn);
    }

    @Override
    public Long grabarEncabezadoSupervision(Connection connection, RegistroSupervision registroSupervision) throws Exception {
        final String SQL_INSERT = "INSERT INTO encabezado_supervision\n" +
                "(fh_registro, nf_registro, promotor, supervisor, zona, informacion, firmado)\n" +
                "VALUES(?,?,?,?,?,?,?) RETURNING id;\n";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_INSERT);
            pst.setTimestamp(1, Timestamp.valueOf(Utilidades.getLocalDateTime()));
            pst.setLong(2, Utilidades.getFechaActualNumero(Utilidades.getLocalDateTime().toLocalDate()));
            pst.setLong(3, registroSupervision.getPromotor());
            pst.setLong(4, registroSupervision.getSupervisor());
            pst.setLong(5, registroSupervision.getZona());
            pst.setString(6, registroSupervision.getInformacion());
            pst.setBoolean(7, registroSupervision.isFirmado()!=true?false:true);
            rs = pst.executeQuery();
            while (rs.next()) {
                registroSupervision.setId(rs.getLong(1));
            }
        } catch (Exception ex) {
            if (ex.getMessage().contains("unique")) {
                throw new Exception("Ya se realizó el proceso de supervisión");
            }
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return registroSupervision.getId();
    }

    @Override
    public void grabarDetalleSupervision(Connection connection, RegistroSupervision registroSupervision) throws Exception {
        final String SQL_INSERT = "INSERT INTO detalle_supervision\n" +
                "(id_supervision, pregunta, cumple, valor)\n" +
                "VALUES(?,?,?,?) RETURNING id;\n";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;

        List<PreguntaSupervision> preguntas = registroSupervision.getPreguntas();
        Long idSupervision = registroSupervision.getId();
        for (PreguntaSupervision pregunta : preguntas) {
            try {
                pst = conn.prepareStatement(SQL_INSERT);
                pst.setLong(1, idSupervision);
                pst.setString(2, pregunta.getPregunta());
                pst.setBoolean(3, pregunta.isCumple() == null ? false : pregunta.isCumple());
                pst.setString(4, pregunta.getValor());
                rs = pst.executeQuery();
                while (rs.next()) {
                    pregunta.setId(rs.getLong(1));
                }
            } catch (Exception ex) {
            } finally {
                try {
                    pst.close();
                } catch (Exception ex) {
                }
            }
        }
    }

    /**
     * Determina si los datos de la firma son correctos y coloca el campo firmado
     * entrue o false retornando los cambios en el parametro enviado.
     * @param connection
     * @param registroSupervision
     * @throws Exception
     */
    @Override
    public void validarFirmaPromotor(Connection connection, RegistroSupervision registroSupervision) throws Exception {
        if (registroSupervision.getFirmaHash().isEmpty()) {
            registroSupervision.setFirmado(false);
            return;
        }
        boolean firmaValida = false;
        final String SQL_SELECT = "SELECT true \n" +
                "FROM usuario u \n" +
                "WHERE\n" +
                "u.clave_hash = ? \n" +
                "AND u.id = ? ;\n";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setString(1, registroSupervision.getFirmaHash());
            pst.setLong(2, registroSupervision.getPromotor());
            rs = pst.executeQuery();
            while (rs.next()) {
                firmaValida = rs.getBoolean(1);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        if (!firmaValida) {
            throw new Exception("Los datos para firmar la supervisión son incorrectos :(");
        }
        registroSupervision.setFirmado(firmaValida);
    }
}
