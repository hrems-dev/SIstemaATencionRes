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
    @Autowired
    private TipoDocumentoService tipoDocumentoService;
    private List<TipoDocumento> tiposDocumento;
    private int indiceTipoDoc = 0;

    private TabPane tabPane;

    @FXML
    public void initialize() {
        try {
            // Crear el TabPane dinámicamente
            tabPane = new TabPane();
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
            // Cargar el formulario de inicio en el primer tab
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/inicio.fxml"));
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
} 