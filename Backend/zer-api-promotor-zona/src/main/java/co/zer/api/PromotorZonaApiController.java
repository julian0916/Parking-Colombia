package co.zer.api;

import co.zer.model.Paginacion;
import co.zer.model.PromotorZona;
import co.zer.repository.IPromotorZonaDAO;
import co.zer.repository.ISesionDAO;
import co.zer.repository.PromotorZonaDAO;
import co.zer.repository.SesionDAO;
import co.zer.service.PromotorZonaService;
import co.zer.service.SeguridadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-11-20T19:19:57.576Z[GMT]")
@RestController
@CrossOrigin(origins = "*")

public class PromotorZonaApiController implements PromotorZonaApi {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final PromotorZonaService promotorZonaService;

    private final SeguridadService seguridadService;


    @org.springframework.beans.factory.annotation.Autowired
    public PromotorZonaApiController(
            ObjectMapper objectMapper,
            HttpServletRequest request,
            PromotorZonaService promotorZonaService,
            SeguridadService seguridadService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.promotorZonaService = promotorZonaService;
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
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.ofNullable(objectMapper);
    }

    @Override
    public Optional<HttpServletRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Object> guardarPromotorZona(String cuenta, @Valid PromotorZona body) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,SeguridadService.ADMINISTRADOR,SeguridadService.SUPERVISOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IPromotorZonaDAO iPromotorZonaDAO = new PromotorZonaDAO();
            PromotorZona resultado = promotorZonaService.guardar(cuenta, iPromotorZonaDAO, body);
            return new ResponseEntity<>(resultado, null, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> getListadoPromotorZonas(String cuenta, @NotNull @Valid Long limite, @NotNull @Valid Long actual, @Valid String filtro, @Valid String orden, @Valid String sentido) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,SeguridadService.ADMINISTRADOR,SeguridadService.SUPERVISOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IPromotorZonaDAO iPromotorZonaDAO = new PromotorZonaDAO();
            Paginacion paginacion = new Paginacion();
            paginacion.setLimite(limite);
            paginacion.setActual(actual);
            paginacion.setFiltro(filtro);
            paginacion.setOrden(orden);
            paginacion.setSentido(sentido);
            Map<String, Object> resultado = promotorZonaService.listarPromotoresZonas(cuenta, iPromotorZonaDAO, paginacion);
            return new ResponseEntity<>(resultado, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> getListadoZonasDelPromor(String cuenta, @NotNull @Valid String correoPromotor) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.ADMINISTRADOR,
                    SeguridadService.PROMOTOR,
                    SeguridadService.SUPERVISOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IPromotorZonaDAO iPromotorZonaDAO = new PromotorZonaDAO();
            List<Object> resultado = promotorZonaService.listarZonasAsignadasPromotor(cuenta, iPromotorZonaDAO, correoPromotor);
            return new ResponseEntity<>(resultado, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> getListadoPromotoresEnZona(String cuenta, @NotNull @Valid Long zona) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.ADMINISTRADOR,
                    SeguridadService.PROMOTOR,
                    SeguridadService.SUPERVISOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IPromotorZonaDAO iPromotorZonaDAO = new PromotorZonaDAO();
            List<Object> resultado = promotorZonaService.listarPromotoresAsignadosAZona(cuenta, iPromotorZonaDAO, zona);
            return new ResponseEntity<>(resultado, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }
}
