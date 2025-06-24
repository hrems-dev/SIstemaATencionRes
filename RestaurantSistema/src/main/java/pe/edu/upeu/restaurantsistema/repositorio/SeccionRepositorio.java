package pe.edu.upeu.restaurantsistema.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.restaurantsistema.modelo.Seccion;

public interface SeccionRepositorio extends JpaRepository<Seccion, Long> {
    Seccion findByNombre(String nombre);
}
