package pe.edu.upeu.sisrestaurant.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "id_tipo_documento")
    private Long idTipoDocumento;
    @Column(name = "nro_identidad")
    private String nroIdentidad;
    @Column(name = "nombres")
    private String nombres;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "correo")
    private String correo;
    @Column(name = "fecha_registro")
    private String fechaRegistro;
    @Column(name = "cant_visitas")
    private Integer cantVisitas;
    @Column(name = "estado")
    private Boolean estado;
}
