package pe.edu.upeu.sisrestaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.upeu.sisrestaurant.modelo.Seccion;

import java.util.Optional;

public interface SeccionRepository extends JpaRepository<Seccion, Long> {
    Optional<Seccion> findByNombre(String nombre);
}
