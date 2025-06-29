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
@Table(name = "doc_venta")
public class DocVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "codigo_venta")
    private String codigoVenta;
    @Column(name = "tipo_doc_venta")
    private String tipoDocVenta;
    @Column(name = "fecha_hora")
    private String fechaHora;
    @Column(name = "estado")
    private String estado;
}
