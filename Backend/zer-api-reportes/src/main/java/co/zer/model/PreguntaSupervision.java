package co.zer.model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * PreguntaSupervision
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-01-14T15:45:43.235Z[GMT]")


public class PreguntaSupervision  implements Serializable  {
  private static final long serialVersionUID = 1L;

  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("pregunta")
  private String pregunta = null;

  @JsonProperty("valor")
  private String valor = null;

  @JsonProperty("cumple")
  private Boolean cumple = null;

  public PreguntaSupervision id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   **/
  @Schema(description = "")
  
    public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PreguntaSupervision pregunta(String pregunta) {
    this.pregunta = pregunta;
    return this;
  }

  /**
   * Get pregunta
   * @return pregunta
   **/
  @Schema(description = "")
  
    public String getPregunta() {
    return pregunta;
  }

  public void setPregunta(String pregunta) {
    this.pregunta = pregunta;
  }

  public PreguntaSupervision valor(String valor) {
    this.valor = valor;
    return this;
  }

  /**
   * Get valor
   * @return valor
   **/
  @Schema(description = "")
  
    public String getValor() {
    return valor;
  }

  public void setValor(String valor) {
    this.valor = valor;
  }

  public PreguntaSupervision cumple(Boolean cumple) {
    this.cumple = cumple;
    return this;
  }

  /**
   * Get cumple
   * @return cumple
   **/
  @Schema(description = "")
  
    public Boolean isCumple() {
    return cumple;
  }

  public void setCumple(Boolean cumple) {
    this.cumple = cumple;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PreguntaSupervision preguntaSupervision = (PreguntaSupervision) o;
    return Objects.equals(this.id, preguntaSupervision.id) &&
        Objects.equals(this.pregunta, preguntaSupervision.pregunta) &&
        Objects.equals(this.valor, preguntaSupervision.valor) &&
        Objects.equals(this.cumple, preguntaSupervision.cumple);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, pregunta, valor, cumple);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PreguntaSupervision {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    pregunta: ").append(toIndentedString(pregunta)).append("\n");
    sb.append("    valor: ").append(toIndentedString(valor)).append("\n");
    sb.append("    cumple: ").append(toIndentedString(cumple)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
