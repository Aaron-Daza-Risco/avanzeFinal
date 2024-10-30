package com.example.viacostafx.Modelo;

import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "Categoria")
public class CategoriaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Categoria_INT")
    private Integer id;

    @Column(length = 50)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(name = "Costo_extra")
    private BigDecimal costoExtra;

    private Boolean isActive;

    @OneToMany(mappedBy = "categoria")
    private List<BusModel> buses;

}
