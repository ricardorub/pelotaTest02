package com.example.peloteros.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Cancha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String tipo;
    private String ubicacion;
    private String direccion;
    private double precioPorHora;
    private String descripcion;
    private String horarioApertura;
    private String horarioCierre;
    private String fotoUrl;
    private String mapaUrl;
}