package co.zer.api;

import co.zer.model.Paginacion;
import co.zer.model.Usuario;
import co.zer.repository.ISesionDAO;
import co.zer.repository.IUsuarioDAO;
import co.zer.repository.SesionDAO;
import co.zer.repository.UsuarioDAO;
import co.zer.service.SeguridadService;
import co.zer.service.UsuarioService;
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

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-11-17T15:56:48.256Z[GMT]")
@RestController
@CrossOrigin(origins = "*")
public class UsuarioApiController implements UsuarioApi {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final UsuarioService usuarioService;

    private final SeguridadService seguridadService;


    @org.springframework.beans.factory.annotation.Autowired
    public UsuarioApiController(
            ObjectMapper objectMapper,
            HttpServletRequest request,
            UsuarioService usuarioService,
            SeguridadService seguridadService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.usuarioService = usuarioService;
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
    public ResponseEntity<Object> getListadoDocumentos(String cuenta) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,SeguridadService.CREADOR_CUENTAS,SeguridadService.ADMINISTRADOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IUsuarioDAO iUsuarioDAO = new UsuarioDAO();
            Map<String, Object> resultado = usuarioService.listarDocumentos(cuenta, iUsuarioDAO);
            return new ResponseEntity<>(resultado, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> getListadoPerfiles(String cuenta) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,SeguridadService.CREADOR_CUENTAS,SeguridadService.ADMINISTRADOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IUsuarioDAO iUsuarioDAO = new UsuarioDAO();
            Map<String, Object> resultado = usuarioService.listarPerfiles(cuenta, iUsuarioDAO);
            return new ResponseEntity<>(resultado, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> getListadoUsuarios(String cuenta, @NotNull @Valid Long limite, @NotNull @Valid Long actual, @Valid String filtro, @Valid String orden, @Valid String sentido) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,
                    SeguridadService.CREADOR_CUENTAS,
                    SeguridadService.ADMINISTRADOR,
                    SeguridadService.SUPERVISOR,
                    SeguridadService.AUXILIAR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IUsuarioDAO iUsuarioDAO = new UsuarioDAO();
            Paginacion paginacion = new Paginacion();
            paginacion.setLimite(limite);
            paginacion.setActual(actual);
            paginacion.setFiltro(filtro);
            paginacion.setOrden(orden);
            paginacion.setSentido(sentido);
            Map<String, Object> resultado = usuarioService.listarUsuarios(cuenta, iUsuarioDAO, paginacion);
            return new ResponseEntity<>(resultado, null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), null, respuestaError);
        }
    }

    @Override
    public ResponseEntity<Object> guardarUsuario(String cuenta, @Valid Usuario body) {
        HttpStatus respuestaError = HttpStatus.UNAUTHORIZED;
        try {
            aplicarSeguridad(cuenta,SeguridadService.CREADOR_CUENTAS,SeguridadService.ADMINISTRADOR);
            respuestaError = HttpStatus.NOT_ACCEPTABLE;
            IUsuarioDAO iUsuarioDAO = new UsuarioDAO();
            Usuario resultado = usuarioService.guardar(cuenta, iUsuarioDAO, body);
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
