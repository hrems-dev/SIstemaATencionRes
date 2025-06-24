package pe.edu.upeu.sisrestaurant.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pe.edu.upeu.sisrestaurant.dto.MenuMenuItenTO;
import pe.edu.upeu.sisrestaurant.service.MenuMenuItenDaoI;
import javafx.scene.control.Button;
// import pe.edu.upeu.sisrestaurant.util.UtilsX; // No existe, dejar como comentario

@Controller
public class GUIMainFX {
    @Autowired
    private ApplicationContext context;
    Preferences userPrefs = Preferences.userRoot();
    // UtilsX util = new UtilsX(); // No existe, dejar como comentario
    @Autowired
    private MenuMenuItenDaoI menuMenuItemDao;
    Properties myresources = new Properties();
    // MenuMenuItenDaoI mmiDao; // No existe, dejar como comentario
    @FXML
    private TabPane tabPaneFx;
    List<MenuMenuItenTO> lista;
    @FXML
    private BorderPane bp;
    @FXML
    private MenuBar menuBarFx;
    private Parent parent;
    Stage stage;
    @FXML
    private Button btnNuevo;

    public static class MenuItemDef {
        public String menu;
        public String item;
        public String action;
        public MenuItemDef(String menu, String item, String action) {
            this.menu = menu;
            this.item = item;
            this.action = action;
        }
    }

    @FXML
    public void initialize() {
        menuBarFx.getMenus().clear();
        // Menú Inicio
        Menu menuInicio = new Menu("Inicio");
        MenuItem itemInicio = new MenuItem("Inicio");
        itemInicio.setOnAction(e -> abrirVistaEnTab("Inicio", "/views/inicio.fxml"));
        menuInicio.getItems().add(itemInicio);
        menuBarFx.getMenus().add(menuInicio);

        // Menú Nuevo Pedido
        Menu menuNuevo = new Menu("Nuevo");
        MenuItem itemNuevoPedido = new MenuItem("Nuevo Pedido");
        itemNuevoPedido.setOnAction(e -> abrirVistaEnTab("Nuevo Pedido", "/views/PedidoForm.fxml"));
        menuNuevo.getItems().add(itemNuevoPedido);
        menuBarFx.getMenus().add(menuNuevo);

        // Menú Pedidos
        Menu menuPedidos = new Menu("Pedidos");
        MenuItem itemPedidos = new MenuItem("Pedidos");
        itemPedidos.setOnAction(e -> abrirVistaEnTab("Pedidos", "/views/PedidoForm.fxml"));
        menuPedidos.getItems().add(itemPedidos);
        menuBarFx.getMenus().add(menuPedidos);

        // Menú Productos
        Menu menuProductos = new Menu("Productos");
        MenuItem itemVerProductos = new MenuItem("Ver Productos");
        itemVerProductos.setOnAction(e -> abrirVistaEnTab("Ver Productos", "/views/ProductoForm.fxml"));
        MenuItem itemAgregarProducto = new MenuItem("Agregar Producto");
        itemAgregarProducto.setOnAction(e -> abrirVistaEnTab("Agregar Producto", "/views/ProductoForm.fxml"));
        menuProductos.getItems().addAll(itemVerProductos, itemAgregarProducto);
        menuBarFx.getMenus().add(menuProductos);

        // Menú Clientes
        Menu menuClientes = new Menu("Clientes");
        MenuItem itemClientes = new MenuItem("Clientes");
        itemClientes.setOnAction(e -> abrirVistaEnTab("Clientes", "/views/ClienteForm.fxml"));
        menuClientes.getItems().add(itemClientes);
        menuBarFx.getMenus().add(menuClientes);

        // Menú Personal
        Menu menuPersonal = new Menu("Personal");
        MenuItem itemPersonal = new MenuItem("Personal");
        itemPersonal.setOnAction(e -> abrirVistaEnTab("Personal", "/views/PersonalForm.fxml"));
        menuPersonal.getItems().add(itemPersonal);
        menuBarFx.getMenus().add(menuPersonal);

        // Menú Mesas
        Menu menuMesas = new Menu("Mesas");
        MenuItem itemMesas = new MenuItem("Mesas");
        itemMesas.setOnAction(e -> abrirVistaEnTab("Mesas", "/views/MesaForm.fxml"));
        menuMesas.getItems().add(itemMesas);
        menuBarFx.getMenus().add(menuMesas);

        // Menú Acerca de
        Menu menuAcercaDe = new Menu("Acerca de");
        MenuItem itemInformacion = new MenuItem("Información");
        itemInformacion.setOnAction(e -> mostrarInformacion());
        MenuItem itemCerrarSesion = new MenuItem("Cerrar Sesión");
        itemCerrarSesion.setOnAction(e -> cerrarSesion(e));
        MenuItem itemSalir = new MenuItem("Salir");
        itemSalir.setOnAction(e -> salir(e));
        menuAcercaDe.getItems().addAll(itemInformacion, itemCerrarSesion, itemSalir);
        menuBarFx.getMenus().add(menuAcercaDe);

        // Abrir por defecto la pestaña de inicio
        abrirVistaEnTab("Inicio", "/views/inicio.fxml");

        // Aplicar el estilo CSS elegante
        if (tabPaneFx.getScene() != null) {
            tabPaneFx.getScene().getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        } else {
            Platform.runLater(() -> {
                if (tabPaneFx.getScene() != null) {
                    tabPaneFx.getScene().getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
                }
            });
        }

        if (btnNuevo != null) {
            btnNuevo.setOnAction(e -> abrirVistaEnTab("Nuevo Pedido", "/views/PedidoForm.fxml"));
        }
    }

    public int[] contarMenuMunuItem(List<MenuMenuItenTO> data) {
        int menui = 0, menuitem = 0;
        String menuN = "";
        for (MenuMenuItenTO mmi : data) {
            if (!mmi.menunombre.equals(menuN)) {
                menuN = mmi.menunombre;
                menui++;
            }
            if (!mmi.menuitemnombre.equals("")) {
                menuitem++;
            }
        }
        return new int[]{menui, menuitem};
    }

    class MenuItemListener {
        public void handle(javafx.event.ActionEvent e) {
            MenuItem item = (MenuItem) e.getSource();
            String action = (String) item.getUserData();
            if (action == null) return;
            switch (action) {
                case "inicio.fxml":
                    abrirVistaEnTab("Inicio", "/views/inicio.fxml");
                    break;
                case "PedidoForm.fxml":
                    abrirVistaEnTab("Pedido", "/views/PedidoForm.fxml");
                    break;
                case "ProductoForm.fxml":
                    abrirVistaEnTab("Producto", "/views/ProductoForm.fxml");
                    break;
                case "ClienteForm.fxml":
                    abrirVistaEnTab("Clientes", "/views/ClienteForm.fxml");
                    break;
                case "PersonalForm.fxml":
                    abrirVistaEnTab("Personal", "/views/PersonalForm.fxml");
                    break;
                case "MesaForm.fxml":
                    abrirVistaEnTab("Mesas", "/views/MesaForm.fxml");
                    break;
                case "informacion":
                    mostrarInformacion();
                    break;
                case "cerrar sesion":
                    cerrarSesion(e);
                    break;
                case "salir":
                    salir(e);
                    break;
                default:
                    System.out.println("Acción no implementada: " + action);
            }
        }
    }

    class SampleMenuListener {
        public void menuSelected(javafx.event.Event e) {
            if (((Menu) e.getSource()).getId().equals("mmiver1")) {
                System.out.println("llego help");
            }
        }
    }

    @FXML
    private void cerrarSesion(javafx.event.ActionEvent event) {
        // Lógica para cerrar sesión (por ejemplo, volver a la pantalla de login)
        System.out.println("Cerrar sesión");
    }

    @FXML
    private void salir(javafx.event.ActionEvent event) {
        // Lógica para cerrar la aplicación
        javafx.application.Platform.exit();
    }

    /**
     * Abre una vista en una pestaña. Si la pestaña ya existe, la selecciona. Si existe la pestaña 'Inicio' y se abre 'Nuevo Pedido', la reemplaza.
     */
    private void abrirVistaEnTab(String titulo, String fxmlPath) {
        try {
            // Buscar si ya existe una pestaña con el mismo título
            Tab tabExistente = null;
            for (Tab tab : tabPaneFx.getTabs()) {
                if (titulo.equals(tab.getText())) {
                    tabExistente = tab;
                    break;
                }
            }
            if (tabExistente != null) {
                tabPaneFx.getSelectionModel().select(tabExistente);
                return;
            }
            // Si se va a abrir 'Nuevo Pedido' y existe 'Inicio', reemplazarla
            if ("Nuevo Pedido".equals(titulo)) {
                Tab tabInicio = null;
                for (Tab tab : tabPaneFx.getTabs()) {
                    if ("Inicio".equals(tab.getText())) {
                        tabInicio = tab;
                        break;
                    }
                }
                if (tabInicio != null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                    loader.setControllerFactory(context::getBean);
                    Parent paneFromFXML = loader.load();
                    tabInicio.setText(titulo);
                    tabInicio.setContent(paneFromFXML);
                    tabPaneFx.getSelectionModel().select(tabInicio);
                    return;
                }
            }
            // Si no existe, crear nueva pestaña
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(context::getBean);
            Parent paneFromFXML = loader.load();
            Tab tab = new Tab(titulo, paneFromFXML);
            tabPaneFx.getTabs().add(tab);
            tabPaneFx.getSelectionModel().select(tab);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarInformacion() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText("Sistema de Restaurante");
        alert.setContentText("Versión 1.0\nDesarrollado por UPEU");
        alert.showAndWait();
    }
} 