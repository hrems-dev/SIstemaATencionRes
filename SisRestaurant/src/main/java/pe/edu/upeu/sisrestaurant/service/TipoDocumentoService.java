package pe.edu.upeu.sisrestaurant.service;

import pe.edu.upeu.sisrestaurant.modelo.TipoDocumento;

public interface TipoDocumentoService {
    TipoDocumento save(TipoDocumento tipoDocumento);
    TipoDocumento getTipoDocumentoById(Long id);
    void deleteTipoDocumentoById(Long id);
}
