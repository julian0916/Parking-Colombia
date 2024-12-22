package com.example.zer.somos.supervision;

import java.sql.Timestamp;

public class PromotorDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private Timestamp horaBloqueo;

    // Constructor
    public PromotorDTO(Long id, String nombre, String apellido, Timestamp horaBloqueo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.horaBloqueo = horaBloqueo;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }


    public Timestamp getHoraBloqueo() {
        return horaBloqueo;
    }

    public void setHoraBloqueo(Timestamp horaBloqueo) {
        this.horaBloqueo = horaBloqueo;
    }
}