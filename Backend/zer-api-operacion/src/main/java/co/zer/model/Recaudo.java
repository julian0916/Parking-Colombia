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
 * Recaudo
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-12-21T14:43:09.407Z[GMT]")


public class Recaudo  implements Serializable  {
  private static final long serialVersionUID = 1L;

  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("promotor")
  private Long promotor = null;

  @JsonProperty("nfRecaudo")
  private Long nfRecaudo = null;

  @JsonProperty("cantidadRecaudada")
  private Long cantidadRecaudada = null;

  @JsonProperty("cantidadReportada")
  private Long cantidadReportada = null;

  @JsonProperty("saldoCuenta")
  private Long saldoCuenta = null;

  @JsonProperty("cerrada")
  private Boolean cerrada = null;

  @JsonProperty("actualizadoSaldo")
  private Boolean actualizadoSaldo = null;

  public Recaudo id(Long id) {
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

  public Recaudo promotor(Long promotor) {
    this.promotor = promotor;
    return this;
  }

  /**
   * Get promotor
   * @return promotor
   **/
  @Schema(description = "")
  
    public Long getPromotor() {
    return promotor;
  }

  public void setPromotor(Long promotor) {
    this.promotor = promotor;
  }

  public Recaudo nfRecaudo(Long nfRecaudo) {
    this.nfRecaudo = nfRecaudo;
    return this;
  }

  /**
   * Get nfRecaudo
   * @return nfRecaudo
   **/
  @Schema(description = "")
  
    public Long getNfRecaudo() {
    return nfRecaudo;
  }

  public void setNfRecaudo(Long nfRecaudo) {
    this.nfRecaudo = nfRecaudo;
  }

  public Recaudo cantidadRecaudada(Long cantidadRecaudada) {
    this.cantidadRecaudada = cantidadRecaudada;
    return this;
  }

  /**
   * Get cantidadRecaudada
   * @return cantidadRecaudada
   **/
  @Schema(description = "")
  
    public Long getCantidadRecaudada() {
    return cantidadRecaudada;
  }

  public void setCantidadRecaudada(Long cantidadRecaudada) {
    this.cantidadRecaudada = cantidadRecaudada;
  }

  public Recaudo cantidadReportada(Long cantidadReportada) {
    this.cantidadReportada = cantidadReportada;
    return this;
  }

  /**
   * Get cantidadReportada
   * @return cantidadReportada
   **/
  @Schema(description = "")
  
    public Long getCantidadReportada() {
    return cantidadReportada;
  }

  public void setCantidadReportada(Long cantidadReportada) {
    this.cantidadReportada = cantidadReportada;
  }

  public Recaudo saldoCuenta(Long saldoCuenta) {
    this.saldoCuenta = saldoCuenta;
    return this;
  }

  /**
   * Get saldoCuenta
   * @return saldoCuenta
   **/
  @Schema(description = "")
  
    public Long getSaldoCuenta() {
    return saldoCuenta;
  }

  public void setSaldoCuenta(Long saldoCuenta) {
    this.saldoCuenta = saldoCuenta;
  }

  public Recaudo cerrada(Boolean cerrada) {
    this.cerrada = cerrada;
    return this;
  }

  /**
   * Get cerrada
   * @return cerrada
   **/
  @Schema(description = "")
  
    public Boolean isCerrada() {
    return cerrada;
  }

  public void setCerrada(Boolean cerrada) {
    this.cerrada = cerrada;
  }

  public Recaudo actualizadoSaldo(Boolean actualizadoSaldo) {
    this.actualizadoSaldo = actualizadoSaldo;
    return this;
  }

  /**
   * Get actualizadoSaldo
   * @return actualizadoSaldo
   **/
  @Schema(description = "")
  
    public Boolean isActualizadoSaldo() {
    return actualizadoSaldo;
  }

  public void setActualizadoSaldo(Boolean actualizadoSaldo) {
    this.actualizadoSaldo = actualizadoSaldo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Recaudo recaudo = (Recaudo) o;
    return Objects.equals(this.id, recaudo.id) &&
        Objects.equals(this.promotor, recaudo.promotor) &&
        Objects.equals(this.nfRecaudo, recaudo.nfRecaudo) &&
        Objects.equals(this.cantidadRecaudada, recaudo.cantidadRecaudada) &&
        Objects.equals(this.cantidadReportada, recaudo.cantidadReportada) &&
        Objects.equals(this.saldoCuenta, recaudo.saldoCuenta) &&
        Objects.equals(this.cerrada, recaudo.cerrada) &&
        Objects.equals(this.actualizadoSaldo, recaudo.actualizadoSaldo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, promotor, nfRecaudo, cantidadRecaudada, cantidadReportada, saldoCuenta, cerrada, actualizadoSaldo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Recaudo {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    promotor: ").append(toIndentedString(promotor)).append("\n");
    sb.append("    nfRecaudo: ").append(toIndentedString(nfRecaudo)).append("\n");
    sb.append("    cantidadRecaudada: ").append(toIndentedString(cantidadRecaudada)).append("\n");
    sb.append("    cantidadReportada: ").append(toIndentedString(cantidadReportada)).append("\n");
    sb.append("    saldoCuenta: ").append(toIndentedString(saldoCuenta)).append("\n");
    sb.append("    cerrada: ").append(toIndentedString(cerrada)).append("\n");
    sb.append("    actualizadoSaldo: ").append(toIndentedString(actualizadoSaldo)).append("\n");
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
