package pe.edu.upeu.sisrestaurant.controller;

import org.springframework.stereotype.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import pe.edu.upeu.sisrestaurant.service.TipoDocumentoService;
import pe.edu.upeu.sisrestaurant.modelo.TipoDocumento;
import javafx.collections.FXCollections;
import javafx.util.StringConverter;
import pe.edu.upeu.sisrestaurant.service.ClienteService;
import pe.edu.upeu.sisrestaurant.modelo.Cliente;
import java.util.regex.Pattern;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.Node;
import pe.edu.upeu.sisrestaurant.SisRestaurantApplication;
import pe.edu.upeu.sisrestaurant.config.ConstantesGlobales;
import pe.edu.upeu.sisrestaurant.controller.PrincipalFrmController;
import javafx.scene.control.TextFormatter;
import java.util.function.UnaryOperator;

@Controller
public class ClienteController {
    @FXML private ComboBox<TipoDocumento> tipoDocumentoComboBox;
    @FXML private TextField nroIdentidadField;
    @FXML private TextField nombresField;
    @FXML private TextField telefonoField;
    @FXML private TextField correoField;

    @Autowired
    private TipoDocumentoService tipoDocumentoService;

    @Autowired
    private ClienteService clienteService;

    private String dniPorDefecto;

    @FXML
    public void initialize() {
        // Llenar el ComboBox de tipo de documento desde la base de datos
        tipoDocumentoComboBox.setItems(FXCollections.observableArrayList(tipoDocumentoService.list()));
        tipoDocumentoComboBox.setConverter(new StringConverter<TipoDocumento>() {
            @Override
            public String toString(TipoDocumento object) {
                return object != null ? object.getNombre() : "";
            }
            @Override
            public TipoDocumento fromString(String string) {
                return tipoDocumentoComboBox.getItems().stream()
                        .filter(td -> td.getNombre().equals(string))
                        .findFirst().orElse(null);
            }
        });
        
        // Deshabilitar el ComboBox de tipo de documento
        tipoDocumentoComboBox.setDisable(true);
        
        // Obtener el tipo de documento seleccionado en el principal
        try {
            org.springframework.context.ApplicationContext ctx = pe.edu.upeu.sisrestaurant.SisRestaurantApplication.getContext();
            PrincipalFrmController principalController = ctx.getBean(PrincipalFrmController.class);
            String docSeleccionado = principalController.getDocSeleccionado();
            tipoDocumentoComboBox.getItems().stream()
                .filter(td -> td.getNombre().equalsIgnoreCase(docSeleccionado))
                .findFirst()
                .ifPresent(td -> tipoDocumentoComboBox.getSelectionModel().select(td));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Configurar validación del campo de número de identidad
        configurarValidacionNumeroIdentidad();
        
        // Listener para actualizar el label del formulario principal al cambiar selección
        tipoDocumentoComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            actualizarLabelDocPrincipal(newVal);
        });
    }

    private void configurarValidacionNumeroIdentidad() {
        try {
            // Obtener el tipo de documento seleccionado en el principal
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
                nroIdentidadField.setTextFormatter(textFormatter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizarValidacionNumeroIdentidad() {
        configurarValidacionNumeroIdentidad();
    }

    private void actualizarLabelDocPrincipal(TipoDocumento tipoDoc) {
        try {
            org.springframework.context.ApplicationContext ctx = SisRestaurantApplication.getContext();
            PrincipalFrmController principalController = ctx.getBean(PrincipalFrmController.class);
            if (tipoDoc != null) {
                principalController.actualizarDocSeleccionado(tipoDoc.getNombre());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void inicializarConDni(String dni) {
        this.dniPorDefecto = dni;
        if (tipoDocumentoComboBox.getItems() != null && !tipoDocumentoComboBox.getItems().isEmpty()) {
            tipoDocumentoComboBox.getItems().stream()
                .filter(td -> td.getNombre().equalsIgnoreCase(ConstantesGlobales.TIPO_DOCUMENTO_DEFAULT))
                .findFirst()
                .ifPresent(td -> tipoDocumentoComboBox.getSelectionModel().select(td));
        }
        nroIdentidadField.setText(dni);
    }

    public void inicializarConClienteCompleto(Cliente cliente) {
        if (cliente != null) {
            this.dniPorDefecto = cliente.getNroIdentidad();
            
            // Llenar todos los campos con la información del cliente
            nroIdentidadField.setText(cliente.getNroIdentidad());
            nombresField.setText(cliente.getNombres());
            telefonoField.setText(cliente.getTelefono());
            correoField.setText(cliente.getCorreo());
            
            // Seleccionar el tipo de documento correspondiente
            if (tipoDocumentoComboBox.getItems() != null && !tipoDocumentoComboBox.getItems().isEmpty()) {
                tipoDocumentoComboBox.getItems().stream()
                    .filter(td -> td.getId().equals(cliente.getIdTipoDocumento()))
                    .findFirst()
                    .ifPresent(td -> tipoDocumentoComboBox.getSelectionModel().select(td));
            }
        }
    }

    public void inicializarConDatosAPI(String dni, String nombres, String apellidoPaterno, String apellidoMaterno) {
        this.dniPorDefecto = dni;
        
        // Llenar campos con datos de la API
        nroIdentidadField.setText(dni);
        nombresField.setText(nombres + " " + apellidoPaterno + " " + apellidoMaterno);
        
        // Seleccionar el tipo de documento por defecto (DNI)
        if (tipoDocumentoComboBox.getItems() != null && !tipoDocumentoComboBox.getItems().isEmpty()) {
            tipoDocumentoComboBox.getItems().stream()
                .filter(td -> td.getNombre().equalsIgnoreCase(ConstantesGlobales.TIPO_DOCUMENTO_DEFAULT))
                .findFirst()
                .ifPresent(td -> tipoDocumentoComboBox.getSelectionModel().select(td));
        }
    }

    @FXML
    private void guardarCliente() {
        StringBuilder errores = new StringBuilder();
        if (tipoDocumentoComboBox.getValue() == null) {
            errores.append("Debe seleccionar un tipo de documento.\n");
        }
        if (nroIdentidadField.getText() == null || nroIdentidadField.getText().trim().isEmpty()) {
            errores.append("El número de identidad es obligatorio.\n");
        }
        if (nombresField.getText() == null || nombresField.getText().trim().isEmpty()) {
            errores.append("El nombre es obligatorio.\n");
        }
        // No agregamos error para teléfono y correo, solo los completamos abajo si están vacíos

        // Validar duplicado por nroIdentidad
        if (nroIdentidadField.getText() != null && !nroIdentidadField.getText().trim().isEmpty()) {
            java.util.List<Cliente> clientes = clienteService.findAll();
            boolean existe = clientes.stream().anyMatch(c -> c.getNroIdentidad().equals(nroIdentidadField.getText().trim()));
            if (existe) {
                errores.append("Ya existe un cliente con ese número de identidad.\n");
            }
        }
        if (errores.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de validación");
            alert.setHeaderText("Por favor, corrija los siguientes errores:");
            alert.setContentText(errores.toString());
            alert.showAndWait();
            return;
        }
        // Guardar cliente
        Cliente cliente = new Cliente();
        cliente.setIdTipoDocumento(tipoDocumentoComboBox.getValue().getId());
        cliente.setNroIdentidad(nroIdentidadField.getText().trim());
        // Si nombres está vacío, poner "0"
        String nombres = (nombresField.getText() == null || nombresField.getText().trim().isEmpty()) ? "" : nombresField.getText().trim();
        cliente.setNombres(nombres);
        // Si teléfono está vacío, poner "0"
        String telefono = (telefonoField.getText() == null || telefonoField.getText().trim().isEmpty()) ? "0" : telefonoField.getText().trim();
        cliente.setTelefono(telefono);
        // Si correo está vacío, poner "ejemplo@hotmail.com"
        String correo = (correoField.getText() == null || correoField.getText().trim().isEmpty()) ? "ejemplo@hotmail.com" : correoField.getText().trim();
        cliente.setCorreo(correo);
        cliente.setFechaRegistro(java.time.LocalDate.now().toString());
        cliente.setCantVisitas(0);
        cliente.setEstado(true);
        clienteService.save(cliente);
        // Actualizar el label del formulario principal con el tipo de documento seleccionado
        actualizarLabelDocPrincipal(tipoDocumentoComboBox.getValue());
        
        // Actualizar el nombre del cliente en el formulario de pedidos
        try {
            org.springframework.context.ApplicationContext ctx = SisRestaurantApplication.getContext();
            pe.edu.upeu.sisrestaurant.controller.PedidoController pedidoController = ctx.getBean(pe.edu.upeu.sisrestaurant.controller.PedidoController.class);
            pedidoController.actualizarNombreCliente(cliente);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cliente guardado");
        alert.setHeaderText(null);
        alert.setContentText("Cliente registrado correctamente.");
        alert.showAndWait();
        
        // Habilitar controles en pedidos y llenar datos del cliente
        try {
            org.springframework.context.ApplicationContext ctx = SisRestaurantApplication.getContext();
            pe.edu.upeu.sisrestaurant.controller.PedidoController pedidoController = ctx.getBean(pe.edu.upeu.sisrestaurant.controller.PedidoController.class);
            pedidoController.habilitarControlesPrincipales();
            pedidoController.llenarDatosCliente(cliente);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Cerrar el panel derecho automáticamente
        try {
            org.springframework.context.ApplicationContext ctx = SisRestaurantApplication.getContext();
            pe.edu.upeu.sisrestaurant.controller.PrincipalFrmController principalController = ctx.getBean(pe.edu.upeu.sisrestaurant.controller.PrincipalFrmController.class);
            principalController.cerrarPanelDerecho();
        } catch (Exception e) {
            e.printStackTrace();
        }
        limpiarFormulario();
    }

    @FXML
    private void abrirFormularioTipoDocumento() {
        // Aquí puedes abrir un diálogo o formulario para agregar un nuevo tipo de documento
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Agregar Tipo de Documento");
        alert.setHeaderText(null);
        alert.setContentText("Funcionalidad para agregar un nuevo tipo de documento.");
        alert.showAndWait();
    }

    @FXML
    private void limpiarFormulario() {
        tipoDocumentoComboBox.getSelectionModel().clearSelection();
        nroIdentidadField.clear();
        nombresField.clear();
        telefonoField.clear();
        correoField.clear();
    }

    @FXML
    private void cancelar() {
        // Cierra la pestaña actual si está en un TabPane
        Node node = tipoDocumentoComboBox.getScene().getRoot();
        while (node != null && !(node instanceof TabPane)) {
            node = node.getParent();
        }
        if (node instanceof TabPane) {
            TabPane tabPane = (TabPane) node;
            Tab tab = tabPane.getSelectionModel().getSelectedItem();
            tabPane.getTabs().remove(tab);
        } else {
            // Si no está en un TabPane, solo limpia
            limpiarFormulario();
        }
    }
} 