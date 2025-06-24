package pe.edu.upeu.restaurantsistema.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.restaurantsistema.modelo.TipoDocumento;

public interface TipoDocumentoRepositorio extends JpaRepository<TipoDocumento, Long> {
    TipoDocumento findByNombre(String nombre);
}
