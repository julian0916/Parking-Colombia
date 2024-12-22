package co.zer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * RegistroSupervision
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-01-14T15:45:43.235Z[GMT]")


public class RegistroSupervision implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("fh_registro")
    private String fhRegistro = null;

    @JsonProperty("nf_registro")
    private Long nfRegistro = null;

    @JsonProperty("promotor")
    private Long promotor = null;

    @JsonProperty("supervisor")
    private Long supervisor = null;

    @JsonProperty("zona")
    private Long zona = null;

    @JsonProperty("firmado")
    private Boolean firmado = null;

    @JsonProperty("firmaHash")
    private String firmaHash = null;

    @JsonProperty("informacion")
    private String informacion = null;

    @JsonProperty("preguntas")
    @Valid
    private List<PreguntaSupervision> preguntas = null;

    private String decodificarTexto(String value) {
        String data = "";
        try {
            data = URLDecoder.decode(value, StandardCharsets.UTF_8.displayName());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void decodificarContenido() {
        setInformacion(decodificarTexto(getInformacion()));
        decodificarontenidoPreguntas();
    }

    private void decodificarontenidoPreguntas() {
        List<PreguntaSupervision> preguntas = getPreguntas();
        for (PreguntaSupervision pregunta : preguntas) {
            pregunta.setPregunta(decodificarTexto(pregunta.getPregunta()));
            pregunta.setValor(decodificarTexto(pregunta.getValor()));
        }
    }

    public RegistroSupervision id(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     *
     * @return id
     **/
    @Schema(description = "")

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegistroSupervision fhRegistro(String fhRegistro) {
        this.fhRegistro = fhRegistro;
        return this;
    }

    /**
     * Get fhRegistro
     *
     * @return fhRegistro
     **/
    @Schema(description = "")

    public String getFhRegistro() {
        return fhRegistro;
    }

    public void setFhRegistro(String fhRegistro) {
        this.fhRegistro = fhRegistro;
    }

    public RegistroSupervision nfRegistro(Long nfRegistro) {
        this.nfRegistro = nfRegistro;
        return this;
    }

    /**
     * Get nfRegistro
     *
     * @return nfRegistro
     **/
    @Schema(description = "")

    public Long getNfRegistro() {
        return nfRegistro;
    }

    public void setNfRegistro(Long nfRegistro) {
        this.nfRegistro = nfRegistro;
    }

    public RegistroSupervision promotor(Long promotor) {
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

    public RegistroSupervision supervisor(Long supervisor) {
        this.supervisor = supervisor;
        return this;
    }

    /**
     * Get supervisor
     *
     * @return supervisor
     **/
    @Schema(description = "")

    public Long getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Long supervisor) {
        this.supervisor = supervisor;
    }

    public RegistroSupervision zona(Long zona) {
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

    public RegistroSupervision firmado(Boolean firmado) {
        this.firmado = firmado;
        return this;
    }

    /**
     * Get firmado
     *
     * @return firmado
     **/
    @Schema(description = "")

    public Boolean isFirmado() {
        return firmado;
    }

    public void setFirmado(Boolean firmado) {
        this.firmado = firmado;
    }

    public RegistroSupervision firmaHash(String firmaHash) {
        this.firmaHash = firmaHash;
        return this;
    }

    /**
     * Get firmaHash
     *
     * @return firmaHash
     **/
    @Schema(description = "")

    public String getFirmaHash() {
        return firmaHash;
    }

    public void setFirmaHash(String firmaHash) {
        this.firmaHash = firmaHash;
    }

    public RegistroSupervision informacion(String informacion) {
        this.informacion = informacion;
        return this;
    }

    /**
     * Get informacion
     *
     * @return informacion
     **/
    @Schema(description = "")

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }

    public RegistroSupervision preguntas(List<PreguntaSupervision> preguntas) {
        this.preguntas = preguntas;
        return this;
    }

    public RegistroSupervision addPreguntasItem(PreguntaSupervision preguntasItem) {
        if (this.preguntas == null) {
            this.preguntas = new ArrayList<>();
        }
        this.preguntas.add(preguntasItem);
        return this;
    }

    /**
     * Get preguntas
     *
     * @return preguntas
     **/
    @Schema(description = "")
    @Valid
    public List<PreguntaSupervision> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<PreguntaSupervision> preguntas) {
        this.preguntas = preguntas;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegistroSupervision registroSupervision = (RegistroSupervision) o;
        return Objects.equals(this.id, registroSupervision.id) &&
                Objects.equals(this.fhRegistro, registroSupervision.fhRegistro) &&
                Objects.equals(this.nfRegistro, registroSupervision.nfRegistro) &&
                Objects.equals(this.promotor, registroSupervision.promotor) &&
                Objects.equals(this.supervisor, registroSupervision.supervisor) &&
                Objects.equals(this.zona, registroSupervision.zona) &&
                Objects.equals(this.firmado, registroSupervision.firmado) &&
                Objects.equals(this.firmaHash, registroSupervision.firmaHash) &&
                Objects.equals(this.informacion, registroSupervision.informacion) &&
                Objects.equals(this.preguntas, registroSupervision.preguntas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fhRegistro, nfRegistro, promotor, supervisor, zona, firmado, firmaHash, informacion, preguntas);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RegistroSupervision {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    fhRegistro: ").append(toIndentedString(fhRegistro)).append("\n");
        sb.append("    nfRegistro: ").append(toIndentedString(nfRegistro)).append("\n");
        sb.append("    promotor: ").append(toIndentedString(promotor)).append("\n");
        sb.append("    supervisor: ").append(toIndentedString(supervisor)).append("\n");
        sb.append("    zona: ").append(toIndentedString(zona)).append("\n");
        sb.append("    firmado: ").append(toIndentedString(firmado)).append("\n");
        sb.append("    firmaHash: ").append(toIndentedString(firmaHash)).append("\n");
        sb.append("    informacion: ").append(toIndentedString(informacion)).append("\n");
        sb.append("    preguntas: ").append(toIndentedString(preguntas)).append("\n");
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
