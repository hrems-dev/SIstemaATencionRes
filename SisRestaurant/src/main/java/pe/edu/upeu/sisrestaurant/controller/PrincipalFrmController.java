package pe.edu.upeu.sisrestaurant.controller;

import org.springframework.stereotype.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import org.springframework.context.ApplicationContext;
import pe.edu.upeu.sisrestaurant.SisRestaurantApplication;
import javafx.scene.control.Label;
import pe.edu.upeu.sisrestaurant.config.ConstantesGlobales;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import pe.edu.upeu.sisrestaurant.service.TipoDocumentoService;
import pe.edu.upeu.sisrestaurant.modelo.TipoDocumento;
import pe.edu.upeu.sisrestaurant.service.PersonalService;
import pe.edu.upeu.sisrestaurant.modelo.Personal;
import pe.edu.upeu.sisrestaurant.dto.SessionManager;
import pe.edu.upeu.sisrestaurant.service.InfoPersonalService;
import pe.edu.upeu.sisrestaurant.modelo.InfoPersonal;
import pe.edu.upeu.sisrestaurant.controller.InicioController;

@Controller
public class PrincipalFrmController {
    @FXML
    private AnchorPane leftPane;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnInicio;
    @FXML
    private Button btnCrearMenu;
    @FXML
    private AnchorPane rightPane;
    @FXML
    private Label lblDocSeleccionado;
    @FXML
    private Button btnCambiarDoc;
    @FXML
    private Label lblUsuario;
    @FXML
    private Button btnAdministracion;
    @Autowired
    private TipoDocumentoService tipoDocumentoService;
    @Autowired
    private PersonalService personalService;
    @Autowired
    private InfoPersonalService infoPersonalService;
    private List<TipoDocumento> tiposDocumento;
    private int indiceTipoDoc = 0;

    private TabPane tabPane;

    @FXML
    public void initialize() {
        try {
            // Mostrar el nombre del personal en el label de usuario
            mostrarNombrePersonal();
            
            // Crear el TabPane dinámicamente
            tabPane = new TabPane();
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
            // Cargar el formulario de inicio en el primer tab
            ApplicationContext ctx = SisRestaurantApplication.getContext();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/inicio.fxml"));
            loader.setControllerFactory(ctx::getBean);
            Parent inicioContent = loader.load();
            Tab tabInicio = new Tab("Inicio", inicioContent);
            tabPane.getTabs().add(tabInicio);
            // Ajustar el TabPane al AnchorPane
            AnchorPane.setTopAnchor(tabPane, 0.0);
            AnchorPane.setBottomAnchor(tabPane, 0.0);
            AnchorPane.setLeftAnchor(tabPane, 0.0);
            AnchorPane.setRightAnchor(tabPane, 0.0);
            leftPane.getChildren().add(tabPane);

            // Cargar tipos de documento
            tiposDocumento = tipoDocumentoService.list();
            if (tiposDocumento != null && !tiposDocumento.isEmpty()) {
                indiceTipoDoc = 0;
                lblDocSeleccionado.setText(tiposDocumento.get(indiceTipoDoc).getNombre());
            }

            // Acción del botón para cambiar tipo de documento
            if (btnCambiarDoc != null) {
                btnCambiarDoc.setOnAction(e -> cambiarTipoDocumento());
            }

            // Acción del botón Nuevo
            btnNuevo.setOnAction(e -> abrirPedidoTab());
            // Acción del botón Inicio
            btnInicio.setOnAction(e -> seleccionarInicioTab());
            // Acción del botón Crear Nuevo Menu
            btnCrearMenu.setOnAction(e -> abrirProductoTab());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarNombrePersonal() {
        try {
            SessionManager sessionManager = SessionManager.getInstance();
            Long userId = sessionManager.getUserId();
            
            if (userId != null) {
                Personal personal = personalService.findByUsuarioId(userId);
                if (personal != null && personal.getDni() != null) {
                    // Obtener la información del personal usando el DNI
                    InfoPersonal infoPersonal = infoPersonalService.getInfoPersonalById(personal.getDni());
                    if (infoPersonal != null) {
                        String nombreCompleto = infoPersonal.getNombre() + " " + infoPersonal.getApellido();
                        lblUsuario.setText(nombreCompleto);
                        System.out.println("[DEBUG] Nombre del personal mostrado: " + nombreCompleto);
                    } else {
                        // Si no hay info personal, mostrar el nombre de usuario
                        String userName = sessionManager.getUserName();
                        if (userName != null && !userName.isEmpty()) {
                            lblUsuario.setText(userName);
                            System.out.println("[DEBUG] Nombre de usuario mostrado: " + userName);
                        } else {
                            lblUsuario.setText("Usuario");
                            System.out.println("[DEBUG] No se encontró información del usuario");
                        }
                    }
                } else {
                    // Si no es personal, mostrar el nombre de usuario
                    String userName = sessionManager.getUserName();
                    if (userName != null && !userName.isEmpty()) {
                        lblUsuario.setText(userName);
                        System.out.println("[DEBUG] Nombre de usuario mostrado: " + userName);
                    } else {
                        lblUsuario.setText("Usuario");
                        System.out.println("[DEBUG] No se encontró información del usuario");
                    }
                }
            } else {
                lblUsuario.setText("Usuario");
                System.out.println("[DEBUG] No hay usuario logueado");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Error al mostrar nombre del personal: " + e.getMessage());
            e.printStackTrace();
            lblUsuario.setText("Usuario");
        }
    }

    private void abrirPedidoTab() {
        try {
            System.out.println("[DEBUG] Intentando abrir pestaña de Pedido...");
            for (Tab tab : tabPane.getTabs()) {
                if ("Pedido".equals(tab.getText())) {
                    System.out.println("[DEBUG] La pestaña de Pedido ya existe. Seleccionando...");
                    tabPane.getSelectionModel().select(tab);
                    return;
                }
            }
            ApplicationContext ctx = SisRestaurantApplication.getContext();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PedidoForm.fxml"));
            loader.setControllerFactory(ctx::getBean);
            Parent pedidoContent = loader.load();
            System.out.println("[DEBUG] PedidoForm.fxml cargado correctamente");
            Object controller = loader.getController();
            if (controller instanceof pe.edu.upeu.sisrestaurant.controller.PedidoController pedidoController) {
                System.out.println("[DEBUG] Llamando a deshabilitarControlesPrincipales()...");
                pedidoController.deshabilitarControlesPrincipales();
            } else {
                System.out.println("[DEBUG] El controlador no es instancia de PedidoController: " + controller);
            }
            Tab tabPedido = new Tab("Pedido", pedidoContent);
            tabPane.getTabs().add(tabPedido);
            tabPane.getSelectionModel().select(tabPedido);
            System.out.println("[DEBUG] Pestaña de Pedido agregada y seleccionada");
        } catch (Exception e) {
            System.out.println("[ERROR] Error al abrir el formulario de pedido: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void abrirProductoTab() {
        try {
            for (Tab tab : tabPane.getTabs()) {
                if ("Producto".equals(tab.getText())) {
                    tabPane.getSelectionModel().select(tab);
                    return;
                }
            }
            ApplicationContext ctx = SisRestaurantApplication.getContext();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ProductoForm.fxml"));
            loader.setControllerFactory(ctx::getBean);
            Parent productoContent = loader.load();
            Tab tabProducto = new Tab("Producto", productoContent);
            tabPane.getTabs().add(tabProducto);
            tabPane.getSelectionModel().select(tabProducto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void seleccionarInicioTab() {
        for (Tab tab : tabPane.getTabs()) {
            if ("Inicio".equals(tab.getText())) {
                tabPane.getSelectionModel().select(tab);
                
                // Actualizar datos de la pantalla de inicio
                try {
                    // Obtener el controlador de inicio y actualizar datos
                    if (tab.getContent() != null) {
                        // Buscar el controlador de inicio en el contexto de Spring
                        ApplicationContext ctx = SisRestaurantApplication.getContext();
                        InicioController inicioController = ctx.getBean(InicioController.class);
                        if (inicioController != null) {
                            inicioController.actualizarDatosSilenciosamente();
                            System.out.println("Datos de inicio actualizados automáticamente");
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error al actualizar datos de inicio: " + e.getMessage());
                }
                
                return;
            }
        }
    }

    public void mostrarFormularioCliente() {
        try {
            ApplicationContext ctx = SisRestaurantApplication.getContext();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ClienteForm.fxml"));
            loader.setControllerFactory(ctx::getBean);
            Parent clienteContent = loader.load();
            rightPane.getChildren().clear();
            AnchorPane.setTopAnchor(clienteContent, 0.0);
            AnchorPane.setBottomAnchor(clienteContent, 0.0);
            AnchorPane.setLeftAnchor(clienteContent, 0.0);
            AnchorPane.setRightAnchor(clienteContent, 0.0);
            rightPane.getChildren().add(clienteContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abrirClienteTab() {
        try {
            // Verificar si ya existe la pestaña de Cliente
            for (Tab tab : tabPane.getTabs()) {
                if ("Cliente".equals(tab.getText())) {
                    tabPane.getSelectionModel().select(tab);
                    return;
                }
            }
            ApplicationContext ctx = SisRestaurantApplication.getContext();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ClienteForm.fxml"));
            loader.setControllerFactory(ctx::getBean);
            Parent clienteContent = loader.load();
            Tab tabCliente = new Tab("Cliente", clienteContent);
            tabPane.getTabs().add(tabCliente);
            tabPane.getSelectionModel().select(tabCliente);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abrirClienteEnPanelDerecho(String dni) {
        try {
            ApplicationContext ctx = SisRestaurantApplication.getContext();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ClienteForm.fxml"));
            loader.setControllerFactory(ctx::getBean);
            Parent clienteContent = loader.load();
            pe.edu.upeu.sisrestaurant.controller.ClienteController clienteController = loader.getController();
            clienteController.inicializarConDni(dni);
            rightPane.getChildren().clear();
            AnchorPane.setTopAnchor(clienteContent, 0.0);
            AnchorPane.setBottomAnchor(clienteContent, 0.0);
            AnchorPane.setLeftAnchor(clienteContent, 0.0);
            AnchorPane.setRightAnchor(clienteContent, 0.0);
            rightPane.getChildren().add(clienteContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abrirClienteEnPanelDerechoConDatos(pe.edu.upeu.sisrestaurant.modelo.Cliente cliente) {
        try {
            ApplicationContext ctx = SisRestaurantApplication.getContext();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ClienteForm.fxml"));
            loader.setControllerFactory(ctx::getBean);
            Parent clienteContent = loader.load();
            pe.edu.upeu.sisrestaurant.controller.ClienteController clienteController = loader.getController();
            clienteController.inicializarConClienteCompleto(cliente);
            rightPane.getChildren().clear();
            AnchorPane.setTopAnchor(clienteContent, 0.0);
            AnchorPane.setBottomAnchor(clienteContent, 0.0);
            AnchorPane.setLeftAnchor(clienteContent, 0.0);
            AnchorPane.setRightAnchor(clienteContent, 0.0);
            rightPane.getChildren().add(clienteContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abrirClienteEnPanelDerechoConDatosAPI(String dni, String nombres, String apellidoPaterno, String apellidoMaterno) {
        try {
            ApplicationContext ctx = SisRestaurantApplication.getContext();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ClienteForm.fxml"));
            loader.setControllerFactory(ctx::getBean);
            Parent clienteContent = loader.load();
            pe.edu.upeu.sisrestaurant.controller.ClienteController clienteController = loader.getController();
            clienteController.inicializarConDatosAPI(dni, nombres, apellidoPaterno, apellidoMaterno);
            rightPane.getChildren().clear();
            AnchorPane.setTopAnchor(clienteContent, 0.0);
            AnchorPane.setBottomAnchor(clienteContent, 0.0);
            AnchorPane.setLeftAnchor(clienteContent, 0.0);
            AnchorPane.setRightAnchor(clienteContent, 0.0);
            rightPane.getChildren().add(clienteContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizarDocSeleccionado(String doc) {
        if (lblDocSeleccionado != null) {
            lblDocSeleccionado.setText(doc);
        }
    }

    private void cambiarTipoDocumento() {
        if (tiposDocumento != null && !tiposDocumento.isEmpty()) {
            indiceTipoDoc = (indiceTipoDoc + 1) % tiposDocumento.size();
            lblDocSeleccionado.setText(tiposDocumento.get(indiceTipoDoc).getNombre());
            
            // Actualizar la validación en el formulario de pedido si está abierto
            actualizarValidacionEnPedido();
        }
    }

    private void actualizarValidacionEnPedido() {
        try {
            // Buscar si hay una pestaña de pedido abierta
            for (Tab tab : tabPane.getTabs()) {
                if ("Pedido".equals(tab.getText())) {
                    // Obtener el controlador de pedido y actualizar la validación
                    org.springframework.context.ApplicationContext ctx = SisRestaurantApplication.getContext();
                    pe.edu.upeu.sisrestaurant.controller.PedidoController pedidoController = ctx.getBean(pe.edu.upeu.sisrestaurant.controller.PedidoController.class);
                    pedidoController.actualizarValidacionNumeroDocumento();
                    break;
                }
            }
            
            // Actualizar también la validación en el panel derecho si hay un formulario de cliente
            if (rightPane.getChildren().size() > 0) {
                org.springframework.context.ApplicationContext ctx = SisRestaurantApplication.getContext();
                pe.edu.upeu.sisrestaurant.controller.ClienteController clienteController = ctx.getBean(pe.edu.upeu.sisrestaurant.controller.ClienteController.class);
                clienteController.actualizarValidacionNumeroIdentidad();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDocSeleccionado() {
        return lblDocSeleccionado != null ? lblDocSeleccionado.getText() : "";
    }

    public void cerrarPanelDerecho() {
        if (rightPane != null) {
            rightPane.getChildren().clear();
        }
    }
    
    public javafx.scene.layout.AnchorPane getRightPane() {
        return rightPane;
    }

    @FXML
    private void onAdministracion() {
        try {
            // Cerrar la ventana principal
            javafx.stage.Stage stage = (javafx.stage.Stage) btnAdministracion.getScene().getWindow();
            stage.close();
            // Abrir el login con contexto de Spring
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/views/login.fxml"));
            loader.setControllerFactory(pe.edu.upeu.sisrestaurant.SisRestaurantApplication.getContext()::getBean);
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage loginStage = new javafx.stage.Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new javafx.scene.Scene(root));
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 