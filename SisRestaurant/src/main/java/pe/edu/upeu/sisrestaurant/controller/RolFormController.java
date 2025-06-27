package pe.edu.upeu.sisrestaurant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.sisrestaurant.modelo.Rol;
import pe.edu.upeu.sisrestaurant.service.RolService;

@Controller
public class RolFormController {

    @FXML
    private TextField nombreField;

    @Autowired
    private RolService rolService;

    @FXML
    public void guardarRol() {
        try {
            if (nombreField.getText().isEmpty()) {
                mostrarAlerta("Error", "Por favor ingrese la descripción del rol");
                return;
            }

            Rol rol = Rol.builder()
                    .descripcion(nombreField.getText())
                    .estado(true)
                    .build();

            rolService.save(rol);
            mostrarAlerta("Éxito", "Rol registrado correctamente");
            limpiarFormulario();

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al guardar rol: " + e.getMessage());
        }
    }

    @FXML
    public void limpiarFormulario() {
        nombreField.clear();
    }

    @FXML
    public void cancelar() {
        // Cerrar la ventana del formulario
        nombreField.getScene().getWindow().hide();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
} 