package pe.edu.upeu.restaurantsistema.modelo;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoId implements Serializable {
    private Long idPedido;
    private Long idProducto;
} 