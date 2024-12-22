package co.zer.repository;

import co.zer.model.Indice;
import co.zer.model.Zona;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ZonaDAO implements IZonaDAO {

    private int siguiente(Integer valor) {
        valor = valor + 1;
        return Math.toIntExact(valor);
    }

    @Override
    public Zona getZonaPorID(Connection connection, Long id) throws Exception {
        Zona res = null;
        final String SQL_SELECT = "SELECT id, nombre, direccion, observacion, latitud, longitud, \n" +
                "celdas_carro, celdas_moto, valor_hora_carro, valor_hora_moto, \n" +
                "entre_semana_inicia, entre_semana_termina, fin_semana_inicia, \n" +
                "fin_semana_termina, activo, minutos_gracia, minutos_para_nueva_gracia \n" +
                "FROM zona r \n" +
                "WHERE r.id = ? ;\n";
        PreparedStatement pst = null;
        Connection conn = connection;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(SQL_SELECT);
            pst.setLong(1, id);
            rs = pst.executeQuery();
            while (rs.next()) {
                Indice indice = new Indice();
                res = new Zona();
                res.setId(rs.getLong(indice.siguiente()));
                res.setNombre(rs.getString(indice.siguiente()));
                res.setDireccion(rs.getString(indice.siguiente()));
                res.setObservacion(rs.getString(indice.siguiente()));
                res.setLatitud(rs.getString(indice.siguiente()));
                res.setLongitud(rs.getString(indice.siguiente()));
                res.setCeldasCarro(rs.getLong(indice.siguiente()));
                res.setCeldasMoto(rs.getLong(indice.siguiente()));
                res.setValorHoraCarro(rs.getLong(indice.siguiente()));
                res.setValorHoraMoto(rs.getLong(indice.siguiente()));
                res.setEntreSemanaInicia(rs.getTime(indice.siguiente()));
                res.setEntreSemanaTermina(rs.getTime(indice.siguiente()));
                res.setFinSemanaInicia(rs.getTime(indice.siguiente()));
                res.setFinSemanaTermina(rs.getTime(indice.siguiente()));
                res.setActivo(rs.getBoolean(indice.siguiente()));
                res.setMinutosGracia(rs.getLong(indice.siguiente()));
                res.setMinutosParaNuevaGracia(rs.getLong(indice.siguiente()));
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
