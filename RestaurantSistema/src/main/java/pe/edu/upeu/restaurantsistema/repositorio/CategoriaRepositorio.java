package pe.edu.upeu.restaurantsistema.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.restaurantsistema.modelo.Categoria;

public interface CategoriaRepositorio extends JpaRepository<Categoria, Long> {
    Categoria findByNombre(String nombre);
}
