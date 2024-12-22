package co.zer.repository;

import co.zer.model.Paginacion;
import co.zer.model.Zona;
import co.zer.utils.Utilidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZonaDAO implements IZonaDAO {

    /**
     * Valida que el formato de la hora sea hh:mm:ss
     *
     * @param contenido
     * @return
     */
    private boolean validarHora(String contenido) {
        Pattern pat = Pattern.compile("\\d\\d:\\d\\d:\\d\\d");
        Matcher mat = pat.matcher(contenido);
        return mat.matches();
    }

    /**
     * Convierte un texto hh:mm:ss en Time
     *
     * @param contenido
     * @return
     * @throws Exception
     */
    private Time getTiempo(String contenido) throws Exception {
        if (!this.validarHora(contenido)) {
            throw new Exception("El formato de la hora debe ser hh:mm:ss");
        }
        return Time.valueOf(contenido);
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
        return contenido.trim().replaceAll("\\s+", " ");
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
    public void ajustarContenido(Zona zona) throws Exception {

        if (zona.isActivo() == null) {
            zona.setActivo(true);
        }
        zona.setNombre(zona.getNombre().trim());
        zona.setDireccion(corregirEspacios(zona.getDireccion()));
        //zona.setObservacion(corregirEspacios(zona.getObservacion()));
        //no se aplica la corrección de espacios porque le retira los enter
        zona.setLatitud(quitarEspacios(zona.getLatitud()));
        zona.setLongitud(quitarEspacios(zona.getLongitud()).toLowerCase());
    }

    @Override
    public void validarContenido(Zona zona) throws Exception {
        final long limiteInicial=1;
        if (zona == null) {
            throw new Exception("Debe ingresar los datos del zona");
        }
        if (zona.getNombre().length() < limiteInicial || zona.getNombre().length() > 100) {
            throw new Exception("El nombre debe tener minimo "+limiteInicial+" caracter y máximo 100");
        }
        if (zona.getDireccion().length() < limiteInicial || zona.getDireccion().length() > 200) {
            throw new Exception("La dirección debe tener minimo "+limiteInicial+" caracter y máximo 200");
        }
        if (zona.getObservacion().length() > 0 && (zona.getObservacion().length() < limiteInicial || zona.getObservacion().length() > 200)) {
            throw new Exception("La observación puede tener minimo "+limiteInicial+" caracter y máximo 200");
        }
        if (zona.getLatitud().length() > 0 && (zona.getLatitud().length() < limiteInicial || zona.getLatitud().length() > 20)) {
            throw new Exception("La latitud puede tener minimo "+limiteInicial+" caracter y máximo 20");
        }
        if (zona.getLongitud().length() > 0 && (zona.getLongitud().length() < limiteInicial || zona.getLongitud().length() > 20)) {
            throw new Exception("La latitud puede tener minimo "+limiteInicial+" caracter y máximo 20");
        }
        if (zona.getCeldasCarro() == null) {
            throw new Exception("Debe seleccionar un número de celdas de carro");
        }
        if (zona.getCeldasCarro() < 0) {
            throw new Exception("Debe seleccionar un número positivo de celdas de carro");
        }
        if (zona.getCeldasMoto() == null) {
            throw new Exception("Debe seleccionar un número de celdas de moto");
        }
        if (zona.getCeldasMoto() < 0) {
            throw new Exception("Debe seleccionar un número positivo de celdas de moto");
        }
        if (zona.getValorHoraCarro() == null) {
            throw new Exception("Debe ingresar un valor hora carro");
        }
        if (zona.getValorHoraCarro() < 0) {
            throw new Exception("Debe ingresar un valor hora carro positivo");
        }
        if (zona.getValorHoraMoto() == null) {
            throw new Exception("Debe ingresar un valor hora moto");
        }
        if (zona.getValorHoraMoto() < 0) {
            throw new Exception("Debe ingresar un valor hora moto positivo");
        }
        Time tiempoIniciaSemana = this.getTiempo(zona.getEntreSemanaInicia());
        Time tiempoTerminaSemana = this.getTiempo(zona.getEntreSemanaTermina());
        if (tiempoIniciaSemana.after(tiempoTerminaSemana)) {
            throw new Exception("La hora de inicio en semana debe ser inferior a la hora de cierre");
        }
        Time tiempoIniciaFinSemana = this.getTiempo(zona.getFinSemanaInicia());
        Time tiempoTerminaFinSemana = this.getTiempo(zona.getFinSemanaTermina());
        if (tiempoIniciaFinSemana.after(tiempoTerminaFinSemana)) {
            throw new Exception("La hora de inicio en fin de semana debe ser inferior a la hora de cierre");
        }
        if (zona.getMinutosGracia()==null || zona.getMinutosGracia()<0) {
            throw new Exception("Debe seleccionar un número positivo o cero para los minutos de gracia");
        }
        if (zona.getMinutosParaNuevaGracia()==null || zona.getMinutosParaNuevaGracia()<0) {
            throw new Exception("Debe seleccionar un número positivo o cero para los minutos para nuevo periodo de gracia");
        }
    }

    @Override
    public Zona guardar(Connection connection, Zona zona) throws Exception {
        if (connection == null) {
            throw new Exception("La conexión no está disponible");
        }
        boolean esActualizar = zona.getId() != null && zona.getId() > 0;
        ajustarContenido(zona);
        validarContenido(zona);
        if (esActualizar) {
            return actualizar(connection, zona);
        }
        return insertar(connection, zona);
    }

    @Override
    public Zona insertar(Connection connection, Zona zona) throws Exception {
        final String SQL_INSERT = "INSERT INTO zona\n" +
                "(nombre, direccion, observacion, latitud, longitud, " +
                "celdas_carro, celdas_moto, valor_hora_carro, valor_hora_moto, " +
                "entre_semana_inicia, entre_semana_termina, fin_semana_inicia, " +
                "fin_semana_termina, activo, minutos_gracia, minutos_para_nueva_gracia)\n" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_INSERT);
            pst.setString(1, zona.getNombre());
            pst.setString(2, zona.getDireccion());
            pst.setString(3, zona.getObservacion());
            pst.setString(4, zona.getLatitud());
            pst.setString(5, zona.getLongitud());
            pst.setLong(6, zona.getCeldasCarro());
            pst.setLong(7, zona.getCeldasMoto());
            pst.setLong(8, zona.getValorHoraCarro());
            pst.setLong(9, zona.getValorHoraMoto());
            pst.setTime(10,getTiempo(zona.getEntreSemanaInicia()));
            pst.setTime(11,getTiempo(zona.getEntreSemanaTermina()));
            pst.setTime(12,getTiempo(zona.getFinSemanaInicia()));
            pst.setTime(13,getTiempo(zona.getFinSemanaTermina()));
            pst.setBoolean(14, zona.isActivo());
            pst.setLong(15, zona.getMinutosGracia());
            pst.setLong(16, zona.getMinutosParaNuevaGracia());

            rs = pst.executeQuery();
            while (rs.next()) {
                zona.setId(rs.getLong(1));
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return zona;
    }

    @Override
    public Zona actualizar(Connection connection, Zona zona) throws Exception {
        final String SQL_UPDATE = "UPDATE zona\n" +
                "SET nombre=?, direccion=?, observacion=?, latitud=?, " +
                "longitud=?, celdas_carro=?, celdas_moto=?, valor_hora_carro=?, valor_hora_moto=?, " +
                "entre_semana_inicia=? , entre_semana_termina=? , fin_semana_inicia=? , " +
                "fin_semana_termina=? , activo=?, " +
                "minutos_gracia=? , minutos_para_nueva_gracia=?\n" +
                "WHERE id=?;\n";
        PreparedStatement pst = null;
        Connection conn = connection;
        try {
            pst = conn.prepareStatement(SQL_UPDATE);
            pst.setString(1, zona.getNombre());
            pst.setString(2, zona.getDireccion());
            pst.setString(3, zona.getObservacion());
            pst.setString(4, zona.getLatitud());
            pst.setString(5, zona.getLongitud());
            pst.setLong(6, zona.getCeldasCarro());
            pst.setLong(7, zona.getCeldasMoto());
            pst.setLong(8, zona.getValorHoraCarro());
            pst.setLong(9, zona.getValorHoraMoto());
            pst.setTime(10,getTiempo(zona.getEntreSemanaInicia()));
            pst.setTime(11,getTiempo(zona.getEntreSemanaTermina()));
            pst.setTime(12,getTiempo(zona.getFinSemanaInicia()));
            pst.setTime(13,getTiempo(zona.getFinSemanaTermina()));
            pst.setBoolean(14, zona.isActivo());
            pst.setLong(15, zona.getMinutosGracia());
            pst.setLong(16, zona.getMinutosParaNuevaGracia());
            pst.setLong(17, zona.getId());
            long result = pst.executeUpdate();
            if (result < 1) {
                throw new Exception("La zona no fue actualizada");
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                pst.close();
            } catch (Exception ex) {
            }
        }
        return zona;
    }

    @Override
    public List<Object> listarZonas(Connection connection, Paginacion paginacion) throws Exception {
        List<Object> zonas = new ArrayList<>();
        final String SQL_FROM_WHERE_COUNT =
                "FROM zona c\n" +
                        "WHERE \n" +
                        "LOWER(cast(c.celdas_moto as varchar))  LIKE ?\n" +
                        "OR LOWER(cast(c.celdas_carro as varchar))  LIKE ?\n" +
                        "OR LOWER(cast(c.valor_hora_carro as varchar))  LIKE ?\n" +
                        "OR LOWER(cast(c.valor_hora_moto as varchar))  LIKE ?\n" +
                        "OR CASE WHEN c.activo THEN 'activa' ELSE 'borrada' END  LIKE ?\n" +
                        "OR LOWER(c.nombre)  LIKE ?\n" +
                        "OR LOWER(c.direccion)  LIKE ?\n" +
                        "OR LOWER(c.observacion)  LIKE ?\n" +
                        "OR LOWER(c.latitud)  LIKE ?\n" +
                        "OR LOWER(c.longitud)  LIKE ?\n";
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
            rs = pst.executeQuery();
            paginacion.setTotal(0L);
            while (rs.next()) {
                paginacion.setTotal(rs.getLong(1));
            }
            paginacion.setColumnas("id, nombre, direccion, observacion, latitud, longitud, " +
                    "celdas_carro, celdas_moto, valor_hora_carro, valor_hora_moto, " +
                    "entre_semana_inicia, entre_semana_termina, fin_semana_inicia, " +
                    "fin_semana_termina, activo, minutos_gracia, minutos_para_nueva_gracia");
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
            pst.setLong(11, paginacion.getLimite());
            pst.setLong(12, (paginacion.getActual() - 1) * paginacion.getLimite());
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
}
