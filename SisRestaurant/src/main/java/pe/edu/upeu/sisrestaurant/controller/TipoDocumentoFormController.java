package pe.edu.upeu.sisrestaurant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.sisrestaurant.modelo.TipoDocumento;
import pe.edu.upeu.sisrestaurant.service.TipoDocumentoService;

@Controller
public class TipoDocumentoFormController {

    @FXML
    private TextField nombreField;

    @Autowired
    private TipoDocumentoService tipoDocumentoService;

    @FXML
    public void guardarTipoDocumento() {
        try {
            if (nombreField.getText().isEmpty()) {
                mostrarAlerta("Error", "Por favor ingrese el nombre del tipo de documento");
                return;
            }

            TipoDocumento tipoDocumento = TipoDocumento.builder()
                    .nombre(nombreField.getText())
                    .build();

            tipoDocumentoService.save(tipoDocumento);
            mostrarAlerta("Éxito", "Tipo de documento registrado correctamente");
            limpiarFormulario();

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al guardar tipo de documento: " + e.getMessage());
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