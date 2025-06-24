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
@Table(name = "personal")
public class Personal {
    @Id
    @Column(name = "id_personal")
    private Long idPersonal;
    @Column(name = "rol")
    private String rol;
    @Column(name = "usuario")
    private String usuario;
}
