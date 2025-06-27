package pe.edu.upeu.sisrestaurant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.sisrestaurant.modelo.Categoria;
import pe.edu.upeu.sisrestaurant.service.CategoriaService;

import java.util.List;

@Controller
public class CategoriaFormController {

    @FXML
    private TextField nombreField;

    @Autowired
    private CategoriaService categoriaService;

    @FXML
    public void guardarCategoria() {
        try {
            if (nombreField.getText().isEmpty()) {
                mostrarAlerta("Error", "Por favor ingrese el nombre de la categoría");
                return;
            }

            Categoria categoria = Categoria.builder()
                    .nombre(nombreField.getText())
                    .estado("Activo")
                    .build();

            categoriaService.save(categoria);
            mostrarAlerta("Éxito", "Categoría registrada correctamente");
            limpiarFormulario();

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al guardar categoría: " + e.getMessage());
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