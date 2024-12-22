package co.zer.service;

import co.zer.model.EstadoVehiculo;
import co.zer.model.Estados;
import co.zer.model.RegistroCompleto;
import co.zer.repository.ICuentasDAO;
import co.zer.repository.IRegistroDAO;
import co.zer.repository.IZonaDAO;
import co.zer.utils.Utilidades;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CierreSistemaService extends BasicService {
    /**
     * Confirma los datos necesarios para completar el registro de pago
     *
     * @param cuenta
     * @param reg
     * @param estadoVehiculo
     * @param iRegistroDAO
     * @return
     * @throws Exception
     */
    private Object confirmarDatosReporte(String cuenta, RegistroCompleto reg, EstadoVehiculo estadoVehiculo, IRegistroDAO iRegistroDAO) throws Exception {
        LocalDateTime fechaHoraActual = ComunRegistro.getFechaHoraActual();
        Long fechaActualNumero = ComunRegistro.getFechaActualNumero();
        Long minutosACobrar = ComunRegistro.minutosACobrar(reg, estadoVehiculo,fechaHoraActual);
        reg.setHCobradas(ComunRegistro.horasACobrar(minutosACobrar));
        reg.setEstado(Estados.Registro.SALIO_REPORTADO);
        if (ComunRegistro.aplicaPeriodoGracia(
                minutosACobrar,
                estadoVehiculo,
                reg.getMinutosGraciaZona(),
                reg.getMinutosParaNuevaGraciaZona())) {
            reg.setEstado(Estados.Registro.SALIO_GRACIA);
            reg.setHCobradas(0l);
        }
        //Se calcula el valor a pagar que si llega a ser
        //cero o inferior entonces se asume que salio como
        //periodo de gracia.
        long valorCobrado = reg.getHCobradas() * reg.getValorH();
        reg.setValorCobrado(valorCobrado);
        if (valorCobrado < 1) {
            reg.setEstado(Estados.Registro.SALIO_GRACIA);
        }

        reg.setFHEgreso(fechaHoraActual);
        reg.setNFEgreso(fechaActualNumero);
        reg.setFHRecaudo(null);
        reg.setNFRecaudo(null);
        reg.setEgresaSistema(true);
        reg.setPromotorEgreso(null);
        reg.setPromotorRecauda(null);
        reg.setPromotorReporta(null);
        return iRegistroDAO.actualizar(this.getConnect(cuenta), reg);
    }

    /**
     * Realiza el proceso de pago de una cuenta con estado abierta
     *
     * @param cuenta
     * @param idCuenta
     * @param iRegistroDAO
     * @param iZonaDAO
     * @return
     * @throws Exception
     */
    public Object cierraCuentaSistema(String cuenta, Long idCuenta, IRegistroDAO iRegistroDAO, IZonaDAO iZonaDAO) throws Exception {
        RegistroCompleto reg = iRegistroDAO.recuperarPorId(this.getConnect(cuenta), idCuenta);
        return cierraCuentaSistemaReg(reg, cuenta, iRegistroDAO, iZonaDAO);
    }

    private Object cierraCuentaSistemaReg(RegistroCompleto reg, String cuenta, IRegistroDAO iRegistroDAO, IZonaDAO iZonaDAO) throws Exception {
        Object respuesta = null;

        if (reg == null) {
            throw new Exception("La cuenta de cobro no es correcta");
        }
        if (!reg.getEstado().equals(Estados.Registro.ABIERTA)) {
            throw new Exception("No se puede cerrar la cuenta de la placa " + reg.getPlaca() + " porque su estado no es abierta");
        }
        EstadoVehiculo estadoVehiculo = CacheEstadoVehiculoService.getEstado(cuenta, reg.getPlaca());
        respuesta = confirmarDatosReporte(cuenta, reg, estadoVehiculo, iRegistroDAO);

        LocalDateTime expiraSaldo = ComunRegistro.getFHExpiraSaldo(reg.getHCobradas(), estadoVehiculo, reg.getFHIngreso());
        LocalDateTime periodoGracia = estadoVehiculo.getFHUltimoPeriodoGracia();
        if (reg.getEstado().equals(Estados.Registro.SALIO_GRACIA)) {
            periodoGracia = reg.getFHEgreso();
        }
        CacheEstadoVehiculoService.salirZonaPostpago(cuenta, reg.getPlaca(), reg.getZona(), reg.getId(), reg.getEsCarro(), expiraSaldo, periodoGracia);
        return respuesta;
    }

    /**
     * Cierra
     *
     * @param iRegistroDAO
     * @param iZonaDAO
     * @param iCuentasDAO
     * @return
     * @throws Exception
     */
    @Transactional
    public Object cierreMasivoSistema(IRegistroDAO iRegistroDAO, IZonaDAO iZonaDAO, ICuentasDAO iCuentasDAO) throws Exception {
        List<String> listCuentas = iCuentasDAO.getCuentas(this.getConnect(Utilidades.CUENTAS));
        List<String> err = new ArrayList<>();
        for (String cuenta : listCuentas) {
            try {
                Connection conn = this.getConnect(cuenta);
                conn.setAutoCommit(true);
                List<RegistroCompleto> listAbiertos = iRegistroDAO.abiertosSistema(conn);
                for (RegistroCompleto reg : listAbiertos) {
                    try {
                        cierraCuentaSistemaReg(reg, cuenta, iRegistroDAO, iZonaDAO);
                    } catch (Exception exception) {
                        err.add("--cierreMasivoSistema:" + exception.getMessage());
                    }
                }
            } catch (Exception ex) {
                err.add("--cierreMasivoSistema:" + ex.getMessage());
            }
        }
        CacheEstadoVehiculoService.eliminarContenidos();
        return err;
    }
}