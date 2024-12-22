package co.zer.repository;

import co.zer.model.Paginacion;
import co.zer.model.Usuario;
import co.zer.utils.Utilidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsuarioDAO implements IUsuarioDAO {

    /**
     * Coloca en tipo título un texto
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
     * @param contenido
     * @return
     */
    private String quitarEspacios(String contenido){
        if(contenido==null)
            return "";
        return contenido.replaceAll("\\s","");
    }

    /**
     * Deja un solo espacio en blanco
     * @param contenido
     * @return
     */
    private String corregirEspacios(String contenido){
        if(contenido==null)
            return "";
        return contenido.trim().replaceAll("[\\s]{2,1000}"," ");
    }

    /**
     * Valida la expresión del correo
     * para que sea lógica con la estructura
     * caracteres@caracteres usando .-_ en el
     * nombre y el dominio
     * @param contenido
     * @return
     */
    private boolean validarCorreo(String contenido){
        Pattern pat = Pattern.compile("[\\w.-_]+@[\\w.-_]+.[\\w-_]+");
        Matcher mat = pat.matcher(contenido);
        return mat.matches();
    }

    @Override
    public void ajustarContenido(Usuario usuario) {

        if (usuario.isActivo() == null) {
            usuario.setActivo(true);
        }
        usuario.setNombre1(capitalizar(usuario.getNombre1()));
        usuario.setNombre2(capitalizar(usuario.getNombre2()));
        usuario.setApellido1(capitalizar(usuario.getApellido1()));
        usuario.setApellido2(capitalizar(usuario.getApellido2()));
        usuario.setIdentificacion(quitarEspacios(usuario.getIdentificacion()));
        usuario.setCorreo(quitarEspacios(usuario.getCorreo()).toLowerCase());
        usuario.setCelular(corregirEspacios(usuario.getCelular()));
        usuario.setFijo(corregirEspacios(usuario.getFijo()));
        usuario.setDireccion(corregirEspacios(usuario.getDireccion()));
        //usuario.setMasInformacion(corregirEspacios(usuario.getMasInformacion()));
        //No se aplica la corrección de espacios porque retira el
        //formato para las textarea y los enter involucrados.

    }

    @Override
    public void validarContenido(Usuario usuario) throws Exception {
        if (usuario == null) {
            throw new Exception("Debe ingresar los datos del usuario");
        }
        if (usuario.getNombre1().length() < 3 || usuario.getNombre1().length() > 50) {
            throw new Exception("El primer nombre debe tener minimo 3 caracteres y máximo 50");
        }
        if (usuario.getNombre2().length() > 0 && (usuario.getNombre2().length() < 3 || usuario.getNombre2().length() > 50)) {
            throw new Exception("El segundo nombre puede tener minimo 3 caracteres y máximo 50");
        }
        if (usuario.getApellido1().length() < 3 || usuario.getApellido1().length() > 50) {
            throw new Exception("El primer apellido debe tener minimo 3 caracteres y máximo 50");
        }
        if ( usuario.getApellido2().length() > 0 && (usuario.getApellido2().length() < 3 || usuario.getApellido2().length() > 50)) {
            throw new Exception("El segundo apellido puede tener minimo 3 caracteres y máximo 50");
        }
        if(usuario.getTipoIdentificacion()==null){
            throw new Exception("Debe seleccionar el tipo de identificacion");
        }
        if(usuario.getTipoIdentificacion()<1){
            throw new Exception("Debe seleccionar el tipo de identificacion");
        }
        if (usuario.getIdentificacion().length() < 6 || usuario.getIdentificacion().length() > 200) {
            throw new Exception("El número de identificación debe contener mínimo 6 caracteres");
        }
        if(usuario.getPerfil()==null){
            throw new Exception("Debe seleccionar el perfil");
        }
        if(usuario.getPerfil()<1){
            throw new Exception("Debe seleccionar el perfil");
        }
        if(!validarCorreo(usuario.getCorreo())){
            throw new Exception("Debe ingresar un correo electrónico válido");
        }
        if ( usuario.getCelular().length() > 50) {
            throw new Exception("El celular no debe superar los 50 caracteres");
        }
        if ( usuario.getFijo().length() > 50) {
            throw new Exception("El fijo no debe superar los 50 caracteres");
        }
        if ( usuario.getDireccion().length() > 50) {
            throw new Exception("La dirección no debe superar los 50 caracteres");
        }
        if ( usuario.getMasInformacion().length() > 500) {
            throw new Exception("Más información no debe superar los 500 caracteres");
        }

    }

    @Override
    public Usuario guardar(Connection connection, Usuario usuario) throws Exception {
        if (connection == null) {
            throw new Exception("La conexión no está disponible");
        }
        boolean esActualizar = usuario.getId() != null && usuario.getId() > 0;
        ajustarContenido(usuario);
        validarContenido(usuario);
        if (esActualizar) {
            return actualizar(connection, usuario);
        }
        return insertar(connection, usuario);
    }

    @Override
    public Usuario insertar(Connection connection, Usuario usuario) throws Exception {
        final String SQL_INSERT = "INSERT INTO usuario\n" +
                "(perfil, tipo_identificacion, identificacion, correo, nombre1, nombre2, apellido1, apellido2, clave_encriptada, clave_hash, celular, fijo, direccion, mas_informacion, activo)\n" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_INSERT);
            pst.setLong(1, usuario.getPerfil());
            pst.setLong(2, usuario.getTipoIdentificacion());
            pst.setString(3, usuario.getIdentificacion());
            pst.setString(4, usuario.getCorreo());
            pst.setString(5, usuario.getNombre1());
            pst.setString(6, usuario.getNombre2());
            pst.setString(7, usuario.getApellido1());
            pst.setString(8, usuario.getApellido2());
            pst.setString(9, usuario.getClaveEncriptada());
            pst.setString(10, usuario.getClaveHash());
            pst.setString(11, usuario.getCelular());
            pst.setString(12, usuario.getFijo());
            pst.setString(13, usuario.getDireccion());
            pst.setString(14, usuario.getMasInformacion());
            pst.setBoolean(15, usuario.isActivo());
            rs = pst.executeQuery();
            while (rs.next()) {
                usuario.setId(rs.getLong(1));
            }
        } catch (Exception ex) {
            if(ex.getMessage().contains("unique")){
                throw new Exception("El correo o la identificación ya fueron asignadas a otro usuario :(");
            }
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return usuario;
    }

    @Override
    public Usuario actualizar(Connection connection, Usuario usuario) throws Exception {
        final String SQL_UPDATE = "UPDATE usuario\n" +
                "SET perfil=?, tipo_identificacion=?, identificacion=?, \n" +
                "correo=?, nombre1=?, nombre2=?, apellido1=?, apellido2=?,\n" +
                " clave_encriptada=?, clave_hash=?, celular=?, fijo=?, \n" +
                "direccion=?, mas_informacion=?, activo=?\n" +
                "WHERE id=?;\n";
        PreparedStatement pst = null;
        Connection conn = connection;
        try {
            pst = conn.prepareStatement(SQL_UPDATE);
            pst.setLong(1, usuario.getPerfil());
            pst.setLong(2, usuario.getTipoIdentificacion());
            pst.setString(3, usuario.getIdentificacion());
            pst.setString(4, usuario.getCorreo());
            pst.setString(5, usuario.getNombre1());
            pst.setString(6, usuario.getNombre2());
            pst.setString(7, usuario.getApellido1());
            pst.setString(8, usuario.getApellido2());
            pst.setString(9, usuario.getClaveEncriptada());
            pst.setString(10, usuario.getClaveHash());
            pst.setString(11, usuario.getCelular());
            pst.setString(12, usuario.getFijo());
            pst.setString(13, usuario.getDireccion());
            pst.setString(14, usuario.getMasInformacion());
            pst.setBoolean(15, usuario.isActivo());
            pst.setLong(16, usuario.getId());
            long result = pst.executeUpdate();
            if (result < 1) {
                throw new Exception("El usuario no fue actualizado");
            }
        } catch (Exception ex) {
            if(ex.getMessage().contains("unique")){
                throw new Exception("El correo o la identificación ya fueron asignadas a otro usuario :(");
            }
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return usuario;
    }

    @Override
    public List<Object> listarUsuarios(Connection connection, Paginacion paginacion) throws Exception {
        List<Object> usuarios = new ArrayList<>();
        final String SQL_FROM_WHERE_COUNT =
                "FROM usuario c,\n" +
                        "cuentas.perfil p,\n" +
                        "cuentas.tipo_identificacion t\n" +
                        "WHERE \n" +
                        "(LOWER(t.nombre_largo)  LIKE ?\n" +
                        "OR LOWER(t.nombre_corto)  LIKE ?\n" +
                        "OR LOWER(p.nombre)  LIKE ?\n" +
                        "OR LOWER(cast(c.id as varchar))  LIKE ?\n" +
                        "OR CASE WHEN c.activo THEN 'activo' ELSE 'borrado' END  LIKE ?\n" +
                        "OR LOWER(c.nombre1)  LIKE ?\n" +
                        "OR LOWER(c.nombre2)  LIKE ?\n" +
                        "OR LOWER(c.apellido1)  LIKE ?\n" +
                        "OR LOWER(c.apellido2)  LIKE ?\n" +
                        "OR LOWER(c.celular)  LIKE ?\n" +
                        "OR LOWER(c.fijo)  LIKE ?\n" +
                        "OR LOWER(c.direccion)  LIKE ?\n" +
                        "OR LOWER(c.mas_informacion)  LIKE ?\n" +
                        "OR LOWER(c.identificacion)  LIKE ?\n" +
                        "OR LOWER(cast(c.perfil as varchar))  LIKE ?\n" +
                        "OR LOWER(c.correo) LIKE ? )\n" +
                        "AND p.id=c.perfil\n" +
                        "AND t.id=c.tipo_identificacion\n";
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
            pst.setString(15, filtro);
            pst.setString(16, filtro);
            rs = pst.executeQuery();
            paginacion.setTotal(0L);
            while (rs.next()) {
                paginacion.setTotal(rs.getLong(1));
            }
            paginacion.setColumnas("c.id, perfil, tipo_identificacion, identificacion, correo, nombre1, nombre2, apellido1, apellido2, clave_encriptada, clave_hash, celular, fijo, direccion, mas_informacion, c.activo, p.nombre, t.nombre_corto");
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
            pst.setString(15, filtro);
            pst.setString(16, filtro);
            pst.setLong(17, paginacion.getLimite());
            pst.setLong(18, (paginacion.getActual() - 1) * paginacion.getLimite());
            rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, Object> usuario = new HashMap<>();
                String[] columnas = paginacion.getColumnas().split(",");
                int indice=1;
                for (String columna : columnas) {
                    usuario.put(columna.replace('.','_').trim(), rs.getObject(indice));
                    indice++;
                }
                usuarios.add(usuario);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return usuarios;
    }

    @Override
    public List<Object> listarDocumentos(Connection connection) throws Exception {
        List<Object> usuarios = new ArrayList<>();
        final String SQL_SELECT = "SELECT id, nombre_corto, nombre_largo, activo\n" +
                "FROM tipo_identificacion\n" +
                "WHERE activo=True\n" +
                "ORDER BY 2;\n";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, Object> acceso = new HashMap<>();
                acceso.put("id", rs.getLong(1));
                acceso.put("nombreCorto", rs.getString(2));
                acceso.put("nombreLargo", rs.getString(3));
                acceso.put("activo", rs.getBoolean(4));
                usuarios.add(acceso);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return usuarios;
    }

    @Override
    public List<Object> listarPerfiles(Connection connection) throws Exception {
        List<Object> perfiles = new ArrayList<>();
        final String SQL_SELECT = "SELECT id, nombre, descripcion, activo\n" +
                "FROM perfil\n" +
                "WHERE activo=True\n" +
                "ORDER BY 2;\n";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, Object> acceso = new HashMap<>();
                acceso.put("id", rs.getLong(1));
                acceso.put("nombre", rs.getString(2));
                acceso.put("descripcion", rs.getString(3));
                acceso.put("activo", rs.getBoolean(4));
                perfiles.add(acceso);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return perfiles;
    }
}
