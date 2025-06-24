package pe.edu.upeu.restaurantsistema.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detallePedido")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedido {
    @EmbeddedId
    private DetallePedidoId id;
    
    @Column(name = "cantidad")
    private Integer cantidad;
    
    @Column(name = "precio")
    private Double precio;
    
    @Column(name = "fechaRegistro")
    private LocalDate fechaRegistro;
    
    @Column(name = "estado", length = 1)
    private String estado;
}
