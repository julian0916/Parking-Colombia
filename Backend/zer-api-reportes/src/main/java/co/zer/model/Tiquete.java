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
 * Tiquete
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-11-23T21:43:17.962Z[GMT]")


public class Tiquete  implements Serializable  {
  private static final long serialVersionUID = 1L;

  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("nit")
  private String nit = null;

  @JsonProperty("nombre")
  private String nombre = null;

  @JsonProperty("lema")
  private String lema = null;

  @JsonProperty("terminos")
  private String terminos = null;

  @JsonProperty("contenido")
  private String contenido = null;

  public Tiquete id(Long id) {
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

  public Tiquete nit(String nit) {
    this.nit = nit;
    return this;
  }

  /**
   * Get nit
   * @return nit
   **/
  @Schema(description = "")
  
    public String getNit() {
    return nit;
  }

  public void setNit(String nit) {
    this.nit = nit;
  }

  public Tiquete nombre(String nombre) {
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

  public Tiquete lema(String lema) {
    this.lema = lema;
    return this;
  }

  /**
   * Get lema
   * @return lema
   **/
  @Schema(description = "")
  
    public String getLema() {
    return lema;
  }

  public void setLema(String lema) {
    this.lema = lema;
  }

  public Tiquete terminos(String terminos) {
    this.terminos = terminos;
    return this;
  }

  /**
   * Get terminos
   * @return terminos
   **/
  @Schema(description = "")
  
    public String getTerminos() {
    return terminos;
  }

  public void setTerminos(String terminos) {
    this.terminos = terminos;
  }

  public Tiquete contenido(String contenido) {
    this.contenido = contenido;
    return this;
  }

  /**
   * Get contenido
   * @return contenido
   **/
  @Schema(description = "")
  
    public String getContenido() {
    return contenido;
  }

  public void setContenido(String contenido) {
    this.contenido = contenido;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Tiquete tiquete = (Tiquete) o;
    return Objects.equals(this.id, tiquete.id) &&
        Objects.equals(this.nit, tiquete.nit) &&
        Objects.equals(this.nombre, tiquete.nombre) &&
        Objects.equals(this.lema, tiquete.lema) &&
        Objects.equals(this.terminos, tiquete.terminos) &&
        Objects.equals(this.contenido, tiquete.contenido);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nit, nombre, lema, terminos, contenido);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Tiquete {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    nit: ").append(toIndentedString(nit)).append("\n");
    sb.append("    nombre: ").append(toIndentedString(nombre)).append("\n");
    sb.append("    lema: ").append(toIndentedString(lema)).append("\n");
    sb.append("    terminos: ").append(toIndentedString(terminos)).append("\n");
    sb.append("    contenido: ").append(toIndentedString(contenido)).append("\n");
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
