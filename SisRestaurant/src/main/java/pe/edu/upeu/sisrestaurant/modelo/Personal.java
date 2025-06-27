package pe.edu.upeu.sisrestaurant.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_personal")
    private Long idPersonal;
    
    // Relación con Rol (Muchos Personal pueden tener un Rol)
    @ManyToOne
    @JoinColumn(name = "id_rol")
    private Rol rol;
    
    // Relación con Usuario (Uno a Uno)
    @OneToOne
    @JoinColumn(name = "id_user")
    private Usuario usuario;
    
    // Relación con InfoPersonal (Uno a Uno, usando idinfo_personal como foráneo)
    @OneToOne
    @JoinColumn(name = "idinfo_personal")
    private InfoPersonal infoPersonal;
}
