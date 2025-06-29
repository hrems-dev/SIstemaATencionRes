@echo off
echo ========================================
echo    COMPILANDO SISRESTAURANT COMO .EXE
echo ========================================

echo.
echo 1. Limpiando proyecto...
call mvn clean

echo.
echo 2. Compilando proyecto...
call mvn compile

echo.
echo 3. Creando JAR ejecutable...
call mvn package -DskipTests

echo.
echo 4. Verificando que el JAR se creo correctamente...
if exist "target\SisRestaurant-0.0.1-SNAPSHOT.jar" (
    echo ✅ JAR creado exitosamente
) else (
    echo ❌ Error: No se pudo crear el JAR
    pause
    exit /b 1
)

echo.
echo 5. Creando ejecutable nativo con jpackage...
jpackage --input target ^
    --name "SisRestaurant" ^
    --main-jar "SisRestaurant-0.0.1-SNAPSHOT.jar" ^
    --main-class "pe.edu.upeu.sisrestaurant.SisRestaurantApplication" ^
    --type exe ^
    --dest "target\exe" ^
    --vendor "UPEU" ^
    --app-version "1.0.0" ^
    --win-dir-chooser ^
    --win-menu ^
    --win-shortcut ^
    --win-menu-group "SisRestaurant" ^
    --java-options "-Dfile.encoding=UTF-8"

echo.
if exist "target\exe\SisRestaurant-1.0.0.exe" (
    echo ✅ Ejecutable creado exitosamente en: target\exe\SisRestaurant-1.0.0.exe
    echo.
    echo 🎉 ¡Compilación completada!
    echo 📁 El ejecutable se encuentra en: target\exe\
) else (
    echo ❌ Error: No se pudo crear el ejecutable
    echo.
    echo 💡 Alternativa: Usar Launch4j para convertir el JAR en .exe
    echo 📥 Descargar Launch4j desde: https://launch4j.sourceforge.net/
)

echo.
pause 