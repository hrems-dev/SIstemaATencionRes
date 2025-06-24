package pe.edu.upeu.restaurantsistema.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "personal")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Personal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPersonal;
    
    @ManyToOne
    @JoinColumn(name = "idRol")
    private Rol rol;
    
    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;
}
