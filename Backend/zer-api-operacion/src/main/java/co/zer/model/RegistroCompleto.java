package co.zer.model;

import co.zer.service.ComunRegistro;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class RegistroCompleto {
    private Long id;
    private String placa;
    private Boolean esCarro;
    private Long zona;
    private String nombreZona;
    private LocalTime HIniciaZona;
    private LocalTime HTerminaZona;
    private Long valorH;
    private Long promotorIngreso;
    private String nombrePromotorIngreso;
    private Long NFIngreso;
    private LocalDateTime FHIngreso;
    private Long estado;
    private Long minutosGraciaZona;
    private Long minutosParaNuevaGraciaZona;
    //Egreso
    private Long HCobradas;
    private Long valorCobrado;
    private Boolean egresaSistema;
    private Long promotorEgreso;
    private String nombrePromotorEgreso;
    private LocalDateTime FHEgreso;
    private Long NFEgreso;
    //Cobro
    private Long promotorRecauda;
    private String nombrePromotorRecauda;
    private Long promotorReporta;
    private String nombrePromotorReporta;
    private LocalDateTime FHRecaudo;
    private Long NFRecaudo;


    public RegistroCompleto() {
    }

    public RegistroCompleto(Registro registro) {
        this.setHCobradas(registro.getHoras());
        this.setPlaca(ComunRegistro.limpiarContenidosPlaca(registro.getPlaca()));
        this.setPromotorIngreso(registro.getPromotor());
        this.setZona(registro.getZona());
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

    public Boolean getEsCarro() {
        return esCarro;
    }

    public void setEsCarro(Boolean esCarro) {
        this.esCarro = esCarro;
    }

    public Long getZona() {
        return zona;
    }

    public void setZona(Long zona) {
        this.zona = zona;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    public LocalTime getHIniciaZona() {
        return HIniciaZona;
    }

    public void setHIniciaZona(LocalTime HIniciaZona) {
        this.HIniciaZona = HIniciaZona;
    }

    public LocalTime getHTerminaZona() {
        return HTerminaZona;
    }

    public void setHTerminaZona(LocalTime HTerminaZona) {
        this.HTerminaZona = HTerminaZona;
    }

    public Long getValorH() {
        return valorH;
    }

    public void setValorH(Long valorH) {
        this.valorH = valorH;
    }

    public Long getPromotorIngreso() {
        return promotorIngreso;
    }

    public void setPromotorIngreso(Long promotorIngreso) {
        this.promotorIngreso = promotorIngreso;
    }

    public String getNombrePromotorIngreso() {
        return nombrePromotorIngreso;
    }

    public void setNombrePromotorIngreso(String nombrePromotorIngreso) {
        this.nombrePromotorIngreso = nombrePromotorIngreso;
    }

    public Long getNFIngreso() {
        return NFIngreso;
    }

    public void setNFIngreso(Long NFIngreso) {
        this.NFIngreso = NFIngreso;
    }

    public LocalDateTime getFHIngreso() {
        return FHIngreso;
    }

    public void setFHIngreso(LocalDateTime FHIngreso) {
        this.FHIngreso = FHIngreso;
    }

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    public Long getHCobradas() {
        return HCobradas;
    }

    public void setHCobradas(Long HCobradas) {
        this.HCobradas = HCobradas;
    }

    public Long getValorCobrado() {
        return valorCobrado;
    }

    public void setValorCobrado(Long valorCobrado) {
        this.valorCobrado = valorCobrado;
    }

    public Boolean getEgresaSistema() {
        return egresaSistema;
    }

    public void setEgresaSistema(Boolean egresaSistema) {
        this.egresaSistema = egresaSistema;
    }

    public Long getPromotorEgreso() {
        return promotorEgreso;
    }

    public void setPromotorEgreso(Long promotorEgreso) {
        this.promotorEgreso = promotorEgreso;
    }

    public String getNombrePromotorEgreso() {
        return nombrePromotorEgreso;
    }

    public void setNombrePromotorEgreso(String nombrePromotorEgreso) {
        this.nombrePromotorEgreso = nombrePromotorEgreso;
    }

    public Long getNFEgreso() {
        return NFEgreso;
    }

    public void setNFEgreso(Long NFEgreso) {
        this.NFEgreso = NFEgreso;
    }

    public LocalDateTime getFHEgreso() {
        return FHEgreso;
    }

    public void setFHEgreso(LocalDateTime FHEgreso) {
        this.FHEgreso = FHEgreso;
    }

    public Long getPromotorRecauda() {
        return promotorRecauda;
    }

    public void setPromotorRecauda(Long promotorRecauda) {
        this.promotorRecauda = promotorRecauda;
    }

    public String getNombrePromotorRecauda() {
        return nombrePromotorRecauda;
    }

    public void setNombrePromotorRecauda(String nombrePromotorRecauda) {
        this.nombrePromotorRecauda = nombrePromotorRecauda;
    }

    public Long getPromotorReporta() {
        return promotorReporta;
    }

    public void setPromotorReporta(Long promotorReporta) {
        this.promotorReporta = promotorReporta;
    }

    public String getNombrePromotorReporta() {
        return nombrePromotorReporta;
    }

    public void setNombrePromotorReporta(String nombrePromotorReporta) {
        this.nombrePromotorReporta = nombrePromotorReporta;
    }

    public LocalDateTime getFHRecaudo() {
        return FHRecaudo;
    }

    public void setFHRecaudo(LocalDateTime FHRecaudo) {
        this.FHRecaudo = FHRecaudo;
    }

    public Long getNFRecaudo() {
        return NFRecaudo;
    }

    public void setNFRecaudo(Long NFRecaudo) {
        this.NFRecaudo = NFRecaudo;
    }

    public Long getMinutosGraciaZona() {
        return minutosGraciaZona;
    }

    public void setMinutosGraciaZona(Long minutosGraciaZona) {
        this.minutosGraciaZona = minutosGraciaZona;
    }

    public Long getMinutosParaNuevaGraciaZona() {
        return minutosParaNuevaGraciaZona;
    }

    public void setMinutosParaNuevaGraciaZona(Long minutosParaNuevaGraciaZona) {
        this.minutosParaNuevaGraciaZona = minutosParaNuevaGraciaZona;
    }
}
