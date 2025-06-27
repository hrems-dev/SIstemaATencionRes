package pe.edu.upeu.sisrestaurant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.sisrestaurant.modelo.Seccion;
import pe.edu.upeu.sisrestaurant.service.SeccionService;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;

@Controller
public class SeccionFormController {
    @FXML private TextField nombreField;
    @FXML private Button guardarButton;
    @FXML private Button limpiarButton;
    @FXML private Button cancelarButton;
    @FXML private TableView<Seccion> tblSecciones;
    @FXML private TableColumn<Seccion, String> colNombre;
    @FXML private TableColumn<Seccion, String> colEstado;

    @Autowired
    private SeccionService seccionService;

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        actualizarTabla();
    }

    private void actualizarTabla() {
        tblSecciones.setItems(FXCollections.observableArrayList(seccionService.list()));
    }

    @FXML
    public void guardarSeccion() {
        String nombre = nombreField.getText();
        if (nombre == null || nombre.trim().isEmpty()) {
            mostrarAlerta("Error", "El nombre de la sección es obligatorio.");
            return;
        }
        Seccion seccion = new Seccion();
        seccion.setNombre(nombre.trim());
        seccion.setEstado("A");
        seccionService.save(seccion);
        mostrarAlerta("Éxito", "Sección guardada correctamente.");
        limpiarFormulario();
        actualizarTabla();
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