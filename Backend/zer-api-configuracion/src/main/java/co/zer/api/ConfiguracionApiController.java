package co.zer.api;

import co.zer.model.Tiquete;
import co.zer.repository.ConfiguracionDAO;
import co.zer.repository.IConfiguracionDAO;
import co.zer.repository.ISesionDAO;
import co.zer.repository.SesionDAO;
import co.zer.service.ConfiguracionService;
import co.zer.service.SeguridadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-01-22T21:02:22.607Z[GMT]")
@RestController
@CrossOrigin(origins = "*")
public class ConfiguracionApiController implements ConfiguracionApi {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final ConfiguracionService configuracionService;

    private final SeguridadService seguridadService;

    @org.springframework.beans.factory.annotation.Autowired
    public ConfiguracionApiController(
            ObjectMapper objectMapper,
            HttpServletRequest request,
            ConfiguracionService configuracionService,
            SeguridadService seguridadService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.configuracionService = configuracionService;
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
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.ofNullable(objectMapper);
    }

    @Override
    public Optional<HttpServletRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Object> getTiquete(String cuenta) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.ADMINISTRADOR,
                    SeguridadService.PROMOTOR,
                    SeguridadService.SUPERVISOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IConfiguracionDAO iConfiguracionDAO = new ConfiguracionDAO();
            Tiquete tiquete = configuracionService.consultar(cuenta, iConfiguracionDAO);
            return new ResponseEntity<>(tiquete, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> guardarTiquete(String cuenta, @Valid Tiquete body) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta, SeguridadService.ADMINISTRADOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IConfiguracionDAO iConfiguracionDAO = new ConfiguracionDAO();
            Tiquete resultado = configuracionService.guardar(cuenta, iConfiguracionDAO, body);
            return new ResponseEntity<>(resultado, null, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> guardarLimiteEndeudamientoPromotor(String cuenta, Long valor) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta, SeguridadService.ADMINISTRADOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IConfiguracionDAO iConfiguracionDAO = new ConfiguracionDAO();
            configuracionService.guardarLimiteEndeudamientoPromotor(cuenta, iConfiguracionDAO, valor);
            return new ResponseEntity<>(valor, null, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> getLimiteEndeudamientoPromotor(String cuenta) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.ADMINISTRADOR,
                    SeguridadService.PROMOTOR,
                    SeguridadService.SUPERVISOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IConfiguracionDAO iConfiguracionDAO = new ConfiguracionDAO();
            Long valor = configuracionService.consultarLimiteEndeudamientoPromotor(cuenta, iConfiguracionDAO);
            return new ResponseEntity<>(valor, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }
}
