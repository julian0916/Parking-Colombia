package co.zer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.sql.Time;
import java.util.Objects;

/**
 * Zona
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-01-14T18:45:49.726Z[GMT]")


public class Zona implements Serializable  {
  private static final long serialVersionUID = 1L;

  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("celdasCarro")
  private Long celdasCarro = null;

  @JsonProperty("celdasMoto")
  private Long celdasMoto = null;

  @JsonProperty("valorHoraCarro")
  private Long valorHoraCarro = null;

  @JsonProperty("valorHoraMoto")
  private Long valorHoraMoto = null;

  @JsonProperty("nombre")
  private String nombre = null;

  @JsonProperty("direccion")
  private String direccion = null;

  @JsonProperty("observacion")
  private String observacion = null;

  @JsonProperty("latitud")
  private String latitud = null;

  @JsonProperty("longitud")
  private String longitud = null;

  @JsonProperty("entreSemanaInicia")
  private Time entreSemanaInicia = null;

  @JsonProperty("entreSemanaTermina")
  private Time entreSemanaTermina = null;

  @JsonProperty("finSemanaInicia")
  private Time finSemanaInicia = null;

  @JsonProperty("finSemanaTermina")
  private Time finSemanaTermina = null;

  @JsonProperty("minutosGracia")
  private Long minutosGracia = null;

  @JsonProperty("minutosParaNuevaGracia")
  private Long minutosParaNuevaGracia = null;

  @JsonProperty("activo")
  private Boolean activo = null;

  public Zona id(Long id) {
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

  public Zona celdasCarro(Long celdasCarro) {
    this.celdasCarro = celdasCarro;
    return this;
  }

  /**
   * Get celdasCarro
   * @return celdasCarro
   **/
  @Schema(description = "")
  
    public Long getCeldasCarro() {
    return celdasCarro;
  }

  public void setCeldasCarro(Long celdasCarro) {
    this.celdasCarro = celdasCarro;
  }

  public Zona celdasMoto(Long celdasMoto) {
    this.celdasMoto = celdasMoto;
    return this;
  }

  /**
   * Get celdasMoto
   * @return celdasMoto
   **/
  @Schema(description = "")
  
    public Long getCeldasMoto() {
    return celdasMoto;
  }

  public void setCeldasMoto(Long celdasMoto) {
    this.celdasMoto = celdasMoto;
  }

  public Zona valorHoraCarro(Long valorHoraCarro) {
    this.valorHoraCarro = valorHoraCarro;
    return this;
  }

  /**
   * Get valorHoraCarro
   * @return valorHoraCarro
   **/
  @Schema(description = "")
  
    public Long getValorHoraCarro() {
    return valorHoraCarro;
  }

  public void setValorHoraCarro(Long valorHoraCarro) {
    this.valorHoraCarro = valorHoraCarro;
  }

  public Zona valorHoraMoto(Long valorHoraMoto) {
    this.valorHoraMoto = valorHoraMoto;
    return this;
  }

  /**
   * Get valorHoraMoto
   * @return valorHoraMoto
   **/
  @Schema(description = "")
  
    public Long getValorHoraMoto() {
    return valorHoraMoto;
  }

  public void setValorHoraMoto(Long valorHoraMoto) {
    this.valorHoraMoto = valorHoraMoto;
  }

  public Zona nombre(String nombre) {
    this.nombre = nombre;
    return this;
  }

  /**
   * Get nombre
   * @return nombre
   **/
  @Schema(description = "")
  
    public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public Zona direccion(String direccion) {
    this.direccion = direccion;
    return this;
  }

  /**
   * Get direccion
   * @return direccion
   **/
  @Schema(description = "")
  
    public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public Zona observacion(String observacion) {
    this.observacion = observacion;
    return this;
  }

  /**
   * Get observacion
   * @return observacion
   **/
  @Schema(description = "")
  
    public String getObservacion() {
    return observacion;
  }

  public void setObservacion(String observacion) {
    this.observacion = observacion;
  }

  public Zona latitud(String latitud) {
    this.latitud = latitud;
    return this;
  }

  /**
   * Get latitud
   * @return latitud
   **/
  @Schema(description = "")
  
    public String getLatitud() {
    return latitud;
  }

  public void setLatitud(String latitud) {
    this.latitud = latitud;
  }

  public Zona longitud(String longitud) {
    this.longitud = longitud;
    return this;
  }

  /**
   * Get longitud
   * @return longitud
   **/
  @Schema(description = "")
  
    public String getLongitud() {
    return longitud;
  }

  public void setLongitud(String longitud) {
    this.longitud = longitud;
  }

  public Zona entreSemanaInicia(Time entreSemanaInicia) {
    this.entreSemanaInicia = entreSemanaInicia;
    return this;
  }

  /**
   * Get entreSemanaInicia
   * @return entreSemanaInicia
   **/
  @Schema(description = "")
  
    public Time getEntreSemanaInicia() {
    return entreSemanaInicia;
  }

  public void setEntreSemanaInicia(Time entreSemanaInicia) {
    this.entreSemanaInicia = entreSemanaInicia;
  }

  public Zona entreSemanaTermina(Time entreSemanaTermina) {
    this.entreSemanaTermina = entreSemanaTermina;
    return this;
  }

  /**
   * Get entreSemanaTermina
   * @return entreSemanaTermina
   **/
  @Schema(description = "")
  
    public Time getEntreSemanaTermina() {
    return entreSemanaTermina;
  }

  public void setEntreSemanaTermina(Time entreSemanaTermina) {
    this.entreSemanaTermina = entreSemanaTermina;
  }

  public Zona finSemanaInicia(Time finSemanaInicia) {
    this.finSemanaInicia = finSemanaInicia;
    return this;
  }

  /**
   * Get finSemanaInicia
   * @return finSemanaInicia
   **/
  @Schema(description = "")
  
    public Time getFinSemanaInicia() {
    return finSemanaInicia;
  }

  public void setFinSemanaInicia(Time finSemanaInicia) {
    this.finSemanaInicia = finSemanaInicia;
  }

  public Zona finSemanaTermina(Time finSemanaTermina) {
    this.finSemanaTermina = finSemanaTermina;
    return this;
  }

  /**
   * Get finSemanaTermina
   * @return finSemanaTermina
   **/
  @Schema(description = "")
  
    public Time getFinSemanaTermina() {
    return finSemanaTermina;
  }

  public void setFinSemanaTermina(Time finSemanaTermina) {
    this.finSemanaTermina = finSemanaTermina;
  }

  public Zona minutosGracia(Long minutosGracia) {
    this.minutosGracia = minutosGracia;
    return this;
  }

  /**
   * Get minutosGracia
   * @return minutosGracia
   **/
  @Schema(description = "")
  
    public Long getMinutosGracia() {
    return minutosGracia;
  }

  public void setMinutosGracia(Long minutosGracia) {
    this.minutosGracia = minutosGracia;
  }

  public Zona minutosParaNuevaGracia(Long minutosParaNuevaGracia) {
    this.minutosParaNuevaGracia = minutosParaNuevaGracia;
    return this;
  }

  /**
   * Get minutosParaNuevaGracia
   * @return minutosParaNuevaGracia
   **/
  @Schema(description = "")
  
    public Long getMinutosParaNuevaGracia() {
    return minutosParaNuevaGracia;
  }

  public void setMinutosParaNuevaGracia(Long minutosParaNuevaGracia) {
    this.minutosParaNuevaGracia = minutosParaNuevaGracia;
  }

  public Zona activo(Boolean activo) {
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
    Zona zona = (Zona) o;
    return Objects.equals(this.id, zona.id) &&
        Objects.equals(this.celdasCarro, zona.celdasCarro) &&
        Objects.equals(this.celdasMoto, zona.celdasMoto) &&
        Objects.equals(this.valorHoraCarro, zona.valorHoraCarro) &&
        Objects.equals(this.valorHoraMoto, zona.valorHoraMoto) &&
        Objects.equals(this.nombre, zona.nombre) &&
        Objects.equals(this.direccion, zona.direccion) &&
        Objects.equals(this.observacion, zona.observacion) &&
        Objects.equals(this.latitud, zona.latitud) &&
        Objects.equals(this.longitud, zona.longitud) &&
        Objects.equals(this.entreSemanaInicia, zona.entreSemanaInicia) &&
        Objects.equals(this.entreSemanaTermina, zona.entreSemanaTermina) &&
        Objects.equals(this.finSemanaInicia, zona.finSemanaInicia) &&
        Objects.equals(this.finSemanaTermina, zona.finSemanaTermina) &&
        Objects.equals(this.minutosGracia, zona.minutosGracia) &&
        Objects.equals(this.minutosParaNuevaGracia, zona.minutosParaNuevaGracia) &&
        Objects.equals(this.activo, zona.activo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, celdasCarro, celdasMoto, valorHoraCarro, valorHoraMoto, nombre, direccion, observacion, latitud, longitud, entreSemanaInicia, entreSemanaTermina, finSemanaInicia, finSemanaTermina, minutosGracia, minutosParaNuevaGracia, activo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Zona {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    celdasCarro: ").append(toIndentedString(celdasCarro)).append("\n");
    sb.append("    celdasMoto: ").append(toIndentedString(celdasMoto)).append("\n");
    sb.append("    valorHoraCarro: ").append(toIndentedString(valorHoraCarro)).append("\n");
    sb.append("    valorHoraMoto: ").append(toIndentedString(valorHoraMoto)).append("\n");
    sb.append("    nombre: ").append(toIndentedString(nombre)).append("\n");
    sb.append("    direccion: ").append(toIndentedString(direccion)).append("\n");
    sb.append("    observacion: ").append(toIndentedString(observacion)).append("\n");
    sb.append("    latitud: ").append(toIndentedString(latitud)).append("\n");
    sb.append("    longitud: ").append(toIndentedString(longitud)).append("\n");
    sb.append("    entreSemanaInicia: ").append(toIndentedString(entreSemanaInicia)).append("\n");
    sb.append("    entreSemanaTermina: ").append(toIndentedString(entreSemanaTermina)).append("\n");
    sb.append("    finSemanaInicia: ").append(toIndentedString(finSemanaInicia)).append("\n");
    sb.append("    finSemanaTermina: ").append(toIndentedString(finSemanaTermina)).append("\n");
    sb.append("    minutosGracia: ").append(toIndentedString(minutosGracia)).append("\n");
    sb.append("    minutosParaNuevaGracia: ").append(toIndentedString(minutosParaNuevaGracia)).append("\n");
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
