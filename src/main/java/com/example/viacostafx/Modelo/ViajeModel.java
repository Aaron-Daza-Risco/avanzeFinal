package com.example.viacostafx.Modelo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "Viaje")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ViajeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Viaje_INT")
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_Agencia_Origen_INT")
    private AgenciaModel agenciaOrigen;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_Agencia_Destino_INT")
    private AgenciaModel agenciaDestino;

    private LocalDateTime fechaHoraSalida;

    private Boolean isActive;

    @OneToMany(mappedBy = "viaje", fetch = FetchType.EAGER)
    private List<ViajeBusModel> viajeBuses;

    @OneToMany(mappedBy = "viaje")
    private List<BoletoModel> boletos;
}
