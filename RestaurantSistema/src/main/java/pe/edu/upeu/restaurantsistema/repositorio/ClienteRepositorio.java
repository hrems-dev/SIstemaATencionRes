package pe.edu.upeu.restaurantsistema.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.restaurantsistema.modelo.Cliente;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {
    Cliente findByNroIdentidad(String nroIdentidad);
    Cliente findByCorreo(String correo);
}
