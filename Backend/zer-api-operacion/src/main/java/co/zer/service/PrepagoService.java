package co.zer.service;

import co.zer.model.EstadoVehiculo;
import co.zer.model.Estados;
import co.zer.model.Registro;
import co.zer.model.RegistroCompleto;
import co.zer.repository.IRegistroDAO;
import co.zer.repository.IZonaDAO;
import co.zer.utils.Uuid;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class PrepagoService extends BasicService {
    /**
     * Valida que se contengan todos los datos necesarios en el registro
     *
     * @param registro
     * @throws Exception
     */
    private void validarRegistro(Registro registro) throws Exception {
        if (registro == null) {
            throw new Exception("Debe ingresar los datos para poder continuar");
        }
        if (registro.getZona() == null) {
            throw new Exception("Los datos de la zona son necesarios");
        }
        if (registro.getZona() < 1) {
            throw new Exception("Los datos de la zona son necesarios");
        }
        if (registro.getPromotor() == null) {
            throw new Exception("Los datos del promotor son necesarios");
        }
        if (registro.getPromotor() < 1) {
            throw new Exception("Los datos del promotor son necesarios");
        }
        if (registro.getHoras() == null) {
            throw new Exception("Debe ingresar las horas que se prepagan");
        }
        if (registro.getHoras() < 1) {
            throw new Exception("Debe ingresar las horas que se prepagan");
        }
        if (ComunRegistro.getTipoPlaca(ComunRegistro.limpiarContenidosPlaca(registro.getPlaca())) == Estados.Placa.NO_ES_PLACA) {
            throw new Exception("Debe ingresar una placa válida");
        }
        registro.setPlaca(ComunRegistro.limpiarContenidosPlaca(registro.getPlaca()));
    }

    private Object registrarIngresoPrepago(String cuenta, Registro registro, RegistroCompleto reg, IRegistroDAO iRegistroDAO, IZonaDAO iZonaDAO) throws Exception {
        LocalDateTime fechaHoraActual = ComunRegistro.getFechaHoraActual();
        Long fechaActualNumero = ComunRegistro.getFechaActualNumero();

        //horasCobradas, placa, promotor, zona
        reg.setId(Uuid.getUuidLong());
        reg.setEsCarro(ComunRegistro.validarPlacaCarro(reg.getPlaca()));

        //hora inicia, hora termina, valorHora,
        ComunRegistro.colocarDatosZona(this.getConnect(cuenta), reg, reg.getZona(), iZonaDAO);

        reg.setEstado(Estados.Registro.PREPAGO);
        reg.setFHIngreso(fechaHoraActual);
        reg.setNFIngreso(fechaActualNumero);
        reg.setFHEgreso(fechaHoraActual.plusHours(reg.getHCobradas()));
        reg.setNFEgreso(ComunRegistro.getFechaActualNumero(reg.getFHEgreso().toLocalDate()));
        reg.setFHRecaudo(fechaHoraActual);
        reg.setNFRecaudo(fechaActualNumero);
        reg.setPromotorRecauda(registro.getPromotor());
        reg.setValorCobrado(reg.getHCobradas() * reg.getValorH());
        reg.setEgresaSistema(false);

        return iRegistroDAO.insertar(this.getConnect(cuenta), reg);
    }

    public Object ingresoPrepago(String cuenta, Registro registro, IRegistroDAO iRegistroDAO, IZonaDAO iZonaDAO, CierreSistemaService cierreSistemaService) throws Exception {
        Object respuesta = null;

        validarRegistro(registro);
        EstadoVehiculo estadoVehiculo = CacheEstadoVehiculoService.getEstado(cuenta, registro.getPlaca());
        Estados.ParaIngresar ingreso = ComunRegistro.pasosParaIngresar(registro.getPlaca(), registro.getZona(), estadoVehiculo);

        RegistroCompleto reg = new RegistroCompleto(registro);

        if (ingreso.respuesta == Estados.ParaIngresar.ESTA_EN_PREPAGO) {
            throw new Exception("El vehículo " + reg.getPlaca() + " esta prepagado hasta " + estadoVehiculo.getFHExpiraCredito().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:a")));
        }
        if (ingreso.respuesta == Estados.ParaIngresar.YA_SE_ENCUENTRA_EN_ZONA) {
            throw new Exception("El vehículo " + reg.getPlaca() + " ya se encuentra activo en la zona");
        }
        if (ingreso.respuesta == Estados.ParaIngresar.DEBE_CERRAR_CUENTA) {
            cierreSistemaService.cierraCuentaSistema(cuenta, ingreso.idCuentaDebeCerrar, iRegistroDAO, iZonaDAO);
            ingreso = ComunRegistro.pasosParaIngresar(registro.getPlaca(), registro.getZona(), estadoVehiculo);
        }
        if (ingreso.respuesta == Estados.ParaIngresar.PUEDE_INGRESAR) {
            respuesta = registrarIngresoPrepago(cuenta, registro, reg, iRegistroDAO, iZonaDAO);
            LocalDateTime fechaExpiraSalo = ComunRegistro.getFHExpiraSaldoPrepago(reg.getHCobradas(), estadoVehiculo);
            CacheEstadoVehiculoService.ingresarZonaPrepago(cuenta, reg.getPlaca(), reg.getZona(), reg.getId(), reg.getEsCarro(), fechaExpiraSalo);
            return respuesta;
        }
        return respuesta;
    }

    public Object salioPrepagoActualizar(String cuenta, Long idPago, IRegistroDAO iRegistroDAO) throws Exception {
        iRegistroDAO.salioPrepagoActualizar(this.getConnect(cuenta), idPago);
        Map<String, String> resp = new HashMap<>();
        resp.put("resultado", "Se ha retirado el vehículo de la zona");
        return resp;
    }
}
