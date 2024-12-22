package com.example.zer.somos.supervision;

import org.json.JSONException;
import org.json.JSONObject;

public class Pregunta {
    private Long id;
    private String pregunta;
    private Boolean cumple;
    private String valor = "";

    public Pregunta(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        try {
            setId(jsonObject.optLong("id", 0L)); // Usa optLong para valores predeterminados
            setPregunta(jsonObject.optString("pregunta", ""));
            setValor(jsonObject.optString("valor", ""));
            setCumple(jsonObject.optBoolean("cumple", false)); // Usa optBoolean para valores predeterminados
        } catch (Exception ex) {
            ex.printStackTrace(); // Log del error
        }
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public Boolean getCumple() {
        return cumple;
    }

    public void setCumple(Boolean cumple) {
        this.cumple = cumple;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}