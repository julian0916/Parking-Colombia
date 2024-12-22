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
 * Usuario
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-11-17T15:56:48.256Z[GMT]")


public class Usuario  implements Serializable  {
  private static final long serialVersionUID = 1L;

  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("perfil")
  private Long perfil = null;

  @JsonProperty("tipoIdentificacion")
  private Long tipoIdentificacion = null;

  @JsonProperty("identificacion")
  private String identificacion = null;

  @JsonProperty("correo")
  private String correo = null;

  @JsonProperty("nombre1")
  private String nombre1 = null;

  @JsonProperty("nombre2")
  private String nombre2 = null;

  @JsonProperty("apellido1")
  private String apellido1 = null;

  @JsonProperty("apellido2")
  private String apellido2 = null;

  @JsonProperty("claveEncriptada")
  private String claveEncriptada = null;

  @JsonProperty("claveHash")
  private String claveHash = null;

  @JsonProperty("celular")
  private String celular = null;

  @JsonProperty("fijo")
  private String fijo = null;

  @JsonProperty("direccion")
  private String direccion = null;

  @JsonProperty("masInformacion")
  private String masInformacion = null;

  @JsonProperty("activo")
  private Boolean activo = null;

  public Usuario id(Long id) {
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

  public Usuario perfil(Long perfil) {
    this.perfil = perfil;
    return this;
  }

  /**
   * Get perfil
   * @return perfil
   **/
  @Schema(description = "")
  
    public Long getPerfil() {
    return perfil;
  }

  public void setPerfil(Long perfil) {
    this.perfil = perfil;
  }

  public Usuario tipoIdentificacion(Long tipoIdentificacion) {
    this.tipoIdentificacion = tipoIdentificacion;
    return this;
  }

  /**
   * Get tipoIdentificacion
   * @return tipoIdentificacion
   **/
  @Schema(description = "")
  
    public Long getTipoIdentificacion() {
    return tipoIdentificacion;
  }

  public void setTipoIdentificacion(Long tipoIdentificacion) {
    this.tipoIdentificacion = tipoIdentificacion;
  }

  public Usuario identificacion(String identificacion) {
    this.identificacion = identificacion;
    return this;
  }

  /**
   * Get identificacion
   * @return identificacion
   **/
  @Schema(description = "")
  
    public String getIdentificacion() {
    return identificacion;
  }

  public void setIdentificacion(String identificacion) {
    this.identificacion = identificacion;
  }

  public Usuario correo(String correo) {
    this.correo = correo;
    return this;
  }

  /**
   * Get correo
   * @return correo
   **/
  @Schema(description = "")
  
    public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public Usuario nombre1(String nombre1) {
    this.nombre1 = nombre1;
    return this;
  }

  /**
   * Get nombre1
   * @return nombre1
   **/
  @Schema(description = "")
  
    public String getNombre1() {
    return nombre1;
  }

  public void setNombre1(String nombre1) {
    this.nombre1 = nombre1;
  }

  public Usuario nombre2(String nombre2) {
    this.nombre2 = nombre2;
    return this;
  }

  /**
   * Get nombre2
   * @return nombre2
   **/
  @Schema(description = "")
  
    public String getNombre2() {
    return nombre2;
  }

  public void setNombre2(String nombre2) {
    this.nombre2 = nombre2;
  }

  public Usuario apellido1(String apellido1) {
    this.apellido1 = apellido1;
    return this;
  }

  /**
   * Get apellido1
   * @return apellido1
   **/
  @Schema(description = "")
  
    public String getApellido1() {
    return apellido1;
  }

  public void setApellido1(String apellido1) {
    this.apellido1 = apellido1;
  }

  public Usuario apellido2(String apellido2) {
    this.apellido2 = apellido2;
    return this;
  }

  /**
   * Get apellido2
   * @return apellido2
   **/
  @Schema(description = "")
  
    public String getApellido2() {
    return apellido2;
  }

  public void setApellido2(String apellido2) {
    this.apellido2 = apellido2;
  }

  public Usuario claveEncriptada(String claveEncriptada) {
    this.claveEncriptada = claveEncriptada;
    return this;
  }

  /**
   * Get claveEncriptada
   * @return claveEncriptada
   **/
  @Schema(description = "")
  
    public String getClaveEncriptada() {
    return claveEncriptada;
  }

  public void setClaveEncriptada(String claveEncriptada) {
    this.claveEncriptada = claveEncriptada;
  }

  public Usuario claveHash(String claveHash) {
    this.claveHash = claveHash;
    return this;
  }

  /**
   * Get claveHash
   * @return claveHash
   **/
  @Schema(description = "")
  
    public String getClaveHash() {
    return claveHash;
  }

  public void setClaveHash(String claveHash) {
    this.claveHash = claveHash;
  }

  public Usuario celular(String celular) {
    this.celular = celular;
    return this;
  }

  /**
   * Get celular
   * @return celular
   **/
  @Schema(description = "")
  
    public String getCelular() {
    return celular;
  }

  public void setCelular(String celular) {
    this.celular = celular;
  }

  public Usuario fijo(String fijo) {
    this.fijo = fijo;
    return this;
  }

  /**
   * Get fijo
   * @return fijo
   **/
  @Schema(description = "")
  
    public String getFijo() {
    return fijo;
  }

  public void setFijo(String fijo) {
    this.fijo = fijo;
  }

  public Usuario direccion(String direccion) {
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

  public Usuario masInformacion(String masInformacion) {
    this.masInformacion = masInformacion;
    return this;
  }

  /**
   * Get masInformacion
   * @return masInformacion
   **/
  @Schema(description = "")
  
    public String getMasInformacion() {
    return masInformacion;
  }

  public void setMasInformacion(String masInformacion) {
    this.masInformacion = masInformacion;
  }

  public Usuario activo(Boolean activo) {
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
    Usuario usuario = (Usuario) o;
    return Objects.equals(this.id, usuario.id) &&
        Objects.equals(this.perfil, usuario.perfil) &&
        Objects.equals(this.tipoIdentificacion, usuario.tipoIdentificacion) &&
        Objects.equals(this.identificacion, usuario.identificacion) &&
        Objects.equals(this.correo, usuario.correo) &&
        Objects.equals(this.nombre1, usuario.nombre1) &&
        Objects.equals(this.nombre2, usuario.nombre2) &&
        Objects.equals(this.apellido1, usuario.apellido1) &&
        Objects.equals(this.apellido2, usuario.apellido2) &&
        Objects.equals(this.claveEncriptada, usuario.claveEncriptada) &&
        Objects.equals(this.claveHash, usuario.claveHash) &&
        Objects.equals(this.celular, usuario.celular) &&
        Objects.equals(this.fijo, usuario.fijo) &&
        Objects.equals(this.direccion, usuario.direccion) &&
        Objects.equals(this.masInformacion, usuario.masInformacion) &&
        Objects.equals(this.activo, usuario.activo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, perfil, tipoIdentificacion, identificacion, correo, nombre1, nombre2, apellido1, apellido2, claveEncriptada, claveHash, celular, fijo, direccion, masInformacion, activo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Usuario {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    perfil: ").append(toIndentedString(perfil)).append("\n");
    sb.append("    tipoIdentificacion: ").append(toIndentedString(tipoIdentificacion)).append("\n");
    sb.append("    identificacion: ").append(toIndentedString(identificacion)).append("\n");
    sb.append("    correo: ").append(toIndentedString(correo)).append("\n");
    sb.append("    nombre1: ").append(toIndentedString(nombre1)).append("\n");
    sb.append("    nombre2: ").append(toIndentedString(nombre2)).append("\n");
    sb.append("    apellido1: ").append(toIndentedString(apellido1)).append("\n");
    sb.append("    apellido2: ").append(toIndentedString(apellido2)).append("\n");
    sb.append("    claveEncriptada: ").append(toIndentedString(claveEncriptada)).append("\n");
    sb.append("    claveHash: ").append(toIndentedString(claveHash)).append("\n");
    sb.append("    celular: ").append(toIndentedString(celular)).append("\n");
    sb.append("    fijo: ").append(toIndentedString(fijo)).append("\n");
    sb.append("    direccion: ").append(toIndentedString(direccion)).append("\n");
    sb.append("    masInformacion: ").append(toIndentedString(masInformacion)).append("\n");
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
