package co.zer.api;

import co.zer.model.Registro;
import co.zer.repository.*;
import co.zer.service.*;
import co.zer.utils.Utilidades;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Optional;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-11-30T15:02:10.636Z[GMT]")
@RestController
@CrossOrigin(origins = "*")
public class OperacionApiController implements OperacionApi {

    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    private final PrepagoService prepagoService;
    private final IngresoService ingresoService;
    private final ConfirmarService confirmarService;
    private final ReportarService reportarService;
    private final ExtemporaneoService extemporaneoService;
    private final PreliquidarService preliquidarService;
    private final InformacionService informacionService;
    private final CierreSistemaService cierreSistemaService;
    private final SeguridadService seguridadService;
    private final PromotorService promotorService;


    @org.springframework.beans.factory.annotation.Autowired
    public OperacionApiController(
            ObjectMapper objectMapper,
            HttpServletRequest request,
            PrepagoService prepagoService,
            IngresoService ingresoService,
            ConfirmarService confirmarService,
            ReportarService reportarService,
            ExtemporaneoService extemporaneoService,
            PreliquidarService preliquidarService,
            InformacionService informacionService,
            CierreSistemaService cierreSistemaService,
            SeguridadService seguridadService,
            PromotorService promotorService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.prepagoService = prepagoService;
        this.ingresoService = ingresoService;
        this.confirmarService = confirmarService;
        this.reportarService = reportarService;
        this.extemporaneoService = extemporaneoService;
        this.preliquidarService = preliquidarService;
        this.informacionService = informacionService;
        this.cierreSistemaService = cierreSistemaService;
        this.seguridadService = seguridadService;
        this.promotorService = promotorService;
    }

    @Scheduled(cron = "0 40 23 * * ?", zone = "America/Bogota")//segundos minutos hora dia_del_mes mes dia_de_la_semana
    @Transactional
    protected void limpiezaNocturna() {
        LocalTime tiempoActual = LocalTime.now(ZoneId.of("America/Bogota"));
        LocalTime tiempoReferenciaInicial = LocalTime.of(23, 30);
        LocalTime tiempoReferenciaFinal = LocalTime.of(23, 59);
        if (tiempoActual.isAfter(tiempoReferenciaInicial) &&
                tiempoActual.isBefore(tiempoReferenciaFinal)) {
            IRegistroDAO iRegistroDAO = new RegistroDAO();
            IZonaDAO iZonaDAO = new ZonaDAO();
            ICuentasDAO iCuentasDAO = new CuentasDAO();
            try {
                Object resultado = cierreSistemaService.cierreMasivoSistema(iRegistroDAO, iZonaDAO, iCuentasDAO);
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Verifica el permiso según los perfiles, la sesión y la cuenta
     *
     * @param cuenta
     * @param perfiles
     * @throws Exception
     */
    private void aplicarSeguridad(String cuenta, int... perfiles) throws Exception {
        ISesionDAO iSesionDAO = new SesionDAO();
        seguridadService.validarPermiso(iSesionDAO, cuenta, request, perfiles);
    }

    @Override
    public ResponseEntity<Object> registrarPrepago(String cuenta, @Valid Registro body) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta, SeguridadService.PROMOTOR, SeguridadService.SUPERVISOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IRegistroDAO iRegistroDAO = new RegistroDAO();
            IZonaDAO iZonaDAO = new ZonaDAO();
            Object resultado = prepagoService.ingresoPrepago(cuenta, body, iRegistroDAO, iZonaDAO, cierreSistemaService);
            return new ResponseEntity<>(resultado, null, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> ingresarVehiculo(String cuenta, @Valid Registro body) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.PROMOTOR,
                    SeguridadService.SUPERVISOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IRegistroDAO iRegistroDAO = new RegistroDAO();
            IZonaDAO iZonaDAO = new ZonaDAO();
            Object resultado = ingresoService.ingresarPostpago(cuenta, body, iRegistroDAO, iZonaDAO, cierreSistemaService);
            return new ResponseEntity<>(resultado, null, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> confirmarPago(String cuenta, @NotNull @Valid Long idPago, @NotNull @Valid String placa, @NotNull @Valid String fhliquidacion, @NotNull @Valid Long promotor) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.PROMOTOR,
                    SeguridadService.SUPERVISOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IRegistroDAO iRegistroDAO = new RegistroDAO();
            IZonaDAO iZonaDAO = new ZonaDAO();
            Object resultado = confirmarService.confirmarPago(cuenta, placa, idPago, promotor, fhliquidacion, iRegistroDAO, iZonaDAO);
            return new ResponseEntity<>(resultado, null, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> reportarVehiculo(String cuenta, @NotNull @Valid Long idCuenta, @NotNull @Valid Long promotor, @NotNull @Valid String placa) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.PROMOTOR,
                    SeguridadService.SUPERVISOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IRegistroDAO iRegistroDAO = new RegistroDAO();
            IZonaDAO iZonaDAO = new ZonaDAO();
            Object resultado = reportarService.confirmarReporte(cuenta, placa, idCuenta, promotor, iRegistroDAO, iZonaDAO);
            return new ResponseEntity<>(resultado, null, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> pagoExtemporaneoFromWEB(String cuenta, @NotNull @Valid Long idPago, @NotNull @Valid String placa) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.ADMINISTRADOR,
                    SeguridadService.AUXILIAR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IRegistroDAO iRegistroDAO = new RegistroDAO();
            IZonaDAO iZonaDAO = new ZonaDAO();
            Object resultado = extemporaneoService.pagoExtemporaneoWEB(cuenta, placa, idPago, iRegistroDAO, iZonaDAO);
            return new ResponseEntity<>(resultado, null, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> pagoExtemporaneo(String cuenta, @NotNull @Valid Long idPago, @NotNull @Valid Long promotor, @NotNull @Valid String placa) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.PROMOTOR,
                    SeguridadService.SUPERVISOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IRegistroDAO iRegistroDAO = new RegistroDAO();
            IZonaDAO iZonaDAO = new ZonaDAO();
            Object resultado = extemporaneoService.pagoExtemporaneo(cuenta, placa, idPago, promotor, iRegistroDAO, iZonaDAO);
            return new ResponseEntity<>(resultado, null, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> liquidarVehiculo(String cuenta, @NotNull @Valid Long idCuenta, @NotNull @Valid String placa) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.PROMOTOR,
                    SeguridadService.SUPERVISOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IRegistroDAO iRegistroDAO = new RegistroDAO();
            IZonaDAO iZonaDAO = new ZonaDAO();
            Object resultado = preliquidarService.preliquidarCuenta(cuenta, placa, idCuenta, iRegistroDAO, iZonaDAO);
            return new ResponseEntity<>(resultado, null, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    /**
     * Sirve para retornar un valor enviado multiplicado por 2,
     * con esto el cliente puede saber si se proceso correctamente y si se encuentra en línea
     *
     * @param cuenta
     * @param valorReferencia
     * @return
     */
    @Override
    public ResponseEntity<Object> getProbarOnline(String cuenta, Long valorReferencia) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.PROMOTOR,
                    SeguridadService.SUPERVISOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            return new ResponseEntity<>(valorReferencia * 2, null, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> getEstadoZona(String cuenta, @NotNull @Valid Long zona) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.PROMOTOR,
                    SeguridadService.SUPERVISOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IRegistroDAO iRegistroDAO = new RegistroDAO();
            IZonaDAO iZonaDAO = new ZonaDAO();
            Object resultado = preliquidarService.preliquidarZona(cuenta, zona, iRegistroDAO, iZonaDAO);
            return new ResponseEntity<>(resultado, null, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> getOcupacionZona(String cuenta, @NotNull @Valid Long zona) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta, SeguridadService.PROMOTOR, SeguridadService.SUPERVISOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IRegistroDAO iRegistroDAO = new RegistroDAO();
            IZonaDAO iZonaDAO = new ZonaDAO();
            Object resultado = informacionService.ocupacionZona(cuenta, zona, iRegistroDAO, iZonaDAO);
            return new ResponseEntity<>(resultado, null, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> getCarteraPlaca(String cuenta, @NotNull @Valid String placa) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.PROMOTOR,
                    SeguridadService.SUPERVISOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IRegistroDAO iRegistroDAO = new RegistroDAO();
            IZonaDAO iZonaDAO = new ZonaDAO();
            Object resultado = informacionService.getCarteraPorPlaca(cuenta, placa, iRegistroDAO, iZonaDAO, preliquidarService);
            return new ResponseEntity<>(resultado, null, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> getUltimosMovimientosHoy(String cuenta, @NotNull @Valid String placa) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.PROMOTOR,
                    SeguridadService.SUPERVISOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IRegistroDAO iRegistroDAO = new RegistroDAO();
            Object resultado = informacionService.getUltimosMovimientosHoy(cuenta, placa,
                    iRegistroDAO);
            return new ResponseEntity<>(resultado, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> salioPrepago(String cuenta, @NotNull @Valid Long idPago) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.PROMOTOR,
                    SeguridadService.SUPERVISOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IRegistroDAO iRegistroDAO = new RegistroDAO();
            Object resultado = prepagoService.salioPrepagoActualizar(cuenta, idPago,
                    iRegistroDAO);
            return new ResponseEntity<>(resultado, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> getAlertasPrepagoZona(String cuenta, @NotNull @Valid Long zona) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.PROMOTOR,
                    SeguridadService.SUPERVISOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            LocalDateTime localDateTime = Utilidades.getLocalDateTime();
            IRegistroDAO iRegistroDAO = new RegistroDAO();
            IZonaDAO iZonaDAO = new ZonaDAO();
            Object resultado = informacionService.getAlertasPrepagoZona(cuenta,
                    zona,
                    iRegistroDAO,
                    iZonaDAO,
                    localDateTime);
            return new ResponseEntity<>(resultado, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> getSuperaLimiteDeEndeudamiento(String cuenta, Long promotor) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.PROMOTOR,
                    SeguridadService.SUPERVISOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IPromotorDAO iPromotorDAO = new PromotorDAO();
            Object resultado = promotorService.superaLimiteEndeudamiento(cuenta,
                    iPromotorDAO,
                    promotor);
            return new ResponseEntity<>(resultado, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> obtenerTotalRecaudado(String cuenta, Long idPromotor) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IPromotorDAO iPromotorDAO = new PromotorDAO();
            Object resultado = promotorService.obtenerTotalRecaudado(cuenta,
                    iPromotorDAO,
                    idPromotor
                    );
            return new ResponseEntity<>(resultado, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> estaBloqueado(String cuenta, Long promotor
    ) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IPromotorDAO iPromotorDAO = new PromotorDAO();
            boolean estaBloqueado = promotorService.estaBloqueado(cuenta,
                    iPromotorDAO,
                    promotor);
            Object resultado = promotorService.estaBloqueado(cuenta,
                    iPromotorDAO,
                    promotor);
            return new ResponseEntity<>(resultado, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> bloquearPromotor(String cuenta, Long promotor
    ) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
        respuestaError = HttpStatus.NOT_ACCEPTABLE;
        IPromotorDAO iPromotorDAO = new PromotorDAO();
            promotorService.bloquearPromotor(cuenta,
                    iPromotorDAO,
                    promotor);
            JSONObject response = new JSONObject();
            response.put("message", "Promotor bloqueado correctamente");
            return new ResponseEntity<>(response.toString(), null, HttpStatus.OK);
        } catch (Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
    }
    }

    @Override
    public ResponseEntity<Object> desbloquearPromotor(String cuenta, Long promotor, Long supervisor
    ) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IPromotorDAO iPromotorDAO = new PromotorDAO();
            promotorService.desbloquearPromotor(cuenta, iPromotorDAO, promotor, supervisor);
            return new ResponseEntity<>("Promotor desbloqueado correctamente", null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }
    @Override
    public ResponseEntity<Object> listarBloqueados(String cuenta) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IPromotorDAO iPromotorDAO = new PromotorDAO();
            Object resultado = promotorService.listarBloqueados(cuenta, iPromotorDAO);
            return new ResponseEntity<>(resultado, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.ofNullable(objectMapper);
    }

    @Override
    public Optional<HttpServletRequest> getRequest() {
        return Optional.ofNullable(request);
    }

}
