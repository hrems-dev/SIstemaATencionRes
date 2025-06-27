package pe.edu.upeu.sisrestaurant;

import org.kordamp.bootstrapfx.BootstrapFX;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.ApplicationContext;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import pe.edu.upeu.sisrestaurant.service.UsuarioService;

@SpringBootApplication
public class SisRestaurantApplication extends Application {
	private static ConfigurableApplicationContext configurableApplicationContext;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() throws Exception {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(SisRestaurantApplication.class);
		builder.application().setWebApplicationType(WebApplicationType.NONE);
		configurableApplicationContext = builder.run(getParameters().getRaw().toArray(new String[0]));
	}

	@Override
	public void start(Stage stage) throws Exception {
		ApplicationContext ctx = getContext();
		UsuarioService usuarioService = ctx.getBean(UsuarioService.class);
		FXMLLoader fxmlLoader;
		String fxmlToLoad;
		boolean maximizar = false;
		if (usuarioService.findAll().isEmpty()) {
			// No hay usuarios, mostrar formulario de configuración
			fxmlToLoad = "/views/ConfiguracionForm.fxml";
		} else {
			// Hay usuarios, mostrar pantalla de login
			fxmlToLoad = "/views/login.fxml";
		}
		if (fxmlToLoad.equals("/views/principalFrm.fxml")) {
			maximizar = true;
		}
		fxmlLoader = new FXMLLoader(getClass().getResource(fxmlToLoad));
		fxmlLoader.setControllerFactory(ctx::getBean);
		Parent parent = fxmlLoader.load();
		Scene scene = new Scene(parent);
		scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
		stage.setScene(scene);
		stage.setTitle("Sistema de Restaurante");
		stage.setResizable(true);
		stage.setMaximized(maximizar);
		stage.show();
	}

	@Override
	public void stop() throws Exception {
		configurableApplicationContext.close();
		super.stop();
	}

	public static ConfigurableApplicationContext getContext() {
		return configurableApplicationContext;
	}
}
