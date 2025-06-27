package pe.edu.upeu.sisrestaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.upeu.sisrestaurant.modelo.TipoDocumento;

import java.util.Optional;

public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Long> {
    Optional<TipoDocumento> findByNombre(String nombre);
}
