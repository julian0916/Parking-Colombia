package co.zer.service;

import co.zer.model.EstadoVehiculo;
import co.zer.model.Estados;
import co.zer.model.RegistroCompleto;
import co.zer.repository.IRegistroDAO;
import co.zer.repository.IZonaDAO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PreliquidarService extends BasicService {
    /**
     * Valida los datos necesarios para confirmar el pago del servicio
     * en el estacionamiento
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
     * @param estadoVehiculo
     * @param iRegistroDAO
     * @return
     * @throws Exception
     */
    public Object generarPreliquidacion(String cuenta,
                                        RegistroCompleto reg,
                                        EstadoVehiculo estadoVehiculo,
                                        IRegistroDAO iRegistroDAO,
                                        LocalDateTime fhLiquidacion,
                                        Long estadoPorDefecto) throws Exception {
        LocalDateTime fechaHoraActual = fhLiquidacion;//ComunRegistro.getFechaHoraActual();
        Long fechaActualNumero = ComunRegistro.getFechaActualNumero(fechaHoraActual.toLocalDate());
        Long minutosACobrar = ComunRegistro.minutosACobrar(reg, estadoVehiculo,fechaHoraActual);
        reg.setHCobradas(ComunRegistro.horasACobrar(minutosACobrar));
        //reg.setEstado(Estados.Registro.SALIO_PAGO);
        reg.setEstado(estadoPorDefecto);
        reg.setFHEgreso(fechaHoraActual);
        reg.setNFEgreso(fechaActualNumero);
        Long minutosTranscurridosIngresoFin = ComunRegistro.minutosTranscurridosIngresoFin(reg);
        if (ComunRegistro.aplicaPeriodoGracia(
                minutosACobrar,
                estadoVehiculo,
                reg.getMinutosGraciaZona(),
                reg.getMinutosParaNuevaGraciaZona())) {
            //se verifica que el periodo de gracia sea el real y
            //no el que se calcula para el cobro dado que el tiempo
            //usado para el cobro tiene encuenta el saldo previo
            if (minutosTranscurridosIngresoFin <= reg.getMinutosGraciaZona()) {
                reg.setEstado(Estados.Registro.SALIO_GRACIA);
            }
            reg.setHCobradas(0l);
        }
        reg.setValorCobrado(reg.getHCobradas() * reg.getValorH());


        reg.setFHRecaudo(fechaHoraActual);
        reg.setNFRecaudo(fechaActualNumero);
        reg.setEgresaSistema(false);
        reg.setPromotorEgreso(null);
        reg.setPromotorRecauda(null);
        reg.setPromotorReporta(null);
        return reg;
    }

    /**
     * Realiza el proceso de pago de una cuenta con estado abierta
     *
     * @param cuenta
     * @param placa
     * @param idCuenta
     * @param iRegistroDAO
     * @param iZonaDAO
     * @return
     * @throws Exception
     */
    public Object preliquidarCuenta(String cuenta, String placa, Long idCuenta, IRegistroDAO iRegistroDAO, IZonaDAO iZonaDAO) throws Exception {
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
        if (!reg.getEstado().equals(Estados.Registro.ABIERTA)) {
            throw new Exception("No se pude preliquidar el pago de la placa " + placa + " porque su estado no es abierto");
        }
        EstadoVehiculo estadoVehiculo = CacheEstadoVehiculoService.getEstado(cuenta, reg.getPlaca());
        LocalDateTime fechaHoraActual = ComunRegistro.getFechaHoraActual();
        respuesta = generarPreliquidacion(cuenta, reg, estadoVehiculo, iRegistroDAO, fechaHoraActual, Estados.Registro.SALIO_PAGO);

        return respuesta;
    }

    /**
     * Preliquida los saldos que tenga en este momento la placa suministrada
     *
     * @param cuenta
     * @param placa
     * @param iRegistroDAO
     * @param iZonaDAO
     * @return
     * @throws Exception
     */
    public Object preliquidarPlaca(String cuenta, String placa, IRegistroDAO iRegistroDAO, IZonaDAO iZonaDAO) throws Exception {
        placa = ComunRegistro.limpiarContenidosPlaca(placa);
        List<RegistroCompleto> registros = iRegistroDAO.abiertosPlaca(this.getConnect(cuenta), placa);
        List<Object> res = new ArrayList<>();
        LocalDateTime fechaHoraActual = ComunRegistro.getFechaHoraActual();
        for (RegistroCompleto reg : registros) {
            try {
                EstadoVehiculo estadoVehiculo = CacheEstadoVehiculoService.getEstado(cuenta, reg.getPlaca());
                res.add(generarPreliquidacion(cuenta, reg, estadoVehiculo, iRegistroDAO, fechaHoraActual, Estados.Registro.SALIO_PAGO));
            } catch (Exception ex) {

            }
        }
        return res;
    }

    /**
     * Realiza el cálculo de lo que al momento se debe, esto
     * para mostrarlo al usuario al momento de solicitar su pago.
     *
     * @param cuenta
     * @param idZona
     * @param iRegistroDAO
     * @param iZonaDAO
     * @return
     * @throws Exception
     */
    public Object preliquidarZonaOLD(String cuenta, Long idZona, IRegistroDAO iRegistroDAO, IZonaDAO iZonaDAO) throws Exception {
        LocalDateTime fechaHoraActual = ComunRegistro.getFechaHoraActual();
        List<RegistroCompleto> listado = iRegistroDAO.abiertosZona(this.getConnect(cuenta), idZona);
        List<RegistroCompleto> listadoPrepagos = iRegistroDAO.getPrepagoActivosZona(this.getConnect(cuenta), idZona, fechaHoraActual);

        List<Object> respuesta = new ArrayList<>();
        for (RegistroCompleto reg : listado) {
            EstadoVehiculo estadoVehiculo = CacheEstadoVehiculoService.getEstado(cuenta, reg.getPlaca());
            respuesta.add(generarPreliquidacion(cuenta, reg, estadoVehiculo, iRegistroDAO, fechaHoraActual, Estados.Registro.SALIO_PAGO));
        }
        for (RegistroCompleto reg : listadoPrepagos) {
            respuesta.add(reg);
        }

        return respuesta;
    }

    /**
     * Dada la zona, el sistema busca los vehículos con estado abierto
     * luego preliquida a cada uno de los vehículos para saber cuánto deben cancelar
     * también se listan los carros que estan con estado prepago
     * que no tengan otro registro o se le haya colocado el estado de
     * retirados de la zona
     *
     * @param cuenta
     * @param idZona
     * @param iRegistroDAO
     * @param iZonaDAO
     * @return
     * @throws Exception
     */
    public Object preliquidarZona(String cuenta, Long idZona, IRegistroDAO iRegistroDAO, IZonaDAO iZonaDAO) throws Exception {
        LocalDateTime fechaHoraActual = ComunRegistro.getFechaHoraActual();
        List<RegistroCompleto> listado = iRegistroDAO.abiertosZona(this.getConnect(cuenta), idZona);
        List<RegistroCompleto> listadoPrepagos = iRegistroDAO.getPrepagoActivosZona(this.getConnect(cuenta), idZona, fechaHoraActual);
        List<Object> respuesta =
                listado
                        .parallelStream()
                        .map(reg ->
                                {
                                    try {
                                        return generarPreliquidacion(cuenta, reg, CacheEstadoVehiculoService.getEstado(cuenta, reg.getPlaca()), iRegistroDAO, fechaHoraActual, Estados.Registro.SALIO_PAGO);
                                    } catch (Exception exception) {
                                        exception.printStackTrace();
                                    }
                                    return null;
                                }
                        )
                        .collect(Collectors.toList());
        respuesta.addAll(
                listadoPrepagos
                        .stream()
                        .collect(Collectors.toList())
        );
        if (respuesta == null) {
            respuesta = new ArrayList();
        }
        return respuesta;
    }
}