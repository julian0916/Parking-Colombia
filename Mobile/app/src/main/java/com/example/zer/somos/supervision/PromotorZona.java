package com.example.zer.somos.supervision;

import org.json.JSONObject;

public class PromotorZona {
    private Long idPromotor;
    private Long idZona;
    private String nombrePromotor;
    private String nombreZona;
    private String numIdentificacionPromotor;

    public PromotorZona(JSONObject jsonObject){
        if(jsonObject==null){
            return;
        }
        try {
            setIdZona(jsonObject.getLong("zona"));
            setNombreZona(jsonObject.getString("z_nombre"));
            setIdPromotor(jsonObject.getLong("promotor"));
            String nombre = jsonObject.getString("nombre1")+" "+
                    jsonObject.getString("nombre2")+" "+
                    jsonObject.getString("apellido1")+" "+
                    jsonObject.getString("apellido2");
            setNombrePromotor(nombre);
        }catch (Exception ex){

        }
    }

    public Long getIdPromotor() {
        return idPromotor;
    }

    public void setIdPromotor(Long idPromotor) {
        this.idPromotor = idPromotor;
    }

    public Long getIdZona() {
        return idZona;
    }

    public void setIdZona(Long idZona) {
        this.idZona = idZona;
    }

    public String getNombrePromotor() {
        return nombrePromotor;
    }

    public void setNombrePromotor(String nombrePromotor) {
        this.nombrePromotor = nombrePromotor;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    public String getNumIdentificacionPromotor() {
        return numIdentificacionPromotor;
    }

    public void setNumIdentificacionPromotor(String numIdentificacionPromotor) {
        this.numIdentificacionPromotor = numIdentificacionPromotor;
    }
}
