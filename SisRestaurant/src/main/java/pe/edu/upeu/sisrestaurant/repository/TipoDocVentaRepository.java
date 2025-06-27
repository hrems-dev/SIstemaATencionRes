package pe.edu.upeu.sisrestaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.upeu.sisrestaurant.modelo.TipoDocVenta;

import java.util.Optional;

public interface TipoDocVentaRepository extends JpaRepository<TipoDocVenta, Long> {
    Optional<TipoDocVenta> findByNombre(String nombre);
}
