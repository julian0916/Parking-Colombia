package co.zer.api;

import co.zer.model.PagoMunicipio;
import co.zer.model.Recaudo;
import co.zer.repository.FinancieraDAO;
import co.zer.repository.IFinancieraDAO;
import co.zer.repository.ISesionDAO;
import co.zer.repository.SesionDAO;
import co.zer.service.FinancieraService;
import co.zer.service.SeguridadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-12-19T16:01:03.579Z[GMT]")
@RestController
@CrossOrigin(origins = "*")
public class FinancieraApiController implements FinancieraApi {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final FinancieraService financieraService;

    private final SeguridadService seguridadService;

    @org.springframework.beans.factory.annotation.Autowired
    public FinancieraApiController(
            ObjectMapper objectMapper,
            HttpServletRequest request,
            FinancieraService financieraService,
            SeguridadService seguridadService) {

        this.objectMapper = objectMapper;
        this.request = request;
        this.financieraService = financieraService;
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
    public ResponseEntity<Object> getRecaudosReportes(String cuenta, @NotNull @Valid Long fnRecaudo) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta, SeguridadService.AUXILIAR, SeguridadService.ADMINISTRADOR, SeguridadService.VISOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IFinancieraDAO iFinancieraDAO = new FinancieraDAO();
            Object resultado = financieraService.getListadoRecaudoReportadoFecha(cuenta, fnRecaudo, iFinancieraDAO);
            return new ResponseEntity<>(resultado, null, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> registrarRecaudoPromotor(String cuenta, @Valid Recaudo body) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta, SeguridadService.AUXILIAR, SeguridadService.ADMINISTRADOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IFinancieraDAO iFinancieraDAO = new FinancieraDAO();
            Object resultado = financieraService.guardarRecaudo(cuenta, body, iFinancieraDAO);
            return new ResponseEntity<>(resultado, null, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> getPagoMunicipio(String cuenta, @NotNull @Valid Long fnPago) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta, SeguridadService.AUXILIAR, SeguridadService.ADMINISTRADOR, SeguridadService.VISOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IFinancieraDAO iFinancieraDAO = new FinancieraDAO();
            Object resultado = financieraService.getPagoMunicipio(cuenta, fnPago, iFinancieraDAO);
            return new ResponseEntity<>(resultado, null, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> registrarPagoMunicipio(String cuenta, @Valid PagoMunicipio body) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta, SeguridadService.AUXILIAR, SeguridadService.ADMINISTRADOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IFinancieraDAO iFinancieraDAO = new FinancieraDAO();
            Object resultado = financieraService.guardarPagoMunicipio(cuenta, body, iFinancieraDAO);
            return new ResponseEntity<>(resultado, null, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> getValidarFechaPagoMunicipio(String cuenta, @NotNull @Valid String fPago) {
        Object resultado = financieraService.getValidarFechaPagoMunicipio(cuenta, fPago);
        return new ResponseEntity<>(resultado, null, HttpStatus.CREATED);
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
