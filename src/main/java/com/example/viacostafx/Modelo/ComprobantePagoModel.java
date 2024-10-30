package com.example.viacostafx.Modelo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Comprobante_Pago")
public class ComprobantePagoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Documento_INT")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_Detalle")
    private DetalleCompraModel detalle;

    private LocalDateTime fechaEmision;

}
