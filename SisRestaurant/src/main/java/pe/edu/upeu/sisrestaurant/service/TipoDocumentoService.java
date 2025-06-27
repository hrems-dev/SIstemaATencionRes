package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.TipoDocumento;
import java.util.List;
import java.util.Optional;

public interface TipoDocumentoService {
    TipoDocumento save(TipoDocumento tipoDocumento);
    TipoDocumento getTipoDocumentoById(Long id);
    void deleteTipoDocumentoById(Long id);
    List<TipoDocumento> list();
    TipoDocumento getTipoDocumentoByNombre(String nombre);
    Optional<TipoDocumento> findByNombre(String nombre);
}
