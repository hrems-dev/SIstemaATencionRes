package pe.edu.upeu.sisrestaurant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.sisrestaurant.modelo.TipoDocVenta;
import pe.edu.upeu.sisrestaurant.service.TipoDocVentaService;

import java.util.Arrays;

@Controller
public class TipoDocVentaFormController {

    @FXML
    private TextField txtNombre;
    @FXML
    private ComboBox<String> cbxEstado;

    @Autowired
    private TipoDocVentaService tipoDocVentaService;

    @FXML
    public void initialize() {
        cbxEstado.getItems().addAll(Arrays.asList("Activo", "Inactivo"));
    }

    @FXML
    public void guardarTipoDocVenta() {
        try {
            if (txtNombre.getText().isEmpty() || cbxEstado.getValue() == null) {
                mostrarAlerta("Error", "Por favor complete todos los campos");
                return;
            }
            TipoDocVenta tipoDocVenta = TipoDocVenta.builder()
                    .nombre(txtNombre.getText())
                    .estado(cbxEstado.getValue())
                    .build();
            tipoDocVentaService.save(tipoDocVenta);
            mostrarAlerta("Éxito", "Tipo de documento de venta registrado correctamente");
            limpiarFormulario();
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al guardar: " + e.getMessage());
        }
    }

    @FXML
    public void limpiarFormulario() {
        txtNombre.clear();
        cbxEstado.setValue(null);
    }

    @FXML
    public void cancelar() {
        txtNombre.getScene().getWindow().hide();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
} 