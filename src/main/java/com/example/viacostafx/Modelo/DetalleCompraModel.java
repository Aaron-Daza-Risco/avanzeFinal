package com.example.viacostafx.Modelo;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "Detalle_Compra")
public class DetalleCompraModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Detalle_INT")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_Compra")
    private CompraModel compra;

    @ManyToOne
    @JoinColumn(name = "ID_Boleto")
    private BoletoModel boleto;

    private LocalDateTime fechaHoraCompra;

    private BigDecimal precio;

    private BigDecimal precioTotal;

    @OneToMany(mappedBy = "detalle")
    private List<ComprobantePagoModel> comprobantes;
}
