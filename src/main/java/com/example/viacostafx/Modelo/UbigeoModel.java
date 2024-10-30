package com.example.viacostafx.Modelo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Ubigeo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UbigeoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Ubigeo_INT")
    private Integer id;

    @Column(name = "Ubigeo_Codigo", length = 10)
    private String codigo;

    @Column(name = "Ubigeo_Departamento", length = 50)
    private String departamento;

    @Column(name = "Ubigeo_Provincia", length = 50)
    private String provincia;

    @Column(name = "Ubigeo_Distrito", length = 50)
    private String distrito;

    @OneToMany(mappedBy = "ubigeo")
    private List<AgenciaModel> agencias;
}
