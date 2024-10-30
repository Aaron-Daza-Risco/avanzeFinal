package com.example.viacostafx.Modelo;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "Agencia")
public class AgenciaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Agencia_INT")
    private Integer id;

    @Column(length = 100)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "ID_Ubigeo")
    private UbigeoModel ubigeo;

    private Boolean isActive;

    @OneToMany(mappedBy = "agenciaOrigen")
    private List<ViajeModel> viajesOrigen;

    @OneToMany(mappedBy = "agenciaDestino")
    private List<ViajeModel> viajesDestino;
}