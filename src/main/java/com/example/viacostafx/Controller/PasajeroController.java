package com.example.viacostafx.Controller;

import com.example.viacostafx.Modelo.PasajeroModel;
import com.example.viacostafx.dao.PasajeroDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PasajeroController implements Initializable {

    @FXML
    private GridPane Tabla1;

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnModificar;

    @FXML
    private TableColumn<PasajeroModel, Integer> idPasajero;

    @FXML
    private TableColumn<PasajeroModel, String> nombrePasajero;

    @FXML
    private TableColumn<PasajeroModel, String> dniPasajero;

    @FXML
    private TableColumn<PasajeroModel, String> emailPasajero;

    @FXML
    private TableColumn<PasajeroModel, String> telefonoPasajero;

    @FXML
    private TableView<PasajeroModel> tablaPasajeros;

    @FXML
    private TextField txtBuscar;

    @FXML
    private TextField txtDni;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtTelefono;

    private PasajeroDao pasajeroDao = new PasajeroDao();
    private ObservableList<PasajeroModel> pasajeros;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarColumnas();
        cargarPasajeros();
        configurarEventos();
        btnBuscar.setOnAction(event -> buscarPasajero());
    }
    private void buscarPasajero() {
        String dniBuscado = txtBuscar.getText().trim(); // Obtener el valor del campo de búsqueda

        // Buscar el pasajero en la base de datos
        List<PasajeroModel> resultados = pasajeroDao.buscarPasajerosPorDni(dniBuscado);

        // Limpiar la tabla
        tablaPasajeros.getItems().clear();

        // Agregar los resultados a la tabla
        if (resultados != null) {
            ObservableList<PasajeroModel> observableList = FXCollections.observableArrayList(resultados);
            tablaPasajeros.setItems(observableList);
        }
    }

    private void inicializarColumnas() {
        idPasajero.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nombrePasajero.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        dniPasajero.setCellValueFactory(cellData -> cellData.getValue().dniProperty());
        emailPasajero.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        telefonoPasajero.setCellValueFactory(cellData -> cellData.getValue().telefonoProperty());
    }

    private void cargarPasajeros() {
        pasajeros = FXCollections.observableArrayList(pasajeroDao.obtenerTodosLosPasajeros());
        tablaPasajeros.setItems(pasajeros);
    }

    private void configurarEventos() {
        btnAgregar.setOnAction(event -> agregarPasajero());
        btnModificar.setOnAction(event -> modificarPasajero());
        tablaPasajeros.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarDetallesPasajero(newSelection);
            }
        });
    }

    private void agregarPasajero() {
        PasajeroModel pasajero = new PasajeroModel();
        pasajero.setNombre(txtNombre.getText());
        pasajero.setDni(txtDni.getText());
        pasajero.setEmail(txtEmail.getText());
        pasajero.setTelefono(txtTelefono.getText());

        pasajeroDao.guardarPasajero(pasajero);
        cargarPasajeros();
        limpiarCampos();
    }

    private void modificarPasajero() {
        PasajeroModel pasajeroSeleccionado = tablaPasajeros.getSelectionModel().getSelectedItem();
        if (pasajeroSeleccionado != null) {
            pasajeroSeleccionado.setNombre(txtNombre.getText());
            pasajeroSeleccionado.setDni(txtDni.getText());
            pasajeroSeleccionado.setEmail(txtEmail.getText());
            pasajeroSeleccionado.setTelefono(txtTelefono.getText());

            pasajeroDao.actualizarPasajero(pasajeroSeleccionado);
            cargarPasajeros();
            limpiarCampos();
        } else {
            mostrarAlerta("No se ha seleccionado ningún pasajero para modificar.");
        }
    }

    private void mostrarDetallesPasajero(PasajeroModel pasajero) {
        txtNombre.setText(pasajero.getNombre());
        txtDni.setText(pasajero.getDni());
        txtEmail.setText(pasajero.getEmail());
        txtTelefono.setText(pasajero.getTelefono());
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtDni.clear();
        txtEmail.clear();
        txtTelefono.clear();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
