package com.example.zer.somos.comunes;

import org.json.JSONObject;

public class DatosSesion {
    private String sesion;
    private String cuenta;
    private String correoPromotor;
    private Long idPromotor;
    private Long idPerfil;
    private String nombrePerfil;
    private String nombreCuenta;
    private Long valorHoraCarro = 0l;
    private Long valorHoraMoto = 0l;
    private Long celdasCarro = 0L;
    private Long celdasMoto = 0L;
    private Long minutosGracia = 0l;
    private Long minutosParaNuevaGracia = 0l;
    //-------------- Partes de la zona
    private Long idZona;
    private String nombreZona;
    //-------------- Partes del promotor
    private String nombrePromotor;
    //-------------- Partes de la cuenta
    private String lema;
    private String nombreParaTiquete;
    private String nit;
    private String terminos;

    public DatosSesion(JSONObject cont) {
        if (cont == null) {
            return;
        }
        try {
            this.setCuenta(cont.getString("cuenta"));
            this.setCorreoPromotor(cont.getString("correo"));
            this.setIdPerfil(cont.getLong("idPerfil"));
            this.setNombrePerfil(cont.getString("perfil"));
            this.setNombreCuenta(cont.getString("nombre"));
            this.setSesion(cont.getString("sesion"));
        } catch (Exception ex) {
        }
    }

    public void setDatosZona(JSONObject cont) {
        DatosSesion local=this;
        new Thread(){
            public void run() {
                if (cont == null) {
                    return;
                }
                try {
                    local.setIdZona(cont.getLong("zona"));
                    local.setNombreZona(cont.getString("z_nombre"));
                    local.setIdPromotor(cont.getLong("promotor"));
                    StringBuilder nomPromotor=new StringBuilder();
                    nomPromotor.append(cont.getString("nombre1"));
                    nomPromotor.append(" ");
                    nomPromotor.append(cont.getString("nombre2"));
                    nomPromotor.append(" ");
                    nomPromotor.append(cont.getString("apellido1"));
                    nomPromotor.append(" ");
                    nomPromotor.append(cont.getString("apellido2"));

                    local.setNombrePromotor(nomPromotor.toString());
                    local.setValorHoraCarro(cont.getLong("z_valor_hora_carro"));
                    local.setValorHoraMoto(cont.getLong("z_valor_hora_moto"));
                    local.setMinutosGracia(cont.getLong("z_minutos_gracia"));
                    local.setMinutosParaNuevaGracia(cont.getLong("z_minutos_para_nueva_gracia"));
                    local.setCeldasCarro(cont.getLong("z_celdas_carro"));
                    local.setCeldasMoto(cont.getLong("z_celdas_moto"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    public void setDatosTiquete(JSONObject cont) {
        if (cont == null) {
            return;
        }
        try {
            this.setNit(cont.getString("nit"));
            this.setLema(cont.getString("lema"));
            this.setNombreParaTiquete(cont.getString("nombre"));
            this.setTerminos(cont.getString("terminos"));
        } catch (Exception ex) {
        }
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getCorreoPromotor() {
        return correoPromotor;
    }

    public void setCorreoPromotor(String correoPromotor) {
        this.correoPromotor = correoPromotor;
    }

    public Long getIdPromotor() {
        return idPromotor;
    }

    public void setIdPromotor(Long idPromotor) {
        this.idPromotor = idPromotor;
    }

    public Long getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Long idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getNombrePerfil() {
        return nombrePerfil;
    }

    public void setNombrePerfil(String nombrePerfil) {
        this.nombrePerfil = nombrePerfil;
    }

    public String getNombreCuenta() {
        return nombreCuenta;
    }

    public void setNombreCuenta(String nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
    }

    public Long getIdZona() {
        return idZona;
    }

    public void setIdZona(Long idZona) {
        this.idZona = idZona;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    public String getNombrePromotor() {
        return nombrePromotor;
    }

    public void setNombrePromotor(String nombrePromotor) {
        this.nombrePromotor = nombrePromotor;
    }

    public String getLema() {
        return lema;
    }

    public void setLema(String lema) {
        this.lema = lema;
    }

    public String getNombreParaTiquete() {
        return nombreParaTiquete;
    }

    public void setNombreParaTiquete(String nombreParaTiquete) {
        this.nombreParaTiquete = nombreParaTiquete;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getTerminos() {
        return terminos;
    }

    public void setTerminos(String terminos) {
        this.terminos = terminos;
    }

    public String getSesion() {
        return sesion;
    }

    public void setSesion(String sesion) {
        this.sesion = sesion;
    }

    public Long getValorHoraCarro() {
        return valorHoraCarro;
    }

    public void setValorHoraCarro(Long valorHoraCarro) {
        this.valorHoraCarro = valorHoraCarro;
    }

    public Long getValorHoraMoto() {
        return valorHoraMoto;
    }

    public void setValorHoraMoto(Long valorHoraMoto) {
        this.valorHoraMoto = valorHoraMoto;
    }

    public Long getCeldasCarro() {
        return celdasCarro;
    }

    public void setCeldasCarro(Long celdasCarro) {
        this.celdasCarro = celdasCarro;
    }

    public Long getCeldasMoto() {
        return celdasMoto;
    }

    public void setCeldasMoto(Long celdasMoto) {
        this.celdasMoto = celdasMoto;
    }

    public Long getMinutosGracia() {
        return minutosGracia;
    }

    public void setMinutosGracia(Long minutosGracia) {
        this.minutosGracia = minutosGracia;
    }

    public Long getMinutosParaNuevaGracia() {
        return minutosParaNuevaGracia;
    }

    public void setMinutosParaNuevaGracia(Long minutosParaNuevaGracia) {
        this.minutosParaNuevaGracia = minutosParaNuevaGracia;
    }
}
