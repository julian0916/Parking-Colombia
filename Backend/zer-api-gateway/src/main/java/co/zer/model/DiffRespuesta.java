package co.zer.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * DiffRespuesta
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-10-31T15:37:34.065Z[GMT]")


public class DiffRespuesta  implements Serializable  {
  private static final long serialVersionUID = 1L;

  @JsonProperty("g")
  private String g = null;

  @JsonProperty("p")
  private String p = null;

  @JsonProperty("B")
  private String B = null;

  @JsonProperty("idSolicitud")
  private String idSolicitud = null;

  public DiffRespuesta g(String g) {
    this.g = g;
    return this;
  }

  /**
   * Get g
   * @return g
  **/
  @ApiModelProperty(value = "")
  
    public String getG() {
    return g;
  }

  public void setG(String g) {
    this.g = g;
  }

  public DiffRespuesta p(String p) {
    this.p = p;
    return this;
  }

  /**
   * Get p
   * @return p
  **/
  @ApiModelProperty(value = "")
  
    public String getP() {
    return p;
  }

  public void setP(String p) {
    this.p = p;
  }

  public DiffRespuesta B(String B) {
    this.B = B;
    return this;
  }

  /**
   * Get B
   * @return B
  **/
  @ApiModelProperty(value = "")
  
    public String getB() {
    return B;
  }

  public void setB(String B) {
    this.B = B;
  }

  public DiffRespuesta idSolicitud(String idSolicitud) {
    this.idSolicitud = idSolicitud;
    return this;
  }

  /**
   * Get idSolicitud
   * @return idSolicitud
  **/
  @ApiModelProperty(value = "")
  
    public String getIdSolicitud() {
    return idSolicitud;
  }

  public void setIdSolicitud(String idSolicitud) {
    this.idSolicitud = idSolicitud;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DiffRespuesta diffRespuesta = (DiffRespuesta) o;
    return Objects.equals(this.g, diffRespuesta.g) &&
        Objects.equals(this.p, diffRespuesta.p) &&
        Objects.equals(this.B, diffRespuesta.B) &&
        Objects.equals(this.idSolicitud, diffRespuesta.idSolicitud);
  }

  @Override
  public int hashCode() {
    return Objects.hash(g, p, B, idSolicitud);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DiffRespuesta {\n");
    
    sb.append("    g: ").append(toIndentedString(g)).append("\n");
    sb.append("    p: ").append(toIndentedString(p)).append("\n");
    sb.append("    B: ").append(toIndentedString(B)).append("\n");
    sb.append("    idSolicitud: ").append(toIndentedString(idSolicitud)).append("\n");
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
