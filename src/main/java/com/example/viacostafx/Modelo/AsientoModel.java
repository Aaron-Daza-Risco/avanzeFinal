package com.example.viacostafx.Modelo;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "Asiento")
public class AsientoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Asiento_INT")
    private Integer id;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "ID_Bus")
    private BusModel bus;

    @Column(length = 3)
    private String numero;

    @Column(length = 20)
    private String estado;

    @OneToMany(mappedBy = "asiento")
    private List<BoletoModel> boletos;
}
