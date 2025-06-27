package pe.edu.upeu.sisrestaurant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.sisrestaurant.modelo.Mesa;
import pe.edu.upeu.sisrestaurant.service.MesaService;

import java.util.Arrays;
import java.util.List;

@Controller
public class MesaFormController {

    @FXML
    private TextField txtNumero;
    @FXML
    private TextField txtCapacidad;
    @FXML
    private ComboBox<String> cbxEstado;

    @Autowired
    private MesaService mesaService;

    @FXML
    public void initialize() {
        cbxEstado.getItems().addAll(Arrays.asList("Disponible", "Ocupada", "Reservada", "Inactiva"));
    }

    @FXML
    public void guardarMesa() {
        try {
            if (txtNumero.getText().isEmpty() || txtCapacidad.getText().isEmpty() || cbxEstado.getValue() == null) {
                mostrarAlerta("Error", "Por favor complete todos los campos");
                return;
            }
            Mesa mesa = Mesa.builder()
                    .numero(Integer.parseInt(txtNumero.getText()))
                    .capacidad(Integer.parseInt(txtCapacidad.getText()))
                    .estado(cbxEstado.getValue())
                    .build();
            mesaService.save(mesa);
            mostrarAlerta("Éxito", "Mesa registrada correctamente");
            limpiarFormulario();
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al guardar mesa: " + e.getMessage());
        }
    }

    @FXML
    public void limpiarFormulario() {
        txtNumero.clear();
        txtCapacidad.clear();
        cbxEstado.setValue(null);
    }

    @FXML
    public void cancelar() {
        txtNumero.getScene().getWindow().hide();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
} 