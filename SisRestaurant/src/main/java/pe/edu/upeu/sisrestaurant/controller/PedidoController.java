package pe.edu.upeu.sisrestaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import pe.edu.upeu.sisrestaurant.repository.PedidoRepository;

@Controller
public class PedidoController {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @FXML private Label lblRegistroPedidos;
    @FXML private Button btnDoc;
    @FXML private TextField txtNroDni;
    @FXML private Button btnSearchCliente;
    @FXML private Label lblNombreCliente;
    @FXML private Button btnNuevoCliente;
    @FXML private Button btnRegistrar;
    @FXML private TableView<?> tblProductos;
    @FXML private ComboBox<?> cbxCategoriaProducto;
    @FXML private TableView<?> tblMesas;
    @FXML private Button btnPrevQuickOptions;
    @FXML private Label lblQuickOptions;
    @FXML private Button btnNextQuickOptions;
    @FXML private TextArea txtaDescripcionPedido;
    @FXML private TableView<?> tblDetallePedido;
    @FXML private Button btnFinalizar;
    @FXML private Button btnCerrar;

    @FXML
    private TabPane tabPaneFx;

    @FXML
    public void initialize() {
        // Inicialización si es necesario
    }

    public void abrirFormularioPedido() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PedidoForm.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent pane = loader.load();
            Tab tab = new Tab("Nuevo Pedido", pane);
            tabPaneFx.getTabs().add(tab);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
} 