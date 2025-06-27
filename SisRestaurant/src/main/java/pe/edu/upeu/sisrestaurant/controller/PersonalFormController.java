package pe.edu.upeu.sisrestaurant.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.sisrestaurant.modelo.InfoPersonal;
import pe.edu.upeu.sisrestaurant.modelo.Personal;
import pe.edu.upeu.sisrestaurant.modelo.Rol;
import pe.edu.upeu.sisrestaurant.modelo.Usuario;
import pe.edu.upeu.sisrestaurant.service.InfoPersonalService;
import pe.edu.upeu.sisrestaurant.service.PersonalService;
import pe.edu.upeu.sisrestaurant.service.RolService;
import pe.edu.upeu.sisrestaurant.service.UsuarioService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class PersonalFormController {

    @FXML
    private TextField txtDni;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtDireccion;
    @FXML
    private ComboBox<Rol> cbxRol;
    @FXML
    private Label lblUsuarioSeleccionado;

    @Autowired
    private PersonalService personalService;
    @Autowired
    private InfoPersonalService infoPersonalService;
    @Autowired
    private RolService rolService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ApplicationContext context;

    private Usuario usuarioSeleccionado;

    @FXML
    public void initialize() {
        configurarComboBoxRol();
        cargarRoles();
    }

    private void configurarComboBoxRol() {
        cbxRol.setConverter(new StringConverter<Rol>() {
            @Override
            public String toString(Rol rol) {
                return rol != null ? rol.getDescripcion() : "";
            }

            @Override
            public Rol fromString(String string) {
                return cbxRol.getItems().stream()
                        .filter(rol -> rol.getDescripcion().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    private void cargarRoles() {
        try {
            List<Rol> roles = rolService.list();
            cbxRol.getItems().clear();
            cbxRol.getItems().addAll(roles);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al cargar roles: " + e.getMessage());
        }
    }

    @FXML
    public void onNuevoRol() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/RolForm.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent rolContent = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Nuevo Rol");
            stage.setScene(new Scene(rolContent));
            stage.show();
            
            // Recargar roles después de cerrar la ventana
            stage.setOnHidden(e -> cargarRoles());
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al abrir formulario de rol: " + e.getMessage());
        }
    }

    @FXML
    public void onNuevoUsuario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UsuarioForm.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent usuarioContent = loader.load();
            
            // Obtener el controlador para configurar el tipo de usuario
            UsuarioFormController usuarioController = loader.getController();
            usuarioController.setTipoUsuarioNormal();
            
            Stage stage = new Stage();
            stage.setTitle("Nuevo Usuario");
            stage.setScene(new Scene(usuarioContent));
            stage.show();
            
            // Configurar callback para cuando se cree el usuario
            stage.setOnHidden(e -> {
                if (usuarioController.getUsuarioCreado() != null) {
                    usuarioSeleccionado = usuarioController.getUsuarioCreado();
                    lblUsuarioSeleccionado.setText(usuarioSeleccionado.getNombre());
                }
            });
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al abrir formulario de usuario: " + e.getMessage());
        }
    }

    @FXML
    public void guardarPersonal() {
        try {
            // Validar campos obligatorios
            if (txtDni.getText().isEmpty() || txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty()) {
                mostrarAlerta("Error", "Por favor complete los campos obligatorios (DNI, Nombre, Apellido)");
                return;
            }
            // Validar formato de DNI
            String dni = txtDni.getText().trim();
            if (!dni.matches("\\d{8}")) {
                mostrarAlerta("Error", "El DNI debe tener exactamente 8 dígitos numéricos.");
                return;
            }
            // Validar unicidad de DNI en InfoPersonal
            if (infoPersonalService.getInfoPersonalById(dni) != null) {
                mostrarAlerta("Error", "Ya existe una persona registrada con ese DNI.");
                return;
            }
            if (cbxRol.getValue() == null) {
                mostrarAlerta("Error", "Por favor seleccione un rol");
                return;
            }
            if (usuarioSeleccionado == null) {
                mostrarAlerta("Error", "Por favor cree un usuario para el personal");
                return;
            }
            // Crear InfoPersonal
            InfoPersonal infoPersonal = InfoPersonal.builder()
                    .dni(dni)
                    .nombre(txtNombre.getText())
                    .apellido(txtApellido.getText())
                    .telefono(txtTelefono.getText())
                    .correo(txtCorreo.getText())
                    .direccion(txtDireccion.getText())
                    .fechaRegistro(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .build();
            // Guardar InfoPersonal
            infoPersonal = infoPersonalService.save(infoPersonal);
            // Crear Personal
            Personal personal = Personal.builder()
                    .idPersonal(generarIdPersonal())
                    .rol(cbxRol.getValue())
                    .usuario(usuarioSeleccionado)
                    .infoPersonal(infoPersonal)
                    .build();
            // Guardar Personal
            personalService.save(personal);
            mostrarAlerta("Éxito", "Personal registrado correctamente");
            limpiarFormulario();
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al guardar personal: " + e.getMessage());
        }
    }

    private Long generarIdPersonal() {
        try {
            List<Personal> personalList = personalService.list();
            if (personalList.isEmpty()) {
                return 1L;
            }
            return personalList.stream()
                    .mapToLong(Personal::getIdPersonal)
                    .max()
                    .orElse(0L) + 1L;
        } catch (Exception e) {
            return 1L;
        }
    }

    @FXML
    public void limpiarFormulario() {
        txtDni.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        txtDireccion.clear();
        cbxRol.setValue(null);
        lblUsuarioSeleccionado.setText("No seleccionado");
        usuarioSeleccionado = null;
    }

    @FXML
    public void cancelar() {
        // Cerrar la ventana del formulario
        txtDni.getScene().getWindow().hide();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
} 