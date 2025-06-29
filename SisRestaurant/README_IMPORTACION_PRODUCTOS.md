# Importación de Productos desde Excel

## Descripción
Se ha agregado la funcionalidad de importación de productos desde archivos Excel al formulario de productos, similar a la funcionalidad existente en el formulario de administración.

## Funcionalidades Agregadas

### 1. Botón "Importar Excel"
- Permite seleccionar un archivo Excel (.xlsx) para importar productos
- Busca automáticamente en la carpeta `src/main/resources/doc`
- Procesa todas las hojas del archivo (cada hoja representa una sección)

### 2. Botón "Plantilla"
- Genera automáticamente una plantilla de Excel con el formato correcto
- Se guarda en `src/main/resources/doc/plantilla_productos.xlsx`
- Crea una hoja por cada sección existente en la base de datos

## Formato del Archivo Excel

### Estructura por Hoja (Sección)
Cada hoja del archivo Excel representa una **sección**. El nombre de la hoja será el nombre de la sección.

### Estructura de Categorías (Horizontal)
Dentro de cada hoja, las categorías se organizan horizontalmente en grupos de 3 columnas:

| BEBIDAS                |        |        | COMIDAS                |        |        |
|------------------------|--------|--------|------------------------|--------|--------|
| Nombre     | Precio   | Stock  | Nombre | Precio   | Stock  |
| Coca Cola  | 5.50     | 100    | Hamburguesa | 15.00 | 50   |
| Cerveza    | 8.00     | 75     | Pizza       | 20.00 | 30   |

### Estructura de Filas
- **Fila 0**: Nombres de las categorías (BEBIDAS, COMIDAS, etc.)
- **Fila 1**: Encabezados de columnas (Nombre, Precio, Stock)
- **Fila 2 en adelante**: Datos de los productos

### Ejemplo de Múltiples Hojas
```
Hoja "Terraza":
- Categorías: Bebidas, Comidas, Postres
- Productos específicos de la terraza

Hoja "Bar":
- Categorías: Bebidas, Snacks
- Productos específicos del bar

Hoja "Sala Principal":
- Categorías: Comidas, Bebidas, Postres
- Productos específicos de la sala principal
```

## Características de la Importación

### Validaciones Automáticas
- **Campos obligatorios**: Nombre y Precio
- **Detección de duplicados**: No se importan productos con nombres duplicados
- **Creación automática**: Si la sección o categoría no existe, se crea automáticamente

### Manejo de Errores
- **Productos duplicados**: Se omiten y se reporta la cantidad
- **Datos inválidos**: Se omiten las filas con datos incorrectos
- **Reporte final**: Muestra la cantidad de productos importados, omitidos, secciones y categorías creadas

### Configuración Automática
- **Estado**: Todos los productos se crean con estado "activo"
- **Stock**: Si no se especifica, se establece en 0
- **Categorías**: Se crean con estado "Activo"
- **Secciones**: Se crean con estado "A"

## Pasos para Usar la Importación

### 1. Generar Plantilla
1. Abrir el formulario de productos
2. Hacer clic en el botón "Plantilla"
3. Se generará automáticamente `plantilla_productos.xlsx` con hojas por cada sección existente

### 2. Llenar la Plantilla
1. Abrir el archivo generado
2. Cada hoja representa una sección
3. En cada hoja, las categorías están organizadas horizontalmente
4. Llenar los datos de productos en las filas correspondientes
5. Guardar el archivo

### 3. Importar Productos
1. En el formulario de productos, hacer clic en "Importar Excel"
2. Seleccionar el archivo Excel con los datos
3. Confirmar la importación
4. Revisar el reporte de resultados

## Archivos Modificados

### Controlador
- `ProductoController.java`: Agregados métodos `importarExcel()`, `generarPlantillaExcel()`, `importarProductosDeExcel()`, `procesarCategoriasEnHoja()`, `procesarProductosDeCategoria()`

### Interfaz
- `ProductoForm.fxml`: Agregados botones "Importar Excel" y "Plantilla"

### Servicios
- `ProductoService.java`: Agregado método `findByNombre()`
- `ProductoServiceImpl.java`: Implementación del método `findByNombre()`
- `ProductoRepository.java`: Agregado método `findByNombre()`

## Dependencias Utilizadas
- Apache POI para manejo de archivos Excel
- JavaFX FileChooser para selección de archivos
- Spring Data JPA para consultas de base de datos
- AtomicInteger para contadores thread-safe

## Notas Importantes
- La importación es incremental (no sobrescribe productos existentes)
- Se recomienda hacer una copia de seguridad antes de importar grandes cantidades
- El archivo Excel debe estar cerrado durante la importación
- Los nombres de productos son únicos (no se permiten duplicados)
- Cada hoja del Excel se convierte en una sección automáticamente
- Las categorías se crean automáticamente si no existen
- El formato horizontal permite organizar mejor los productos por categoría 