package com.gym.gym_tobifit.dto;

import lombok.Data;

@Data
public class RegistroRequest {

    private String nombre;
    private String apellido;
    private String telefono;
    private Integer edad;
    private String correo;
    private String contrasena;
}