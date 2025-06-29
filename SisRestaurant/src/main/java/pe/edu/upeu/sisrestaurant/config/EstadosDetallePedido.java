package pe.edu.upeu.sisrestaurant.config;

/**
 * Clase que define las constantes para los estados del detalle de pedido
 */
public class EstadosDetallePedido {
    
    public static final String AGREGADO = "AGREGADO";  // Estado inicial cuando se agrega al pedido
    public static final String PENDIENTE = "PENDIENTE";
    public static final String EN_PROCESO = "EN_PROCESO";
    public static final String CANCELADO = "CANCELADO";
    
    /**
     * Verifica si un estado es válido
     * @param estado El estado a verificar
     * @return true si el estado es válido, false en caso contrario
     */
    public static boolean esEstadoValido(String estado) {
        return AGREGADO.equals(estado) ||
               PENDIENTE.equals(estado) || 
               EN_PROCESO.equals(estado) || 
               CANCELADO.equals(estado);
    }
    
    /**
     * Obtiene todos los estados disponibles como array
     * @return Array con todos los estados disponibles
     */
    public static String[] getEstadosDisponibles() {
        return new String[]{AGREGADO, PENDIENTE, EN_PROCESO, CANCELADO};
    }
} 