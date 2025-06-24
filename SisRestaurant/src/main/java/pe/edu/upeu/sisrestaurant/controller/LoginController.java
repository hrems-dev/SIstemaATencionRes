package pe.edu.upeu.sisrestaurant.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import pe.edu.upeu.sisrestaurant.componente.StageManager;
import pe.edu.upeu.sisrestaurant.componente.Toast;
import pe.edu.upeu.sisrestaurant.dto.SessionManager;
import pe.edu.upeu.sisrestaurant.modelo.Usuario;
import pe.edu.upeu.sisrestaurant.service.UsuarioService;

@Controller
public class LoginController {
    @Autowired
    private ApplicationContext context;
    @Autowired
    UsuarioService us;
    @FXML
    TextField txtUsuario;
    @FXML
    PasswordField txtClave;
    @FXML
    Button btnIngresar;

    @FXML
    public void login(ActionEvent event) throws IOException {
        try {
            Usuario usu = us.loginUsuario(txtUsuario.getText(), txtClave.getText());
            if (usu != null) {
                System.out.println("Login exitoso, cargando menuAdm.fxml...");
                SessionManager.getInstance().setUserId(usu.getIduser());
                SessionManager.getInstance().setUserName(usu.getNombre());
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/menuAdm.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent mainRoot = loader.load();
                System.out.println("menuAdm.fxml cargado correctamente");
                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getBounds();
                Scene mainScene = new Scene(mainRoot, bounds.getWidth(), bounds.getHeight() - 30);
                // Si tienes un archivo de estilos, descomenta la siguiente línea:
                // mainScene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                // Si tienes un icono, descomenta la siguiente línea:
                // stage.getIcons().add(new Image(getClass().getResource("/img/store.png").toExternalForm()));
                stage.setScene(mainScene);
                stage.setTitle("Pantalla Principal");
                stage.setX(bounds.getMinX());
                stage.setY(bounds.getMinY());
                stage.setResizable(true);
                StageManager.setPrimaryStage(stage);
                stage.setWidth(bounds.getWidth());
                stage.setHeight(bounds.getHeight());
                stage.show();
            } else {
                System.out.println("Login fallido: credenciales inválidas");
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                double with = stage.getWidth() * 2;
                double h = stage.getHeight() / 2;
                Toast.showToast(stage, "Credencial invalido!! intente nuevamente", 2000, with, h);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar menuAdm.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 