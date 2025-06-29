package pe.edu.upeu.sisrestaurant.config;

import pe.edu.upeu.sisrestaurant.modelo.Seccion;

public class ConstantesGlobales {
    public static final String TIPO_DOCUMENTO_DEFAULT = "DNI";
    // ID del tipo de documento DNI que ya existe en la base de datos
    public static final Long TIPO_DOCUMENTO_DEFAULT_ID = 1L;
    
    // Variable global para la sección seleccionada
    private static Seccion seccionSeleccionada = null;
    
    public static Seccion getSeccionSeleccionada() {
        return seccionSeleccionada;
    }
    
    public static void setSeccionSeleccionada(Seccion seccion) {
        seccionSeleccionada = seccion;
    }
} 