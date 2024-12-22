package co.zer.api;

import co.zer.model.Cuenta;
import co.zer.model.Paginacion;
import co.zer.repository.CuentaDAO;
import co.zer.repository.ICuentaDAO;
import co.zer.repository.ISesionDAO;
import co.zer.repository.SesionDAO;
import co.zer.service.CuentaService;
import co.zer.service.SeguridadService;
import co.zer.utils.SendEmail;
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

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-11-11T13:26:34.040Z[GMT]")
@RestController
@CrossOrigin(origins = "*")
public class CuentaApiController implements CuentaApi {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final CuentaService cuentaService;

    private final SeguridadService seguridadService;

    @org.springframework.beans.factory.annotation.Autowired
    public CuentaApiController(
            ObjectMapper objectMapper,
            HttpServletRequest request,
            CuentaService cuentaService,
            SeguridadService seguridadService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.cuentaService = cuentaService;
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
    public ResponseEntity<Object> guardarCuenta(@Valid Cuenta body) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad("",SeguridadService.CREADOR_CUENTAS);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            ICuentaDAO iCuentaDAO = new CuentaDAO();
            Cuenta resultado = cuentaService.guardar(iCuentaDAO, body);
            return new ResponseEntity<>(resultado, null, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> getListadoCuenta(@NotNull @Valid Long limite, @NotNull @Valid Long actual, @Valid String filtro, @Valid String orden, @Valid String sentido) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad("",SeguridadService.CREADOR_CUENTAS);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            ICuentaDAO iCuentaDAO = new CuentaDAO();
            Paginacion paginacion = new Paginacion();
            paginacion.setLimite(limite);
            paginacion.setActual(actual);
            paginacion.setFiltro(filtro);
            paginacion.setOrden(orden);
            paginacion.setSentido(sentido);
            Map<String, Object> resultado = cuentaService.listarCuentas(iCuentaDAO, paginacion);
            return new ResponseEntity<>(resultado, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> getListadoByUser(String usuario) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad("",SeguridadService.CREADOR_CUENTAS);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            ICuentaDAO iCuentaDAO = new CuentaDAO();
            Map<String, Object> resultado = cuentaService.listarCuentasUsuario(iCuentaDAO, usuario);
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
