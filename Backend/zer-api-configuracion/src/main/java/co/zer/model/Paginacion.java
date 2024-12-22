package co.zer.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Paginacion
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-11-11T19:10:38.485Z[GMT]")


public class Paginacion  implements Serializable  {
  private static final long serialVersionUID = 1L;

  @JsonProperty("columnas")
  private String columnas = null;

  @JsonProperty("total")
  private Long total = null;

  @JsonProperty("limite")
  private Long limite = null;

  @JsonProperty("actual")
  private Long actual = null;

  @JsonProperty("filtro")
  private String filtro = null;

  @JsonProperty("orden")
  private String orden = null;

  @JsonProperty("sentido")
  private String sentido = null;

  public Paginacion columnas(String columnas) {
    this.columnas = columnas;
    return this;
  }

  /**
   * Get columnas
   * @return columnas
   **/
  @Schema(description = "")
  
    public String getColumnas() {
    return columnas;
  }

  public void setColumnas(String columnas) {
    this.columnas = columnas;
  }

  public Paginacion total(Long total) {
    this.total = total;
    return this;
  }

  /**
   * Get total
   * @return total
   **/
  @Schema(description = "")
  
    public Long getTotal() {
    return total;
  }

  public void setTotal(Long total) {
    this.total = total;
  }

  public Paginacion limite(Long limite) {
    this.limite = limite;
    return this;
  }

  /**
   * Get limite
   * @return limite
   **/
  @Schema(description = "")
  
    public Long getLimite() {
    return limite;
  }

  public void setLimite(Long limite) {
    this.limite = limite;
  }

  public Paginacion actual(Long actual) {
    this.actual = actual;
    return this;
  }

  /**
   * Get actual
   * @return actual
   **/
  @Schema(description = "")
  
    public Long getActual() {
    return actual;
  }

  public void setActual(Long actual) {
    this.actual = actual;
  }

  public Paginacion filtro(String filtro) {
    this.filtro = filtro;
    return this;
  }

  /**
   * Get filtro
   * @return filtro
   **/
  @Schema(description = "")
  
    public String getFiltro() {
    return filtro;
  }

  public void setFiltro(String filtro) {
    this.filtro = filtro;
  }

  public Paginacion orden(String orden) {
    this.orden = orden;
    return this;
  }

  /**
   * Get orden
   * @return orden
   **/
  @Schema(description = "")
  
    public String getOrden() {
    return orden;
  }

  public void setOrden(String orden) {
    this.orden = orden;
  }

  public Paginacion sentido(String sentido) {
    this.sentido = sentido;
    return this;
  }

  /**
   * Get sentido
   * @return sentido
   **/
  @Schema(description = "")
  
    public String getSentido() {
    return sentido;
  }

  public void setSentido(String sentido) {
    this.sentido = sentido;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Paginacion paginacion = (Paginacion) o;
    return Objects.equals(this.columnas, paginacion.columnas) &&
        Objects.equals(this.total, paginacion.total) &&
        Objects.equals(this.limite, paginacion.limite) &&
        Objects.equals(this.actual, paginacion.actual) &&
        Objects.equals(this.filtro, paginacion.filtro) &&
        Objects.equals(this.orden, paginacion.orden) &&
        Objects.equals(this.sentido, paginacion.sentido);
  }

  @Override
  public int hashCode() {
    return Objects.hash(columnas, total, limite, actual, filtro, orden, sentido);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Paginacion {\n");
    
    sb.append("    columnas: ").append(toIndentedString(columnas)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    limite: ").append(toIndentedString(limite)).append("\n");
    sb.append("    actual: ").append(toIndentedString(actual)).append("\n");
    sb.append("    filtro: ").append(toIndentedString(filtro)).append("\n");
    sb.append("    orden: ").append(toIndentedString(orden)).append("\n");
    sb.append("    sentido: ").append(toIndentedString(sentido)).append("\n");
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
