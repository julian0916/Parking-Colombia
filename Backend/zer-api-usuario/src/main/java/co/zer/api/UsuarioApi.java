/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.23).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package co.zer.api;

import co.zer.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CookieValue;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-11-17T15:56:48.256Z[GMT]")
public interface UsuarioApi {

    Logger log = LoggerFactory.getLogger(UsuarioApi.class);

    default Optional<ObjectMapper> getObjectMapper(){
        return Optional.empty();
    }

    default Optional<HttpServletRequest> getRequest(){
        return Optional.empty();
    }

    default Optional<String> getAcceptHeader() {
        return getRequest().map(r -> r.getHeader("Accept"));
    }

    @Operation(summary = "", description = "", tags={ "usuario", "apiGateway" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Listado de tipos de documento.", content = @Content(schema = @Schema(implementation = Object.class))),
        
        @ApiResponse(responseCode = "401", description = "No está autorizado.") })
    @RequestMapping(value = "/v1/usuario/tipo-documento/{cuenta}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    default ResponseEntity<Object> getListadoDocumentos(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("cuenta") String cuenta) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    return new ResponseEntity<>(getObjectMapper().get().readValue("{ }", Object.class), HttpStatus.NOT_IMPLEMENTED);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default UsuarioApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @Operation(summary = "", description = "", tags={ "usuario", "apiGateway" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Listado de los perfiles", content = @Content(schema = @Schema(implementation = Object.class))),
        
        @ApiResponse(responseCode = "401", description = "No está autorizado.") })
    @RequestMapping(value = "/v1/usuario/perfil/{cuenta}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    default ResponseEntity<Object> getListadoPerfiles(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("cuenta") String cuenta) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    return new ResponseEntity<>(getObjectMapper().get().readValue("{ }", Object.class), HttpStatus.NOT_IMPLEMENTED);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default UsuarioApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @Operation(summary = "", description = "", tags={ "usuario", "apiGateway" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Listado de usuarios.", content = @Content(schema = @Schema(implementation = Object.class))),
        
        @ApiResponse(responseCode = "401", description = "No está autorizado.") })
    @RequestMapping(value = "/v1/usuario/listado/{cuenta}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    default ResponseEntity<Object> getListadoUsuarios(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("cuenta") String cuenta, @NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "limite", required = true) Long limite, @NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "actual", required = true) Long actual, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "filtro", required = false) String filtro, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "orden", required = false) String orden, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "sentido", required = false) String sentido) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    return new ResponseEntity<>(getObjectMapper().get().readValue("{ }", Object.class), HttpStatus.NOT_IMPLEMENTED);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default UsuarioApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @Operation(summary = "", description = "", tags={ "usuario", "apiGateway" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "Información correctamente almacenada y disponible.", content = @Content(schema = @Schema(implementation = Object.class))),
        
        @ApiResponse(responseCode = "400", description = "No se pudo almacenar por error en los datos."),
        
        @ApiResponse(responseCode = "401", description = "No está autorizado.") })
    @RequestMapping(value = "/v1/usuario/usuario/{cuenta}",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    default ResponseEntity<Object> guardarUsuario(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("cuenta") String cuenta, @Parameter(in = ParameterIn.DEFAULT, description = "Usuario", required=true, schema=@Schema()) @Valid @RequestBody Usuario body) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    return new ResponseEntity<>(getObjectMapper().get().readValue("{ }", Object.class), HttpStatus.NOT_IMPLEMENTED);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default UsuarioApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}

