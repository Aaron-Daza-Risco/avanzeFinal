package com.example.viacostafx.Controller;
import com.example.viacostafx.Modelo.AsientoModel;
import com.example.viacostafx.Modelo.BusModel;
import com.example.viacostafx.Modelo.ViajeBusModel;
import com.example.viacostafx.dao.AsientoDao;
import com.example.viacostafx.dao.BusDao;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.List;

public class BusPiso1Controller implements Initializable {
    @FXML
    private GridPane gridAsientos;

    @FXML
    private TextArea txtServicios;

    private Map<Integer, Button> botonesAsientos;
    private AsientoDao asientoDAO;
    private BusDao busDAO;
    private int busId;
    private ViajeBusModel viajeBusDAO;
    private int viajeId;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gridAsientos.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            }
        });
    }


    public BusPiso1Controller() {
        asientoDAO = new AsientoDao();
        busDAO = new BusDao();
        botonesAsientos = new HashMap<>();
    }

    public void mostrarDescripcionServicios(String descripcionServicios) {
        txtServicios.setText(descripcionServicios);
    }

    public void setBusId(int busId) {
        this.busId = busId;
        inicializarAsientos();
    }

    private void inicializarAsientos() {

            gridAsientos.getChildren().clear();

            // Obtén el busId asociado al viaje


            // Cargar los asientos correspondientes al bus
            List<AsientoModel> asientos = asientoDAO.obtenerAsientosPorBus(busId);
            System.out.println("Cantidad de asientos: " + asientos.size());

            // Resto de la lógica para agregar botones de asientos en el grid...
            int columna = 1;
            int fila = 3;
            for (AsientoModel asiento : asientos) {
                Button btn = new Button(asiento.getNumero());
                btn.setMinSize(50, 50);
                btn.setMaxSize(50, 50);
                botonesAsientos.put(asiento.getId(), btn);

                if (asiento.getEstado().equals("DESOCUPADO")) {
                    btn.getStyleClass().add("asiento-disponible");
                } else {
                    btn.getStyleClass().add("asiento-ocupado");
                }

                btn.setOnAction(e -> handleAsientoClick(asiento));
                gridAsientos.add(btn, columna, fila);

                fila--;
                if (fila < 0) {
                    fila = 3;
                    columna++;
                }
            }

            // Añadir asiento de conductor
            Button btnConductor = new Button("Conductor");
            btnConductor.setMinSize(50, 50);
            btnConductor.getStyleClass().add("asiento-conductor");
            gridAsientos.add(btnConductor, 0, 3);
        }



    private void handleAsientoClick(AsientoModel asiento) {
        boolean nuevoEstado = !asiento.getEstado().equals("OCUPADO");
        asiento.setEstado(nuevoEstado ? "OCUPADO" : "DESOCUPADO");

        if (asientoDAO.actualizarAsiento(asiento)) {
            actualizarEstiloBoton(asiento);
        }
    }

    private void actualizarEstiloBoton(AsientoModel asiento) {
        Button btn = botonesAsientos.get(asiento.getId());
        btn.getStyleClass().removeAll("asiento-ocupado", "asiento-disponible");
        if (asiento.getEstado().equals("DESOCUPADO")) {
            btn.getStyleClass().add("asiento-disponible");
        } else {
            btn.getStyleClass().add("asiento-ocupado");
        }
    }
}