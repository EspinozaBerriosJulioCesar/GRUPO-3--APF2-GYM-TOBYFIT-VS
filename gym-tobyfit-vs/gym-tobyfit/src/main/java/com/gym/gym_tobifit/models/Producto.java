package com.gym.gym_tobifit.models;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
@Entity @Table(name="productos") @Data @NoArgsConstructor @AllArgsConstructor
public class Producto {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=100) private String nombre;
 @Column(nullable=false,length=50) private String categoria;
 @Column(nullable=false,length=20) private String genero;
 @Column(nullable=false,precision=10,scale=2) private BigDecimal precio;
 @Column(nullable=false,length=255) private String imagenPrincipal;
 @Column(columnDefinition="TEXT") private String imagenes;
 @Column(columnDefinition="TEXT") private String modelos;
 @Column(columnDefinition="boolean default true") private Boolean activo=true;
}
