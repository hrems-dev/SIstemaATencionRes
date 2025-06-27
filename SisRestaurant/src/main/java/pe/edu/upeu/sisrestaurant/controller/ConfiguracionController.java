package pe.edu.upeu.sisrestaurant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.sisrestaurant.modelo.Configuracion;
import pe.edu.upeu.sisrestaurant.modelo.Usuario;
import pe.edu.upeu.sisrestaurant.service.ConfiguracionService;
import pe.edu.upeu.sisrestaurant.service.UsuarioService;
import javafx.util.Pair;

@Controller
public class ConfiguracionController {
    @Autowired
    private ConfiguracionService configuracionService;
    @Autowired
    private UsuarioService usuarioService;

    @FXML private TextField txtNombreRestaurante;
    @FXML private TextField txtRuc;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtPropietario;
    @FXML private TextField txtLogoUrl;
    @FXML private TextField txtCodLicencia;
    @FXML private Button btnGuardar;
    @FXML private Button btnAgregarRoot;
    @FXML private Label lblUsuarioRoot;

    private Usuario usuarioRoot;

    @FXML
    public void initialize() {
        usuarioRoot = null;
        lblUsuarioRoot.setText("");
        btnAgregarRoot.setDisable(false);
    }

    @FXML
    private void onGuardar() {
        String codLicencia = txtCodLicencia.getText();
        if (!"cod5555".equals(codLicencia)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Licencia inválida");
            alert.setHeaderText(null);
            alert.setContentText("El código de licencia ingresado no es válido.");
            alert.showAndWait();
            return;
        }
        // Validar campos obligatorios
        if (txtNombreRestaurante.getText().isEmpty() || txtRuc.getText().isEmpty() || usuarioRoot == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campos obligatorios");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, complete los campos obligatorios y registre un usuario root.");
            alert.showAndWait();
            return;
        }
        // Asignar un idConfig único usando el máximo + 1
        long nextConfigId = configuracionService.list().stream()
            .mapToLong(c -> c.getIdConfig() != null ? c.getIdConfig() : 0L)
            .max()
            .orElse(0L) + 1;
        Configuracion config = Configuracion.builder()
                .idConfig(nextConfigId)
                .nombreRestaurante(txtNombreRestaurante.getText())
                .ruc(txtRuc.getText())
                .direccion(txtDireccion.getText())
                .telefono(txtTelefono.getText())
                .correo(txtCorreo.getText())
                .propietario(txtPropietario.getText())
                .logoUrl(txtLogoUrl.getText())
                .codLicencia(codLicencia)
                .usuario(usuarioRoot)
                .build();
        configuracionService.save(config);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Configuración guardada");
        alert.setHeaderText(null);
        alert.setContentText("La configuración se guardó correctamente. Ahora puede iniciar sesión.");
        alert.showAndWait();
        // Cerrar ventana y mostrar login
        Stage stage = (Stage) btnGuardar.getScene().getWindow();
        stage.close();
        // Abrir login
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/views/login.fxml"));
            org.springframework.context.ApplicationContext ctx = pe.edu.upeu.sisrestaurant.SisRestaurantApplication.getContext();
            loader.setControllerFactory(ctx::getBean);
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage loginStage = new javafx.stage.Stage();
            loginStage.setScene(new javafx.scene.Scene(root));
            loginStage.setTitle("Sistema de Restaurante - Login");
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAgregarRoot() {
        // Crear un diálogo personalizado con dos campos
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Agregar Usuario Root");
        dialog.setHeaderText("Ingrese los datos del usuario root:");
        ButtonType okButtonType = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Usuario root");
        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Contraseña");

        grid.add(new Label("Usuario:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("Contraseña:"), 0, 1);
        grid.add(txtPass, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new javafx.util.Pair<>(txtNombre.getText(), txtPass.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            String nombre = result.getKey();
            String pass = result.getValue();
            if (nombre.trim().isEmpty() || pass.trim().isEmpty()) return;
            // Asignar un iduser único usando el máximo + 1
            long nextId = usuarioService.findAll().stream()
                .mapToLong(u -> u.getIduser() != null ? u.getIduser() : 0L)
                .max()
                .orElse(0L) + 1;
            usuarioRoot = pe.edu.upeu.sisrestaurant.modelo.Usuario.builder()
                .iduser(nextId)
                .nombre(nombre)
                .password(pass)
                .estado(true)
                .tipoUsuario("root")
                .build();
            usuarioRoot = usuarioService.save(usuarioRoot);
            lblUsuarioRoot.setText(usuarioRoot.getNombre());
            btnAgregarRoot.setDisable(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Usuario Root creado");
            alert.setHeaderText(null);
            alert.setContentText("Usuario root agregado correctamente.");
            alert.showAndWait();
        });
    }
} 