package pe.edu.upeu.restaurantsistema.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipoDocumento")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoDocumento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "estado", length = 1)
    private String estado;
}
