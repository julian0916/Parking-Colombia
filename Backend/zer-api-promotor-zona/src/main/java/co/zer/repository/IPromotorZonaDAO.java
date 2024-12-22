package co.zer.repository;

import co.zer.model.Paginacion;
import co.zer.model.PromotorZona;

import java.sql.Connection;
import java.util.List;

public interface IPromotorZonaDAO {
    void ajustarContenido(PromotorZona promotorZona);
    void validarContenido(PromotorZona promotorZona) throws Exception;
    PromotorZona guardar(Connection connection, PromotorZona promotorZona) throws Exception;
    PromotorZona insertar(Connection connection, PromotorZona promotorZona) throws Exception;
    PromotorZona actualizar(Connection connection, PromotorZona promotorZona) throws Exception;
    List<Object> listarPromotoresZonas(Connection connection, Paginacion paginacion) throws Exception;
    List<Object> listarZonasAsignadasPromotor(Connection connection, String correoPromotor) throws Exception;
    List<Object> listarPromotoresAsignadoAZona(Connection connection, Long zona) throws Exception;
}
