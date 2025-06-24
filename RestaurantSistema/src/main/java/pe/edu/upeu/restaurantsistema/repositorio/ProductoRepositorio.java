package pe.edu.upeu.restaurantsistema.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.restaurantsistema.modelo.Producto;
import java.util.List;

public interface ProductoRepositorio extends JpaRepository<Producto, Long> {
    Producto findByNombre(String nombre);
    List<Producto> findByCategoriaId(Long categoriaId);
    List<Producto> findBySeccionId(Long seccionId);
}
