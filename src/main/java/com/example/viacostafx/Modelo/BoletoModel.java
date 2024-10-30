package com.example.viacostafx.Modelo;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "Boleto")
public class BoletoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Boleto_INT")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_Viaje")
    private ViajeModel viaje;

    @ManyToOne
    @JoinColumn(name = "ID_Asiento")
    private AsientoModel asiento;

    private LocalDateTime fechaHoraCompra;

    private BigDecimal precio;

    @OneToMany(mappedBy = "boleto")
    private List<DetalleCompraModel> detalles;
}
