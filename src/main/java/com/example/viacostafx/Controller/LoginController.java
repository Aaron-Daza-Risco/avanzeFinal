package com.example.viacostafx.Controller;

import com.example.viacostafx.Modelo.EmpleadosModel;
import com.example.viacostafx.dao.EmpleadosDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private EmpleadosDao empleadosDao;
    public LoginController() {
        this.empleadosDao = new EmpleadosDao();
    }

    @FXML
    private TextField txtUsername;
    @FXML
    private Button btnLogin;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label txtLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnLogin.setOnAction(event -> iniciarSesion(txtUsername.getText(), txtPassword.getText()));
    }


    private void iniciarSesion(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            mostrarAlerta("Por favor, completa ambos campos de usuario y contraseña.");
            return;
        }

        // Llamada al método de autenticación en el Modelo
        EmpleadosModel empleado = EmpleadosModel.autenticarUsuario(username, password);
        if (empleado != null) {
            cargarInterfazPrincipal();
        } else {
            mostrarAlerta("Usuario o contraseña incorrectos. Por favor, verifica tus credenciales.");
        }
    }


    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error al comprobar credenciales");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        // Centrar la alerta en la ventana principal
        alert.initOwner((Stage) txtLabel.getScene().getWindow()); // Establece la ventana principal como dueño
        alert.initModality(Modality.WINDOW_MODAL); // Hace que la alerta sea modal respecto a la ventana principal

        // Centrar la alerta en la pantalla principal
        alert.setOnShown(event -> {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Stage ownerStage = (Stage) alert.getOwner();
            if (ownerStage != null) {
                stage.setX(ownerStage.getX() + (ownerStage.getWidth() - stage.getWidth()) / 2);
                stage.setY(ownerStage.getY() + (ownerStage.getHeight() - stage.getHeight()) / 2);
            }
        });

        alert.showAndWait();
    }

    private void cargarInterfazPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/InterfazPrincipal.fxml"));
            Parent mainInterfaceRoot = loader.load();

            // Obtener la ventana actual desde el contexto de la escena
            Stage stage = (Stage) txtLabel.getScene().getWindow();
            Scene scene = new Scene(mainInterfaceRoot);

            // Configura la escena en el stage
            stage.setScene(scene);
            stage.setMaximized(true);
            // Muestra el stage para obtener sus dimensiones
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
            txtLabel.setText("Error al cargar la interfaz principal");
        }
    }
}