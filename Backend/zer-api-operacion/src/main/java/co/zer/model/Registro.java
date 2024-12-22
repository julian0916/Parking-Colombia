package co.zer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.Objects;

/**
 * Registro
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-11-26T17:13:53.730Z[GMT]")


public class Registro implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("placa")
    private String placa = null;

    @JsonProperty("zona")
    private Long zona = null;

    @JsonProperty("promotor")
    private Long promotor = null;

    @JsonProperty("horas")
    private Long horas = null;

    @JsonProperty("esPrepago")
    private Boolean esPrepago = null;

    public Registro placa(String placa) {
        this.placa = placa;
        return this;
    }

    /**
     * Get placa
     *
     * @return placa
     **/
    @Schema(description = "")

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Registro zona(Long zona) {
        this.zona = zona;
        return this;
    }

    /**
     * Get zona
     *
     * @return zona
     **/
    @Schema(description = "")

    public Long getZona() {
        return zona;
    }

    public void setZona(Long zona) {
        this.zona = zona;
    }

    public Registro promotor(Long promotor) {
        this.promotor = promotor;
        return this;
    }

    /**
     * Get promotor
     *
     * @return promotor
     **/
    @Schema(description = "")

    public Long getPromotor() {
        return promotor;
    }

    public void setPromotor(Long promotor) {
        this.promotor = promotor;
    }

    public Registro horas(Long horas) {
        this.horas = horas;
        return this;
    }

    /**
     * Get horas
     *
     * @return horas
     **/
    @Schema(description = "")

    public Long getHoras() {
        return horas;
    }

    public void setHoras(Long horas) {
        this.horas = horas;
    }

    public Registro esPrepago(Boolean esPrepago) {
        this.esPrepago = esPrepago;
        return this;
    }

    /**
     * Get esPrepago
     *
     * @return esPrepago
     **/
    @Schema(description = "")

    public Boolean isEsPrepago() {
        return esPrepago;
    }

    public void setEsPrepago(Boolean esPrepago) {
        this.esPrepago = esPrepago;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Registro registro = (Registro) o;
        return Objects.equals(this.placa, registro.placa) &&
                Objects.equals(this.zona, registro.zona) &&
                Objects.equals(this.promotor, registro.promotor) &&
                Objects.equals(this.horas, registro.horas) &&
                Objects.equals(this.esPrepago, registro.esPrepago);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placa, zona, promotor, horas, esPrepago);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Registro {\n");

        sb.append("    placa: ").append(toIndentedString(placa)).append("\n");
        sb.append("    zona: ").append(toIndentedString(zona)).append("\n");
        sb.append("    promotor: ").append(toIndentedString(promotor)).append("\n");
        sb.append("    horas: ").append(toIndentedString(horas)).append("\n");
        sb.append("    esPrepago: ").append(toIndentedString(esPrepago)).append("\n");
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
