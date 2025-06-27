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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import pe.edu.upeu.sisrestaurant.config.ConstantesGlobales;
import pe.edu.upeu.sisrestaurant.service.TipoDocumentoService;
import pe.edu.upeu.sisrestaurant.modelo.TipoDocumento;
import pe.edu.upeu.sisrestaurant.controller.PrincipalFrmController;
import javafx.scene.control.TextFormatter;
import java.util.function.UnaryOperator;
import pe.edu.upeu.sisrestaurant.modelo.Cliente;
import pe.edu.upeu.sisrestaurant.service.ClienteService;
import pe.edu.upeu.sisrestaurant.service.CategoriaService;
import pe.edu.upeu.sisrestaurant.service.ProductoService;
import pe.edu.upeu.sisrestaurant.modelo.Categoria;
import pe.edu.upeu.sisrestaurant.modelo.Producto;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.util.Callback;
import pe.edu.upeu.sisrestaurant.service.MesaService;
import pe.edu.upeu.sisrestaurant.modelo.Mesa;
import javafx.scene.control.SelectionMode;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import pe.edu.upeu.sisrestaurant.modelo.Seccion;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import pe.edu.upeu.sisrestaurant.modelo.Pedido;
import pe.edu.upeu.sisrestaurant.service.PedidoService;
import pe.edu.upeu.sisrestaurant.service.DetallePedidoService;
import pe.edu.upeu.sisrestaurant.modelo.DetallePedido;
import java.time.LocalDateTime;
import pe.edu.upeu.sisrestaurant.service.TipoDocVentaService;
import pe.edu.upeu.sisrestaurant.service.DocVentaService;

@Controller
public class PedidoController {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private TipoDocumentoService tipoDocumentoService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private MesaService mesaService;
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private DetallePedidoService detallePedidoService;
    @Autowired
    private TipoDocVentaService tipoDocVentaService;
    @Autowired
    private DocVentaService docVentaService;
    
    @FXML private Label lblRegistroPedidos;
    @FXML private TextField txtNroDni;
    @FXML private Button btnSearchCliente;
    @FXML private Label lblNombreCliente;
    @FXML private Button btnNuevoCliente;
    @FXML private Button btnRegistrar;
    @FXML private TableView<Producto> tblProductos;
    @FXML private ComboBox<Categoria> cbxCategoriaProducto;
    @FXML private TableView<Mesa> tblMesas;
    @FXML private Button btnPrevQuickOptions;
    @FXML private Label lblQuickOptions;
    @FXML private Button btnNextQuickOptions;
    @FXML private TextArea txtaDescripcionPedido;
    @FXML private TableView<DetallePedidoRow> tblDetallePedido;
    @FXML private Button btnFinalizar;
    @FXML private Button btnCerrar;
    @FXML private Button btnBuscarCliente;

    @FXML
    private TabPane tabPaneFx;

    @FXML private GridPane gridPane;
    @FXML private HBox hBox;
    
    // Variable para almacenar el cliente encontrado en la búsqueda
    private Cliente clienteEncontrado = null;
    private boolean clienteEncontradoEnAPI = false;
    private String nombreClienteAPI = null;
    private ClienteAPIData clienteAPIData = null;
    
    // Clase interna para almacenar datos del cliente de la API
    private static class ClienteAPIData {
        private String nombres;
        private String apellidoPaterno;
        private String apellidoMaterno;
        
        public ClienteAPIData(String nombres, String apellidoPaterno, String apellidoMaterno) {
            this.nombres = nombres;
            this.apellidoPaterno = apellidoPaterno;
            this.apellidoMaterno = apellidoMaterno;
        }
        
        public String getNombreCompleto() {
            return nombres + " " + apellidoPaterno + " " + apellidoMaterno;
        }
        
        public String getNombres() {
            return nombres;
        }
        
        public String getApellidoPaterno() {
            return apellidoPaterno;
        }
        
        public String getApellidoMaterno() {
            return apellidoMaterno;
        }
    }

    private ObservableList<DetallePedidoRow> detallePedidoRows = FXCollections.observableArrayList();
    private double totalPedido = 0.0;

    @FXML private TableColumn<Producto, String> colNombreProducto;
    @FXML private TableColumn<Producto, Double> colPrecioProducto;
    @FXML private TableColumn<Producto, Void> colAccionAgregar;

    @FXML private TableColumn<Mesa, Integer> colNumeroMesa;
    @FXML private TableColumn<Mesa, String> colEstadoMesa;

    @FXML private TableColumn<DetallePedidoRow, String> colClienteDetalle;
    @FXML private TableColumn<DetallePedidoRow, String> colMesaDetalle;
    @FXML private TableColumn<DetallePedidoRow, Double> colTotalDetalle;

    private Pedido pedidoActual = null;

    @FXML private ComboBox<pe.edu.upeu.sisrestaurant.modelo.TipoDocVenta> cbxTipoDocVenta;

    @FXML
    public void initialize() {
        // Inicialización si es necesario
        configurarValidacionNumeroDocumento();
        
        // Agregar listener para buscar cliente al presionar Enter
        txtNroDni.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onBuscarCliente();
            }
        });
        
        // Agregar listener para limpiar campos cuando se borre el DNI
        txtNroDni.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                limpiarCamposCliente();
            }
        });
        
        // Inicialmente deshabilitar el botón de nuevo cliente
        btnNuevoCliente.setDisable(true);
        
        // Limpiar el label del nombre del cliente al inicializar
        lblNombreCliente.setText("Nombre de Cliente");
        
        inicializarCategoriasYProductos();
        inicializarMesas();
        inicializarDetallePedido();
        inicializarComboTipoDocVenta();
    }

    private void configurarValidacionNumeroDocumento() {
        // Obtener el tipo de documento seleccionado en el principal
        try {
            org.springframework.context.ApplicationContext ctx = pe.edu.upeu.sisrestaurant.SisRestaurantApplication.getContext();
            PrincipalFrmController principalController = ctx.getBean(PrincipalFrmController.class);
            String docSeleccionado = principalController.getDocSeleccionado();
            
            // Buscar el tipo de documento en la base de datos
            TipoDocumento tipoDoc = tipoDocumentoService.getTipoDocumentoByNombre(docSeleccionado);
            if (tipoDoc != null && tipoDoc.getCantVal() != null) {
                int maxLength = tipoDoc.getCantVal();
                
                // Crear un TextFormatter que limite la longitud y solo permita números
                UnaryOperator<TextFormatter.Change> filter = change -> {
                    String newText = change.getControlNewText();
                    if (newText.length() <= maxLength && newText.matches("\\d*")) {
                        return change;
                    }
                    return null; // Rechazar el cambio
                };
                
                TextFormatter<String> textFormatter = new TextFormatter<>(filter);
                txtNroDni.setTextFormatter(textFormatter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buscarClientePorDNI(String dni) {
        if (dni != null && !dni.trim().isEmpty()) {
            try {
                // Primero buscar en la API de DNI
                ClienteAPIData clienteAPIData = buscarClienteEnAPI(dni.trim());
                
                if (clienteAPIData != null) {
                    // Cliente encontrado en API - mostrar nombre y habilitar botón
                    lblNombreCliente.setText(clienteAPIData.getNombreCompleto());
                    btnNuevoCliente.setDisable(false);
                } else {
                    // No encontrado en API, buscar en base de datos local
                    Cliente clienteLocal = buscarClienteEnBaseDatos(dni.trim());
                    
                    if (clienteLocal != null) {
                        // Cliente encontrado en base de datos - mostrar nombre y habilitar botón
                        lblNombreCliente.setText(clienteLocal.getNombres());
                        btnNuevoCliente.setDisable(false);
                    } else {
                        // Cliente no encontrado ni en API ni en base de datos
                        lblNombreCliente.setText("Cliente no encontrado");
                        btnNuevoCliente.setDisable(true);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                lblNombreCliente.setText("Error al buscar cliente");
                btnNuevoCliente.setDisable(true);
            }
        } else {
            // Campo vacío - limpiar y deshabilitar
            lblNombreCliente.setText("Nombre de Cliente");
            btnNuevoCliente.setDisable(true);
        }
    }

    private ClienteAPIData buscarClienteEnAPI(String dni) {
        try {
            // URL de la API de APIsPeru
            String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Imhlcm1lc19jaGFtYmlsbGFfQGhvdG1haWwuY29tIn0.Ki99hjd1ixTZbOIW_82GaIF_s6kSPQuTePO-o6Vlzds";
            String url = "https://dniruc.apisperu.com/api/v1/dni/" + dni + "?token=" + token;
            
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                // Parsear la respuesta JSON de APIsPeru
                JSONObject jsonResponse = new JSONObject(response.toString());
                
                // Verificar si la consulta fue exitosa
                if (jsonResponse.optBoolean("success", false)) {
                    // Extraer datos del cliente
                    String nombres = jsonResponse.optString("nombres", "");
                    String apellidoPaterno = jsonResponse.optString("apellidoPaterno", "");
                    String apellidoMaterno = jsonResponse.optString("apellidoMaterno", "");
                    
                    // Retornar objeto con todos los datos
                    return new ClienteAPIData(nombres, apellidoPaterno, apellidoMaterno);
                } else {
                    // Cliente no encontrado o error en la API
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Cliente buscarClienteEnBaseDatos(String dni) {
        try {
            // Buscar cliente por número de identidad en la base de datos local
            java.util.List<Cliente> clientes = clienteService.findAll();
            return clientes.stream()
                .filter(c -> c.getNroIdentidad().equals(dni))
                .findFirst()
                .orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void actualizarNombreCliente(Cliente cliente) {
        if (cliente != null) {
            lblNombreCliente.setText(cliente.getNombres());
            btnNuevoCliente.setDisable(false);
        }
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
    
    public void deshabilitarControlesPrincipales() {
        if (gridPane != null) gridPane.setDisable(true);
        if (hBox != null) hBox.setDisable(true);
    }

    public void habilitarControlesPrincipales() {
        if (gridPane != null) gridPane.setDisable(false);
        if (hBox != null) hBox.setDisable(false);
    }

    @FXML
    private void onRegistrar() {
        // Validar si hay productos en el catálogo general
        if (productoService.list().isEmpty()) {
            mostrarAlerta("Error", "No existen productos registrados en el sistema. Registre productos antes de asociar un cliente al pedido.");
            return;
        }
        // Validar si hay productos agregados al pedido
        if (detallePedidoRows.isEmpty()) {
            mostrarAlerta("Error", "Debe agregar al menos un producto antes de registrar el cliente en el pedido.");
            return;
        }
        if (pedidoActual != null && clienteEncontrado != null) {
            pedidoActual.setIdCliente(clienteEncontrado.getId());
            pedidoService.save(pedidoActual);
            mostrarAlerta("Cliente registrado", "El cliente ha sido asociado correctamente al pedido.");
            // Habilitar campos deshabilitados para continuar el proceso
            tblProductos.setDisable(false);
            tblDetallePedido.setDisable(false);
            btnFinalizar.setDisable(false);
            habilitarControlesPrincipales();
            // Deshabilitar el botón de registrar cliente y buscar cliente
            btnRegistrar.setDisable(true);
            btnBuscarCliente.setDisable(true);
        } else {
            mostrarAlerta("Error", "Debe seleccionar una mesa y un cliente antes de registrar el cliente en el pedido.");
        }
    }

    @FXML
    private void onGenerarCliente() {
        String dni = txtNroDni.getText().trim();
        
        if (dni.isEmpty()) {
            // Mostrar mensaje de error si no hay DNI
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("DNI Requerido");
            alert.setHeaderText(null);
            alert.setContentText("Debe ingresar un número de DNI antes de buscar.");
            alert.showAndWait();
            return;
        }
        
        // Mostrar indicador de búsqueda
        lblNombreCliente.setText("Buscando cliente...");
        btnNuevoCliente.setDisable(true);
        
        // Limpiar cliente encontrado anteriormente
        clienteEncontrado = null;
        clienteEncontradoEnAPI = false;
        clienteAPIData = null;
        
        try {
            // PRIMERO: Buscar en la base de datos local
            Cliente clienteLocal = buscarClienteEnBaseDatos(dni);
            
            if (clienteLocal != null) {
                // ✅ Cliente encontrado en base de datos - SOLO MOSTRAR NOMBRE
                lblNombreCliente.setText(clienteLocal.getNombres());
                btnNuevoCliente.setDisable(false);
                
                // Guardar el cliente encontrado para uso posterior (cuando se presione +)
                clienteEncontrado = clienteLocal;
                clienteEncontradoEnAPI = false;
            } else {
                // No encontrado en base de datos, buscar en la API de DNI
                ClienteAPIData clienteAPIData = buscarClienteEnAPI(dni);
                
                if (clienteAPIData != null) {
                    // ✅ Cliente encontrado en API - SOLO MOSTRAR NOMBRE
                    lblNombreCliente.setText(clienteAPIData.getNombreCompleto());
                    btnNuevoCliente.setDisable(false);
                    
                    // Marcar que se encontró en API y almacenar datos
                    clienteEncontrado = null;
                    clienteEncontradoEnAPI = true;
                    this.clienteAPIData = clienteAPIData;
                } else {
                    // Cliente no encontrado ni en API ni en base de datos
                    lblNombreCliente.setText("Cliente no encontrado");
                    btnNuevoCliente.setDisable(true);
                    
                    // Limpiar variables en caso de error
                    clienteEncontrado = null;
                    clienteEncontradoEnAPI = false;
                    clienteAPIData = null;

                    // Abrir automáticamente la ventana de cliente con el DNI ingresado
                    org.springframework.context.ApplicationContext ctx = pe.edu.upeu.sisrestaurant.SisRestaurantApplication.getContext();
                    PrincipalFrmController principalController = ctx.getBean(PrincipalFrmController.class);
                    principalController.abrirClienteEnPanelDerecho(dni);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblNombreCliente.setText("Error al buscar cliente");
            btnNuevoCliente.setDisable(true);
            
            // Limpiar variables en caso de error
            clienteEncontrado = null;
            clienteEncontradoEnAPI = false;
            clienteAPIData = null;
        }
    }

    public JSONObject consultarDni(String dni) {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Imhlcm1lc19jaGFtYmlsbGFfQGhvdG1haWwuY29tIn0.Ki99hjd1ixTZbOIW_82GaIF_s6kSPQuTePO-o6Vlzds";
        String urlStr = "https://dniruc.apisperu.com/api/v1/dni/" + dni + "?token=" + token;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                return null;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            conn.disconnect();
            return new JSONObject(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void llenarDatosCliente(pe.edu.upeu.sisrestaurant.modelo.Cliente cliente) {
        if (lblNombreCliente != null) {
            lblNombreCliente.setText(cliente.getNombres());
        }
        if (txtNroDni != null) {
            txtNroDni.setText(cliente.getNroIdentidad());
        }
    }

    public void limpiarCamposCliente() {
        lblNombreCliente.setText("Nombre de Cliente");
        btnNuevoCliente.setDisable(true);
        
        // Limpiar variables del cliente encontrado
        clienteEncontrado = null;
        clienteEncontradoEnAPI = false;
        clienteAPIData = null;
    }

    public void actualizarValidacionNumeroDocumento() {
        configurarValidacionNumeroDocumento();
    }

    @FXML
    private void onBtnNuevoCliente() {
        try {
            // Obtener el DNI ingresado
            String dni = txtNroDni.getText().trim();
            if (dni.isEmpty()) {
                // Mostrar mensaje de error si no hay DNI
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
                alert.setTitle("DNI Requerido");
                alert.setHeaderText(null);
                alert.setContentText("Debe ingresar un número de DNI antes de agregar datos del cliente.");
                alert.showAndWait();
                return;
            }
            
            // Abrir formulario de cliente según el origen de los datos
            org.springframework.context.ApplicationContext ctx = pe.edu.upeu.sisrestaurant.SisRestaurantApplication.getContext();
            PrincipalFrmController principalController = ctx.getBean(PrincipalFrmController.class);
            
            if (clienteEncontrado != null) {
                // ✅ Cliente encontrado en base de datos - LLENAR TODOS LOS CAMPOS
                principalController.abrirClienteEnPanelDerechoConDatos(clienteEncontrado);
            } else if (clienteEncontradoEnAPI && clienteAPIData != null) {
                // ✅ Cliente encontrado en API - CON DNI Y NOMBRE
                principalController.abrirClienteEnPanelDerechoConDatosAPI(
                    dni, 
                    clienteAPIData.getNombres(), 
                    clienteAPIData.getApellidoPaterno(), 
                    clienteAPIData.getApellidoMaterno()
                );
            } else {
                // ✅ Cliente no encontrado - FORMULARIO VACÍO
                principalController.abrirClienteEnPanelDerecho(dni);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void inicializarCategoriasYProductos() {
        // Cargar categorías
        cbxCategoriaProducto.setItems(FXCollections.observableArrayList(categoriaService.list()));
        cbxCategoriaProducto.setConverter(new StringConverter<Categoria>() {
            @Override
            public String toString(Categoria object) {
                return object == null ? "" : object.getNombre();
            }

            @Override
            public Categoria fromString(String string) {
                return null;
            }
        });
        
        // Seleccionar la primera categoría por defecto
        if (!cbxCategoriaProducto.getItems().isEmpty()) {
            cbxCategoriaProducto.getSelectionModel().selectFirst();
        }
        
        // Cargar productos filtrados por la sección global
        cargarProductosPorSeccionGlobal();
        
        // Agregar listener para filtrar por categoría
        cbxCategoriaProducto.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            filtrarProductosPorCategoria();
        });
        
        // Configurar tabla de productos
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecioProducto.setCellValueFactory(new PropertyValueFactory<>("precio"));
        
        agregarColumnaAccionAgregar();
    }
    
    private void cargarProductosPorSeccionGlobal() {
        Seccion seccionGlobal = ConstantesGlobales.getSeccionSeleccionada();
        if (seccionGlobal != null) {
            // Filtrar productos por la sección global
            java.util.List<Producto> todosLosProductos = productoService.list();
            java.util.List<Producto> productosFiltrados = new java.util.ArrayList<>();
            
            for (Producto p : todosLosProductos) {
                if (seccionGlobal.getId() != null && seccionGlobal.getId().equals(p.getIdSeccion())) {
                    productosFiltrados.add(p);
                }
            }
            
            tblProductos.setItems(FXCollections.observableArrayList(productosFiltrados));
        } else {
            // Si no hay sección global, mostrar todos los productos
            tblProductos.setItems(FXCollections.observableArrayList(productoService.list()));
        }
    }

    private void filtrarProductosPorCategoria() {
        Categoria categoriaSeleccionada = cbxCategoriaProducto.getSelectionModel().getSelectedItem();
        Seccion seccionGlobal = ConstantesGlobales.getSeccionSeleccionada();
        
        if (categoriaSeleccionada != null) {
            java.util.List<Producto> todosLosProductos = productoService.list();
            java.util.List<Producto> productosFiltrados = new java.util.ArrayList<>();
            
            for (Producto p : todosLosProductos) {
                boolean coincideCategoria = categoriaSeleccionada.getId() != null && 
                                          categoriaSeleccionada.getId().equals(p.getIdCategoria());
                boolean coincideSeccion = seccionGlobal == null || 
                                        (seccionGlobal.getId() != null && 
                                         seccionGlobal.getId().equals(p.getIdSeccion()));
                
                if (coincideCategoria && coincideSeccion) {
                    productosFiltrados.add(p);
                }
            }
            
            tblProductos.setItems(FXCollections.observableArrayList(productosFiltrados));
        } else {
            cargarProductosPorSeccionGlobal();
        }
    }

    private void inicializarMesas() {
        colNumeroMesa.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colEstadoMesa.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tblMesas.setItems(FXCollections.observableArrayList(mesaService.list()));
        tblMesas.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        // Seleccionar la primera mesa disponible
        for (Mesa mesa : tblMesas.getItems()) {
            if ("disponible".equalsIgnoreCase(mesa.getEstado())) {
                tblMesas.getSelectionModel().select(mesa);
                break;
            }
        }
        // Listener para crear pedido al seleccionar mesa
        tblMesas.getSelectionModel().selectedItemProperty().addListener((obs, oldMesa, newMesa) -> {
            if (newMesa != null && clienteEncontrado != null) {
                if (detallePedidoRows.isEmpty()) {
                    crearPedidoInicial(newMesa, clienteEncontrado);
                } else {
                    mostrarAlerta("No permitido", "No puede cambiar de mesa después de agregar productos al pedido.");
                    // Volver a seleccionar la mesa anterior
                    tblMesas.getSelectionModel().select(oldMesa);
                }
            }
        });
    }

    private void crearPedidoInicial(Mesa mesa, Cliente cliente) {
        if (pedidoActual == null) {
            pedidoActual = Pedido.builder()
                .idMesa(mesa.getId())
                .idCliente(cliente.getId())
                .fechaHora(LocalDateTime.now().toString())
                .estado("PENDIENTE")
                .build();
            pedidoActual = pedidoService.save(pedidoActual);
            tblMesas.setDisable(true);
            mostrarAlerta("Pedido creado", "Se ha creado un nuevo pedido para la mesa seleccionada.");
        }
    }

    private void inicializarDetallePedido() {
        colClienteDetalle.setCellValueFactory(cellData -> new SimpleStringProperty(lblNombreCliente.getText()));
        colMesaDetalle.setCellValueFactory(cellData -> {
            Mesa mesa = tblMesas.getSelectionModel().getSelectedItem();
            return new SimpleStringProperty(mesa != null ? String.valueOf(mesa.getNumero()) : "");
        });
        colTotalDetalle.setCellValueFactory(cellData -> new SimpleDoubleProperty(totalPedido).asObject());
        tblDetallePedido.setItems(detallePedidoRows);
    }

    private void agregarColumnaAccionAgregar() {
        colAccionAgregar.setCellFactory(param -> new TableCell<Producto, Void>() {
            private final TextField txtCantidad = new TextField("1");
            private final Button btnAgregar = new Button("Agregar");
            private final HBox hbox = new HBox(5, txtCantidad, btnAgregar);
            {
                btnAgregar.setOnAction(event -> {
                    Producto producto = getTableView().getItems().get(getIndex());
                    int cantidad = 1;
                    try { cantidad = Integer.parseInt(txtCantidad.getText()); } catch (Exception e) { cantidad = 1; }
                    agregarProductoADetalle(producto, cantidad);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(hbox);
                }
            }
        });
    }

    private void agregarProductoADetalle(Producto producto, int cantidad) {
        if (pedidoActual == null) {
            Mesa mesa = tblMesas.getSelectionModel().getSelectedItem();
            if (mesa != null && clienteEncontrado != null) {
                crearPedidoInicial(mesa, clienteEncontrado);
            } else {
                mostrarAlerta("Error", "Debe seleccionar una mesa y un cliente antes de agregar productos.");
                return;
            }
        }
        // Si el pedido está pendiente, actualizar a EN PROCESO
        if ("PENDIENTE".equals(pedidoActual.getEstado())) {
            pedidoActual.setEstado("EN PROCESO");
            pedidoService.save(pedidoActual);
        }
        // Guardar detalle en la base de datos
        DetallePedido detalle = DetallePedido.builder()
            .idPedido(pedidoActual.getId())
            .idProducto(producto.getId())
            .cantidad(cantidad)
            .precio(producto.getPrecio())
            .fechaRegistro(LocalDateTime.now().toString())
            .estado("ACTIVO")
            .build();
        detallePedidoService.save(detalle);
        // Actualizar la tabla de detalles (puedes mejorar la lógica para evitar duplicados)
        totalPedido += producto.getPrecio() * cantidad;
        detallePedidoRows.add(new DetallePedidoRow(producto.getNombre(), String.valueOf(tblMesas.getSelectionModel().getSelectedItem() != null ? tblMesas.getSelectionModel().getSelectedItem().getNumero() : ""), totalPedido));
        tblDetallePedido.refresh();
    }

    public static class DetallePedidoRow {
        private final SimpleStringProperty cliente;
        private final SimpleStringProperty mesa;
        private final SimpleDoubleProperty total;
        public DetallePedidoRow(String cliente, String mesa, double total) {
            this.cliente = new SimpleStringProperty(cliente);
            this.mesa = new SimpleStringProperty(mesa);
            this.total = new SimpleDoubleProperty(total);
        }
        public String getCliente() { return cliente.get(); }
        public String getMesa() { return mesa.get(); }
        public Double getTotal() { return total.get(); }
    }

    @FXML
    private void onBuscarCliente() {
        String dni = txtNroDni.getText().trim();
        buscarClientePorDNI(dni);
    }

    @FXML
    private void onFinalizar() {
        if (pedidoActual != null && !detallePedidoRows.isEmpty()) {
            // Validar selección de tipo de doc venta
            pe.edu.upeu.sisrestaurant.modelo.TipoDocVenta tipoDocVentaSel = cbxTipoDocVenta.getSelectionModel().getSelectedItem();
            if (tipoDocVentaSel == null) {
                mostrarAlerta("Error", "Debe seleccionar un tipo de documento de venta.");
                return;
            }
            // Generar código de venta único (puedes mejorar la lógica si lo deseas)
            String codigoVenta = "VTA-" + System.currentTimeMillis();
            // Crear y guardar DocVenta
            pe.edu.upeu.sisrestaurant.modelo.DocVenta docVenta = pe.edu.upeu.sisrestaurant.modelo.DocVenta.builder()
                .codigoVenta(codigoVenta)
                .tipoDocVenta(tipoDocVentaSel.getNombre())
                .fechaHora(java.time.LocalDateTime.now().toString())
                .estado("EMITIDO")
                .idPedido(pedidoActual.getId())
                .build();
            docVentaService.save(docVenta);
            pedidoActual.setEstado("FINALIZADO");
            pedidoService.save(pedidoActual);
            mostrarAlerta("Pedido finalizado", "El pedido y su documento de venta han sido guardados correctamente.\nCódigo: " + codigoVenta);
            reiniciarFormularioPedido();
        } else {
            mostrarAlerta("Error", "Debe agregar productos antes de finalizar el pedido.");
        }
    }

    @FXML
    private void onCancelar() {
        reiniciarFormularioPedido();
    }

    private void reiniciarFormularioPedido() {
        pedidoActual = null;
        detallePedidoRows.clear();
        totalPedido = 0.0;
        tblDetallePedido.refresh();
        tblMesas.setDisable(false);
        tblMesas.getSelectionModel().clearSelection();
        tblProductos.setDisable(true);
        tblDetallePedido.setDisable(true);
        btnFinalizar.setDisable(true);
        btnRegistrar.setDisable(false);
        btnBuscarCliente.setDisable(false);
        limpiarCamposCliente();
        lblNombreCliente.setText("Nombre de Cliente");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void actualizarProductosDesdeFormulario() {
        inicializarCategoriasYProductos();
    }

    private void inicializarComboTipoDocVenta() {
        cbxTipoDocVenta.setItems(FXCollections.observableArrayList(tipoDocVentaService.list()));
        cbxTipoDocVenta.setConverter(new StringConverter<pe.edu.upeu.sisrestaurant.modelo.TipoDocVenta>() {
            @Override
            public String toString(pe.edu.upeu.sisrestaurant.modelo.TipoDocVenta object) {
                return object == null ? "" : object.getNombre();
            }
            @Override
            public pe.edu.upeu.sisrestaurant.modelo.TipoDocVenta fromString(String string) {
                return null;
            }
        });
        if (!cbxTipoDocVenta.getItems().isEmpty()) {
            cbxTipoDocVenta.getSelectionModel().selectFirst();
        }
    }
} 