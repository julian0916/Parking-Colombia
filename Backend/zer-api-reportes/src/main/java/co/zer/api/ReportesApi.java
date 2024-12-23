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

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-04T00:55:18.818Z[GMT]")
@Validated
public interface ReportesApi {

    Logger log = LoggerFactory.getLogger(ReportesApi.class);

    default Optional<ObjectMapper> getObjectMapper(){
        return Optional.empty();
    }

    default Optional<HttpServletRequest> getRequest(){
        return Optional.empty();
    }

    default Optional<String> getAcceptHeader() {
        return getRequest().map(r -> r.getHeader("Accept"));
    }

    @Operation(summary = "", description = "", tags={ "reportes", "apiGateway" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenido del reporte disponible", content = @Content(schema = @Schema(implementation = Object.class))),

            @ApiResponse(responseCode = "401", description = "No está autorizado.") })
    @RequestMapping(value = "/v1/reportes/reporte_cartera/{cuenta}/{nfInicial}/{nfFinal}",
            produces = { "application/json" },
            method = RequestMethod.GET)
    default ResponseEntity<Object> getReporteCartera(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("cuenta") String cuenta, @Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("nfInicial") Long nfInicial, @Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("nfFinal") Long nfFinal, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "limite", required = false) Long limite, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "actual", required = false) Long actual, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "orden", required = false) String orden, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "sentido", required = false) String sentido) {
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
            log.warn("ObjectMapper or HttpServletRequest not configured in default ReportesApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @Operation(summary = "Obtener reporte histórico", description = "Obtiene el reporte histórico de registros a partir de una fecha específica.", tags = {"reportes", "apiGateway"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenido del reporte disponible", content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "401", description = "No está autorizado.")
    })
    @RequestMapping(value = "/v1/reportes/reporte_historico/{cuenta}/{fechaConsulta}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    default ResponseEntity<Object> getReporteHistorico(@Parameter(in = ParameterIn.PATH, description = "Nombre de la cuenta", required = true, schema = @Schema()) @PathVariable("cuenta") String cuenta,
                                                       @Parameter(in = ParameterIn.PATH, description = "Fecha de consulta en formato milisegundos desde el epoch", required = true, schema = @Schema()) @PathVariable("fechaConsulta") Long fechaConsulta) {
        if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    return new ResponseEntity<>(getObjectMapper().get().readValue("{ }", Object.class), HttpStatus.NOT_IMPLEMENTED);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default ReportesApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @Operation(summary = "", description = "", tags={ "reportes", "apiGateway" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenido de l reporte disponible", content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "401", description = "No está autorizado.")
    })
    @RequestMapping(value = "/v1/reportes/reporte_historico/{cuenta}/{mes}/{year}", produces = { "application/json" }, method = RequestMethod.PUT)
    default ResponseEntity<Object> getReporteHistoricoMensual(
            @Parameter(in = ParameterIn.PATH, description = "Nombre de la cuenta", required=true, schema=@Schema()) @PathVariable("cuenta") String cuenta,
            @Parameter(in = ParameterIn.PATH, description = "Mes a seleccionar en formato integer", required=true,schema=@Schema()) @PathVariable("mes") Integer mes,
            @Parameter(in = ParameterIn.PATH, description = "Año a seleccionar en formato Integer", required = true, schema=@Schema()) @PathVariable("year") Integer year) {

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
            log.warn("ObjectMapper or HttpServletRequest not configured in default ReportesApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }




    @Operation(summary = "", description = "", tags={ "reportes", "apiGateway" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenido de l reporte disponible", content = @Content(schema = @Schema(implementation = Object.class))),

            @ApiResponse(responseCode = "401", description = "No está autorizado.") })
    @RequestMapping(value = "/v1/reportes/reporte_mensual/{cuenta}",
            produces = { "application/json" },
            method = RequestMethod.GET)
    default ResponseEntity<Object> getReporteMensual(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("cuenta") String cuenta, @NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "enero", required = true) Boolean enero, @NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "febrero", required = true) Boolean febrero, @NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "marzo", required = true) Boolean marzo, @NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "abril", required = true) Boolean abril, @NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "mayo", required = true) Boolean mayo, @NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "junio", required = true) Boolean junio, @NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "julio", required = true) Boolean julio, @NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "agosto", required = true) Boolean agosto, @NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "septiembre", required = true) Boolean septiembre, @NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "octubre", required = true) Boolean octubre, @NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "noviembre", required = true) Boolean noviembre, @NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "diciembre", required = true) Boolean diciembre, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "year", required = false) Long year) {
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
            log.warn("ObjectMapper or HttpServletRequest not configured in default ReportesApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @Operation(summary = "", description = "", tags={ "reportes", "apiGateway" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenido del reporte disponible", content = @Content(schema = @Schema(implementation = Object.class))),

            @ApiResponse(responseCode = "401", description = "No está autorizado.") })
    @RequestMapping(value = "/v1/reportes/reporte_saldo_promotor/{cuenta}/{activos}",
            produces = { "application/json" },
            method = RequestMethod.GET)
    default ResponseEntity<Object> getReporteSaldosPromotores(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("cuenta") String cuenta, @Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("activos") Boolean activos, @NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "promotor", required = true) Long promotor, @NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "traerTodo", required = true) Boolean traerTodo, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "limite", required = false) Long limite, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "actual", required = false) Long actual, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "orden", required = false) String orden, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "sentido", required = false) String sentido) {
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
            log.warn("ObjectMapper or HttpServletRequest not configured in default ReportesApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @Operation(summary = "", description = "", tags={ "reportes", "apiGateway" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenido del reporte disponible", content = @Content(schema = @Schema(implementation = Object.class))),

            @ApiResponse(responseCode = "401", description = "No está autorizado.") })
    @RequestMapping(value = "/v1/reportes/reporte_supervision/{cuenta}",
            produces = { "application/json" },
            method = RequestMethod.GET)
    default ResponseEntity<Object> getReporteSupervision(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("cuenta") String cuenta, @NotNull @Parameter(in = ParameterIn.QUERY, description = "", required=true, schema=@Schema()) @Valid @RequestParam(value = "fechaInicio", required = true) Long fechaInicio, @NotNull @Parameter(in = ParameterIn.QUERY, description = "", required=true, schema=@Schema()) @Valid @RequestParam(value = "fechaFin", required = true) Long fechaFin, @NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "promotor", required = true) Long promotor, @NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "traerTodo", required = true) Boolean traerTodo, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "limite", required = false) Long limite, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "actual", required = false) Long actual, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "orden", required = false) String orden, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "sentido", required = false) String sentido) {
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
            log.warn("ObjectMapper or HttpServletRequest not configured in default ReportesApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @Operation(summary = "", description = "", tags={ "reportes", "apiGateway" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenido del reporte disponible", content = @Content(schema = @Schema(implementation = Object.class))),

            @ApiResponse(responseCode = "401", description = "No está autorizado.") })
    @RequestMapping(value = "/v1/reportes/reporte_vehiculo/{cuenta}/{nfInicial}/{nfFinal}/{placa}",
            produces = { "application/json" },
            method = RequestMethod.GET)
    default ResponseEntity<Object> getReporteVehiculo(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("cuenta") String cuenta, @Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("nfInicial") Long nfInicial, @Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("nfFinal") Long nfFinal, @Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("placa") String placa, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "limite", required = false) Long limite, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "actual", required = false) Long actual, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "orden", required = false) String orden, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "sentido", required = false) String sentido) {
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
            log.warn("ObjectMapper or HttpServletRequest not configured in default ReportesApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
