package pe.edu.upeu.sisrestaurant.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "detalle_pedido")
public class DetallePedido {
    @Id
    @Column(name = "id_pedido")
    private Long idPedido;
    @Column(name = "id_producto")
    private Long idProducto;
    @Column(name = "cantidad")
    private Integer cantidad;
    @Column(name = "precio")
    private Double precio;
    @Column(name = "fecha_registro")
    private String fechaRegistro;
    @Column(name = "estado")
    private String estado;
}
