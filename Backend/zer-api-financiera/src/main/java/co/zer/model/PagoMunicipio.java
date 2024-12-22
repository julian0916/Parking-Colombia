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
 * PagoMunicipio
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-01-08T19:10:40.112Z[GMT]")


public class PagoMunicipio  implements Serializable  {
  private static final long serialVersionUID = 1L;

  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("nfPago")
  private Long nfPago = null;

  @JsonProperty("informacion")
  private String informacion = null;

  @JsonProperty("valorConsignado")
  private Long valorConsignado = null;

  @JsonProperty("codigoFactura")
  private String codigoFactura = null;

  @JsonProperty("cerrado")
  private Boolean cerrado = null;

  public PagoMunicipio id(Long id) {
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

  public PagoMunicipio nfPago(Long nfPago) {
    this.nfPago = nfPago;
    return this;
  }

  /**
   * Get nfPago
   * @return nfPago
   **/
  @Schema(description = "")
  
    public Long getNfPago() {
    return nfPago;
  }

  public void setNfPago(Long nfPago) {
    this.nfPago = nfPago;
  }

  public PagoMunicipio informacion(String informacion) {
    this.informacion = informacion;
    return this;
  }

  /**
   * Get informacion
   * @return informacion
   **/
  @Schema(description = "")
  
    public String getInformacion() {
    return informacion;
  }

  public void setInformacion(String informacion) {
    this.informacion = informacion;
  }

  public PagoMunicipio valorConsignado(Long valorConsignado) {
    this.valorConsignado = valorConsignado;
    return this;
  }

  /**
   * Get valorConsignado
   * @return valorConsignado
   **/
  @Schema(description = "")
  
    public Long getValorConsignado() {
    return valorConsignado;
  }

  public void setValorConsignado(Long valorConsignado) {
    this.valorConsignado = valorConsignado;
  }

  public PagoMunicipio codigoFactura(String codigoFactura) {
    this.codigoFactura = codigoFactura;
    return this;
  }

  /**
   * Get codigoFactura
   * @return codigoFactura
   **/
  @Schema(description = "")
  
    public String getCodigoFactura() {
    return codigoFactura;
  }

  public void setCodigoFactura(String codigoFactura) {
    this.codigoFactura = codigoFactura;
  }

  public PagoMunicipio cerrado(Boolean cerrado) {
    this.cerrado = cerrado;
    return this;
  }

  /**
   * Get cerrado
   * @return cerrado
   **/
  @Schema(description = "")
  
    public Boolean isCerrado() {
    return cerrado;
  }

  public void setCerrado(Boolean cerrado) {
    this.cerrado = cerrado;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PagoMunicipio pagoMunicipio = (PagoMunicipio) o;
    return Objects.equals(this.id, pagoMunicipio.id) &&
        Objects.equals(this.nfPago, pagoMunicipio.nfPago) &&
        Objects.equals(this.informacion, pagoMunicipio.informacion) &&
        Objects.equals(this.valorConsignado, pagoMunicipio.valorConsignado) &&
        Objects.equals(this.codigoFactura, pagoMunicipio.codigoFactura) &&
        Objects.equals(this.cerrado, pagoMunicipio.cerrado);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nfPago, informacion, valorConsignado, codigoFactura, cerrado);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PagoMunicipio {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    nfPago: ").append(toIndentedString(nfPago)).append("\n");
    sb.append("    informacion: ").append(toIndentedString(informacion)).append("\n");
    sb.append("    valorConsignado: ").append(toIndentedString(valorConsignado)).append("\n");
    sb.append("    codigoFactura: ").append(toIndentedString(codigoFactura)).append("\n");
    sb.append("    cerrado: ").append(toIndentedString(cerrado)).append("\n");
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
