package pe.edu.upeu.sisrestaurant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.sisrestaurant.modelo.Usuario;
import pe.edu.upeu.sisrestaurant.service.UsuarioService;

import java.util.List;

@Controller
public class UsuarioFormController {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtClave;
    @FXML
    private TextField txtCorreo;
    @FXML
    private Label lblTipoUsuario;

    @Autowired
    private UsuarioService usuarioService;

    private String tipoUsuario = "normal";
    private Usuario usuarioCreado = null;

    @FXML
    public void initialize() {
        // Configuración inicial
    }

    public void setTipoUsuarioNormal() {
        this.tipoUsuario = "normal";
        lblTipoUsuario.setText("Normal");
    }

    public void setTipoUsuarioRoot() {
        this.tipoUsuario = "root";
        lblTipoUsuario.setText("Root");
    }

    @FXML
    public void guardarUsuario() {
        try {
            // Validar campos obligatorios
            if (txtUsuario.getText().isEmpty() || txtClave.getText().isEmpty()) {
                mostrarAlerta("Error", "Por favor complete los campos obligatorios (Usuario y Contraseña)");
                return;
            }

            // Verificar si el usuario ya existe
            if (usuarioExiste(txtUsuario.getText())) {
                mostrarAlerta("Error", "El nombre de usuario ya existe");
                return;
            }

            // Crear usuario
            Usuario usuario = Usuario.builder()
                    .iduser(generarIdUsuario())
                    .nombre(txtUsuario.getText())
                    .password(txtClave.getText())
                    .estado(true)
                    .tipoUsuario(tipoUsuario)
                    .build();

            // Guardar usuario
            usuarioCreado = usuarioService.save(usuario);
            mostrarAlerta("Éxito", "Usuario registrado correctamente");
            
            // Cerrar la ventana después de guardar
            txtUsuario.getScene().getWindow().hide();

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al guardar usuario: " + e.getMessage());
        }
    }

    private boolean usuarioExiste(String nombreUsuario) {
        try {
            List<Usuario> usuarios = usuarioService.findAll();
            return usuarios.stream()
                    .anyMatch(u -> u.getNombre().equals(nombreUsuario));
        } catch (Exception e) {
            return false;
        }
    }

    private Long generarIdUsuario() {
        try {
            List<Usuario> usuarios = usuarioService.findAll();
            if (usuarios.isEmpty()) {
                return 1L;
            }
            return usuarios.stream()
                    .mapToLong(Usuario::getIduser)
                    .max()
                    .orElse(0L) + 1L;
        } catch (Exception e) {
            return 1L;
        }
    }

    @FXML
    public void limpiarFormulario() {
        txtUsuario.clear();
        txtClave.clear();
        txtCorreo.clear();
    }

    @FXML
    public void cancelar() {
        // Cerrar la ventana del formulario
        txtUsuario.getScene().getWindow().hide();
    }

    public Usuario getUsuarioCreado() {
        return usuarioCreado;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
} 