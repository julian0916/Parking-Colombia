package co.zer.repository;

import co.zer.model.RegistroCompleto;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IRegistroDAO {
    RegistroCompleto guardar(Connection connection, RegistroCompleto registroCompleto) throws Exception;

    RegistroCompleto insertar(Connection connection, RegistroCompleto registroCompleto) throws Exception;

    RegistroCompleto actualizar(Connection connection, RegistroCompleto registroCompleto) throws Exception;

    RegistroCompleto recuperarPorId(Connection connection, Long id) throws Exception;

    List<RegistroCompleto> carteraPorPlaca(Connection connection, String placa) throws Exception;

    List<RegistroCompleto> abiertosZona(Connection connection, Long idZona) throws Exception;


    List<Map<String, Object>> ocupacionZona(Connection connection, Long idZona) throws Exception;

    List<RegistroCompleto> abiertosSistema(Connection connection) throws Exception;

    List<RegistroCompleto> abiertosPlaca(Connection connection, String placa) throws Exception;

    List<RegistroCompleto> getAlertasPrepagoZona(Connection connection, Long idZona, LocalDateTime localDateTime) throws Exception;

    List<RegistroCompleto> getPrepagoActivosZona(Connection connection, Long idZona, LocalDateTime localDateTime) throws Exception;

    void salioPrepagoActualizar(Connection connection, Long idPago) throws Exception;

    List<RegistroCompleto> getUltimosMovimientosHoy(Connection connection, String  placa) throws Exception;
}
