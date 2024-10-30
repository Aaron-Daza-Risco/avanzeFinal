package com.example.viacostafx.Controller;

import com.example.viacostafx.Modelo.AsientoModel;
import com.example.viacostafx.dao.AsientoDao;
import com.example.viacostafx.dao.BusDao;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import lombok.Setter;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class BusPiso1y2Controller implements Initializable {
    @FXML
    private TextArea txtServicios;

    @Setter
    private int busId;
    @FXML
    private GridPane gridAsiento1;

    @FXML
    private GridPane gridAsiento2;
    private Map<Integer, Button> botonesAsientos;
    private AsientoDao asientoDAO;
    private BusDao busDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void mostrarDescripcionServicios(String descripcionServicios) {
        txtServicios.setText(descripcionServicios);
    }

    public void generarAsientos(){




    }

}
