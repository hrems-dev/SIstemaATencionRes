package pe.edu.upeu.restaurantsistema.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipoDocVenta")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoDocVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;
    
    @Column(name = "estado", length = 1)
    private String estado;
}
