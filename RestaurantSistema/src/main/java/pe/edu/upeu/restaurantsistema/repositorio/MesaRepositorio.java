package pe.edu.upeu.restaurantsistema.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.restaurantsistema.modelo.Mesa;
import java.util.List;

public interface MesaRepositorio extends JpaRepository<Mesa, Long> {
    Mesa findByNumero(Integer numero);
    List<Mesa> findByEstado(String estado);
}
