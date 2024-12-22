package co.zer.api;

import co.zer.model.Paginacion;
import co.zer.repository.*;
import co.zer.service.FinanzasService;
import co.zer.service.RegistroService;
import co.zer.service.SeguridadService;
import co.zer.service.SupervisionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-01-18T16:20:57.176Z[GMT]")
@RestController
@CrossOrigin(origins = "*")
public class ReportesApiController implements ReportesApi {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final SupervisionService supervisionService;

    private final SeguridadService seguridadService;

    private final FinanzasService finanzasService;

    private final RegistroService registroService;

    @org.springframework.beans.factory.annotation.Autowired
    public ReportesApiController(
            ObjectMapper objectMapper,
            HttpServletRequest request,
            SupervisionService supervisionService,
            SeguridadService seguridadService,
            FinanzasService finanzasService,
            RegistroService registroService
    ) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.supervisionService = supervisionService;
        this.seguridadService = seguridadService;
        this.finanzasService = finanzasService;
        this.registroService = registroService;
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
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.ofNullable(objectMapper);
    }

    @Override
    public Optional<HttpServletRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Object> getReporteSupervision(
            String cuenta,
            @NotNull @Valid Long fechaConsultaInicial,
            @NotNull @Valid Long fechaConsultaFinal,
            @NotNull @Valid Long promotor,
            @NotNull @Valid Boolean traerTodo,
            @Valid Long limite,
            @Valid Long actual,
            @Valid String orden,
            @Valid String sentido) {

        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.ADMINISTRADOR,
                    SeguridadService.SUPERVISOR,
                    SeguridadService.AUXILIAR,
                    SeguridadService.VISOR);

            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            Paginacion paginacion = new Paginacion();
            paginacion.setLimite(limite);
            paginacion.setActual(actual);
            paginacion.setOrden(orden);
            paginacion.setSentido(sentido);
            paginacion.setTraerTodo(traerTodo);
            ISupervisionDAO iSupervisionDAO = new SupervisionDAO();
            Map<String, Object> reporte = supervisionService.reporte(
                    cuenta,
                    iSupervisionDAO,
                    fechaConsultaInicial,
                    fechaConsultaFinal,
                    promotor,
                    paginacion);
            return new ResponseEntity<>(reporte, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> getReporteSaldosPromotores(
            String cuenta,
            Boolean activos,
            @NotNull @Valid Long promotor,
            @NotNull @Valid Boolean traerTodo,
            @Valid Long limite,
            @Valid Long actual,
            @Valid String orden,
            @Valid String sentido) {

        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.ADMINISTRADOR,
                    SeguridadService.SUPERVISOR,
                    SeguridadService.AUXILIAR,
                    SeguridadService.VISOR);

            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            Paginacion paginacion = new Paginacion();
            paginacion.setLimite(limite);
            paginacion.setActual(actual);
            paginacion.setOrden(orden);
            paginacion.setSentido(sentido);
            paginacion.setTraerTodo(traerTodo);
            IFinancieraDAO iFinancieraDAO = new FinancieraDAO();
            Map<String, Object> reporte = finanzasService.reporte(
                    cuenta,
                    iFinancieraDAO,
                    activos,
                    promotor,
                    paginacion);
            return new ResponseEntity<>(reporte, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> getReporteHistorico(String cuenta, Long fechaConsulta) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.ADMINISTRADOR,
                    SeguridadService.AUXILIAR,
                    SeguridadService.VISOR
            );

            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IRegistroDAO iRegistroDAO = new RegistroDAO();
            Map<String, Object> reporte = registroService.reporteHistorico(
                    cuenta,
                    iRegistroDAO,
                    fechaConsulta);
            return new ResponseEntity<>(reporte, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> getReporteHistoricoMensual(
            String cuenta,
            Integer mes,
            Integer year) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.ADMINISTRADOR,
                    SeguridadService.AUXILIAR,
                    SeguridadService.VISOR);

            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            Paginacion paginacion = new Paginacion();
            IRegistroDAO iRegistroDAO = new RegistroDAO();
            Map<String, Object> reporte = registroService.reporteHistoricoMensual(
                    cuenta,
                    iRegistroDAO,
                    mes,
                    year);
            return new ResponseEntity<>(reporte, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }


    @Override
    public ResponseEntity<Object> getReporteMensual(String cuenta, @NotNull @Valid Boolean enero, @NotNull @Valid Boolean febrero, @NotNull @Valid Boolean marzo, @NotNull @Valid Boolean abril, @NotNull @Valid Boolean mayo, @NotNull @Valid Boolean junio, @NotNull @Valid Boolean julio, @NotNull @Valid Boolean agosto, @NotNull @Valid Boolean septiembre, @NotNull @Valid Boolean octubre, @NotNull @Valid Boolean noviembre, @NotNull @Valid Boolean diciembre, @Valid Long nfInicial) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.ADMINISTRADOR,
                    SeguridadService.AUXILIAR,
                    SeguridadService.VISOR);

            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            Paginacion paginacion = new Paginacion();
            IRegistroDAO iRegistroDAO = new RegistroDAO();
            Map<String, Object> reporte = registroService.reporte(
                    cuenta,
                    iRegistroDAO,
                    nfInicial,
                    enero,
                    febrero,
                    marzo,
                    abril,
                    mayo,
                    junio,
                    julio,
                    agosto,
                    septiembre,
                    octubre,
                    noviembre,
                    diciembre);
            return new ResponseEntity<>(reporte, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> getReporteVehiculo(String cuenta, Long nfInicial, Long nfFinal, String placa, @Valid Long limite, @Valid Long actual, @Valid String orden, @Valid String sentido) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            
            Paginacion paginacion = new Paginacion();
            paginacion.setLimite(limite);
            paginacion.setActual(actual);
            paginacion.setOrden(orden);
            paginacion.setSentido(sentido);
            IRegistroDAO iRegistroDAO = new RegistroDAO();
            Map<String, Object> reporte = registroService.reporteVehiculo(
                    cuenta,
                    iRegistroDAO,
                    nfInicial,
                    nfFinal,
                    placa,
                    paginacion);
            return new ResponseEntity<>(reporte, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> getReporteCartera(String cuenta, Long nfInicial, Long nfFinal, @Valid Long limite, @Valid Long actual, @Valid String orden, @Valid String sentido) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.ADMINISTRADOR,
                    SeguridadService.AUXILIAR,
                    SeguridadService.VISOR);

            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            Paginacion paginacion = new Paginacion();
            paginacion.setLimite(limite);
            paginacion.setActual(actual);
            paginacion.setOrden(orden);
            paginacion.setSentido(sentido);
            IRegistroDAO iRegistroDAO = new RegistroDAO();
            Map<String, Object> reporte = registroService.reporteCartera(
                    cuenta,
                    iRegistroDAO,
                    nfInicial,
                    nfFinal,
                    paginacion);
            return new ResponseEntity<>(reporte, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }
}
