package com.example.viacostafx.Modelo;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "Compra")

public class CompraModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Compra_INT")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_Pasajero")
    private PasajeroModel pasajero;

    @ManyToOne
    @JoinColumn(name = "ID_Empleado")
    private EmpleadosModel empleado;

    @OneToMany(mappedBy = "compra")
    private List<DetalleCompraModel> detalles;
}

