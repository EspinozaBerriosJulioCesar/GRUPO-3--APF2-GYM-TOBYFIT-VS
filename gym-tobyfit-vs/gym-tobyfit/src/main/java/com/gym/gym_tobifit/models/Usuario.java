package com.gym.gym_tobifit.models;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="usuarios") @Data @NoArgsConstructor @AllArgsConstructor
public class Usuario {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=50) private String nombre;
 @Column(nullable=false,length=50) private String apellido;
 @Column(nullable=false,length=15) private String telefono;
 @Column(nullable=false) private Integer edad;
 @Column(nullable=false,unique=true,length=100) private String correo;
 @Column(nullable=false,length=255) private String contrasena;
 @Enumerated(EnumType.STRING) private Rol rol;
 public enum Rol { USUARIO, ADMIN }
}
