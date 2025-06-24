package pe.edu.upeu.restaurantsistema.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "infoPersonal")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoPersonal {
    @Id
    @Column(name = "dni", length = 20, nullable = false)
    private String dni;
    
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;
    
    @Column(name = "apellido", length = 100, nullable = false)
    private String apellido;
    
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Column(name = "correo", length = 100)
    private String correo;
    
    @Column(name = "direccion", length = 255)
    private String direccion;
    
    @Column(name = "fechaRegistro")
    private LocalDate fechaRegistro;
}
