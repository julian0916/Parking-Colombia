package co.zer.api;

import co.zer.model.DiffRespuesta;
import co.zer.repository.AutorizarDAO;
import co.zer.repository.IAutorizarDAO;
import co.zer.repository.ISesionDAO;
import co.zer.repository.SesionDAO;
import co.zer.service.AutenticacionService;
import co.zer.service.AutorizacionService;
import co.zer.utils.Hash;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-10-29T13:54:35.081Z[GMT]")
@Controller
@CrossOrigin(origins = "*")
public class AccesoApiController implements AccesoApi {

    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    private final AutenticacionService autenticacionService;
    private final AutorizacionService autorizacionService;

    @org.springframework.beans.factory.annotation.Autowired
    public AccesoApiController(ObjectMapper objectMapper, HttpServletRequest request, AutenticacionService autenticacionService, AutorizacionService autorizacionService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.autenticacionService = autenticacionService;
        this.autorizacionService = autorizacionService;
    }

    @Scheduled(cron = "* 30 * * * ?", zone = "America/Bogota")//segundos minutos hora dia_del_mes mes dia_de_la_semana
    //@Scheduled(cron = "45 * * * * ?")//solo para pruebas para que a los 45 segundos se dispare
    @Transactional
    protected void limpiezaSesion() {
           ISesionDAO iSesionDAO = new SesionDAO();
            try {
                autorizacionService.terminarSesionTiempo(iSesionDAO);
            } catch (Exception ex) {
            }
    }

    @Override
    public ResponseEntity<DiffRespuesta> getAuthDiff() {
        DiffRespuesta diffRespuesta = autenticacionService.getAuthInicial();
        return new ResponseEntity<DiffRespuesta>(diffRespuesta, null, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Object>> getAuth(@NotNull @Valid String idSolicitud, @NotNull @Valid String A, @NotNull @Valid String contentAuth) {

        final String USUARIO = "usuario";
        final String CLAVE = "clave";
        List<String> camposBuscados = new ArrayList<>();
        camposBuscados.add(USUARIO);
        camposBuscados.add(CLAVE);

        Map<String, String> respuesta = autenticacionService.getAuthFinal(camposBuscados, idSolicitud, A, contentAuth);

        IAutorizarDAO iAutorizarDAO = new AutorizarDAO();
        ISesionDAO iSesionDAO = new SesionDAO();
        List<Object> cuentaPeril = autorizacionService.iniciarSesion(respuesta.get(USUARIO),
                Hash.getHashStringHexa(respuesta.get(CLAVE)),
                iAutorizarDAO, iSesionDAO);

        HttpStatus codRespuesta = HttpStatus.NOT_ACCEPTABLE;
        if (cuentaPeril != null && cuentaPeril.size() > 0) {
            codRespuesta = HttpStatus.OK;
        }
        cuentaPeril = cuentaPeril == null ? new ArrayList<>() : cuentaPeril;
        return new ResponseEntity<List<Object>>(cuentaPeril, null, codRespuesta);
    }

    @Override
    public ResponseEntity<Object> salirDelaSesion(String sesion) {
        try {
            ISesionDAO iSesionDAO = new SesionDAO();
            autorizacionService.terminarSesion(sesion, iSesionDAO);
            return new ResponseEntity<>("Proceso correcto", null, HttpStatus.CREATED);
        }catch (Exception ex){
            return new ResponseEntity<>(ex.getMessage(), null, HttpStatus.NOT_ACCEPTABLE);
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
