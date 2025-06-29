package pe.edu.upeu.sisrestaurant.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.sisrestaurant.modelo.*;
import pe.edu.upeu.sisrestaurant.service.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class InicioController implements Initializable {

    @Autowired
    private DocVentaService docVentaService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private MesaService mesaService;

    @Autowired
    private DetallePedidoService detallePedidoService;

    @Autowired
    private ProductoService productoService;

    @FXML
    private TableView<DocumentoVentaRow> tblDocumentosVenta;

    @FXML
    private TableColumn<DocumentoVentaRow, String> colCodigoVenta;

    @FXML
    private TableColumn<DocumentoVentaRow, String> colCliente;

    @FXML
    private TableColumn<DocumentoVentaRow, String> colMesa;

    @FXML
    private TableColumn<DocumentoVentaRow, Double> colTotal;

    @FXML
    private TableColumn<DocumentoVentaRow, String> colEstado;

    @FXML
    private TableColumn<DocumentoVentaRow, Void> colAcciones;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Button btnBuscar;

    @FXML
    private CheckBox chkConDeuda;

    @FXML
    private CheckBox chkRecurrentes;

    @FXML
    private Label lblUsuario;

    @FXML
    private Button btnActualizar;

    private ObservableList<DocumentoVentaRow> documentosVentaData = FXCollections.observableArrayList();
    private FilteredList<DocumentoVentaRow> filteredData;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("=== INICIALIZANDO INICIOCONTROLLER ===");
        
        try {
            // Verificar que los servicios estén inyectados
            System.out.println("🔍 Verificando servicios inyectados:");
            System.out.println("  - DocVentaService: " + (docVentaService != null ? "✅" : "❌"));
            System.out.println("  - PedidoService: " + (pedidoService != null ? "✅" : "❌"));
            System.out.println("  - ClienteService: " + (clienteService != null ? "✅" : "❌"));
            System.out.println("  - MesaService: " + (mesaService != null ? "✅" : "❌"));
            System.out.println("  - DetallePedidoService: " + (detallePedidoService != null ? "✅" : "❌"));
            System.out.println("  - ProductoService: " + (productoService != null ? "✅" : "❌"));
            
            // Verificar que los elementos FXML estén inyectados
            System.out.println("🔍 Verificando elementos FXML:");
            System.out.println("  - tblDocumentosVenta: " + (tblDocumentosVenta != null ? "✅" : "❌"));
            System.out.println("  - btnActualizar: " + (btnActualizar != null ? "✅" : "❌"));
            System.out.println("  - txtBuscar: " + (txtBuscar != null ? "✅" : "❌"));
            
            // Verificar datos en la base de datos
            verificarDatosEnBD();
            
            configurarTabla();
            System.out.println("✅ Tabla configurada");
            
            configurarFiltros();
            System.out.println("✅ Filtros configurados");
            
            configurarBotones();
            System.out.println("✅ Botones configurados");
            
            cargarDocumentosVenta();
            System.out.println("✅ Datos cargados inicialmente");
            
            System.out.println("=== INICIALIZACIÓN COMPLETADA ===");
            
        } catch (Exception e) {
            System.err.println("❌ ERROR en initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void verificarDatosEnBD() {
        try {
            System.out.println("\n=== VERIFICANDO DATOS EN BASE DE DATOS ===");
            
            // Verificar documentos de venta
            List<DocVenta> docVentas = docVentaService.list();
            System.out.println("📄 Documentos de venta en BD: " + docVentas.size());
            if (!docVentas.isEmpty()) {
                System.out.println("  - Primer documento: ID=" + docVentas.get(0).getId() + 
                                 ", Código=" + docVentas.get(0).getCodigoVenta() + 
                                 ", Estado=" + docVentas.get(0).getEstado());
            }
            
            // Verificar pedidos
            List<Pedido> pedidos = pedidoService.list();
            System.out.println("📋 Pedidos en BD: " + pedidos.size());
            if (!pedidos.isEmpty()) {
                System.out.println("  - Primer pedido: ID=" + pedidos.get(0).getId() + 
                                 ", Mesa=" + pedidos.get(0).getIdMesa() + 
                                 ", Cliente=" + pedidos.get(0).getIdCliente() + 
                                 ", DocVenta=" + pedidos.get(0).getIdDocVenta());
            }
            
            // Verificar clientes
            List<Cliente> clientes = clienteService.findAll();
            System.out.println("👥 Clientes en BD: " + clientes.size());
            if (!clientes.isEmpty()) {
                System.out.println("  - Primer cliente: ID=" + clientes.get(0).getId() + 
                                 ", Nombres=" + clientes.get(0).getNombres());
            }
            
            // Verificar mesas
            List<Mesa> mesas = mesaService.list();
            System.out.println("🪑 Mesas en BD: " + mesas.size());
            if (!mesas.isEmpty()) {
                System.out.println("  - Primera mesa: ID=" + mesas.get(0).getId() + 
                                 ", Número=" + mesas.get(0).getNumero());
            }
            
            // Verificar detalles de pedido
            List<DetallePedido> detalles = detallePedidoService.list();
            System.out.println("📝 Detalles de pedido en BD: " + detalles.size());
            if (!detalles.isEmpty()) {
                System.out.println("  - Primer detalle: ID=" + detalles.get(0).getId() + 
                                 ", Pedido=" + detalles.get(0).getIdPedido() + 
                                 ", Estado=" + detalles.get(0).getEstado());
            }
            
            System.out.println("=== FIN VERIFICACIÓN DE DATOS ===\n");
            
        } catch (Exception e) {
            System.err.println("❌ ERROR al verificar datos en BD: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void configurarTabla() {
        try {
            System.out.println("🔧 Configurando tabla de documentos de venta...");
            
            // Verificar que las columnas no sean null
            if (colCodigoVenta == null || colCliente == null || colMesa == null || 
                colTotal == null || colEstado == null || colAcciones == null) {
                System.err.println("❌ ERROR: Una o más columnas son null");
                System.err.println("  - colCodigoVenta: " + (colCodigoVenta != null ? "✅" : "❌"));
                System.err.println("  - colCliente: " + (colCliente != null ? "✅" : "❌"));
                System.err.println("  - colMesa: " + (colMesa != null ? "✅" : "❌"));
                System.err.println("  - colTotal: " + (colTotal != null ? "✅" : "❌"));
                System.err.println("  - colEstado: " + (colEstado != null ? "✅" : "❌"));
                System.err.println("  - colAcciones: " + (colAcciones != null ? "✅" : "❌"));
                return;
            }
            
            // Configurar las columnas
            colCodigoVenta.setCellValueFactory(new PropertyValueFactory<>("codigoVenta"));
            colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
            colMesa.setCellValueFactory(new PropertyValueFactory<>("mesa"));
            colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
            colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
            
            System.out.println("✅ CellValueFactory configurado para todas las columnas");

            // Configurar formato de total
            colTotal.setCellFactory(column -> new TableCell<DocumentoVentaRow, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.format("S/ %.2f", item));
                    }
                }
            });
            
            System.out.println("✅ Formato de total configurado");

            // Configurar columna de acciones
            configurarColumnaAcciones();
            System.out.println("✅ Columna de acciones configurada");

            // Configurar filtro de búsqueda
            filteredData = new FilteredList<>(documentosVentaData, p -> true);
            tblDocumentosVenta.setItems(filteredData);
            
            System.out.println("✅ Filtro de búsqueda configurado");
            System.out.println("✅ Items de tabla asignados: " + tblDocumentosVenta.getItems().size());
            
        } catch (Exception e) {
            System.err.println("❌ ERROR al configurar tabla: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void configurarColumnaAcciones() {
        colAcciones.setCellFactory(new Callback<TableColumn<DocumentoVentaRow, Void>, TableCell<DocumentoVentaRow, Void>>() {
            @Override
            public TableCell<DocumentoVentaRow, Void> call(TableColumn<DocumentoVentaRow, Void> param) {
                return new TableCell<DocumentoVentaRow, Void>() {
                    private final HBox buttonBox = new HBox(5);

                    {
                        buttonBox.setStyle("-fx-alignment: center;");
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            DocumentoVentaRow row = getTableView().getItems().get(getIndex());
                            buttonBox.getChildren().clear();
                            
                            // Agregar botones según el estado
                            switch (row.getEstado()) {
                                case "PENDIENTE":
                                    Button btnVerDetalle = new Button("Ver");
                                    btnVerDetalle.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                                    btnVerDetalle.setOnAction(e -> verDetallePedido(row));
                                    buttonBox.getChildren().add(btnVerDetalle);
                                    
                                    Button btnProcesar = new Button("Cancelar Pago");
                                    btnProcesar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                                    btnProcesar.setOnAction(e -> procesarDocumento(row));
                                    buttonBox.getChildren().add(btnProcesar);
                                    break;
                                    
                                case "EN_PROCESO":
                                    Button btnFinalizar = new Button("Finalizar");
                                    btnFinalizar.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                                    btnFinalizar.setOnAction(e -> finalizarDocumento(row));
                                    buttonBox.getChildren().add(btnFinalizar);
                                    
                                    Button btnVer = new Button("Ver");
                                    btnVer.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
                                    btnVer.setOnAction(e -> verDocumento(row));
                                    buttonBox.getChildren().add(btnVer);
                                    break;
                                    
                                case "CANCELADO":
                                    Button btnVerCancelado = new Button("Ver");
                                    btnVerCancelado.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");
                                    btnVerCancelado.setOnAction(e -> verDocumento(row));
                                    buttonBox.getChildren().add(btnVerCancelado);
                                    break;
                                    
                                default:
                                    Button btnVerDefault = new Button("Ver");
                                    btnVerDefault.setStyle("-fx-background-color: #607D8B; -fx-text-fill: white;");
                                    btnVerDefault.setOnAction(e -> verDocumento(row));
                                    buttonBox.getChildren().add(btnVerDefault);
                                    break;
                            }
                            
                            setGraphic(buttonBox);
                        }
                    }
                };
            }
        });
    }

    private void configurarFiltros() {
        // Filtro de búsqueda
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(documento -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                String lowerCaseFilter = newValue.toLowerCase();
                return documento.getCodigoVenta().toLowerCase().contains(lowerCaseFilter) ||
                       documento.getCliente().toLowerCase().contains(lowerCaseFilter) ||
                       documento.getMesa().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Filtro de deuda
        chkConDeuda.selectedProperty().addListener((observable, oldValue, newValue) -> {
            aplicarFiltros();
        });

        // Filtro de recurrentes
        chkRecurrentes.selectedProperty().addListener((observable, oldValue, newValue) -> {
            aplicarFiltros();
        });
    }

    private void aplicarFiltros() {
        filteredData.setPredicate(documento -> {
            // Filtro de búsqueda
            String busqueda = txtBuscar.getText();
            if (busqueda != null && !busqueda.isEmpty()) {
                String lowerCaseFilter = busqueda.toLowerCase();
                if (!documento.getCodigoVenta().toLowerCase().contains(lowerCaseFilter) &&
                    !documento.getCliente().toLowerCase().contains(lowerCaseFilter) &&
                    !documento.getMesa().toLowerCase().contains(lowerCaseFilter)) {
                    return false;
                }
            }

            // Filtro de deuda (documentos con estado PENDIENTE)
            if (chkConDeuda.isSelected() && !"PENDIENTE".equals(documento.getEstado())) {
                return false;
            }

            // Filtro de recurrentes (documentos con estado CANCELADO)
            if (chkRecurrentes.isSelected() && !"CANCELADO".equals(documento.getEstado())) {
                return false;
            }

            return true;
        });
    }

    private void cargarDocumentosVenta() {
        try {
            System.out.println("\n=== CARGANDO DOCUMENTOS DE VENTA ===");
            
            // Limpiar datos anteriores
            documentosVentaData.clear();
            System.out.println("✅ Lista de datos limpiada");
            
            // Obtener todos los documentos de venta ordenados por fecha descendente (más recientes primero)
            System.out.println("🔍 Obteniendo documentos de venta ordenados por fecha...");
            List<DocVenta> documentosVenta = docVentaService.listOrderByFechaDesc();
            System.out.println("📊 Documentos de venta encontrados: " + documentosVenta.size());
            
            if (documentosVenta.isEmpty()) {
                System.out.println("⚠️ No hay documentos de venta en la base de datos");
                return;
            }
            
            // Procesar cada documento
            for (DocVenta docVenta : documentosVenta) {
                try {
                    System.out.println("\n🔍 Procesando documento ID: " + docVenta.getId());
                    System.out.println("  - Código: " + docVenta.getCodigoVenta());
                    System.out.println("  - Estado: " + docVenta.getEstado());
                    
                    // Obtener el pedido asociado
                    System.out.println("  🔍 Buscando pedido con idDocVenta: " + docVenta.getId());
                    Pedido pedido = pedidoService.findByIdDocVenta(docVenta.getId());
                    
                    if (pedido == null) {
                        System.err.println("  ❌ Pedido no encontrado para idDocVenta: " + docVenta.getId());
                        continue;
                    }
                    
                    System.out.println("  ✅ Pedido encontrado");
                    System.out.println("    - Mesa ID: " + pedido.getIdMesa());
                    System.out.println("    - Cliente ID: " + pedido.getIdCliente());
                    
                    // Obtener cliente
                    Cliente cliente = null;
                    if (pedido.getIdCliente() != null) {
                        System.out.println("  🔍 Buscando cliente con ID: " + pedido.getIdCliente());
                        cliente = clienteService.getClienteById(pedido.getIdCliente());
                        if (cliente != null) {
                            System.out.println("  ✅ Cliente encontrado: " + cliente.getNombres());
                        } else {
                            System.err.println("  ❌ Cliente no encontrado");
                        }
                    } else {
                        System.out.println("  ⚠️ Pedido sin cliente asignado");
                    }
                    
                    // Obtener mesa
                    Mesa mesa = null;
                    if (pedido.getIdMesa() != null) {
                        System.out.println("  🔍 Buscando mesa con ID: " + pedido.getIdMesa());
                        mesa = mesaService.getMesaById(pedido.getIdMesa());
                        if (mesa != null) {
                            System.out.println("  ✅ Mesa encontrada: Mesa " + mesa.getNumero());
                        } else {
                            System.err.println("  ❌ Mesa no encontrada");
                        }
                    } else {
                        System.out.println("  ⚠️ Pedido sin mesa asignada");
                    }
                    
                    // Calcular total
                    System.out.println("  🔍 Calculando total...");
                    List<DetallePedido> detalles = detallePedidoService.list();
                    // Filtrar detalles por pedido
                    detalles = detalles.stream()
                            .filter(detalle -> pedido.getId().equals(detalle.getIdPedido()))
                            .collect(java.util.stream.Collectors.toList());
                    System.out.println("  📊 Detalles encontrados: " + detalles.size());
                    
                    double total = detalles.stream()
                            .filter(detalle -> !"cancelado".equals(detalle.getEstado()))
                            .mapToDouble(detalle -> detalle.getPrecio() * detalle.getCantidad())
                            .sum();
                    
                    System.out.println("  💰 Total calculado: S/ " + total);
                    
                    // Crear fila de datos
                    DocumentoVentaRow row = new DocumentoVentaRow(
                            docVenta.getCodigoVenta(),
                            cliente != null ? cliente.getNombres() : "Sin cliente",
                            mesa != null ? "Mesa " + mesa.getNumero() : "Sin mesa",
                            total,
                            docVenta.getEstado(),
                            docVenta.getId(),
                            pedido.getId()
                    );
                    
                    System.out.println("  ✅ Fila creada: " + row);
                    documentosVentaData.add(row);
                    
                } catch (Exception e) {
                    System.err.println("  ❌ ERROR procesando documento ID " + docVenta.getId() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            System.out.println("\n📊 RESUMEN FINAL:");
            System.out.println("  - Documentos procesados: " + documentosVentaData.size());
            System.out.println("  - Items en tabla: " + tblDocumentosVenta.getItems().size());
            System.out.println("  - Items en filteredData: " + (filteredData != null ? filteredData.size() : "null"));
            
            // Forzar actualización de la tabla
            if (tblDocumentosVenta != null) {
                tblDocumentosVenta.refresh();
                System.out.println("✅ Tabla refrescada");
            }
            
        } catch (Exception e) {
            System.err.println("❌ ERROR en cargarDocumentosVenta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private double calcularTotalPedido(Long idPedido) {
        try {
            List<DetallePedido> detalles = detallePedidoService.list().stream()
                .filter(d -> d.getIdPedido().equals(idPedido))
                .toList();
            
            return detalles.stream()
                .mapToDouble(detalle -> detalle.getPrecio() * detalle.getCantidad())
                .sum();
        } catch (Exception e) {
            System.err.println("Error al calcular total del pedido " + idPedido + ": " + e.getMessage());
            return 0.0;
        }
    }

    @FXML
    private void onBuscar() {
        aplicarFiltros();
    }

    private void procesarDocumento(DocumentoVentaRow row) {
        // Mostrar modal de confirmación
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Cancelación de Pago");
        alert.setHeaderText(null);
        alert.setContentText("¿Está seguro que desea cancelar el pago y liberar la mesa?");
        java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            try {
                DocVenta docVenta = docVentaService.getDocVentaById(row.getIdDocVenta());
                if (docVenta != null) {
                    // Cambiar estado del documento a CANCELADO
                    docVenta.setEstado("CANCELADO");
                    docVentaService.save(docVenta);
                    
                    // Obtener el pedido asociado para actualizar la mesa
                    Pedido pedido = pedidoService.findByIdDocVenta(row.getIdDocVenta());
                    if (pedido != null && pedido.getIdMesa() != null) {
                        // Obtener la mesa y cambiar su estado a "Disponible"
                        Mesa mesa = mesaService.getMesaById(pedido.getIdMesa());
                        if (mesa != null) {
                            mesa.setEstado("Disponible");
                            mesaService.save(mesa);
                            System.out.println("✅ Mesa " + mesa.getNumero() + " actualizada a estado 'Disponible'");
                        }
                    }
                    
                    // Generar PDF con los datos del pedido cancelado
                    generarPDFPedidoCancelado(row, docVenta, pedido);
                    
                    // Actualizar la tabla
                    cargarDocumentosVenta();
                    
                    mostrarAlerta("Éxito", "Pago cancelado exitosamente. Se ha generado un PDF con los detalles.");
                }
            } catch (Exception e) {
                System.err.println("Error al procesar documento: " + e.getMessage());
                e.printStackTrace();
                mostrarAlerta("Error", "Error al procesar documento: " + e.getMessage());
            }
        }
    }
    
    private void generarPDFPedidoCancelado(DocumentoVentaRow row, DocVenta docVenta, Pedido pedido) {
        try {
            // Crear el documento PDF con tamaño de boleta (80mm x 200mm aprox)
            com.itextpdf.text.Rectangle pageSize = new com.itextpdf.text.Rectangle(226, 567); // 80mm x 200mm en puntos
            com.itextpdf.text.Document document = new com.itextpdf.text.Document(pageSize, 10, 10, 10, 10);
            
            // Generar nombre de archivo único
            String fileName = "Boleta_Cancelacion_" + row.getCodigoVenta() + "_" + 
                            java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
            
            // Obtener la ruta del proyecto y crear la carpeta facturas si no existe
            String projectPath = System.getProperty("user.dir");
            String facturasPath = projectPath + "/facturas";
            java.io.File facturasDir = new java.io.File(facturasPath);
            if (!facturasDir.exists()) {
                facturasDir.mkdirs();
            }
            
            String filePath = facturasPath + "/" + fileName;
            
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(filePath));
            document.open();
            
            // Título del documento con fuentes más pequeñas para boleta
            com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font subtitleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 8, com.itextpdf.text.Font.NORMAL);
            com.itextpdf.text.Font smallFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 7, com.itextpdf.text.Font.NORMAL);
            
            // Título principal
            com.itextpdf.text.Paragraph title = new com.itextpdf.text.Paragraph("BOLETA DE CANCELACIÓN", titleFont);
            title.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            document.add(title);
            document.add(new com.itextpdf.text.Paragraph(" ")); // Espacio
            
            // Información del restaurante
            com.itextpdf.text.Font restaurantFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 9, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font infoFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 7, com.itextpdf.text.Font.NORMAL);
            
            document.add(new com.itextpdf.text.Paragraph("SISRESTAURANT", restaurantFont));
            document.add(new com.itextpdf.text.Paragraph("Av. Principal 123, Centro Comercial", infoFont));
            document.add(new com.itextpdf.text.Paragraph("RUC: 20123456789", infoFont));
            document.add(new com.itextpdf.text.Paragraph("Tel: (01) 234-5678", infoFont));
            document.add(new com.itextpdf.text.Paragraph(" ")); // Espacio
            
            // Línea separadora
            document.add(new com.itextpdf.text.Paragraph("--------------------------------", normalFont));
            document.add(new com.itextpdf.text.Paragraph(" ")); // Espacio
            
            // Información del documento
            document.add(new com.itextpdf.text.Paragraph("Código: " + row.getCodigoVenta(), subtitleFont));
            document.add(new com.itextpdf.text.Paragraph("Fecha: " + 
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), normalFont));
            document.add(new com.itextpdf.text.Paragraph("Estado: CANCELADO", normalFont));
            document.add(new com.itextpdf.text.Paragraph(" ")); // Espacio
            
            // Información del cliente y mesa
            document.add(new com.itextpdf.text.Paragraph("Cliente: " + row.getCliente(), normalFont));
            document.add(new com.itextpdf.text.Paragraph("Mesa: " + row.getMesa(), normalFont));
            document.add(new com.itextpdf.text.Paragraph(" ")); // Espacio
            
            // Detalles de productos
            document.add(new com.itextpdf.text.Paragraph("PRODUCTOS:", subtitleFont));
            
            if (pedido != null) {
                List<DetallePedido> detalles = detallePedidoService.list().stream()
                    .filter(d -> d.getIdPedido().equals(pedido.getId()))
                    .toList();
                
                if (!detalles.isEmpty()) {
                    // Crear tabla para los productos con columnas más compactas
                    com.itextpdf.text.pdf.PdfPTable table = new com.itextpdf.text.pdf.PdfPTable(3);
                    table.setWidthPercentage(100);
                    
                    // Definir anchos de columna para formato boleta
                    float[] columnWidths = {50f, 20f, 30f};
                    table.setWidths(columnWidths);
                    
                    // Encabezados de la tabla
                    table.addCell(new com.itextpdf.text.Phrase("Producto", smallFont));
                    table.addCell(new com.itextpdf.text.Phrase("Cant", smallFont));
                    table.addCell(new com.itextpdf.text.Phrase("Precio", smallFont));
                    
                    double totalGeneral = 0;
                    
                    for (DetallePedido detalle : detalles) {
                        // Obtener información del producto
                        Producto producto = productoService.list().stream()
                            .filter(p -> p.getId().equals(detalle.getIdProducto()))
                            .findFirst().orElse(null);
                        
                        if (producto != null) {
                            String nombreProducto = producto.getNombre();
                            int cantidad = detalle.getCantidad();
                            double precio = detalle.getPrecio();
                            double subtotal = precio * cantidad;
                            totalGeneral += subtotal;
                            
                            // Truncar nombre si es muy largo para boleta
                            if (nombreProducto.length() > 20) {
                                nombreProducto = nombreProducto.substring(0, 17) + "...";
                            }
                            
                            table.addCell(new com.itextpdf.text.Phrase(nombreProducto, smallFont));
                            table.addCell(new com.itextpdf.text.Phrase(String.valueOf(cantidad), smallFont));
                            table.addCell(new com.itextpdf.text.Phrase("S/ " + String.format("%.2f", subtotal), smallFont));
                        }
                    }
                    
                    document.add(table);
                    document.add(new com.itextpdf.text.Paragraph(" ")); // Espacio
                    
                    // Total
                    document.add(new com.itextpdf.text.Paragraph("TOTAL: S/ " + String.format("%.2f", totalGeneral), subtitleFont));
                } else {
                    document.add(new com.itextpdf.text.Paragraph("No hay productos", normalFont));
                }
            }
            
            document.add(new com.itextpdf.text.Paragraph(" ")); // Espacio
            
            // Nota de cancelación más compacta
            document.add(new com.itextpdf.text.Paragraph("NOTA:", subtitleFont));
            document.add(new com.itextpdf.text.Paragraph("Pedido cancelado. Mesa liberada.", smallFont));
            document.add(new com.itextpdf.text.Paragraph(" ")); // Espacio
            
            // Pie de página
            document.add(new com.itextpdf.text.Paragraph("--------------------------------", normalFont));
            document.add(new com.itextpdf.text.Paragraph("Gracias por su visita", smallFont));
            document.add(new com.itextpdf.text.Paragraph("¡Vuelva pronto!", smallFont));
            
            // Cerrar el documento
            document.close();
            
            System.out.println("✅ PDF generado exitosamente: " + filePath);
            
            // Abrir la ubicación del PDF en el explorador de archivos
            try {
                java.awt.Desktop.getDesktop().open(new java.io.File(facturasPath));
                System.out.println("✅ Ubicación del PDF abierta en el explorador");
            } catch (Exception e) {
                System.err.println("⚠️ No se pudo abrir la ubicación del PDF: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error al generar PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void finalizarDocumento(DocumentoVentaRow row) {
        try {
            DocVenta docVenta = docVentaService.getDocVentaById(row.getIdDocVenta());
            if (docVenta != null) {
                docVenta.setEstado("CANCELADO");
                docVentaService.save(docVenta);
                mostrarAlerta("Documento Finalizado", "El documento " + row.getCodigoVenta() + " ha sido finalizado");
                cargarDocumentosVenta();
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al finalizar documento: " + e.getMessage());
        }
    }

    private void verDocumento(DocumentoVentaRow row) {
        mostrarAlerta("Ver Documento", 
            "Código: " + row.getCodigoVenta() + "\n" +
            "Cliente: " + row.getCliente() + "\n" +
            "Mesa: " + row.getMesa() + "\n" +
            "Total: S/ " + String.format("%.2f", row.getTotal()) + "\n" +
            "Estado: " + row.getEstado());
    }

    private void verDetallePedido(DocumentoVentaRow row) {
        try {
            // Obtener el pedido asociado al documento de venta
            Pedido pedido = pedidoService.findByIdDocVenta(row.getIdDocVenta());
            if (pedido != null) {
                // Obtener los detalles del pedido
                List<DetallePedido> detalles = detallePedidoService.list().stream()
                    .filter(d -> d.getIdPedido().equals(pedido.getId()))
                    .toList();
                
                // Crear el contenido del panel derecho
                VBox content = new VBox(10);
                content.setPadding(new javafx.geometry.Insets(20));
                content.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-width: 1;");
                
                // Título
                Label titulo = new Label("Detalle del Pedido - " + row.getCodigoVenta());
                titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");
                
                // Información del cliente y mesa
                Label infoCliente = new Label("Cliente: " + row.getCliente());
                Label infoMesa = new Label("Mesa: " + row.getMesa());
                Label infoTotal = new Label("Total: S/ " + String.format("%.2f", row.getTotal()));
                
                // Lista de productos
                VBox productosBox = new VBox(5);
                productosBox.setStyle("-fx-background-color: #f9f9f9; -fx-padding: 10; -fx-border-color: #ddd; -fx-border-width: 1;");
                
                Label productosTitulo = new Label("Productos:");
                productosTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                productosBox.getChildren().add(productosTitulo);
                
                if (!detalles.isEmpty()) {
                    for (DetallePedido detalle : detalles) {
                        // Obtener información del producto
                        Producto producto = productoService.list().stream()
                            .filter(p -> p.getId().equals(detalle.getIdProducto()))
                            .findFirst().orElse(null);
                        
                        if (producto != null) {
                            String productoInfo = String.format("• %s - Cantidad: %d - Precio: S/ %.2f - Subtotal: S/ %.2f",
                                producto.getNombre(), detalle.getCantidad(), detalle.getPrecio(), 
                                detalle.getPrecio() * detalle.getCantidad());
                            Label productoLabel = new Label(productoInfo);
                            productoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");
                            productosBox.getChildren().add(productoLabel);
                        }
                    }
                } else {
                    Label noProductos = new Label("No hay productos registrados");
                    noProductos.setStyle("-fx-font-style: italic; -fx-text-fill: #999;");
                    productosBox.getChildren().add(noProductos);
                }
                
                // Botón cerrar
                Button btnCerrar = new Button("Cerrar");
                btnCerrar.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-padding: 8 16;");
                btnCerrar.setOnAction(e -> {
                    try {
                        org.springframework.context.ApplicationContext ctx = pe.edu.upeu.sisrestaurant.SisRestaurantApplication.getContext();
                        pe.edu.upeu.sisrestaurant.controller.PrincipalFrmController principalController = ctx.getBean(pe.edu.upeu.sisrestaurant.controller.PrincipalFrmController.class);
                        principalController.cerrarPanelDerecho();
                    } catch (Exception ex) {
                        System.err.println("Error al cerrar panel derecho: " + ex.getMessage());
                    }
                });
                
                // Agregar todos los elementos al contenido
                content.getChildren().addAll(titulo, infoCliente, infoMesa, infoTotal, productosBox, btnCerrar);
                
                // Mostrar en el panel derecho
                mostrarEnPanelDerecho(content);
                
            } else {
                mostrarAlerta("Error", "No se encontró el pedido asociado al documento de venta.");
            }
        } catch (Exception e) {
            System.err.println("Error al mostrar detalle del pedido: " + e.getMessage());
            e.printStackTrace();
            mostrarAlerta("Error", "Error al mostrar detalle del pedido: " + e.getMessage());
        }
    }
    
    private void mostrarEnPanelDerecho(javafx.scene.Node content) {
        try {
            // Obtener el controlador principal y mostrar el contenido en el panel derecho
            org.springframework.context.ApplicationContext ctx = pe.edu.upeu.sisrestaurant.SisRestaurantApplication.getContext();
            pe.edu.upeu.sisrestaurant.controller.PrincipalFrmController principalController = ctx.getBean(pe.edu.upeu.sisrestaurant.controller.PrincipalFrmController.class);
            
            // Limpiar el panel derecho y agregar el nuevo contenido
            principalController.cerrarPanelDerecho();
            javafx.scene.layout.AnchorPane rightPane = principalController.getRightPane();
            if (rightPane != null) {
                javafx.scene.layout.AnchorPane.setTopAnchor(content, 0.0);
                javafx.scene.layout.AnchorPane.setBottomAnchor(content, 0.0);
                javafx.scene.layout.AnchorPane.setLeftAnchor(content, 0.0);
                javafx.scene.layout.AnchorPane.setRightAnchor(content, 0.0);
                rightPane.getChildren().add(content);
            }
        } catch (Exception e) {
            System.err.println("Error al mostrar en panel derecho: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void actualizarDatos() {
        try {
            // Mostrar indicador de carga
            btnActualizar.setText("Actualizando...");
            btnActualizar.setDisable(true);
            
            // Recargar datos
            cargarDocumentosVenta();
            
            // Mostrar confirmación
            mostrarAlerta("Datos Actualizados", 
                "✅ La lista de documentos de venta ha sido actualizada exitosamente.\n" +
                "📊 Total de documentos: " + documentosVentaData.size() + "\n" +
                "🕒 Última actualización: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
            
            // Restaurar botón
            btnActualizar.setText("Actualizar");
            btnActualizar.setDisable(false);
            
            System.out.println("Datos actualizados exitosamente - " + documentosVentaData.size() + " documentos cargados");
            
        } catch (Exception e) {
            System.err.println("Error al actualizar datos: " + e.getMessage());
            e.printStackTrace();
            
            // Restaurar botón en caso de error
            btnActualizar.setText("Actualizar");
            btnActualizar.setDisable(false);
            
            mostrarAlerta("Error", "Error al actualizar los datos: " + e.getMessage());
        }
    }

    private void configurarBotones() {
        // Configurar botón actualizar
        if (btnActualizar != null) {
            btnActualizar.setOnAction(e -> actualizarDatos());
            btnActualizar.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        }
    }

    /**
     * Método público para actualizar datos desde otros controladores
     */
    public void actualizarDatosSilenciosamente() {
        try {
            cargarDocumentosVenta();
            System.out.println("Datos actualizados silenciosamente - " + documentosVentaData.size() + " documentos cargados");
        } catch (Exception e) {
            System.err.println("Error al actualizar datos silenciosamente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Método para refrescar la tabla cuando se hace visible
     */
    public void refrescarAlHacerVisible() {
        if (tblDocumentosVenta != null && tblDocumentosVenta.isVisible()) {
            actualizarDatosSilenciosamente();
        }
    }

    // Clase interna para representar los datos de la tabla
    public static class DocumentoVentaRow {
        private final SimpleStringProperty codigoVenta;
        private final SimpleStringProperty cliente;
        private final SimpleStringProperty mesa;
        private final SimpleDoubleProperty total;
        private final SimpleStringProperty estado;
        private final Long idDocVenta;
        private final Long idPedido;

        public DocumentoVentaRow(String codigoVenta, String cliente, String mesa, double total, String estado, Long idDocVenta, Long idPedido) {
            this.codigoVenta = new SimpleStringProperty(codigoVenta);
            this.cliente = new SimpleStringProperty(cliente);
            this.mesa = new SimpleStringProperty(mesa);
            this.total = new SimpleDoubleProperty(total);
            this.estado = new SimpleStringProperty(estado);
            this.idDocVenta = idDocVenta;
            this.idPedido = idPedido;
        }

        // Getters
        public String getCodigoVenta() { return codigoVenta.get(); }
        public String getCliente() { return cliente.get(); }
        public String getMesa() { return mesa.get(); }
        public Double getTotal() { return total.get(); }
        public String getEstado() { return estado.get(); }
        public Long getIdDocVenta() { return idDocVenta; }
        public Long getIdPedido() { return idPedido; }

        // Setters
        public void setCodigoVenta(String codigoVenta) { this.codigoVenta.set(codigoVenta); }
        public void setCliente(String cliente) { this.cliente.set(cliente); }
        public void setMesa(String mesa) { this.mesa.set(mesa); }
        public void setTotal(Double total) { this.total.set(total); }
        public void setEstado(String estado) { this.estado.set(estado); }
    }
}
