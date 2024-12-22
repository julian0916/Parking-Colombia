package co.zer.service;

import co.zer.model.EstadoVehiculo;
import co.zer.model.Estados;
import co.zer.model.RegistroCompleto;
import co.zer.repository.IRegistroDAO;
import co.zer.repository.IZonaDAO;
import co.zer.utils.Utilidades;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConfirmarService extends BasicService {
    /**
     * Valida los datos necesarios para confirmar el pago del servicio
     * en el estacionamiento
     *
     * @param placa
     * @param idCuenta
     * @throws Exception
     */
    private void validarDatos(String placa, Long idCuenta, Long promotor, LocalDateTime fhLiquidacion) throws Exception {
        if (promotor == null) {
            throw new Exception("Se requieren los datos del promotor");
        }
        if (promotor < 1) {
            throw new Exception("Se requieren los datos del promotor");
        }
        if (idCuenta == null) {
            throw new Exception("El id de pago es indispensable");
        }
        if (idCuenta < 1) {
            throw new Exception("El id de pago es indispensable");
        }
        if (ComunRegistro.getTipoPlaca(ComunRegistro.limpiarContenidosPlaca(placa)) == Estados.Placa.NO_ES_PLACA) {
            throw new Exception("Debe ingresar una placa válida");
        }
        if (fhLiquidacion.plusMinutes(Utilidades.MINUTOS_PRELIQUIDACION_VALIDA).isBefore(Utilidades.getLocalDateTime())) {
            throw new Exception("Debe volver a consultar el valor de pago para la placa ");
        }
    }

    /**
     * Confirma los datos necesarios para completar el registro de pago
     *
     * @param cuenta
     * @param reg
     * @param promotor
     * @param estadoVehiculo
     * @param iRegistroDAO
     * @return
     * @throws Exception
     */
    private Object confirmarDatosPago(String cuenta,
                                      RegistroCompleto reg,
                                      Long promotor,
                                      EstadoVehiculo estadoVehiculo,
                                      IRegistroDAO iRegistroDAO,
                                      LocalDateTime fhLiquidacion) throws Exception {

        PreliquidarService preliquidarService = new PreliquidarService();
        preliquidarService.generarPreliquidacion(cuenta,
                reg,
                estadoVehiculo,
                iRegistroDAO,
                fhLiquidacion,
                Estados.Registro.SALIO_PAGO);

        reg.setEgresaSistema(false);
        reg.setPromotorEgreso(promotor);
        reg.setPromotorRecauda(promotor);
        reg.setPromotorReporta(null);
        return iRegistroDAO.actualizar(this.getConnect(cuenta), reg);
    }

    /**
     * Realiza el proceso de pago de una cuenta con estado abierta
     * este proceso se basa en la preliquidación, por lo que se
     * requiere de la fecha de preliquidación la cual debe ser inferior
     * a 5 minutos con respecto a la fecha actual.
     *
     * @param cuenta
     * @param placa
     * @param idCuenta
     * @param promotor
     * @param fhliquidacion
     * @param iRegistroDAO
     * @param iZonaDAO
     * @return
     * @throws Exception
     */
    public Object confirmarPago(String cuenta, String placa, Long idCuenta, Long promotor, String fhliquidacion, IRegistroDAO iRegistroDAO, IZonaDAO iZonaDAO) throws Exception {
        Object respuesta = null;
        LocalDateTime fhLiquidacion;
        try {
            fhLiquidacion = LocalDateTime.parse(fhliquidacion);
        } catch (Exception ex) {
            fhLiquidacion = ComunRegistro.getFechaHoraActual();
        }
        validarDatos(placa, idCuenta, promotor, fhLiquidacion);
        placa = ComunRegistro.limpiarContenidosPlaca(placa);
        RegistroCompleto reg = iRegistroDAO.recuperarPorId(this.getConnect(cuenta), idCuenta);
        if (reg == null) {
            throw new Exception("La cuenta de cobro no es correcta");
        }
        if (!reg.getPlaca().equals(placa)) {
            throw new Exception("El id no corresponde a la placa " + placa);
        }
        if (!reg.getEstado().equals(Estados.Registro.ABIERTA)) {
            throw new Exception("No se pude registrar el pago de la placa " + placa + " porque su estado no es abierto");
        }

        EstadoVehiculo estadoVehiculo = CacheEstadoVehiculoService.getEstado(cuenta, reg.getPlaca());
        respuesta = confirmarDatosPago(cuenta, reg, promotor, estadoVehiculo, iRegistroDAO, fhLiquidacion);

        LocalDateTime expiraSaldo = ComunRegistro.getFHExpiraSaldo(reg.getHCobradas(), estadoVehiculo, reg.getFHIngreso());
        LocalDateTime periodoGracia = null;
        if (estadoVehiculo != null) {
            periodoGracia = estadoVehiculo.getFHUltimoPeriodoGracia();
        }
        if (reg.getEstado().equals(Estados.Registro.SALIO_GRACIA)) {
            periodoGracia = reg.getFHEgreso();
        }
        CacheEstadoVehiculoService.salirZonaPostpago(
                cuenta,
                reg.getPlaca(),
                reg.getZona(),
                reg.getId(),
                reg.getEsCarro(),
                expiraSaldo,
                periodoGracia);

        return respuesta;
    }
}