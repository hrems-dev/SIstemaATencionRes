# 🚀 Compilación de SisRestaurant como Ejecutable (.exe)

Este documento explica cómo compilar la aplicación SisRestaurant como un archivo ejecutable (.exe) para Windows.

## 📋 Requisitos Previos

### 1. **Java Development Kit (JDK) 17 o superior**
- Descargar desde: https://adoptium.net/
- Asegurarse de que JAVA_HOME esté configurado

### 2. **Maven**
- Descargar desde: https://maven.apache.org/download.cgi
- Asegurarse de que Maven esté en el PATH

### 3. **JavaFX SDK** (si no está incluido en el JDK)
- Descargar desde: https://openjfx.io/

## 🔧 Métodos de Compilación

### **Método 1: Usando jpackage (Recomendado)**

jpackage es la herramienta oficial de Oracle para crear ejecutables nativos.

#### Pasos:

1. **Compilar el proyecto:**
   ```bash
   mvn clean package -DskipTests
   ```

2. **Crear el ejecutable con jpackage:**
   ```bash
   jpackage --input target \
     --name "SisRestaurant" \
     --main-jar "SisRestaurant-0.0.1-SNAPSHOT.jar" \
     --main-class "pe.edu.upeu.sisrestaurant.SisRestaurantApplication" \
     --type exe \
     --dest "target\exe" \
     --vendor "UPEU" \
     --app-version "1.0.0" \
     --win-dir-chooser \
     --win-menu \
     --win-shortcut \
     --win-menu-group "SisRestaurant" \
     --java-options "-Dfile.encoding=UTF-8"
   ```

3. **Ejecutar el script automático:**
   ```bash
   build-exe.bat
   ```

### **Método 2: Usando Launch4j**

Launch4j es una herramienta gráfica para convertir JARs en ejecutables.

#### Pasos:

1. **Descargar Launch4j:**
   - URL: https://launch4j.sourceforge.net/
   - Descargar la versión más reciente

2. **Compilar el proyecto:**
   ```bash
   mvn clean package -DskipTests
   ```

3. **Abrir Launch4j y cargar la configuración:**
   - Abrir Launch4j
   - Cargar el archivo `launch4j-config.xml`
   - O configurar manualmente:
     - **Output file:** `target/SisRestaurant.exe`
     - **JAR:** `target/SisRestaurant-0.0.1-SNAPSHOT.jar`
     - **Min JRE version:** `17`
     - **Heap size:** `256 MB` inicial, `1024 MB` máximo

4. **Generar el ejecutable:**
   - Hacer clic en "Build wrapper"

### **Método 3: Usando Maven Plugin**

El proyecto ya incluye configuración en `pom.xml` para crear ejecutables.

#### Pasos:

1. **Compilar con el plugin:**
   ```bash
   mvn clean package jpackage:jpackage
   ```

## 📁 Estructura de Archivos Generados

Después de la compilación exitosa, encontrarás:

```
target/
├── SisRestaurant-0.0.1-SNAPSHOT.jar    # JAR ejecutable
├── exe/
│   └── SisRestaurant-1.0.0.exe         # Ejecutable nativo (jpackage)
└── SisRestaurant.exe                   # Ejecutable (Launch4j)
```

## 🎯 Características del Ejecutable

### **Información del Archivo:**
- **Nombre:** SisRestaurant.exe
- **Versión:** 1.0.0
- **Vendor:** UPEU
- **Descripción:** Sistema de Restaurante - UPEU
- **Copyright:** UPEU 2024

### **Configuración del JRE:**
- **Versión mínima:** Java 17
- **Memoria inicial:** 256 MB
- **Memoria máxima:** 1024 MB
- **Encoding:** UTF-8

### **Características de Windows:**
- ✅ Instalador con selector de directorio
- ✅ Acceso directo en el menú de inicio
- ✅ Acceso directo en el escritorio
- ✅ Grupo de programas "SisRestaurant"

## 🔍 Solución de Problemas

### **Error: "jpackage no se reconoce como comando"**
- Asegúrate de tener JDK 14+ instalado
- jpackage viene incluido con JDK 14 y versiones posteriores

### **Error: "No se puede encontrar el JAR"**
- Ejecuta `mvn clean package` primero
- Verifica que el archivo JAR se haya creado en `target/`

### **Error: "Java no está instalado"**
- Instala Java 17 o superior
- Configura JAVA_HOME correctamente

### **Error: "No se puede iniciar la aplicación"**
- Verifica que todas las dependencias estén incluidas
- Revisa los logs de error en la consola

## 📦 Distribución

### **Para distribuir el ejecutable:**

1. **Con jpackage:** El ejecutable incluye todo lo necesario
2. **Con Launch4j:** El usuario necesita Java instalado

### **Recomendaciones:**
- Usar jpackage para distribución final
- Incluir un README con instrucciones de instalación
- Probar en diferentes versiones de Windows

## 🎉 ¡Listo!

Una vez completado el proceso, tendrás un archivo ejecutable que puedes distribuir a los usuarios finales sin necesidad de que instalen Java por separado (con jpackage) o con Java preinstalado (con Launch4j).

---

**Nota:** El método jpackage es el más moderno y recomendado, ya que crea un instalador nativo que incluye todo lo necesario para ejecutar la aplicación. 