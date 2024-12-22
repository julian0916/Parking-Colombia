package co.zer.api;

import co.zer.model.PreguntaSupervision;
import co.zer.model.RegistroSupervision;
import co.zer.repository.ISesionDAO;
import co.zer.repository.ISupervisionDAO;
import co.zer.repository.SesionDAO;
import co.zer.repository.SupervisionDAO;
import co.zer.service.SeguridadService;
import co.zer.service.SupervisionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-01-09T17:10:57.548Z[GMT]")
@RestController
@CrossOrigin(origins = "*")
public class SupervisionApiController implements SupervisionApi {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final SupervisionService supervisionService;

    private final SeguridadService seguridadService;


    @org.springframework.beans.factory.annotation.Autowired
    public SupervisionApiController(
            ObjectMapper objectMapper,
            HttpServletRequest request,
            SupervisionService supervisionService,
            SeguridadService seguridadService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.supervisionService = supervisionService;
        this.seguridadService = seguridadService;
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
    public ResponseEntity<Object> getPreguntasSupervision(String cuenta) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.ADMINISTRADOR,
                    SeguridadService.SUPERVISOR,
                    SeguridadService.VISOR);

            respuestaError = HttpStatus.NOT_ACCEPTABLE;

            ISupervisionDAO iSupervisionDAO = new SupervisionDAO();
            List<PreguntaSupervision> preguntas = supervisionService.consultar(cuenta, iSupervisionDAO);
            return new ResponseEntity<>(preguntas, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> registrarPregunta(String cuenta, @Valid PreguntaSupervision body) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.ADMINISTRADOR,
                    SeguridadService.SUPERVISOR);

            respuestaError = HttpStatus.NOT_ACCEPTABLE;

            ISupervisionDAO iSupervisionDAO = new SupervisionDAO();
            List<PreguntaSupervision> preguntas = supervisionService.guardar(cuenta, iSupervisionDAO, body);
            return new ResponseEntity<>(preguntas, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> borrarPreguntaSupervision(String cuenta, Long id) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.ADMINISTRADOR,
                    SeguridadService.SUPERVISOR);

            respuestaError = HttpStatus.NOT_ACCEPTABLE;

            ISupervisionDAO iSupervisionDAO = new SupervisionDAO();
            List<PreguntaSupervision> preguntas = supervisionService.borrar(cuenta, iSupervisionDAO, id);
            return new ResponseEntity<>(preguntas, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> registrarSupervision(String cuenta, @Valid RegistroSupervision registroSupervision) {
        Map<String, String> resp = new HashMap<>();
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.ADMINISTRADOR,
                    SeguridadService.SUPERVISOR);

            respuestaError = HttpStatus.NOT_ACCEPTABLE;

            ISupervisionDAO iSupervisionDAO = new SupervisionDAO();
            registroSupervision.decodificarContenido();
            supervisionService.guardarRegistroSupervision(cuenta, iSupervisionDAO, registroSupervision);
            resp.put("mensaje", "Registro de supervisión correctamente almacenado");
            return new ResponseEntity<>(resp, null, HttpStatus.OK);
        } catch (Exception ex) {
            resp.put("mensaje", ex.getMessage());
            return new ResponseEntity<>(resp, null, respuestaError);
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
