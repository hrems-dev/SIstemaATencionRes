package pe.edu.upeu.restaurantsistema.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cliente")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "idTipoDocumento")
    private TipoDocumento tipoDocumento;
    
    @Column(name = "nroIdentidad", length = 20, nullable = false)
    private String nroIdentidad;
    
    @Column(name = "nombres", length = 100, nullable = false)
    private String nombres;
    
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Column(name = "correo", length = 100)
    private String correo;
    
    @Column(name = "fechaRegistro")
    private LocalDate fechaRegistro;
    
    @Column(name = "cantVisitas")
    private Integer cantVisitas;
    
    @Column(name = "estado", length = 1)
    private String estado;
}
