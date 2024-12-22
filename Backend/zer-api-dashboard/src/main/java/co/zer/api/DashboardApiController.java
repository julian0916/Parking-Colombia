package co.zer.api;

import co.zer.repository.IRegistroDAO;
import co.zer.repository.ISesionDAO;
import co.zer.repository.RegistroDAO;
import co.zer.repository.SesionDAO;
import co.zer.service.DashboardService;
import co.zer.service.SeguridadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-12-14T20:53:20.588Z[GMT]")
@RestController
@CrossOrigin(origins = "*")
public class DashboardApiController implements DashboardApi {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final DashboardService dashboardService;

    private final SeguridadService seguridadService;


    @org.springframework.beans.factory.annotation.Autowired
    public DashboardApiController(
            ObjectMapper objectMapper,
            HttpServletRequest request,
            DashboardService dashboardService,
            SeguridadService seguridadService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.dashboardService = dashboardService;
        this.seguridadService = seguridadService;
    }

    /**
     * Verifica el permiso según los perfiles, la sesión y la cuenta
     * @param cuenta
     * @param perfiles
     * @throws Exception
     */
    private void aplicarSeguridad(String cuenta, int... perfiles) throws Exception {
        ISesionDAO iSesionDAO = new SesionDAO();
        seguridadService.validarPermiso(iSesionDAO, cuenta, request, perfiles);
    }

    @Override
    public ResponseEntity<Object> getDatosDashboard(String cuenta, @NotNull @Valid Long fnInicio, @NotNull @Valid Long fnFin) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,SeguridadService.ADMINISTRADOR,SeguridadService.MUNICIPIO,SeguridadService.VISOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IRegistroDAO iRegistroDAO = new RegistroDAO();
            Object resultado = dashboardService.getDatosDashboard(cuenta, fnInicio, fnFin, iRegistroDAO);
            return new ResponseEntity<>(resultado, null, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> getOcupacionZonaHora(String cuenta, @NotNull @Valid String fhConsulta) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,SeguridadService.ADMINISTRADOR,SeguridadService.MUNICIPIO,SeguridadService.VISOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IRegistroDAO iRegistroDAO = new RegistroDAO();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime fLDConsulta = LocalDateTime.parse(fhConsulta,formatter);
            Object resultado = dashboardService.getOcupacionZona(cuenta, fLDConsulta, iRegistroDAO);
            return new ResponseEntity<>(resultado, null, HttpStatus.CREATED);
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
