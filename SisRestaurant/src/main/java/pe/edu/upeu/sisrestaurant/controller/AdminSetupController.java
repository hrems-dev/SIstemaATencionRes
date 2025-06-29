package pe.edu.upeu.sisrestaurant.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.sisrestaurant.SisRestaurantApplication;
import pe.edu.upeu.sisrestaurant.service.CategoriaService;
import pe.edu.upeu.sisrestaurant.service.TipoDocumentoService;
import pe.edu.upeu.sisrestaurant.service.RolService;
import pe.edu.upeu.sisrestaurant.service.MesaService;
import pe.edu.upeu.sisrestaurant.service.SeccionService;
import pe.edu.upeu.sisrestaurant.service.TipoDocVentaService;
import pe.edu.upeu.sisrestaurant.service.UsuarioService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import javafx.stage.FileChooser;
import java.io.FileInputStream;
import java.util.Iterator;
import pe.edu.upeu.sisrestaurant.modelo.Categoria;
import pe.edu.upeu.sisrestaurant.modelo.Mesa;
import pe.edu.upeu.sisrestaurant.modelo.Seccion;
import pe.edu.upeu.sisrestaurant.modelo.TipoDocumento;
import pe.edu.upeu.sisrestaurant.modelo.Rol;
import pe.edu.upeu.sisrestaurant.modelo.TipoDocVenta;
import javafx.scene.control.Alert.AlertType;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import pe.edu.upeu.sisrestaurant.dto.SessionManager;
import javafx.scene.control.Button;

@Controller
public class AdminSetupController {
    
    @Autowired
    private ApplicationContext context;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private TipoDocumentoService tipoDocumentoService;
    @Autowired
    private RolService rolService;
    @Autowired
    private MesaService mesaService;
    @Autowired
    private SeccionService seccionService;
    @Autowired
    private TipoDocVentaService tipoDocVentaService;
    @Autowired
    private UsuarioService usuarioService;

    @FXML private Label lblCategoriasCount;
    @FXML private Label lblTipoDocCount;
    @FXML private Label lblRolesCount;
    @FXML private Label lblMesasCount;
    @FXML private Label lblSeccionesCount;
    @FXML private Label lblTipoDocVentaCount;
    @FXML private Label lblUsuariosCount;
    @FXML private Button btnIrAlSistema;

    @FXML
    public void initialize() {
        actualizarContadores();
        generarPlantillaExcel();
    }

    @FXML
    private void actualizarContadores() {
        lblCategoriasCount.setText(String.valueOf(categoriaService.list().size()));
        lblTipoDocCount.setText(String.valueOf(tipoDocumentoService.list().size()));
        lblRolesCount.setText(String.valueOf(rolService.list().size()));
        lblMesasCount.setText(String.valueOf(mesaService.list().size()));
        lblSeccionesCount.setText(String.valueOf(seccionService.list().size()));
        lblTipoDocVentaCount.setText(String.valueOf(tipoDocVentaService.list().size()));
        lblUsuariosCount.setText(String.valueOf(usuarioService.findAll().size()));
    }
    
    @FXML
    private void abrirCategorias() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CategoriaForm.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent categoriaContent = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestión de Categorías");
            stage.setScene(new Scene(categoriaContent));
            stage.show();
        } catch (Exception e) {
            mostrarMensaje("Error al abrir formulario de Categorías: " + e.getMessage());
        }
    }
    
    @FXML
    private void abrirTipoDocumentos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/TipoDocumentoForm.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent tipoDocContent = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestión de Tipos de Documento");
            stage.setScene(new Scene(tipoDocContent));
            stage.show();
        } catch (Exception e) {
            mostrarMensaje("Error al abrir formulario de Tipos de Documento: " + e.getMessage());
        }
    }
    
    @FXML
    private void abrirRol() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/RolForm.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent rolContent = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestión de Roles");
            stage.setScene(new Scene(rolContent));
            stage.show();
        } catch (Exception e) {
            mostrarMensaje("Error al abrir formulario de Roles: " + e.getMessage());
        }
    }
    
    @FXML
    private void abrirMesas() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MesaForm.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent mesaContent = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestión de Mesas");
            stage.setScene(new Scene(mesaContent));
            stage.show();
        } catch (Exception e) {
            mostrarMensaje("Error al abrir formulario de Mesas: " + e.getMessage());
        }
    }
    
    @FXML
    private void abrirSeccion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/SeccionForm.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent seccionContent = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestión de Secciones");
            stage.setScene(new Scene(seccionContent));
            stage.show();
        } catch (Exception e) {
            mostrarMensaje("Error al abrir formulario de Secciones: " + e.getMessage());
        }
    }
    
    @FXML
    private void abrirTipoDocVenta() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/TipoDocVentaForm.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent tipoDocVentaContent = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestión de Tipos de Documento de Venta");
            stage.setScene(new Scene(tipoDocVentaContent));
            stage.show();
        } catch (Exception e) {
            mostrarMensaje("Error al abrir formulario de Tipos de Documento de Venta: " + e.getMessage());
        }
    }
    
    @FXML
    private void abrirUsuario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PersonalForm.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent personalContent = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestión de Personal");
            stage.setScene(new Scene(personalContent));
            stage.show();
        } catch (Exception e) {
            mostrarMensaje("Error al abrir formulario de Personal: " + e.getMessage());
        }
    }
    
    @FXML
    private void abrirProducto() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ProductoForm.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent productoContent = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestión de Productos");
            stage.setScene(new Scene(productoContent));
            stage.show();
        } catch (Exception e) {
            mostrarMensaje("Error al abrir formulario de Productos: " + e.getMessage());
        }
    }
    
    @FXML
    private void irAlSistema() {
        try {
            // Limpiar la sesión
            SessionManager.getInstance().setUserId(null);
            SessionManager.getInstance().setUserName(null);
            // Abrir login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            loader.setControllerFactory(context::getBean);
            Parent loginRoot = loader.load();
            Stage loginStage = new Stage();
            loginStage.setTitle("Iniciar Sesión");
            loginStage.setScene(new Scene(loginRoot));
            loginStage.show();
            // Cerrar la ventana actual usando un nodo seguro
            Stage stage = (Stage) lblCategoriasCount.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            mostrarMensaje("Error al ir al login: " + e.getMessage());
        }
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
            
            Sheet sheet = workbook.getSheet("ImportacionGeneral");
            if (sheet == null) {
                mostrarAlerta("Error", "No se encontró la hoja 'ImportacionGeneral' en el archivo.");
                return;
            }

            importarDatosDeHojaHorizontal(sheet);

            mostrarAlerta("Éxito", "Datos importados correctamente.");
            actualizarContadores();
            
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al importar datos: " + e.getMessage());
        }
    }
    
    private void importarDatosDeHojaHorizontal(Sheet sheet) {
        DataFormatter formatter = new DataFormatter();
        Map<String, Integer> sectionColumns = new HashMap<>();
        Row titleRow = sheet.getRow(0);
        if (titleRow == null) {
            mostrarAlerta("Error", "Formato de plantilla incorrecto: no se encontró la fila de títulos.");
            return;
        }

        for (Cell cell : titleRow) {
            if (cell != null && cell.getCellType() == CellType.STRING) {
                String cellValue = cell.getStringCellValue();
                if (cellValue.startsWith("---") && cellValue.endsWith("---")) {
                    sectionColumns.put(cellValue, cell.getColumnIndex());
                }
            }
        }

        for (int i = 2; i <= sheet.getLastRowNum(); i++) {
            Row dataRow = sheet.getRow(i);
            if (dataRow == null) continue;

            for (Map.Entry<String, Integer> entry : sectionColumns.entrySet()) {
                String sectionName = entry.getKey();
                int startCol = entry.getValue();
                
                Cell firstCell = dataRow.getCell(startCol);
                if (firstCell == null || firstCell.getCellType() == CellType.BLANK) {
                    continue; 
                }
                
                try {
                    switch (sectionName) {
                        case "--- CATEGORIAS ---":
                            String nombreCategoria = formatter.formatCellValue(dataRow.getCell(startCol));
                            if (nombreCategoria.isEmpty() || categoriaService.findByNombre(nombreCategoria).isPresent()) continue;
                            Categoria categoria = new Categoria();
                            categoria.setNombre(nombreCategoria);
                            categoria.setEstado("Activo"); 
                            categoriaService.save(categoria);
                            break;
                        case "--- MESAS ---":
                            String numeroMesaStr = formatter.formatCellValue(dataRow.getCell(startCol));
                            if (numeroMesaStr.isEmpty()) continue;
                            int numeroMesa = Integer.parseInt(numeroMesaStr);
                            if (mesaService.findByNumero(numeroMesa).isPresent()) continue;
                            
                            String capacidadMesaStr = formatter.formatCellValue(dataRow.getCell(startCol + 1));
                            Mesa mesa = new Mesa();
                            mesa.setNumero(numeroMesa);
                            mesa.setCapacidad(Integer.parseInt(capacidadMesaStr));
                            mesa.setEstado("Disponible");
                            mesaService.save(mesa);
                            break;
                        case "--- SECCIONES ---":
                            String nombreSeccion = formatter.formatCellValue(dataRow.getCell(startCol));
                            if (nombreSeccion.isEmpty() || seccionService.findByNombre(nombreSeccion).isPresent()) continue;
                            Seccion seccion = new Seccion();
                            seccion.setNombre(nombreSeccion);
                            seccion.setEstado("Activo");
                            seccionService.save(seccion);
                            break;
                        case "--- TIPO DOCUMENTO ---":
                            String nombreTipoDoc = formatter.formatCellValue(dataRow.getCell(startCol));
                            if (nombreTipoDoc.isEmpty() || tipoDocumentoService.findByNombre(nombreTipoDoc).isPresent()) continue;
                            
                            String cantValTipoDoc = formatter.formatCellValue(dataRow.getCell(startCol + 1));
                            TipoDocumento tipoDoc = new TipoDocumento();
                            tipoDoc.setNombre(nombreTipoDoc);
                            tipoDoc.setCantVal(Integer.parseInt(cantValTipoDoc));
                            tipoDocumentoService.save(tipoDoc);
                            break;
                        case "--- ROLES ---":
                            String descRol = formatter.formatCellValue(dataRow.getCell(startCol));
                            if (descRol.isEmpty() || rolService.findByDescripcion(descRol).isPresent()) continue;
                            Rol rol = new Rol();
                            rol.setDescripcion(descRol);
                            rol.setEstado(true);
                            rolService.save(rol);
                            break;
                        case "--- TIPO DOC VENTA ---":
                            String nombreTipoDocVenta = formatter.formatCellValue(dataRow.getCell(startCol));
                            if (nombreTipoDocVenta.isEmpty() || tipoDocVentaService.findByNombre(nombreTipoDocVenta).isPresent()) continue;
                            
                            String ciglasTipoDocVenta = formatter.formatCellValue(dataRow.getCell(startCol + 1));
                            TipoDocVenta tipoDocVenta = new TipoDocVenta();
                            tipoDocVenta.setNombre(nombreTipoDocVenta);
                            tipoDocVenta.setCiglas(ciglasTipoDocVenta);
                            tipoDocVenta.setEstado("Activo");
                            tipoDocVentaService.save(tipoDocVenta);
                            break;
                    }
                } catch (Exception e) {
                     System.err.println("Error procesando fila: " + dataRow.getRowNum() + " en seccion: " + sectionName);
                     e.printStackTrace();
                }
            }
        }
    }
    
    private void generarPlantillaExcel() {
        File file = new File("src/main/resources/doc/plantilla_importacion.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("ImportacionGeneral");
        DataValidationHelper dvHelper = sheet.getDataValidationHelper();
        int colIndex = 0;

        // --- Crear Encabezados de Secciones ---
        colIndex = crearSeccionHorizontal(sheet, dvHelper, 0, "--- CATEGORIAS ---", new String[]{"Nombre"}, null, -1);
        colIndex = crearSeccionHorizontal(sheet, dvHelper, colIndex, "--- MESAS ---", new String[]{"Numero", "Capacidad"}, null, -1);
        colIndex = crearSeccionHorizontal(sheet, dvHelper, colIndex, "--- SECCIONES ---", new String[]{"Nombre"}, null, -1);
        colIndex = crearSeccionHorizontal(sheet, dvHelper, colIndex, "--- TIPO DOCUMENTO ---", new String[]{"Nombre", "CantVal"}, null, -1);
        colIndex = crearSeccionHorizontal(sheet, dvHelper, colIndex, "--- ROLES ---", new String[]{"Descripcion"}, null, -1);
        colIndex = crearSeccionHorizontal(sheet, dvHelper, colIndex, "--- TIPO DOC VENTA ---", new String[]{"Nombre", "Ciglas"}, null, -1);

        // --- Rellenar Datos de la Base de Datos ---
        int maxRows = 0;

        // Obtener datos y calcular el máximo de filas
        List<Categoria> categorias = categoriaService.list();
        maxRows = Math.max(maxRows, categorias.size());
        
        List<Mesa> mesas = mesaService.list();
        maxRows = Math.max(maxRows, mesas.size());

        List<Seccion> secciones = seccionService.list();
        maxRows = Math.max(maxRows, secciones.size());

        List<TipoDocumento> tipoDocumentos = tipoDocumentoService.list();
        maxRows = Math.max(maxRows, tipoDocumentos.size());

        List<Rol> roles = rolService.list();
        maxRows = Math.max(maxRows, roles.size());

        List<TipoDocVenta> tipoDocVentas = tipoDocVentaService.list();
        maxRows = Math.max(maxRows, tipoDocVentas.size());

        // Escribir los datos fila por fila
        for (int i = 0; i < maxRows; i++) {
            Row dataRow = sheet.getRow(i + 2);
            if(dataRow == null) dataRow = sheet.createRow(i + 2);
            
            colIndex = 0;
            if (i < categorias.size()) {
                dataRow.createCell(colIndex).setCellValue(categorias.get(i).getNombre());
            }
            colIndex += 2; // Siguiente sección (Nombre + 1 columna de espacio)

            if (i < mesas.size()) {
                Mesa mesa = mesas.get(i);
                dataRow.createCell(colIndex).setCellValue(mesa.getNumero());
                dataRow.createCell(colIndex + 1).setCellValue(mesa.getCapacidad());
            }
            colIndex += 3; // Siguiente sección (Numero, Capacidad + 1)
            
            if (i < secciones.size()) {
                dataRow.createCell(colIndex).setCellValue(secciones.get(i).getNombre());
            }
            colIndex += 2;
            
            if (i < tipoDocumentos.size()) {
                TipoDocumento tipoDoc = tipoDocumentos.get(i);
                dataRow.createCell(colIndex).setCellValue(tipoDoc.getNombre());
                dataRow.createCell(colIndex + 1).setCellValue(tipoDoc.getCantVal());
            }
            colIndex += 3;

            if (i < roles.size()) {
                dataRow.createCell(colIndex).setCellValue(roles.get(i).getDescripcion());
            }
            colIndex += 2;

            if (i < tipoDocVentas.size()) {
                TipoDocVenta tipoDocVenta = tipoDocVentas.get(i);
                dataRow.createCell(colIndex).setCellValue(tipoDocVenta.getNombre());
                dataRow.createCell(colIndex + 1).setCellValue(tipoDocVenta.getCiglas());
            }
            colIndex += 3; // Siguiente sección (Nombre, Ciglas + 1)
        }
        
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int crearSeccionHorizontal(Sheet sheet, DataValidationHelper dvHelper, int startCol, String titulo, String[] headers, String[] validationList, int validationLocalCol) {
        Row titleRow = sheet.getRow(0);
        if (titleRow == null) titleRow = sheet.createRow(0);
        titleRow.createCell(startCol).setCellValue(titulo);

        Row headerRow = sheet.getRow(1);
        if (headerRow == null) headerRow = sheet.createRow(1);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(startCol + i).setCellValue(headers[i]);
        }

        if (validationList != null && validationLocalCol >= 0) {
            DataValidationConstraint constraint = dvHelper.createExplicitListConstraint(validationList);
            CellRangeAddressList addressList = new CellRangeAddressList(2, 500, startCol + validationLocalCol, startCol + validationLocalCol);
            DataValidation validation = dvHelper.createValidation(constraint, addressList);
            sheet.addValidationData(validation);
        }
        
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(startCol + i);
        }

        return startCol + headers.length + 1;
    }
    
    private void mostrarMensaje(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Panel de Administración");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
} 