package pe.edu.upeu.sisrestaurant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.upeu.sisrestaurant.modelo.TipoDocumento;
import pe.edu.upeu.sisrestaurant.repository.TipoDocumentoRepository;
import pe.edu.upeu.sisrestaurant.service.TipoDocumentoService;

@Service
public class TipoDocumentoServiceImpl implements TipoDocumentoService {

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Override
    public TipoDocumento save(TipoDocumento tipoDocumento) {
        return tipoDocumentoRepository.save(tipoDocumento);
    }

    @Override
    public TipoDocumento getTipoDocumentoById(Long id) {
        return tipoDocumentoRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteTipoDocumentoById(Long id) {
        tipoDocumentoRepository.deleteById(id);
    }
}
