package co.zer.repository;

import co.zer.model.Paginacion;
import co.zer.model.PagoMunicipio;
import co.zer.model.Recaudo;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IFinancieraDAO {

    PagoMunicipio getPagoMunicipio(Connection connection, Long fnPago) throws Exception;
    PagoMunicipio guardarPagoMunicipio(Connection connection,
                                       PagoMunicipio pagoMunicipio) throws Exception;

    List<Map<String,Object>> getListadoRecaudoReportadoFecha(Connection connection, Long fnRecaudo) throws Exception;

    Map<String, Object> guardarRecaudo(Connection connection,Recaudo recaudo) throws Exception;

    Map<String, Object> guardarEstadoCuenta(Connection connection,
                                            Long promotor,
                                            Long nuevoValor,
                                            boolean insertar,
                                            Long fnRecaudo) throws Exception;

    long recaudoActualizadoEnEstadoCuenta(Connection connection, Long idRecaudo, Long saldo) throws Exception;

    long recaudoActualizarSaldosPosteriores(Connection connection, Long idPromotor,Long nfRecaudo) throws Exception;

    Map<String, Object> getRecaudoReportadoFechaPromotor(
            Connection connection,
            Long fnRecaudo,
            Long promotor) throws Exception;

}
