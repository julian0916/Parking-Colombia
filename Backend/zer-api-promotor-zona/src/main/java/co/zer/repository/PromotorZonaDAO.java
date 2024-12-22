package co.zer.repository;

import co.zer.model.Indice;
import co.zer.model.Paginacion;
import co.zer.model.PromotorZona;
import co.zer.utils.Utilidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PromotorZonaDAO implements IPromotorZonaDAO {

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
    public void ajustarContenido(PromotorZona promotorZona) {

        if (promotorZona.isActivo() == null) {
            promotorZona.setActivo(true);
        }
        if (promotorZona.isLunes() == null) {
            promotorZona.setLunes(false);
        }
        if (promotorZona.isMartes() == null) {
            promotorZona.setMartes(false);
        }
        if (promotorZona.isMiercoles() == null) {
            promotorZona.setMiercoles(false);
        }
        if (promotorZona.isJueves() == null) {
            promotorZona.setJueves(false);
        }
        if (promotorZona.isViernes() == null) {
            promotorZona.setViernes(false);
        }
        if (promotorZona.isSabado() == null) {
            promotorZona.setSabado(false);
        }
        if (promotorZona.isDomingo() == null) {
            promotorZona.setDomingo(false);
        }
        if (promotorZona.isActivo() == null) {
            promotorZona.setActivo(false);
        }
        //promotorZona.setDetalle(corregirEspacios(promotorZona.getDetalle()));
        //No se corrige porque modifica el formato
        //del área de texto.
    }

    @Override
    public void validarContenido(PromotorZona promotorZona) throws Exception {
        if (promotorZona == null) {
            throw new Exception("Debe ingresar los datos del promotor y la zona");
        }
        if (promotorZona.getPromotor() == null) {
            throw new Exception("Debe ingresar los datos del promotor");
        }
        if (promotorZona.getPromotor() < 1) {
            throw new Exception("Debe ingresar los datos del promotor");
        }
        if (promotorZona.getZona() == null) {
            throw new Exception("Debe ingresar los datos de la zona");
        }
        if (promotorZona.getZona() < 1) {
            throw new Exception("Debe ingresar los datos de la zona");
        }
        if (promotorZona.getDetalle().length() > 0 && (promotorZona.getDetalle().length() < 3 || promotorZona.getDetalle().length() > 1000)) {
            throw new Exception("El detalle puede tener minimo 3 caracteres y máximo 1000");
        }
    }

    @Override
    public PromotorZona guardar(Connection connection, PromotorZona promotorZona) throws Exception {
        if (connection == null) {
            throw new Exception("La conexión no está disponible");
        }
        boolean esActualizar = promotorZona.getId() != null && promotorZona.getId() > 0;
        ajustarContenido(promotorZona);
        validarContenido(promotorZona);
        if (esActualizar) {
            return actualizar(connection, promotorZona);
        }
        return insertar(connection, promotorZona);
    }

    @Override
    public PromotorZona insertar(Connection connection, PromotorZona promotorZona) throws Exception {
        final String SQL_INSERT = "INSERT INTO promotor_zona\n" +
                "(promotor, zona, lunes, martes, miercoles, jueves, viernes, sabado, domingo, detalle, activo)\n" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n RETURNING id;";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_INSERT);
            pst.setLong(1, promotorZona.getPromotor());
            pst.setLong(2, promotorZona.getZona());
            pst.setBoolean(3, promotorZona.isLunes());
            pst.setBoolean(4, promotorZona.isMartes());
            pst.setBoolean(5, promotorZona.isMiercoles());
            pst.setBoolean(6, promotorZona.isJueves());
            pst.setBoolean(7, promotorZona.isViernes());
            pst.setBoolean(8, promotorZona.isSabado());
            pst.setBoolean(9, promotorZona.isDomingo());
            pst.setString(10, promotorZona.getDetalle());
            pst.setBoolean(11, promotorZona.isActivo());
            rs = pst.executeQuery();
            while (rs.next()) {
                promotorZona.setId(rs.getLong(1));
            }
        } catch (Exception ex) {
            if (ex.getMessage().contains("unique")) {
                throw new Exception("Ya existe el promotor en la zona seleccionada");
            }
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return promotorZona;
    }

    @Override
    public PromotorZona actualizar(Connection connection, PromotorZona promotorZona) throws Exception {
        final String SQL_UPDATE = "UPDATE promotor_zona\n" +
                "SET promotor=?, zona=?, lunes=?, martes=?, miercoles=?, jueves=?, viernes=?, sabado=?, domingo=?, detalle=?, activo=?\n" +
                "WHERE id=?;\n";
        PreparedStatement pst = null;
        Connection conn = connection;
        try {
            pst = conn.prepareStatement(SQL_UPDATE);
            pst.setLong(1, promotorZona.getPromotor());
            pst.setLong(2, promotorZona.getZona());
            pst.setBoolean(3, promotorZona.isLunes());
            pst.setBoolean(4, promotorZona.isMartes());
            pst.setBoolean(5, promotorZona.isMiercoles());
            pst.setBoolean(6, promotorZona.isJueves());
            pst.setBoolean(7, promotorZona.isViernes());
            pst.setBoolean(8, promotorZona.isSabado());
            pst.setBoolean(9, promotorZona.isDomingo());
            pst.setString(10, promotorZona.getDetalle());
            pst.setBoolean(11, promotorZona.isActivo());
            pst.setLong(12, promotorZona.getId());
            long result = pst.executeUpdate();
            if (result < 1) {
                throw new Exception("El promotor en la zona no fue actualizado");
            }
        } catch (Exception ex) {
            if (ex.getMessage().contains("unique")) {
                throw new Exception("Ya existe el promotor en la zona seleccionada");
            }
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return promotorZona;
    }

    @Override
    public List<Object> listarPromotoresZonas(Connection connection, Paginacion paginacion) throws Exception {
        List<Object> zonas = new ArrayList<>();
        final String SQL_FROM_WHERE_COUNT =
                "FROM promotor_zona c,\n" +
                        "usuario u,\n" +
                        "zona z\n" +
                        "WHERE \n" +
                        "(CASE WHEN c.activo THEN 'activo' ELSE 'borrado' END  LIKE ?\n" +
                        "OR CASE WHEN c.lunes THEN 'lunes' ELSE '-' END  LIKE ?\n" +
                        "OR CASE WHEN c.martes THEN 'martes' ELSE '-' END  LIKE ?\n" +
                        "OR CASE WHEN c.miercoles THEN 'miercoles' ELSE '-' END  LIKE ?\n" +
                        "OR CASE WHEN c.jueves THEN 'jueves' ELSE '-' END  LIKE ?\n" +
                        "OR CASE WHEN c.viernes THEN 'viernes' ELSE '-' END  LIKE ?\n" +
                        "OR CASE WHEN c.sabado THEN 'sabado' ELSE '-' END  LIKE ?\n" +
                        "OR CASE WHEN c.domingo THEN 'domingo' ELSE '-' END  LIKE ?\n" +
                        "OR LOWER(c.detalle)  LIKE ? \n" +
                        "OR LOWER(u.nombre1)  LIKE ? \n" +
                        "OR LOWER(u.nombre2)  LIKE ? \n" +
                        "OR LOWER(u.apellido1)  LIKE ? \n" +
                        "OR LOWER(u.apellido2)  LIKE ? \n" +
                        "OR LOWER(z.nombre)  LIKE ? )\n" +
                        "AND u.id = c.promotor \n" +
                        "AND z.id = c.zona \n";

        final String SQL_COUNT = "SELECT count(*)\n" + SQL_FROM_WHERE_COUNT;
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
            pst.setString(5, filtro);
            pst.setString(6, filtro);
            pst.setString(7, filtro);
            pst.setString(8, filtro);
            pst.setString(9, filtro);
            pst.setString(10, filtro);
            pst.setString(11, filtro);
            pst.setString(12, filtro);
            pst.setString(13, filtro);
            pst.setString(14, filtro);

            rs = pst.executeQuery();
            paginacion.setTotal(0L);
            while (rs.next()) {
                paginacion.setTotal(rs.getLong(1));
            }
            paginacion.setColumnas("c.id, promotor, zona, lunes, martes, miercoles, jueves, viernes, sabado, domingo, detalle, c.activo, nombre1, nombre2, apellido1, apellido2, z.nombre\n");
            String ORDER_BY = Utilidades.sentenciaOrdenar(paginacion) + "\n";
            final String SQL_SELECT = "SELECT " + paginacion.getColumnas() + "\n" +
                    SQL_FROM_WHERE_COUNT +
                    ORDER_BY +
                    "LIMIT ?\n" +
                    "OFFSET ?;";
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setString(1, filtro);
            pst.setString(2, filtro);
            pst.setString(3, filtro);
            pst.setString(4, filtro);
            pst.setString(5, filtro);
            pst.setString(6, filtro);
            pst.setString(7, filtro);
            pst.setString(8, filtro);
            pst.setString(9, filtro);
            pst.setString(10, filtro);
            pst.setString(11, filtro);
            pst.setString(12, filtro);
            pst.setString(13, filtro);
            pst.setString(14, filtro);
            pst.setLong(15, paginacion.getLimite());
            pst.setLong(16, (paginacion.getActual() - 1) * paginacion.getLimite());
            rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, Object> zona = new HashMap<>();
                String[] columnas = paginacion.getColumnas().split(",");
                int indice = 1;
                for (String columna : columnas) {
                    zona.put(columna.replace('.', '_').trim(), rs.getObject(indice));
                    indice++;
                }
                zonas.add(zona);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return zonas;
    }

    @Override
    public List<Object> listarZonasAsignadasPromotor(Connection connection, String correoPromotor) throws Exception {
        List<Object> zonas = new ArrayList<>();
        final String COLUMNAS = "c.id, promotor, zona, lunes, martes, " +
                "miercoles, jueves, viernes, sabado, domingo, detalle, " +
                "c.activo, nombre1, nombre2, apellido1, apellido2, z.nombre, z.valor_hora_carro, " +
                "z.valor_hora_moto, z.celdas_carro, z.celdas_moto, z.minutos_gracia, z.minutos_para_nueva_gracia \n";
        final String SQL_SELECT = "SELECT " + COLUMNAS + "\n" +
                "FROM usuario u,\n" +
                "promotor_zona c, \n" +
                "zona z\n" +
                "WHERE \n" +
                "z.id = c.zona\n" +
                "AND u.perfil = ?\n" +
                "AND c.activo = true \n" +
                "AND c.promotor = u.id\n" +
                "AND u.correo = ? \n" +
                "UNION DISTINCT\n" +
                "SELECT 0, u.id, z.id, true, true, \n" +
                "true, true, true, true, true, '', \n" +
                "u.activo, nombre1, nombre2, apellido1, apellido2, z.nombre, z.valor_hora_carro, " +
                "z.valor_hora_moto, z.celdas_carro, z.celdas_moto, z.minutos_gracia, z.minutos_para_nueva_gracia \n"+
                "FROM usuario u,\n" +
                "zona z\n" +
                "WHERE \n" +
                "u.activo = true \n" +
                "AND u.perfil = ?\n" +
                "AND u.correo = ? \n" +
                "ORDER BY 17";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setLong(1, Utilidades.PERFIL_PROMOTOR);
            pst.setString(2, correoPromotor);
            pst.setLong(3, Utilidades.PERFIL_SUPERVISOR);
            pst.setString(4, correoPromotor);
            rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, Object> zona = new HashMap<>();
                int indice = 1;
                for (String columna : COLUMNAS.split(",")) {
                    zona.put(columna.replace('.', '_').trim(), rs.getObject(indice));
                    indice++;
                }
                zonas.add(zona);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return zonas;
    }

    @Override
    public List<Object> listarPromotoresAsignadoAZona(Connection connection, Long zonaId) throws Exception {
        List<Object> zonas = new ArrayList<>();
        final String COLUMNAS = "c.id, promotor, zona, lunes, martes, " +
                "miercoles, jueves, viernes, sabado, domingo, detalle, " +
                "c.activo, nombre1, nombre2, apellido1, apellido2, z.nombre";
        final String SQL_SELECT = "SELECT " + COLUMNAS + "\n" +
                "FROM usuario u,\n" +
                "promotor_zona c, \n" +
                "zona z\n" +
                "WHERE \n" +
                "CASE \n" +
                "WHEN ?= 1 THEN lunes = true\n"+
                "WHEN ?= 2 THEN martes = true\n"+
                "WHEN ?= 3 THEN miercoles = true\n"+
                "WHEN ?= 4 THEN jueves = true\n"+
                "WHEN ?= 5 THEN viernes = true\n"+
                "WHEN ?= 6 THEN sabado = true\n"+
                "WHEN ?= 7 THEN domingo = true\n" +
                "END\n"+
                "AND c.activo = true \n" +
                "AND u.id = c.promotor \n" +
                "AND c.zona = z.id\n" +
                "AND z.id = ? \n" +
                "ORDER BY nombre1, nombre2, apellido1, apellido2 " ;
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs;
        try {
            Indice indice = new Indice();
            int diaSemanaActual=Utilidades.getDiaActualNumero();

            pst = conn.prepareStatement(SQL_SELECT);
            pst.setLong(indice.siguiente(), diaSemanaActual);
            pst.setLong(indice.siguiente(), diaSemanaActual);
            pst.setLong(indice.siguiente(), diaSemanaActual);
            pst.setLong(indice.siguiente(), diaSemanaActual);
            pst.setLong(indice.siguiente(), diaSemanaActual);
            pst.setLong(indice.siguiente(), diaSemanaActual);
            pst.setLong(indice.siguiente(), diaSemanaActual);
            pst.setLong(indice.siguiente(), zonaId);
            rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, Object> zona = new HashMap<>();
                int pos = 1;
                for (String columna : COLUMNAS.split(",")) {
                    zona.put(columna.replace('.', '_').trim(), rs.getObject(pos));
                    pos++;
                }
                zonas.add(zona);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return zonas;
    }
}
