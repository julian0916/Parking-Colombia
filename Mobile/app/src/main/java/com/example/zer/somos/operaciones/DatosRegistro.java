package com.example.zer.somos.operaciones;

import org.json.JSONObject;

public class DatosRegistro {
    private Long id;
    private String placa;
    private String ingreso;
    private String egreso;
    private String recaudo;
    private String promotorIngreso;
    private String promotorEgreso;
    private String nombreZona;
    private Long horasCobradas;
    private Long valorCobro;
    private Long estado;
    private JSONObject contenido;

    public DatosRegistro(JSONObject conten) {
        if (conten == null) {
            return;
        }
        try {
            this.setContenido(conten);
            this.setId(conten.getLong("id"));
            this.setEstado(conten.getLong("estado"));
            this.setIngreso(conten.getString("fhingreso"));
            this.setEgreso(conten.getString("fhegreso"));
            this.setRecaudo(conten.getString("fhrecaudo"));
            this.setPlaca(conten.getString("placa"));
            this.setNombreZona(conten.getString("nombreZona"));
            this.setPromotorIngreso(conten.getString("nombrePromotorIngreso"));
            this.setPromotorEgreso(conten.getString("nombrePromotorEgreso"));
            this.setHorasCobradas(conten.getLong("hcobradas"));
            this.setValorCobro(conten.getLong("valorCobrado"));
        } catch (Exception ex) {
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getIngreso() {
        return ingreso;
    }

    public void setIngreso(String ingreso) {
        this.ingreso = ingreso;
    }

    public String getRecaudo() {
        return recaudo;
    }

    public void setRecaudo(String recaudo) {
        this.recaudo = recaudo;
    }

    public String getPromotorIngreso() {
        return promotorIngreso;
    }

    public void setPromotorIngreso(String promotorIngreso) {
        this.promotorIngreso = promotorIngreso;
    }

    public String getPromotorEgreso() {
        return promotorEgreso;
    }

    public void setPromotorEgreso(String promotorEgreso) {
        this.promotorEgreso = promotorEgreso;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    public Long getHorasCobradas() {
        return horasCobradas;
    }

    public void setHorasCobradas(Long horasCobradas) {
        this.horasCobradas = horasCobradas;
    }

    public Long getValorCobro() {
        return valorCobro;
    }

    public void setValorCobro(Long valorCobro) {
        this.valorCobro = valorCobro;
    }

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    public String getEgreso() {
        return egreso;
    }

    public void setEgreso(String egreso) {
        this.egreso = egreso;
    }

    public JSONObject getContenido() {
        return contenido;
    }

    public void setContenido(JSONObject contenido) {
        this.contenido = contenido;
    }
}
