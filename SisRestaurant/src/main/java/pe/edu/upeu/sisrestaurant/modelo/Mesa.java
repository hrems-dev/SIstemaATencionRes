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
@Table(name = "mesa")
public class Mesa {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "numero")
    private Integer numero;
    @Column(name = "capacidad")
    private Integer capacidad;
     @Column(name = "estado")
    private String estado;
}
