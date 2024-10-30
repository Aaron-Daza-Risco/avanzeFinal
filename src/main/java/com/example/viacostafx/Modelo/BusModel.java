package com.example.viacostafx.Modelo;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "Buses")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BusModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Bus_INT")
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(length = 7)
    private String placa;

    @Column(length = 50)
    private String modelo;

    private Integer capacidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_Categoria")
    private CategoriaModel categoria;

    private Boolean isActive;

    @OneToMany(mappedBy = "bus")
    private List<ChoferModel> choferes;

    @OneToMany(mappedBy = "bus", fetch = FetchType.EAGER)
    private Set<AsientoModel> asientos;

    @OneToMany(mappedBy = "bus")
    private List<ViajeBusModel> viajeBuses;
}
