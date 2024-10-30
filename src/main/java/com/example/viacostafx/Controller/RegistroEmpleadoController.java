package com.example.viacostafx.Controller;
import com.example.viacostafx.Modelo.EmpleadosModel;
import com.example.viacostafx.dao.EmpleadosDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.security.SecureRandom;
import java.util.List;

public class RegistroEmpleadoController {
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtDNI;
    @FXML private TextField txtTelefono;
    @FXML private TableView<EmpleadosModel> tablaEmpleados;
    @FXML private TableColumn<EmpleadosModel, String> colNombre;
    @FXML private TableColumn<EmpleadosModel, String> colApellido;
    @FXML private TableColumn<EmpleadosModel, Integer> colDNI;
    @FXML private TableColumn<EmpleadosModel, Integer> colTelefono;
    @FXML private TableColumn<EmpleadosModel, String> colUsuario;
    @FXML private TableColumn<EmpleadosModel, String> colContrasenia;
    @FXML private TableColumn<EmpleadosModel, Void> colAcciones;

    private EmpleadosDao empleadosDao;
    private ObservableList<EmpleadosModel> listaEmpleados;

    @FXML
    public void initialize() {
        empleadosDao = new EmpleadosDao();
        listaEmpleados = FXCollections.observableArrayList();

        // Configurar columnas
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colDNI.setCellValueFactory(new PropertyValueFactory<>("DNI"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colContrasenia.setCellValueFactory(new PropertyValueFactory<>("contrasenia"));

        // Agregar columna de acciones
        agregarBotonesAccion();

        // Cargar datos
        cargarEmpleados();
    }

    private void agregarBotonesAccion() {
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox botones = new HBox(5, btnEditar, btnEliminar);

            {
                btnEditar.setOnAction(event -> {
                    EmpleadosModel empleado = getTableView().getItems().get(getIndex());
                    editarEmpleado(empleado);
                });

                btnEliminar.setOnAction(event -> {
                    EmpleadosModel empleado = getTableView().getItems().get(getIndex());
                    eliminarEmpleado(empleado);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : botones);
            }
        });
    }

    @FXML
    private void handleAgregar() {
        try {
            EmpleadosModel empleado = new EmpleadosModel();
            empleado.setNombre(txtNombre.getText());
            empleado.setApellido(txtApellido.getText());
            empleado.setDNI(Integer.parseInt(txtDNI.getText()));
            empleado.setTelefono(Integer.parseInt(txtTelefono.getText()));

            // Generar usuario y contraseña
            String usuario = generarUsuario(empleado.getNombre(), empleado.getApellido());
            String contrasenia = generarContrasenia();

            empleado.setUsuario(usuario);
            empleado.setContrasenia(contrasenia);

            if (empleadosDao.crearEmpleado(empleado)) {
                mostrarAlerta("Éxito", "Empleado agregado correctamente\nUsuario: " + usuario + "\nContraseña: " + contrasenia, Alert.AlertType.INFORMATION);
                limpiarCampos();
                cargarEmpleados();
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "DNI y teléfono deben ser números", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al agregar empleado: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private String generarUsuario(String nombre, String apellido) {
        // Generar usuario con primera letra del nombre + apellido en minúsculas
        String usuario = (nombre.charAt(0) + apellido).toLowerCase()
                .replaceAll("[áéíóúñ]", "a")
                .replaceAll("\\s+", "");

        // Verificar si ya existe y agregar número si es necesario
        int contador = 1;
        String usuarioBase = usuario;
        while (empleadosDao.obtenerEmpleadoPorUsername(usuario) != null) {
            usuario = usuarioBase + contador++;
        }
        return usuario;
    }

    private String generarContrasenia() {
        // Generar contraseña segura de 8 caracteres
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        SecureRandom random = new SecureRandom();
        StringBuilder contrasenia = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            contrasenia.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }

        return contrasenia.toString();
    }

    private void editarEmpleado(EmpleadosModel empleado) {
        txtNombre.setText(empleado.getNombre());
        txtApellido.setText(empleado.getApellido());
        txtDNI.setText(String.valueOf(empleado.getDNI()));
        txtTelefono.setText(String.valueOf(empleado.getTelefono()));

        // Crear botón de guardar cambios
        Button btnGuardar = new Button("Guardar Cambios");
        btnGuardar.setOnAction(e -> {
            empleado.setNombre(txtNombre.getText());
            empleado.setApellido(txtApellido.getText());
            empleado.setDNI(Integer.parseInt(txtDNI.getText()));
            empleado.setTelefono(Integer.parseInt(txtTelefono.getText()));

            if (empleadosDao.actualizarEmpleado(empleado)) {
                mostrarAlerta("Éxito", "Empleado actualizado correctamente", Alert.AlertType.INFORMATION);
                limpiarCampos();
                cargarEmpleados();
            }
        });
    }

    private void eliminarEmpleado(EmpleadosModel empleado) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Está seguro de eliminar este empleado?");
        alert.setContentText("Esta acción no se puede deshacer");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (empleadosDao.eliminarEmpleado(empleado.getId())) {
                    mostrarAlerta("Éxito", "Empleado eliminado correctamente", Alert.AlertType.INFORMATION);
                    cargarEmpleados();
                }
            }
        });
    }


    private void cargarEmpleados() {
        List<EmpleadosModel> empleados = empleadosDao.obtenerTodosEmpleados();
        listaEmpleados.clear();
        listaEmpleados.addAll(empleados);
        tablaEmpleados.setItems(listaEmpleados);
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtApellido.clear();
        txtDNI.clear();
        txtTelefono.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
