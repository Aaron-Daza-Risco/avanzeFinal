package com.example.viacostafx.Modelo;
import jakarta.persistence.*;
import lombok.*;
@Data
@Entity
@Table(name = "Choferes")
public class ChoferModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Chofer_INT")
    private Integer id;

    @Column(length = 50)
    private String nombre;

    @Column(length = 8)
    private String dni;

    @Column(name = "Nro_Licencia", length = 20)
    private String numeroLicencia;

    @Column(length = 15)
    private String telefono;

    @Column(length = 100)
    private String email;

    @ManyToOne
    @JoinColumn(name = "ID_Bus")
    private BusModel bus;

    private Boolean isActive;
}
