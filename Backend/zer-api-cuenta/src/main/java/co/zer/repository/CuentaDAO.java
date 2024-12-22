package co.zer.repository;

import co.zer.model.Cuenta;
import co.zer.model.Paginacion;
import co.zer.utils.Utilidades;
import co.zer.utils.Uuid;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CuentaDAO implements ICuentaDAO {

    @Override
    public void ajustarContenido(Cuenta cuenta) {
        cuenta.setNombre(cuenta.getNombre().trim());
        if (cuenta.isActivo() == null) {
            cuenta.setActivo(true);
        }
        if (cuenta.getId() == null) {
            cuenta.setId(Uuid.getUuidTexto());
        }
        if (cuenta.getId().trim().length() < 1) {
            cuenta.setId(Uuid.getUuidTexto());
        }
    }

    @Override
    public void validarContenido(Cuenta cuenta) throws Exception {
        if (cuenta == null) {
            throw new Exception("Debe ingresar los datos");
        }
        if (cuenta.getNombre() != null && (cuenta.getNombre().length() < 5 || cuenta.getNombre().length() > 200)) {
            throw new Exception("El nombre debe tener minimo 5 caracteres y máximo 200");
        }
        if (cuenta.getId() != null && cuenta.getId().length() > 220) {
            throw new Exception("El schema debe tener máximo 220 caracteres");
        }
        if (cuenta.getInformacion() != null && cuenta.getInformacion().length() > 400) {
            throw new Exception("La información debe tener máximo 400 caracteres");
        }
    }

    @Override
    public Cuenta guardar(Connection connection, Cuenta cuenta) throws Exception {
        if (connection == null) {
            throw new Exception("La conexión no está disponible");
        }
        boolean esActualizar = cuenta.getId() != null && cuenta.getId().trim().length() > 0;
        validarContenido(cuenta);
        ajustarContenido(cuenta);
        if (esActualizar) {
            return actualizar(connection, cuenta);
        }
        return insertar(connection, cuenta);
    }

    @Override
    public Cuenta insertar(Connection connection, Cuenta cuenta) throws Exception {
        final String SQL_INSERT = "INSERT INTO cuenta\n" +
                "(id, nombre, informacion, activo)\n" +
                "VALUES(?, ?, ?, ?) RETURNING id;";
        final String SQL_CREATE_SCHEMA = "SELECT crear_cuenta (?);";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_INSERT);
            pst.setString(1, cuenta.getId());
            pst.setString(2, cuenta.getNombre());
            pst.setString(3, cuenta.getInformacion());
            pst.setBoolean(4, cuenta.isActivo());
            pst.executeQuery();
            pst = conn.prepareStatement(SQL_CREATE_SCHEMA);
            pst.setString(1, cuenta.getId());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getBoolean(1) != true) {
                    conn.rollback();
                    throw new Exception("Ocurrió un error en la generación de la cuenta " + cuenta.getId() + " " + cuenta.getNombre());
                }
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return cuenta;
    }

    @Override
    public Cuenta actualizar(Connection connection, Cuenta cuenta) throws Exception {
        final String SQL_UPDATE = "UPDATE cuenta\n" +
                "SET  nombre=?, informacion=?, activo=?\n" +
                "WHERE id=?;";
        PreparedStatement pst = null;
        Connection conn = connection;
        try {
            pst = conn.prepareStatement(SQL_UPDATE);
            pst.setString(1, cuenta.getNombre());
            pst.setString(2, cuenta.getInformacion());
            pst.setBoolean(3, cuenta.isActivo());
            pst.setString(4, cuenta.getId());
            long result = pst.executeUpdate();
            if (result < 1) {
                throw new Exception("La cuenta no fue actualizada");
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return cuenta;
    }

    @Override
    public List<Cuenta> listarCuentas(Connection connection, Paginacion paginacion) throws Exception {
        List<Cuenta> cuentas = new ArrayList<>();
        final String SQL_COUNT = "SELECT count(*)\n" +
                "FROM cuenta c\n" +
                "WHERE \n" +
                "LOWER(c.id)  LIKE ?\n" +
                "OR CASE WHEN c.activo THEN 'activa' ELSE 'borrada' END  LIKE ?\n" +
                "OR LOWER(c.nombre)  LIKE ?\n" +
                "OR LOWER(c.informacion) LIKE ? ;";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            String filtro = Utilidades.filtroLike(paginacion);
            pst = conn.prepareStatement(SQL_COUNT);
            pst.setString(1, filtro);
            pst.setString(2, filtro);
            pst.setString(3, filtro);
            pst.setString(4, filtro);
            rs = pst.executeQuery();
            paginacion.setTotal(0L);
            while (rs.next()) {
                paginacion.setTotal(rs.getLong(1));
            }
            paginacion.setColumnas("id,nombre,informacion,activo");
            String ORDER_BY = Utilidades.sentenciaOrdenar(paginacion) + "\n";
            final String SQL_SELECT = "SELECT c.id,c.nombre,c.informacion,c.activo\n" +
                    "FROM cuenta c\n" +
                    "WHERE \n" +
                    "LOWER(c.id)  LIKE ?\n" +
                    "OR CASE WHEN c.activo THEN 'activa' ELSE 'borrada' END  LIKE ?\n" +
                    "OR LOWER(c.nombre)  LIKE ?\n" +
                    "OR LOWER(c.informacion) LIKE ? " +
                    ORDER_BY +
                    "LIMIT ?\n" +
                    "OFFSET ?;";
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setString(1, filtro);
            pst.setString(2, filtro);
            pst.setString(3, filtro);
            pst.setString(4, filtro);
            pst.setLong(5, paginacion.getLimite());
            pst.setLong(6, (paginacion.getActual() - 1) * paginacion.getLimite());
            rs = pst.executeQuery();
            while (rs.next()) {
                Cuenta cuenta = new Cuenta();
                cuenta.setId(rs.getString(1));
                cuenta.setNombre(rs.getString(2));
                cuenta.setInformacion(rs.getString(3));
                cuenta.setActivo(rs.getBoolean(4));
                cuentas.add(cuenta);
            }

        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return cuentas;
    }

    @Override
    public List<Object> listarCuentasUsuario(Connection connection, String usuario) throws Exception {
        List<Object> cuentas = new ArrayList<>();
        final String SQL_SELECT = "SELECT a.perfil, p.nombre, c.id, c.nombre, c.informacion, c.activo, a.correo\n" +
                "FROM \n" +
                "acceso a,\n" +
                "cuenta c,\n" +
                "perfil p\n" +
                "WHERE\n" +
                "p.id = a.perfil \n" +
                "AND a.activo = TRUE\n" +
                "AND c.id = a.cuenta\n" +
                "AND a.activo = TRUE\n" +
                "AND a.correo = ?\n" +
                "UNION \n" +
                "SELECT ?,(select p2.nombre from perfil p2 where id=?), c.id, c.nombre, c.informacion, c.activo, ?\n" +
                "FROM \n" +
                "cuenta c\n" +
                "WHERE\n" +
                "EXISTS (select 1 from acceso a where a.perfil=? and a.correo = ?)\n" +
                "ORDER BY 4;";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            final long PERFIL_CUENTAS = 100;//corresponde al super administrador de las cuentas
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setString(1, usuario);
            pst.setLong(2, PERFIL_CUENTAS);
            pst.setLong(3, PERFIL_CUENTAS);
            pst.setString(4, usuario);
            pst.setLong(5, PERFIL_CUENTAS);
            pst.setString(6, usuario);
            rs = pst.executeQuery();
            while (rs.next()) {
                Map<String,Object> acceso= new HashMap<>();
                acceso.put("idPerfil",rs.getLong(1));
                acceso.put("perfil",rs.getString(2));
                acceso.put("cuenta",rs.getString(3));
                acceso.put("nombre",rs.getString(4));
                acceso.put("informacion",rs.getString(5));
                acceso.put("activo",rs.getBoolean(6));
                acceso.put("correo",rs.getString(7));
                cuentas.add(acceso);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return cuentas;
    }
}
