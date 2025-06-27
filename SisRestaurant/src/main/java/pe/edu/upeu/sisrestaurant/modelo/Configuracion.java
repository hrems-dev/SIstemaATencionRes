package pe.edu.upeu.sisrestaurant.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "configuracion")
public class Configuracion {
    @Id
    @Column(name = "id_config")
    private Long idConfig;

    @Column(name = "nombre_restaurante")
    private String nombreRestaurante;

    @Column(name = "ruc")
    private String ruc;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "correo")
    private String correo;

    @Column(name = "propietario")
    private String propietario;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "codlicencia")
    private String codLicencia;

    // Relación con Usuario (Muchos Configuracion pueden estar asociados a un Usuario)
    @ManyToOne
    @JoinColumn(name = "id_user")
    private Usuario usuario;
} 