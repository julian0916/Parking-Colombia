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
 * Cuenta
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-11-13T19:35:27.100Z[GMT]")


public class Cuenta  implements Serializable  {
  private static final long serialVersionUID = 1L;

  @JsonProperty("id")
  private String id = null;

  @JsonProperty("nombre")
  private String nombre = null;

  @JsonProperty("informacion")
  private String informacion = null;

  @JsonProperty("activo")
  private Boolean activo = null;

  public Cuenta id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   **/
  @Schema(description = "")
  
    public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Cuenta nombre(String nombre) {
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

  public Cuenta informacion(String informacion) {
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

  public Cuenta activo(Boolean activo) {
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
    Cuenta cuenta = (Cuenta) o;
    return Objects.equals(this.id, cuenta.id) &&
        Objects.equals(this.nombre, cuenta.nombre) &&
        Objects.equals(this.informacion, cuenta.informacion) &&
        Objects.equals(this.activo, cuenta.activo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nombre, informacion, activo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Cuenta {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    nombre: ").append(toIndentedString(nombre)).append("\n");
    sb.append("    informacion: ").append(toIndentedString(informacion)).append("\n");
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
