package com.example.viacostafx.Modelo;
import jakarta.persistence.*;
import lombok.*;
@Data
@Entity
@Table(name = "Viaje_Bus")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ViajeBusModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Viaje_Bus_INT")
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_Viaje")
    private ViajeModel viaje;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_Bus")
    private BusModel bus;


}
