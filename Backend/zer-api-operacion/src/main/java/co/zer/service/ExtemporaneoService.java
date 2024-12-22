package co.zer.service;

import co.zer.model.EstadoVehiculo;
import co.zer.model.Estados;
import co.zer.model.RegistroCompleto;
import co.zer.repository.IRegistroDAO;
import co.zer.repository.IZonaDAO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ExtemporaneoService extends BasicService {
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
        validarDatos(placa, idCuenta);
    }

    /**
     *
     * @param placa
     * @param idCuenta
     * @throws Exception
     */
    private void validarDatos(String placa, Long idCuenta) throws Exception {
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
    private Object confirmarPagoExtemporaneo(String cuenta, RegistroCompleto reg, Long promotor, EstadoVehiculo estadoVehiculo, IRegistroDAO iRegistroDAO) throws Exception {
        LocalDateTime fechaHoraActual = ComunRegistro.getFechaHoraActual();
        Long fechaActualNumero = ComunRegistro.getFechaActualNumero();
        reg.setEstado(Estados.Registro.PAGO_EXTEMPORANEO);
        reg.setFHRecaudo(fechaHoraActual);
        reg.setNFRecaudo(fechaActualNumero);
        reg.setEgresaSistema(false);
        reg.setPromotorRecauda(promotor);
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
    public Object pagoExtemporaneo(String cuenta, String placa, Long idCuenta, Long promotor, IRegistroDAO iRegistroDAO, IZonaDAO iZonaDAO) throws Exception {
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
        if (!reg.getEstado().equals(Estados.Registro.SALIO_REPORTADO)) {
            throw new Exception("No se puede hacer el pago a la placa " + placa + " porque su estado no es reportada");
        }

        respuesta = confirmarPagoExtemporaneo(cuenta, reg, promotor, null, iRegistroDAO);

        return respuesta;
    }

    /**
     * Permite registrar un pago extemporáneo desde la
     * aplicación WEB, importante es que estos registros
     * quedan en el campo promotor_recauda con valor null
     * @param cuenta
     * @param placa
     * @param idCuenta
     * @param iRegistroDAO
     * @param iZonaDAO
     * @return
     * @throws Exception
     */
    public Object pagoExtemporaneoWEB(String cuenta, String placa, Long idCuenta, IRegistroDAO iRegistroDAO, IZonaDAO iZonaDAO) throws Exception {
        Object respuesta = null;

        validarDatos(placa, idCuenta);
        placa = ComunRegistro.limpiarContenidosPlaca(placa);
        RegistroCompleto reg = iRegistroDAO.recuperarPorId(this.getConnect(cuenta), idCuenta);
        if (reg == null) {
            throw new Exception("La cuenta de cobro no es correcta");
        }
        if (!reg.getPlaca().equals(placa)) {
            throw new Exception("El id no corresponde a la placa " + placa);
        }
        if (!reg.getEstado().equals(Estados.Registro.SALIO_REPORTADO)) {
            throw new Exception("No se puede hacer el pago a la placa " + placa + " porque su estado no es reportada");
        }
        respuesta = confirmarPagoExtemporaneo(cuenta, reg, null, null, iRegistroDAO);

        return respuesta;
    }
}