package com.example.viacostafx.Controller;

import com.example.viacostafx.Modelo.BusModel;
import com.example.viacostafx.Modelo.ViajeBusModel;
import com.example.viacostafx.Modelo.ViajeModel;
import com.example.viacostafx.dao.AgenciaDao;
import com.example.viacostafx.dao.ViajeDao;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;


public class InterfazPrincipalController implements Initializable {
    @FXML
    private ComboBox<String> destinoCombo;
    @FXML
    private DatePicker viajeDate;
    @FXML
    private ComboBox<String> origenCombo;
    @FXML
    private DatePicker retornoDate;
    @FXML
    private Button btnBuscar;
    @FXML
    private GridPane Tabla1; // Vinculamos el GridPane desde el FXML

    @FXML
    private TableView<ViajeModel> tablaViajes;

    @FXML
    private TableColumn<ViajeModel, String> origenColumn;
    @FXML
    private TableColumn<ViajeModel, String> destinoColumn;
    @FXML
    private TableColumn<ViajeModel, String> horaSalidaColumn;
    @FXML
    private TableColumn<ViajeModel, String> tipoBusColumn;
    @FXML
    private TableColumn<ViajeModel, String> disponibilidadColumn;
    @FXML
    private TableColumn<ViajeModel, String> precioColumn;
    @FXML
    private Button btnRegistroUsuarios;
    @FXML
    private AnchorPane mainContent;
    @FXML
    private Pane contentArea;

    private Node registroUsuariosPanel;

    private Pane panelSecundario;
    private List<Node> elementosOriginalesTabla1;

    private ObservableList<String> distritosList;
    private FilteredList<String> origenFilteredList;
    private FilteredList<String> destinoFilteredList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        elementosOriginalesTabla1 = new ArrayList<>(Tabla1.getChildren());
        btnBuscar.setOnAction(event -> buscarYCargarPanelSecundario());

        // Cargar distritos en los ComboBox
        cargarDistritosEnComboBox();

        // Configurar cellValueFactory para cada columna
        origenColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAgenciaOrigen().getUbigeo().getDistrito()));
        destinoColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAgenciaDestino().getUbigeo().getDistrito()));
        horaSalidaColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFechaHoraSalida().toLocalTime().toString()));
        tipoBusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(obtenerTipoBus(cellData.getValue())));
        disponibilidadColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(obtenerDisponibilidad(cellData.getValue())));
        precioColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("S/ %.2f", calcularPrecio(cellData.getValue()))));
    }


    private void cargarDistritosEnComboBox() {
        List<String> distritos = AgenciaDao.obtenerDistritosConAgencias();

        ObservableList<String> distritosList = FXCollections.observableArrayList(distritos);

        origenCombo.setItems(distritosList);
        destinoCombo.setItems(distritosList);

        // Establecer texto de ayuda (prompt)
        origenCombo.setPromptText("Seleccione origen");
        destinoCombo.setPromptText("Seleccione destino");

        tablaViajes.setRowFactory(tv -> {
            TableRow<ViajeModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    ViajeModel viajeSeleccionado = row.getItem();
                    manejarDobleClicEnViaje(viajeSeleccionado);
                }
            });
            return row;
        });
    }

    private void manejarDobleClicEnViaje(ViajeModel viaje) {
        BusModel bus = obtenerBusDelViaje(viaje); // Obtener el bus del viaje
        if (bus != null) {
            int capacidad = bus.getCapacidad();
            String descripcionServicios = bus.getCategoria().getDescripcion();

            if (capacidad == 36) {
                abrirInterfaz("/GUI/BusPiso1.fxml", descripcionServicios, bus);
            } else if (capacidad == 51) {
                abrirInterfaz("/GUI/BusPiso1y2.fxml", descripcionServicios, bus);
            } else {
                mostrarAlerta("No se encontró una interfaz para la capacidad de " + capacidad + " asientos.");
            }
        } else {
            mostrarAlerta("No se pudo obtener la información del bus para el viaje seleccionado.");
        }
    }


    private void abrirInterfaz(String fxmlPath, String descripcionServicios, BusModel bus) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Pane root = loader.load();

            // Obtener el controlador de la nueva interfaz
            Object controller = loader.getController();

            if (controller instanceof BusPiso1Controller) {
                BusPiso1Controller busController = (BusPiso1Controller) controller;
                busController.mostrarDescripcionServicios(descripcionServicios);
                busController.setBusId(bus.getId());
            } else if (controller instanceof BusPiso1y2Controller) {
                BusPiso1y2Controller busController = (BusPiso1y2Controller) controller;
                busController.mostrarDescripcionServicios(descripcionServicios);
                // Puedes pasar el busId o cualquier otra información necesaria
                busController.setBusId(bus.getId());
            } else {
                mostrarAlerta("Controlador desconocido.");
                return;
            }

            // Crear una nueva escena y mostrarla en una nueva ventana
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Selección de Asiento");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al cargar la interfaz.");
        }
    }


    @FXML
    private void buscarYCargarPanelSecundario() {
        if (retornoDate.getValue() != null){
            mostrarPanelSecundario();
        } else if (retornoDate.getValue() == null){
            Tabla1.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == 0);
            cargarTablaPrincipall();

            // Aquí llamamos al nuevo método para cargar los viajes en la tabla
            buscarYCargarViajesEnTabla();
        }
    }

    private void mostrarPanelSecundario() {
        // Cargamos el Panel Secundario desde el FXML sólo una vez
        if (panelSecundario == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/InterfazSecundaria.fxml"));
                panelSecundario = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("No se pudo cargar el Panel Secundario");
                return;
            }
        }

        // Añadimos el panel secundario al GridPane principal
        if (Tabla1 != null) {
            // Eliminamos cualquier elemento previo en la posición deseada, si es necesario
            Tabla1.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == 1);

            // Añadimos el panel secundario en la posición deseada
            Tabla1.add(panelSecundario, 0, 1);

        } else {
            System.out.println("Error: gridPane no está inicializado.");
        }
    }

    private void cargarTablaPrincipall() {
        Tabla1.getChildren().clear();
        Tabla1.getChildren().addAll(elementosOriginalesTabla1);
    }

    private void buscarYCargarViajesEnTabla() {
        String origen = origenCombo.getSelectionModel().getSelectedItem();
        String destino = destinoCombo.getSelectionModel().getSelectedItem();
        LocalDate fechaSeleccionada = viajeDate.getValue();

        if (origen == null || destino == null || fechaSeleccionada == null) {
            // Mostrar mensaje de error indicando que deben seleccionar origen, destino y fecha
            mostrarAlerta("Debe seleccionar origen, destino y fecha de viaje.");
            return;
        }

        // Obtener los viajes disponibles
        List<ViajeModel> viajesDisponibles = ViajeDao.obtenerViajesDisponibles(origen, destino, fechaSeleccionada);

        if (viajesDisponibles == null || viajesDisponibles.isEmpty()) {
            // Mostrar mensaje indicando que no hay viajes disponibles
            mostrarAlerta("No hay viajes disponibles para los criterios seleccionados.");
            return;
        }

        // Cargar los viajes en la tabla
        cargarViajesEnTabla(viajesDisponibles);
    }


    private void cargarViajesEnTabla(List<ViajeModel> viajesDisponibles) {
        tablaViajes.setItems(FXCollections.observableArrayList(viajesDisponibles));
    }


    private String obtenerTipoBus(ViajeModel viaje) {
        BusModel bus = obtenerBusDelViaje(viaje);

        if (bus != null) {
            System.out.println("Bus ID: " + bus.getId());
            if (bus.getCategoria() != null) {
                System.out.println("Categoría: " + bus.getCategoria().getNombre());
                return bus.getCategoria().getNombre();
            } else {
                System.out.println("bus.getCategoria() es null");
            }
        } else {
            System.out.println("Bus es null");
        }
        return "Desconocido";
    }



    private BusModel obtenerBusDelViaje(ViajeModel viaje) {
        if (viaje.getViajeBuses() != null) {
            System.out.println("viaje.getViajeBuses().size(): " + viaje.getViajeBuses().size());
            if (!viaje.getViajeBuses().isEmpty()) {
                ViajeBusModel viajeBus = viaje.getViajeBuses().iterator().next();
                if (viajeBus != null) {
                    System.out.println("viajeBus ID: " + viajeBus.getId());
                    BusModel bus = viajeBus.getBus();
                    if (bus != null) {
                        System.out.println("Bus ID: " + bus.getId());
                        return bus;
                    } else {
                        System.out.println("viajeBus.getBus() es null");
                    }
                } else {
                    System.out.println("viajeBus es null");
                }
            } else {
                System.out.println("viaje.getViajeBuses() está vacío");
            }
        } else {
            System.out.println("viaje.getViajeBuses() es null");
        }
        return null;
    }
    @FXML
    private void onRegistroUsuariosClick() {
        try {
            // Cargar el panel de registro solo si no se ha cargado antes
            if (registroUsuariosPanel == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/RegistroEmpleado.fxml"));
                registroUsuariosPanel = (AnchorPane) loader.load(); // O el tipo específico que corresponda (Pane, VBox, etc.)

                // Configurar el tamaño usando propiedades directamente
                AnchorPane.setTopAnchor(registroUsuariosPanel, 0.0);
                AnchorPane.setBottomAnchor(registroUsuariosPanel, 0.0);
                AnchorPane.setLeftAnchor(registroUsuariosPanel, 0.0);
                AnchorPane.setRightAnchor(registroUsuariosPanel, 0.0);
            }

            // Limpiar el área de contenido
            contentArea.getChildren().clear();

            // Añadir el panel de registro al área de contenido
            contentArea.getChildren().add(registroUsuariosPanel);

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al cargar la vista");
            alert.setContentText("No se pudo cargar la vista de registro de usuarios: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void onMenuPrincipalClick() {
        try {
            // Limpiar el contenido actual
            mainContent.getChildren().clear();

            // Cargar la interfaz principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/InterfazPrincipal.fxml"));
            AnchorPane principalView = loader.load();

            // Configurar el nuevo contenido para que ocupe todo el espacio
            AnchorPane.setTopAnchor(principalView, 0.0);
            AnchorPane.setBottomAnchor(principalView, 0.0);
            AnchorPane.setLeftAnchor(principalView, 0.0);
            AnchorPane.setRightAnchor(principalView, 0.0);

            // Añadir la vista principal al mainContent
            mainContent.getChildren().add(principalView);

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al cargar la interfaz principal");
            alert.setContentText("No se pudo cargar la interfaz principal: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }



    private String obtenerDisponibilidad(ViajeModel viaje) {
        BusModel bus = obtenerBusDelViaje(viaje);
        if (bus != null) {
            if (bus.getAsientos() != null) {
                long asientosDisponibles = bus.getAsientos().stream()
                        .filter(asiento -> "DESOCUPADO".equalsIgnoreCase(asiento.getEstado()))
                        .count();
                System.out.println("Asientos disponibles: " + asientosDisponibles);
                return asientosDisponibles > 0 ? "Disponible (" + asientosDisponibles + " asientos)" : "Bus Lleno";
            } else {
                System.out.println("Asientos es null");
            }
        } else {
            System.out.println("Bus es null");
        }
        return "Desconocido";
    }


    private double calcularPrecio(ViajeModel viaje) {
        double precioBase = 35.0;
        BusModel bus = obtenerBusDelViaje(viaje);

        if (bus != null && bus.getCategoria() != null && bus.getCategoria().getCostoExtra() != null) {
            precioBase += bus.getCategoria().getCostoExtra().doubleValue();
        }

        return precioBase;
    }


    private void mostrarAlerta(String mensaje) {
        // Implementar la lógica para mostrar un mensaje al usuario (puede ser un Alert de JavaFX)
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


}