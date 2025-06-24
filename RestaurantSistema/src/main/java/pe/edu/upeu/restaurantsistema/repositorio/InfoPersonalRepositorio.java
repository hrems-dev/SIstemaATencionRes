package pe.edu.upeu.restaurantsistema.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.restaurantsistema.modelo.InfoPersonal;

@Repository
public interface InfoPersonalRepositorio extends JpaRepository<InfoPersonal, String> {
}
