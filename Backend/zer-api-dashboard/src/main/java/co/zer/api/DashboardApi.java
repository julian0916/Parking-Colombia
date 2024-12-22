/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.23).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package co.zer.api;

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

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-12-17T17:02:06.140Z[GMT]")
public interface DashboardApi {

    Logger log = LoggerFactory.getLogger(DashboardApi.class);

    default Optional<ObjectMapper> getObjectMapper(){
        return Optional.empty();
    }

    default Optional<HttpServletRequest> getRequest(){
        return Optional.empty();
    }

    default Optional<String> getAcceptHeader() {
        return getRequest().map(r -> r.getHeader("Accept"));
    }

    @Operation(summary = "", description = "", tags={ "dashboard", "apiGateway" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Información disponible.", content = @Content(schema = @Schema(implementation = Object.class))),
        
        @ApiResponse(responseCode = "400", description = "No se pudo procesar por error en los datos."),
        
        @ApiResponse(responseCode = "401", description = "No está autorizado.") })
    @RequestMapping(value = "/v1/dashboard/datos/{cuenta}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    default ResponseEntity<Object> getDatosDashboard(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("cuenta") String cuenta, @NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "fnInicio", required = true) Long fnInicio, @NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "fnFin", required = true) Long fnFin) {
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
            log.warn("ObjectMapper or HttpServletRequest not configured in default DashboardApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @Operation(summary = "", description = "", tags={ "dashboard", "apiGateway" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Información disponible.", content = @Content(schema = @Schema(implementation = Object.class))),
        
        @ApiResponse(responseCode = "400", description = "No se pudo procesar por error en los datos."),
        
        @ApiResponse(responseCode = "401", description = "No está autorizado.") })
    @RequestMapping(value = "/v1/dashboard/ocupacion_zona/{cuenta}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    default ResponseEntity<Object> getOcupacionZonaHora(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("cuenta") String cuenta, @NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "fhConsulta", required = true) String fhConsulta) {
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
            log.warn("ObjectMapper or HttpServletRequest not configured in default DashboardApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}

