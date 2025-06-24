package pe.edu.upeu.restaurantsistema.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.restaurantsistema.modelo.TipoDocVenta;

public interface TipoDocVentaRepositorio extends JpaRepository<TipoDocVenta, Long> {
    TipoDocVenta findByNombre(String nombre);
}
