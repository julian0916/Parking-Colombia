package co.zer.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;

/**
 * PromotorZona
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-11-20T19:19:57.576Z[GMT]")


public class PromotorZona implements Serializable  {
  private static final long serialVersionUID = 1L;

  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("promotor")
  private Long promotor = null;

  @JsonProperty("zona")
  private Long zona = null;

  @JsonProperty("lunes")
  private Boolean lunes = null;

  @JsonProperty("martes")
  private Boolean martes = null;

  @JsonProperty("miercoles")
  private Boolean miercoles = null;

  @JsonProperty("jueves")
  private Boolean jueves = null;

  @JsonProperty("viernes")
  private Boolean viernes = null;

  @JsonProperty("sabado")
  private Boolean sabado = null;

  @JsonProperty("domingo")
  private Boolean domingo = null;

  @JsonProperty("detalle")
  private String detalle = null;

  @JsonProperty("activo")
  private Boolean activo = null;

  public PromotorZona id(Long id) {
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

  public PromotorZona promotor(Long promotor) {
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

  public PromotorZona zona(Long zona) {
    this.zona = zona;
    return this;
  }

  /**
   * Get zona
   * @return zona
   **/
  @Schema(description = "")
  
    public Long getZona() {
    return zona;
  }

  public void setZona(Long zona) {
    this.zona = zona;
  }

  public PromotorZona lunes(Boolean lunes) {
    this.lunes = lunes;
    return this;
  }

  /**
   * Get lunes
   * @return lunes
   **/
  @Schema(description = "")
  
    public Boolean isLunes() {
    return lunes;
  }

  public void setLunes(Boolean lunes) {
    this.lunes = lunes;
  }

  public PromotorZona martes(Boolean martes) {
    this.martes = martes;
    return this;
  }

  /**
   * Get martes
   * @return martes
   **/
  @Schema(description = "")
  
    public Boolean isMartes() {
    return martes;
  }

  public void setMartes(Boolean martes) {
    this.martes = martes;
  }

  public PromotorZona miercoles(Boolean miercoles) {
    this.miercoles = miercoles;
    return this;
  }

  /**
   * Get miercoles
   * @return miercoles
   **/
  @Schema(description = "")
  
    public Boolean isMiercoles() {
    return miercoles;
  }

  public void setMiercoles(Boolean miercoles) {
    this.miercoles = miercoles;
  }

  public PromotorZona jueves(Boolean jueves) {
    this.jueves = jueves;
    return this;
  }

  /**
   * Get jueves
   * @return jueves
   **/
  @Schema(description = "")
  
    public Boolean isJueves() {
    return jueves;
  }

  public void setJueves(Boolean jueves) {
    this.jueves = jueves;
  }

  public PromotorZona viernes(Boolean viernes) {
    this.viernes = viernes;
    return this;
  }

  /**
   * Get viernes
   * @return viernes
   **/
  @Schema(description = "")
  
    public Boolean isViernes() {
    return viernes;
  }

  public void setViernes(Boolean viernes) {
    this.viernes = viernes;
  }

  public PromotorZona sabado(Boolean sabado) {
    this.sabado = sabado;
    return this;
  }

  /**
   * Get sabado
   * @return sabado
   **/
  @Schema(description = "")
  
    public Boolean isSabado() {
    return sabado;
  }

  public void setSabado(Boolean sabado) {
    this.sabado = sabado;
  }

  public PromotorZona domingo(Boolean domingo) {
    this.domingo = domingo;
    return this;
  }

  /**
   * Get domingo
   * @return domingo
   **/
  @Schema(description = "")
  
    public Boolean isDomingo() {
    return domingo;
  }

  public void setDomingo(Boolean domingo) {
    this.domingo = domingo;
  }

  public PromotorZona detalle(String detalle) {
    this.detalle = detalle;
    return this;
  }

  /**
   * Get detalle
   * @return detalle
   **/
  @Schema(description = "")
  
    public String getDetalle() {
    return detalle;
  }

  public void setDetalle(String detalle) {
    this.detalle = detalle;
  }

  public PromotorZona activo(Boolean activo) {
    this.activo = activo;
    return this;
  }

  /**
   * Get activo
   * @return activo
   **/
  @Schema(description = "")
  
    public Boolean isActivo() {
    return activo;
  }

  public void setActivo(Boolean activo) {
    this.activo = activo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PromotorZona promotorZona = (PromotorZona) o;
    return Objects.equals(this.id, promotorZona.id) &&
        Objects.equals(this.promotor, promotorZona.promotor) &&
        Objects.equals(this.zona, promotorZona.zona) &&
        Objects.equals(this.lunes, promotorZona.lunes) &&
        Objects.equals(this.martes, promotorZona.martes) &&
        Objects.equals(this.miercoles, promotorZona.miercoles) &&
        Objects.equals(this.jueves, promotorZona.jueves) &&
        Objects.equals(this.viernes, promotorZona.viernes) &&
        Objects.equals(this.sabado, promotorZona.sabado) &&
        Objects.equals(this.domingo, promotorZona.domingo) &&
        Objects.equals(this.detalle, promotorZona.detalle) &&
        Objects.equals(this.activo, promotorZona.activo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, promotor, zona, lunes, martes, miercoles, jueves, viernes, sabado, domingo, detalle, activo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PromotorZona {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    promotor: ").append(toIndentedString(promotor)).append("\n");
    sb.append("    zona: ").append(toIndentedString(zona)).append("\n");
    sb.append("    lunes: ").append(toIndentedString(lunes)).append("\n");
    sb.append("    martes: ").append(toIndentedString(martes)).append("\n");
    sb.append("    miercoles: ").append(toIndentedString(miercoles)).append("\n");
    sb.append("    jueves: ").append(toIndentedString(jueves)).append("\n");
    sb.append("    viernes: ").append(toIndentedString(viernes)).append("\n");
    sb.append("    sabado: ").append(toIndentedString(sabado)).append("\n");
    sb.append("    domingo: ").append(toIndentedString(domingo)).append("\n");
    sb.append("    detalle: ").append(toIndentedString(detalle)).append("\n");
    sb.append("    activo: ").append(toIndentedString(activo)).append("\n");
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
