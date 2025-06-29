package pe.edu.upeu.sisrestaurant.controller;

import org.springframework.stereotype.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import pe.edu.upeu.sisrestaurant.modelo.Categoria;
import pe.edu.upeu.sisrestaurant.modelo.Producto;
import pe.edu.upeu.sisrestaurant.modelo.Seccion;
import pe.edu.upeu.sisrestaurant.service.CategoriaService;
import pe.edu.upeu.sisrestaurant.service.ProductoService;
import pe.edu.upeu.sisrestaurant.service.SeccionService;
import pe.edu.upeu.sisrestaurant.config.ConstantesGlobales;

import org.springframework.beans.factory.annotation.Autowired;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class ProductoController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private SeccionService seccionService;

    @Autowired
    private ProductoService productoService;

    @FXML
    private Label lblTitulo;

    @FXML
    private ComboBox<Categoria> cbxCategoria;

    @FXML
    private TextField txtNombreProducto;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtStock;

    @FXML
    private Button btnGuardar;

    @FXML
    private TableView<Producto> tblProductos;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, Double> colPrecio;
    
    @FXML
    private TableColumn<Producto, Integer> colStock;

    @FXML
    private TableColumn<Producto, String> colEstado;

    @FXML
    private ComboBox<Seccion> cbxSeccion;

    @FXML
    private Button btnNuevaSeccion;

    @FXML
    private TextArea txtaDescripcion;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnAplicar;

    @FXML
    private TableColumn<Producto, Void> colAcciones;

    private boolean llamadoDesdePedido = false;

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        agregarColumnaAcciones();
        actualizarTabla();

        // Cargar categorías
        cargarCategorias();
        
        // Cargar secciones
        cargarSecciones();

        // Acción para el botón de nueva sección
        btnNuevaSeccion.setOnAction(e -> abrirFormularioSeccion());
    }

    private void cargarCategorias() {
        // Cargar solo las categorías existentes en la base de datos
        cbxCategoria.setItems(javafx.collections.FXCollections.observableArrayList(categoriaService.list()));
        cbxCategoria.setConverter(new StringConverter<Categoria>() {
            @Override
            public String toString(Categoria object) {
                return object == null ? "" : object.getNombre();
            }

            @Override
            public Categoria fromString(String string) {
                return null; // No se necesita para un ComboBox no editable
            }
        });
        // Seleccionar la primera opción por defecto
        if (!cbxCategoria.getItems().isEmpty()) {
            cbxCategoria.getSelectionModel().selectFirst();
        }
    }

    private void cargarSecciones() {
        // Verificar si hay secciones en la base de datos
        if (seccionService.list().isEmpty()) {
            // Agregar secciones de prueba
            Seccion sec1 = new Seccion();
            sec1.setNombre("Terraza");
            sec1.setEstado("A");
            seccionService.save(sec1);
            
            Seccion sec2 = new Seccion();
            sec2.setNombre("Sala Principal");
            sec2.setEstado("A");
            seccionService.save(sec2);
            
            Seccion sec3 = new Seccion();
            sec3.setNombre("Sala VIP");
            sec3.setEstado("A");
            seccionService.save(sec3);
            
            Seccion sec4 = new Seccion();
            sec4.setNombre("Bar");
            sec4.setEstado("A");
            seccionService.save(sec4);
        }
        
        cbxSeccion.setItems(javafx.collections.FXCollections.observableArrayList(seccionService.list()));
        cbxSeccion.setConverter(new StringConverter<Seccion>() {
            @Override
            public String toString(Seccion object) {
                return object == null ? "" : object.getNombre();
            }

            @Override
            public Seccion fromString(String string) {
                return null; // No se necesita para un ComboBox no editable
            }
        });
        
        // Seleccionar la primera opción por defecto
        if (!cbxSeccion.getItems().isEmpty()) {
            cbxSeccion.getSelectionModel().selectFirst();
        }

        // Listener para filtrar productos por sección
        cbxSeccion.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            filtrarProductosPorSeccion();
        });
    }

    private void filtrarProductosPorSeccion() {
        Seccion seccionSeleccionada = cbxSeccion.getSelectionModel().getSelectedItem();
        if (seccionSeleccionada != null) {
            java.util.List<pe.edu.upeu.sisrestaurant.modelo.Producto> productos = productoService.list();
            java.util.List<pe.edu.upeu.sisrestaurant.modelo.Producto> filtrados = new java.util.ArrayList<>();
            for (pe.edu.upeu.sisrestaurant.modelo.Producto p : productos) {
                if (seccionSeleccionada.getId() != null && seccionSeleccionada.getId().equals(p.getIdSeccion())) {
                    filtrados.add(p);
                }
            }
            tblProductos.setItems(javafx.collections.FXCollections.observableArrayList(filtrados));
        } else {
            tblProductos.setItems(javafx.collections.FXCollections.observableArrayList(productoService.list()));
        }
    }

    private void agregarColumnaAcciones() {
        colAcciones.setCellFactory(param -> new javafx.scene.control.TableCell<Producto, Void>() {
            private final Button btnEliminar = new Button("X");
            {
                btnEliminar.setOnAction(event -> {
                    Producto producto = getTableView().getItems().get(getIndex());
                    eliminarProducto(producto);
                });
                btnEliminar.setStyle("-fx-background-color:rgb(255, 48, 24); -fx-text-fill: white;");
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnEliminar);
                }
            }
        });
    }

    private void eliminarProducto(Producto producto) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText(null);
        alert.setContentText("¿Está seguro de eliminar el producto '" + producto.getNombre() + "'?");
        java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            productoService.deleteProductoById(producto.getId());
            actualizarTabla();
        }
    }

    @FXML
    private void guardarProducto() {
        // Validación de campos
        StringBuilder errores = new StringBuilder();
        if (txtNombreProducto.getText().isEmpty()) {
            errores.append("El nombre del producto no puede estar vacío.\n");
        }
        if (cbxCategoria.getSelectionModel().getSelectedItem() == null) {
            errores.append("Debe seleccionar una categoría.\n");
        }
        if (cbxSeccion.getSelectionModel().getSelectedItem() == null) {
            errores.append("Debe seleccionar una sección.\n");
        }

        Double precio = null;
        try {
            precio = Double.parseDouble(txtPrecio.getText());
        } catch (NumberFormatException e) {
            errores.append("El precio debe ser un número válido.\n");
        }

        Integer stock = null;
        try {
            stock = Integer.parseInt(txtStock.getText());
        } catch (NumberFormatException e) {
            errores.append("El stock debe ser un número entero válido.\n");
        }
        
        if (errores.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Validación");
            alert.setHeaderText("Por favor, corrija los siguientes errores:");
            alert.setContentText(errores.toString());
            alert.showAndWait();
            return;
        }

        Producto p = new Producto();
        p.setNombre(txtNombreProducto.getText());
        p.setPrecio(precio);
        p.setStock(stock);
        p.setIdCategoria(cbxCategoria.getSelectionModel().getSelectedItem().getId());
        p.setIdSeccion(cbxSeccion.getSelectionModel().getSelectedItem().getId());
        p.setEstado("activo");

        productoService.save(p);
        
        // Guardar la sección seleccionada en la variable global
        ConstantesGlobales.setSeccionSeleccionada(cbxSeccion.getSelectionModel().getSelectedItem());
        
        filtrarProductosPorSeccion();
        limpiarCampos();
    }

    private void limpiarCampos() {
        txtNombreProducto.clear();
        txtPrecio.clear();
        txtStock.clear();
        txtaDescripcion.clear();
        
        // Seleccionar las primeras opciones en lugar de limpiar
        if (!cbxCategoria.getItems().isEmpty()) {
            cbxCategoria.getSelectionModel().selectFirst();
        }
        // Mantener la sección seleccionada (no limpiar)
        // La sección se mantiene como está
    }

    private void abrirFormularioSeccion() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/views/SeccionForm.fxml"));
            org.springframework.context.ApplicationContext ctx = pe.edu.upeu.sisrestaurant.SisRestaurantApplication.getContext();
            loader.setControllerFactory(ctx::getBean);
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Registrar Sección");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();
            // Actualizar combo de secciones después de cerrar
            cargarSecciones();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void actualizarTabla() {
        filtrarProductosPorSeccion();
    }

    @FXML
    private void onAplicar() {
        // Guardar cambios del producto si corresponde (puedes agregar lógica de edición aquí)
        actualizarTabla();
        // Actualizar la tabla de productos en el formulario de pedidos si está abierto
        try {
            org.springframework.context.ApplicationContext ctx = pe.edu.upeu.sisrestaurant.SisRestaurantApplication.getContext();
            pe.edu.upeu.sisrestaurant.controller.PedidoController pedidoController = ctx.getBean(pe.edu.upeu.sisrestaurant.controller.PedidoController.class);
            pedidoController.actualizarProductosDesdeFormulario();
        } catch (Exception e) {
            // Si no está abierto el formulario de pedidos, no hacer nada
        }
        // Si fue llamado desde Pedido, cerrar la ventana
        if (llamadoDesdePedido) {
            cerrarPestanaActual();
        }
    }

    private void cerrarPestanaActual() {
        try {
            javafx.scene.Node node = btnAplicar.getScene().getRoot();
            while (node != null && !(node instanceof javafx.scene.control.TabPane)) {
                node = node.getParent();
            }
            if (node instanceof javafx.scene.control.TabPane) {
                javafx.scene.control.TabPane tabPane = (javafx.scene.control.TabPane) node;
                javafx.scene.control.Tab tab = tabPane.getSelectionModel().getSelectedItem();
                tabPane.getTabs().remove(tab);
            }
        } catch (Exception e) {
            // Si no está en un TabPane, no hacer nada
        }
    }

    public void setLlamadoDesdePedido(boolean valor) {
        this.llamadoDesdePedido = valor;
    }

    @FXML
    private void importarExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos Excel", "*.xlsx"));
        
        File initialDirectory = new File("src/main/resources/doc");
        if (initialDirectory.exists() && initialDirectory.isDirectory()) {
            fileChooser.setInitialDirectory(initialDirectory);
        }

        File file = fileChooser.showOpenDialog(null);

        if (file == null) {
            return;
        }

        try (FileInputStream fis = new FileInputStream(file); Workbook workbook = WorkbookFactory.create(fis)) {
            
            // Verificar que el archivo tenga al menos una hoja
            if (workbook.getNumberOfSheets() == 0) {
                mostrarAlerta("Error", "El archivo no contiene hojas.");
                return;
            }

            importarProductosDeExcel(workbook);
            actualizarTabla();
            
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al importar productos: " + e.getMessage());
        }
    }

    private void importarProductosDeExcel(Workbook workbook) {
        DataFormatter formatter = new DataFormatter();
        AtomicInteger productosImportados = new AtomicInteger(0);
        AtomicInteger productosDuplicados = new AtomicInteger(0);
        AtomicInteger seccionesOmitidas = new AtomicInteger(0);
        AtomicInteger categoriasOmitidas = new AtomicInteger(0);

        List<Seccion> seccionesExistentes = seccionService.list();
        List<Categoria> categoriasExistentes = categoriaService.list();

        // Recorrer todas las hojas (cada hoja es una sección)
        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            String nombreSeccion = sheet.getSheetName();
            
            if (nombreSeccion == null || nombreSeccion.trim().isEmpty()) {
                continue;
            }

            // Buscar la sección existente
            Optional<Seccion> seccionOpt = seccionesExistentes.stream()
                .filter(s -> s.getNombre().equalsIgnoreCase(nombreSeccion.trim()))
                .findFirst();
            if (seccionOpt.isEmpty()) {
                seccionesOmitidas.incrementAndGet();
                continue; // Omitir hoja
            }

            // Procesar las categorías en la hoja, solo si existen
            procesarCategoriasEnHoja(sheet, seccionOpt.get(), formatter, productosImportados, productosDuplicados, categoriasOmitidas, categoriasExistentes);
        }

        mostrarAlerta("Importación Completada", 
            "Productos importados: " + productosImportados.get() + "\n" +
            "Productos duplicados omitidos: " + productosDuplicados.get() + "\n" +
            "Secciones omitidas: " + seccionesOmitidas.get() + "\n" +
            "Categorías omitidas: " + categoriasOmitidas.get());
    }

    private void procesarCategoriasEnHoja(Sheet sheet, Seccion seccion, DataFormatter formatter, 
                                         AtomicInteger productosImportados, AtomicInteger productosDuplicados, AtomicInteger categoriasOmitidas, List<Categoria> categoriasExistentes) {
        
        Row headerRow = sheet.getRow(0); // Primera fila con nombres de categorías
        if (headerRow == null) return;

        Row subHeaderRow = sheet.getRow(1); // Segunda fila con "Nombre", "Precio", "Stock"
        if (subHeaderRow == null) return;

        // Encontrar las columnas de cada categoría (grupos de 3 columnas)
        for (int colIndex = 0; colIndex < headerRow.getLastCellNum(); colIndex += 3) {
            Cell categoriaCell = headerRow.getCell(colIndex);
            if (categoriaCell == null || categoriaCell.getCellType() == CellType.BLANK) {
                continue;
            }

            String nombreCategoria = formatter.formatCellValue(categoriaCell);
            if (nombreCategoria.isEmpty()) {
                continue;
            }

            // Buscar la categoría existente
            Optional<Categoria> categoriaOpt = categoriasExistentes.stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombreCategoria.trim()))
                .findFirst();
            if (categoriaOpt.isEmpty()) {
                categoriasOmitidas.incrementAndGet();
                continue; // Omitir grupo de columnas
            }

            // Procesar productos de esta categoría
            procesarProductosDeCategoria(sheet, seccion, categoriaOpt.get(), colIndex, formatter, productosImportados, productosDuplicados);
        }
    }

    private void procesarProductosDeCategoria(Sheet sheet, Seccion seccion, Categoria categoria, 
                                            int colInicio, DataFormatter formatter, 
                                            AtomicInteger productosImportados, AtomicInteger productosDuplicados) {
        
        // Empezar desde la fila 2 (después de los encabezados)
        for (int rowIndex = 2; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row dataRow = sheet.getRow(rowIndex);
            if (dataRow == null) continue;

            try {
                // Leer datos de las 3 columnas de la categoría
                String nombre = formatter.formatCellValue(dataRow.getCell(colInicio));
                String precioStr = formatter.formatCellValue(dataRow.getCell(colInicio + 1));
                String stockStr = formatter.formatCellValue(dataRow.getCell(colInicio + 2));

                // Validar que no estén vacíos los campos obligatorios
                if (nombre.isEmpty() || precioStr.isEmpty()) {
                    continue;
                }

                // Verificar si el producto ya existe
                if (productoService.findByNombre(nombre).isPresent()) {
                    productosDuplicados.incrementAndGet();
                    continue;
                }

                // Crear producto
                Producto producto = new Producto();
                producto.setNombre(nombre);
                producto.setPrecio(Double.parseDouble(precioStr));
                producto.setStock(stockStr.isEmpty() ? 0 : Integer.parseInt(stockStr));
                producto.setIdCategoria(categoria.getId());
                producto.setIdSeccion(seccion.getId());
                producto.setEstado("activo");

                productoService.save(producto);
                productosImportados.incrementAndGet();

            } catch (Exception e) {
                System.err.println("Error procesando fila " + (rowIndex + 1) + " en categoría " + categoria.getNombre() + ": " + e.getMessage());
            }
        }
    }

    @FXML
    private void generarPlantillaExcel() {
        File file = new File("src/main/resources/doc/plantilla_productos.xlsx");

        try (Workbook workbook = new XSSFWorkbook()) {
            // Obtener secciones y categorías existentes
            List<Seccion> secciones = seccionService.list();
            List<Categoria> categorias = categoriaService.list();

            if (secciones.isEmpty() || categorias.isEmpty()) {
                mostrarAlerta("Advertencia", "No existen secciones o categorías en la base de datos. Agregue primero para poder generar la plantilla.");
                return;
            }

            // Crear una hoja por cada sección existente
            for (Seccion seccion : secciones) {
                crearHojaSeccion(workbook, seccion, categorias);
            }

            // Guardar archivo
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }

            mostrarAlerta("Éxito", "Plantilla generada en: " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al generar plantilla: " + e.getMessage());
        }
    }

    private void crearHojaSeccion(Workbook workbook, Seccion seccion, List<Categoria> categorias) {
        Sheet sheet = workbook.createSheet(seccion.getNombre());

        // Crear fila de encabezados de categorías (fila 0)
        Row headerRow = sheet.createRow(0);
        
        // Crear fila de sub-encabezados (fila 1)
        Row subHeaderRow = sheet.createRow(1);

        // Estilo para encabezados
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Agregar categorías en grupos de 3 columnas
        int colIndex = 0;
        for (Categoria categoria : categorias) {
            // Encabezado de categoría (fila 0)
            Cell categoriaCell = headerRow.createCell(colIndex);
            categoriaCell.setCellValue(categoria.getNombre());
            categoriaCell.setCellStyle(headerStyle);
            
            // Sub-encabezados (fila 1)
            Cell nombreCell = subHeaderRow.createCell(colIndex);
            nombreCell.setCellValue("Nombre");
            nombreCell.setCellStyle(headerStyle);
            
            Cell precioCell = subHeaderRow.createCell(colIndex + 1);
            precioCell.setCellValue("Precio");
            precioCell.setCellStyle(headerStyle);
            
            Cell stockCell = subHeaderRow.createCell(colIndex + 2);
            stockCell.setCellValue("Stock");
            stockCell.setCellStyle(headerStyle);

            colIndex += 3;
        }

        // Autoajustar columnas
        for (int i = 0; i < colIndex; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
} 