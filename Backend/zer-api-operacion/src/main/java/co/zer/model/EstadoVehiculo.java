package co.zer.model;

import java.time.LocalDateTime;

public class EstadoVehiculo {
    private Long idCuenta;//este es el id de la cuenta
    private String placa;//placa del vehículo
    private Boolean esCarro; //true es carro false es moto
    private Long zona;//zona en la que se encuentra
    private Boolean estaAbierta;//true está en la zona estado abierta
    private Boolean esPrepago;//si fue una venta prepago
    private LocalDateTime FHExpiraCredito;//fecha y hora hasta donde le alcanza el último pago
    private LocalDateTime FHUltimoPeriodoGracia;//fecha y hora en que
    private Long FNIngreso;//Corresponde a la fecha de ingreso, esto ayuda a limpiar los contenidos

    public Long getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(Long idCuenta) {
        this.idCuenta = idCuenta;
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

    public Boolean getEstaAbierta() {
        return estaAbierta;
    }

    public void setEstaAbierta(Boolean estaAbierta) {
        this.estaAbierta = estaAbierta;
    }

    public Boolean getEsPrepago() {
        return esPrepago;
    }

    public void setEsPrepago(Boolean esPrepago) {
        this.esPrepago = esPrepago;
    }

    public LocalDateTime getFHExpiraCredito() {
        return FHExpiraCredito;
    }

    public void setFHExpiraCredito(LocalDateTime FHExpiraCredito) {
        this.FHExpiraCredito = FHExpiraCredito;
    }

    public LocalDateTime getFHUltimoPeriodoGracia() {
        return FHUltimoPeriodoGracia;
    }

    public void setFHUltimoPeriodoGracia(LocalDateTime FHUltimoPeriodoGracia) {
        this.FHUltimoPeriodoGracia = FHUltimoPeriodoGracia;
    }

    public Long getFNIngreso() {
        return FNIngreso;
    }

    public void setFNIngreso(Long FNIngreso) {
        this.FNIngreso = FNIngreso;
    }
}
