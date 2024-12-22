package co.zer.service;

import co.zer.model.PagoMunicipio;
import co.zer.model.Recaudo;
import co.zer.repository.IFinancieraDAO;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

@Service
public class FinancieraService extends BasicService {
    public List<Map<String, Object>> getListadoRecaudoReportadoFecha(String cuenta,
                                                                     Long fnRecaudo,
                                                                     IFinancieraDAO iFinancieraDAO) throws Exception {

        return iFinancieraDAO.getListadoRecaudoReportadoFecha(this.getConnect(cuenta),
                fnRecaudo);
    }

    public Map<String, Object> guardarRecaudo(String cuenta,
                                              Recaudo recaudo,
                                              IFinancieraDAO iFinancieraDAO) throws Exception {

        Connection connection = this.getConnect(cuenta);
        connection.setAutoCommit(false);
        //Este campo es de control para la GUI
        //con él se refresca el contenido presentado
        Long posArray = recaudo.getPosArray();
        Map<String, Object> resultado = iFinancieraDAO.getRecaudoReportadoFechaPromotor(
                connection,
                recaudo.getNfRecaudo(),
                recaudo.getPromotor());
        try {
            if (resultado.size() > 0) {
                recaudo.setId((Long) resultado.get("id_recaudo"));
            }

            Long dif = recaudo.getCantidadReportada() - recaudo.getCantidadRecaudada();
            if (dif > 0l) {
                dif = 0l;
            }
            dif = dif + recaudo.getValorAbono();
            recaudo.setDiferencia(dif);
            resultado = iFinancieraDAO.guardarRecaudo(connection, recaudo);
            Integer saldo=(Integer) resultado.get("saldo");
            if(saldo!=null){
                dif +=saldo;
            }
            resultado.put("diferencia", dif);
            if ((boolean) resultado.get("cerrada") == true &&
                    (boolean) resultado.get("actualizo_cuenta") == false) {

                //administra el global del estado
                //mantiene el valor actual que el promotor tiene
                //con el sistema
                resultado = iFinancieraDAO.guardarEstadoCuenta(
                        connection,
                        recaudo.getPromotor(),
                        (long) resultado.get("diferencia"),
                        (boolean) resultado.get("tieneEstadoCuenta"),
                        recaudo.getNfRecaudo());

                //se almacena el saldo actual de la cuenta con el global actual
                iFinancieraDAO.recaudoActualizadoEnEstadoCuenta(connection,
                        (Long) resultado.get("id_recaudo"),
                        (Long) resultado.get("saldo_cuenta"));

                //en caso de ser un recaudo en el medio de otros
                //se recalculan los saldos desde ese hacia delante
                iFinancieraDAO.recaudoActualizarSaldosPosteriores(connection,
                        recaudo.getPromotor(),
                        recaudo.getNfRecaudo());
            }
            connection.commit();
        } catch (Exception ex) {
            connection.rollback();
        } finally {
            resultado = iFinancieraDAO.getRecaudoReportadoFechaPromotor(
                    connection,
                    recaudo.getNfRecaudo(),
                    recaudo.getPromotor());
            resultado.put("posArray", posArray);
        }

        return resultado;
    }

    public PagoMunicipio getPagoMunicipio(String cuenta,
                                          Long fnRecaudo,
                                          IFinancieraDAO iFinancieraDAO) throws Exception {
        return iFinancieraDAO.getPagoMunicipio(this.getConnect(cuenta), fnRecaudo);
    }

    public PagoMunicipio guardarPagoMunicipio(String cuenta,
                                              PagoMunicipio pagoMunicipio,
                                              IFinancieraDAO iFinancieraDAO) throws Exception {
        return iFinancieraDAO.guardarPagoMunicipio(this.getConnect(cuenta), pagoMunicipio);
    }

    public Boolean getValidarFechaPagoMunicipio(String cuenta, String fecha) {
        try {
            int year = Integer.parseInt(fecha.substring(0, 4));
            int mes = Integer.parseInt(fecha.substring(4, 6));
            int dia = Integer.parseInt(fecha.substring(6));
            /*LocalDate localDateSolicitada = LocalDate.of(year,mes,dia);
            LocalDate localDateActual = Utilidades.getLocalDateTime().toLocalDate();
            LocalDate localDateMin = localDateActual.minusDays(5);//5 dias desde el actual

            if (localDateSolicitada.isBefore(localDateMin)) {
                return false;
            }

            if (localDateSolicitada.isAfter(localDateActual)) {
                return false;
            }*/

            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}