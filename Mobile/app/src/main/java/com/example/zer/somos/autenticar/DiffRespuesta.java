package com.example.zer.somos.autenticar;

import java.io.Serializable;
import java.util.Objects;

public class DiffRespuesta implements Serializable {
    private static final long serialVersionUID = 1L;

    private String g = null;

    private String p = null;

    private String B = null;

    private String idSolicitud = null;

    public DiffRespuesta g(String g) {
        this.g = g;
        return this;
    }


    public String getG() {
        return g;
    }

    public void setG(String g) {
        this.g = g;
    }

    public DiffRespuesta p(String p) {
        this.p = p;
        return this;
    }


    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public DiffRespuesta B(String B) {
        this.B = B;
        return this;
    }


    public String getB() {
        return B;
    }

    public void setB(String B) {
        this.B = B;
    }

    public DiffRespuesta idSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
        return this;
    }


    public String getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DiffRespuesta diffRespuesta = (DiffRespuesta) o;
        return Objects.equals(this.g, diffRespuesta.g) &&
                Objects.equals(this.p, diffRespuesta.p) &&
                Objects.equals(this.B, diffRespuesta.B) &&
                Objects.equals(this.idSolicitud, diffRespuesta.idSolicitud);
    }

    @Override
    public int hashCode() {
        return Objects.hash(g, p, B, idSolicitud);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DiffRespuesta {\n");

        sb.append("    g: ").append(toIndentedString(g)).append("\n");
        sb.append("    p: ").append(toIndentedString(p)).append("\n");
        sb.append("    B: ").append(toIndentedString(B)).append("\n");
        sb.append("    idSolicitud: ").append(toIndentedString(idSolicitud)).append("\n");
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
