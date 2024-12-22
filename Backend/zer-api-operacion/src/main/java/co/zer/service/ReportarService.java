package co.zer.service;

import co.zer.model.EstadoVehiculo;
import co.zer.model.Estados;
import co.zer.model.RegistroCompleto;
import co.zer.repository.IRegistroDAO;
import co.zer.repository.IZonaDAO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReportarService extends BasicService {
    /**
     * Valida los datos necesarios para confirmar el pago del servicio
     * en el estacionamiento
     *
     * @param placa
     * @param idCuenta
     * @throws Exception
     */
    private void validarDatos(String placa, Long idCuenta, Long promotor) throws Exception {
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
    private Object confirmarDatosReporte(String cuenta, RegistroCompleto reg, Long promotor, EstadoVehiculo estadoVehiculo, IRegistroDAO iRegistroDAO) throws Exception {
        LocalDateTime fechaHoraActual = ComunRegistro.getFechaHoraActual();
        PreliquidarService preliquidarService = new PreliquidarService();
        preliquidarService.generarPreliquidacion(cuenta,
                reg,
                estadoVehiculo,
                iRegistroDAO,
                fechaHoraActual,
                Estados.Registro.SALIO_REPORTADO);
        /*Long fechaActualNumero = ComunRegistro.getFechaActualNumero();
        Long minutosACobrar = ComunRegistro.minutosACobrar(reg, estadoVehiculo);
        reg.setHCobradas(ComunRegistro.horasACobrar(minutosACobrar));
        reg.setEstado(Estados.Registro.SALIO_REPORTADO);
        if (ComunRegistro.aplicaPeriodoGracia(minutosACobrar, estadoVehiculo)) {
            reg.setEstado(Estados.Registro.SALIO_GRACIA);
            reg.setHCobradas(0l);
        }
        reg.setValorCobrado(reg.getHCobradas() * reg.getValorH());

        reg.setFHEgreso(fechaHoraActual);
        reg.setNFEgreso(fechaActualNumero);*/
        reg.setFHRecaudo(null);
        reg.setNFRecaudo(null);
        reg.setEgresaSistema(false);
        reg.setPromotorEgreso(promotor);
        reg.setPromotorRecauda(null);
        reg.setPromotorReporta(promotor);
        return iRegistroDAO.actualizar(this.getConnect(cuenta), reg);
    }

    /**
     * Realiza el proceso de pago de una cuenta con estado abierta
     *
     * @param cuenta
     * @param placa
     * @param idCuenta
     * @param promotor
     * @param iRegistroDAO
     * @param iZonaDAO
     * @return
     * @throws Exception
     */
    public Object confirmarReporte(String cuenta, String placa, Long idCuenta, Long promotor, IRegistroDAO iRegistroDAO, IZonaDAO iZonaDAO) throws Exception {
        Object respuesta = null;

        validarDatos(placa, idCuenta, promotor);
        placa = ComunRegistro.limpiarContenidosPlaca(placa);
        RegistroCompleto reg = iRegistroDAO.recuperarPorId(this.getConnect(cuenta), idCuenta);
        if (reg == null) {
            throw new Exception("La cuenta de cobro no es correcta");
        }
        if (!reg.getPlaca().equals(placa)) {
            throw new Exception("El id no corresponde a la placa " + placa);
        }
        if (!reg.getEstado().equals(Estados.Registro.ABIERTA)) {
            throw new Exception("No se puede reportar a la placa " + placa + " porque su estado no es abierto");
        }
        EstadoVehiculo estadoVehiculo = CacheEstadoVehiculoService.getEstado(cuenta, reg.getPlaca());
        respuesta = confirmarDatosReporte(cuenta, reg, promotor, estadoVehiculo, iRegistroDAO);

        LocalDateTime expiraSaldo = ComunRegistro.getFHExpiraSaldo(reg.getHCobradas(), estadoVehiculo, reg.getFHIngreso());
        LocalDateTime periodoGracia = estadoVehiculo.getFHUltimoPeriodoGracia();
        if (reg.getEstado().equals(Estados.Registro.SALIO_GRACIA)) {
            periodoGracia = reg.getFHEgreso();
        }
        CacheEstadoVehiculoService.salirZonaPostpago(cuenta, reg.getPlaca(), reg.getZona(), reg.getId(), reg.getEsCarro(), expiraSaldo, periodoGracia);

        return respuesta;
    }
}