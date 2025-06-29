package pe.edu.upeu.sisrestaurant.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "id_mesa")
    private Long idMesa;
    @Column(name = "id_cliente")
    private Long idCliente;
    @Column(name = "fecha_hora")
    private String fechaHora;
    @Column(name = "estado")
    private String estado;
    @Column(name = "id_personal")
    private Long idPersonal;
    @Column(name = "id_doc_venta")
    private Long idDocVenta;
}
