package pe.edu.upeu.restaurantsistema.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;

@Entity
@Table(name = "docVenta")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "codigoVenta", length = 50, nullable = false)
    private String codigoVenta;
    
    @ManyToOne
    @JoinColumn(name = "tipoDocVenta")
    private TipoDocVenta tipoDocVenta;
    
    @Column(name = "fechaHora")
    private Date fechaHora;
    
    @Column(name = "estado", length = 1)
    private String estado;
}
