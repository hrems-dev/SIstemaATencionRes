package pe.edu.upeu.sisrestaurant.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GeneradorPlantillaProductos {
    
    public static void main(String[] args) {
        generarPlantillaProductos();
    }
    
    public static void generarPlantillaProductos() {
        File file = new File("src/main/resources/doc/plantilla_productos.xlsx");

        try (Workbook workbook = new XSSFWorkbook()) {
            
            // Crear hojas para diferentes secciones
            String[] secciones = {"Terraza", "Sala Principal", "Bar", "Sala VIP"};
            String[] categorias = {"Bebidas", "Comidas", "Postres", "Ensaladas"};
            
            for (String seccion : secciones) {
                crearHojaSeccion(workbook, seccion, categorias);
            }

            // Guardar archivo
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }

            System.out.println("Plantilla generada exitosamente en: " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al generar plantilla: " + e.getMessage());
        }
    }
    
    private static void crearHojaSeccion(Workbook workbook, String seccion, String[] categorias) {
        Sheet sheet = workbook.createSheet(seccion);

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
        for (String categoria : categorias) {
            // Encabezado de categoría (fila 0)
            Cell categoriaCell = headerRow.createCell(colIndex);
            categoriaCell.setCellValue(categoria);
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

        // Agregar datos de ejemplo
        agregarDatosEjemplo(sheet, categorias);

        // Autoajustar columnas
        for (int i = 0; i < colIndex; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    private static void agregarDatosEjemplo(Sheet sheet, String[] categorias) {
        // Datos de ejemplo por categoría
        String[][] datosEjemplo = {
            {"Coca Cola", "5.50", "100", "Hamburguesa", "15.00", "50", "Helado", "6.00", "20", "César", "12.00", "15"},
            {"Cerveza", "8.00", "75", "Pizza", "20.00", "30", "Torta", "8.00", "15", "Mixta", "10.00", "20"},
            {"Agua", "3.00", "50", "Pollo", "18.00", "40", "Flan", "5.50", "10", "Griega", "11.00", "12"},
            {"Jugo", "4.50", "30", "Pasta", "12.00", "25", "Gelatina", "4.00", "30", "Caprese", "13.00", "8"}
        };

        // Agregar datos de ejemplo (empezando desde la fila 2)
        for (int rowIndex = 0; rowIndex < datosEjemplo.length; rowIndex++) {
            Row dataRow = sheet.createRow(rowIndex + 2);
            String[] filaDatos = datosEjemplo[rowIndex];
            
            int colIndex = 0;
            for (String dato : filaDatos) {
                if (colIndex < dataRow.getSheet().getRow(0).getLastCellNum()) {
                    dataRow.createCell(colIndex).setCellValue(dato);
                    colIndex++;
                }
            }
        }
    }
} 