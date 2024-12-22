package co.zer.api;

import co.zer.model.Paginacion;
import co.zer.model.Zona;
import co.zer.repository.ISesionDAO;
import co.zer.repository.IZonaDAO;
import co.zer.repository.SesionDAO;
import co.zer.repository.ZonaDAO;
import co.zer.service.SeguridadService;
import co.zer.service.ZonaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Optional;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-11-19T18:57:30.157Z[GMT]")
@RestController
@CrossOrigin(origins = "*")
public class ZonaApiController implements ZonaApi {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final ZonaService zonaService;

    private final SeguridadService seguridadService;


    @org.springframework.beans.factory.annotation.Autowired
    public ZonaApiController(
            ObjectMapper objectMapper,
            HttpServletRequest request,
            ZonaService zonaService,
            SeguridadService seguridadService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.zonaService = zonaService;
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
    public ResponseEntity<Object> guardarZona(String cuenta, @Valid Zona body) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,SeguridadService.ADMINISTRADOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IZonaDAO iZonaDAO = new ZonaDAO();
            Zona resultado = zonaService.guardar(cuenta, iZonaDAO, body);
            return new ResponseEntity<>(resultado, null, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> getListadoZonas(String cuenta, @NotNull @Valid Long limite, @NotNull @Valid Long actual, @Valid String filtro, @Valid String orden, @Valid String sentido) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,SeguridadService.ADMINISTRADOR,SeguridadService.SUPERVISOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IZonaDAO iZonaDAO = new ZonaDAO();
            Paginacion paginacion = new Paginacion();
            paginacion.setLimite(limite);
            paginacion.setActual(actual);
            paginacion.setFiltro(filtro);
            paginacion.setOrden(orden);
            paginacion.setSentido(sentido);
            Map<String, Object> resultado = zonaService.listarZonas(cuenta, iZonaDAO, paginacion);
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
