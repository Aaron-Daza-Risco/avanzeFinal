package com.example.viacostafx.Modelo;

import jakarta.persistence.*;
import lombok.Data;
import javafx.beans.property.*;

import java.util.List;

@Data
@Entity
@Table(name = "Pasajero")
public class PasajeroModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Pasajero_INT")
    private Integer id;

    @Column(length = 50)
    private String nombre;

    @Column(length = 8)
    private String dni;

    @Column(length = 100)
    private String email;

    @Column(length = 15)
    private String telefono;

    @OneToMany(mappedBy = "pasajero")
    private List<CompraModel> compras;

    // Métodos para adaptar a Property de JavaFX
    public IntegerProperty idProperty() {
        return new SimpleIntegerProperty(id);
    }

    public StringProperty nombreProperty() {
        return new SimpleStringProperty(nombre);
    }

    public StringProperty dniProperty() {
        return new SimpleStringProperty(dni);
    }

    public StringProperty emailProperty() {
        return new SimpleStringProperty(email);
    }

    public StringProperty telefonoProperty() {
        return new SimpleStringProperty(telefono);
    }
}
