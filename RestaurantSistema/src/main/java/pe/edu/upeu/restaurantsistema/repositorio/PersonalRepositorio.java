package pe.edu.upeu.restaurantsistema.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.restaurantsistema.modelo.Personal;

@Repository
public interface PersonalRepositorio extends JpaRepository<Personal, Long> {
}
