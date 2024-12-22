package co.zer.repository;

import co.zer.model.Tiquete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfiguracionDAO implements IConfiguracionDAO {

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
    public void ajustarContenido(Tiquete tiquete) {
        tiquete.setId(ID);
        tiquete.setNit(quitarEspacios(tiquete.getNit()));
        tiquete.setLema(corregirEspacios(tiquete.getLema()));
        tiquete.setNombre(corregirEspacios(tiquete.getNombre()));
        tiquete.setTerminos(tiquete.getTerminos().trim());
        tiquete.setContenido(tiquete.getContenido().trim());
    }

    @Override
    public void validarContenido(Tiquete tiquete) throws Exception {
        if (tiquete == null) {
            throw new Exception("Debe ingresar los datos del promotor y la zona");
        }
        if (tiquete.getNit().length() < 3 || tiquete.getNit().length() > 20) {
            throw new Exception("El NIT debe tener mínimo 3 y máximo 20 caracteres");
        }
        if (tiquete.getNombre().length() < 3 || tiquete.getNombre().length() > 100) {
            throw new Exception("El nombre debe tener mínimo 3 y máximo 100 caracteres");
        }
        if (tiquete.getLema().length() < 3 || tiquete.getLema().length() > 100) {
            throw new Exception("El lema debe tener mínimo 3 y máximo 100 caracteres");
        }
        if (tiquete.getTerminos().length() < 3 || tiquete.getTerminos().length() > 2000) {
            throw new Exception("Los términos deben tener mínimo 3 y máximo 2000 caracteres");
        }
    }

    @Override
    public Tiquete guardar(Connection connection, Tiquete tiquete) throws Exception {
        if (connection == null) {
            throw new Exception("La conexión no está disponible");
        }
        boolean esActualizar = tiquete.getId() != null && tiquete.getId() == ID;
        ajustarContenido(tiquete);
        validarContenido(tiquete);
        if (esActualizar) {
            return actualizar(connection, tiquete);
        }
        return insertar(connection, tiquete);
    }

    @Override
    public Tiquete insertar(Connection connection, Tiquete tiquete) throws Exception {
        final String SQL_INSERT = "INSERT INTO tiquete\n" +
                "(id, nit, nombre, lema, terminos, contenido)\n" +
                "VALUES(?, ?, ?, ?, ?, ?);\n";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_INSERT);
            pst.setLong(1, tiquete.getId());
            pst.setString(2, tiquete.getNit());
            pst.setString(3, tiquete.getNombre());
            pst.setString(4, tiquete.getLema());
            pst.setString(5, tiquete.getTerminos());
            pst.setString(6, tiquete.getContenido());
        } catch (Exception ex) {
            if (ex.getMessage().contains("unique")) {
                throw new Exception("Ya existe el código del tiquete");
            }
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return tiquete;
    }

    @Override
    public Tiquete actualizar(Connection connection, Tiquete tiquete) throws Exception {
        final String SQL_UPDATE = "UPDATE tiquete\n" +
                "SET nit=?, nombre=?, lema=?, terminos=?, contenido=?\n" +
                "WHERE id=?;\n";
        PreparedStatement pst = null;
        Connection conn = connection;
        try {
            pst = conn.prepareStatement(SQL_UPDATE);
            pst.setString(1, tiquete.getNit());
            pst.setString(2, tiquete.getNombre());
            pst.setString(3, tiquete.getLema());
            pst.setString(4, tiquete.getTerminos());
            pst.setString(5, tiquete.getContenido());
            pst.setLong(6, tiquete.getId());
            long result = pst.executeUpdate();
            if (result < 1) {
                throw new Exception("El tiquete no fue actualizado");
            }
        } catch (Exception ex) {
            if (ex.getMessage().contains("unique")) {
                throw new Exception("Ya existe el código del tiquete");
            }
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return tiquete;
    }

    @Override
    public Tiquete consultar(Connection connection) throws Exception {
        Tiquete tiquete = null;
        final String SQL_SELECT = "SELECT id, nit, nombre, lema, terminos, contenido\n" +
                "FROM tiquete \n" +
                "WHERE id = ? ;";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setLong(1, ID);
            rs = pst.executeQuery();
            while (rs.next()) {
                tiquete = new Tiquete();
                tiquete.setId(ID);
                tiquete.setNit(rs.getString(2));
                tiquete.setNombre(rs.getString(3));
                tiquete.setLema(rs.getString(4));
                tiquete.setTerminos(rs.getString(5));
                tiquete.setContenido(rs.getString(6));
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return tiquete;
    }

    @Override
    public void guardarLimiteEndeudamientoPromotor(Connection connection, Long valor) throws Exception {
        final String SQL_UPDATE = "UPDATE configuracion\n" +
                "SET limite_endeudamiento_promotor=?\n" +
                "WHERE id= ? ;";
        PreparedStatement pst = null;
        Connection conn = connection;
        try {
            pst = conn.prepareStatement(SQL_UPDATE);
            pst.setLong(1, Math.abs(valor));
            pst.setLong(2, IConfiguracionDAO.ID);
            long result = pst.executeUpdate();
            if (result < 1) {
                throw new Exception("El valor no fue guardado");
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
    public Long getLimiteEndeudamientoPromotor(Connection connection) throws Exception {
        Long resultado = null;
        final String SQL_SELECT = "SELECT limite_endeudamiento_promotor\n" +
                "FROM configuracion\n" +
                "WHERE id= ?";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setLong(1, ID);
            rs = pst.executeQuery();
            while (rs.next()) {
                resultado = rs.getLong(1);
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
