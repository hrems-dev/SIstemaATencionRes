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
import pe.edu.upeu.sisrestaurant.service.PersonalService;
import pe.edu.upeu.sisrestaurant.dto.SessionManager;
import pe.edu.upeu.sisrestaurant.modelo.Personal;
import java.util.List;
import pe.edu.upeu.sisrestaurant.service.UsuarioService;
import java.util.Optional;
import pe.edu.upeu.sisrestaurant.config.EstadosDetallePedido;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

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
    @Autowired
    private PersonalService personalService;
    @Autowired
    private UsuarioService usuarioService;
    
    @FXML private Label lblRegistroPedidos;
    @FXML private TextField txtNroDni;
    @FXML private Button btnSearchCliente;
    @FXML private Label lblNombreCliente;
    @FXML private Button btnNuevoCliente;
    @FXML private Button btnRegistrar;
    @FXML private TableView<Producto> tblProductos;
    @FXML private ComboBox<Categoria> cbxCategoriaProducto;
    @FXML private GridPane contenedorMesas;
    @FXML private Button btnPrevQuickOptions;
    @FXML private Label lblQuickOptions;
    @FXML private Button btnNextQuickOptions;
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

    @FXML private TableColumn<DetallePedidoRow, String> colClienteDetalle;
    @FXML private TableColumn<DetallePedidoRow, String> colMesaDetalle;
    @FXML private TableColumn<DetallePedidoRow, Double> colTotalDetalle;

    private Pedido pedidoActual = null;

    private Mesa mesaSeleccionada = null;

    @FXML private TableView<DetallePedidoRow> tblDetallePedido;
    @FXML private Button btnFinalizar;

    @FXML private TableColumn<DetallePedidoRow, String> colProductoDetalle;
    @FXML private TableColumn<DetallePedidoRow, Double> colPrecioUnitarioDetalle;
    @FXML private TableColumn<DetallePedidoRow, Integer> colCantidadDetalle;
    @FXML private TableColumn<DetallePedidoRow, Double> colSubtotalDetalle;
    @FXML private TableColumn<DetallePedidoRow, String> colEstadoDetalle;
    @FXML private TableColumn<DetallePedidoRow, Void> colAccionEstadoDetalle;

    @FXML
    public void initialize() {
        try {
            System.out.println("Iniciando PedidoController...");
            
            // Verificar la relación usuario-personal al inicializar
            verificarRelacionUsuarioPersonal();
            
            // Inicialización si es necesario
            configurarValidacionNumeroDocumento();
            
            // Agregar listener para buscar cliente al presionar Enter
            if (txtNroDni != null) {
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
            }
            
            // Inicialmente deshabilitar el botón de nuevo cliente
            if (btnNuevoCliente != null) {
                btnNuevoCliente.setDisable(true);
            }
            
            // Limpiar el label del nombre del cliente al inicializar
            if (lblNombreCliente != null) {
                lblNombreCliente.setText("Nombre de Cliente");
            }
            
            // Inicializar componentes
            inicializarCategoriasYProductos();
            inicializarMesas();
            inicializarDetallePedido();
            
            // Al iniciar, dejar la tabla de productos vacía y deshabilitar el ComboBox de categoría
            if (tblProductos != null) tblProductos.setItems(FXCollections.observableArrayList());
            if (cbxCategoriaProducto != null) cbxCategoriaProducto.setDisable(true);
            
            System.out.println("PedidoController inicializado correctamente");
        } catch (Exception e) {
            System.err.println("Error al inicializar PedidoController: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Método para verificar la relación entre usuarios y personal
     */
    private void verificarRelacionUsuarioPersonal() {
        try {
            System.out.println("\n=== VERIFICACIÓN DE RELACIÓN USUARIO-PERSONAL ===");
            
            // Obtener usuario actual de la sesión
            Long userId = SessionManager.getInstance().getUserId();
            String userName = SessionManager.getInstance().getUserName();
            
            System.out.println("📋 Información de sesión:");
            System.out.println("   - User ID: " + userId);
            System.out.println("   - User Name: " + userName);
            
            if (userId == null) {
                System.out.println("❌ ERROR: No hay usuario logueado en la sesión");
                mostrarAlerta("Error de Sesión", "No hay usuario logueado en la sesión. Debe iniciar sesión nuevamente.");
                return;
            }
            
            // Buscar el usuario en la base de datos para obtener su tipo
            System.out.println("🔍 Buscando usuario en base de datos...");
            pe.edu.upeu.sisrestaurant.modelo.Usuario usuario = usuarioService.findById(userId);
            if (usuario == null) {
                System.out.println("❌ ERROR: Usuario no encontrado en la base de datos");
                mostrarAlerta("Error de Usuario", "El usuario no existe en la base de datos.");
                return;
            }
            
            System.out.println("👤 Usuario encontrado en BD:");
            System.out.println("   - ID: " + usuario.getIduser());
            System.out.println("   - Nombre: " + usuario.getNombre());
            System.out.println("   - Tipo: " + usuario.getTipoUsuario());
            System.out.println("   - Estado: " + usuario.getEstado());
            
            // Si es usuario root, no necesita personal
            if ("root".equals(usuario.getTipoUsuario())) {
                System.out.println("👑 Usuario root detectado - No requiere registro de personal");
                return;
            }
            
            // Para usuarios normales, verificar que tengan personal asociado
            if ("normal".equals(usuario.getTipoUsuario())) {
                System.out.println("🔍 Buscando personal para usuario normal...");
                Personal personal = personalService.findByUsuarioId(userId);
                
                if (personal != null) {
                    System.out.println("✅ Personal encontrado para usuario normal:");
                    System.out.println("   - ID Personal: " + personal.getIdPersonal());
                    System.out.println("   - DNI: " + personal.getDni());
                    System.out.println("   - Rol: " + (personal.getRol() != null ? personal.getRol().getDescripcion() : "Sin rol"));
                    System.out.println("   - Usuario asociado: " + (personal.getUsuario() != null ? personal.getUsuario().getNombre() : "NULL"));
                } else {
                    System.out.println("❌ ERROR: Usuario normal sin registro de personal");
                    
                    // Mostrar información detallada para diagnóstico
                    System.out.println("\n=== DIAGNÓSTICO DETALLADO ===");
                    System.out.println("Usuario ID: " + userId + ", Nombre: " + userName + ", Tipo: " + usuario.getTipoUsuario());
                    
                    // Listar todo el personal para ver qué hay disponible
                    System.out.println("\n=== PERSONAL DISPONIBLE EN EL SISTEMA ===");
                    List<Personal> personalList = personalService.list();
                    if (personalList.isEmpty()) {
                        System.out.println("❌ No hay personal registrado en el sistema");
                    } else {
                        System.out.println("📋 Personal registrado (" + personalList.size() + " registros):");
                        for (Personal p : personalList) {
                            System.out.println("   - ID Personal: " + p.getIdPersonal() + 
                                             ", ID Usuario: " + (p.getUsuario() != null ? p.getUsuario().getIduser() : "NULL") + 
                                             ", DNI: " + p.getDni() +
                                             ", Rol: " + (p.getRol() != null ? p.getRol().getDescripcion() : "Sin rol"));
                        }
                    }
                    
                    // Verificar si hay algún personal con el mismo ID de usuario
                    System.out.println("\n=== BÚSQUEDA ESPECÍFICA ===");
                    boolean encontrado = false;
                    for (Personal p : personalList) {
                        if (p.getUsuario() != null && p.getUsuario().getIduser().equals(userId)) {
                            System.out.println("✅ ENCONTRADO: Personal con ID de usuario " + userId + ":");
                            System.out.println("   - ID Personal: " + p.getIdPersonal());
                            System.out.println("   - DNI: " + p.getDni());
                            encontrado = true;
                            break;
                        }
                    }
                    
                    if (!encontrado) {
                        System.out.println("❌ NO ENCONTRADO: Ningún personal asociado al usuario ID " + userId);
                    }
                    
                    // Mostrar error específico al usuario
                    String mensajeError = "El usuario '" + userName + "' no está registrado como personal del sistema.\n\n" +
                                        "Para poder crear pedidos, debe:\n" +
                                        "1. Tener un registro en la tabla 'personal'\n" +
                                        "2. Estar asociado a un rol válido\n" +
                                        "3. Contactar al administrador para su registro\n\n" +
                                        "Usuario ID: " + userId + "\n" +
                                        "Tipo: " + usuario.getTipoUsuario();
                    
                    mostrarAlerta("Usuario No Registrado", mensajeError);
                    
                    // Deshabilitar funcionalidades que requieren personal
                    deshabilitarFuncionalidadesPorFaltaDePersonal();
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error al verificar relación usuario-personal: " + e.getMessage());
            e.printStackTrace();
            mostrarAlerta("Error de Verificación", "Error al verificar la información del usuario: " + e.getMessage());
        }
    }
    
    /**
     * Método para deshabilitar funcionalidades cuando no hay personal registrado
     */
    private void deshabilitarFuncionalidadesPorFaltaDePersonal() {
        try {
            // Deshabilitar botones y funcionalidades que requieren personal
            if (btnRegistrar != null) btnRegistrar.setDisable(true);
            if (btnFinalizar != null) btnFinalizar.setDisable(true);
            if (btnNuevoCliente != null) btnNuevoCliente.setDisable(true);
            if (btnBuscarCliente != null) btnBuscarCliente.setDisable(true);
            if (contenedorMesas != null) contenedorMesas.setDisable(true);
            if (tblProductos != null) tblProductos.setDisable(true);
            
            System.out.println("Funcionalidades deshabilitadas por falta de registro de personal");
        } catch (Exception e) {
            System.err.println("Error al deshabilitar funcionalidades: " + e.getMessage());
        }
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
                        // Abrir automáticamente la ventana de registro de cliente
                        org.springframework.context.ApplicationContext ctx = pe.edu.upeu.sisrestaurant.SisRestaurantApplication.getContext();
                        pe.edu.upeu.sisrestaurant.controller.PrincipalFrmController principalController = ctx.getBean(pe.edu.upeu.sisrestaurant.controller.PrincipalFrmController.class);
                        principalController.abrirClienteEnPanelDerecho(dni.trim());
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
            // Establecer el cliente encontrado
            clienteEncontrado = cliente;
            
            // Actualizar el nombre del cliente
            lblNombreCliente.setText(cliente.getNombres());
            
            // Habilitar el botón de nuevo cliente
            btnNuevoCliente.setDisable(false);
            
            System.out.println("Nombre de cliente actualizado: " + cliente.getNombres());
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
        // Si existen productos en la base de datos, habilitar campos y botón de registrar cliente
        if (!productoService.list().isEmpty()) {
            tblProductos.setDisable(false);
            tblDetallePedido.setDisable(false);
            btnRegistrar.setDisable(false);
            btnFinalizar.setDisable(false);
            habilitarControlesPrincipales();
        } else {
            mostrarAlerta("Sin productos", "No existen productos registrados. Debe agregar productos antes de continuar.");
            return;
        }
        
        // Validar si hay cliente, si no existe, registrar automáticamente
        if (clienteEncontrado == null && txtNroDni.getText() != null && !txtNroDni.getText().trim().isEmpty()) {
            String dni = txtNroDni.getText().trim();
            if (!dni.matches("\\d{8}")) {
                mostrarAlerta("Error", "El DNI debe tener exactamente 8 dígitos numéricos.");
                return;
            }
            
            // Buscar si el cliente ya existe en la base de datos
            Cliente clienteExistente = buscarClienteEnBaseDatos(dni);
            
            if (clienteExistente != null) {
                // Cliente ya existe - siempre actualizar el nombre
                clienteExistente.setNombres(lblNombreCliente.getText());
                
                // Solo aplicar datos por defecto si hay un pedido activo
                if (pedidoActual != null) {
                    // Sumar 1 a la cantidad de visitas solo si está en pedido
                    clienteExistente.setCantVisitas(clienteExistente.getCantVisitas() != null ? 
                        clienteExistente.getCantVisitas() + 1 : 1);
                }
                
                clienteExistente = clienteService.save(clienteExistente);
                clienteEncontrado = clienteExistente;
                mostrarAlerta("Cliente actualizado", "Se ha actualizado el cliente con DNI: " + dni);
            } else {
                // Usar directamente el ID del tipo de documento DNI desde la constante global
                Long idTipoDocumento = ConstantesGlobales.TIPO_DOCUMENTO_DEFAULT_ID;
                
                // Registrar cliente nuevo - siempre con nombre
                pe.edu.upeu.sisrestaurant.modelo.Cliente nuevoCliente = pe.edu.upeu.sisrestaurant.modelo.Cliente.builder()
                    .idTipoDocumento(idTipoDocumento)
                    .nroIdentidad(dni)
                    .nombres(lblNombreCliente.getText())
                    .estado(true)
                    .fechaRegistro(java.time.LocalDate.now().toString())
                    .build();
                
                // Solo aplicar datos por defecto si hay un pedido activo
                if (pedidoActual != null) {
                    nuevoCliente.setTelefono("000000000");
                    nuevoCliente.setCorreo("ejemplo@gmail.com");
                    nuevoCliente.setCantVisitas(0);
                }
                
                nuevoCliente = clienteService.save(nuevoCliente);
                clienteEncontrado = nuevoCliente;
                mostrarAlerta("Cliente registrado", "Se ha registrado automáticamente el cliente con DNI: " + dni);
            }
        }
        
        // Verificar si hay mesa seleccionada y cliente para crear el pedido
        if (mesaSeleccionada != null && clienteEncontrado != null && pedidoActual == null) {
            // Crear el pedido inicial
            crearPedidoInicial(mesaSeleccionada, clienteEncontrado);
            mostrarAlerta("Pedido creado", "Se ha creado el pedido y registrado el cliente correctamente.");
            // Deshabilitar el botón de registrar cliente y buscar cliente
            btnRegistrar.setDisable(true);
            btnBuscarCliente.setDisable(true);
            btnNuevoCliente.setDisable(true);
        } else if (pedidoActual != null && clienteEncontrado != null) {
            // Si ya existe un pedido, solo asociar el cliente
            pedidoActual.setIdCliente(clienteEncontrado.getId());
            pedidoService.save(pedidoActual);
            mostrarAlerta("Cliente registrado", "El cliente ha sido asociado correctamente al pedido.");
            // Deshabilitar el botón de registrar cliente y buscar cliente
            btnRegistrar.setDisable(true);
            btnBuscarCliente.setDisable(true);
            btnNuevoCliente.setDisable(true);
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
        if (cliente != null) {
            // Establecer el cliente encontrado
            clienteEncontrado = cliente;
            
            // Llenar los campos del formulario
            if (lblNombreCliente != null) {
                lblNombreCliente.setText(cliente.getNombres());
            }
            if (txtNroDni != null) {
                txtNroDni.setText(cliente.getNroIdentidad());
            }
            
            // Deshabilitar el botón de registrar cliente ya que el cliente ya está registrado
            if (btnRegistrar != null) {
                btnRegistrar.setDisable(true);
            }
            if (btnBuscarCliente != null) {
                btnBuscarCliente.setDisable(true);
            }
            if (btnNuevoCliente != null) {
                btnNuevoCliente.setDisable(true);
            }
            
            System.out.println("Cliente registrado desde formulario: " + cliente.getNombres() + " (DNI: " + cliente.getNroIdentidad() + ")");
        }
    }

    public void limpiarCamposCliente() {
        lblNombreCliente.setText("Nombre de Cliente");
        btnNuevoCliente.setDisable(false);
        
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
        // Agregar listener para cargar productos automáticamente cuando se seleccione una categoría
        cbxCategoriaProducto.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            actualizarTablaProductosPorCategoria();
        });
        // Configurar tabla de productos solo una vez
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecioProducto.setCellValueFactory(new PropertyValueFactory<>("precio"));
        agregarColumnaAccionAgregar(); // SOLO aquí
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

    private void actualizarTablaProductosPorCategoria() {
        Categoria categoriaSeleccionada = cbxCategoriaProducto.getSelectionModel().getSelectedItem();
        if (categoriaSeleccionada != null) {
            // Filtrar productos por la categoría seleccionada
            List<Producto> productosFiltrados = productoService.list().stream()
                .filter(p -> p.getIdCategoria() != null && p.getIdCategoria().equals(categoriaSeleccionada.getId()))
                .toList();
            tblProductos.setItems(FXCollections.observableArrayList(productosFiltrados));
        } else {
            // Si no hay categoría seleccionada, mostrar todos los productos
            cargarTodosLosProductos();
        }
    }
    
    private void cargarTodosLosProductos() {
        // Cargar todos los productos sin filtrar
        tblProductos.setItems(FXCollections.observableArrayList(productoService.list()));
    }

    private double calcularTotalPedidoPorMesa(Long idMesa) {
        try {
            // Buscar pedidos activos para la mesa
            List<Pedido> pedidos = pedidoService.list().stream()
                .filter(p -> p.getIdMesa() != null && p.getIdMesa().equals(idMesa) && 
                           ("PENDIENTE".equals(p.getEstado()) || "EN PROCESO".equals(p.getEstado())))
                .toList();
            
            double total = 0.0;
            for (Pedido pedido : pedidos) {
                // Calcular total de detalles del pedido
                List<DetallePedido> detalles = detallePedidoService.list().stream()
                    .filter(d -> d.getIdPedido() != null && d.getIdPedido().equals(pedido.getId()))
                    .toList();
                
                for (DetallePedido detalle : detalles) {
                    total += detalle.getPrecio() * detalle.getCantidad();
                }
            }
            return total;
        } catch (Exception e) {
            System.err.println("Error al calcular total de pedido por mesa: " + e.getMessage());
            return 0.0;
        }
    }

    private void inicializarMesas() {
        try {
            // Validar que el contenedor de mesas no sea null
            if (contenedorMesas == null) {
                System.err.println("Error: contenedorMesas es null");
                return;
            }
            
            // Limpiar el contenedor de mesas
            contenedorMesas.getChildren().clear();
            
            // Obtener todas las mesas
            List<Mesa> mesas = mesaService.list();
            
            if (mesas == null || mesas.isEmpty()) {
                System.out.println("No hay mesas disponibles");
                return;
            }
            
            // Crear botones para cada mesa
            int col = 0;
            int row = 0;
            int maxCols = 3; // Máximo 3 columnas
            
            for (Mesa mesa : mesas) {
                if (mesa == null) continue;
                
                // Calcular total del pedido para esta mesa
                double totalMesa = calcularTotalPedidoPorMesa(mesa.getId());
                String textoTotal = totalMesa > 0 ? String.format("\nS/ %.2f", totalMesa) : "";
                
                Button btnMesa = new Button("Mesa " + mesa.getNumero() + "\n" + mesa.getEstado() + textoTotal);
                btnMesa.setPrefSize(120, 80); // Aumentar tamaño del botón
                btnMesa.setStyle(getEstiloMesa(mesa.getEstado()));
                
                // Agregar listener para seleccionar mesa
                btnMesa.setOnAction(event -> {
                    // Si la mesa está ocupada, mostrar mensaje y no permitir selección
                    if ("ocupada".equalsIgnoreCase(mesa.getEstado())) {
                        mostrarAlerta("Mesa ocupada", "La mesa seleccionada está ocupada y no se puede seleccionar.");
                        return;
                    }
                    String dni = (txtNroDni != null) ? txtNroDni.getText().trim() : "";
                    String nombreCliente = (lblNombreCliente != null) ? lblNombreCliente.getText().trim() : "";

                    if (!dni.isEmpty() && !nombreCliente.isEmpty() && !nombreCliente.equals("Nombre de Cliente")) {
                        // Buscar si el cliente ya existe
                        Cliente cliente = buscarClienteEnBaseDatos(dni);
                        if (cliente == null) {
                            // Registrar nuevo cliente
                            cliente = Cliente.builder()
                                .idTipoDocumento(ConstantesGlobales.TIPO_DOCUMENTO_DEFAULT_ID)
                                .nroIdentidad(dni)
                                .nombres(nombreCliente)
                                .estado(true)
                                .fechaRegistro(java.time.LocalDate.now().toString())
                                .build();
                            cliente = clienteService.save(cliente);
                        }
                        clienteEncontrado = cliente;

                        // Crear el pedido si aún no existe
                        if (pedidoActual == null) {
                            mesaSeleccionada = mesa;
                            crearPedidoInicial(mesa, clienteEncontrado);
                            // Deshabilitar todos los botones de mesa
                            contenedorMesas.getChildren().forEach(node -> {
                                if (node instanceof Button) {
                                    node.setDisable(true);
                                }
                            });
                        }
                    } else {
                        mostrarAlerta("Error", "Debe ingresar el DNI y el nombre del cliente antes de seleccionar una mesa.");
                    }
                });
                
                // Agregar el botón al GridPane
                contenedorMesas.add(btnMesa, col, row);
                
                // Calcular siguiente posición
                col++;
                if (col >= maxCols) {
                    col = 0;
                    row++;
                }
            }
            
            System.out.println("Mesas inicializadas correctamente: " + mesas.size() + " mesas");
        } catch (Exception e) {
            System.err.println("Error al inicializar mesas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private String getEstiloMesa(String estado) {
        switch (estado.toLowerCase()) {
            case "disponible":
                return "-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;";
            case "en proceso":
                return "-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold;";
            case "ocupada":
                return "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;";
            case "reservada":
                return "-fx-background-color: #9b59b6; -fx-text-fill: white; -fx-font-weight: bold;";
            case "inactiva":
                return "-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold;";
            default:
                return "-fx-background-color: #bdc3c7; -fx-text-fill: black; -fx-font-weight: bold;";
        }
    }

    private void crearPedidoInicial(Mesa mesa, Cliente cliente) {
        if (pedidoActual == null) {
            System.out.println("\n=== CREANDO PEDIDO INICIAL ===");
            
            // Obtener el personal desde la sesión global
            Long userId = SessionManager.getInstance().getUserId();
            String userName = SessionManager.getInstance().getUserName();
            
            System.out.println("📋 Información de sesión:");
            System.out.println("   - User ID: " + userId);
            System.out.println("   - User Name: " + userName);
            
            Personal personal = null;
            
            if (userId != null) {
                System.out.println("🔍 Buscando personal para usuario ID: " + userId);
                personal = personalService.findByUsuarioId(userId);
                
                if (personal != null) {
                    System.out.println("✅ Personal encontrado:");
                    System.out.println("   - ID Personal: " + personal.getIdPersonal());
                    System.out.println("   - DNI: " + personal.getDni());
                    System.out.println("   - Rol: " + (personal.getRol() != null ? personal.getRol().getDescripcion() : "Sin rol"));
                } else {
                    System.out.println("❌ No se encontró personal para el usuario ID: " + userId);
                }
            } else {
                System.out.println("❌ ERROR: User ID es null en la sesión");
            }
            
            // Verificar si el usuario es root o normal
            pe.edu.upeu.sisrestaurant.modelo.Usuario usuario = null;
            if (userId != null) {
                usuario = usuarioService.findById(userId);
                if (usuario != null) {
                    System.out.println("👤 Usuario encontrado en BD:");
                    System.out.println("   - ID: " + usuario.getIduser());
                    System.out.println("   - Nombre: " + usuario.getNombre());
                    System.out.println("   - Tipo: " + usuario.getTipoUsuario());
                    System.out.println("   - Estado: " + usuario.getEstado());
                } else {
                    System.out.println("❌ Usuario no encontrado en BD para ID: " + userId);
                }
            }
            
            if (usuario != null && "root".equals(usuario.getTipoUsuario())) {
                // Usuario root puede crear pedidos sin personal
                System.out.println("👑 Usuario root detectado - No requiere registro de personal");
                personal = null; // No necesita personal
            } else if (personal == null) {
                // Usuario normal sin personal - mostrar error y no permitir crear pedido
                System.out.println("❌ ERROR: No se puede crear pedido - Usuario sin registro de personal");
                mostrarAlerta("Error de Registro", 
                    "No se puede crear el pedido porque el usuario no está registrado como personal del sistema.\n\n" +
                    "Contacte al administrador para completar su registro.");
                return;
            }
            
            // Crear el pedido
            System.out.println("📝 Creando pedido con los siguientes datos:");
            System.out.println("   - Mesa ID: " + mesa.getId() + " (Número: " + mesa.getNumero() + ")");
            System.out.println("   - Cliente ID: " + cliente.getId() + " (Nombre: " + cliente.getNombres() + ")");
            System.out.println("   - Personal ID: " + (personal != null ? personal.getIdPersonal() : "NULL (root)"));
            
            pedidoActual = Pedido.builder()
                .idMesa(mesa.getId())
                .idCliente(cliente.getId())
                .idPersonal(personal != null ? personal.getIdPersonal() : null) // Puede ser null para root
                .fechaHora(LocalDateTime.now().toString())
                .estado("PENDIENTE")
                .build();
            
            System.out.println("💾 Guardando pedido en base de datos...");
            pedidoActual = pedidoService.save(pedidoActual);
            
            if (pedidoActual != null && pedidoActual.getId() != null) {
                System.out.println("✅ Pedido guardado exitosamente con ID: " + pedidoActual.getId());
            } else {
                System.out.println("❌ ERROR: No se pudo guardar el pedido");
            }
            
            // Cambiar el estado de la mesa a "En Proceso" (anaranjado)
            mesa.setEstado("En Proceso");
            mesaService.save(mesa);
            System.out.println("🔄 Mesa " + mesa.getNumero() + " actualizada a estado 'En Proceso'");
            
            // Actualizar la tabla de mesas
            actualizarTablaMesas();
            
            System.out.println("🎉 Pedido creado exitosamente con ID: " + pedidoActual.getId());
            mostrarAlerta("Pedido creado", "Se ha creado un nuevo pedido para la mesa seleccionada.");

            // Habilitar todos los controles relevantes
            if (tblProductos != null) tblProductos.setDisable(false);
            if (tblDetallePedido != null) tblDetallePedido.setDisable(false);
            if (btnFinalizar != null) btnFinalizar.setDisable(false);
            if (btnRegistrar != null) btnRegistrar.setDisable(false);
            if (btnBuscarCliente != null) btnBuscarCliente.setDisable(false);
            if (contenedorMesas != null) contenedorMesas.setDisable(false);
            if (btnNuevoCliente != null) btnNuevoCliente.setDisable(false);
            if (cbxCategoriaProducto != null) {
                cbxCategoriaProducto.setDisable(false);
                // Seleccionar automáticamente la primera categoría y cargar productos
                if (!cbxCategoriaProducto.getItems().isEmpty()) {
                    cbxCategoriaProducto.getSelectionModel().selectFirst();
                    actualizarTablaProductosPorCategoria();
                }
            }
        } else {
            System.out.println("⚠️  Ya existe un pedido actual, no se puede crear otro");
        }
    }

    private void actualizarTablaMesas() {
        // Recargar las mesas en el GridPane con totales actualizados
        inicializarMesas();
    }

    private void inicializarDetallePedido() {
        try {
            // Validar que las columnas no sean null
            if (colProductoDetalle == null || colPrecioUnitarioDetalle == null || colCantidadDetalle == null || colSubtotalDetalle == null || colEstadoDetalle == null || colAccionEstadoDetalle == null) {
                System.err.println("Error: Una o más columnas del detalle son null");
                return;
            }
            colProductoDetalle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProducto()));
            colPrecioUnitarioDetalle.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrecioUnitario()).asObject());
            colCantidadDetalle.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCantidad()).asObject());
            colSubtotalDetalle.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getSubtotal()).asObject());
            colEstadoDetalle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstado()));
            
            // Configurar columna de acciones simplificada - solo botón X para quitar
            colAccionEstadoDetalle.setCellFactory(param -> new TableCell<DetallePedidoRow, Void>() {
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        DetallePedidoRow detalleRow = getTableView().getItems().get(getIndex());
                        
                        Button btnQuitar = new Button("X");
                        btnQuitar.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-width: 25px; -fx-max-width: 25px;");
                        btnQuitar.setTooltip(new Tooltip("Quitar producto"));
                        
                        btnQuitar.setOnAction(event -> {
                            quitarDetallePedido(detalleRow);
                        });
                        
                        setGraphic(btnQuitar);
                    }
                }
            });
            
            tblDetallePedido.setItems(detallePedidoRows);
            System.out.println("Detalle de pedido inicializado correctamente");
        } catch (Exception e) {
            System.err.println("Error al inicializar detalle de pedido: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void agregarColumnaAccionAgregar() {
        try {
            // Validar que la columna no sea null
            if (colAccionAgregar == null) {
                System.err.println("Error: colAccionAgregar es null");
                return;
            }
            
            colAccionAgregar.setCellFactory(param -> new TableCell<Producto, Void>() {
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        TextField txtCantidad = new TextField("1");
                        Button btnAgregar = new Button("Agregar");
                        HBox hbox = new HBox(5, txtCantidad, btnAgregar);
                        btnAgregar.setOnAction(event -> {
                            Producto producto = getTableView().getItems().get(getIndex());
                            int cantidad = 1;
                            try { cantidad = Integer.parseInt(txtCantidad.getText()); } catch (Exception e) { cantidad = 1; }
                            agregarProductoADetalle(producto, cantidad);
                        });
                        setGraphic(hbox);
                    }
                }
            });
            
            System.out.println("Columna de acción agregar configurada correctamente");
        } catch (Exception e) {
            System.err.println("Error al configurar columna de acción agregar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void agregarProductoADetalle(Producto producto, int cantidad) {
        try {
            if (producto == null) {
                System.err.println("Error: Producto es null");
                return;
            }
            
            if (pedidoActual == null) {
                if (mesaSeleccionada != null && clienteEncontrado != null) {
                    crearPedidoInicial(mesaSeleccionada, clienteEncontrado);
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

            // Buscar si ya existe un detalle para este producto en el pedido actual
            Optional<DetallePedido> existente = detallePedidoService.findByIdPedidoAndIdProducto(pedidoActual.getId(), producto.getId());
            if (existente.isPresent()) {
                DetallePedido detalle = existente.get();
                detalle.setCantidad(detalle.getCantidad() + cantidad);
                detallePedidoService.save(detalle);
            } else {
                DetallePedido detalle = DetallePedido.builder()
                    .idPedido(pedidoActual.getId())
                    .idProducto(producto.getId())
                    .cantidad(cantidad)
                    .precio(producto.getPrecio())
                    .fechaRegistro(java.time.LocalDateTime.now().toString())
                    .estado(EstadosDetallePedido.AGREGADO)
                    .build();
                detallePedidoService.save(detalle);
            }

            // Actualizar la tabla de detalles (puedes mejorar la lógica para evitar duplicados)
            // Recargar los detalles del pedido actual
            recargarDetallePedidoRows();

            if (tblDetallePedido != null) {
                tblDetallePedido.refresh();
            }
            
            // Actualizar los botones de mesa para mostrar el nuevo total
            actualizarBotonesMesas();
            
            System.out.println("Producto agregado correctamente: " + producto.getNombre() + " - Cantidad: " + cantidad);
        } catch (Exception e) {
            System.err.println("Error al agregar producto al detalle: " + e.getMessage());
            e.printStackTrace();
            mostrarAlerta("Error", "Error al agregar producto: " + e.getMessage());
        }
    }

    // Nuevo método para recargar los detalles del pedido actual en la tabla
    private void recargarDetallePedidoRows() {
        detallePedidoRows.clear();
        if (pedidoActual != null) {
            List<DetallePedido> detalles = detallePedidoService.list().stream()
                .filter(d -> d.getIdPedido().equals(pedidoActual.getId()))
                .filter(d -> !EstadosDetallePedido.CANCELADO.equals(d.getEstado())) // Filtrar detalles cancelados
                .toList();
            double total = 0.0;
            for (DetallePedido detalle : detalles) {
                Producto prod = productoService.list().stream()
                    .filter(p -> p.getId().equals(detalle.getIdProducto()))
                    .findFirst().orElse(null);
                String nombreProd = prod != null ? prod.getNombre() : "";
                double precioUnitario = detalle.getPrecio();
                int cantidad = detalle.getCantidad();
                double subtotal = precioUnitario * cantidad;
                String estado = detalle.getEstado() != null ? detalle.getEstado() : EstadosDetallePedido.AGREGADO;
                detallePedidoRows.add(new DetallePedidoRow(nombreProd, precioUnitario, cantidad, subtotal, estado, detalle.getId(), detalle.getIdProducto()));
                total += subtotal;
            }
            totalPedido = total;
        }
    }

    public static class DetallePedidoRow {
        private final SimpleStringProperty producto;
        private final SimpleDoubleProperty precioUnitario;
        private final SimpleIntegerProperty cantidad;
        private final SimpleDoubleProperty subtotal;
        private final SimpleStringProperty estado;
        private final Long idDetalle;
        private final Long idProducto;
        
        public DetallePedidoRow(String producto, double precioUnitario, int cantidad, double subtotal, String estado, Long idDetalle, Long idProducto) {
            this.producto = new SimpleStringProperty(producto);
            this.precioUnitario = new SimpleDoubleProperty(precioUnitario);
            this.cantidad = new SimpleIntegerProperty(cantidad);
            this.subtotal = new SimpleDoubleProperty(subtotal);
            this.estado = new SimpleStringProperty(estado);
            this.idDetalle = idDetalle;
            this.idProducto = idProducto;
        }
        public String getProducto() { return producto.get(); }
        public Double getPrecioUnitario() { return precioUnitario.get(); }
        public Integer getCantidad() { return cantidad.get(); }
        public Double getSubtotal() { return subtotal.get(); }
        public String getEstado() { return estado.get(); }
        public Long getIdDetalle() { return idDetalle; }
        public Long getIdProducto() { return idProducto; }
    }

    @FXML
    private void onBuscarCliente() {
        String dni = txtNroDni.getText().trim();
        buscarClientePorDNI(dni);
    }

    @FXML
    private void onFinalizar() {
        if (pedidoActual != null && !detallePedidoRows.isEmpty()) {
            // Obtener automáticamente el tipo de documento de venta basado en el tipo de documento global
            pe.edu.upeu.sisrestaurant.modelo.TipoDocVenta tipoDocVentaSel = obtenerTipoDocVentaAutomatico();
            if (tipoDocVentaSel == null) {
                mostrarAlerta("Error", "No se pudo determinar el tipo de documento de venta automáticamente.");
                return;
            }
            
            // Generar código de venta sumando +1 al ciglas del tipo de documento de venta
            String codigoVenta = generarCodigoVenta(tipoDocVentaSel);
            
            // Crear y guardar DocVenta con estado PENDIENTE
            pe.edu.upeu.sisrestaurant.modelo.DocVenta docVenta = pe.edu.upeu.sisrestaurant.modelo.DocVenta.builder()
                .codigoVenta(codigoVenta)
                .tipoDocVenta(tipoDocVentaSel.getNombre())
                .fechaHora(java.time.LocalDateTime.now().toString())
                .estado("PENDIENTE")
                .build();
            docVentaService.save(docVenta);
            
            // Asignar el ID del documento de venta al pedido
            pedidoActual.setIdDocVenta(docVenta.getId());
            
            // Cambiar el estado de todos los detalles del pedido a "PENDIENTE"
            cambiarEstadoDetallesPedido(EstadosDetallePedido.PENDIENTE, false);
            
            // Actualizar el estado del pedido
            pedidoActual.setEstado("FINALIZADO");
            pedidoService.save(pedidoActual);
            
            // Cambiar el estado de la mesa a "Ocupada"
            Mesa mesa = mesaService.getMesaById(pedidoActual.getIdMesa());
            if (mesa != null) {
                mesa.setEstado("Ocupada");
                mesaService.save(mesa);
            }
            
            // Actualizar los botones de mesa
            actualizarTablaMesas();
            
            mostrarAlerta("Pedido Finalizado", 
                "✅ Pedido finalizado exitosamente\n" +
                "📄 Documento de venta generado automáticamente\n" +
                "🔢 Código: " + codigoVenta + "\n" +
                "📋 Tipo: " + tipoDocVentaSel.getNombre() + " (automático)\n" +
                "💰 Total: S/ " + String.format("%.2f", totalPedido) + "\n" +
                "📊 Estado de documento: PENDIENTE\n" +
                "📊 Estado de detalles: PENDIENTE");
            
            reiniciarFormularioPedido();
        } else {
            mostrarAlerta("Error", "Debe agregar productos antes de finalizar el pedido.");
        }
    }

    @FXML
    private void onCancelar() {
        // Cerrar la pestaña actual
        cerrarPestanaActual();
    }

    private void cerrarPestanaActual() {
        try {
            // Buscar el TabPane padre de manera más robusta
            javafx.scene.Node node = btnCerrar.getScene().getRoot();
            javafx.scene.control.TabPane tabPane = null;
            
            // Buscar recursivamente el TabPane
            tabPane = buscarTabPaneRecursivamente(node);
            
            if (tabPane != null) {
                javafx.scene.control.Tab tab = tabPane.getSelectionModel().getSelectedItem();
                if (tab != null) {
                    tabPane.getTabs().remove(tab);
                    System.out.println("✅ Pestaña de Pedido cerrada exitosamente");
                }
            } else {
                System.out.println("⚠️ No se encontró TabPane para cerrar la pestaña");
                // Como fallback, intentar cerrar la ventana
                javafx.stage.Stage stage = (javafx.stage.Stage) btnCerrar.getScene().getWindow();
                if (stage != null) {
                    stage.close();
                    System.out.println("✅ Ventana cerrada como fallback");
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error al cerrar la pestaña: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private javafx.scene.control.TabPane buscarTabPaneRecursivamente(javafx.scene.Node node) {
        if (node == null) return null;
        
        if (node instanceof javafx.scene.control.TabPane) {
            return (javafx.scene.control.TabPane) node;
        }
        
        if (node instanceof javafx.scene.Parent) {
            javafx.scene.Parent parent = (javafx.scene.Parent) node;
            for (javafx.scene.Node child : parent.getChildrenUnmodifiable()) {
                javafx.scene.control.TabPane result = buscarTabPaneRecursivamente(child);
                if (result != null) {
                    return result;
                }
            }
        }
        
        return null;
    }

    private void reiniciarFormularioPedido() {
        pedidoActual = null;
        mesaSeleccionada = null;
        detallePedidoRows.clear();
        totalPedido = 0.0;
        tblDetallePedido.refresh();
        tblProductos.setDisable(true);
        tblDetallePedido.setDisable(true);
        btnFinalizar.setDisable(true);
        btnRegistrar.setDisable(false);
        btnBuscarCliente.setDisable(false);

        // Limpiar campos de cliente
        limpiarCamposCliente();
        lblNombreCliente.setText("Nombre de Cliente");
        if (txtNroDni != null) txtNroDni.clear();
        clienteEncontrado = null; // Limpiar variable interna

        // Actualizar la tabla de mesas
        actualizarTablaMesas();
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

    /**
     * Obtiene automáticamente el tipo de documento de venta basado en el tipo de documento global
     */
    private pe.edu.upeu.sisrestaurant.modelo.TipoDocVenta obtenerTipoDocVentaAutomatico() {
        try {
            // Obtener el tipo de documento actual desde PrincipalFrmController
            String tipoDocActual = obtenerTipoDocumentoActual();
            System.out.println("Tipo de documento actual: " + tipoDocActual);
            
            // Determinar el tipo de documento de venta basado en el tipo de documento
            String tipoDocVentaSeleccionado = "BOLETA"; // Por defecto
            
            if ("RUC".equalsIgnoreCase(tipoDocActual)) {
                tipoDocVentaSeleccionado = "FACTURA";
            }
            
            // Buscar el tipo de documento de venta correspondiente en la base de datos
            List<pe.edu.upeu.sisrestaurant.modelo.TipoDocVenta> tiposDocVenta = tipoDocVentaService.list();
            for (pe.edu.upeu.sisrestaurant.modelo.TipoDocVenta tipoDocVenta : tiposDocVenta) {
                if (tipoDocVentaSeleccionado.equalsIgnoreCase(tipoDocVenta.getNombre())) {
                    System.out.println("Tipo de documento de venta seleccionado automáticamente: " + tipoDocVenta.getNombre());
                    return tipoDocVenta;
                }
            }
            
            // Si no se encontró el tipo específico, retornar el primero disponible
            if (!tiposDocVenta.isEmpty()) {
                System.out.println("Seleccionado primer tipo de documento de venta disponible: " + tiposDocVenta.get(0).getNombre());
                return tiposDocVenta.get(0);
            }
            
            return null;
            
        } catch (Exception e) {
            System.err.println("Error al obtener tipo de documento de venta automáticamente: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene el tipo de documento actual desde PrincipalFrmController
     */
    private String obtenerTipoDocumentoActual() {
        try {
            // Obtener el controlador principal desde el contexto de Spring
            PrincipalFrmController principalController = context.getBean(PrincipalFrmController.class);
            return principalController.getDocSeleccionado();
        } catch (Exception e) {
            System.err.println("Error al obtener tipo de documento actual: " + e.getMessage());
            return "DNI"; // Valor por defecto
        }
    }

    private void actualizarBotonesMesas() {
        try {
            // Recorrer todos los botones en el contenedor de mesas
            for (javafx.scene.Node node : contenedorMesas.getChildren()) {
                if (node instanceof Button) {
                    Button btnMesa = (Button) node;
                    String textoActual = btnMesa.getText();
                    
                    // Extraer el número de mesa del texto actual
                    if (textoActual.startsWith("Mesa ")) {
                        String[] partes = textoActual.split("\n");
                        if (partes.length > 0) {
                            String numeroMesaStr = partes[0].replace("Mesa ", "");
                            try {
                                int numeroMesa = Integer.parseInt(numeroMesaStr);
                                
                                // Buscar la mesa en la base de datos
                                Mesa mesa = mesaService.list().stream()
                                    .filter(m -> m.getNumero() == numeroMesa)
                                    .findFirst()
                                    .orElse(null);
                                
                                if (mesa != null) {
                                    // Calcular el nuevo total
                                    double totalMesa = calcularTotalPedidoPorMesa(mesa.getId());
                                    String textoTotal = totalMesa > 0 ? String.format("\nS/ %.2f", totalMesa) : "";
                                    
                                    // Actualizar el texto del botón
                                    String nuevoTexto = "Mesa " + mesa.getNumero() + "\n" + mesa.getEstado() + textoTotal;
                                    btnMesa.setText(nuevoTexto);
                                }
                            } catch (NumberFormatException e) {
                                System.err.println("Error al parsear número de mesa: " + numeroMesaStr);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar botones de mesa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cambiarEstadoDetalle(DetallePedidoRow detalleRow, String nuevoEstado) {
        try {
            if (detalleRow.getIdDetalle() != null) {
                DetallePedido detalle = detallePedidoService.getDetallePedidoById(detalleRow.getIdDetalle());
                if (detalle != null) {
                    detalle.setEstado(nuevoEstado);
                    detallePedidoService.save(detalle);
                    
                    // Actualizar el estado en la fila de la tabla
                    detalleRow.estado.set(nuevoEstado);
                    
                    // Refrescar la tabla
                    tblDetallePedido.refresh();
                    
                    System.out.println("Estado del detalle actualizado: " + detalleRow.getProducto() + " - Nuevo estado: " + nuevoEstado);
                    
                    // Mostrar mensaje de confirmación
                    mostrarAlerta("Estado Actualizado", "El estado del producto '" + detalleRow.getProducto() + "' ha sido cambiado a: " + nuevoEstado);
                } else {
                    mostrarAlerta("Error", "No se pudo encontrar el detalle de pedido en la base de datos.");
                }
            } else {
                mostrarAlerta("Error", "No se puede cambiar el estado de un detalle sin ID.");
            }
        } catch (Exception e) {
            System.err.println("Error al cambiar estado del detalle: " + e.getMessage());
            e.printStackTrace();
            mostrarAlerta("Error", "Error al cambiar el estado: " + e.getMessage());
        }
    }

    private void cambiarEstadoDetallesPedido(String nuevoEstado, boolean mostrarAlerta) {
        try {
            if (pedidoActual != null) {
                List<DetallePedido> detalles = detallePedidoService.list().stream()
                    .filter(d -> d.getIdPedido().equals(pedidoActual.getId()))
                    .toList();
                for (DetallePedido detalle : detalles) {
                    detalle.setEstado(nuevoEstado);
                    detallePedidoService.save(detalle);
                }
                
                // Si los detalles cambian a CANCELADO, también cambiar el estado del documento de venta
                if (EstadosDetallePedido.CANCELADO.equals(nuevoEstado) && pedidoActual.getIdDocVenta() != null) {
                    pe.edu.upeu.sisrestaurant.modelo.DocVenta docVenta = docVentaService.getDocVentaById(pedidoActual.getIdDocVenta());
                    if (docVenta != null) {
                        docVenta.setEstado("CANCELADO");
                        docVentaService.save(docVenta);
                        System.out.println("Estado del documento de venta cambiado a CANCELADO: " + docVenta.getCodigoVenta());
                    }
                }
                
                recargarDetallePedidoRows();
                tblDetallePedido.refresh();
                System.out.println("Estado de todos los detalles actualizado: " + nuevoEstado);
                if (mostrarAlerta) {
                    mostrarAlerta("Estado Actualizado", "El estado de todos los detalles del pedido ha sido cambiado a: " + nuevoEstado);
                }
            } else {
                if (mostrarAlerta) {
                    mostrarAlerta("Error", "No se puede cambiar el estado de detalles sin un pedido activo.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cambiar estado de detalles del pedido: " + e.getMessage());
            e.printStackTrace();
            if (mostrarAlerta) {
                mostrarAlerta("Error", "Error al cambiar el estado: " + e.getMessage());
            }
        }
    }
    
    /**
     * Cambia el estado de todos los detalles del pedido a "EN_PROCESO"
     * Se llama cuando se está trabajando con la factura
     */
    public void iniciarProcesamientoFactura() {
        if (pedidoActual != null) {
            cambiarEstadoDetallesPedido(EstadosDetallePedido.EN_PROCESO, true);
            
            // También cambiar el estado del documento de venta a EN_PROCESO
            if (pedidoActual.getIdDocVenta() != null) {
                pe.edu.upeu.sisrestaurant.modelo.DocVenta docVenta = docVentaService.getDocVentaById(pedidoActual.getIdDocVenta());
                if (docVenta != null) {
                    docVenta.setEstado("EN_PROCESO");
                    docVentaService.save(docVenta);
                    System.out.println("Estado del documento de venta cambiado a EN_PROCESO: " + docVenta.getCodigoVenta());
                }
            }
            
            System.out.println("🔄 Iniciando procesamiento de factura - Estado: EN_PROCESO");
        }
    }
    
    /**
     * Cambia el estado de todos los detalles del pedido a "CANCELADO"
     * Se llama cuando ya se generó la factura
     */
    public void finalizarFactura() {
        if (pedidoActual != null) {
            cambiarEstadoDetallesPedido(EstadosDetallePedido.CANCELADO, true);
            
            // El estado del documento de venta ya se cambia automáticamente en cambiarEstadoDetallesPedido
            System.out.println("✅ Factura finalizada - Estado: CANCELADO");
        }
    }

    /**
     * Genera un código de venta sumando +1 al ciglas del tipo de documento de venta
     */
    private String generarCodigoVenta(pe.edu.upeu.sisrestaurant.modelo.TipoDocVenta tipoDocVenta) {
        try {
            String ciglas = tipoDocVenta.getCiglas();
            if (ciglas == null || ciglas.trim().isEmpty()) {
                // Si no hay ciglas, usar un código por defecto
                return "VTA-" + System.currentTimeMillis();
            }
            
            // Buscar el último documento de venta con este tipo para obtener el siguiente número
            List<pe.edu.upeu.sisrestaurant.modelo.DocVenta> documentosExistentes = docVentaService.list().stream()
                .filter(doc -> tipoDocVenta.getNombre().equals(doc.getTipoDocVenta()))
                .filter(doc -> doc.getCodigoVenta() != null && doc.getCodigoVenta().startsWith(ciglas))
                .toList();
            
            int siguienteNumero = 1;
            if (!documentosExistentes.isEmpty()) {
                // Encontrar el número más alto usado
                int maxNumero = documentosExistentes.stream()
                    .mapToInt(doc -> {
                        try {
                            String codigo = doc.getCodigoVenta();
                            if (codigo.startsWith(ciglas)) {
                                String numeroStr = codigo.substring(ciglas.length());
                                return Integer.parseInt(numeroStr);
                            }
                            return 0;
                        } catch (NumberFormatException e) {
                            return 0;
                        }
                    })
                    .max()
                    .orElse(0);
                siguienteNumero = maxNumero + 1;
            }
            
            // Generar el nuevo código con formato: CIGLAS + número secuencial
            String codigoVenta = ciglas + String.format("%06d", siguienteNumero);
            System.out.println("Código de venta generado: " + codigoVenta + " (Tipo: " + tipoDocVenta.getNombre() + ")");
            
            return codigoVenta;
            
        } catch (Exception e) {
            System.err.println("Error al generar código de venta: " + e.getMessage());
            e.printStackTrace();
            // En caso de error, usar timestamp como respaldo
            return "VTA-" + System.currentTimeMillis();
        }
    }

    /**
     * Quita un detalle del pedido cambiando su estado a CANCELADO
     */
    private void quitarDetallePedido(DetallePedidoRow detalleRow) {
        try {
            if (detalleRow.getIdDetalle() != null) {
                // Confirmar la acción
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmar Acción");
                alert.setHeaderText(null);
                alert.setContentText("¿Está seguro que desea quitar el producto '" + detalleRow.getProducto() + "' del pedido?");
                
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    DetallePedido detalle = detallePedidoService.getDetallePedidoById(detalleRow.getIdDetalle());
                    if (detalle != null) {
                        // Cambiar estado a CANCELADO
                        detalle.setEstado(EstadosDetallePedido.CANCELADO);
                        detallePedidoService.save(detalle);
                        
                        // Remover de la lista de la tabla
                        detallePedidoRows.remove(detalleRow);
                        
                        // Recalcular total
                        recargarDetallePedidoRows();
                        
                        // Actualizar botones de mesa
                        actualizarBotonesMesas();
                        
                        // Refrescar la tabla
                        tblDetallePedido.refresh();
                        
                        System.out.println("Producto quitado del pedido: " + detalleRow.getProducto());
                        mostrarAlerta("Producto Quitado", "El producto '" + detalleRow.getProducto() + "' ha sido quitado del pedido.");
                    } else {
                        mostrarAlerta("Error", "No se pudo encontrar el detalle de pedido en la base de datos.");
                    }
                }
            } else {
                mostrarAlerta("Error", "No se puede quitar un detalle sin ID.");
            }
        } catch (Exception e) {
            System.err.println("Error al quitar detalle del pedido: " + e.getMessage());
            e.printStackTrace();
            mostrarAlerta("Error", "Error al quitar detalle: " + e.getMessage());
        }
    }
} 