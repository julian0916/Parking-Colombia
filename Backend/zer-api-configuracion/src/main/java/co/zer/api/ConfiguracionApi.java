/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.23).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package co.zer.api;

import co.zer.model.Tiquete;
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

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-01-22T21:02:22.607Z[GMT]")
public interface ConfiguracionApi {

    Logger log = LoggerFactory.getLogger(ConfiguracionApi.class);

    default Optional<ObjectMapper> getObjectMapper(){
        return Optional.empty();
    }

    default Optional<HttpServletRequest> getRequest(){
        return Optional.empty();
    }

    default Optional<String> getAcceptHeader() {
        return getRequest().map(r -> r.getHeader("Accept"));
    }

    @Operation(summary = "", description = "", tags={ "configuracion", "apiGateway" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Contenido actual de la variable solicitada.", content = @Content(schema = @Schema(implementation = Object.class))),
        
        @ApiResponse(responseCode = "401", description = "No está autorizado.") })
    @RequestMapping(value = "/v1/configuracion/get_limite_endeudamiento_promotor/{cuenta}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    default ResponseEntity<Object> getLimiteEndeudamientoPromotor(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("cuenta") String cuenta) {
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
            log.warn("ObjectMapper or HttpServletRequest not configured in default ConfiguracionApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @Operation(summary = "", description = "", tags={ "configuracion", "apiGateway" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Contenido actual del tiquete.", content = @Content(schema = @Schema(implementation = Object.class))),
        
        @ApiResponse(responseCode = "401", description = "No está autorizado.") })
    @RequestMapping(value = "/v1/configuracion/get_tiquete/{cuenta}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    default ResponseEntity<Object> getTiquete(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("cuenta") String cuenta) {
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
            log.warn("ObjectMapper or HttpServletRequest not configured in default ConfiguracionApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @Operation(summary = "", description = "", tags={ "configuracion", "apiGateway" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "Información correctamente almacenada y disponible.", content = @Content(schema = @Schema(implementation = Object.class))),
        
        @ApiResponse(responseCode = "400", description = "No se pudo almacenar por error en los datos."),
        
        @ApiResponse(responseCode = "401", description = "No está autorizado.") })
    @RequestMapping(value = "/v1/configuracion/limite_endeudamiento_promotor/{cuenta}/{valor}",
        produces = { "application/json" }, 
        method = RequestMethod.PUT)
    default ResponseEntity<Object> guardarLimiteEndeudamientoPromotor(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("cuenta") String cuenta, @Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("valor") Long valor) {
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
            log.warn("ObjectMapper or HttpServletRequest not configured in default ConfiguracionApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @Operation(summary = "", description = "", tags={ "configuracion", "apiGateway" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "Información correctamente almacenada y disponible.", content = @Content(schema = @Schema(implementation = Object.class))),
        
        @ApiResponse(responseCode = "400", description = "No se pudo almacenar por error en los datos."),
        
        @ApiResponse(responseCode = "401", description = "No está autorizado.") })
    @RequestMapping(value = "/v1/configuracion/tiquete/{cuenta}",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    default ResponseEntity<Object> guardarTiquete(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("cuenta") String cuenta, @Parameter(in = ParameterIn.DEFAULT, description = "Tiquete", required=true, schema=@Schema()) @Valid @RequestBody Tiquete body) {
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
            log.warn("ObjectMapper or HttpServletRequest not configured in default ConfiguracionApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}

